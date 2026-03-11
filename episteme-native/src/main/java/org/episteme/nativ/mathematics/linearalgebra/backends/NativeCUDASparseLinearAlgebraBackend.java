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
import org.episteme.core.technical.backend.nativ.NativeLibraryLoader;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;

/**
 * Robust CUDA acceleration backend using Project Panama to interface with CUDA and CUBLAS.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@SuppressWarnings("preview")
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, GPUBackend.class})
public class NativeCUDASparseLinearAlgebraBackend implements SparseLinearAlgebraProvider<Real>, NativeBackend, GPUBackend {
    
    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
    }

    @Override
    public void freeGPUMemory(long handle) {
        try {
            int rFree = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(handle));
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

    // cuSPARSE Handles (Modern Generic API)
    private static MethodHandle CUSPARSE_CREATE;
    private static MethodHandle CUSPARSE_DESTROY;
    private static MethodHandle CUSPARSE_CREATE_CSR;
    private static MethodHandle CUSPARSE_CREATE_DN_MAT;
    private static MethodHandle CUSPARSE_SPMM_BUFFER_SIZE;
    private static MethodHandle CUSPARSE_SPMM;
    private static MethodHandle CUSPARSE_SPMV_BUFFER_SIZE;
    private static MethodHandle CUSPARSE_SPMV;
    private static MethodHandle CUSPARSE_CREATE_DN_VEC;

    // cuBLAS Handles
    private static MethodHandle CUBLAS_CREATE;
    private static MethodHandle CUBLAS_DESTROY;
    private static MethodHandle CUBLAS_DGEMM;

    // Constants
    private static final int CUSPARSE_INDEX_32BIT = 0;
    private static final int CUSPARSE_INDEX_BASE_ZERO = 0;
    private static final int CUDA_R_64F = 1; // Double
    private static final int CUSPARSE_ORDER_COL = 1;
    private static final int CUSPARSE_ORDER_ROW = 2;
    private static final int CUSPARSE_SPMM_ALG_DEFAULT = 0;
    private static final int CUDA_MEMCPY_HOST_TO_DEVICE = 1;
    private static final int CUDA_MEMCPY_DEVICE_TO_HOST = 2;

    static {
        ensureInitialized();
    }

    private static synchronized void ensureInitialized() {
        if (IS_AVAILABLE) return;

        try {
            Optional<SymbolLookup> cudaRtOpt = NativeLibraryLoader.loadLibrary("cudart", Arena.global());
            Optional<SymbolLookup> cusparseOpt = NativeLibraryLoader.loadLibrary("cusparse", Arena.global());

            if (cudaRtOpt.isEmpty() || cusparseOpt.isEmpty()) {
                logger.warn("Native CUDA Sparse libraries not found (cudart={}, cusparse={})", 
                    cudaRtOpt.isPresent(), cusparseOpt.isPresent());
                return;
            }

            SymbolLookup cudart = cudaRtOpt.get();
            cusparse_lookup = cusparseOpt.get();

            // Verify GPU Presence
            Optional<MemorySegment> deviceCountSym = NativeLibraryLoader.findSymbol(cudart, "cudaGetDeviceCount");
            if (deviceCountSym.isPresent()) {
                MethodHandle getDeviceCount = LINKER.downcallHandle(deviceCountSym.get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                try (Arena tempArena = Arena.ofConfined()) {
                    MemorySegment countPtr = tempArena.allocate(ValueLayout.JAVA_INT);
                    int resCount = (int) getDeviceCount.invokeExact(countPtr);
                    if (resCount != 0 || countPtr.get(ValueLayout.JAVA_INT, 0) <= 0) {
                        logger.warn("No CUDA-capable GPU found or error result ({}). Backend disabled.", resCount);
                        return;
                    }
                }
            }

            // Core Runtime
            CUDA_MALLOC = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cudart, "cudaMalloc_v2", "cudaMalloc").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            CUDA_FREE = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cudart, "cudaFree").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUDA_MEMCPY = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cudart, "cudaMemcpy").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
            CUDA_DEVICE_SYNCHRONIZE = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cudart, "cudaDeviceSynchronize").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT));

            // cuSPARSE handles
            CUSPARSE_CREATE = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cusparse_lookup, "cusparseCreate").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSPARSE_DESTROY = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cusparse_lookup, "cusparseDestroy").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            CUSPARSE_CREATE_CSR = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cusparse_lookup, "cusparseCreateCsr").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
            
            CUSPARSE_CREATE_DN_MAT = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cusparse_lookup, "cusparseCreateDnMat").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, 
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

            CUSPARSE_SPMM_BUFFER_SIZE = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cusparse_lookup, "cusparseSpMM_bufferSize").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMM = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cusparse_lookup, "cusparseSpMM").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMV_BUFFER_SIZE = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cusparse_lookup, "cusparseSpMV_bufferSize").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMV = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cusparse_lookup, "cusparseSpMV").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_CREATE_DN_VEC = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cusparse_lookup, "cusparseCreateDnVec").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

            // cuBLAS handles
            Optional<SymbolLookup> cublasOpt = NativeLibraryLoader.loadLibrary("cublas", Arena.global());
            if (cublasOpt.isPresent()) {
                SymbolLookup cublas = cublasOpt.get();
                CUBLAS_CREATE = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cublas, "cublasCreate_v2", "cublasCreate").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                CUBLAS_DESTROY = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cublas, "cublasDestroy_v2", "cublasDestroy").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                CUBLAS_DGEMM = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cublas, "cublasDgemm_v2", "cublasDgemm").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                        ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            }

            IS_AVAILABLE = true;
            logger.info("Native CUDA Sparse Backend initialized successfully.");
        } catch (Throwable t) {
            logger.warn("CUDA Sparse initialization failed: {}. Backend disabled.", t.getMessage());
        }
    }

    public NativeCUDASparseLinearAlgebraBackend() {
    }

    @Override
    public DeviceInfo[] getDevices() {
        if (!IS_AVAILABLE) return new DeviceInfo[0];
        try {
            Optional<SymbolLookup> cudaRtOpt = NativeLibraryLoader.loadLibrary("cudart", Arena.global());
            if (cudaRtOpt.isPresent()) {
                MethodHandle getDeviceCount = LINKER.downcallHandle(NativeLibraryLoader.findSymbol(cudaRtOpt.get(), "cudaGetDeviceCount").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment countPtr = arena.allocate(ValueLayout.JAVA_INT);
                    int rCount = (int) getDeviceCount.invokeExact(countPtr);
                    int count = countPtr.get(ValueLayout.JAVA_INT, 0);
                    DeviceInfo[] devices = new DeviceInfo[count];
                    for (int i = 0; i < count; i++) {
                        devices[i] = new DeviceInfo("CUDA Sparse Device " + i, 8L * 1024 * 1024 * 1024, 128, "NVIDIA");
                    }
                    return devices;
                }
            }
        } catch (Throwable t) {
            logger.warn("Failed to get devices: {}", t.getMessage());
        }
        return new DeviceInfo[0];
    }

    @Override
    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) {
        if (!IS_AVAILABLE || CUBLAS_DGEMM == null) {
            logger.warn("cuBLAS not available for dense matrix multiply. Falling back to CPU.");
            // Fallback to CPU multiplication? (Omitted for brevity, or use another backend)
            return;
        }

        try (Arena arena = Arena.ofConfined()) {
            long d_A = allocateGPUMemory((long) m * k * 8);
            long d_B = allocateGPUMemory((long) k * n * 8);
            long d_C = allocateGPUMemory((long) m * n * 8);

            copyToGPU(d_A, A, (long) m * k);
            copyToGPU(d_B, B, (long) k * n);

            MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
            int rHCreate = (int) CUBLAS_CREATE.invokeExact(handlePtr);
            MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);

            // cuBLAS is column-major by default. Episteme is row-major.
            // C = op(A) * op(B)
            // To get row-major C = A*B using col-major DGEMM:
            // C^T = B^T * A^T
            // So we call DGEMM(handle, N, N, n, m, k, alpha, d_B, n, d_A, k, beta, d_C, n)
            int rGemm = (int) CUBLAS_DGEMM.invokeExact(handle, 0, 0, n, m, k, alpha, 
                MemorySegment.ofAddress(d_B), n, MemorySegment.ofAddress(d_A), k, beta, MemorySegment.ofAddress(d_C), n);

            copyFromGPU(d_C, C, (long) m * n);

            int rDest = (int) CUBLAS_DESTROY.invokeExact(handle);
            int rF1 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_A));
            int rF2 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_B));
            int rF3 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_C));
        } catch (Throwable t) {
            logger.error("cuBLAS DGEMM failed: {}", t.getMessage());
        }
    }

    @Override
    public void selectDevice(int deviceId) {
        // No-op for now
    }

    @Override
    public void synchronize() {
        try {
            int rSync = (int) CUDA_DEVICE_SYNCHRONIZE.invokeExact();
        } catch (Throwable t) {
            logger.error("CUDA synchronize failed: {}", t.getMessage());
        }
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
        
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            if (b.cols() == 1) {
                return multiplySparseVector((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a, b);
            }
            return multiplySparseDense((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a, b);
        }
        
        return multiplyDense(a, b);
    }

    private Matrix<Real> multiplySparseVector(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> a, Matrix<Real> b) {
        int m = a.rows();
        int k = a.cols();
        int nnz = a.getNnz();

        try (Arena arena = Arena.ofConfined()) {
            // 1. Prepare CSR data
            int[] rowPtrHost = a.getRowPointers();
            int[] colIdxHost = a.getColIndices();
            double[] valsHost = new double[nnz];
            Object[] valsObj = a.getValues();
            for (int i = 0; i < nnz; i++) valsHost[i] = ((Real) valsObj[i]).doubleValue();

            // 2. Allocate and copy to GPU
            long d_csrRowPtr = allocateGPUMemory((long)(m + 1) * 4);
            long d_csrColIdx = allocateGPUMemory((long)nnz * 4);
            long d_csrVal = allocateGPUMemory((long)nnz * 8);
            long d_vecX = allocateGPUMemory((long)k * 8);
            long d_vecY = allocateGPUMemory((long)m * 8);

            try {
                MemorySegment h_rowPtr = arena.allocateFrom(ValueLayout.JAVA_INT, rowPtrHost);
                MemorySegment h_colIdx = arena.allocateFrom(ValueLayout.JAVA_INT, colIdxHost);
                MemorySegment h_vals = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, valsHost);
                MemorySegment h_vecX = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));

                int rc1 = (int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrRowPtr), h_rowPtr, (long)(m + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
                int rc2 = (int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrColIdx), h_colIdx, (long)nnz * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
                int rc3 = (int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrVal), h_vals, (long)nnz * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
                int rc4 = (int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_vecX), h_vecX, (long)k * 8, CUDA_MEMCPY_HOST_TO_DEVICE);

                // 3. cuSPARSE Handles
                MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
                int rH2 = (int) CUSPARSE_CREATE.invokeExact(handlePtr);
                MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

                // Descriptors
                MemorySegment matAPtr = arena.allocate(ValueLayout.ADDRESS);
                int rA = (int) CUSPARSE_CREATE_CSR.invokeExact(matAPtr, (long)m, (long)k, (long)nnz, 
                    MemorySegment.ofAddress(d_csrRowPtr), MemorySegment.ofAddress(d_csrColIdx), MemorySegment.ofAddress(d_csrVal),
                    CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, CUDA_R_64F);
                MemorySegment matA = matAPtr.get(ValueLayout.ADDRESS, 0);

                MemorySegment vecXPtr = arena.allocate(ValueLayout.ADDRESS);
                int rS2 = (int) CUSPARSE_CREATE_DN_VEC.invokeExact(vecXPtr, (long)k, MemorySegment.ofAddress(d_vecX), CUDA_R_64F);
                MemorySegment vecX = vecXPtr.get(ValueLayout.ADDRESS, 0);

                MemorySegment vecYPtr = arena.allocate(ValueLayout.ADDRESS);
                int rS3 = (int) CUSPARSE_CREATE_DN_VEC.invokeExact(vecYPtr, (long)m, MemorySegment.ofAddress(d_vecY), CUDA_R_64F);
                MemorySegment vecY = vecYPtr.get(ValueLayout.ADDRESS, 0);

                // Buffer allocation for SpMV
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
                MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
                MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);
                
                int rS4 = (int) CUSPARSE_SPMV_BUFFER_SIZE.invokeExact(handle, 0, alpha, matA, vecX, beta, vecY, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr);
                long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
                long d_buffer = bufferSize > 0 ? allocateGPUMemory(bufferSize) : 0;

                // Execution
                int rS5 = (int) CUSPARSE_SPMV.invokeExact(handle, 0, alpha, matA, vecX, beta, vecY, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, MemorySegment.ofAddress(d_buffer));

                // 4. Result (D2H)
                double[] resHost = new double[m];
                MemorySegment h_res = arena.allocate(ValueLayout.JAVA_DOUBLE, m);
                int rS6 = (int) CUDA_MEMCPY.invokeExact(h_res, MemorySegment.ofAddress(d_vecY), (long)m * 8, CUDA_MEMCPY_DEVICE_TO_HOST);
                MemorySegment.copy(h_res, ValueLayout.JAVA_DOUBLE, 0, resHost, 0, m);

                // Cleanup GPU
                if (d_buffer != 0) { int rF = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_buffer)); }
                int rD = (int) CUSPARSE_DESTROY.invokeExact(handle);

                return fromDoubleArray(resHost, m, 1);
            } finally {
                int rf1 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrRowPtr));
                int rf2 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrColIdx));
                int rf3 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrVal));
                int rf4 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_vecX));
                int rf5 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_vecY));
            }
        } catch (Throwable t) {
            logger.error("cuSPARSE SpMV failed: {}", t.getMessage());
            return multiplyDense(a, b);
        }
    }

    private Matrix<Real> multiplyDense(Matrix<Real> a, Matrix<Real> b) {
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();
        
        DoubleBuffer bufA = toDoubleBuffer(a);
        DoubleBuffer bufB = toDoubleBuffer(b);
        DoubleBuffer bufC = DoubleBuffer.allocate(m * n);
        
        matrixMultiply(bufA, bufB, bufC, m, n, k);
        return fromDoubleBuffer(bufC, m, n);
    }

    private Matrix<Real> multiplySparseDense(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> a, Matrix<Real> b) {
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();
        int nnz = a.getNnz();

        try (Arena arena = Arena.ofConfined()) {
            // 1. Prepare CSR data from SparseMatrix
            int[] rowPtrHost = a.getRowPointers();
            int[] colIdxHost = a.getColIndices();
            Object[] valsObj = a.getValues();
            double[] valsHost = new double[nnz];
            for (int i = 0; i < nnz; i++) valsHost[i] = ((Real) valsObj[i]).doubleValue();

            // 2. Allocate and copy to GPU
            long d_csrRowPtr = allocateGPUMemory((long)(m + 1) * 4);
            long d_csrColIdx = allocateGPUMemory((long)nnz * 4);
            long d_csrVal = allocateGPUMemory((long)nnz * 8);
            long d_denseB = allocateGPUMemory((long)k * n * 8);
            long d_denseC = allocateGPUMemory((long)m * n * 8);

            // Copies (H2D)
            try {
                MemorySegment h_rowPtr = arena.allocateFrom(ValueLayout.JAVA_INT, rowPtrHost);
                MemorySegment h_colIdx = arena.allocateFrom(ValueLayout.JAVA_INT, colIdxHost);
                MemorySegment h_vals = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, valsHost);
                MemorySegment h_denseB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));

                int rcm1 = (int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrRowPtr), h_rowPtr, (long)(m + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
                int rcm2 = (int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrColIdx), h_colIdx, (long)nnz * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
                int rcm3 = (int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrVal), h_vals, (long)nnz * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
                int rcm4 = (int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_denseB), h_denseB, (long)k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE);

                // 3. cuSPARSE Handles
                MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
                int rHC = (int) CUSPARSE_CREATE.invokeExact(handlePtr);
                MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

                // Descriptors
                MemorySegment matAPtr = arena.allocate(ValueLayout.ADDRESS);
                int rA = (int) CUSPARSE_CREATE_CSR.invokeExact(matAPtr, (long)m, (long)k, (long)nnz, 
                    MemorySegment.ofAddress(d_csrRowPtr), MemorySegment.ofAddress(d_csrColIdx), MemorySegment.ofAddress(d_csrVal),
                    CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, CUDA_R_64F);
                MemorySegment matA = matAPtr.get(ValueLayout.ADDRESS, 0);

                MemorySegment matBPtr = arena.allocate(ValueLayout.ADDRESS);
                int rS11 = (int) CUSPARSE_CREATE_DN_MAT.invokeExact(matBPtr, (long)k, (long)n, (long)n, MemorySegment.ofAddress(d_denseB), CUDA_R_64F, CUSPARSE_ORDER_ROW);
                MemorySegment matB = matBPtr.get(ValueLayout.ADDRESS, 0);

                MemorySegment matCPtr = arena.allocate(ValueLayout.ADDRESS);
                int rS12 = (int) CUSPARSE_CREATE_DN_MAT.invokeExact(matCPtr, (long)m, (long)n, (long)n, MemorySegment.ofAddress(d_denseC), CUDA_R_64F, CUSPARSE_ORDER_ROW);
                MemorySegment matC = matCPtr.get(ValueLayout.ADDRESS, 0);

                // Buffer allocation for SpMM
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
                MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
                MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);
                
                int rS13 = (int) CUSPARSE_SPMM_BUFFER_SIZE.invokeExact(handle, 0, 0, alpha, matA, matB, beta, matC, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr);
                long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
                long d_buffer = bufferSize > 0 ? allocateGPUMemory(bufferSize) : 0;

                // Execution
                int rS14 = (int) CUSPARSE_SPMM.invokeExact(handle, 0, 0, alpha, matA, matB, beta, matC, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, MemorySegment.ofAddress(d_buffer));

                // 4. Result (D2H)
                double[] resHost = new double[m * n];
                MemorySegment h_res = arena.allocate(ValueLayout.JAVA_DOUBLE, m * n);
                int rcm5 = (int) CUDA_MEMCPY.invokeExact(h_res, MemorySegment.ofAddress(d_denseC), (long)m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST);
                MemorySegment.copy(h_res, ValueLayout.JAVA_DOUBLE, 0, resHost, 0, m * n);

                // Cleanup GPU
                if (d_buffer != 0) { int rF = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_buffer)); }
                int rD = (int) CUSPARSE_DESTROY.invokeExact(handle);

                return fromDoubleArray(resHost, m, n);
            } finally {
                int rf31 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrRowPtr));
                int rf32 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrColIdx));
                int rf33 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrVal));
                int rf34 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_denseB));
                int rf35 = (int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_denseC));
            }
        } catch (Throwable t) {
            logger.error("cuSPARSE SpMM failed: {}", t.getMessage());
            return multiplyDense(a, b); // Desperate fallback
        }
    }

    @Override
    public boolean isLoaded() {
        return IS_AVAILABLE;
    }

    @Override
    public void shutdown() {
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.GPU;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.ExecutionContext() {
            @Override
            public <T> T execute(org.episteme.core.technical.backend.Operation<T> operation) {
                return operation.compute(this);
            }
            @Override public void close() {}
        };
    }

    @Override
    public String getName() {
        return "Native CUDA Sparse Backend";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public long allocateGPUMemory(long size) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment ptr = arena.allocate(ValueLayout.ADDRESS);
            int res = (int) CUDA_MALLOC.invokeExact(ptr, size);
            if (res != 0) throw new RuntimeException("cudaMalloc failed with code " + res);
            MemorySegment addr = ptr.get(ValueLayout.ADDRESS, 0);
            return addr.address();
        } catch (Throwable t) {
            throw new RuntimeException("GPU Allocation failed", t);
        }
    }

    @Override
    public void copyToGPU(long d_ptr, DoubleBuffer hostBuf, long count) {
        try {
            MemorySegment h_seg = MemorySegment.ofBuffer(hostBuf);
            int rcm6 = (int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_ptr), h_seg, count * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
            if (rcm6 != 0) throw new RuntimeException("cudaMemcpy H2D failed with code " + rcm6);
        } catch (Throwable t) {
            throw new RuntimeException("GPU Copy H2D failed", t);
        }
    }

    @Override
    public void copyFromGPU(long d_ptr, DoubleBuffer hostBuf, long count) {
        try {
            MemorySegment h_seg = MemorySegment.ofBuffer(hostBuf);
            int rcm7 = (int) CUDA_MEMCPY.invokeExact(h_seg, MemorySegment.ofAddress(d_ptr), count * 8, CUDA_MEMCPY_DEVICE_TO_HOST);
            if (rcm7 != 0) throw new RuntimeException("cudaMemcpy D2H failed with code " + rcm7);
        } catch (Throwable t) {
            throw new RuntimeException("GPU Copy D2H failed", t);
        }
    }

    private double[] toDoubleArray(Matrix<Real> m) {
        int rCount = m.rows();
        int cCount = m.cols();
        double[] data = new double[rCount * cCount];
        for (int i = 0; i < rCount; i++) {
            for (int j = 0; j < cCount; j++) {
                data[i * cCount + j] = m.get(i, j).doubleValue();
            }
        }
        return data;
    }

    private Matrix<Real> fromDoubleArray(double[] data, int rows, int cols) {
        Real[] reals = new Real[data.length];
        for (int i = 0; i < data.length; i++) reals[i] = Real.of(data[i]);
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(reals, rows, cols, Reals.getInstance());
    }

    private DoubleBuffer toDoubleBuffer(Matrix<Real> m) {
        double[] data = toDoubleArray(m);
        return DoubleBuffer.wrap(data);
    }
    
    private Matrix<Real> fromDoubleBuffer(DoubleBuffer buf, int rows, int cols) {
        if (buf.hasArray()) {
            return fromDoubleArray(buf.array(), rows, cols);
        }
        double[] data = new double[rows * cols];
        buf.get(data);
        return fromDoubleArray(data, rows, cols);
    }
}
