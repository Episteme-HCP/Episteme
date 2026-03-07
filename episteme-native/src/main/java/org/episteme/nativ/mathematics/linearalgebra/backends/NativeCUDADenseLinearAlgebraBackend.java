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
import org.episteme.core.technical.backend.nativ.NativeLibraryLoader;
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
@SuppressWarnings("rawtypes")
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

    // cuSolver Handles
    private static MethodHandle CUSOLVER_CREATE;
    private static MethodHandle CUSOLVER_DESTROY;
    private static MethodHandle CUSOLVER_DGETRF_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_DGETRF;
    private static MethodHandle CUSOLVER_DGETRS;
    private static MethodHandle CUSOLVER_DGEQRF_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_DGEQRF;
    private static MethodHandle CUSOLVER_DORGQR_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_DORGQR;
    private static MethodHandle CUSOLVER_DGESVD_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_DGESVD;
    private static MethodHandle CUSOLVER_DPOTRF_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_DPOTRF;
    private static MethodHandle CUSOLVER_DSYEVD_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_DSYEVD;

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
            
            if (mallocSym.isEmpty() || freeSym.isEmpty() || memcpySym.isEmpty() || syncSym.isEmpty()) {
                logger.warn("Required CUDA runtime symbols missing (malloc={}, free={}, memcpy={}, sync={}). Backend disabled.", 
                    mallocSym.isPresent(), freeSym.isPresent(), memcpySym.isPresent(), syncSym.isPresent());
                return;
            }

            CUDA_MALLOC = LINKER.downcallHandle(mallocSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            CUDA_FREE = LINKER.downcallHandle(freeSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUDA_MEMCPY = LINKER.downcallHandle(memcpySym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
            CUDA_DEVICE_SYNCHRONIZE = LINKER.downcallHandle(syncSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT));

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
                
                Optional<MemorySegment> geqrfBufferSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDgeqrf_bufferSize");
                Optional<MemorySegment> geqrfSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDgeqrf");
                Optional<MemorySegment> orgqrBufferSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDorgqr_bufferSize");
                Optional<MemorySegment> orgqrSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDorgqr");
                Optional<MemorySegment> gesvdBufferSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDgesvd_bufferSize");
                Optional<MemorySegment> gesvdSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDgesvd");

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
                    
                    if (geqrfBufferSym.isPresent() && geqrfSym.isPresent() && orgqrBufferSym.isPresent() && orgqrSym.isPresent()) {
                        CUSOLVER_DGEQRF_BUFFER_SIZE = LINKER.downcallHandle(geqrfBufferSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                        CUSOLVER_DGEQRF = LINKER.downcallHandle(geqrfSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                        CUSOLVER_DORGQR_BUFFER_SIZE = LINKER.downcallHandle(orgqrBufferSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
                        CUSOLVER_DORGQR = LINKER.downcallHandle(orgqrSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                    }

                    if (gesvdBufferSym.isPresent() && gesvdSym.isPresent()) {
                         CUSOLVER_DGESVD_BUFFER_SIZE = LINKER.downcallHandle(gesvdBufferSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                         CUSOLVER_DGESVD = LINKER.downcallHandle(gesvdSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT,
                            ValueLayout.ADDRESS, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
                    }

                    Optional<MemorySegment> potrfBufferSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDpotrf_bufferSize");
                    Optional<MemorySegment> potrfSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDpotrf");
                    if (potrfBufferSym.isPresent() && potrfSym.isPresent()) {
                         CUSOLVER_DPOTRF_BUFFER_SIZE = LINKER.downcallHandle(potrfBufferSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT,
                             ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                         CUSOLVER_DPOTRF = LINKER.downcallHandle(potrfSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT,
                             ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                    }
                    
                    Optional<MemorySegment> syevdBufferSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDsyevd_bufferSize");
                    Optional<MemorySegment> syevdSym = NativeLibraryLoader.findSymbol(cusolver, "cusolverDnDsyevd");
                    if (syevdBufferSym.isPresent() && syevdSym.isPresent()) {
                         CUSOLVER_DSYEVD_BUFFER_SIZE = LINKER.downcallHandle(syevdBufferSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT,
                             ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
                         CUSOLVER_DSYEVD = LINKER.downcallHandle(syevdSym.get(), FunctionDescriptor.of(ValueLayout.JAVA_INT,
                             ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                    }

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
            
            checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long)m * n * Double.BYTES));
            checkCuda((int) CUDA_MALLOC.invokeExact(d_C, (long)m * n * Double.BYTES));
            
            try {
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_A.get(ValueLayout.ADDRESS, 0), h_A, (long)m * n * Double.BYTES, 1));
                
                MemorySegment handle = arena.allocate(ValueLayout.ADDRESS);
                int res = (int) CUBLAS_CREATE.invokeExact(handle);
                checkCublas(res);
                
                try {
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
                    MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
                    
                    // cublasDgeam(handle, transa, transb, m, n, alpha, A, lda, beta, B, ldb, C, ldc)
                    checkCublas((int) CUBLAS_DGEAM.invokeExact(handle.get(ValueLayout.ADDRESS, 0), 
                        1, 0, n, m, alpha, d_A.get(ValueLayout.ADDRESS, 0), m, beta, MemorySegment.NULL, m, d_C.get(ValueLayout.ADDRESS, 0), n));
                    
                    double[] resultData = new double[m * n];
                    MemorySegment h_Result = arena.allocate(ValueLayout.JAVA_DOUBLE, (long)m * n);
                    checkCuda((int) CUDA_MEMCPY.invokeExact(h_Result, d_C.get(ValueLayout.ADDRESS, 0), (long)m * n * Double.BYTES, CUDA_MEMCPY_DEVICE_TO_HOST));
                    MemorySegment.copy(h_Result, ValueLayout.JAVA_DOUBLE, 0, resultData, 0, m * n);
                    
                    return fromDoubleArray(resultData, n, m);
                } finally {
                    int resD = (int) CUBLAS_DESTROY.invokeExact(handle.get(ValueLayout.ADDRESS, 0));
                    checkCublas(resD);
                }
            } finally {
                int res1 = (int) CUDA_FREE.invokeExact(d_A.get(ValueLayout.ADDRESS, 0));
                checkCuda(res1);
                int res2 = (int) CUDA_FREE.invokeExact(d_C.get(ValueLayout.ADDRESS, 0));
                checkCuda(res2);
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

            checkCuda((int) CUDA_MALLOC.invokeExact(p_A, (long) m * k * 8));
            checkCuda((int) CUDA_MALLOC.invokeExact(p_B, (long) k * n * 8));
            checkCuda((int) CUDA_MALLOC.invokeExact(p_C, (long) m * n * 8));

            MemorySegment d_A = p_A.get(ValueLayout.ADDRESS, 0);
            MemorySegment d_B = p_B.get(ValueLayout.ADDRESS, 0);
            MemorySegment d_C = p_C.get(ValueLayout.ADDRESS, 0);

            // Copy to Device
            MemorySegment segA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_A);
            MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_B);

            checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, segA, (long) m * k * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, segB, (long) k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

            // cuBLAS Handle
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            int resH = (int) CUBLAS_CREATE.invokeExact(p_Handle);
            checkCublas(resH);
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            // cuBLAS DGEMM Setup (Alpha, Beta)
            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);

            // Call DGEMM
            // Note: cuBLAS is Column Major by default. 
            // To compute C = A * B in Row Major, we can compute C_T = B_T * A_T.
            // Or just use cuBLAS's column-major logic with swapped arguments.
            checkCublas((int) CUBLAS_DGEMM.invokeExact(handle, CUBLAS_OP_N, CUBLAS_OP_N, 
                n, m, k, alpha, d_B, n, d_A, k, beta, d_C, n));

            // Copy back
            double[] h_C = new double[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(segC, d_C, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, h_C, 0, m * n);

            // Cleanup
            int rD = (int) CUBLAS_DESTROY.invokeExact(handle);
            checkCublas(rD);
            int rA = (int) CUDA_FREE.invokeExact(d_A);
            checkCuda(rA);
            int rB = (int) CUDA_FREE.invokeExact(d_B);
            checkCuda(rB);
            int rC = (int) CUDA_FREE.invokeExact(d_C);
            checkCuda(rC);
            
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
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            int resH = (int) CUSOLVER_CREATE.invokeExact(p_Handle);
            checkCuda(resH);
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            MemorySegment segA = MemorySegment.NULL;
            MemorySegment segB = MemorySegment.NULL;
            MemorySegment segIpiv = MemorySegment.NULL;
            MemorySegment segInfo = MemorySegment.NULL;
            MemorySegment segWork = MemorySegment.NULL;

            try {
                MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_B = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Ipiv = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Info = arena.allocate(ValueLayout.ADDRESS);

                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) n * n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_B, (long) n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Ipiv, (long) n * 4));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segB = d_B.get(ValueLayout.ADDRESS, 0);
                segIpiv = d_Ipiv.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toDoubleArray(a);
                double[] h_B = toDoubleVec(b);
                double[] h_At = new double[n * n];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];

                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
                MemorySegment hostB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_B);

                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, hostA, (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(segB, hostB, (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_DGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 8));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_DGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
                checkCuda((int) CUSOLVER_DGETRS.invokeExact(handle, CUBLAS_OP_N, n, 1, segA, n, segIpiv, segB, n, segInfo));

                double[] h_X = new double[n];
                MemorySegment hostX = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostX, segB, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostX, ValueLayout.JAVA_DOUBLE, 0, h_X, 0, n);

                return fromDoubleVec(h_X);
            } finally {
                if (!segA.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segA);
                if (!segB.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segB);
                if (!segIpiv.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segIpiv);
                if (!segInfo.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segInfo);
                if (!segWork.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segWork);
                int resD = (int) CUSOLVER_DESTROY.invokeExact(handle);
                checkCuda(resD);
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
            int resH = (int) CUSOLVER_CREATE.invokeExact(p_Handle);
            checkCuda(resH);
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            MemorySegment segA = MemorySegment.NULL;
            MemorySegment segB = MemorySegment.NULL;
            MemorySegment segIpiv = MemorySegment.NULL;
            MemorySegment segInfo = MemorySegment.NULL;
            MemorySegment segWork = MemorySegment.NULL;

            try {
                // Allocate GPU Memory
                MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_B = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Ipiv = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Info = arena.allocate(ValueLayout.ADDRESS);

                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) n * n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_B, (long) n * n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Ipiv, (long) n * 4));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segB = d_B.get(ValueLayout.ADDRESS, 0);
                segIpiv = d_Ipiv.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toDoubleArray(a);
                double[] h_At = new double[n * n];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];

                // Identity is also transposed (which is still identity) for col-major
                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
                MemorySegment hostB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, identity);

                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, hostA, (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(segB, hostB, (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                // 1. LU Factorization
                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_DGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 8));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_DGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
                
                // 2. Solve for each column of identity (nrhs = n)
                checkCuda((int) CUSOLVER_DGETRS.invokeExact(handle, CUBLAS_OP_N, n, n, segA, n, segIpiv, segB, n, segInfo));

                // Copy result back
                double[] h_InvT = new double[n * n];
                MemorySegment hostInv = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostInv, segB, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostInv, ValueLayout.JAVA_DOUBLE, 0, h_InvT, 0, n * n);

                // Transpose back to row-major
                double[] h_Inv = new double[n * n];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_Inv[r * n + c] = h_InvT[c * n + r];

                return fromDoubleArray(h_Inv, n, n);
            } finally {
                if (!segA.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segA);
                if (!segB.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segB);
                if (!segIpiv.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segIpiv);
                if (!segInfo.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segInfo);
                if (!segWork.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segWork);
                int resD = (int) CUSOLVER_DESTROY.invokeExact(handle);
                checkCuda(resD);
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
            int resH = (int) CUSOLVER_CREATE.invokeExact(p_Handle);
            checkCuda(resH);
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            MemorySegment segA = MemorySegment.NULL;
            MemorySegment segIpiv = MemorySegment.NULL;
            MemorySegment segInfo = MemorySegment.NULL;
            MemorySegment segWork = MemorySegment.NULL;

            try {
                MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Ipiv = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Info = arena.allocate(ValueLayout.ADDRESS);

                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) n * n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Ipiv, (long) n * 4));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segIpiv = d_Ipiv.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toDoubleArray(a);
                double[] h_At = new double[n * n];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];

                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, hostA, (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_DGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 8));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_DGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));

                // Copy result back (segA now contains LU)
                double[] h_LU = new double[n * n];
                MemorySegment hostLU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostLU, segA, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostLU, ValueLayout.JAVA_DOUBLE, 0, h_LU, 0, n * n);

                int[] h_Ipiv = new int[n];
                MemorySegment hostIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostIpiv, segIpiv, (long) n * 4, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostIpiv, ValueLayout.JAVA_INT, 0, h_Ipiv, 0, n);

                double det = 1.0;
                for (int i = 0; i < n; i++) {
                    det *= h_LU[i * n + i]; 
                    if (h_Ipiv[i] != i + 1) det = -det;
                }

                return Real.of(det);
            } finally {
                if (!segA.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segA);
                if (!segIpiv.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segIpiv);
                if (!segInfo.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segInfo);
                if (!segWork.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segWork);
                int rdD = (int) CUSOLVER_DESTROY.invokeExact(handle);
                checkCuda(rdD);
            }
        } catch (Throwable t) {
            logger.error("CUDA determinant failed: {}", t.getMessage());
            return LinearAlgebraProvider.super.determinant(a);
        }
    }

    private void checkCuda(int result) {
        if (result != 0) throw new RuntimeException("CUDA Error: " + result);
    }

    private void checkCublas(int result) {
        if (result != 0) throw new RuntimeException("cuBLAS Error: " + result);
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

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<Real> qr(Matrix<Real> a) {
        if (!IS_AVAILABLE || CUSOLVER_DGEQRF == null) return LinearAlgebraProvider.super.qr(a);

        int m = a.rows();
        int n = a.cols();
        int k = Math.min(m, n);

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUSOLVER_CREATE.invokeExact(p_Handle));
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            MemorySegment segA = MemorySegment.NULL;
            MemorySegment segTau = MemorySegment.NULL;
            MemorySegment segInfo = MemorySegment.NULL;
            MemorySegment segWork = MemorySegment.NULL;

            try {
                MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Tau = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Info = arena.allocate(ValueLayout.ADDRESS);

                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) m * n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Tau, (long) k * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segTau = d_Tau.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toDoubleArray(a);
                double[] h_At = new double[m * n];
                for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) h_At[c * m + r] = h_A[r * n + c];

                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, hostA, (long) m * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                // 1. GEQRF
                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_DGEQRF_BUFFER_SIZE.invokeExact(handle, m, n, segA, m, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 8));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_DGEQRF.invokeExact(handle, m, n, segA, m, segTau, segWork, lwork, segInfo));

                // Extract R
                double[] h_QR = new double[m * n];
                MemorySegment hostQR = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostQR, segA, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostQR, ValueLayout.JAVA_DOUBLE, 0, h_QR, 0, m * n);

                double[] rData = new double[m * n];
                for (int r = 0; r < m; r++) {
                    for (int c = 0; c < n; c++) {
                        if (c >= r) rData[r * n + c] = h_QR[c * m + r];
                        else rData[r * n + c] = 0.0;
                    }
                }
                Matrix<Real> R = fromDoubleArray(rData, m, n);

                // 2. ORGQR to get Q
                checkCuda((int) CUSOLVER_DORGQR_BUFFER_SIZE.invokeExact(handle, m, k, k, segA, m, segTau, p_Lwork));
                int lworkQ = p_Lwork.get(ValueLayout.JAVA_INT, 0);
                
                if (lworkQ > lwork) {
                    CUDA_FREE.invokeExact(segWork);
                    MemorySegment d_WorkQ = arena.allocate(ValueLayout.ADDRESS);
                    checkCuda((int) CUDA_MALLOC.invokeExact(d_WorkQ, (long) lworkQ * 8));
                    segWork = d_WorkQ.get(ValueLayout.ADDRESS, 0);
                    lwork = lworkQ;
                }

                checkCuda((int) CUSOLVER_DORGQR.invokeExact(handle, m, k, k, segA, m, segTau, segWork, lwork, segInfo));

                double[] h_Q = new double[m * k];
                MemorySegment hostQ = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * k);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostQ, segA, (long) m * k * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostQ, ValueLayout.JAVA_DOUBLE, 0, h_Q, 0, m * k);

                double[] qData = new double[m * k];
                for (int r = 0; r < m; r++) for (int c = 0; c < k; c++) qData[r * k + c] = h_Q[c * m + r];
                Matrix<Real> Q = fromDoubleArray(qData, m, k);

                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<>(Q, R);

            } finally {
                if (!segA.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segA);
                if (!segTau.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segTau);
                if (!segInfo.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segInfo);
                if (!segWork.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segWork);
                CUSOLVER_DESTROY.invokeExact(handle);
            }
        } catch (Throwable t) {
            logger.error("CUDA QR failed: {}", t.getMessage());
            return LinearAlgebraProvider.super.qr(a);
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<Real> svd(Matrix<Real> a) {
        if (!IS_AVAILABLE || CUSOLVER_DGESVD == null) return LinearAlgebraProvider.super.svd(a);

        int m = a.rows();
        int n = a.cols();
        int k = Math.min(m, n);

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUSOLVER_CREATE.invokeExact(p_Handle));
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            MemorySegment segA = MemorySegment.NULL;
            MemorySegment segS = MemorySegment.NULL;
            MemorySegment segU = MemorySegment.NULL;
            MemorySegment segVT = MemorySegment.NULL;
            MemorySegment segInfo = MemorySegment.NULL;
            MemorySegment segWork = MemorySegment.NULL;

            try {
                MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_S = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_U = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_VT = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Info = arena.allocate(ValueLayout.ADDRESS);

                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) m * n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_S, (long) k * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_U, (long) m * m * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_VT, (long) n * n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segS = d_S.get(ValueLayout.ADDRESS, 0);
                segU = d_U.get(ValueLayout.ADDRESS, 0);
                segVT = d_VT.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toDoubleArray(a);
                double[] h_At = new double[m * n];
                for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) h_At[c * m + r] = h_A[r * n + c];

                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, hostA, (long) m * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_DGESVD_BUFFER_SIZE.invokeExact(handle, m, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 8));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                // jobu = 'A' (all), jobvt = 'A' (all)
                checkCuda((int) CUSOLVER_DGESVD.invokeExact(handle, (byte)'A', (byte)'A', m, n, segA, m, segS, segU, m, segVT, n, segWork, lwork, MemorySegment.NULL, segInfo));

                // Copy results back
                double[] h_S = new double[k];
                MemorySegment hostS = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostS, segS, (long) k * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostS, ValueLayout.JAVA_DOUBLE, 0, h_S, 0, k);

                double[] h_Ut = new double[m * m];
                MemorySegment hostU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * m);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostU, segU, (long) m * m * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostU, ValueLayout.JAVA_DOUBLE, 0, h_Ut, 0, m * m);

                double[] h_VTT = new double[n * n];
                MemorySegment hostVT = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostVT, segVT, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostVT, ValueLayout.JAVA_DOUBLE, 0, h_VTT, 0, n * n);

                // Transpose U and VT back to row-major
                // U in col-major is (U^T) in row-major? No, U is m x m.
                // If U is m x m col-major, U[c*m+r] = result[r*m+c]
                double[] uData = new double[m * m];
                for (int r = 0; r < m; r++) for (int c = 0; c < m; c++) uData[r * m + c] = h_Ut[c * m + r];
                
                // VT is n x n. (VT) col-major stored as [c*n+r]
                // V(r,c) = VT(c,r) = [r + c*n]? No, LD is n.
                // In col-major h_VTT: VT(c,r) is at index c + r*n.
                double[] vData = new double[n * n];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) vData[r * n + c] = h_VTT[r * n + c];

                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<>(
                    fromDoubleArray(uData, m, m),
                    fromDoubleVec(h_S),
                    fromDoubleArray(vData, n, n)
                );

            } finally {
                if (!segA.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segA);
                if (!segS.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segS);
                if (!segU.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segU);
                if (!segVT.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segVT);
                if (!segInfo.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segInfo);
                if (!segWork.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segWork);
                CUSOLVER_DESTROY.invokeExact(handle);
            }
        } catch (Throwable t) {
            logger.error("CUDA SVD failed: {}", t.getMessage());
            return LinearAlgebraProvider.super.svd(a);
        }
    }

    private Vector<Real> fromDoubleVec(double[] d) {
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(d);
    }

    @Override public String getNativeLibraryName() { return "cuda"; }
    @Override public DeviceInfo[] getDevices() { return new DeviceInfo[0]; }
    @Override public void selectDevice(int deviceId) { }
    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<Real> cholesky(Matrix<Real> a) {
        if (!IS_AVAILABLE || CUSOLVER_DPOTRF == null) return LinearAlgebraProvider.super.cholesky(a);

        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square for Cholesky");

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUSOLVER_CREATE.invokeExact(p_Handle));
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            MemorySegment segA = MemorySegment.NULL;
            MemorySegment segInfo = MemorySegment.NULL;
            MemorySegment segWork = MemorySegment.NULL;

            try {
                MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Info = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) n * n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toDoubleArray(a);
                // potrf is col-major. A = L * L^T. 
                // Since A is symmetric, col-major(A) == row-major(A).
                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_A);
                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, hostA, (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                // CUSOLVER_FILL_MODE_LOWER = 0
                checkCuda((int) CUSOLVER_DPOTRF_BUFFER_SIZE.invokeExact(handle, 0, n, segA, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 8));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_DPOTRF.invokeExact(handle, 0, n, segA, n, segWork, lwork, segInfo));

                double[] h_L = new double[n * n];
                MemorySegment hostL = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostL, segA, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostL, ValueLayout.JAVA_DOUBLE, 0, h_L, 0, n * n);

                // Zero out upper part manually for result
                double[] lData = new double[n * n];
                for (int r = 0; r < n; r++) {
                    for (int c = 0; c < n; c++) {
                        if (r >= c) lData[r * n + c] = h_L[c * n + r]; // Col-major L(r,c) to Row-major
                        else lData[r * n + c] = 0.0;
                    }
                }

                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<>(fromDoubleArray(lData, n, n));
            } finally {
                if (!segA.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segA);
                if (!segInfo.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segInfo);
                if (!segWork.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segWork);
                CUSOLVER_DESTROY.invokeExact(handle);
            }
        } catch (Throwable t) {
            logger.error("CUDA Cholesky failed: {}", t.getMessage());
            return LinearAlgebraProvider.super.cholesky(a);
        }
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<Real> lu(Matrix<Real> a) {
        if (!IS_AVAILABLE || CUSOLVER_DGETRF == null) return LinearAlgebraProvider.super.lu(a);

        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square for LU");

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUSOLVER_CREATE.invokeExact(p_Handle));
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            MemorySegment segA = MemorySegment.NULL;
            MemorySegment segIpiv = MemorySegment.NULL;
            MemorySegment segInfo = MemorySegment.NULL;
            MemorySegment segWork = MemorySegment.NULL;

            try {
                MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Ipiv = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Info = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) n * n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Ipiv, (long) n * 4));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segIpiv = d_Ipiv.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toDoubleArray(a);
                double[] h_At = new double[n * n];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];

                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, hostA, (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_DGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 8));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_DGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));

                double[] h_LU = new double[n * n];
                MemorySegment hostLU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostLU, segA, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostLU, ValueLayout.JAVA_DOUBLE, 0, h_LU, 0, n * n);

                int[] h_Ipiv = new int[n];
                MemorySegment hostIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostIpiv, segIpiv, (long) n * 4, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostIpiv, ValueLayout.JAVA_INT, 0, h_Ipiv, 0, n);

                // Reconstruct L, U, and P
                double[] lData = new double[n * n];
                double[] uData = new double[n * n];
                for (int r = 0; r < n; r++) {
                    for (int c = 0; c < n; c++) {
                        double val = h_LU[c * n + r]; // LU is in col-major index c*n+r
                        if (r > c) {
                            lData[r * n + c] = val;
                            uData[r * n + c] = 0.0;
                        } else if (r == c) {
                            lData[r * n + c] = 1.0;
                            uData[r * n + c] = val;
                        } else {
                            lData[r * n + c] = 0.0;
                            uData[r * n + c] = val;
                        }
                    }
                }

                // Convert ipiv to permutation vector
                int[] perm = new int[n];
                for (int i = 0; i < n; i++) perm[i] = i;
                for (int i = 0; i < n; i++) {
                    int j = h_Ipiv[i] - 1;
                    if (i != j) {
                        int tmp = perm[i];
                        perm[i] = perm[j];
                        perm[j] = tmp;
                    }
                }
                
                double[] pData = new double[n];
                for (int i = 0; i < n; i++) pData[i] = perm[i];

                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<>(
                    fromDoubleArray(lData, n, n),
                    fromDoubleArray(uData, n, n),
                    fromDoubleVec(pData)
                );
            } finally {
                if (!segA.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segA);
                if (!segIpiv.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segIpiv);
                if (!segInfo.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segInfo);
                if (!segWork.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segWork);
                CUSOLVER_DESTROY.invokeExact(handle);
            }
        } catch (Throwable t) {
            logger.error("CUDA LU failed: {}", t.getMessage());
            return LinearAlgebraProvider.super.lu(a);
        }
    }
    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<Real> eigen(Matrix<Real> a) {
        if (!IS_AVAILABLE || CUSOLVER_DSYEVD == null) return LinearAlgebraProvider.super.eigen(a);

        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square for Eigen");

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUSOLVER_CREATE.invokeExact(p_Handle));
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

            MemorySegment segA = MemorySegment.NULL;
            MemorySegment segW = MemorySegment.NULL;
            MemorySegment segInfo = MemorySegment.NULL;
            MemorySegment segWork = MemorySegment.NULL;

            try {
                MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_W = arena.allocate(ValueLayout.ADDRESS);
                MemorySegment d_Info = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) n * n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_W, (long) n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segW = d_W.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toDoubleArray(a);
                // syevd assumes symmetric. A(row,col) == A(col,row).
                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_A);
                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, hostA, (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                // jobz=1 (VECTORS), uplo=0 (LOWER)
                checkCuda((int) CUSOLVER_DSYEVD_BUFFER_SIZE.invokeExact(handle, 1, 0, n, segA, n, segW, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 8));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_DSYEVD.invokeExact(handle, 1, 0, n, segA, n, segW, segWork, lwork, segInfo));

                double[] h_W = new double[n];
                MemorySegment hostW = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostW, segW, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostW, ValueLayout.JAVA_DOUBLE, 0, h_W, 0, n);

                double[] h_V = new double[n * n];
                MemorySegment hostV = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostV, segA, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostV, ValueLayout.JAVA_DOUBLE, 0, h_V, 0, n * n);

                // h_V is in col-major index c*n+r. Result [r*n+c] = h_V[c*n+r]
                double[] vData = new double[n * n];
                for (int r = 0; r < n; r++) {
                    for (int c = 0; c < n; c++) {
                        vData[r * n + c] = h_V[c * n + r];
                    }
                }

                return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<>(
                    fromDoubleArray(vData, n, n),
                    fromDoubleVec(h_W)
                );
            } finally {
                if (!segA.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segA);
                if (!segW.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segW);
                if (!segInfo.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segInfo);
                if (!segWork.equals(MemorySegment.NULL)) CUDA_FREE.invokeExact(segWork);
                CUSOLVER_DESTROY.invokeExact(handle);
            }
        } catch (Throwable t) {
            logger.error("CUDA Eigen failed: {}", t.getMessage());
            return LinearAlgebraProvider.super.eigen(a);
        }
    }

    @Override
    public void copyToGPU(long handle, java.nio.DoubleBuffer buffer, long count) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment host = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, buffer.array());
            checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(handle), host, count * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
        } catch (Throwable t) {
            logger.error("Failed to copy to GPU: {}", t.getMessage());
        }
    }

    @Override
    public void copyFromGPU(long handle, java.nio.DoubleBuffer buffer, long count) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, count);
            checkCuda((int) CUDA_MEMCPY.invokeExact(host, MemorySegment.ofAddress(handle), count * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, buffer.array(), 0, (int) count);
        } catch (Throwable t) {
            logger.error("Failed to copy from GPU: {}", t.getMessage());
        }
    }

    @Override
    public long allocateGPUMemory(long size) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p = arena.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUDA_MALLOC.invokeExact(p, size));
            return p.get(ValueLayout.ADDRESS, 0).address();
        } catch (Throwable t) {
            logger.error("Failed to allocate GPU memory: {}", t.getMessage());
            return 0;
        }
    }

    @Override
    public void freeGPUMemory(long handle) {
        try {
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(handle)));
        } catch (Throwable t) {
            logger.error("Failed to free GPU memory: {}", t.getMessage());
        }
    }

    @Override public void synchronize() { try { int res = (int) CUDA_DEVICE_SYNCHRONIZE.invokeExact(); checkCuda(res); } catch (Throwable t) {} }
    @Override public void matrixMultiply(java.nio.DoubleBuffer A, java.nio.DoubleBuffer B, java.nio.DoubleBuffer C, int m, int n, int k) { }

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
            context.hasHint(OperationContext.Hint.MAT_SOLVE) ||
            context.hasHint(OperationContext.Hint.MAT_QR) ||
            context.hasHint(OperationContext.Hint.MAT_SVD) ||
            context.hasHint(OperationContext.Hint.MAT_CHOLESKY) ||
            context.hasHint(OperationContext.Hint.MAT_LU) ||
            context.hasHint(OperationContext.Hint.MAT_EIGEN)) {
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
