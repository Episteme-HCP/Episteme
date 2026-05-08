/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.gpu.cuda;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralized manager for CUDA lifecycle and native handles.
 */
public final class CUDAManager {
    private static final Logger logger = LoggerFactory.getLogger(CUDAManager.class);
    public static final int CUDA_MEMCPY_H_TO_D = 1;
    public static final int CUDA_MEMCPY_D_TO_H = 2;
    public static final int CUDA_MEMCPY_D_TO_D = 3;

    private static final Arena managerArena = Arena.ofShared();
    private static final Linker LINKER = Linker.nativeLinker();

    private static boolean initialized = false;
    private static boolean available = false;
    private static boolean useCusolver = true;

    private static SymbolLookup cudaLookup;
    private static SymbolLookup cudartLookup;
    private static SymbolLookup cublasLookup;
    private static SymbolLookup cusparseLookup;
    private static SymbolLookup cusolverLookup;

    private static MemorySegment cublasHandle;
    private static MemorySegment cusparseHandle;
    private static MemorySegment cusolverHandle;

    // --- Driver API (cuda) ---
    public static MethodHandle CU_MEM_ALLOC;
    public static MethodHandle CU_MEM_FREE;
    public static MethodHandle CU_MEMCPY_H_TO_D;
    public static MethodHandle CU_MEMCPY_D_TO_H;
    public static MethodHandle CU_CTX_SYNCHRONIZE;
    public static MethodHandle CU_GET_ERROR_STRING;

    // --- Runtime API (cudart) ---
    public static MethodHandle CUDA_MALLOC;
    public static MethodHandle CUDA_FREE;
    public static MethodHandle CUDA_MEMCPY;
    public static MethodHandle CUDA_MEMSET;
    public static MethodHandle CUDA_DEVICE_SYNCHRONIZE;
    public static MethodHandle CUDA_GET_ERROR_STRING;
    public static MethodHandle CUDA_GET_DEVICE_COUNT;

    // --- cuBLAS ---
    public static MethodHandle CUBLAS_CREATE;
    public static MethodHandle CUBLAS_DESTROY;
    public static MethodHandle CUBLAS_DGEMM;
    public static MethodHandle CUBLAS_SGEMM;
    public static MethodHandle CUBLAS_ZGEMM;
    public static MethodHandle CUBLAS_DGEAM;
    public static MethodHandle CUBLAS_ZGEAM;
    public static MethodHandle CUBLAS_DDOT;
    public static MethodHandle CUBLAS_DAXPY;
    public static MethodHandle CUBLAS_DSCAL;
    public static MethodHandle CUBLAS_STATUS_GET_STRING;

    // --- cuSPARSE ---
    public static MethodHandle CUSPARSE_CREATE;
    public static MethodHandle CUSPARSE_DESTROY;
    public static MethodHandle CUSPARSE_CREATE_CSR;
    public static MethodHandle CUSPARSE_DESTROY_SP_MAT;
    public static MethodHandle CUSPARSE_CREATE_DN_VEC;
    public static MethodHandle CUSPARSE_DESTROY_DN_VEC;
    public static MethodHandle CUSPARSE_CREATE_DN_MAT;
    public static MethodHandle CUSPARSE_DESTROY_DN_MAT;
    public static MethodHandle CUSPARSE_SPMV;
    public static MethodHandle CUSPARSE_SPMV_BUFFER_SIZE;
    public static MethodHandle CUSPARSE_SPMM;
    public static MethodHandle CUSPARSE_SPMM_BUFFER_SIZE;
    public static MethodHandle CUSPARSE_STATUS_GET_STRING;

    // --- cuSolver ---
    public static MethodHandle CUSOLVER_CREATE;
    public static MethodHandle CUSOLVER_DESTROY;
    public static MethodHandle CUSOLVER_DGETRF_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_DGETRF;
    public static MethodHandle CUSOLVER_DGETRS;
    public static MethodHandle CUSOLVER_ZGETRF_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_ZGETRF;
    public static MethodHandle CUSOLVER_ZGETRS;
    public static MethodHandle CUSOLVER_STATUS_GET_STRING;

    private CUDAManager() {}

    public static synchronized void ensureInitialized() {
        if (initialized) return;
        initialized = true;

        try {
            cudaLookup = NativeFFMLoader.loadLibrary("cuda", managerArena).orElse(null);
            cudartLookup = NativeFFMLoader.loadLibrary("cudart", managerArena).orElse(null);
            cublasLookup = NativeFFMLoader.loadLibrary("cublas", managerArena).orElse(null);
            cusparseLookup = NativeFFMLoader.loadLibrary("cusparse", managerArena).orElse(null);
            cusolverLookup = NativeFFMLoader.loadLibrary("cusolver", managerArena).orElse(null);

            if (cudaLookup == null && cudartLookup == null) {
                logger.warn("CUDA libraries not found. CUDA backends will be unavailable.");
                return;
            }

            bindSymbols();

            try (Arena temp = Arena.ofConfined()) {
                MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
                
                if (CUBLAS_CREATE != null && (int) NativeSafe.invoke(CUBLAS_CREATE, p) == 0) {
                    cublasHandle = p.get(ValueLayout.ADDRESS, 0).reinterpret(0);
                    cublasHandle = MemorySegment.ofAddress(cublasHandle.address());
                }

                if (CUSPARSE_CREATE != null && (int) NativeSafe.invoke(CUSPARSE_CREATE, p) == 0) {
                    cusparseHandle = p.get(ValueLayout.ADDRESS, 0).reinterpret(0);
                    cusparseHandle = MemorySegment.ofAddress(cusparseHandle.address());
                }

                if (useCusolver && CUSOLVER_CREATE != null) {
                    if ((int) NativeSafe.invoke(CUSOLVER_CREATE, p) == 0) {
                        cusolverHandle = p.get(ValueLayout.ADDRESS, 0).reinterpret(0);
                        cusolverHandle = MemorySegment.ofAddress(cusolverHandle.address());
                    } else {
                        useCusolver = false;
                    }
                }
            }

            available = (cublasHandle != null);
            
            if (available) {
                logger.info("CUDA Manager initialized successfully. cuSolver: {}, cuSPARSE: {}", useCusolver, (cusparseHandle != null));
            }
        } catch (Throwable t) {
            logger.error("Failed to initialize CUDA Manager: {}", t.getMessage());
        }
    }

    private static void bindSymbols() {
        // Driver API
        CU_MEM_ALLOC = lookup(cudaLookup, "cuMemAlloc_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
        CU_MEM_FREE = lookup(cudaLookup, "cuMemFree_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
        CU_MEMCPY_H_TO_D = lookup(cudaLookup, "cuMemcpyHtoD_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
        CU_MEMCPY_D_TO_H = lookup(cudaLookup, "cuMemcpyDtoH_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG));
        CU_CTX_SYNCHRONIZE = lookup(cudaLookup, "cuCtxSynchronize", FunctionDescriptor.of(ValueLayout.JAVA_INT));
        CU_GET_ERROR_STRING = lookup(cudaLookup, "cuGetErrorString", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

        // Runtime API
        CUDA_MALLOC = lookup(cudartLookup, "cudaMalloc", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
        CUDA_FREE = lookup(cudartLookup, "cudaFree", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUDA_MEMCPY = lookup(cudartLookup, "cudaMemcpy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
        CUDA_MEMSET = lookup(cudartLookup, "cudaMemset", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
        CUDA_DEVICE_SYNCHRONIZE = lookup(cudartLookup, "cudaDeviceSynchronize", FunctionDescriptor.of(ValueLayout.JAVA_INT));
        CUDA_GET_ERROR_STRING = lookup(cudartLookup, "cudaGetErrorString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUDA_GET_DEVICE_COUNT = lookup(cudartLookup, "cudaGetDeviceCount", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

        // cuBLAS
        CUBLAS_CREATE = lookup(cublasLookup, "cublasCreate_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUBLAS_DESTROY = lookup(cublasLookup, "cublasDestroy_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUBLAS_DGEMM = lookup(cublasLookup, "cublasDgemm_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT
        ));
        CUBLAS_SGEMM = lookup(cublasLookup, "cublasSgemm_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT
        ));
        CUBLAS_ZGEMM = lookup(cublasLookup, "cublasZgemm_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT
        ));
        CUBLAS_DGEAM = lookup(cublasLookup, "cublasDgeam_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.JAVA_INT
        ));
        CUBLAS_ZGEAM = lookup(cublasLookup, "cublasZgeam_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.JAVA_INT
        ));
        CUBLAS_DDOT = lookup(cublasLookup, "cublasDdot_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUBLAS_DAXPY = lookup(cublasLookup, "cublasDaxpy_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_DSCAL = lookup(cublasLookup, "cublasDscal_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_STATUS_GET_STRING = lookup(cublasLookup, "cublasGetStatusString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

        // cuSPARSE
        CUSPARSE_CREATE = lookup(cusparseLookup, "cusparseCreate", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_DESTROY = lookup(cusparseLookup, "cusparseDestroy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_CREATE_CSR = lookup(cusparseLookup, "cusparseCreateCsr", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
        CUSPARSE_DESTROY_SP_MAT = lookup(cusparseLookup, "cusparseDestroySpMat", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_CREATE_DN_VEC = lookup(cusparseLookup, "cusparseCreateDnVec", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUSPARSE_DESTROY_DN_VEC = lookup(cusparseLookup, "cusparseDestroyDnVec", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_CREATE_DN_MAT = lookup(cusparseLookup, "cusparseCreateDnMat", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
        CUSPARSE_DESTROY_DN_MAT = lookup(cusparseLookup, "cusparseDestroyDnMat", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_SPMV_BUFFER_SIZE = lookup(cusparseLookup, "cusparseSpMV_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_SPMV = lookup(cusparseLookup, "cusparseSpMV", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_SPMM_BUFFER_SIZE = lookup(cusparseLookup, "cusparseSpMM_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_SPMM = lookup(cusparseLookup, "cusparseSpMM", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_STATUS_GET_STRING = lookup(cusparseLookup, "cusparseGetStatusString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

        // cuSolver
        CUSOLVER_CREATE = lookup(cusolverLookup, "cusolverDnCreate", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_DESTROY = lookup(cusolverLookup, "cusolverDnDestroy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_DGETRF_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnDgetrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_DGETRF = lookup(cusolverLookup, "cusolverDnDgetrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        CUSOLVER_DGETRS = lookup(cusolverLookup, "cusolverDnDgetrs", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_ZGETRF_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnZgetrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_ZGETRF = lookup(cusolverLookup, "cusolverDnZgetrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        CUSOLVER_ZGETRS = lookup(cusolverLookup, "cusolverDnZgetrs", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_STATUS_GET_STRING = lookup(cusolverLookup, "cusolverGetStatusString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
    }

    public static boolean isAvailable() {
        ensureInitialized();
        return available;
    }

    public static boolean isUseCusolver() {
        ensureInitialized();
        return useCusolver;
    }

    public static SymbolLookup getCudaLookup() { return cudaLookup; }
    public static SymbolLookup getCudartLookup() { return cudartLookup; }
    public static SymbolLookup getCublasLookup() { return cublasLookup; }
    public static SymbolLookup getCusparseLookup() { return cusparseLookup; }
    public static SymbolLookup getCusolverLookup() { return cusolverLookup; }

    public static MemorySegment getCublasHandle() { return cublasHandle; }
    public static MemorySegment getCusparseHandle() { return cusparseHandle; }
    public static MemorySegment getCusolverHandle() { return cusolverHandle; }

    public static MethodHandle lookup(SymbolLookup lookup, String name, FunctionDescriptor desc) {
        if (lookup == null) return null;
        return NativeFFMLoader.findSymbol(lookup, name)
            .map(s -> LINKER.downcallHandle(s, desc))
            .orElse(null);
    }

    public static void shutdown() {
        synchronized (CUDAManager.class) {
            if (cublasHandle != null) {
                try { NativeSafe.invoke(CUBLAS_DESTROY, cublasHandle); } catch (Throwable t) { logger.warn("Failed to destroy cuBLAS handle", t); }
                cublasHandle = null;
            }
            if (cusparseHandle != null) {
                try { NativeSafe.invoke(CUSPARSE_DESTROY, cusparseHandle); } catch (Throwable t) { logger.warn("Failed to destroy cuSPARSE handle", t); }
                cusparseHandle = null;
            }
            if (cusolverHandle != null) {
                try { NativeSafe.invoke(CUSOLVER_DESTROY, cusolverHandle); } catch (Throwable t) { logger.warn("Failed to destroy cuSolver handle", t); }
                cusolverHandle = null;
            }
            initialized = false;
        }
    }
}
