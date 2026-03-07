/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.DoubleBuffer;
import java.util.Optional;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.technical.algorithm.AutoTuningManager;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.nativ.technical.backend.nativ.NativeLibraryLoader;
import com.google.auto.service.AutoService;

/**
 * High-performance Dense CUDA Backend using Project Panama (FFM).
 * Binds directly to cuBLAS for matrix operations.
 * Implements {@link LinearAlgebraProvider} (Dense).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService({Backend.class, ComputeBackend.class, GPUBackend.class, NativeBackend.class, LinearAlgebraProvider.class})
public class NativeCUDADenseLinearAlgebraBackend implements NativeBackend, LinearAlgebraProvider<Real>, GPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(NativeCUDADenseLinearAlgebraBackend.class);
    private static boolean IS_AVAILABLE = false;
    private static final Linker LINKER = NativeLibraryLoader.getLinker();

    // CUDA/cuBLAS Handles
    private static MethodHandle CUBLAS_CREATE;
    private static MethodHandle CUBLAS_DESTROY;
    private static MethodHandle CUBLAS_DGEMM;
    private static MethodHandle CUBLAS_DGEAM;
    private static MethodHandle CUDA_MALLOC;
    private static MethodHandle CUDA_FREE;
    private static MethodHandle CUDA_MEMCPY;
    private static MethodHandle CUDA_DEVICE_SYNCHRONIZE;
    private static MethodHandle CUDA_GET_ERROR_STRING;

    // cuSolver Handles
    private static MethodHandle CUSOLVER_CREATE;
    private static MethodHandle CUSOLVER_DESTROY;
    private static MethodHandle CUSOLVER_DGETRF_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_DGETRF;
    private static MethodHandle CUSOLVER_DGETRS;

    // Constants
    private static final int CUBLAS_OP_N = 0;
    private static final int CUDA_MEMCPY_HOST_TO_DEVICE = 1;
    private static final int CUDA_MEMCPY_DEVICE_TO_HOST = 2;

    private static synchronized void ensureInitialized() {
        if (IS_AVAILABLE) return;

        SymbolLookup cublas = null;
        SymbolLookup cusolver = null;

        try { // Test loading
            Optional<SymbolLookup> cudaRtOpt = NativeLibraryLoader.loadLibrary("cudart", Arena.global());
            Optional<SymbolLookup> cublasOpt = NativeLibraryLoader.loadLibrary("cublas", Arena.global());
            
            if (cudaRtOpt.isEmpty() || cublasOpt.isEmpty()) {
                logger.warn("Native CUDA/cuBLAS libraries not found (cudart={}, cublas={})", cudaRtOpt.isPresent(), cublasOpt.isPresent());
                return;
            }
            
            SymbolLookup cudart = cudaRtOpt.get();
            cublas = cublasOpt.get();

            // CUDA Runtime Memory Management
            Optional<MemorySegment> mallocSym = NativeLibraryLoader.findSymbol(cudart, "cudaMalloc");
            Optional<MemorySegment> freeSym = NativeLibraryLoader.findSymbol(cudart, "cudaFree");
            Optional<MemorySegment> memcpySym = NativeLibraryLoader.findSymbol(cudart, "cudaMemcpy");
            Optional<MemorySegment> syncSym = NativeLibraryLoader.findSymbol(cudart, "cudaDeviceSynchronize");
            Optional<MemorySegment> getErrSym = NativeLibraryLoader.findSymbol(cudart, "cudaGetErrorString");

            if (mallocSym.isEmpty() || freeSym.isEmpty() || memcpySym.isEmpty() || syncSym.isEmpty() || getErrSym.isEmpty()) {
                logger.warn("Required CUDA runtime symbols missing (malloc={}, free={}, memcpy={}, sync={}, err={}). Backend disabled.", 
                    mallocSym.isPresent(), freeSym.isPresent(), memcpySym.isPresent(), syncSym.isPresent(), getErrSym.isPresent());
                return;
            }

            CUDA_MALLOC = LINKER.downcallHandle(mallocSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            CUDA_FREE = LINKER.downcallHandle(freeSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUDA_MEMCPY = LINKER.downcallHandle(memcpySym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
            CUDA_DEVICE_SYNCHRONIZE = LINKER.downcallHandle(syncSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT));
            CUDA_GET_ERROR_STRING = LINKER.downcallHandle(getErrSym.get(), FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

            // cuBLAS
            Optional<MemorySegment> createSym = NativeLibraryLoader.findSymbol(cublas, "cublasCreate_v2", "cublasCreate");
            Optional<MemorySegment> destroySym = NativeLibraryLoader.findSymbol(cublas, "cublasDestroy_v2", "cublasDestroy");
            Optional<MemorySegment> dgemmSym = NativeLibraryLoader.findSymbol(cublas, "cublasDgemm_v2", "cublasDgemm");
            Optional<MemorySegment> dgeamSym = NativeLibraryLoader.findSymbol(cublas, "cublasDgeam_v2", "cublasDgeam");

            if (createSym.isEmpty() || destroySym.isEmpty() || dgemmSym.isEmpty() || dgeamSym.isEmpty()) {
                logger.warn("Required cuBLAS symbols missing (create={}, destroy={}, dgemm={}, dgeam={}). Backend disabled.", 
                    createSym.isPresent(), destroySym.isPresent(), dgemmSym.isPresent(), dgeamSym.isPresent());
                return;
            }

            CUBLAS_CREATE = LINKER.downcallHandle(createSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUBLAS_DESTROY = LINKER.downcallHandle(destroySym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUBLAS_DGEMM = LINKER.downcallHandle(dgemmSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT
            ));
            
            CUBLAS_DGEAM = LINKER.downcallHandle(dgeamSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT
            ));

            // cuSolver
            Optional<SymbolLookup> cusolverOpt = NativeLibraryLoader.loadLibrary("cusolver", Arena.global());
            if (cusolverOpt.isPresent()) {
                cusolver = cusolverOpt.get();
                Optional<MemorySegment> createSolverSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnCreate");
                Optional<MemorySegment> destroySolverSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDestroy");
                Optional<MemorySegment> getrfBufferSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDgetrf_bufferSize");
                Optional<MemorySegment> getrfSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDgetrf");
                Optional<MemorySegment> getrsSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDgetrs");

                if (createSolverSym.isPresent() && destroySolverSym.isPresent() && getrfBufferSym.isPresent() && 
                    getrfSym.isPresent() && getrsSym.isPresent()) {
                    
                    CUSOLVER_CREATE = LINKER.downcallHandle(createSolverSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                    CUSOLVER_DESTROY = LINKER.downcallHandle(destroySolverSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                    CUSOLVER_DGETRF_BUFFER_SIZE = LINKER.downcallHandle(getrfBufferSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                        ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                    CUSOLVER_DGETRF = LINKER.downcallHandle(getrfSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                        ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
                    CUSOLVER_DGETRS = LINKER.downcallHandle(getrsSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                        ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                    
                    logger.info("Native cuSolver functionality linked successfully.");
                } else {
                    logger.warn("Some cuSolver symbols missing. Solver operations will be disabled.");
                }
            } else {
                logger.warn("cuSolver library not found. Solver operations will be disabled.");
            }

            IS_AVAILABLE = true;
            logger.info("Native CUDA/cuBLAS Backend initialized successfully.");
        } catch (Throwable t) {
            logger.warn("Failed to initialize CUDA/cuBLAS Backend: {} - {}", t.getClass().getSimpleName(), t.getMessage());
            logger.debug("Stack trace:", t);
        }
    }

    @Override
    public boolean isAvailable() { ensureInitialized(); return IS_AVAILABLE; }

    @Override
    public boolean isLoaded() { return IS_AVAILABLE; }

    @Override
    public String getEnvironmentInfo() {
        return IS_AVAILABLE ? "GPU (CUDA)" : "N/A";
    }

    @Override
    public String getName() { return "Native CUDA Dense Backend"; }

    @Override
    public int getPriority() { return 110; } // Higher than Native SIMD (90) and Native BLAS (100)

    @Override
    public boolean isCompatible(Ring<?> ring) { return ring instanceof Reals; }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        ensureInitialized();
        int m = a.rows();
        int n = a.cols();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment d_C = arena.allocate(ValueLayout.ADDRESS);
            
            checkCuda(CUDA_MALLOC.invokeExact(d_A, (long)m * n * Double.BYTES));
            checkCuda(CUDA_MALLOC.invokeExact(d_C, (long)m * n * Double.BYTES));
            
            try {
                checkCuda(CUDA_MEMCPY.invokeExact(d_A.get(ValueLayout.ADDRESS, 0), h_A, (long)m * n * Double.BYTES, 1));
                
                MemorySegment handle = arena.allocate(ValueLayout.ADDRESS);
                checkCublas(CUBLAS_CREATE.invokeExact(handle));
                
                try {
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
                    MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
                    
                    // cublasDgeam(handle, transa, transb, m, n, alpha, A, lda, beta, B, ldb, C, ldc)
                    // We use CUBLAS_OP_T for transa to perform C = alpha * A^T + beta * B
                    // Here B is null/not used as beta is 0.0
                    checkCublas(CUBLAS_DGEAM.invokeExact(handle.get(ValueLayout.ADDRESS, 0), 
                        1, 0, n, m, alpha, d_A.get(ValueLayout.ADDRESS, 0), m, beta, MemorySegment.NULL, m, d_C.get(ValueLayout.ADDRESS, 0), n));
                    
                    double[] resultData = new double[m * n];
                    checkCuda(CUDA_MEMCPY.invokeExact(MemorySegment.ofArray(resultData), d_C.get(ValueLayout.ADDRESS, 0), (long)m * n * Double.BYTES, 2));
                    
                    return fromDoubleArray(resultData, n, m);
                } finally {
                    checkCublas(CUBLAS_DESTROY.invokeExact(handle.get(ValueLayout.ADDRESS, 0)));
                }
            } finally {
                checkCuda(CUDA_FREE.invokeExact(d_A.get(ValueLayout.ADDRESS, 0)));
                checkCuda(CUDA_FREE.invokeExact(d_C.get(ValueLayout.ADDRESS, 0)));
            }
        } catch (Throwable t) {
            logger.error("CUDA Transpose failed, falling back", t);
            return fallback().transpose(a);
        }
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA not available");

        logger.debug("Entering CUDA multiply: [{}x{}] * [{}x{}]", a.rows(), a.cols(), b.rows(), b.cols());
        long start = System.nanoTime();
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        if (k != b.rows()) throw new IllegalArgumentException("Dimension mismatch");

        try (Arena arena = Arena.ofConfined()) {
            // Allocate Host Buffers
            double[] h_A = toDoubleArray(a);
            double[] h_B = toDoubleArray(b);

            // Allocate Device Pointers
            MemorySegment p_A = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment p_B = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment p_C = arena.allocate(ValueLayout.ADDRESS);

            checkCuda(CUDA_MALLOC.invokeExact(p_A, (long) m * k * 8));
            checkCuda(CUDA_MALLOC.invokeExact(p_B, (long) k * n * 8));
            checkCuda(CUDA_MALLOC.invokeExact(p_C, (long) m * n * 8));

            MemorySegment d_A = p_A.get(ValueLayout.ADDRESS, 0);
            MemorySegment d_B = p_B.get(ValueLayout.ADDRESS, 0);
            MemorySegment d_C = p_C.get(ValueLayout.ADDRESS, 0);

            // Copy to Device
            MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_A);
            MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_B);

            checkCuda(CUDA_MEMCPY.invokeExact(d_A, segA, (long) m * k * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda(CUDA_MEMCPY.invokeExact(d_B, segB, (long) k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

            // cuBLAS Handle
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCublas(CUBLAS_CREATE.invokeExact(p_Handle));
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            // cuBLAS DGEMM Setup (Alpha, Beta)
            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);

            // Call DGEMM
            // Note: cuBLAS is Column Major by default. 
            // To compute C = A * B in Row Major, we can compute C_T = B_T * A_T.
            // Or just use cuBLAS's column-major logic with swapped arguments.
            checkCublas(CUBLAS_DGEMM.invokeExact(handle, CUBLAS_OP_N, CUBLAS_OP_N, 
                n, m, k, alpha, d_B, n, d_A, k, beta, d_C, n));

            // Copy back
            double[] h_C = new double[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda(CUDA_MEMCPY.invokeExact(segC, d_C, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, h_C, 0, m * n);

            // Cleanup
            CUBLAS_DESTROY.invokeExact(handle);
            CUDA_FREE.invokeExact(d_A);
            CUDA_FREE.invokeExact(d_B);
            CUDA_FREE.invokeExact(d_C);
            
            Matrix<Real> result = fromDoubleArray(h_C, m, n);
            org.episteme.core.util.PerformanceLogger.log("MatrixMultiply", "Dense/CUDA", System.nanoTime() - start);
            return result;

        } catch (Throwable t) {
            throw new RuntimeException("CUDA GPU execution error", t);
        }
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (!IS_AVAILABLE || CUSOLVER_DGETRS == null) return LinearAlgebraProvider.super.solve(a, b);

        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square for solve");
        if (n != b.dimension()) throw new IllegalArgumentException("Vector dimension mismatch");

        try (Arena arena = Arena.ofConfined()) {
            // cuSolver Handle
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCuda(CUSOLVER_CREATE.invokeExact(p_Handle));
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            try {
                // Allocate GPU Memory
                MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_B = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Ipiv = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Info = arena.allocate(ValueLayout.ADDRESS);

                checkCuda(CUDA_MALLOC.invokeExact(d_A, (long) n * n * 8));
                checkCuda(CUDA_MALLOC.invokeExact(d_B, (long) n * 8));
                checkCuda(CUDA_MALLOC.invokeExact(d_Ipiv, (long) n * 4));
                checkCuda(CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                MemorySegment segA = d_A.get(ValueLayout.ADDRESS, 0);
                MemorySegment segB = d_B.get(ValueLayout.ADDRESS, 0);
                MemorySegment segIpiv = d_Ipiv.get(ValueLayout.ADDRESS, 0);
                MemorySegment segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                // Copy to GPU (Row-major to Col-major conversion needed for cuSolver if we don't transpose)
                // However, our toDoubleArray is row-major. cuSolver expects col-major.
                // We can use cuBLAS to transpose or just transpose in Java.
                double[] h_A = toDoubleArray(a); // Row-major
                double[] h_B = toDoubleVec(b);

                // For simplicity, we transpose in Java for now.
                double[] h_At = new double[n * n];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];

                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
                MemorySegment hostB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_B);

                checkCuda(CUDA_MEMCPY.invokeExact(segA, hostA, (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda(CUDA_MEMCPY.invokeExact(segB, hostB, (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                // 1. LU Factorization (GETRF)
                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda(CUSOLVER_DGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda(CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 8));
                MemorySegment segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda(CUSOLVER_DGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
                
                // 2. Solve (GETRS)
                checkCuda(CUSOLVER_DGETRS.invokeExact(handle, CUBLAS_OP_N, n, 1, segA, n, segIpiv, segB, n, segInfo));

                // Copy result back
                double[] h_X = new double[n];
                MemorySegment hostX = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
                checkCuda(CUDA_MEMCPY.invokeExact(hostX, segB, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostX, ValueLayout.JAVA_DOUBLE, 0, h_X, 0, n);

                // Cleanup GPU mem
                CUDA_FREE.invokeExact(segA);
                CUDA_FREE.invokeExact(segB);
                CUDA_FREE.invokeExact(segIpiv);
                CUDA_FREE.invokeExact(segInfo);
                CUDA_FREE.invokeExact(segWork);

                return fromDoubleVec(h_X);
            } finally {
                CUSOLVER_DESTROY.invokeExact(handle);
            }
        } catch (Throwable t) {
            logger.error("CUDA solve failed: {}", t.getMessage());
            return LinearAlgebraProvider.super.solve(a, b);
        }
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (!IS_AVAILABLE || CUSOLVER_DGETRS == null) return LinearAlgebraProvider.super.inverse(a);

        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square for inverse");

        // Identity matrix as B
        double[] identity = new double[n * n];
        for (int i = 0; i < n; i++) identity[i * n + i] = 1.0;

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCuda(CUSOLVER_CREATE.invokeExact(p_Handle));
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            try {
                // Allocate GPU Memory
                MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_B = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Ipiv = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Info = arena.allocate(ValueLayout.ADDRESS);

                checkCuda(CUDA_MALLOC.invokeExact(d_A, (long) n * n * 8));
                checkCuda(CUDA_MALLOC.invokeExact(d_B, (long) n * n * 8));
                checkCuda(CUDA_MALLOC.invokeExact(d_Ipiv, (long) n * 4));
                checkCuda(CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                MemorySegment segA = d_A.get(ValueLayout.ADDRESS, 0);
                MemorySegment segB = d_B.get(ValueLayout.ADDRESS, 0);
                MemorySegment segIpiv = d_Ipiv.get(ValueLayout.ADDRESS, 0);
                MemorySegment segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toDoubleArray(a);
                double[] h_At = new double[n * n];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];

                // Identity is also transposed (which is still identity) for col-major
                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
                MemorySegment hostB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, identity);

                checkCuda(CUDA_MEMCPY.invokeExact(segA, hostA, (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda(CUDA_MEMCPY.invokeExact(segB, hostB, (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                // 1. LU Factorization
                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda(CUSOLVER_DGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda(CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 8));
                MemorySegment segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda(CUSOLVER_DGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
                
                // 2. Solve for each column of identity (nrhs = n)
                checkCuda(CUSOLVER_DGETRS.invokeExact(handle, CUBLAS_OP_N, n, n, segA, n, segIpiv, segB, n, segInfo));

                // Copy result back (segB now contains the inverse in column-major? 
                // Wait, if B was identity (col-major), segB will be inverse(A) in col-major.
                // We want row-major back.
                double[] h_InvT = new double[n * n];
                MemorySegment hostInv = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
                checkCuda(CUDA_MEMCPY.invokeExact(hostInv, segB, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostInv, ValueLayout.JAVA_DOUBLE, 0, h_InvT, 0, n * n);

                // Transpose back to row-major
                double[] h_Inv = new double[n * n];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_Inv[r * n + c] = h_InvT[c * n + r];

                CUDA_FREE.invokeExact(segA);
                CUDA_FREE.invokeExact(segB);
                CUDA_FREE.invokeExact(segIpiv);
                CUDA_FREE.invokeExact(segInfo);
                CUDA_FREE.invokeExact(segWork);

                return fromDoubleArray(h_Inv, n, n);
            } finally {
                CUSOLVER_DESTROY.invokeExact(handle);
            }
        } catch (Throwable t) {
            logger.error("CUDA inverse failed: {}", t.getMessage());
            return LinearAlgebraProvider.super.inverse(a);
        }
    }

    @Override
    public Real determinant(Matrix<Real> a) {
        if (!IS_AVAILABLE || CUSOLVER_DGETRS == null) return LinearAlgebraProvider.super.determinant(a);

        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square for determinant");

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCuda(CUSOLVER_CREATE.invokeExact(p_Handle));
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            try {
                MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Ipiv = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Info = arena.allocate(ValueLayout.ADDRESS);

                checkCuda(CUDA_MALLOC.invokeExact(d_A, (long) n * n * 8));
                checkCuda(CUDA_MALLOC.invokeExact(d_Ipiv, (long) n * 4));
                checkCuda(CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                MemorySegment segA = d_A.get(ValueLayout.ADDRESS, 0);
                MemorySegment segIpiv = d_Ipiv.get(ValueLayout.ADDRESS, 0);
                MemorySegment segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toDoubleArray(a);
                double[] h_At = new double[n * n];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];

                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
                checkCuda(CUDA_MEMCPY.invokeExact(segA, hostA, (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda(CUSOLVER_DGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda(CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 8));
                MemorySegment segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda(CUSOLVER_DGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));

                // Copy result back (segA now contains LU)
                double[] h_LU = new double[n * n];
                MemorySegment hostLU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
                checkCuda(CUDA_MEMCPY.invokeExact(hostLU, segA, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostLU, ValueLayout.JAVA_DOUBLE, 0, h_LU, 0, n * n);

                int[] h_Ipiv = new int[n];
                MemorySegment hostIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                checkCuda(CUDA_MEMCPY.invokeExact(hostIpiv, segIpiv, (long) n * 4, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostIpiv, ValueLayout.JAVA_INT, 0, h_Ipiv, 0, n);

                double det = 1.0;
                for (int i = 0; i < n; i++) {
                    det *= h_LU[i * n + i]; // Diagonal of U (h_LU is col-major, so [i*n+i] is still diagonal)
                    if (h_Ipiv[i] != i + 1) det = -det;
                }

                CUDA_FREE.invokeExact(segA);
                CUDA_FREE.invokeExact(segIpiv);
                CUDA_FREE.invokeExact(segInfo);
                CUDA_FREE.invokeExact(segWork);

                return Real.of(det);
            } finally {
                CUSOLVER_DESTROY.invokeExact(handle);
            }
        } catch (Throwable t) {
            logger.error("CUDA determinant failed: {}", t.getMessage());
            return LinearAlgebraProvider.super.determinant(a);
        }
    }

    private void checkCuda(Object result) {
        int code = (int) result;
        if (code != 0) throw new RuntimeException("CUDA Error: " + code);
    }

    private void checkCublas(Object result) {
        int code = (int) result;
        if (code != 0) throw new RuntimeException("cuBLAS Error: " + code);
    }

    private double[] toDoubleArray(Matrix<Real> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols];
        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                data[i*cols + j] = m.get(i, j).doubleValue();
            }
        }
        return data;
    }

    private Matrix<Real> fromDoubleArray(double[] data, int rows, int cols) {
        Real[] reals = new Real[data.length];
        for(int i=0; i<data.length; i++) reals[i] = Real.of(data[i]);
        return new DenseMatrix<Real>(reals, rows, cols, Reals.getInstance());
    }

    private double[] toDoubleVec(Vector<Real> v) {
        double[] d = new double[v.dimension()];
        for (int i = 0; i < d.length; i++) d[i] = v.get(i).doubleValue();
        return d;
    }

    private Vector<Real> fromDoubleVec(double[] d) {
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(d);
    }

    @Override public String getNativeLibraryName() { return "cuda"; }
    @Override public DeviceInfo[] getDevices() { return new DeviceInfo[0]; }
    @Override public void selectDevice(int deviceId) { }
    @Override public long allocateGPUMemory(long size) { return 0; }
    @Override public void copyToGPU(long handle, DoubleBuffer buffer, long count) { }
    @Override public void copyFromGPU(long handle, DoubleBuffer buffer, long count) { }
    @Override public void freeGPUMemory(long handle) { }
    @Override public void synchronize() { try { CUDA_DEVICE_SYNCHRONIZE.invokeExact(); } catch (Throwable t) {} }
    @Override public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) { }

    @Override
    public double score(OperationContext context) {
        if (!IS_AVAILABLE) return -1;
        if (MathContext.getCurrent().getRealPrecision() == MathContext.RealPrecision.EXACT) {
            return -1.0; // Hardware Float/Double cannot handle Arbitrary Precision MathContext
        }

        double base = AutoTuningManager.getDynamicScore(getName(), context.getDimensionality(), getPriority());

        // Check for unsupported operations
        if (context.hasHint(OperationContext.Hint.MAT_INV) ||
            context.hasHint(OperationContext.Hint.MAT_DET) ||
            context.hasHint(OperationContext.Hint.MAT_SOLVE)) {
            if (CUSOLVER_DGETRS == null) return 0.1; // Fallback
            base += 10.0;
        }

        if (context.getDataSize() < 256) base -= 200; // Prefer CPU for small matrices
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 50;
        return base;
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.GPU;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return null;
    }
}
