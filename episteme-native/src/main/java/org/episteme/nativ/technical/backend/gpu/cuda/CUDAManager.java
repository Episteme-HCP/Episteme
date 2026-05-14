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
    public static MethodHandle CUBLAS_CGEMM;
    public static MethodHandle CUBLAS_SGEMV;
    public static MethodHandle CUBLAS_DGEMV;
    public static MethodHandle CUBLAS_CGEMV;
    public static MethodHandle CUBLAS_ZGEMV;
    public static MethodHandle CUBLAS_SGEAM;
    public static MethodHandle CUBLAS_DGEAM;
    public static MethodHandle CUBLAS_ZGEAM;
    public static MethodHandle CUBLAS_CGEAM;
    public static MethodHandle CUBLAS_DDOT;
    public static MethodHandle CUBLAS_SDOT;
    public static MethodHandle CUBLAS_CDOT;
    public static MethodHandle CUBLAS_ZDOTU;
    public static MethodHandle CUBLAS_DAXPY;
    public static MethodHandle CUBLAS_SAXPY;
    public static MethodHandle CUBLAS_CAXPY;
    public static MethodHandle CUBLAS_ZAXPY;
    public static MethodHandle CUBLAS_DSCAL;
    public static MethodHandle CUBLAS_SSCAL;
    public static MethodHandle CUBLAS_CSCAL;
    public static MethodHandle CUBLAS_ZSCAL;
    public static MethodHandle CUBLAS_SNRM2;
    public static MethodHandle CUBLAS_SCNRM2;
    public static MethodHandle CUBLAS_DNRM2;
    public static MethodHandle CUBLAS_DZNRM2;
    public static MethodHandle CUBLAS_STRSM;
    public static MethodHandle CUBLAS_DTRSM;
    public static MethodHandle CUBLAS_CTRSM;
    public static MethodHandle CUBLAS_ZTRSM;
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
    public static MethodHandle CUSPARSE_CSR2CSC_BUFFER_SIZE;
    public static MethodHandle CUSPARSE_CSR2CSC;
    public static MethodHandle CUSPARSE_SPARSE_TO_DENSE_BUFFER_SIZE;
    public static MethodHandle CUSPARSE_SPARSE_TO_DENSE;
    public static MethodHandle CUSPARSE_DENSE_TO_SPARSE_BUFFER_SIZE;
    public static MethodHandle CUSPARSE_DENSE_TO_SPARSE;
    public static MethodHandle CUSPARSE_GET_SIZE;
    public static MethodHandle CUSPARSE_SET_POINTERS;
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
    public static MethodHandle CUSOLVER_SGETRF_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_SGETRF;
    public static MethodHandle CUSOLVER_SGETRS;
    public static MethodHandle CUSOLVER_CGETRF_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_CGETRF;
    public static MethodHandle CUSOLVER_CGETRS;
    
    // QR
    public static MethodHandle CUSOLVER_SGEQRF_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_SGEQRF;
    public static MethodHandle CUSOLVER_DGEQRF_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_DGEQRF;
    
    // SVD
    public static MethodHandle CUSOLVER_SGESVD_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_SGESVD;
    public static MethodHandle CUSOLVER_DGESVD_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_DGESVD;
    public static MethodHandle CUSOLVER_ZGEQRF_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_ZGEQRF;
    public static MethodHandle CUSOLVER_CGEQRF_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_CGEQRF;
    public static MethodHandle CUSOLVER_ZGESVD_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_ZGESVD;
    public static MethodHandle CUSOLVER_CGESVD_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_CGESVD;
    
    // Cholesky
    public static MethodHandle CUSOLVER_SPOTRF_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_SPOTRF;
    public static MethodHandle CUSOLVER_DPOTRF_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_DPOTRF;
    public static MethodHandle CUSOLVER_ZPOTRF_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_ZPOTRF;
    public static MethodHandle CUSOLVER_CPOTRF_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_CPOTRF;
    
    // Eigen
    public static MethodHandle CUSOLVER_SSYEVD_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_SSYEVD;
    public static MethodHandle CUSOLVER_DSYEVD_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_DSYEVD;
    public static MethodHandle CUSOLVER_ZHEEVD_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_ZHEEVD;
    public static MethodHandle CUSOLVER_CHEEVD_BUFFER_SIZE;
    public static MethodHandle CUSOLVER_CHEEVD;

    public static MethodHandle CUSOLVER_STATUS_GET_STRING;

    private CUDAManager() {}

    public static synchronized void ensureInitialized() {
        if (initialized) return;

        if (Boolean.getBoolean("episteme.native.disable") || 
            Boolean.getBoolean("episteme.backend.gpu.disabled") ||
            Boolean.getBoolean("episteme.native.skip.cuda") || 
            Boolean.getBoolean("episteme.backend.cuda.disabled")) {
            logger.info("CUDA: Initialization skipped as requested by system property.");
            initialized = false;
            return;
        }

        initialized = true;

        try {
            cudaLookup = NativeFFMLoader.loadLibrary("cuda", managerArena).orElse(null);
            cudartLookup = NativeFFMLoader.loadLibrary("cudart", managerArena).orElse(null);
            
            if (cudaLookup == null && cudartLookup == null) {
                logger.info("CUDA libraries (cuda/cudart) not found. GPU acceleration via CUDA disabled.");
                return;
            }

            bindSymbols();

            try (Arena temp = Arena.ofConfined()) {
                // Check if any CUDA devices are present
                if (CUDA_GET_DEVICE_COUNT != null) {
                    MemorySegment countPtr = temp.allocate(ValueLayout.JAVA_INT);
                    try {
                        int res = (int) NativeSafe.invoke(CUDA_GET_DEVICE_COUNT, countPtr);
                        if (res != 0 || countPtr.get(ValueLayout.JAVA_INT, 0) == 0) {
                            logger.info("No CUDA devices found (count: {}). CUDA backends disabled.", countPtr.get(ValueLayout.JAVA_INT, 0));
                            return;
                        }
                    } catch (Throwable t) {
                        logger.warn("Failed to query CUDA device count: {}. Assuming no GPU.", t.getMessage());
                        return;
                    }
                }

                cublasLookup = NativeFFMLoader.loadLibrary("cublas", managerArena).orElse(null);
                cusparseLookup = NativeFFMLoader.loadLibrary("cusparse", managerArena).orElse(null);
                cusolverLookup = NativeFFMLoader.loadLibrary("cusolver", managerArena).orElse(null);

                MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
                
                if (CUBLAS_CREATE != null) {
                    try {
                        if ((int) NativeSafe.invoke(CUBLAS_CREATE, p) == 0) {
                            cublasHandle = p.get(ValueLayout.ADDRESS, 0);
                        }
                    } catch (Throwable t) {
                        logger.error("Failed to create cuBLAS handle: {}", t.getMessage());
                    }
                }
                
                if (CUSPARSE_CREATE != null) {
                    try {
                        if ((int) NativeSafe.invoke(CUSPARSE_CREATE, p) == 0) {
                            cusparseHandle = p.get(ValueLayout.ADDRESS, 0);
                        }
                    } catch (Throwable t) {
                        logger.error("Failed to create cuSPARSE handle: {}", t.getMessage());
                    }
                }
                
                if (useCusolver && CUSOLVER_CREATE != null) {
                    try {
                        if ((int) NativeSafe.invoke(CUSOLVER_CREATE, p) == 0) {
                            cusolverHandle = p.get(ValueLayout.ADDRESS, 0);
                        } else {
                            useCusolver = false;
                        }
                    } catch (Throwable t) {
                        logger.error("Failed to create cuSolver handle: {}", t.getMessage());
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
        CUBLAS_CGEMM = lookup(cublasLookup, "cublasCgemm_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT
        ));
        
        CUBLAS_SGEMV = lookup(cublasLookup, "cublasSgemv_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT
        ));
        CUBLAS_DGEMV = lookup(cublasLookup, "cublasDgemv_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT
        ));
        CUBLAS_CGEMV = lookup(cublasLookup, "cublasCgemv_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT
        ));
        CUBLAS_ZGEMV = lookup(cublasLookup, "cublasZgemv_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT
        ));
        
        CUBLAS_SGEAM = lookup(cublasLookup, "cublasSgeam", FunctionDescriptor.of(ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.JAVA_INT
        ));
        
        CUBLAS_DGEAM = lookup(cublasLookup, "cublasDgeam", FunctionDescriptor.of(ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.JAVA_INT
        ));
        CUBLAS_ZGEAM = lookup(cublasLookup, "cublasZgeam", FunctionDescriptor.of(ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.JAVA_INT
        ));
        CUBLAS_CGEAM = lookup(cublasLookup, "cublasCgeam", FunctionDescriptor.of(ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
            ValueLayout.JAVA_INT
        ));
        CUBLAS_DDOT = lookup(cublasLookup, "cublasDdot_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUBLAS_SDOT = lookup(cublasLookup, "cublasSdot_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUBLAS_CDOT = lookup(cublasLookup, "cublasCdotu_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUBLAS_DAXPY = lookup(cublasLookup, "cublasDaxpy_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_SAXPY = lookup(cublasLookup, "cublasSaxpy_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_CAXPY = lookup(cublasLookup, "cublasCaxpy_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_ZAXPY = lookup(cublasLookup, "cublasZaxpy_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_DSCAL = lookup(cublasLookup, "cublasDscal_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_SSCAL = lookup(cublasLookup, "cublasSscal_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_CSCAL = lookup(cublasLookup, "cublasCscal_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_ZSCAL = lookup(cublasLookup, "cublasZscal_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_SNRM2 = lookup(cublasLookup, "cublasSnrm2_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUBLAS_SCNRM2 = lookup(cublasLookup, "cublasScnrm2_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUBLAS_DNRM2 = lookup(cublasLookup, "cublasDnrm2_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUBLAS_DZNRM2 = lookup(cublasLookup, "cublasDznrm2_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUBLAS_ZDOTU = lookup(cublasLookup, "cublasZdotu_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUBLAS_STRSM = lookup(cublasLookup, "cublasStrsm_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_DTRSM = lookup(cublasLookup, "cublasDtrsm_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_CTRSM = lookup(cublasLookup, "cublasCtrsm_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
        CUBLAS_ZTRSM = lookup(cublasLookup, "cublasZtrsm_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
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
        CUSPARSE_CSR2CSC_BUFFER_SIZE = lookup(cusparseLookup, "cusparseCsr2cscEx2_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_CSR2CSC = lookup(cusparseLookup, "cusparseCsr2cscEx2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_SPARSE_TO_DENSE_BUFFER_SIZE = lookup(cusparseLookup, "cusparseSparseToDense_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_SPARSE_TO_DENSE = lookup(cusparseLookup, "cusparseSparseToDense", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_DENSE_TO_SPARSE_BUFFER_SIZE = lookup(cusparseLookup, "cusparseDenseToSparse_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_DENSE_TO_SPARSE = lookup(cusparseLookup, "cusparseDenseToSparse", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSPARSE_GET_SIZE = lookup(cusparseLookup, "cusparseSpMatGetSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        CUSPARSE_SET_POINTERS = lookup(cusparseLookup, "cusparseCsrSetPointers", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
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
        CUSOLVER_SGETRF_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnSgetrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_SGETRF = lookup(cusolverLookup, "cusolverDnSgetrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        CUSOLVER_SGETRS = lookup(cusolverLookup, "cusolverDnSgetrs", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_CGETRF_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnCgetrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_CGETRF = lookup(cusolverLookup, "cusolverDnCgetrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        CUSOLVER_CGETRS = lookup(cusolverLookup, "cusolverDnCgetrs", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
            ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        
        // QR
        CUSOLVER_SGEQRF_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnSgeqrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_SGEQRF = lookup(cusolverLookup, "cusolverDnSgeqrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_DGEQRF_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnDgeqrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_DGEQRF = lookup(cusolverLookup, "cusolverDnDgeqrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        
        // SVD
        CUSOLVER_SGESVD_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnSgesvd_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_SGESVD = lookup(cusolverLookup, "cusolverDnSgesvd", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_DGESVD_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnDgesvd_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_DGESVD = lookup(cusolverLookup, "cusolverDnDgesvd", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        
        CUSOLVER_ZGEQRF_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnZgeqrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_ZGEQRF = lookup(cusolverLookup, "cusolverDnZgeqrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_CGEQRF_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnCgeqrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_CGEQRF = lookup(cusolverLookup, "cusolverDnCgeqrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        
        CUSOLVER_ZGESVD_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnZgesvd_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_ZGESVD = lookup(cusolverLookup, "cusolverDnZgesvd", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_CGESVD_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnCgesvd_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_CGESVD = lookup(cusolverLookup, "cusolverDnCgesvd", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

        // Cholesky
        CUSOLVER_SPOTRF_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnSpotrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_SPOTRF = lookup(cusolverLookup, "cusolverDnSpotrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_DPOTRF_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnDpotrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_DPOTRF = lookup(cusolverLookup, "cusolverDnDpotrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_ZPOTRF_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnZpotrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_ZPOTRF = lookup(cusolverLookup, "cusolverDnZpotrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_CPOTRF_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnCpotrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_CPOTRF = lookup(cusolverLookup, "cusolverDnCpotrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

        // Eigen
        CUSOLVER_SSYEVD_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnSsyevd_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        CUSOLVER_SSYEVD = lookup(cusolverLookup, "cusolverDnSsyevd", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_DSYEVD_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnDsyevd_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        CUSOLVER_DSYEVD = lookup(cusolverLookup, "cusolverDnDsyevd", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_ZHEEVD_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnZheevd_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        CUSOLVER_ZHEEVD = lookup(cusolverLookup, "cusolverDnZheevd", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        CUSOLVER_CHEEVD_BUFFER_SIZE = lookup(cusolverLookup, "cusolverDnCheevd_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        CUSOLVER_CHEEVD = lookup(cusolverLookup, "cusolverDnCheevd", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

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
