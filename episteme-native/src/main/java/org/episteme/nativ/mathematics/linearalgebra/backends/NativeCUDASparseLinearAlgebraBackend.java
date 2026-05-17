/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.structures.rings.FieldElement;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.numbers.real.RealFloat;
import org.episteme.core.mathematics.numbers.real.RealDouble;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.context.NumericalConfiguration;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.MatrixSolver;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.DoubleBuffer;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Robust CUDA acceleration backend using Project Panama to interface with CUDA and CUBLAS.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, GPUBackend.class, org.episteme.core.technical.algorithm.AlgorithmProvider.class})
public class NativeCUDASparseLinearAlgebraBackend<E extends FieldElement<E>> implements SparseLinearAlgebraProvider<E>, NativeBackend, GPUBackend {
    
    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE && !isExplicitlyDisabled();
    }

    @Override
    public boolean isLoaded() {
        return IS_AVAILABLE;
    }

    @Override
    public String getNativeLibraryName() {
        return "cuda";
    }

    @Override
    public String getName() {
        return "Native CUDA Sparse Linear Algebra Backend";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public double score(org.episteme.core.technical.algorithm.OperationContext context) {
        if (!isAvailable()) return -1;
        
        // Strict domain boundary: reject dense operations
        if (context.hasHint(org.episteme.core.technical.algorithm.OperationContext.Hint.DENSE)) {
            return -1.0;
        }

        return org.episteme.core.technical.algorithm.AutoTuningManager.getDynamicScore(getName(), context.getDimensionality(), getPriority());
    }

    @Override
    public void shutdown() {
        if (IS_AVAILABLE) {
            try {
                if (staticCusparseHandle != null) {
                    CUSPARSE_DESTROY.invokeExact(staticCusparseHandle);
                    staticCusparseHandle = null;
                }
                if (staticCublasHandle != null) {
                    CUBLAS_DESTROY.invokeExact(staticCublasHandle);
                    staticCublasHandle = null;
                }
                IS_AVAILABLE = false;
                initAttempted = false;
            } catch (Throwable t) {
                logger.warn("Error during CUDA shutdown: {}", t.getMessage());
            }
        }
    }


    @Override
    public String getType() {
        return "linearalgebra";
    }


    @Override
    public void freeGPUMemory(long handle) {
        if (handle == 0) return;
        try {
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(handle)));
        } catch (Throwable t) {
            logger.error("Failed to free GPU memory: {}", t.getMessage());
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(NativeCUDASparseLinearAlgebraBackend.class);
    private static final Linker LINKER = Linker.nativeLinker();
    
    // CUDA Runtime / cuSPARSE State
    private static boolean IS_AVAILABLE = false;
    private static boolean initAttempted = false;
    private static SymbolLookup cusparse_lookup;

    // CUDA Handles
    private static MethodHandle CUDA_MALLOC;
    private static MethodHandle CUDA_FREE;
    private static MethodHandle CUDA_MEMCPY;
    private static MethodHandle CUDA_DEVICE_SYNCHRONIZE;
    private static MethodHandle CUDA_GET_DEVICE_COUNT;
    private static MethodHandle CUDA_GET_ERROR_STRING;
    private static MethodHandle CU_CTX_GET_CURRENT;
    private static MethodHandle CU_CTX_GET_DEVICE;
    private static MethodHandle CUDA_MEMSET;
    
    private static MemorySegment staticCusparseHandle;
    private static MemorySegment staticCublasHandle;

    private static MethodHandle CUSPARSE_CREATE;
    private static MethodHandle CUSPARSE_DESTROY;
    private static MethodHandle CUSPARSE_CREATE_CSR;
    private static MethodHandle CUSPARSE_DESTROY_SP_MAT;
    private static MethodHandle CUSPARSE_CREATE_DN_VEC;
    private static MethodHandle CUSPARSE_DESTROY_DN_VEC;
    private static MethodHandle CUSPARSE_CREATE_DN_MAT;
    private static MethodHandle CUSPARSE_DESTROY_DN_MAT;
    private static MethodHandle CUSPARSE_SPMV;
    private static MethodHandle CUSPARSE_SPMV_BUFFER_SIZE;
    private static MethodHandle CUSPARSE_SPMM;
    private static MethodHandle CUSPARSE_SPMM_BUFFER_SIZE;
    
    // cuBLAS Handles
    private static MethodHandle CUBLAS_CREATE;
    private static MethodHandle CUBLAS_DESTROY;
    private static MethodHandle CUBLAS_DGEMM;
    private static MethodHandle CUBLAS_DDOT;
    private static MethodHandle CUBLAS_DAXPY;
    private static MethodHandle CUBLAS_DSCAL;
    private static MethodHandle CUBLAS_DGEAM;



    // Constants
    private static final int CUSPARSE_INDEX_16U = 0;
    private static final int CUSPARSE_INDEX_32BIT = 1; // CUSPARSE_INDEX_32I
    private static final int CUSPARSE_INDEX_64BIT = 2; // CUSPARSE_INDEX_64I
    private static final int CUSPARSE_INDEX_BASE_ZERO = 0;
    private static final int CUDA_R_64F = 1; // Double
    private static final int CUSPARSE_ORDER_COL = 1;
    private static final int CUSPARSE_ORDER_ROW = 2; // Correct for cuSPARSE 12+
    private static final int CUSPARSE_SPMM_ALG_DEFAULT = 0;
    private static final int CUDA_MEMCPY_HOST_TO_DEVICE = 1;
    private static final int CUDA_MEMCPY_DEVICE_TO_HOST = 2;
    private static final int CUDA_MEMCPY_DEVICE_TO_DEVICE = 3;

    static {
        ensureInitialized();
    }


    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        if (ring == null) return false;
        Object zero = ring.zero();
        return zero instanceof Real || zero instanceof Complex;
    }

    private static MethodHandle lookup(SymbolLookup lookup, String name, FunctionDescriptor descriptor) {
        return NativeFFMLoader.findSymbol(lookup, name)
            .map(s -> LINKER.downcallHandle(s, descriptor))
            .orElse(null);
    }

    private static synchronized void ensureInitialized() {
        if (initAttempted) return;
        initAttempted = true;

        System.err.println("[CUDA-Sparse-Init] Starting...");
        try {
            // 1. Load CUDA Runtime
            Optional<SymbolLookup> cudaRtOpt = findLibrary("cudart", Arena.global());
            if (cudaRtOpt.isEmpty()) {
                System.err.println("[CUDA-Sparse-Init] ABORT: cudart not found.");
                return;
            }
            SymbolLookup cudart = cudaRtOpt.get();

            // 2. cuSPARSE
            Optional<SymbolLookup> cusparseOpt = findLibrary("cusparse", Arena.global());
            if (cusparseOpt.isEmpty()) {
                System.err.println("[CUDA-Sparse-Init] ABORT: cusparse not found.");
                return;
            }
            cusparse_lookup = cusparseOpt.get();

            // 3. cuBLAS
            Optional<SymbolLookup> cublasOpt = findLibrary("cublas", Arena.global());
            if (cublasOpt.isEmpty()) {
                 System.err.println("[CUDA-Sparse-Init] ABORT: cublas not found.");
                return;
            }
            SymbolLookup cublas = cublasOpt.get();

            // 5. Bind Handles
            System.err.println("[CUDA-Sparse-Init] Binding symbols...");
            CUDA_MALLOC = lookup(cudart, "cudaMalloc", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            CUDA_FREE = lookup(cudart, "cudaFree", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUDA_MEMCPY = lookup(cudart, "cudaMemcpy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
            CUDA_DEVICE_SYNCHRONIZE = lookup(cudart, "cudaDeviceSynchronize", FunctionDescriptor.of(ValueLayout.JAVA_INT));
            CUDA_GET_DEVICE_COUNT = lookup(cudart, "cudaGetDeviceCount", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            CUDA_MEMSET = lookup(cudart, "cudaMemset", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            CUSPARSE_CREATE = lookup(cusparse_lookup, "cusparseCreate", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSPARSE_DESTROY = lookup(cusparse_lookup, "cusparseDestroy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            CUSPARSE_CREATE_CSR = lookup(cusparse_lookup, "cusparseCreateCsr", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
            CUSPARSE_DESTROY_SP_MAT = lookup(cusparse_lookup, "cusparseDestroySpMat", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            CUSPARSE_CREATE_DN_MAT = lookup(cusparse_lookup, "cusparseCreateDnMat", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
            CUSPARSE_DESTROY_DN_MAT = lookup(cusparse_lookup, "cusparseDestroyDnMat", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMM_BUFFER_SIZE = lookup(cusparse_lookup, "cusparseSpMM_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMM = lookup(cusparse_lookup, "cusparseSpMM", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMV_BUFFER_SIZE = lookup(cusparse_lookup, "cusparseSpMV_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMV = lookup(cusparse_lookup, "cusparseSpMV", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_CREATE_DN_VEC = lookup(cusparse_lookup, "cusparseCreateDnVec", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            CUSPARSE_DESTROY_DN_VEC = lookup(cusparse_lookup, "cusparseDestroyDnVec", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUBLAS_CREATE = lookup(cublas, "cublasCreate_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            if (CUBLAS_CREATE == null) CUBLAS_CREATE = lookup(cublas, "cublasCreate", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUBLAS_DESTROY = lookup(cublas, "cublasDestroy_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            if (CUBLAS_DESTROY == null) CUBLAS_DESTROY = lookup(cublas, "cublasDestroy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            CUBLAS_DGEMM = lookup(cublas, "cublasDgemm_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

            CUBLAS_DDOT = lookup(cublas, "cublasDdot_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUBLAS_DAXPY = lookup(cublas, "cublasDaxpy_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

            CUBLAS_DSCAL = lookup(cublas, "cublasDscal_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            
            CUBLAS_DGEAM = lookup(cublas, "cublasDgeam_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            if (CUBLAS_DGEAM == null) CUBLAS_DGEAM = lookup(cublas, "cublasDgeam", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

            if (CUSPARSE_CREATE == null || CUDA_MALLOC == null) {
                System.err.println("[CUDA-Sparse-Init] ABORT: Essential symbols missing (cusparseCreate=" + (CUSPARSE_CREATE != null) + ", cudaMalloc=" + (CUDA_MALLOC != null) + ")");
                return;
            }

            // 6. Check for Hardware
            if (CUDA_GET_DEVICE_COUNT != null) {
                try (Arena tempArena = Arena.ofConfined()) {
                    MemorySegment countPtr = tempArena.allocate(ValueLayout.JAVA_INT);
                    System.err.println("[CUDA-Sparse-Init] Invoking cudaGetDeviceCount...");
                    int cudaResult = (int) CUDA_GET_DEVICE_COUNT.invokeExact(countPtr);
                    int deviceCount = countPtr.get(ValueLayout.JAVA_INT, 0);
                    System.err.println("[CUDA-Sparse-Init] cudaGetDeviceCount result=" + cudaResult + ", count=" + deviceCount);
                    if (cudaResult != 0 || deviceCount <= 0) {
                        logger.warn("No CUDA-capable GPU devices found (result={}, count={}). Backend disabled.", cudaResult, deviceCount);
                        return;
                    }
                }
            } else {
                System.err.println("[CUDA-Sparse-Init] ABORT: cudaGetDeviceCount symbol missing.");
                return;
            }

            IS_AVAILABLE = true;
            System.err.println("[CUDA-Sparse-Init] SUCCESS: Native CUDA Sparse Backend is READY.");
            logger.info("Native CUDA Sparse Backend initialized successfully.");
        } catch (Throwable t) {
            System.err.println("[CUDA-Sparse-Init] CRITICAL FAILURE: " + t.getClass().getSimpleName() + " - " + t.getMessage());
            IS_AVAILABLE = false;
            logger.error("CUDA Sparse initialization failed: {}.", t.getMessage());
        }
    }

    public NativeCUDASparseLinearAlgebraBackend() {
    }

    @Override
    public DeviceInfo[] getDevices() {
        if (!IS_AVAILABLE) return new DeviceInfo[0];
        return new DeviceInfo[]{new DeviceInfo("CUDA Sparse Device", 8L * 1024 * 1024 * 1024, 128, "NVIDIA")};
    }

    @Override
    public String getEnvironmentInfo() {
        return IS_AVAILABLE ? "GPU (CUDA)" : "N/A";
    }

    @Override
    public ExecutionContext createContext() {
        if (!isAvailable()) return null;
        try (Arena arena = Arena.ofConfined()) {
            int devId = 0;
            // Best effort to get active device and context
            MemorySegment pCtx = arena.allocate(ValueLayout.ADDRESS);
            long ctxHandle = 0;
            if (CU_CTX_GET_CURRENT != null) {
                if ((int)CU_CTX_GET_CURRENT.invokeExact(pCtx) == 0) {
                    ctxHandle = pCtx.get(ValueLayout.ADDRESS, 0).address();
                }
            }
            
            MemorySegment pDev = arena.allocate(ValueLayout.JAVA_INT);
            if (CU_CTX_GET_DEVICE != null) {
                if ((int)CU_CTX_GET_DEVICE.invokeExact(pDev) == 0) {
                    devId = pDev.get(ValueLayout.JAVA_INT, 0);
                }
            }
            
            return org.episteme.nativ.technical.backend.gpu.cuda.CUDAExecutionContext.fromNative(ctxHandle, devId);
        } catch (Throwable t) {
            return new org.episteme.nativ.technical.backend.gpu.cuda.CUDAExecutionContext(null, null);
        }
    }

    @Override
    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) {
        if (!IS_AVAILABLE) return;
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * 8, tracker);
            MemorySegment d_B = malloc((long) k * n * 8, tracker);
            MemorySegment d_C = malloc((long) m * n * 8, tracker);

            checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, MemorySegment.ofBuffer(A), (long) m * k * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, MemorySegment.ofBuffer(B), (long) k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);

            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_DGEMM.invokeExact(handle, 0, 0, n, m, k, alpha, d_B, n, d_A, k, beta, d_C, n));

            checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofBuffer(C), d_C, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
        } catch (Throwable t) {
            logger.error("cuBLAS DGEMM failed: {}", t.getMessage());
            throw new RuntimeException("Matrix multiply failed", t);
        }
    }

    @Override
    public void selectDevice(int deviceId) {}

    @Override
    public void synchronize() {
        try {
            NativeSafe.invoke(CUDA_DEVICE_SYNCHRONIZE);
        } catch (Throwable t) {
            logger.error("CUDA synchronize failed: {}", t.getMessage());
        }
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            if (b.cols() == 1) {
                return multiplySparseVector((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) a, b.getColumn(0)).toMatrix();
            }
            return multiplySparseDense((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) a, b);
        }
        return multiplyDense(a, b);
    }



    @Override
    public E determinant(Matrix<E> a) {
        throw new UnsupportedOperationException("CUDA Sparse Backend does not support determinant yet.");
    }

    @Override
    public E trace(Matrix<E> A) {
        if (A.rows() != A.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = A.rows();
        Ring<E> ring = A.getScalarRing();
        E sum = ring.zero();
        for (int i = 0; i < n; i++) {
            sum = ring.add(sum, A.get(i, i));
        }
        return sum;
    }



    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            return multiplySparseVector((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) a, b);
        }
        throw new UnsupportedOperationException("CUDA multiply not supported for non-sparse matrices.");
    }

    private Vector<E> multiplySparseVector(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> a, Vector<E> b) {
        int m = a.rows();
        int k = a.cols();
        int nnz = a.getNnz();

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            int[] rowPtrHost = a.getRowPointers();
            int[] colIdxHost = a.getColIndices();
            double[] valsHost = new double[nnz];
            Object[] valsObj = a.getValues();
            for (int i = 0; i < nnz; i++) {
                E val = (E) valsObj[i];
                valsHost[i] = getRealValue((E) val);
            }

            MemorySegment d_csrRowPtr = malloc((long)(m + 1) * 4, tracker);
            MemorySegment d_csrColIdx = malloc((long)nnz * 4, tracker);
            MemorySegment d_csrVal = malloc((long)nnz * 8, tracker);
            MemorySegment d_x = malloc((long)k * 8, tracker);
            MemorySegment d_y = malloc((long)m * 8, tracker);

            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrRowPtr, arena.allocateFrom(ValueLayout.JAVA_INT, rowPtrHost), (long)(m + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrColIdx, arena.allocateFrom(ValueLayout.JAVA_INT, colIdxHost), (long)nnz * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrVal, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, valsHost), (long)nnz * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_x, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b)), (long)k * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

            spmv_internal(m, k, nnz, d_csrRowPtr, d_csrColIdx, d_csrVal, d_x, d_y, arena, tracker);

            double[] yHost = new double[m];
            MemorySegment hostY = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostY, d_y, (long) m * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostY, ValueLayout.JAVA_DOUBLE, 0, yHost, 0, m);

            return toVector(yHost, a.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("CUDA Sparse-Vector multiply failed", t);
        }
    }

    private Matrix<E> multiplySparseDense(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> a, Matrix<E> b) {
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();
        int nnz = a.getNnz();

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_csrRowPtr = malloc((long)(m + 1) * 4, tracker);
            MemorySegment d_csrColIdx = malloc((long)nnz * 4, tracker);
            MemorySegment d_csrVal = malloc((long)nnz * 8, tracker);
            MemorySegment d_B = malloc((long)k * n * 8, tracker);
            MemorySegment d_C = malloc((long)m * n * 8, tracker);

            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrRowPtr, arena.allocateFrom(ValueLayout.JAVA_INT, a.getRowPointers()), (long)(m + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrColIdx, arena.allocateFrom(ValueLayout.JAVA_INT, a.getColIndices()), (long)nnz * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
            double[] vals = new double[nnz];
            Object[] valsObj = a.getValues();
            for (int i = 0; i < nnz; i++) vals[i] = getRealValue((E) valsObj[i]);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrVal, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, vals), (long)nnz * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            
            double[] bData = new double[k * n];
            for (int i = 0; i < k; i++) for (int j = 0; j < n; j++) bData[i * n + j] = getRealValue(b.get(i, j));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bData), (long)k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

            spmm_internal(m, n, k, nnz, d_csrRowPtr, d_csrColIdx, d_csrVal, d_B, d_C, arena, tracker);

            double[] cData = new double[m * n];
            MemorySegment hostC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostC, d_C, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostC, ValueLayout.JAVA_DOUBLE, 0, cData, 0, m * n);

            return toMatrix(cData, m, n, b.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("CUDA Sparse-Dense multiply failed", t);
        }
    }

    private Matrix<E> multiplyDense(Matrix<E> a, Matrix<E> b) {
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            double[] aData = new double[m * k];
            double[] bData = new double[k * n];
            for (int i = 0; i < m; i++) for (int j = 0; j < k; j++) aData[i * k + j] = getRealValue(a.get(i, j));
            for (int i = 0; i < k; i++) for (int j = 0; j < n; j++) bData[i * n + j] = getRealValue(b.get(i, j));

            MemorySegment d_A = malloc((long) m * k * 8, tracker);
            MemorySegment d_B = malloc((long) k * n * 8, tracker);
            MemorySegment d_C = malloc((long) m * n * 8, tracker);

            checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, aData), (long) m * k * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bData), (long) k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

            MemorySegment handle = createCublasHandle(tracker);
            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);

            checkCublas((int) CUBLAS_DGEMM.invokeExact(handle, 0, 0, n, m, k, alpha, d_B, n, d_A, k, beta, d_C, n));

            double[] cData = new double[m * n];
            MemorySegment hostC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostC, d_C, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostC, ValueLayout.JAVA_DOUBLE, 0, cData, 0, m * n);

            return toMatrix(cData, m, n, a.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("CUDA Dense-Dense multiply failed", t);
        }
    }

    private MemorySegment createCusparseHandle(ResourceTracker tracker) throws Throwable {
        if (!IS_AVAILABLE) ensureInitialized();
        if (staticCusparseHandle == null) {
            MemorySegment handlePtr = Arena.ofAuto().allocate(ValueLayout.ADDRESS);
            checkCusparse((int) CUSPARSE_CREATE.invokeExact(handlePtr));
            staticCusparseHandle = handlePtr.get(ValueLayout.ADDRESS, 0);
        }
        return staticCusparseHandle;
    }

    private MemorySegment createCublasHandle(ResourceTracker tracker) throws Throwable {
        if (!IS_AVAILABLE) ensureInitialized();
        if (staticCublasHandle == null) {
            MemorySegment handlePtr = Arena.ofAuto().allocate(ValueLayout.ADDRESS);
            checkCublas((int) CUBLAS_CREATE.invokeExact(handlePtr));
            staticCublasHandle = handlePtr.get(ValueLayout.ADDRESS, 0);
        }
        return staticCublasHandle;
    }

    private void spmv_internal(int m, int k, int nnz, MemorySegment d_rowPtr, MemorySegment d_colIdx, MemorySegment d_val, MemorySegment d_x, MemorySegment d_y, Arena arena, ResourceTracker tracker) throws Throwable {
        MemorySegment matAPtr = arena.allocate(ValueLayout.ADDRESS);
        checkCusparse((int) CUSPARSE_CREATE_CSR.invokeExact(matAPtr, (long)m, (long)k, (long)nnz, 
            d_rowPtr, d_colIdx, d_val,
            CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, CUDA_R_64F));
        MemorySegment matA = matAPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(matA, s -> { try { CUSPARSE_DESTROY_SP_MAT.invokeExact(s); } catch (Throwable t) { logger.error("Failed to destroy sparse matrix: {}", t.getMessage()); } });

        MemorySegment vechXPtr = arena.allocate(ValueLayout.ADDRESS);
        checkCusparse((int) CUSPARSE_CREATE_DN_VEC.invokeExact(vechXPtr, (long)k, d_x, CUDA_R_64F));
        MemorySegment vecX = vechXPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(vecX, s -> { try { CUSPARSE_DESTROY_DN_VEC.invokeExact(s); } catch (Throwable t) { logger.error("Failed to destroy dense vector X: {}", t.getMessage()); } });

        MemorySegment vechYPtr = arena.allocate(ValueLayout.ADDRESS);
        checkCusparse((int) CUSPARSE_CREATE_DN_VEC.invokeExact(vechYPtr, (long)m, d_y, CUDA_R_64F));
        MemorySegment vecY = vechYPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(vecY, s -> { try { CUSPARSE_DESTROY_DN_VEC.invokeExact(s); } catch (Throwable t) { logger.error("Failed to destroy dense vector Y: {}", t.getMessage()); } });

        MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
        MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
        MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);

        MemorySegment handle = createCusparseHandle(tracker);

        checkCusparse((int) CUSPARSE_SPMV_BUFFER_SIZE.invokeExact(handle, 0, alpha, matA, vecX, beta, vecY, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr));
        long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
        
        MemorySegment d_buffer = malloc(bufferSize, tracker);

        checkCusparse((int) CUSPARSE_SPMV.invokeExact(handle, 0, alpha, matA, vecX, beta, vecY, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, d_buffer));
    }

    private void spmm_internal(int m, int n, int k, int nnz, MemorySegment d_rowPtr, MemorySegment d_colIdx, MemorySegment d_val, MemorySegment d_B, MemorySegment d_C, Arena arena, ResourceTracker tracker) throws Throwable {
        MemorySegment matAPtr = arena.allocate(ValueLayout.ADDRESS);
        checkCusparse((int) CUSPARSE_CREATE_CSR.invokeExact(matAPtr, (long)m, (long)k, (long)nnz, d_rowPtr, d_colIdx, d_val,
            CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, CUDA_R_64F));
        MemorySegment matA = matAPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(matA, s -> { try { CUSPARSE_DESTROY_SP_MAT.invokeExact(s); } catch (Throwable t) { logger.error("Failed to destroy sparse matrix: {}", t.getMessage()); } });

        MemorySegment matBPtr = arena.allocate(ValueLayout.ADDRESS);
        checkCusparse((int) CUSPARSE_CREATE_DN_MAT.invokeExact(matBPtr, (long)k, (long)n, (long)n, d_B, CUDA_R_64F, CUSPARSE_ORDER_ROW));
        MemorySegment matB = matBPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(matB, s -> { try { CUSPARSE_DESTROY_DN_MAT.invokeExact(s); } catch (Throwable t) { logger.error("Failed to destroy dense matrix B: {}", t.getMessage()); } });

        MemorySegment matCPtr = arena.allocate(ValueLayout.ADDRESS);
        checkCusparse((int) CUSPARSE_CREATE_DN_MAT.invokeExact(matCPtr, (long)m, (long)n, (long)n, d_C, CUDA_R_64F, CUSPARSE_ORDER_ROW));
        MemorySegment matC = matCPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(matC, s -> { try { CUSPARSE_DESTROY_DN_MAT.invokeExact(s); } catch (Throwable t) { logger.error("Failed to destroy dense matrix C: {}", t.getMessage()); } });

        MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
        MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
        MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);

        MemorySegment handle = createCusparseHandle(tracker);

        checkCusparse((int) CUSPARSE_SPMM_BUFFER_SIZE.invokeExact(handle, 0, 0, alpha, matA, matB, beta, matC, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr));
        long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
        
        MemorySegment d_buffer = malloc(bufferSize, tracker);

        checkCusparse((int) CUSPARSE_SPMM.invokeExact(handle, 0, 0, alpha, matA, matB, beta, matC, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, d_buffer));
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        int n = b.dimension();
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> A_sparse = ensureSparse(a);

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            double tol_val = getRealValue(tolerance);
            
            MemorySegment d_r = malloc((long) n * 8, tracker);
            MemorySegment d_r_hat = malloc((long) n * 8, tracker);
            MemorySegment d_p = malloc((long) n * 8, tracker);
            MemorySegment d_v = malloc((long) n * 8, tracker);
            MemorySegment d_s = malloc((long) n * 8, tracker);
            MemorySegment d_t = malloc((long) n * 8, tracker);
            MemorySegment d_x = malloc((long) n * 8, tracker);
            MemorySegment d_b = malloc((long) n * 8, tracker);

            int nnz_val = A_sparse.getNnz();
            MemorySegment d_csrRowPtr = malloc((long) (n + 1) * 4, tracker);
            MemorySegment d_csrColIdx = malloc((long) nnz_val * 4, tracker);
            MemorySegment d_csrVal = malloc((long) nnz_val * 8, tracker);

            checkCuda((int) CUDA_MEMCPY.invokeExact(d_b, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            if (x0 != null) {
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_x, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(x0)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            } else {
                checkCuda((int) CUDA_MEMSET.invokeExact(d_x, 0, (long) n * 8));
            }

            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrRowPtr, arena.allocateFrom(ValueLayout.JAVA_INT, A_sparse.getRowPointers()), (long) (n + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrColIdx, arena.allocateFrom(ValueLayout.JAVA_INT, A_sparse.getColIndices()), (long) nnz_val * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
            double[] vals = new double[nnz_val];
            Object[] valsObj = A_sparse.getValues();
            for (int i = 0; i < nnz_val; i++) {
                vals[i] = getRealValue((E) valsObj[i]);
            }
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrVal, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, vals), (long) nnz_val * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

            spmv_internal(n, n, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_x, d_r, arena, tracker);
            daxpy_internal(n, -1.0, d_r, d_b, tracker);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_r_hat, d_r, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE));

            double rho = 1.0, alpha = 1.0, omega = 1.0;
            double rho_prev, beta;

            for (int i = 0; i < maxIterations; i++) {
                rho_prev = rho;
                rho = ddot_internal(n, d_r_hat, d_r, tracker);
                if (Math.abs(rho) < 1e-20) break;

                if (i == 0) {
                    checkCuda((int) CUDA_MEMCPY.invokeExact(d_p, d_r, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE));
                } else {
                    beta = (rho / rho_prev) * (alpha / omega);
                    daxpy_internal(n, -omega, d_v, d_p, tracker);
                    dscal_internal(n, beta, d_p, tracker);
                    daxpy_internal(n, 1.0, d_r, d_p, tracker);
                }

                spmv_internal(n, n, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_p, d_v, arena, tracker);
                alpha = rho / ddot_internal(n, d_r_hat, d_v, tracker);

                checkCuda((int) CUDA_MEMCPY.invokeExact(d_s, d_r, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE));
                daxpy_internal(n, -alpha, d_v, d_s, tracker);

                double s_norm = Math.sqrt(ddot_internal(n, d_s, d_s, tracker));
                if (s_norm < tol_val) {
                    daxpy_internal(n, alpha, d_p, d_x, tracker);
                    break;
                }

                spmv_internal(n, n, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_s, d_t, arena, tracker);
                omega = ddot_internal(n, d_t, d_s, tracker) / ddot_internal(n, d_t, d_t, tracker);

                daxpy_internal(n, alpha, d_p, d_x, tracker);
                daxpy_internal(n, omega, d_s, d_x, tracker);

                checkCuda((int) CUDA_MEMCPY.invokeExact(d_r, d_s, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE));
                daxpy_internal(n, -omega, d_t, d_r, tracker);

                if (Math.sqrt(ddot_internal(n, d_r, d_r, tracker)) < tol_val) break;
            }

            double[] xHost = new double[n];
            MemorySegment hostX = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostX, d_x, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostX, ValueLayout.JAVA_DOUBLE, 0, xHost, 0, n);

            return toVector(xHost, A_sparse.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("CUDA BiCGSTAB failed", t);
        }
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        NumericalConfiguration config = org.episteme.core.Episteme.getNumericalConfiguration();
        double tol_val = (tolerance != null) ? getRealValue(tolerance) : config.getEpsilonDouble();
        int maxIter = (maxIterations > 0) ? maxIterations : config.getMaxIterations();
        int n = b.dimension();
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> A_sparse = ensureSparse(a);

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_x = malloc((long) n * 8, tracker);
            MemorySegment d_r = malloc((long) n * 8, tracker);
            MemorySegment d_p = malloc((long) n * 8, tracker);
            MemorySegment d_Ap = malloc((long) n * 8, tracker);
            MemorySegment d_b = malloc((long) n * 8, tracker);

            int nnz_val = A_sparse.getNnz();
            MemorySegment d_csrRowPtr = malloc((long) (n + 1) * 4, tracker);
            MemorySegment d_csrColIdx = malloc((long) nnz_val * 4, tracker);
            MemorySegment d_csrVal = malloc((long) nnz_val * 8, tracker);

            checkCuda((int) CUDA_MEMCPY.invokeExact(d_b, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            if (x0 != null) {
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_x, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(x0)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            } else {
                checkCuda((int) CUDA_MEMSET.invokeExact(d_x, 0, (long) n * 8));
            }

            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrRowPtr, arena.allocateFrom(ValueLayout.JAVA_INT, A_sparse.getRowPointers()), (long) (n + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrColIdx, arena.allocateFrom(ValueLayout.JAVA_INT, A_sparse.getColIndices()), (long) nnz_val * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
            double[] valsArr = new double[nnz_val];
            Object[] valsObj = A_sparse.getValues();
            for (int i = 0; i < nnz_val; i++) valsArr[i] = getRealValue((E) valsObj[i]);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrVal, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, valsArr), (long) nnz_val * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

            spmv_internal(n, n, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_x, d_r, arena, tracker);
            daxpy_internal(n, -1.0, d_r, d_b, tracker);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_p, d_r, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE));

            double rsold = ddot_internal(n, d_r, d_r, tracker);

            for (int i = 0; i < maxIter; i++) {
                spmv_internal(n, n, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_p, d_Ap, arena, tracker);
                double alpha = rsold / ddot_internal(n, d_p, d_Ap, tracker);
                daxpy_internal(n, alpha, d_p, d_x, tracker);
                daxpy_internal(n, -alpha, d_Ap, d_r, tracker);

                double rsnew = ddot_internal(n, d_r, d_r, tracker);
                if (Math.sqrt(rsnew) < tol_val) break;
                dscal_internal(n, rsnew / rsold, d_p, tracker);
                daxpy_internal(n, 1.0, d_r, d_p, tracker);
                rsold = rsnew;
            }

            double[] xHost = new double[n];
            MemorySegment hostX = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostX, d_x, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostX, ValueLayout.JAVA_DOUBLE, 0, xHost, 0, n);

            return toVector(xHost, A_sparse.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("CUDA Conjugate Gradient failed", t);
        }
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
        NumericalConfiguration config = org.episteme.core.Episteme.getNumericalConfiguration();
        double tol_val = (tolerance != null) ? getRealValue(tolerance) : config.getEpsilonDouble();
        int maxIter = (maxIterations > 0) ? maxIterations : config.getMaxIterations();
        int m_rows = a.rows();
        int k_cols = a.cols();
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> A_sparse = ensureSparse(a);

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_x = malloc((long) m_rows * 8, tracker);
            if (x0 != null) {
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_x, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(x0)), (long) m_rows * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            } else {
                checkCuda((int) CUDA_MEMSET.invokeExact(d_x, 0, (long) m_rows * 8));
            }
            
            int nnz_val = A_sparse.getNnz();
            MemorySegment d_csrRowPtr = malloc((long) (m_rows + 1) * 4, tracker);
            MemorySegment d_csrColIdx = malloc((long) nnz_val * 4, tracker);
            MemorySegment d_csrVal = malloc((long) nnz_val * 8, tracker);
            
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrRowPtr, arena.allocateFrom(ValueLayout.JAVA_INT, A_sparse.getRowPointers()), (long) (m_rows + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrColIdx, arena.allocateFrom(ValueLayout.JAVA_INT, A_sparse.getColIndices()), (long) nnz_val * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
            double[] valsArr = new double[nnz_val];
            Object[] valsObj = A_sparse.getValues();
            for (int i = 0; i < nnz_val; i++) {
                valsArr[i] = getRealValue((E) valsObj[i]);
            }
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_csrVal, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, valsArr), (long) nnz_val * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

            for (int restart = 0; restart < restarts; restart++) {
                try (ResourceTracker restartTracker = new ResourceTracker()) {
                    // r = b - Ax
                    MemorySegment d_r = malloc((long) m_rows * 8, restartTracker);
                    spmv_internal(m_rows, k_cols, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_x, d_r, arena, restartTracker);
                    
                    double[] bArr = toDoubleArray(b);
                    MemorySegment d_b = malloc((long) m_rows * 8, restartTracker);
                    checkCuda((int) CUDA_MEMCPY.invokeExact(d_b, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bArr), (long) m_rows * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
                    daxpy_internal(m_rows, -1.0, d_r, d_b, restartTracker);
                    checkCuda((int) CUDA_MEMCPY.invokeExact(d_r, d_b, (long) m_rows * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE));

                    double beta = Math.sqrt(ddot_internal(m_rows, d_r, d_r, restartTracker));
                    if (beta < tol_val) break;

                    dscal_internal(m_rows, 1.0 / beta, d_r, restartTracker);
                    
                    MemorySegment[] V = new MemorySegment[maxIter + 1];
                    V[0] = d_r;

                    double[][] H = new double[maxIter + 1][maxIter];

                    int j = 0;
                    for (j = 0; j < maxIter; j++) {
                        V[j+1] = malloc((long) m_rows * 8, restartTracker);
                        spmv_internal(m_rows, k_cols, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, V[j], V[j+1], arena, restartTracker);

                        for (int i = 0; i <= j; i++) {
                            H[i][j] = ddot_internal(m_rows, V[i], V[j+1], restartTracker);
                            daxpy_internal(m_rows, -H[i][j], V[i], V[j+1], restartTracker);
                        }
                        H[j+1][j] = Math.sqrt(ddot_internal(m_rows, V[j+1], V[j+1], restartTracker));
                        if (H[j+1][j] < 1e-15) break; 
                        dscal_internal(m_rows, 1.0 / H[j+1][j], V[j+1], restartTracker);
                    }

                    double[] y = solveHessenbergSystem_internal(H, beta, j, A_sparse.getScalarRing());
                    for (int i = 0; i < y.length; i++) {
                        daxpy_internal(m_rows, y[i], V[i], d_x, restartTracker);
                    }
                    
                    // Final check for overall residual in this restart
                    MemorySegment d_check_r = malloc((long) m_rows * 8, restartTracker);
                    spmv_internal(m_rows, k_cols, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_x, d_check_r, arena, restartTracker);
                    MemorySegment d_check_b = malloc((long) m_rows * 8, restartTracker);
                    checkCuda((int) CUDA_MEMCPY.invokeExact(d_check_b, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bArr), (long) m_rows * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
                    daxpy_internal(m_rows, -1.0, d_check_r, d_check_b, restartTracker);
                    if (Math.sqrt(ddot_internal(m_rows, d_check_b, d_check_b, restartTracker)) < tol_val) break;
                }
            }

            double[] xHost = new double[m_rows];
            MemorySegment hostX = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m_rows);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostX, d_x, (long) m_rows * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostX, ValueLayout.JAVA_DOUBLE, 0, xHost, 0, m_rows);
            return toVector(xHost, A_sparse.getScalarRing());

        } catch (Throwable t) {
            throw new RuntimeException("CUDA GMRES failed", t);
        }
    }

    private double[] solveHessenbergSystem_internal(double[][] H, double beta, int size, Ring<E> ring) {
        // Hessenberg system is typically small, solve on CPU using standard MatrixSolver
        // We use Real for the internal Hessenberg solver as it's a real system for real GMRES
        Real[][] hMatrix = new Real[size + 1][size];
        Real[] g = new Real[size + 1];
        g[0] = Real.of(beta);
        for (int i = 1; i <= size; i++) g[i] = Real.ZERO;
        for (int i = 0; i <= size; i++) {
            for (int j = 0; j < size; j++) {
                hMatrix[i][j] = Real.of(H[i][j]);
            }
        }
        
        org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<Real> A = 
            new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(hMatrix, org.episteme.core.mathematics.sets.Reals.getInstance());
        Real[] solution = MatrixSolver.solve(A, g, MatrixSolver.Strategy.QR);
        
        double[] res = new double[size];
        for (int i = 0; i < size; i++) res[i] = solution[i].doubleValue();
        return res;
    }

    private double ddot_internal(int n, MemorySegment d_x, MemorySegment d_y, ResourceTracker tracker) throws Throwable {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment resPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_DDOT.invokeExact(handle, n, d_x, 1, d_y, 1, resPtr));
            return resPtr.get(ValueLayout.JAVA_DOUBLE, 0);
        }
    }

    private void daxpy_internal(int n, double alpha, MemorySegment d_x, MemorySegment d_y, ResourceTracker tracker) throws Throwable {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment alphaPtr = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, alpha);
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_DAXPY.invokeExact(handle, n, alphaPtr, d_x, 1, d_y, 1));
        }
    }

    private void dscal_internal(int n, double alpha, MemorySegment d_x, ResourceTracker tracker) throws Throwable {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment alphaPtr = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, alpha);
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_DSCAL.invokeExact(handle, n, alphaPtr, d_x, 1));
        }
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
        int m = a.rows();
        int n = a.cols();
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            double[] data = new double[m * n];
            for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) data[i * n + j] = getRealValue(a.get(i, j));
            
            MemorySegment d_in = malloc((long) m * n * 8, tracker);
            MemorySegment d_out = malloc((long) m * n * 8, tracker);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_in, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, data), (long) m * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);

            MemorySegment handle = createCublasHandle(tracker);
            // DGEAM can perform transpose: C = alpha * A^T + beta * B^T
            // Using transa=1 and transb=1 ensures dimensions match (m x n result from n x m input)
            checkCublas((int) CUBLAS_DGEAM.invokeExact(handle, 1, 1, m, n, alpha, 
                d_in, n, beta, d_in, n, d_out, m));

            double[] resData = new double[m * n];
            MemorySegment hostX = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostX, d_out, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostX, ValueLayout.JAVA_DOUBLE, 0, resData, 0, m * n);
            
            return toMatrix(resData, n, m, a.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("CUDA Transpose failed", t);
        }
    }


    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.GPU;
    }



    // --- Utilities ---

    // --- Utilities ---

    private MemorySegment malloc(long size, ResourceTracker tracker) {
        if (!IS_AVAILABLE) throw new IllegalStateException("CUDA not available");
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment ptr = temp.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUDA_MALLOC.invokeExact(ptr, size));
            MemorySegment d = ptr.get(ValueLayout.ADDRESS, 0);
            if (d.address() == 0) throw new RuntimeException("cudaMalloc returned NULL address");
            return tracker.track(d, p -> {
                try { checkCuda((int) CUDA_FREE.invokeExact(p)); } catch (Throwable t) { logger.error("Failed to free GPU memory: {}", t.getMessage()); }
            });
        } catch (Throwable t) {
            throw new RuntimeException("GPU Allocation failed for size " + size, t);
        }
    }


    @Override
    public long allocateGPUMemory(long size) {
        // Legacy fallback - not recommended
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment ptr = temp.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUDA_MALLOC.invokeExact(ptr, size));
            return ptr.get(ValueLayout.ADDRESS, 0).address();
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public void copyToGPU(long d_ptr, DoubleBuffer hostBuf, long sizeBytes) {
        if (d_ptr == 0) throw new IllegalArgumentException("Cannot copy to NULL GPU pointer");
        try {
            MemorySegment h_seg = MemorySegment.ofBuffer(hostBuf);
            checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_ptr), h_seg, sizeBytes, CUDA_MEMCPY_HOST_TO_DEVICE));
        } catch (Throwable t) {
            throw new RuntimeException("GPU Copy H2D failed", t);
        }
    }

    @Override
    public void copyFromGPU(long d_ptr, DoubleBuffer hostBuf, long sizeBytes) {
        if (d_ptr == 0) throw new IllegalArgumentException("Cannot copy from NULL GPU pointer");
        try {
            MemorySegment h_seg = MemorySegment.ofBuffer(hostBuf);
            checkCuda((int) CUDA_MEMCPY.invokeExact(h_seg, MemorySegment.ofAddress(d_ptr), sizeBytes, CUDA_MEMCPY_DEVICE_TO_HOST));
        } catch (Throwable t) {
            throw new RuntimeException("GPU Copy D2H failed", t);
        }
    }

    private static void checkCuda(int result) {
        if (result != 0) {
            if (CUDA_GET_ERROR_STRING == null) throw new RuntimeException("CUDA Error " + result);
            try {
                MemorySegment seg = (MemorySegment) CUDA_GET_ERROR_STRING.invokeExact(result);
                String msg = seg.reinterpret(1024).getString(0);
                logger.error("CUDA Error {}: {}", result, msg);
                throw new RuntimeException("CUDA Error " + result + ": " + msg);
            } catch (Throwable t) { throw new RuntimeException("CUDA Error " + result, t); }
        }
    }

    private static void checkCublas(int result) {
        if (result != 0) {
            throw new RuntimeException("cuBLAS Error " + result);
        }
    }

    private static void checkCusparse(int result) {
        if (result != 0) {
            throw new RuntimeException("cuSPARSE Error " + result);
        }
    }

    private double[] toDoubleArray(Vector<E> v) {
        double[] res = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) res[i] = getRealValue(v.get(i));
        return res;
    }

    private float[] toFloatArray(Vector<E> v) {
        float[] res = new float[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) res[i] = (float) getRealValue(v.get(i));
        return res;
    }

    private Vector<E> toVector(double[] data, Ring<E> ring) {
        int dim = data.length;
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(dim, ring.zero());
        for (int i = 0; i < dim; i++) {
            if (data[i] != 0.0) storage.set(i, castScalar(data[i], ring));
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, ring);
    }

    private Vector<E> toVector(float[] data, Ring<E> ring) {
        int dim = data.length;
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(dim, ring.zero());
        for (int i = 0; i < dim; i++) {
            if (data[i] != 0.0f) storage.set(i, (E) RealFloat.create(data[i]));
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, ring);
    }

    private E castScalar(double val, Ring<E> ring) {
        Object zero = ring.zero();
        if (zero instanceof RealFloat) return (E) RealFloat.create((float) val);
        if (zero instanceof RealDouble) return (E) RealDouble.of(val);
        return (E) Real.of(val);
    }

    private Matrix<E> toMatrix(double[] data, int rows, int cols, Ring<E> ring) {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, ring.zero());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double val = data[i * cols + j];
                if (val != 0.0) storage.set(i, j, castScalar(val, ring));
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, ring);
    }

    private Matrix<E> toMatrix(float[] data, int rows, int cols, Ring<E> ring) {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, ring.zero());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float val = data[i * cols + j];
                if (val != 0.0f) storage.set(i, j, (E) RealFloat.create(val));
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, ring);
    }

    private static Optional<SymbolLookup> findLibrary(String name, Arena arena) {
        String[] paths = {
            "/usr/local/cuda/lib64/lib" + name + ".so",
            "/usr/lib/x86_64-linux-gnu/lib" + name + ".so",
            "/usr/lib/lib" + name + ".so",
            name + ".dll" // Windows
        };
        
        for (String path : paths) {
            try {
                return Optional.of(SymbolLookup.libraryLookup(java.nio.file.Path.of(path), arena));
            } catch (Exception e) {
                // Try next
            }
        }
        
        // Fallback to NativeFFMLoader (which maps name to libname.so)
        return NativeFFMLoader.loadLibrary(name, arena);
    }

    @SuppressWarnings("unchecked")
    private org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> ensureSparse(Matrix<E> A) {
        if (A instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            return (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) A;
        }
        // Fallback: convert dense to sparse
        int rows = A.rows();
        int cols = A.cols();
        Ring<E> ring = A.getScalarRing();
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, ring.zero());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E val = A.get(i, j);
                if (val != null && !ring.zero().equals(val)) storage.set(i, j, val);
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, ring);
    }
    private double getRealValue(E val) {
        if (val == null) return 0.0;
        if (val instanceof Number n) return n.doubleValue();
        if (val instanceof Real r) return r.doubleValue();
        if (val instanceof Complex c) return c.real();
        try {
            return (double) val.getClass().getMethod("doubleValue").invoke(val);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Complex getComplexValue(E val) {
        if (val == null) return Complex.ZERO;
        if (val instanceof Complex c) return c;
        if (val instanceof Real r) return Complex.of(r.doubleValue());
        if (val instanceof Number n) return Complex.of(n.doubleValue());
        return Complex.of(getRealValue(val));
    }
}
