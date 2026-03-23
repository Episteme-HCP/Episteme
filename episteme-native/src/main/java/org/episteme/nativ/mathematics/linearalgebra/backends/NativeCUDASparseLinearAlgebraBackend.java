/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.DoubleBuffer;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.core.mathematics.sets.Reals;

import com.google.auto.service.AutoService;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;

import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.MatrixSolver;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.MatrixSolver.Strategy;

/**
 * Robust CUDA acceleration backend using Project Panama to interface with CUDA and CUBLAS.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, GPUBackend.class})
public class NativeCUDASparseLinearAlgebraBackend implements SparseLinearAlgebraProvider<Real>, NativeBackend, GPUBackend {
    
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
        return "CUDA Sparse Linear Algebra Backend";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public boolean isExplicitlyDisabled() {
        return "true".equalsIgnoreCase(System.getProperty("episteme.cuda.disabled"));
    }

    @Override
    public void freeGPUMemory(long handle) {
        if (handle == 0) return;
        try {
            NativeSafe.invoke(CUDA_FREE, MemorySegment.ofAddress(handle));
        } catch (Throwable t) {
            logger.error("Failed to free GPU memory: {}", t.getMessage());
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(NativeCUDASparseLinearAlgebraBackend.class);
    private static final Linker LINKER = Linker.nativeLinker();
    
    // CUDA Runtime / cuSPARSE State
    private static boolean IS_AVAILABLE = false;
    private static SymbolLookup cusparse_lookup;

    // CUDA Handles
    private static MethodHandle CUDA_MALLOC;
    private static MethodHandle CUDA_FREE;
    private static MethodHandle CUDA_MEMCPY;
    private static MethodHandle CUDA_DEVICE_SYNCHRONIZE;
    private static MethodHandle CU_CTX_GET_CURRENT;
    private static MethodHandle CU_CTX_GET_DEVICE;

    private static MethodHandle CUSPARSE_CREATE;
    private static MethodHandle CUSPARSE_CREATE_CSR;
    private static MethodHandle CUSPARSE_CREATE_DN_VEC;
    private static MethodHandle CUSPARSE_CREATE_DN_MAT;
    private static MethodHandle CUSPARSE_SPMV;
    private static MethodHandle CUSPARSE_SPMV_BUFFER_SIZE;
    private static MethodHandle CUSPARSE_SPMM;
    private static MethodHandle CUSPARSE_SPMM_BUFFER_SIZE;
    
    // cuSolver Handles
    private static SymbolLookup cusolver_lookup;

    // cuBLAS Handles
    private static MethodHandle CUBLAS_CREATE;
    private static MethodHandle CUBLAS_DESTROY;
    private static MethodHandle CUBLAS_DGEMM;
    private static MethodHandle CUBLAS_DDOT;
    private static MethodHandle CUBLAS_DAXPY;
    private static MethodHandle CUBLAS_DSCAL;
    private static MethodHandle CUBLAS_DGEAM;
 
    private static MemorySegment CUBLAS_HANDLE = MemorySegment.NULL;
    private static MemorySegment CUSPARSE_HANDLE = MemorySegment.NULL;

    // Constants
    private static final int CUSPARSE_INDEX_32BIT = 0;
    private static final int CUSPARSE_INDEX_BASE_ZERO = 0;
    private static final int CUDA_R_64F = 1; // Double
    private static final int CUSPARSE_ORDER_ROW = 2;
    private static final int CUSPARSE_SPMM_ALG_DEFAULT = 0;
    private static final int CUDA_MEMCPY_HOST_TO_DEVICE = 1;
    private static final int CUDA_MEMCPY_DEVICE_TO_HOST = 2;
    private static final int CUDA_MEMCPY_DEVICE_TO_DEVICE = 3;

    static {
        ensureInitialized();
    }


    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        return ring instanceof org.episteme.core.mathematics.sets.Reals;
    }

    private static MethodHandle lookup(SymbolLookup lookup, String name, FunctionDescriptor descriptor) {
        return NativeFFMLoader.findSymbol(lookup, name)
            .map(s -> LINKER.downcallHandle(s, descriptor))
            .orElse(null);
    }

    private static synchronized void ensureInitialized() {
        if (IS_AVAILABLE) return;

        try (Arena tempArena = Arena.ofConfined()) {
            // 1. Load CUDA Runtime
            Optional<SymbolLookup> cudaRtOpt = NativeFFMLoader.loadLibrary("cudart", Arena.global());
            if (cudaRtOpt.isEmpty()) {
                logger.warn("CUDA Runtime (cudart) not found. CUDA Sparse Backend disabled.");
                return;
            }
            SymbolLookup cudart = cudaRtOpt.get();

            // 2. cuSPARSE
            Optional<SymbolLookup> cusparseOpt = NativeFFMLoader.loadLibrary("cusparse", Arena.global());
            if (cusparseOpt.isEmpty()) {
                logger.warn("cuSPARSE library not found. CUDA Sparse Backend disabled.");
                return;
            }
            cusparse_lookup = cusparseOpt.get();

            // 3. cuBLAS
            Optional<SymbolLookup> cublasOpt = NativeFFMLoader.loadLibrary("cublas", Arena.global());
            if (cublasOpt.isEmpty()) {
                logger.warn("cuBLAS library not found. CUDA Sparse Backend disabled.");
                return;
            }
            SymbolLookup cublas = cublasOpt.get();

            // 4. cuSolver
            Optional<SymbolLookup> cusolverOpt = NativeFFMLoader.loadLibrary("cusolver", Arena.global());
            if (cusolverOpt.isPresent()) {
                cusolver_lookup = cusolverOpt.get();
            }

            // 5. Bind Handles
            CUDA_MALLOC = lookup(cudart, "cudaMalloc", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            CUDA_FREE = lookup(cudart, "cudaFree", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUDA_MEMCPY = lookup(cudart, "cudaMemcpy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
            CUDA_DEVICE_SYNCHRONIZE = lookup(cudart, "cudaDeviceSynchronize", FunctionDescriptor.of(ValueLayout.JAVA_INT));

            // Try loading CUDA driver
            Optional<SymbolLookup> cudaDrvOpt = NativeFFMLoader.loadLibrary("cuda", Arena.global());
            if (cudaDrvOpt.isEmpty()) cudaDrvOpt = NativeFFMLoader.loadLibrary("nvcuda", Arena.global());
            SymbolLookup cudaDrv = cudaDrvOpt.orElse(null);
            
            if (cudaDrv != null) {
                CU_CTX_GET_CURRENT = lookup(cudaDrv, "cuCtxGetCurrent", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                CU_CTX_GET_DEVICE = lookup(cudaDrv, "cuCtxGetDevice", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            }

            CUSPARSE_CREATE = lookup(cusparse_lookup, "cusparseCreate", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            CUSPARSE_CREATE_CSR = lookup(cusparse_lookup, "cusparseCreateCsr", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
            
            CUSPARSE_CREATE_DN_MAT = lookup(cusparse_lookup, "cusparseCreateDnMat", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

            CUSPARSE_SPMM_BUFFER_SIZE = lookup(cusparse_lookup, "cusparseSpMM_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMM = lookup(cusparse_lookup, "cusparseSpMM", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMV_BUFFER_SIZE = lookup(cusparse_lookup, "cusparseSpMV_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMV = lookup(cusparse_lookup, "cusparseSpMV", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_CREATE_DN_VEC = lookup(cusparse_lookup, "cusparseCreateDnVec", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

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


            // Global Handles
            MemorySegment chPtr = tempArena.allocate(ValueLayout.ADDRESS);
            NativeSafe.invoke(CUBLAS_CREATE, chPtr);
            CUBLAS_HANDLE = chPtr.get(ValueLayout.ADDRESS, 0).reinterpret(0);

            MemorySegment shPtr = tempArena.allocate(ValueLayout.ADDRESS);
            NativeSafe.invoke(CUSPARSE_CREATE, shPtr);
            CUSPARSE_HANDLE = shPtr.get(ValueLayout.ADDRESS, 0).reinterpret(0);


            IS_AVAILABLE = true;
            logger.info("Native CUDA Sparse Backend initialized successfully (Panama).");
        } catch (Throwable t) {
            IS_AVAILABLE = false; // Ensure it stays false
            logger.error("CUDA Sparse initialization failed: {}. Backend disabled.", t.getMessage(), t);
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
        try (Arena arena = Arena.ofConfined()) {
            long d_A = allocateGPUMemory((long) m * k * 8);
            long d_B = allocateGPUMemory((long) k * n * 8);
            long d_C = allocateGPUMemory((long) m * n * 8);

            copyToGPU(d_A, A, (long) m * k);
            copyToGPU(d_B, B, (long) k * n);

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);

            NativeSafe.invoke(CUBLAS_DGEMM, CUBLAS_HANDLE, 0, 0, n, m, k, alpha, 
                MemorySegment.ofAddress(d_B), n, 
                MemorySegment.ofAddress(d_A), k, 
                beta, 
                MemorySegment.ofAddress(d_C), n);

            copyFromGPU(d_C, C, (long) m * n);

            freeGPUMemory(d_A);
            freeGPUMemory(d_B);
            freeGPUMemory(d_C);
        } catch (Throwable t) {
            logger.error("cuBLAS DGEMM failed: {}", t.getMessage());
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
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            if (b.cols() == 1) {
                return multiplySparseVector((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a, b.getColumn(0)).toMatrix();
            }
            return multiplySparseDense((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a, b);
        }
        return multiplyDense(a, b);
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            return multiplySparseVector((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a, b);
        }
        return SparseLinearAlgebraProvider.super.multiply(a, b);
    }

    private Vector<Real> multiplySparseVector(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> a, Vector<Real> b) {
        int m = a.rows();
        int k = a.cols();
        int nnz = a.getNnz();

        try (Arena arena = Arena.ofConfined()) {
            int[] rowPtrHost = a.getRowPointers();
            int[] colIdxHost = a.getColIndices();
            double[] valsHost = new double[nnz];
            Object[] valsObj = a.getValues();
            for (int i = 0; i < nnz; i++) valsHost[i] = ((Real) valsObj[i]).doubleValue();

            long d_csrRowPtr = allocateGPUMemory((long)(m + 1) * 4);
            long d_csrColIdx = allocateGPUMemory((long)nnz * 4);
            long d_csrVal = allocateGPUMemory((long)nnz * 8);
            long d_x = allocateGPUMemory((long)k * 8);
            long d_y = allocateGPUMemory((long)m * 8);

            MemorySegment h_csrRowPtr = arena.allocateFrom(ValueLayout.JAVA_INT, rowPtrHost);
            MemorySegment h_csrColIdx = arena.allocateFrom(ValueLayout.JAVA_INT, colIdxHost);
            MemorySegment h_csrVal = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, valsHost);
            MemorySegment h_x = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));

            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrRowPtr), h_csrRowPtr, (long)(m + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrColIdx), h_csrColIdx, (long)nnz * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrVal), h_csrVal, (long)nnz * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_x), h_x, (long)k * 8, CUDA_MEMCPY_HOST_TO_DEVICE);

            spmv_internal(m, k, nnz, d_csrRowPtr, d_csrColIdx, d_csrVal, d_x, d_y, arena);

            double[] yHost = new double[m];
            copyFromGPU(d_y, DoubleBuffer.wrap(yHost), m);

            freeGPUMemory(d_csrRowPtr);
            freeGPUMemory(d_csrColIdx);
            freeGPUMemory(d_csrVal);
            freeGPUMemory(d_x);
            freeGPUMemory(d_y);

            return toRealVector(yHost);
        } catch (Throwable t) {
            throw new RuntimeException("CUDA Sparse-Vector multiply failed", t);
        }
    }

    private Matrix<Real> multiplySparseDense(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> a, Matrix<Real> b) {
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();
        int nnz = a.getNnz();

        try (Arena arena = Arena.ofConfined()) {
            long d_csrRowPtr = allocateGPUMemory((long)(m + 1) * 4);
            long d_csrColIdx = allocateGPUMemory((long)nnz * 4);
            long d_csrVal = allocateGPUMemory((long)nnz * 8);
            long d_B = allocateGPUMemory((long)k * n * 8);
            long d_C = allocateGPUMemory((long)m * n * 8);

            MemorySegment h_rowPtr = arena.allocateFrom(ValueLayout.JAVA_INT, a.getRowPointers());
            MemorySegment h_colIdx = arena.allocateFrom(ValueLayout.JAVA_INT, a.getColIndices());
            double[] vals = new double[nnz];
            Object[] valsObj = a.getValues();
            for (int i = 0; i < nnz; i++) vals[i] = ((Real) valsObj[i]).doubleValue();
            MemorySegment h_vals = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, vals);
            
            double[] bData = new double[k * n];
            for (int i = 0; i < k; i++) for (int j = 0; j < n; j++) bData[i * n + j] = b.get(i, j).doubleValue();
            MemorySegment h_B = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bData);

            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrRowPtr), h_rowPtr, (long)(m + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrColIdx), h_colIdx, (long)nnz * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrVal), h_vals, (long)nnz * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_B), h_B, (long)k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE);

            spmm_internal(m, n, k, nnz, d_csrRowPtr, d_csrColIdx, d_csrVal, d_B, d_C, arena);

            double[] cData = new double[m * n];
            copyFromGPU(d_C, DoubleBuffer.wrap(cData), m * n);

            freeGPUMemory(d_csrRowPtr);
            freeGPUMemory(d_csrColIdx);
            freeGPUMemory(d_csrVal);
            freeGPUMemory(d_B);
            freeGPUMemory(d_C);

            return toRealMatrix(cData, m, n);
        } catch (Throwable t) {
            throw new RuntimeException("CUDA Sparse-Dense multiply failed", t);
        }
    }

    private Matrix<Real> multiplyDense(Matrix<Real> a, Matrix<Real> b) {
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();
        DoubleBuffer bufferA = DoubleBuffer.allocate(m * k);
        DoubleBuffer bufferB = DoubleBuffer.allocate(k * n);
        DoubleBuffer bufferC = DoubleBuffer.allocate(m * n);
        for (int i = 0; i < m; i++) for (int j = 0; j < k; j++) bufferA.put(a.get(i, j).doubleValue());
        for (int i = 0; i < k; i++) for (int j = 0; j < n; j++) bufferB.put(b.get(i, j).doubleValue());
        bufferA.flip(); bufferB.flip();
        matrixMultiply(bufferA, bufferB, bufferC, m, n, k);
        double[] cData = new double[m * n];
        bufferC.get(cData);
        return toRealMatrix(cData, m, n);
    }

    private void spmv_internal(int m, int k, int nnz, long d_rowPtr, long d_colIdx, long d_val, long d_x, long d_y, Arena arena) throws Throwable {
        MemorySegment matAPtr = arena.allocate(ValueLayout.ADDRESS);
        NativeSafe.invoke(CUSPARSE_CREATE_CSR, matAPtr, (long)m, (long)k, (long)nnz, 
            MemorySegment.ofAddress(d_rowPtr), MemorySegment.ofAddress(d_colIdx), MemorySegment.ofAddress(d_val),
            CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, CUDA_R_64F);
        MemorySegment matA = matAPtr.get(ValueLayout.ADDRESS, 0).reinterpret(0);

        MemorySegment vechXPtr = arena.allocate(ValueLayout.ADDRESS);
        NativeSafe.invoke(CUSPARSE_CREATE_DN_VEC, vechXPtr, (long)k, MemorySegment.ofAddress(d_x), CUDA_R_64F);
        MemorySegment vecX = vechXPtr.get(ValueLayout.ADDRESS, 0).reinterpret(0);

        MemorySegment vechYPtr = arena.allocate(ValueLayout.ADDRESS);
        NativeSafe.invoke(CUSPARSE_CREATE_DN_VEC, vechYPtr, (long)m, MemorySegment.ofAddress(d_y), CUDA_R_64F);
        MemorySegment vecY = vechYPtr.get(ValueLayout.ADDRESS, 0).reinterpret(0);

        MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
        MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
        MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);

        NativeSafe.invoke(CUSPARSE_SPMV_BUFFER_SIZE, CUSPARSE_HANDLE, 0, alpha, matA, vecX, beta, vecY, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr);
        long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
        long d_buffer = allocateGPUMemory(bufferSize);

        NativeSafe.invoke(CUSPARSE_SPMV, CUSPARSE_HANDLE, 0, alpha, matA, vecX, beta, vecY, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, MemorySegment.ofAddress(d_buffer));

        freeGPUMemory(d_buffer);
    }

    private void spmm_internal(int m, int n, int k, int nnz, long d_rowPtr, long d_colIdx, long d_val, long d_B, long d_C, Arena arena) throws Throwable {
        MemorySegment matAPtr = arena.allocate(ValueLayout.ADDRESS);
        NativeSafe.invoke(CUSPARSE_CREATE_CSR, matAPtr, (long)m, (long)k, (long)nnz, MemorySegment.ofAddress(d_rowPtr), MemorySegment.ofAddress(d_colIdx), MemorySegment.ofAddress(d_val),
            CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, CUDA_R_64F);
        MemorySegment matA = matAPtr.get(ValueLayout.ADDRESS, 0).reinterpret(0);

        MemorySegment matBPtr = arena.allocate(ValueLayout.ADDRESS);
        NativeSafe.invoke(CUSPARSE_CREATE_DN_MAT, matBPtr, (long)k, (long)n, (long)n, MemorySegment.ofAddress(d_B), CUDA_R_64F, CUSPARSE_ORDER_ROW);
        MemorySegment matB = matBPtr.get(ValueLayout.ADDRESS, 0).reinterpret(0);

        MemorySegment matCPtr = arena.allocate(ValueLayout.ADDRESS);
        NativeSafe.invoke(CUSPARSE_CREATE_DN_MAT, matCPtr, (long)m, (long)n, (long)n, MemorySegment.ofAddress(d_C), CUDA_R_64F, CUSPARSE_ORDER_ROW);
        MemorySegment matC = matCPtr.get(ValueLayout.ADDRESS, 0).reinterpret(0);

        MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
        MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
        MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);

        NativeSafe.invoke(CUSPARSE_SPMM_BUFFER_SIZE, CUSPARSE_HANDLE, 0, 0, alpha, matA, matB, beta, matC, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr);
        long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
        long d_buffer = allocateGPUMemory(bufferSize);

        NativeSafe.invoke(CUSPARSE_SPMM, CUSPARSE_HANDLE, 0, 0, alpha, matA, matB, beta, matC, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, MemorySegment.ofAddress(d_buffer));

        freeGPUMemory(d_buffer);
    }

    @Override
    public Vector<Real> bicgstab(Matrix<Real> A, Vector<Real> b, Vector<Real> x0, Real tol, int maxIter) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
        int n = b.dimension();
        if (!(A instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix)) {
             throw new IllegalArgumentException("A must be a SparseMatrix");
        }
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> A_sparse = (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) A;

        try (Arena arena = Arena.ofConfined()) {
            double tol_val = tol.doubleValue();
            
            // Allocate GPU memory for vectors
            long d_r = allocateGPUMemory((long) n * 8);
            long d_r_hat = allocateGPUMemory((long) n * 8);
            long d_p = allocateGPUMemory((long) n * 8);
            long d_v = allocateGPUMemory((long) n * 8);
            long d_s = allocateGPUMemory((long) n * 8);
            long d_t = allocateGPUMemory((long) n * 8);
            long d_x = allocateGPUMemory((long) n * 8);
            long d_b = allocateGPUMemory((long) n * 8);

            // Matrix CSR data
            int nnz_val = A_sparse.getNnz();
            long d_csrRowPtr = allocateGPUMemory((long) (n + 1) * 4);
            long d_csrColIdx = allocateGPUMemory((long) nnz_val * 4);
            long d_csrVal = allocateGPUMemory((long) nnz_val * 8);

            // Copy data to GPU
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_b), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
            if (x0 != null) NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_x), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(x0)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
            else NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_x), MemorySegment.NULL, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE); 

            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrRowPtr), arena.allocateFrom(ValueLayout.JAVA_INT, A_sparse.getRowPointers()), (long) (n + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrColIdx), arena.allocateFrom(ValueLayout.JAVA_INT, A_sparse.getColIndices()), (long) nnz_val * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
            double[] vals = new double[nnz_val];
            Object[] valsObj = A_sparse.getValues();
            for (int i = 0; i < nnz_val; i++) vals[i] = ((Real) valsObj[i]).doubleValue();
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrVal), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, vals), (long) nnz_val * 8, CUDA_MEMCPY_HOST_TO_DEVICE);

            // BiCGSTAB initialization
            // r = b - Ax
            spmv_internal(n, n, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_x, d_r, arena);
            daxpy_internal(n, -1.0, d_r, d_b); // r = b - r (r was Ax)
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_r_hat), MemorySegment.ofAddress(d_r), (long) n * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE);

            double rho = 1.0, alpha = 1.0, omega = 1.0;
            double rho_prev, beta;

            for (int i = 0; i < maxIter; i++) {
                rho_prev = rho;
                rho = ddot_internal(n, d_r_hat, d_r);
                if (Math.abs(rho) < 1e-20) break;

                if (i == 0) {
                    NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_p), MemorySegment.ofAddress(d_r), (long) n * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE);
                } else {
                    beta = (rho / rho_prev) * (alpha / omega);
                    // p = r + beta * (p - omega * v)
                    daxpy_internal(n, -omega, d_v, d_p); // p = p - omega*v
                    dscal_internal(n, beta, d_p);        // p = beta*p
                    daxpy_internal(n, 1.0, d_r, d_p);    // p = r + p
                }

                spmv_internal(n, n, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_p, d_v, arena);
                alpha = rho / ddot_internal(n, d_r_hat, d_v);

                // s = r - alpha * v
                NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_s), MemorySegment.ofAddress(d_r), (long) n * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE);
                daxpy_internal(n, -alpha, d_v, d_s);

                // Check norm of s
                double s_norm = Math.sqrt(ddot_internal(n, d_s, d_s));
                if (s_norm < tol_val) {
                    daxpy_internal(n, alpha, d_p, d_x);
                    break;
                }

                spmv_internal(n, n, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_s, d_t, arena);
                omega = ddot_internal(n, d_t, d_s) / ddot_internal(n, d_t, d_t);

                // x = x + alpha * p + omega * s
                daxpy_internal(n, alpha, d_p, d_x);
                daxpy_internal(n, omega, d_s, d_x);

                // r = s - omega * t
                NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_r), MemorySegment.ofAddress(d_s), (long) n * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE);
                daxpy_internal(n, -omega, d_t, d_r);

                if (Math.sqrt(ddot_internal(n, d_r, d_r)) < tol_val) break;
            }

            double[] xHost = new double[n];
            copyFromGPU(d_x, DoubleBuffer.wrap(xHost), n);

            // Cleanup
            freeGPUMemory(d_r); freeGPUMemory(d_r_hat); freeGPUMemory(d_p); freeGPUMemory(d_v);
            freeGPUMemory(d_s); freeGPUMemory(d_t); freeGPUMemory(d_x); freeGPUMemory(d_b);
            freeGPUMemory(d_csrRowPtr); freeGPUMemory(d_csrColIdx); freeGPUMemory(d_csrVal);

            return toRealVector(xHost);
        } catch (Throwable t) {
            throw new RuntimeException("CUDA BiCGSTAB failed", t);
        }
    }

    @Override
    public Vector<Real> conjugateGradient(Matrix<Real> A, Vector<Real> b, Vector<Real> x0, Real tol, int maxIter) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
        int n = b.dimension();
        if (!(A instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix)) {
            throw new IllegalArgumentException("A must be a SparseMatrix");
        }
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> A_sparse = (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) A;

        try (Arena arena = Arena.ofConfined()) {
            double tol_val = tol.doubleValue();
            long d_x = allocateGPUMemory((long) n * 8);
            long d_r = allocateGPUMemory((long) n * 8);
            long d_p = allocateGPUMemory((long) n * 8);
            long d_Ap = allocateGPUMemory((long) n * 8);
            long d_b = allocateGPUMemory((long) n * 8);

            int nnz_val = A_sparse.getNnz();
            long d_csrRowPtr = allocateGPUMemory((long) (n + 1) * 4);
            long d_csrColIdx = allocateGPUMemory((long) nnz_val * 4);
            long d_csrVal = allocateGPUMemory((long) nnz_val * 8);

            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_b), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
            if (x0 != null) NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_x), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(x0)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
            else NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_x), MemorySegment.NULL, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE);

            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrRowPtr), arena.allocateFrom(ValueLayout.JAVA_INT, A_sparse.getRowPointers()), (long) (n + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrColIdx), arena.allocateFrom(ValueLayout.JAVA_INT, A_sparse.getColIndices()), (long) nnz_val * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
            double[] valsArr = new double[nnz_val];
            Object[] valsObj = A_sparse.getValues();
            for (int i = 0; i < nnz_val; i++) valsArr[i] = ((Real) valsObj[i]).doubleValue();
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrVal), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, valsArr), (long) nnz_val * 8, CUDA_MEMCPY_HOST_TO_DEVICE);

            // r = b - Ax
            spmv_internal(n, n, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_x, d_r, arena);
            daxpy_internal(n, -1.0, d_r, d_b); // r = b - r
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_p), MemorySegment.ofAddress(d_r), (long) n * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE);

            double rsold = ddot_internal(n, d_r, d_r);

            for (int i = 0; i < maxIter; i++) {
                spmv_internal(n, n, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_p, d_Ap, arena);
                double alpha = rsold / ddot_internal(n, d_p, d_Ap);
                daxpy_internal(n, alpha, d_p, d_x);
                daxpy_internal(n, -alpha, d_Ap, d_r);

                double rsnew = ddot_internal(n, d_r, d_r);
                if (Math.sqrt(rsnew) < tol_val) break;
                dscal_internal(n, rsnew / rsold, d_p);
                daxpy_internal(n, 1.0, d_r, d_p);
                rsold = rsnew;
            }

            double[] xHost = new double[n];
            copyFromGPU(d_x, DoubleBuffer.wrap(xHost), n);

            freeGPUMemory(d_x); freeGPUMemory(d_r); freeGPUMemory(d_p); freeGPUMemory(d_Ap); freeGPUMemory(d_b);
            freeGPUMemory(d_csrRowPtr); freeGPUMemory(d_csrColIdx); freeGPUMemory(d_csrVal);

            return toRealVector(xHost);
        } catch (Throwable t) {
            throw new RuntimeException("CUDA Conjugate Gradient failed", t);
        }
    }

    @Override
    public Vector<Real> gmres(Matrix<Real> A, Vector<Real> b, Vector<Real> x0, Real tol, int maxIter, int restarts) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
        int m_rows = A.rows();
        int k_cols = A.cols();
        if (!(A instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix)) {
            throw new IllegalArgumentException("A must be a SparseMatrix");
        }
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> A_sparse = (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) A;

        try (Arena arena = Arena.ofConfined()) {
            double tol_val = tol.doubleValue();
            long d_x = allocateGPUMemory((long) m_rows * 8);
            if (x0 != null) NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_x), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(x0)), (long) m_rows * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
            
            int nnz_val = A_sparse.getNnz();
            long d_csrRowPtr = allocateGPUMemory((long) (m_rows + 1) * 4);
            long d_csrColIdx = allocateGPUMemory((long) nnz_val * 4);
            long d_csrVal = allocateGPUMemory((long) nnz_val * 8);
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrRowPtr), arena.allocateFrom(ValueLayout.JAVA_INT, A_sparse.getRowPointers()), (long) (m_rows + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrColIdx), arena.allocateFrom(ValueLayout.JAVA_INT, A_sparse.getColIndices()), (long) nnz_val * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
            double[] valsArr = new double[nnz_val];
            Object[] valsObj = A_sparse.getValues();
            for (int i = 0; i < nnz_val; i++) valsArr[i] = ((Real) valsObj[i]).doubleValue();
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_csrVal), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, valsArr), (long) nnz_val * 8, CUDA_MEMCPY_HOST_TO_DEVICE);

            for (int restart = 0; restart < restarts; restart++) {
                // r = b - Ax
                long d_r = allocateGPUMemory((long) m_rows * 8);
                spmv_internal(m_rows, k_cols, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, d_x, d_r, arena);
                double[] bArr = toDoubleArray(b);
                long d_b = allocateGPUMemory((long) m_rows * 8);
                NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_b), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bArr), (long) m_rows * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
                daxpy_internal(m_rows, -1.0, d_r, d_b);
                NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_r), MemorySegment.ofAddress(d_b), (long) m_rows * 8, CUDA_MEMCPY_DEVICE_TO_DEVICE);

                double beta = Math.sqrt(ddot_internal(m_rows, d_r, d_r));
                if (beta < tol_val) {
                    freeGPUMemory(d_r); freeGPUMemory(d_b);
                    break;
                }

                dscal_internal(m_rows, 1.0 / beta, d_r);
                
                long[] V = new long[maxIter + 1];
                V[0] = d_r;
                double[][] H = new double[maxIter + 1][maxIter];

                int j = 0;
                for (j = 0; j < maxIter; j++) {
                    V[j+1] = allocateGPUMemory((long) m_rows * 8);
                    spmv_internal(m_rows, k_cols, nnz_val, d_csrRowPtr, d_csrColIdx, d_csrVal, V[j], V[j+1], arena);

                    for (int i = 0; i <= j; i++) {
                        H[i][j] = ddot_internal(m_rows, V[i], V[j+1]);
                        daxpy_internal(m_rows, -H[i][j], V[i], V[j+1]);
                    }
                    H[j+1][j] = Math.sqrt(ddot_internal(m_rows, V[j+1], V[j+1]));
                    if (H[j+1][j] < 1e-15) break; 
                    dscal_internal(m_rows, 1.0 / H[j+1][j], V[j+1]);
                }

                // Solve the small Hessenberg system on CPU
                double[] y = solveHessenbergSystem_internal(H, beta, j);
                for (int i = 0; i < y.length; i++) {
                    daxpy_internal(m_rows, y[i], V[i], d_x);
                }

                // Free V vectors and d_b
                for (int i = 0; i <= j; i++) if (V[i] != 0) freeGPUMemory(V[i]);
                freeGPUMemory(d_b);
            }

            double[] xHost = new double[m_rows];
            copyFromGPU(d_x, DoubleBuffer.wrap(xHost), m_rows);
            freeGPUMemory(d_x);
            freeGPUMemory(d_csrRowPtr); freeGPUMemory(d_csrColIdx); freeGPUMemory(d_csrVal);

            return toRealVector(xHost);
        } catch (Throwable t) {
            throw new RuntimeException("CUDA GMRES failed", t);
        }
    }

    private double[] solveHessenbergSystem_internal(double[][] H, double beta, int size) {
        Real[][] hMatrix = new Real[size + 1][size];
        Real[] g = new Real[size + 1];
        g[0] = Real.of(beta);
        for (int i = 1; i <= size; i++) g[i] = Real.ZERO;
        for (int i = 0; i <= size; i++) {
            for (int j = 0; j < size; j++) {
                hMatrix[i][j] = Real.of(H[i][j]);
            }
        }
        
        Matrix<Real> A = new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(hMatrix, Reals.getInstance());
        Real[] solution = MatrixSolver.solve(A, g, Strategy.QR);
        
        double[] res = new double[size];
        for (int i = 0; i < size; i++) res[i] = solution[i].doubleValue();
        return res;
    }

    private double ddot_internal(int n, long d_x, long d_y) throws Throwable {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment resPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
            NativeSafe.invoke(CUBLAS_DDOT, CUBLAS_HANDLE, n, MemorySegment.ofAddress(d_x), 1, MemorySegment.ofAddress(d_y), 1, resPtr);
            return resPtr.get(ValueLayout.JAVA_DOUBLE, 0);
        }
    }

    private void daxpy_internal(int n, double alpha, long d_x, long d_y) throws Throwable {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment alphaPtr = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, alpha);
            NativeSafe.invoke(CUBLAS_DAXPY, CUBLAS_HANDLE, n, alphaPtr, MemorySegment.ofAddress(d_x), 1, MemorySegment.ofAddress(d_y), 1);
        }
    }

    private void dscal_internal(int n, double alpha, long d_x) throws Throwable {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment alphaPtr = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, alpha);
            NativeSafe.invoke(CUBLAS_DSCAL, CUBLAS_HANDLE, n, alphaPtr, MemorySegment.ofAddress(d_x), 1);
        }
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
        int m = a.rows();
        int n = a.cols();
        
        try (Arena arena = Arena.ofConfined()) {
            double[] data = new double[m * n];
            for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) data[i * n + j] = a.get(i, j).doubleValue();
            
            long d_in = allocateGPUMemory((long) m * n * 8);
            long d_out = allocateGPUMemory((long) m * n * 8);
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_in), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, data), (long) m * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE);

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);

            // DGEAM can perform transpose: C = alpha * A^T + beta * B^T
            NativeSafe.invoke(CUBLAS_DGEAM, CUBLAS_HANDLE, 1, 0, m, n, alpha, 
                MemorySegment.ofAddress(d_in), n, beta, MemorySegment.ofAddress(d_in), n, 
                MemorySegment.ofAddress(d_out), m);

            double[] resData = new double[m * n];
            copyFromGPU(d_out, DoubleBuffer.wrap(resData), m * n);
            
            freeGPUMemory(d_in);
            freeGPUMemory(d_out);
            
            return toRealMatrix(resData, n, m);
        } catch (Throwable t) {
            throw new RuntimeException("CUDA Transpose failed", t);
        }
    }

    @Override
    public Real determinant(Matrix<Real> a) {
        throw new UnsupportedOperationException("Native CUDA Determinant implementation pending");
    }

    @Override
    public void shutdown() {}

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.GPU;
    }



    // --- Utilities ---

    @Override
    public long allocateGPUMemory(long size) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment ptr = temp.allocate(ValueLayout.ADDRESS);
            NativeSafe.invoke(CUDA_MALLOC, ptr, size);
            return ptr.get(ValueLayout.ADDRESS, 0).address();
        } catch (Throwable t) {
            throw new RuntimeException("GPU Allocation failed", t);
        }
    }

    @Override
    public void copyToGPU(long d_ptr, DoubleBuffer hostBuf, long sizeBytes) {
        try {
            MemorySegment h_seg = MemorySegment.ofBuffer(hostBuf);
            NativeSafe.invoke(CUDA_MEMCPY, MemorySegment.ofAddress(d_ptr), h_seg, sizeBytes, CUDA_MEMCPY_HOST_TO_DEVICE);
        } catch (Throwable t) {
            throw new RuntimeException("GPU Copy H2D failed", t);
        }
    }

    @Override
    public void copyFromGPU(long d_ptr, DoubleBuffer hostBuf, long sizeBytes) {
        try {
            MemorySegment h_seg = MemorySegment.ofBuffer(hostBuf);
            NativeSafe.invoke(CUDA_MEMCPY, h_seg, MemorySegment.ofAddress(d_ptr), sizeBytes, CUDA_MEMCPY_DEVICE_TO_HOST);
        } catch (Throwable t) {
            throw new RuntimeException("GPU Copy D2H failed", t);
        }
    }

    private double[] toDoubleArray(Vector<Real> v) {
        double[] res = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) res[i] = v.get(i).doubleValue();
        return res;
    }

    private Vector<Real> toRealVector(double[] data) {
        int dim = data.length;
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(dim, Real.ZERO);
        for (int i = 0; i < dim; i++) {
            if (data[i] != 0.0) storage.set(i, Real.of(data[i]));
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, Reals.getInstance());
    }

    private Matrix<Real> toRealMatrix(double[] data, int rows, int cols) {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, Real.ZERO);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double val = data[i * cols + j];
                if (val != 0.0) storage.set(i, j, Real.of(val));
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, Reals.getInstance());
    }
}
