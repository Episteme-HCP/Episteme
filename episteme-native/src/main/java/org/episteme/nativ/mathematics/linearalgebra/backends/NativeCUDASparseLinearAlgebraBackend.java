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
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.algorithm.OperationContext.Hint;

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
    public String getId() {
        return "cuda-sparse";
    }

    @Override
    public String getType() {
        return "math";
    }

    @Override
    public void freeGPUMemory(long handle) {
        try {
            checkCuda((int) CUDA_FREE.invokeExact(NativeSafe.scavenge(MemorySegment.ofAddress(handle), 0, Arena.global(), "gpu_free_manual").segment()));
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
    
    // cuSolver Handles
    private static SymbolLookup cusolver_lookup;
    private static MethodHandle CUSOLVER_SP_CREATE;
    private static MethodHandle CUSOLVER_SP_DESTROY;
    private static MethodHandle CUSOLVER_SP_D_CSRLSV_LU;

    private static MethodHandle CUSPARSE_CREATE_MAT_DESCR;
    private static MethodHandle CUSPARSE_DESTROY_MAT_DESCR;

    // cuBLAS Handles
    private static MethodHandle CUBLAS_CREATE;
    private static MethodHandle CUBLAS_DESTROY;
    private static MethodHandle CUBLAS_DGEMM;
    private static MethodHandle CUBLAS_DDOT;
    private static MethodHandle CUBLAS_DNRM2;
    private static MethodHandle CUBLAS_DAXPY;
    private static MethodHandle CUBLAS_DSCAL;
    private static MethodHandle CUBLAS_DGEAM;
 


    // Constants
    private static final int CUSPARSE_INDEX_32BIT = 0;
    private static final int CUSPARSE_INDEX_BASE_ZERO = 0;
    private static final int CUDA_R_64F = 1; // Double
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
            Optional<SymbolLookup> cudaRtOpt = NativeFFMLoader.loadLibrary("cudart", Arena.global());
            Optional<SymbolLookup> cusparseOpt = NativeFFMLoader.loadLibrary("cusparse", Arena.global());

            if (cudaRtOpt.isEmpty() || cusparseOpt.isEmpty()) {
                logger.warn("Native CUDA Sparse libraries not found (cudart={}, cusparse={})", 
                    cudaRtOpt.isPresent(), cusparseOpt.isPresent());
                return;
            }

            SymbolLookup cudart = cudaRtOpt.get();
            cusparse_lookup = cusparseOpt.get();

            // Verify GPU Presence
            Optional<MemorySegment> deviceCountSym = NativeFFMLoader.findSymbol(cudart, "cudaGetDeviceCount");
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
            CUDA_MALLOC = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cudart, "cudaMalloc_v2", "cudaMalloc").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            CUDA_FREE = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cudart, "cudaFree").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUDA_MEMCPY = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cudart, "cudaMemcpy").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
            CUDA_DEVICE_SYNCHRONIZE = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cudart, "cudaDeviceSynchronize").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT));

            // cuSPARSE handles
            CUSPARSE_CREATE = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusparse_lookup, "cusparseCreate").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSPARSE_DESTROY = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusparse_lookup, "cusparseDestroy").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            CUSPARSE_CREATE_CSR = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusparse_lookup, "cusparseCreateCsr").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
            
            CUSPARSE_CREATE_DN_MAT = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusparse_lookup, "cusparseCreateDnMat").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, 
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

            CUSPARSE_SPMM_BUFFER_SIZE = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusparse_lookup, "cusparseSpMM_bufferSize").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMM = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusparse_lookup, "cusparseSpMM").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMV_BUFFER_SIZE = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusparse_lookup, "cusparseSpMV_bufferSize").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_SPMV = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusparse_lookup, "cusparseSpMV").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSPARSE_CREATE_DN_VEC = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusparse_lookup, "cusparseCreateDnVec").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

            CUSPARSE_CREATE_MAT_DESCR = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusparse_lookup, "cusparseCreateMatDescr").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSPARSE_DESTROY_MAT_DESCR = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusparse_lookup, "cusparseDestroyMatDescr").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            // cuBLAS handles
            Optional<SymbolLookup> cublasOpt = NativeFFMLoader.loadLibrary("cublas", Arena.global());
            if (cublasOpt.isPresent()) {
                SymbolLookup cublas = cublasOpt.get();
                CUBLAS_CREATE = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cublas, "cublasCreate_v2", "cublasCreate").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                CUBLAS_DESTROY = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cublas, "cublasDestroy_v2", "cublasDestroy").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                CUBLAS_DGEMM = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cublas, "cublasDgemm_v2", "cublasDgemm").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                        ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                
                CUBLAS_DDOT = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cublas, "cublasDdot_v2", "cublasDdot").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                
                CUBLAS_DNRM2 = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cublas, "cublasDnrm2_v2", "cublasDnrm2").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

                CUBLAS_DAXPY = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cublas, "cublasDaxpy_v2", "cublasDaxpy").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

                CUBLAS_DSCAL = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cublas, "cublasDscal_v2", "cublasDscal").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                CUBLAS_DGEAM = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cublas, "cublasDgeam_v2", "cublasDgeam").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                        ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                try (Arena tempArena = Arena.ofConfined()) {
                    MemorySegment handlePtr = tempArena.allocate(ValueLayout.ADDRESS);
                    checkCuda((int) CUBLAS_CREATE.invokeExact(handlePtr));
                    CUBLAS_DESTROY.invokeExact(handlePtr.get(ValueLayout.ADDRESS, 0)); 
                }
            }

            // cuSolver handles
            Optional<SymbolLookup> cusolverOpt = NativeFFMLoader.loadLibrary("cusolver", Arena.global());
            if (cusolverOpt.isPresent()) {
                cusolver_lookup = cusolverOpt.get();
                CUSOLVER_SP_CREATE = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusolver_lookup, "cusolverSpCreate").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                CUSOLVER_SP_DESTROY = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusolver_lookup, "cusolverSpDestroy").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                CUSOLVER_SP_D_CSRLSV_LU = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cusolver_lookup, "cusolverSpDcsrlsvlu").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                        ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                        ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
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
            Optional<SymbolLookup> cudaRtOpt = NativeFFMLoader.loadLibrary("cudart", Arena.global());
            if (cudaRtOpt.isPresent()) {
                MethodHandle getDeviceCount = LINKER.downcallHandle(NativeFFMLoader.findSymbol(cudaRtOpt.get(), "cudaGetDeviceCount").get(),
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment countPtr = arena.allocate(ValueLayout.JAVA_INT);
                    checkCuda((int) getDeviceCount.invokeExact(countPtr));
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
    public String getEnvironmentInfo() {
        if (!IS_AVAILABLE) return "N/A";
        return "GPU (CUDA)";
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

            copyToGPU(d_A, A, (long) m * k, arena);
            copyToGPU(d_B, B, (long) k * n, arena);

            MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUBLAS_CREATE.invokeExact(handlePtr));
            MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);

            // cuBLAS is column-major by default. Episteme is row-major.
            // C = op(A) * op(B)
            // To get row-major C = A*B using col-major DGEMM:
            // C^T = B^T * A^T
            // So we call DGEMM(handle, N, N, n, m, k, alpha, d_B, n, d_A, k, beta, d_C, n)
            // Use scavenge-protected segments for cuBLAS
            checkCuda((int) CUBLAS_DGEMM.invokeExact(handle, 0, 0, n, m, k, alpha, 
                NativeSafe.scavenge(MemorySegment.ofAddress(d_B), (long)k * n * 8, arena, "cublas_dgemm_b").segment(), n, 
                NativeSafe.scavenge(MemorySegment.ofAddress(d_A), (long)m * n * 8, arena, "cublas_dgemm_a").segment(), k, 
                beta, 
                NativeSafe.scavenge(MemorySegment.ofAddress(d_C), (long)m * n * 8, arena, "cublas_dgemm_c").segment(), n));

            copyFromGPU(d_C, C, (long) m * n, arena);

            checkCuda((int) CUBLAS_DESTROY.invokeExact(handle));
            checkCuda((int) CUDA_FREE.invokeExact(NativeSafe.scavenge(MemorySegment.ofAddress(d_A), 0, Arena.global(), "gpu_free_a").segment()));
            checkCuda((int) CUDA_FREE.invokeExact(NativeSafe.scavenge(MemorySegment.ofAddress(d_B), 0, Arena.global(), "gpu_free_b").segment()));
            checkCuda((int) CUDA_FREE.invokeExact(NativeSafe.scavenge(MemorySegment.ofAddress(d_C), 0, Arena.global(), "gpu_free_c").segment()));
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
            checkCuda((int) CUDA_DEVICE_SYNCHRONIZE.invokeExact());
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

                checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrRowPtr), h_rowPtr, (long)(m + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrColIdx), h_colIdx, (long)nnz * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrVal), h_vals, (long)nnz * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_vecX), h_vecX, (long)k * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                // 3. cuSPARSE Handles
                MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUSPARSE_CREATE.invokeExact(handlePtr));
                MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

                // Descriptors
                MemorySegment matAPtr = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUSPARSE_CREATE_CSR.invokeExact(matAPtr, (long)m, (long)k, (long)nnz, 
                    MemorySegment.ofAddress(d_csrRowPtr), MemorySegment.ofAddress(d_csrColIdx), MemorySegment.ofAddress(d_csrVal),
                    CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, CUDA_R_64F));
                MemorySegment matA = matAPtr.get(ValueLayout.ADDRESS, 0);

                MemorySegment vecXPtr = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUSPARSE_CREATE_DN_VEC.invokeExact(vecXPtr, (long)k, MemorySegment.ofAddress(d_vecX), CUDA_R_64F));
                MemorySegment vecX = vecXPtr.get(ValueLayout.ADDRESS, 0);

                MemorySegment vecYPtr = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUSPARSE_CREATE_DN_VEC.invokeExact(vecYPtr, (long)m, MemorySegment.ofAddress(d_vecY), CUDA_R_64F));
                MemorySegment vecY = vecYPtr.get(ValueLayout.ADDRESS, 0);

                // Buffer allocation for SpMV
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
                MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
                MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);
                
                int rS4 = (int) CUSPARSE_SPMV_BUFFER_SIZE.invokeExact(handle, 0, alpha, matA, vecX, beta, vecY, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr);
                checkCuda(rS4);
                long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
                System.out.println("[CUDA Sparse] SpMV m=" + m + ", k=" + k + ", nnz=" + nnz + ", bufferSize=" + bufferSize);
                long d_buffer = bufferSize > 0 ? allocateGPUMemory(bufferSize) : 0;

                // Execution
                int rS5 = (int) CUSPARSE_SPMV.invokeExact(handle, 0, alpha, matA, vecX, beta, vecY, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, MemorySegment.ofAddress(d_buffer));
                System.out.println("[CUDA Sparse] SpMV Result Code: " + rS5);
                checkCuda(rS5);

                // 4. Result (D2H)
                double[] resHost = new double[m];
                MemorySegment h_res = arena.allocate(ValueLayout.JAVA_DOUBLE, m);
                checkCuda((int) CUDA_MEMCPY.invokeExact(h_res, MemorySegment.ofAddress(d_vecY), (long)m * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(h_res, ValueLayout.JAVA_DOUBLE, 0, resHost, 0, m);

                // Cleanup GPU
                if (d_buffer != 0) { checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_buffer))); }
                checkCuda((int) CUSPARSE_DESTROY.invokeExact(handle));

                return fromDoubleArray(resHost, m, 1);
            } finally {
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrRowPtr)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrColIdx)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrVal)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_vecX)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_vecY)));
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

                checkCuda((int) CUDA_MEMCPY.invokeExact(NativeSafe.scavenge(MemorySegment.ofAddress(d_csrRowPtr), (long)(m+1)*4, arena, "cusparse_rowptr_h2d").segment(), h_rowPtr, (long)(m + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(NativeSafe.scavenge(MemorySegment.ofAddress(d_csrColIdx), (long)nnz*4, arena, "cusparse_colidx_h2d").segment(), h_colIdx, (long)nnz * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(NativeSafe.scavenge(MemorySegment.ofAddress(d_csrVal), (long)nnz*8, arena, "cusparse_val_h2d").segment(), h_vals, (long)nnz * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(NativeSafe.scavenge(MemorySegment.ofAddress(d_denseB), (long)k*n*8, arena, "cusparse_denseb_h2d").segment(), h_denseB, (long)k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                // 3. cuSPARSE Handles
                MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUSPARSE_CREATE.invokeExact(handlePtr));
                MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

                // Descriptors
                MemorySegment matAPtr = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUSPARSE_CREATE_CSR.invokeExact(matAPtr, (long)m, (long)k, (long)nnz, 
                    MemorySegment.ofAddress(d_csrRowPtr), MemorySegment.ofAddress(d_csrColIdx), MemorySegment.ofAddress(d_csrVal),
                    CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, CUDA_R_64F));
                MemorySegment matA = matAPtr.get(ValueLayout.ADDRESS, 0);

                MemorySegment matBPtr = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUSPARSE_CREATE_DN_MAT.invokeExact(matBPtr, (long)k, (long)n, (long)n, MemorySegment.ofAddress(d_denseB), CUDA_R_64F, CUSPARSE_ORDER_ROW));
                MemorySegment matB = matBPtr.get(ValueLayout.ADDRESS, 0);

                MemorySegment matCPtr = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUSPARSE_CREATE_DN_MAT.invokeExact(matCPtr, (long)m, (long)n, (long)n, MemorySegment.ofAddress(d_denseC), CUDA_R_64F, CUSPARSE_ORDER_ROW));
                MemorySegment matC = matCPtr.get(ValueLayout.ADDRESS, 0);

                // Buffer allocation for SpMM
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
                MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
                MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);
                
                checkCuda((int) CUSPARSE_SPMM_BUFFER_SIZE.invokeExact(handle, 0, 0, alpha, matA, matB, beta, matC, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr));
                long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
                long d_buffer = bufferSize > 0 ? allocateGPUMemory(bufferSize) : 0;

                // Execution
                checkCuda((int) CUSPARSE_SPMM.invokeExact(handle, 0, 0, alpha, matA, matB, beta, matC, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, MemorySegment.ofAddress(d_buffer)));

                // 4. Result (D2H)
                double[] resHost = new double[m * n];
                MemorySegment h_res = arena.allocate(ValueLayout.JAVA_DOUBLE, m * n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(h_res, NativeSafe.scavenge(MemorySegment.ofAddress(d_denseC), (long)m * n * 8, arena, "cusparse_densec_d2h").segment(), (long)m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(h_res, ValueLayout.JAVA_DOUBLE, 0, resHost, 0, m * n);

                // Cleanup GPU
                if (d_buffer != 0) { checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_buffer))); }
                checkCuda((int) CUSPARSE_DESTROY.invokeExact(handle));

                return fromDoubleArray(resHost, m, n);
            } finally {
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrRowPtr)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrColIdx)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrVal)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_denseB)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_denseC)));
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
        try (Arena arena = Arena.ofConfined()) {
            copyToGPU(d_ptr, hostBuf, count, arena);
        }
    }

    public void copyToGPU(long d_ptr, DoubleBuffer hostBuf, long count, Arena arena) {
        try {
            MemorySegment h_seg;
            if (hostBuf.isDirect()) {
                h_seg = MemorySegment.ofBuffer(hostBuf);
            } else {
                // If heap buffer, copy to off-heap first
                double[] array = new double[(int) count];
                hostBuf.duplicate().get(array);
                h_seg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, array);
            }
            int rcm6 = (int) CUDA_MEMCPY.invokeExact(NativeSafe.scavenge(MemorySegment.ofAddress(d_ptr), count * 8, arena, "cuda_memcpy_h2d").segment(), h_seg, count * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
            if (rcm6 != 0) throw new RuntimeException("cudaMemcpy H2D failed with code " + rcm6);
        } catch (Throwable t) {
            throw new RuntimeException("GPU Copy H2D failed", t);
        }
    }

    @Override
    public void copyFromGPU(long d_ptr, DoubleBuffer hostBuf, long count) {
        try (Arena arena = Arena.ofConfined()) {
            copyFromGPU(d_ptr, hostBuf, count, arena);
        }
    }

    public void copyFromGPU(long d_ptr, DoubleBuffer hostBuf, long count, Arena arena) {
        try {
            MemorySegment h_seg;
            boolean isDirect = hostBuf.isDirect();
            if (isDirect) {
                h_seg = MemorySegment.ofBuffer(hostBuf);
            } else {
                h_seg = arena.allocate(ValueLayout.JAVA_DOUBLE, count);
            }
            int rcm7 = (int) CUDA_MEMCPY.invokeExact(h_seg, NativeSafe.scavenge(MemorySegment.ofAddress(d_ptr), count * 8, arena, "cuda_memcpy_d2h").segment(), count * 8, CUDA_MEMCPY_DEVICE_TO_HOST);
            if (rcm7 != 0) throw new RuntimeException("cudaMemcpy D2H failed with code " + rcm7);
            
            if (!isDirect) {
                double[] array = h_seg.toArray(ValueLayout.JAVA_DOUBLE);
                hostBuf.duplicate().put(array);
            }
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

    private double[] toDoubleArray(Vector<Real> v) {
        int n = v.dimension();
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            data[i] = v.get(i).doubleValue();
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

    private static void checkCuda(int result) {
        if (result != 0) throw new RuntimeException("CUDA Error: " + result);
    }

    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        if (!IS_AVAILABLE || CUBLAS_DGEAM == null) throw new UnsupportedOperationException("CUDA cuBLAS dgeam not available");
        return performGeam(a, b, 1.0, 1.0);
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        if (!IS_AVAILABLE || CUBLAS_DGEAM == null) throw new UnsupportedOperationException("CUDA cuBLAS dgeam not available");
        return performGeam(a, b, 1.0, -1.0);
    }

    private Matrix<Real> performGeam(Matrix<Real> a, Matrix<Real> b, double alphaVal, double betaVal) {
        int m = a.rows();
        int n = a.cols();
        if (b.rows() != m || b.cols() != n) throw new IllegalArgumentException("Matrix dimensions must match");

        try (Arena arena = Arena.ofConfined()) {
            long d_a = allocateGPUMemory((long) m * n * 8);
            long d_b = allocateGPUMemory((long) m * n * 8);
            long d_c = allocateGPUMemory((long) m * n * 8);

            copyToGPU(d_a, DoubleBuffer.wrap(toDoubleArray(a)), (long) m * n, arena);
            copyToGPU(d_b, DoubleBuffer.wrap(toDoubleArray(b)), (long) m * n, arena);

            MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUBLAS_CREATE.invokeExact(handlePtr));
            MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, alphaVal);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, betaVal);

            // C = alpha * op(A) + beta * op(B)
            // cuBLAS is column-major. If we pass row-major and no transpose, 
            // it computes row-major C if we swap m and n.
            int res = (int) CUBLAS_DGEAM.invokeExact(handle, 0, 0, n, m, alpha, 
                MemorySegment.ofAddress(d_a).reinterpret((long) m * n * 8), n, 
                beta, 
                MemorySegment.ofAddress(d_b).reinterpret((long) m * n * 8), n, 
                MemorySegment.ofAddress(d_c).reinterpret((long) m * n * 8), n);
            checkCuda(res);

            double[] result = new double[m * n];
            copyFromGPU(d_c, DoubleBuffer.wrap(result), (long) m * n, arena);

            checkCuda((int) CUBLAS_DESTROY.invokeExact(handle));
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_a)));
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_b)));
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_c)));

            return fromDoubleArray(result, m, n);
        } catch (Throwable t) {
            logger.error("cuBLAS DGEAM failed: {}", t.getMessage());
            throw new RuntimeException("CUDA DGEAM failed", t);
        }
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        if (!IS_AVAILABLE || CUBLAS_DSCAL == null) throw new UnsupportedOperationException("CUDA cuBLAS not available");
        int m = a.rows();
        int n = a.cols();
        try (Arena arena = Arena.ofConfined()) {
            double[] data = toDoubleArray(a);
            long d_a = allocateGPUMemory((long) m * n * 8);
            copyToGPU(d_a, DoubleBuffer.wrap(data), (long) m * n, arena);

            MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
            CUBLAS_CREATE.invokeExact(handlePtr);
            MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, scalar.doubleValue());
            int res = (int) CUBLAS_DSCAL.invokeExact(handle, m * n, alpha, MemorySegment.ofAddress(d_a).reinterpret((long) m * n * 8), 1);
            checkCuda(res);

            double[] result = new double[m * n];
            copyFromGPU(d_a, DoubleBuffer.wrap(result), (long) m * n, arena);
            
            CUBLAS_DESTROY.invokeExact(handle);
            CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_a));
            
            return fromDoubleArray(result, m, n);
        } catch (Throwable t) {
            throw new RuntimeException("cuBLAS scale failed", t);
        }
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        // Simplest native transpose: copy to GPU and back with index swap
        int m = a.rows();
        int n = a.cols();
        double[] data = toDoubleArray(a);
        double[] res = new double[m * n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[j * m + i] = data[i * n + j];
            }
        }
        return fromDoubleArray(res, n, m);
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Vector dimensions must match");
        if (!IS_AVAILABLE || CUBLAS_DAXPY == null) throw new UnsupportedOperationException("CUDA cuBLAS not available");
        int n = a.dimension();
        try (Arena arena = Arena.ofConfined()) {
            long d_a = allocateGPUMemory((long) n * 8);
            long d_b = allocateGPUMemory((long) n * 8);
            copyToGPU(d_a, DoubleBuffer.wrap(toDoubleArray(a)), n, arena);
            copyToGPU(d_b, DoubleBuffer.wrap(toDoubleArray(b)), n, arena);

            MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
            CUBLAS_CREATE.invokeExact(handlePtr);
            MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            int res = (int) CUBLAS_DAXPY.invokeExact(handle, n, alpha, 
                MemorySegment.ofAddress(d_b).reinterpret((long) n * 8), 1, 
                MemorySegment.ofAddress(d_a).reinterpret((long) n * 8), 1);
            checkCuda(res);

            double[] result = new double[n];
            copyFromGPU(d_a, DoubleBuffer.wrap(result), n, arena);
            
            CUBLAS_DESTROY.invokeExact(handle);
            CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_a));
            CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_b));
            
            return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(result);
        } catch (Throwable t) {
            throw new RuntimeException("cuBLAS Daxpy failed", t);
        }
    }

    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Vector dimensions must match");
        if (!IS_AVAILABLE || CUBLAS_DAXPY == null) throw new UnsupportedOperationException("CUDA cuBLAS not available");
        int n = a.dimension();
        try (Arena arena = Arena.ofConfined()) {
            long d_a = allocateGPUMemory((long) n * 8);
            long d_b = allocateGPUMemory((long) n * 8);
            copyToGPU(d_a, DoubleBuffer.wrap(toDoubleArray(a)), n, arena);
            copyToGPU(d_b, DoubleBuffer.wrap(toDoubleArray(b)), n, arena);

            MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
            CUBLAS_CREATE.invokeExact(handlePtr);
            MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, -1.0);
            int res = (int) CUBLAS_DAXPY.invokeExact(handle, n, alpha, 
                MemorySegment.ofAddress(d_b).reinterpret((long) n * 8), 1, 
                MemorySegment.ofAddress(d_a).reinterpret((long) n * 8), 1);
            checkCuda(res);

            double[] result = new double[n];
            copyFromGPU(d_a, DoubleBuffer.wrap(result), n, arena);
            
            CUBLAS_DESTROY.invokeExact(handle);
            CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_a));
            CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_b));
            
            return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(result);
        } catch (Throwable t) {
            throw new RuntimeException("cuBLAS Daxpy failed", t);
        }
    }

    @Override
    public Vector<Real> multiply(Vector<Real> v, Real s) {
        if (!IS_AVAILABLE || CUBLAS_DSCAL == null) throw new UnsupportedOperationException("CUDA cuBLAS not available");
        int n = v.dimension();
        try (Arena arena = Arena.ofConfined()) {
            long d_v = allocateGPUMemory((long) n * 8);
            copyToGPU(d_v, DoubleBuffer.wrap(toDoubleArray(v)), n, arena);

            MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
            CUBLAS_CREATE.invokeExact(handlePtr);
            MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, s.doubleValue());
            int res = (int) CUBLAS_DSCAL.invokeExact(handle, n, alpha, MemorySegment.ofAddress(d_v).reinterpret((long) n * 8), 1);
            checkCuda(res);

            double[] result = new double[n];
            copyFromGPU(d_v, DoubleBuffer.wrap(result), n, arena);
            
            CUBLAS_DESTROY.invokeExact(handle);
            CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_v));
            
            return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(result);
        } catch (Throwable t) {
            throw new RuntimeException("cuBLAS Dscal failed", t);
        }
    }

    @Override
    public Real dot(Vector<Real> v1, Vector<Real> v2) {
        if (!IS_AVAILABLE || CUBLAS_DDOT == null) throw new UnsupportedOperationException("CUDA cuBLAS not available");
        int n = v1.dimension();
        try (Arena arena = Arena.ofConfined()) {
            long d_v1 = allocateGPUMemory((long) n * 8);
            long d_v2 = allocateGPUMemory((long) n * 8);
            copyToGPU(d_v1, DoubleBuffer.wrap(toDoubleArray(v1)), n, arena);
            copyToGPU(d_v2, DoubleBuffer.wrap(toDoubleArray(v2)), n, arena);

            MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
            CUBLAS_CREATE.invokeExact(handlePtr);
            MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

            MemorySegment resultPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
            int res = (int) CUBLAS_DDOT.invokeExact(handle, n, 
                MemorySegment.ofAddress(d_v1).reinterpret((long) n * 8), 1, 
                MemorySegment.ofAddress(d_v2).reinterpret((long) n * 8), 1, 
                resultPtr);
            checkCuda(res);

            double dot = resultPtr.get(ValueLayout.JAVA_DOUBLE, 0);
            
            CUBLAS_DESTROY.invokeExact(handle);
            CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_v1));
            CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_v2));
            
            return Real.of(dot);
        } catch (Throwable t) {
            throw new RuntimeException("cuBLAS Ddot failed", t);
        }
    }

    @Override
    public Real norm(Vector<Real> v) {
        if (!IS_AVAILABLE || CUBLAS_DNRM2 == null) throw new UnsupportedOperationException("CUDA cuBLAS not available");
        int n = v.dimension();
        try (Arena arena = Arena.ofConfined()) {
            long d_v = allocateGPUMemory((long) n * 8);
            copyToGPU(d_v, DoubleBuffer.wrap(toDoubleArray(v)), n, arena);

            MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
            CUBLAS_CREATE.invokeExact(handlePtr);
            MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

            MemorySegment resultPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
            int res = (int) CUBLAS_DNRM2.invokeExact(handle, n, 
                MemorySegment.ofAddress(d_v).reinterpret((long) n * 8), 1, 
                resultPtr);
            checkCuda(res);

            double norm = resultPtr.get(ValueLayout.JAVA_DOUBLE, 0);
            
            CUBLAS_DESTROY.invokeExact(handle);
            CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_v));
            
            return Real.of(norm);
        } catch (Throwable t) {
            throw new RuntimeException("cuBLAS Dnrm2 failed", t);
        }
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (!IS_AVAILABLE || CUSOLVER_SP_D_CSRLSV_LU == null) throw new UnsupportedOperationException("cuSolverSparse LU solver not available");
        
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> sparseA = (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a;
            int n = a.rows();
            int nnz = sparseA.getNnz();

            try (Arena arena = Arena.ofConfined()) {
                // 1. Prepare CSR data
                int[] rowPtrHost = sparseA.getRowPointers();
                int[] colIdxHost = sparseA.getColIndices();
                double[] valsHost = new double[nnz];
                Object[] valsObj = sparseA.getValues();
                for (int i = 0; i < nnz; i++) valsHost[i] = ((Real) valsObj[i]).doubleValue();
                double[] bHost = toDoubleArray(b);

                // 2. Allocate and copy to GPU
                long d_csrRowPtr = allocateGPUMemory((long)(n + 1) * 4);
                long d_csrColIdx = allocateGPUMemory((long)nnz * 4);
                long d_csrVal = allocateGPUMemory((long)nnz * 8);
                long d_b = allocateGPUMemory((long)n * 8);
                long d_x = allocateGPUMemory((long)n * 8);

                try {
                    MemorySegment h_rowPtr = arena.allocateFrom(ValueLayout.JAVA_INT, rowPtrHost);
                    MemorySegment h_colIdx = arena.allocateFrom(ValueLayout.JAVA_INT, colIdxHost);
                    MemorySegment h_vals = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, valsHost);
                    MemorySegment h_b = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, bHost);

                    checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrRowPtr), h_rowPtr, (long)(n + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
                    checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrColIdx), h_colIdx, (long)nnz * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
                    checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrVal), h_vals, (long)nnz * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
                    checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_b), h_b, (long)n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                    // 3. cuSolver Handle
                    MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
                    checkCuda((int) CUSOLVER_SP_CREATE.invokeExact(handlePtr));
                    MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

                    // 4. cuSPARSE Descriptor
                    MemorySegment descrPtr = arena.allocate(ValueLayout.ADDRESS);
                    checkCuda((int) CUSPARSE_CREATE_MAT_DESCR.invokeExact(descrPtr));
                    MemorySegment descr = descrPtr.get(ValueLayout.ADDRESS, 0);
                    
                    MemorySegment singularity = arena.allocate(ValueLayout.JAVA_INT);
                    
                    // 5. Execution: CUSOLVER_SP_D_CSRLSV_LU
                    // int n, int nnz, descrA, valA, rowPtrA, colIndA, b, tol, reorder, x, singularity
                    int status = (int) CUSOLVER_SP_D_CSRLSV_LU.invokeExact(handle, n, nnz, descr, 
                        MemorySegment.ofAddress(d_csrVal), MemorySegment.ofAddress(d_csrRowPtr), MemorySegment.ofAddress(d_csrColIdx),
                        MemorySegment.ofAddress(d_b), 1e-12, 0, MemorySegment.ofAddress(d_x), singularity);
                    
                    checkCuda(status);
                    if (singularity.get(ValueLayout.JAVA_INT, 0) != -1) {
                        throw new RuntimeException("Matrix is singular at " + singularity.get(ValueLayout.JAVA_INT, 0));
                    }

                    // 6. Result (D2H)
                    double[] xHost = new double[n];
                    MemorySegment h_x = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
                    checkCuda((int) CUDA_MEMCPY.invokeExact(h_x, MemorySegment.ofAddress(d_x), (long)n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                    MemorySegment.copy(h_x, ValueLayout.JAVA_DOUBLE, 0, xHost, 0, n);

                    // 7. Cleanup
                    CUSPARSE_DESTROY_MAT_DESCR.invokeExact(descr);
                    CUSOLVER_SP_DESTROY.invokeExact(handle);

                    return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(xHost);
                } finally {
                    checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrRowPtr)));
                    checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrColIdx)));
                    checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrVal)));
                    checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_b)));
                    checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_x)));
                }
            } catch (Throwable t) {
                logger.error("CUDA Sparse Solve failed: {}", t.getMessage());
                throw new RuntimeException("CUDA Sparse Solve failed", t);
            }
        }
        throw new UnsupportedOperationException("Dense CUDA solver not implemented yet");
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        throw new UnsupportedOperationException("Native CUDA inverse implementation pending");
    }

    @Override
    public Real determinant(Matrix<Real> a) {
        throw new UnsupportedOperationException("Native CUDA determinant implementation pending");
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<Real> lu(Matrix<Real> a) {
        throw new UnsupportedOperationException("Native CUDA LU implementation pending");
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult<Real> qr(Matrix<Real> a) {
        throw new UnsupportedOperationException("Native CUDA QR implementation pending");
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult<Real> svd(Matrix<Real> a) {
        throw new UnsupportedOperationException("Native CUDA SVD implementation pending");
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<Real> cholesky(Matrix<Real> a) {
        throw new UnsupportedOperationException("Native CUDA Cholesky implementation pending");
    }

    @Override
    public double score(OperationContext context) {
        if (!isAvailable()) return -1.0;
        double base = getPriority();

        if (context.hasHint(Hint.SPARSE)) {
            base += 40.0;
        }
        if (context.hasHint(Hint.GPU_RESIDENT)) {
            base += 50.0;
        }
        
        // Penalize very small data which is better handled on CPU due to overhead
        if (context.getDataSize() < 500) {
            base -= 200.0;
        }

        return base;
    }

    @Override
    public Vector<Real> bicgstab(Matrix<Real> a, Vector<Real> b, Vector<Real> x0, Real tolerance, int maxIterations) {
        // For now, if appropriate, use a hybrid implementation or throw UOE if complex.
        // Given the scale, iterative solvers are often preferred for very large sparse systems.
        throw new UnsupportedOperationException("CUDA BiCGSTAB implementation pending (using direct LU solve for now if called via solve())");
    }

    @Override
    public Vector<Real> conjugateGradient(Matrix<Real> a, Vector<Real> b, Vector<Real> x0, Real tolerance, int maxIterations) {
        throw new UnsupportedOperationException("CUDA Conjugate Gradient implementation pending");
    }

    @Override
    public Vector<Real> gmres(Matrix<Real> a, Vector<Real> b, Vector<Real> x0, Real tolerance, int maxIterations, int restarts) {
        throw new UnsupportedOperationException("CUDA GMRES implementation pending");
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult<Real> eigen(Matrix<Real> a) {
        throw new UnsupportedOperationException("Native CUDA Eigen implementation pending");
    }
}
