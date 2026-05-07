/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.episteme.core.mathematics.linearalgebra.vectors.storage.VectorStorage;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.HeapRealDoubleMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.structures.rings.FieldElement;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.numbers.real.RealFloat;
import org.episteme.core.mathematics.numbers.real.RealDouble;
import com.google.auto.service.AutoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * High-performance Native CUDA Dense Linear Algebra Backend using FFM API.
 * Leverages NVIDIA cuBLAS and cuSolver for matrix operations.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, GPUBackend.class})
public class NativeCUDADenseLinearAlgebraBackend<E extends FieldElement<E>> implements LinearAlgebraProvider<E>, NativeBackend, GPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(NativeCUDADenseLinearAlgebraBackend.class);
    private static boolean IS_AVAILABLE = false;
    private static boolean USE_CUSOLVER = false; // Disabled for audit stability due to AWS/libcusolver crashes
    private static final Arena SHARED_ARENA = Arena.ofShared();

    // Native Library Lookups
    private static SymbolLookup cublas_lookup;
    private static SymbolLookup cusolver_lookup;
    private static SymbolLookup cuda_lookup;

    private static final Linker LINKER = Linker.nativeLinker();

    // CUDA Driver API
    private static MethodHandle CUDA_MALLOC;
    private static MethodHandle CUDA_FREE;
    private static MethodHandle CUDA_MEMCPY_H_TO_D;
    private static MethodHandle CUDA_MEMCPY_D_TO_H;
    private static MethodHandle CUDA_DEVICE_SYNCHRONIZE;
    private static MethodHandle CUDA_GET_ERROR_STRING;
    private static MethodHandle CU_CTX_GET_CURRENT;
    private static MethodHandle CU_CTX_GET_DEVICE;

    private static final int CUDA_SUCCESS = 0;

    // cuBLAS API
    private static MethodHandle CUBLAS_CREATE;
    private static MethodHandle CUBLAS_DESTROY;
    private static MethodHandle CUBLAS_DGEMM;
    private static MethodHandle CUBLAS_DGEAM;
    private static MethodHandle CUBLAS_DDOT;
    private static MethodHandle CUBLAS_DNRM2;
    private static MethodHandle CUBLAS_SGEMM;
    private static MethodHandle CUBLAS_ZGEMM;
    private static MethodHandle CUBLAS_ZGEAM;
    private static MethodHandle CUBLAS_STATUS_GET_STRING;

    // cuSolver API
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
    private static MethodHandle CUSOLVER_DSYEVJ_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_DSYEVJ;
    private static MethodHandle CUSOLVER_STATUS_GET_STRING;
    
    // Static Handles for Caching
    private static MemorySegment staticCublasHandle;
    private static MemorySegment staticCusolverHandle;

    
    // cuSolver Complex
    private static MethodHandle CUSOLVER_ZGETRF_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_ZGETRF;
    private static MethodHandle CUSOLVER_ZGETRS;
    private static MethodHandle CUSOLVER_ZGEQRF_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_ZGEQRF;
    private static MethodHandle CUSOLVER_ZORGQR_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_ZORGQR;
    private static MethodHandle CUSOLVER_ZGESVD_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_ZGESVD;
    private static MethodHandle CUSOLVER_ZHEEVD_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_ZHEEVD;
    private static MethodHandle CUSOLVER_ZPOTRF_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_ZPOTRF;

    private static synchronized void ensureInitialized() {
        if (IS_AVAILABLE) return;
        
        try {
            System.err.println("[CUDA-Init] Loading libraries...");
            cuda_lookup = NativeFFMLoader.loadLibrary("cuda", SHARED_ARENA).orElse(null);
            cublas_lookup = NativeFFMLoader.loadLibrary("cublas", SHARED_ARENA).orElse(null);
            cusolver_lookup = NativeFFMLoader.loadLibrary("cusolver", SHARED_ARENA).orElse(null);

            if (cuda_lookup == null || cublas_lookup == null || cusolver_lookup == null) {
                System.err.println("[CUDA-Init] ABORT: Failed to load one or more CUDA libraries (CUDA/cuBLAS/cuSolver).");
                return;
            }

            System.err.println("[CUDA-Init] Binding symbols...");
            // CUDA Symbols
            CUDA_MALLOC = lookup(cuda_lookup, "cuMemAlloc_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            CUDA_FREE = lookup(cuda_lookup, "cuMemFree_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG));
            CUDA_MEMCPY_H_TO_D = lookup(cuda_lookup, "cuMemcpyHtoD_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            if (CUDA_MEMCPY_H_TO_D == null) CUDA_MEMCPY_H_TO_D = lookup(cuda_lookup, "cuMemcpyHtoD", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            
            CUDA_MEMCPY_D_TO_H = lookup(cuda_lookup, "cuMemcpyDtoH_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG));
            if (CUDA_MEMCPY_D_TO_H == null) CUDA_MEMCPY_D_TO_H = lookup(cuda_lookup, "cuMemcpyDtoH", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG));
            
            CUDA_DEVICE_SYNCHRONIZE = lookup(cuda_lookup, "cuCtxSynchronize", FunctionDescriptor.of(ValueLayout.JAVA_INT));
            CUDA_GET_ERROR_STRING = lookup(cuda_lookup, "cuGetErrorString", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CU_CTX_GET_CURRENT = lookup(cuda_lookup, "cuCtxGetCurrent", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CU_CTX_GET_DEVICE = lookup(cuda_lookup, "cuCtxGetDevice", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            // cuBLAS Symbols
            CUBLAS_CREATE = lookup(cublas_lookup, "cublasCreate_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUBLAS_DESTROY = lookup(cublas_lookup, "cublasDestroy_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUBLAS_DGEMM = lookup(cublas_lookup, "cublasDgemm_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT
            ));

            CUBLAS_DGEAM = lookup(cublas_lookup, "cublasDgeam_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT
            ));
            if (CUBLAS_DGEAM == null) {
                CUBLAS_DGEAM = lookup(cublas_lookup, "cublasDgeam", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.JAVA_INT
                ));
            }

            CUBLAS_DDOT = lookup(cublas_lookup, "cublasDdot_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUBLAS_SGEMM = lookup(cublas_lookup, "cublasSgemm_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT
            ));


            CUBLAS_ZGEMM = lookup(cublas_lookup, "cublasZgemm_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT
            ));
            CUBLAS_ZGEAM = lookup(cublas_lookup, "cublasZgeam_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT
            ));
            if (CUBLAS_ZGEAM == null) {
                CUBLAS_ZGEAM = lookup(cublas_lookup, "cublasZgeam", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                    ValueLayout.JAVA_INT
                ));
            }

            CUBLAS_STATUS_GET_STRING = lookup(cublas_lookup, "cublasGetStatusString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            if (CUBLAS_STATUS_GET_STRING == null) CUBLAS_STATUS_GET_STRING = lookup(cublas_lookup, "cublasGetErrorString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

            // cuSolver Core
            CUSOLVER_DGETRF_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnDgetrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_DGETRF = lookup(cusolver_lookup, "cusolverDnDgetrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            CUSOLVER_DGETRS = lookup(cusolver_lookup, "cusolverDnDgetrs", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            CUSOLVER_DGEQRF_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnDgeqrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_DGEQRF = lookup(cusolver_lookup, "cusolverDnDgeqrf", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_DORGQR_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnDorgqr_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            CUSOLVER_DORGQR = lookup(cusolver_lookup, "cusolverDnDorgqr", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_DGESVD_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnDgesvd_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_DGESVD = lookup(cusolver_lookup, "cusolverDnDgesvd", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            
            CUSOLVER_DPOTRF_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnDpotrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_DPOTRF = lookup(cusolver_lookup, "cusolverDnDpotrf", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            CUSOLVER_DSYEVJ_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnDsyevj_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            CUSOLVER_DSYEVJ = lookup(cusolver_lookup, "cusolverDnDsyevj", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            CUSOLVER_CREATE = lookup(cusolver_lookup, "cusolverDnCreate", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_DESTROY = lookup(cusolver_lookup, "cusolverDnDestroy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_STATUS_GET_STRING = lookup(cusolver_lookup, "cusolverGetStatusString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            
            if (CUSOLVER_CREATE == null || CUSOLVER_DESTROY == null || CUBLAS_DGEAM == null) {
                 System.err.println("[CUDA-Init] ABORT: Essential cuSolver or cuBLAS symbols missing.");
                 return;
            }

            System.err.println("[CUDA-Init] Binding complex decomposition symbols...");
            CUSOLVER_ZGETRF_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnZgetrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_ZGETRF = lookup(cusolver_lookup, "cusolverDnZgetrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            CUSOLVER_ZGETRS = lookup(cusolver_lookup, "cusolverDnZgetrs", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            CUSOLVER_ZGEQRF_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnZgeqrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_ZGEQRF = lookup(cusolver_lookup, "cusolverDnZgeqrf", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_ZORGQR_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnZungqr_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            CUSOLVER_ZORGQR = lookup(cusolver_lookup, "cusolverDnZungqr", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUBLAS_DNRM2 = lookup(cublas_lookup, "cublasDnrm2_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUSOLVER_ZGESVD_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnZgesvd_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_ZGESVD = lookup(cusolver_lookup, "cusolverDnZgesvd", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_BYTE, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            
            CUSOLVER_ZHEEVD_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnZheevd_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            CUSOLVER_ZHEEVD = lookup(cusolver_lookup, "cusolverDnZheevd", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            CUSOLVER_ZPOTRF_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnZpotrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_ZPOTRF = lookup(cusolver_lookup, "cusolverDnZpotrf", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
            System.err.println("[CUDA-Init] Initializing cached handles...");
            try (Arena temp = Arena.ofConfined()) {
                MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
                
                // Create cuBLAS handle
                if (CUBLAS_CREATE != null) {
                    int status = (int) NativeSafe.invoke(CUBLAS_CREATE, p);
                    if (status == 0) {
                        staticCublasHandle = p.get(ValueLayout.ADDRESS, 0).reinterpret(0);
                        staticCublasHandle = MemorySegment.ofAddress(staticCublasHandle.address()); 
                    } else {
                        System.err.println("[CUDA-Init] ABORT: Failed to create cuBLAS handle. Status: " + status);
                        return;
                    }
                }

                // Create cuSolver handle
                if (USE_CUSOLVER && CUSOLVER_CREATE != null) {
                    try {
                        int status = (int) NativeSafe.invoke(CUSOLVER_CREATE, p);
                        if (status == 0) {
                            staticCusolverHandle = p.get(ValueLayout.ADDRESS, 0).reinterpret(0);
                            staticCusolverHandle = MemorySegment.ofAddress(staticCusolverHandle.address());
                        } else {
                            System.err.println("[CUDA-Init] WARNING: Failed to create cuSolver handle. Disabling cuSolver features.");
                            USE_CUSOLVER = false;
                        }
                    } catch (Throwable t) {
                        System.err.println("[CUDA-Init] WARNING: cuSolver initialization crashed: " + t.getMessage());
                        USE_CUSOLVER = false;
                    }
                }

            }

            IS_AVAILABLE = true;
            System.err.println("[CUDA-Init] SUCCESS: Backend is AVAILABLE. cuSolver: " + USE_CUSOLVER);
            logger.info("Native CUDA/cuBLAS Backend initialized successfully. cuSolver enabled: {}", USE_CUSOLVER);
        } catch (Throwable t) {
            logger.warn("Failed to initialize CUDA Backend: {} - {}", t.getClass().getSimpleName(), t.getMessage());
        }
    }

    private static MethodHandle lookup(SymbolLookup lookup, String name, FunctionDescriptor desc) {
        if (lookup == null) return null;
        return NativeFFMLoader.findSymbol(lookup, name)
            .map(s -> LINKER.downcallHandle(s, desc))
            .orElse(null);
    }

    @Override
    public boolean isAvailable() { 
        ensureInitialized(); 
        return IS_AVAILABLE && !isExplicitlyDisabled(); 
    }

    @Override public String getId() { return "cuda-dense"; }
    @Override public String getType() { return "math"; }
    @Override public boolean isLoaded() { return IS_AVAILABLE; }
    @Override public String getEnvironmentInfo() { return IS_AVAILABLE ? "GPU (CUDA)" : "N/A"; }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        if (!isAvailable()) return null;
        try (Arena arena = Arena.ofConfined()) {
            int devId = 0;
            MemorySegment pCtx = arena.allocate(ValueLayout.ADDRESS);
            long ctxHandle = 0;
            if (CU_CTX_GET_CURRENT != null && (int) NativeSafe.invoke(CU_CTX_GET_CURRENT, pCtx) == 0) {
                ctxHandle = pCtx.get(ValueLayout.ADDRESS, 0).address();
            }
            MemorySegment pDev = arena.allocate(ValueLayout.JAVA_INT);
            if (CU_CTX_GET_DEVICE != null && (int) NativeSafe.invoke(CU_CTX_GET_DEVICE, pDev) == 0) {
                devId = pDev.get(ValueLayout.JAVA_INT, 0);
            }
            return org.episteme.nativ.technical.backend.gpu.cuda.CUDAExecutionContext.fromNative(ctxHandle, devId);
        }
 catch (Throwable t) {
            return new org.episteme.nativ.technical.backend.gpu.cuda.CUDAExecutionContext(null, null);
        }
    }

    private MemorySegment malloc(long size, ResourceTracker tracker) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
            checkCuda((int) NativeSafe.invoke(CUDA_MALLOC, p, size));
            MemorySegment d = p.get(ValueLayout.ADDRESS, 0);
            return tracker.track(d, ptr -> {
                try { checkCuda((int) NativeSafe.invoke(CUDA_FREE, ptr.address())); } catch (Throwable t) { logger.error("Failed to free GPU memory: {}", t.getMessage()); }
            });
        } catch (Throwable t) { throw new RuntimeException("CUDA malloc failed", t); }
    }

    private static MemorySegment createCublasHandle(ResourceTracker tracker) {
        if (staticCublasHandle == null) throw new IllegalStateException("cuBLAS handle not initialized");
        return staticCublasHandle;
    }

    private static MemorySegment createCusolverHandle(ResourceTracker tracker) {
        if (!USE_CUSOLVER || staticCusolverHandle == null) throw new UnsupportedOperationException("cuSolver not available");
        return staticCusolverHandle;
    }

    @Override
    public String getName() {
        return "Native CUDA Dense Linear Algebra Backend";
    }
    @Override public int getPriority() { return 110; }
    @Override 
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) { 
        if (ring == null) return false;
        Object zero = ring.zero();
        return zero instanceof Real || zero instanceof Complex;
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for transpose()");
        if (isComplex(a)) return transposeComplex(a);
        int rows = a.rows(); int cols = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) rows * cols * 8, tracker);
            MemorySegment d_C = malloc((long) rows * cols * 8, tracker);
            
            MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_A.address(), hostA, (long) rows * cols * 8));
            
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) NativeSafe.invoke(CUBLAS_DGEAM, handle, 1, 0, rows, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), d_A, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), d_A, cols, 
                d_C, rows));
            
            double[] result = new double[rows * cols];
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) rows * cols);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, host, d_C.address(), (long) rows * cols * 8));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, result, 0, rows * cols);
            return toMatrix(result, cols, rows);
        } catch (Throwable t) { throw new RuntimeException("CUDA transpose failed", t); }

    }

    private Matrix<E> transposeComplex(Matrix<E> a) {
        int rows = a.rows(); int cols = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) rows * cols * 16, tracker);
            MemorySegment d_C = malloc((long) rows * cols * 16, tracker);
            
            MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(a));
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_A.address(), hostA, (long) rows * cols * 16));
            
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) NativeSafe.invoke(CUBLAS_ZGEAM, handle, 1, 0, rows, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0), d_A, cols, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0), d_A, cols, 
                d_C, rows));
            
            double[] result = new double[rows * cols * 2];
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) rows * cols * 2);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, host, d_C.address(), (long) rows * cols * 16));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, result, 0, rows * cols * 2);
            return toMatrixComplex(result, cols, rows);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex transpose failed", t); }
    }


    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for multiply()");
        if (isComplex(a)) return multiplyComplex(a, b);
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        boolean isFloat = isFloat(a);
        long elementSize = isFloat ? 4 : 8;
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * elementSize, tracker);
            MemorySegment d_B = malloc((long) k * n * elementSize, tracker);
            MemorySegment d_C = malloc((long) m * n * elementSize, tracker);
            
            if (isFloat) {
                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(a));
                MemorySegment hostB = arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(b));
                checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_A.address(), hostA, (long) m * k * 4));
                checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_B.address(), hostB, (long) k * n * 4));
                MemorySegment handle = createCublasHandle(tracker);
                checkCublas((int) NativeSafe.invoke(CUBLAS_SGEMM, handle, 0, 0, n, m, k, arena.allocateFrom(ValueLayout.JAVA_FLOAT, 1.0f), d_B, n, d_A, k, arena.allocateFrom(ValueLayout.JAVA_FLOAT, 0.0f), d_C, n));
                float[] h_C = new float[m * n];
                MemorySegment segC = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m * n);
                checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, segC, d_C.address(), (long) m * n * 4));
                MemorySegment.copy(segC, ValueLayout.JAVA_FLOAT, 0, h_C, 0, m * n);
                return fromFloatArray(h_C, m, n);
            } else {
                MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
                MemorySegment hostB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
                checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_A.address(), hostA, (long) m * k * 8));
                checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_B.address(), hostB, (long) k * n * 8));
                MemorySegment handle = createCublasHandle(tracker);
                checkCublas((int) NativeSafe.invoke(CUBLAS_DGEMM, handle, 0, 0, n, m, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), d_B, n, d_A, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), d_C, n));
                double[] h_C = new double[m * n];
                MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
                checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, segC, d_C.address(), (long) m * n * 8));
                MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, h_C, 0, m * n);
                return toMatrix(h_C, m, n);
            }
        } catch (Throwable t) { throw new RuntimeException("CUDA multiply failed", t); }

    }

    private Matrix<E> multiplyComplex(Matrix<E> a, Matrix<E> b) {
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * 16, tracker);
            MemorySegment d_B = malloc((long) k * n * 16, tracker);
            MemorySegment d_C = malloc((long) m * n * 16, tracker);
            
            MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(a));
            MemorySegment hostB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(b));
            
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_A.address(), hostA, (long) m * k * 16));
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_B.address(), hostB, (long) k * n * 16));
            
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) NativeSafe.invoke(CUBLAS_ZGEMM, handle, 0, 0, n, m, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0), d_B, n, d_A, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0), d_C, n));
            
            double[] resultData = new double[m * n * 2];
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, host, d_C.address(), (long) m * n * 16));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, resultData, 0, m * n * 2);
            return toMatrixComplex(resultData, m, n);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex multiply failed", t); }

    }

    @Override public Matrix<E> add(Matrix<E> a, Matrix<E> b) { if (isComplex(a)) return geamComplex(a, b, org.episteme.core.mathematics.numbers.complex.Complex.ONE, org.episteme.core.mathematics.numbers.complex.Complex.ONE); return geam(a, b, 1.0, 1.0); }
    @Override public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) { if (isComplex(a)) return geamComplex(a, b, org.episteme.core.mathematics.numbers.complex.Complex.ONE, org.episteme.core.mathematics.numbers.complex.Complex.ONE.negate()); return geam(a, b, 1.0, -1.0); }
    @Override public Matrix<E> scale(E scalar, Matrix<E> a) { if (isComplex(a)) return geamComplex(a, null, getComplexValue(scalar), org.episteme.core.mathematics.numbers.complex.Complex.ZERO); return geam(a, null, getRealValue(scalar), 0.0); }

    private Matrix<E> geam(Matrix<E> a, Matrix<E> b, double alphaVal, double betaVal) {
        int m = a.rows(); int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 8, tracker);
            MemorySegment d_C = malloc((long) m * n * 8, tracker);
            
            MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_A.address(), hostA, (long) m * n * 8));
            
            MemorySegment d_B = (b != null) ? malloc((long) m * n * 8, tracker) : d_A;
            if (b != null) {
                MemorySegment hostB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
                checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_B.address(), hostB, (long) m * n * 8));
            }
            
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) NativeSafe.invoke(CUBLAS_DGEAM, handle, 0, 0, n, m, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, alphaVal), d_A, n, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, betaVal), d_B, n, d_C, n));
            
            double[] h_C = new double[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, segC, d_C.address(), (long) m * n * 8));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, h_C, 0, m * n);
            return toMatrix(h_C, m, n);
        } catch (Throwable t) { throw new RuntimeException("CUDA GEAM failed", t); }
    }


    private Matrix<E> geamComplex(Matrix<E> a, Matrix<E> b, org.episteme.core.mathematics.numbers.complex.Complex alpha, org.episteme.core.mathematics.numbers.complex.Complex beta) {
        int m = a.rows(); int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 16, tracker);
            MemorySegment d_B = malloc((long) m * n * 16, tracker);
            MemorySegment d_C = malloc((long) m * n * 16, tracker);
            
            MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(a));
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_A.address(), hostA, (long) m * n * 16));
            
            if (b != null) {
                MemorySegment hostB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(b));
                checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_B.address(), hostB, (long) m * n * 16));
            }
            
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) NativeSafe.invoke(CUBLAS_ZGEAM, handle, 0, 0, n, m, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, alpha.getReal().doubleValue(), alpha.getImaginary().doubleValue()), d_A, n, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, beta.getReal().doubleValue(), beta.getImaginary().doubleValue()), d_B, n, d_C, n));
            
            double[] resultData = new double[m * n * 2];
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, host, d_C.address(), (long) m * n * 16));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, resultData, 0, m * n * 2);
            return toMatrixComplex(resultData, m, n);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex GEAM failed", t); }
    }


    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> x) {
        int m = a.rows(); int k = a.cols();
        Matrix<E> xMat = toMatrix(toDoubleVec(x), k, 1);
        Matrix<E> resMat = multiply(a, xMat);
        double[] resData = new double[m];
        for(int i=0; i<m; i++) resData[i] = getRealValue(resMat.get(i, 0));
        return toVector(resData);
    }

    @Override public Vector<E> add(Vector<E> a, Vector<E> b) { Matrix<E> mc = add(toMatrix(toDoubleVec(a), 1, a.dimension()), toMatrix(toDoubleVec(b), 1, b.dimension())); return toVector(toDoubleArray(mc)); }
    @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { Matrix<E> mc = subtract(toMatrix(toDoubleVec(a), 1, a.dimension()), toMatrix(toDoubleVec(b), 1, b.dimension())); return toVector(toDoubleArray(mc)); }
    @Override public Vector<E> multiply(Vector<E> v, E s) { Matrix<E> mc = scale(s, toMatrix(toDoubleVec(v), 1, v.dimension())); return toVector(toDoubleArray(mc)); }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for dot()");
        int n = a.dimension();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * 8, tracker);
            MemorySegment d_B = malloc((long) n * 8, tracker);
            
            MemorySegment hostA = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(a));
            MemorySegment hostB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(b));
            
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_A.address(), hostA, (long) n * 8));
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_B.address(), hostB, (long) n * 8));
            
            MemorySegment handle = createCublasHandle(tracker);
            MemorySegment result = arena.allocate(ValueLayout.JAVA_DOUBLE);
            checkCublas((int) NativeSafe.invoke(CUBLAS_DDOT, handle, n, d_A, 1, d_B, 1, result));
            return (E) Real.of(result.get(ValueLayout.JAVA_DOUBLE, 0));
        } catch (Throwable t) { throw new RuntimeException("CUDA dot failed", t); }

    }

    @Override
    public E norm(Vector<E> v) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for norm()");
        int n = v.dimension();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_V = malloc((long) n * 8, tracker);
            
            MemorySegment hostV = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(v));
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, d_V.address(), hostV, (long) n * 8));
            
            MemorySegment handle = createCublasHandle(tracker);
            MemorySegment result = arena.allocate(ValueLayout.JAVA_DOUBLE);
            checkCublas((int) NativeSafe.invoke(CUBLAS_DNRM2, handle, n, d_V, 1, result));
            return toScalar(result.get(ValueLayout.JAVA_DOUBLE, 0), v);
        } catch (Throwable t) { throw new RuntimeException("CUDA norm failed", t); }

    }

    @Override
    public Vector<E> normalize(Vector<E> v) {
        E n = norm(v);
        if (n.isZero()) return v;
        return multiply(v, n.inverse());
    }

    @Override
    public Vector<E> cross(Vector<E> a, Vector<E> b) {
        if (a.dimension() != 3 || b.dimension() != 3) throw new IllegalArgumentException("Cross product only for 3D vectors");
        double[] va = toDoubleVec(a);
        double[] vb = toDoubleVec(b);
        return toVector(new double[]{
            va[1]*vb[2] - va[2]*vb[1],
            va[2]*vb[0] - va[0]*vb[2],
            va[0]*vb[1] - va[1]*vb[0]
        });
    }

    @Override
    public E angle(Vector<E> a, Vector<E> b) {
        E d = dot(a, b);
        E na = norm(a);
        E nb = norm(b);
        return toScalar(Math.acos(getRealValue(d) / (getRealValue(na) * getRealValue(nb))), a);
    }

    @Override
    public Vector<E> projection(Vector<E> a, Vector<E> b) {
        E d = dot(a, b);
        E n2 = dot(b, b);
        return multiply(b, d.divide(n2));
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (!isAvailable() || !USE_CUSOLVER) throw new UnsupportedOperationException(getName() + ": CUDA/cuSolver not available for inverse()");
        if (isComplex(a)) return inverseComplex(a);
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 8, tracker);
            MemorySegment segB = malloc((long) n * n * 8, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            
            double[] h_A = toDoubleArray(a); 
            double[] h_At = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];
            
            double[] identity = new double[n * n]; 
            for (int i = 0; i < n; i++) identity[i * n + i] = 1.0;
            
            MemorySegment hostAt = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
            MemorySegment hostId = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, identity);
            
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, segA.address(), hostAt, (long) n * n * 8));
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, segB.address(), hostId, (long) n * n * 8));
            
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUSOLVER_DGETRF_BUFFER_SIZE, handle, n, n, segA, n, p_Lwork));
            
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCusolver((int) NativeSafe.invoke(CUSOLVER_DGETRF, handle, n, n, segA, n, segWork, segIpiv, segInfo));
            checkCusolver((int) NativeSafe.invoke(CUSOLVER_DGETRS, handle, 0, n, n, segA, n, segIpiv, segB, n, segInfo));
            
            double[] resData = new double[n * n];
            MemorySegment hostB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, hostB, segB.address(), (long) n * n * 8));
            MemorySegment.copy(hostB, ValueLayout.JAVA_DOUBLE, 0, resData, 0, n * n);
            
            double[] h_Res = new double[n * n]; 
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_Res[r * n + c] = resData[c * n + r];
            return toMatrix(h_Res, n, n);
        } catch (Throwable t) { throw new RuntimeException("CUDA inverse failed", t); }
    }


    private Matrix<E> inverseComplex(Matrix<E> a) {
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 16, tracker);
            MemorySegment segB = malloc((long) n * n * 16, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            
            double[] h_A = toComplexDoubleArray(a); 
            double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            
            double[] identity = new double[n * n * 2]; 
            for (int i = 0; i < n; i++) identity[(i * n + i) * 2] = 1.0;
            
            MemorySegment hostAt = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
            MemorySegment hostId = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, identity);
            
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, segA.address(), hostAt, (long) n * n * 16));
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, segB.address(), hostId, (long) n * n * 16));
            
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUSOLVER_ZGETRF_BUFFER_SIZE, handle, n, n, segA, n, p_Lwork));
            
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCusolver((int) NativeSafe.invoke(CUSOLVER_ZGETRF, handle, n, n, segA, n, segWork, segIpiv, segInfo));
            checkCusolver((int) NativeSafe.invoke(CUSOLVER_ZGETRS, handle, 0, n, n, segA, n, segIpiv, segB, n, segInfo));
            
            double[] resData = new double[n * n * 2];
            MemorySegment hostB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, hostB, segB.address(), (long) n * n * 16));
            MemorySegment.copy(hostB, ValueLayout.JAVA_DOUBLE, 0, resData, 0, n * n * 2);
            
            double[] h_Res = new double[n * n * 2]; 
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_Res[(r * n + c) * 2] = resData[(c * n + r) * 2]; h_Res[(r * n + c) * 2 + 1] = resData[(c * n + r) * 2 + 1]; }
            return toMatrixComplex(h_Res, n, n);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex inverse failed", t); }
    }


    @Override
    public E determinant(Matrix<E> a) {
        if (!isAvailable() || !USE_CUSOLVER) throw new UnsupportedOperationException(getName() + ": CUDA/cuSolver not available for determinant()");
        if (isComplex(a)) return determinantComplex(a);
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 8, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            
            double[] h_A = toDoubleArray(a); 
            double[] h_At = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];
            
            MemorySegment hostAt = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, segA.address(), hostAt, (long) n * n * 8));
            
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUSOLVER_DGETRF_BUFFER_SIZE, handle, n, n, segA, n, p_Lwork));
            
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCusolver((int) NativeSafe.invoke(CUSOLVER_DGETRF, handle, n, n, segA, n, segWork, segIpiv, segInfo));
            
            double[] h_LU = new double[n * n];
            MemorySegment hostLU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, hostLU, segA.address(), (long) n * n * 8));
            MemorySegment.copy(hostLU, ValueLayout.JAVA_DOUBLE, 0, h_LU, 0, n * n);
            
            int[] ipiv = new int[n];
            MemorySegment hostIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, hostIpiv, segIpiv.address(), (long) n * 4));
            MemorySegment.copy(hostIpiv, ValueLayout.JAVA_INT, 0, ipiv, 0, n);
            
            double det = 1.0;
            for (int i = 0; i < n; i++) {
                det *= h_LU[i * n + i];
                if (ipiv[i] != (i + 1)) det = -det;
            }
            return (E) Real.of(det);
        } catch (Throwable t) { throw new RuntimeException("CUDA determinant failed", t); }
    }

    private E determinantComplex(Matrix<E> a) {
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 16, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            
            double[] h_A = toComplexDoubleArray(a); 
            double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            
            MemorySegment hostAt = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_H_TO_D, segA.address(), hostAt, (long) n * n * 16));
            
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) NativeSafe.invoke(CUSOLVER_ZGETRF_BUFFER_SIZE, handle, n, n, segA, n, p_Lwork));
            
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCusolver((int) NativeSafe.invoke(CUSOLVER_ZGETRF, handle, n, n, segA, n, segWork, segIpiv, segInfo));
            
            double[] h_LU = new double[n * n * 2];
            MemorySegment hostLU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, hostLU, segA.address(), (long) n * n * 16));
            MemorySegment.copy(hostLU, ValueLayout.JAVA_DOUBLE, 0, h_LU, 0, n * n * 2);
            
            int[] ipiv = new int[n];
            MemorySegment hostIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
            checkCuda((int) NativeSafe.invoke(CUDA_MEMCPY_D_TO_H, hostIpiv, segIpiv.address(), (long) n * 4));
            MemorySegment.copy(hostIpiv, ValueLayout.JAVA_INT, 0, ipiv, 0, n);
            
            org.episteme.core.mathematics.numbers.complex.Complex det = org.episteme.core.mathematics.numbers.complex.Complex.ONE;
            for (int i = 0; i < n; i++) {
                det = det.multiply(org.episteme.core.mathematics.numbers.complex.Complex.of(h_LU[i * n * 2 + i * 2], h_LU[i * n * 2 + i * 2 + 1]));
                if (ipiv[i] != (i + 1)) det = det.negate();
            }
            return (E) det;
        } catch (Throwable t) { throw new RuntimeException("CUDA complex determinant failed", t); }
    }

    @Override
    public E trace(Matrix<E> A) {
        if (A.rows() != A.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = A.rows();
        if (isComplex(A)) {
            org.episteme.core.mathematics.numbers.complex.Complex sum = org.episteme.core.mathematics.numbers.complex.Complex.ZERO;
            for (int i = 0; i < n; i++) sum = sum.add((org.episteme.core.mathematics.numbers.complex.Complex) (Object) A.get(i, i));
            return (E) (Object) sum;
        }
        double sum = 0;
        if (A instanceof org.episteme.nativ.mathematics.linearalgebra.matrices.NativeRealDoubleMatrix nm) {
            MemorySegment seg = nm.segment();
            for (int i = 0; i < n; i++) {
                sum += seg.get(ValueLayout.JAVA_DOUBLE, (long) i * (n + 1) * 8);
            }
        } else {
            for (int i = 0; i < n; i++) {
                sum += getRealValue(A.get(i, i));
            }
        }
        return (E) Real.of(sum);
    }

    // --- Transcendental Operations (Host-side fallback for CUDA) ---

    @Override public Matrix<E> exp(Matrix<E> m) { return applyFunc(m, Math::exp, org.episteme.core.mathematics.numbers.complex.Complex::exp); }
    @Override public Matrix<E> log(Matrix<E> m) { return applyFunc(m, Math::log, org.episteme.core.mathematics.numbers.complex.Complex::log); }
    @Override public Matrix<E> log10(Matrix<E> m) { return applyFunc(m, Math::log10, org.episteme.core.mathematics.numbers.complex.Complex::log10); }
    @Override public Matrix<E> sin(Matrix<E> m) { return applyFunc(m, Math::sin, org.episteme.core.mathematics.numbers.complex.Complex::sin); }
    @Override public Matrix<E> cos(Matrix<E> m) { return applyFunc(m, Math::cos, org.episteme.core.mathematics.numbers.complex.Complex::cos); }
    @Override public Matrix<E> tan(Matrix<E> m) { return applyFunc(m, Math::tan, org.episteme.core.mathematics.numbers.complex.Complex::tan); }
    @Override public Matrix<E> asin(Matrix<E> m) { return applyFunc(m, Math::asin, c -> { throw new UnsupportedOperationException("asin not supported for Complex"); }); }
    @Override public Matrix<E> acos(Matrix<E> m) { return applyFunc(m, Math::acos, c -> { throw new UnsupportedOperationException("acos not supported for Complex"); }); }
    @Override public Matrix<E> atan(Matrix<E> m) { return applyFunc(m, Math::atan, c -> { throw new UnsupportedOperationException("atan not supported for Complex"); }); }
    @Override public Matrix<E> sinh(Matrix<E> m) { return applyFunc(m, Math::sinh, org.episteme.core.mathematics.numbers.complex.Complex::sinh); }
    @Override public Matrix<E> cosh(Matrix<E> m) { return applyFunc(m, Math::cosh, org.episteme.core.mathematics.numbers.complex.Complex::cosh); }
    @Override public Matrix<E> tanh(Matrix<E> m) { return applyFunc(m, Math::tanh, org.episteme.core.mathematics.numbers.complex.Complex::tanh); }
    @Override public Matrix<E> asinh(Matrix<E> m) { return applyFunc(m, x -> Math.log(x + Math.sqrt(x * x + 1.0)), c -> { throw new UnsupportedOperationException("asinh not supported for Complex"); }); }
    @Override public Matrix<E> acosh(Matrix<E> m) { return applyFunc(m, x -> Math.log(x + Math.sqrt(x * x - 1.0)), c -> { throw new UnsupportedOperationException("acosh not supported for Complex"); }); }
    @Override public Matrix<E> atanh(Matrix<E> m) { return applyFunc(m, x -> 0.5 * Math.log((1.0 + x) / (1.0 - x)), c -> { throw new UnsupportedOperationException("atanh not supported for Complex"); }); }
    @Override public Matrix<E> sqrt(Matrix<E> m) { return applyFunc(m, Math::sqrt, org.episteme.core.mathematics.numbers.complex.Complex::sqrt); }
    @Override public Matrix<E> cbrt(Matrix<E> m) { return applyFunc(m, Math::cbrt, org.episteme.core.mathematics.numbers.complex.Complex::cbrt); }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> pow(Matrix<E> m, E exponent) {
        int rows = m.rows();
        int cols = m.cols();
        boolean complex = isComplex(m);
        boolean single = isFloat(m);
        
        if (complex) {
            org.episteme.core.mathematics.numbers.complex.Complex exp = getComplexValue(exponent);
            org.episteme.core.mathematics.numbers.complex.Complex[][] res = new org.episteme.core.mathematics.numbers.complex.Complex[rows][cols];
            for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) res[i][j] = ((org.episteme.core.mathematics.numbers.complex.Complex) (Object) m.get(i, j)).pow(exp);
            return (Matrix<E>) (Object) Matrix.of(res, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>) (Object) m.getScalarRing());
        } else {
            double exp = getRealValue(exponent);
            if (single) {
                float[] data = toFloatArray(m);
                for (int i = 0; i < data.length; i++) data[i] = (float) Math.pow(data[i], exp);
                return fromFloatArray(data, rows, cols);
            } else {
                double[] data = toDoubleArray(m);
                for (int i = 0; i < data.length; i++) data[i] = Math.pow(data[i], exp);
                return toMatrix(data, rows, cols);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> applyFunc(Matrix<E> m, java.util.function.DoubleUnaryOperator realOp, java.util.function.UnaryOperator<org.episteme.core.mathematics.numbers.complex.Complex> complexOp) {
        int rows = m.rows();
        int cols = m.cols();
        boolean complex = isComplex(m);
        boolean single = isFloat(m);
        
        if (complex) {
            org.episteme.core.mathematics.numbers.complex.Complex[][] res = new org.episteme.core.mathematics.numbers.complex.Complex[rows][cols];
            for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) res[i][j] = complexOp.apply((org.episteme.core.mathematics.numbers.complex.Complex) (Object) m.get(i, j));
            return (Matrix<E>) (Object) Matrix.of(res, (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>) (Object) m.getScalarRing());
        } else {
            if (single) {
                float[] data = toFloatArray(m);
                for (int i = 0; i < data.length; i++) data[i] = (float) realOp.applyAsDouble(data[i]);
                return fromFloatArray(data, rows, cols);
            } else {
                double[] data = toDoubleArray(m);
                for (int i = 0; i < data.length; i++) data[i] = realOp.applyAsDouble(data[i]);
                return toMatrix(data, rows, cols);
            }
        }
    }

    private SVDResult<E> processSVDResult(int m, int n, double[] h_S, double[] h_U, double[] h_VT) {
        int k = Math.min(m, n);
        double[] h_Ur = new double[m * m]; for (int r = 0; r < m; r++) for (int c = 0; c < m; c++) h_Ur[r * m + c] = h_U[c * m + r];
        double[] h_VTr = new double[n * n]; for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_VTr[r * n + c] = h_VT[c * n + r];
        
        Matrix<E> U = toMatrix(h_Ur, m, m);
        Vector<E> S = toMatrix(h_S, k, 1).getColumn(0);
        Matrix<E> VT = toMatrix(h_VTr, n, n);
        
        return new SVDResult<>(U, S, VT);
    }

    private SVDResult<E> processSVDResultComplex(int m, int n, double[] h_S, double[] h_U, double[] h_VT) {
        int k = Math.min(m, n);
        double[] h_Ur = new double[m * m * 2]; for (int r = 0; r < m; r++) for (int c = 0; c < m; c++) { h_Ur[(r * m + c) * 2] = h_U[(c * m + r) * 2]; h_Ur[(r * m + c) * 2 + 1] = h_U[(c * m + r) * 2 + 1]; }
        double[] h_VTr = new double[n * n * 2]; for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_VTr[(r * n + c) * 2] = h_VT[(c * n + r) * 2]; h_VTr[(r * n + c) * 2 + 1] = h_VT[(c * n + r) * 2 + 1]; }
        return new SVDResult<>(toMatrixComplex(h_Ur, m, m), toMatrix(h_S, k, 1).getColumn(0), toMatrixComplex(h_VTr, n, n));
    }

    @Override
    public QRResult<E> qr(Matrix<E> a) {
        if (!isAvailable() || !USE_CUSOLVER) throw new UnsupportedOperationException(getName() + ": CUDA/cuSolver not available for qr()");
        if (isComplex(a)) return qrComplex(a);
        int m = a.rows(); int n = a.cols(); int k = Math.min(m, n);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) m * n * 8, tracker);
            MemorySegment segTau = malloc((long) k * 8, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toDoubleArray(a); double[] h_At = new double[m * n];
            for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) h_At[c * m + r] = h_A[r * n + c];
            checkCuda((int) CUDA_MEMCPY_H_TO_D.invokeExact(segA.address(), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) m * n * 8));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) CUSOLVER_DGEQRF_BUFFER_SIZE.invokeExact(handle, m, n, segA, m, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCusolver((int) CUSOLVER_DGEQRF.invokeExact(handle, m, n, segA, m, segTau, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segInfo));
            double[] h_QR = new double[m * n];
            MemorySegment hostQR = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostQR, segA.address(), (long) m * n * 8));
            MemorySegment.copy(hostQR, ValueLayout.JAVA_DOUBLE, 0, h_QR, 0, m * n);
            double[] rData = new double[m * n]; for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) { if (c >= r) rData[r * n + c] = h_QR[c * m + r]; else rData[r * n + c] = 0.0; }
            Matrix<E> R = toMatrix(rData, m, n);
            checkCusolver((int) CUSOLVER_DORGQR_BUFFER_SIZE.invokeExact(handle, m, k, k, segA, m, segTau, p_Lwork));
            int lworkQ = p_Lwork.get(ValueLayout.JAVA_INT, 0); segWork = malloc((long) lworkQ * 8, tracker);
            checkCusolver((int) CUSOLVER_DORGQR.invokeExact(handle, m, k, k, segA, m, segTau, segWork, lworkQ, segInfo));
            double[] h_Q = new double[m * k];
            MemorySegment hostQ = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * k);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostQ, segA.address(), (long) m * k * 8));
            MemorySegment.copy(hostQ, ValueLayout.JAVA_DOUBLE, 0, h_Q, 0, m * k);
            double[] qData = new double[m * k]; for (int r = 0; r < m; r++) for (int c = 0; c < k; c++) qData[r * k + c] = h_Q[c * m + r];
            return new QRResult<>(toMatrix(qData, m, k), R);
        } catch (Throwable t) { throw new RuntimeException("CUDA QR failed", t); }
    }

    private QRResult<E> qrComplex(Matrix<E> a) {
        int m = a.rows(); int n = a.cols(); int k = Math.min(m, n);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) m * n * 16, tracker);
            MemorySegment segTau = malloc((long) k * 16, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[m * n * 2];
            for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY_H_TO_D.invokeExact(segA.address(), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) m * n * 16));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) CUSOLVER_ZGEQRF_BUFFER_SIZE.invokeExact(handle, m, n, segA, m, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCusolver((int) CUSOLVER_ZGEQRF.invokeExact(handle, m, n, segA, m, segTau, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segInfo));
            double[] h_QR = new double[m * n * 2];
            MemorySegment hostQR = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostQR, segA.address(), (long) m * n * 16));
            MemorySegment.copy(hostQR, ValueLayout.JAVA_DOUBLE, 0, h_QR, 0, m * n * 2);
            double[] rData = new double[m * n * 2]; for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) { if (c >= r) { rData[(r * n + c) * 2] = h_QR[(c * m + r) * 2]; rData[(r * n + c) * 2 + 1] = h_QR[(c * m + r) * 2 + 1]; } }
            Matrix<E> R = toMatrixComplex(rData, m, n);
            checkCusolver((int) CUSOLVER_ZORGQR_BUFFER_SIZE.invokeExact(handle, m, k, k, segA, m, segTau, p_Lwork));
            int lworkQ = p_Lwork.get(ValueLayout.JAVA_INT, 0); segWork = malloc((long) lworkQ * 16, tracker);
            checkCusolver((int) CUSOLVER_ZORGQR.invokeExact(handle, m, k, k, segA, m, segTau, segWork, lworkQ, segInfo));
            double[] h_Q = new double[m * k * 2];
            MemorySegment hostQ = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * k * 2);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostQ, segA.address(), (long) m * k * 16));
            MemorySegment.copy(hostQ, ValueLayout.JAVA_DOUBLE, 0, h_Q, 0, m * k * 2);
            double[] qData = new double[m * k * 2]; for (int r = 0; r < m; r++) for (int c = 0; c < k; c++) { qData[(r * k + c) * 2] = h_Q[(c * m + r) * 2]; qData[(r * k + c) * 2 + 1] = h_Q[(c * m + r) * 2 + 1]; }
            return new QRResult<>(toMatrixComplex(qData, m, k), R);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex QR failed", t); }
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        if (!isAvailable() || !USE_CUSOLVER) throw new UnsupportedOperationException(getName() + ": CUDA/cuSolver not available for svd()");
        if (isComplex(a)) return svdComplex(a);
        int m = a.rows(); int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) m * n * 8, tracker);
            MemorySegment segS = malloc((long) Math.min(m, n) * 8, tracker);
            MemorySegment segU = malloc((long) m * m * 8, tracker);
            MemorySegment segVT = malloc((long) n * n * 8, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toDoubleArray(a); double[] h_At = new double[m * n];
            for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) h_At[c * m + r] = h_A[r * n + c];
            checkCuda((int) CUDA_MEMCPY_H_TO_D.invokeExact(segA.address(), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) m * n * 8));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) CUSOLVER_DGESVD_BUFFER_SIZE.invokeExact(handle, m, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCusolver((int) CUSOLVER_DGESVD.invokeExact(handle, (byte)'A', (byte)'A', m, n, segA, m, segS, segU, m, segVT, n, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), MemorySegment.NULL, segInfo));
            double[] h_S = new double[Math.min(m, n)]; MemorySegment hostS = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) h_S.length);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostS, segS.address(), (long) h_S.length * 8));
            MemorySegment.copy(hostS, ValueLayout.JAVA_DOUBLE, 0, h_S, 0, h_S.length);
            double[] h_U = new double[m * m]; MemorySegment hostU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * m);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostU, segU.address(), (long) m * m * 8));
            MemorySegment.copy(hostU, ValueLayout.JAVA_DOUBLE, 0, h_U, 0, m * m);
            double[] h_VT = new double[n * n]; MemorySegment hostVT = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostVT, segVT.address(), (long) n * n * 8));
            MemorySegment.copy(hostVT, ValueLayout.JAVA_DOUBLE, 0, h_VT, 0, n * n);
            return processSVDResult(m, n, h_S, h_U, h_VT);
        } catch (Throwable t) { throw new RuntimeException("CUDA SVD failed", t); }
    }

    private SVDResult<E> svdComplex(Matrix<E> a) {
        int m = a.rows(); int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) m * n * 16, tracker);
            MemorySegment segS = malloc((long) Math.min(m, n) * 8, tracker);
            MemorySegment segU = malloc((long) m * m * 16, tracker);
            MemorySegment segVT = malloc((long) n * n * 16, tracker);
            MemorySegment segRwork = malloc((long) Math.min(m, n) * 8, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[m * n * 2];
            for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY_H_TO_D.invokeExact(segA.address(), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) m * n * 16));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) CUSOLVER_ZGESVD_BUFFER_SIZE.invokeExact(handle, m, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCusolver((int) CUSOLVER_ZGESVD.invokeExact(handle, (byte)'A', (byte)'A', m, n, segA, m, segS, segU, m, segVT, n, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segRwork, segInfo));
            double[] h_S = new double[Math.min(m, n)]; MemorySegment hostS = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) h_S.length);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostS, segS.address(), (long) h_S.length * 8));
            MemorySegment.copy(hostS, ValueLayout.JAVA_DOUBLE, 0, h_S, 0, h_S.length);
            double[] h_U = new double[m * m * 2]; MemorySegment hostU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * m * 2);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostU, segU.address(), (long) m * m * 16));
            MemorySegment.copy(hostU, ValueLayout.JAVA_DOUBLE, 0, h_U, 0, m * m * 2);
            double[] h_VT = new double[n * n * 2]; MemorySegment hostVT = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostVT, segVT.address(), (long) n * n * 16));
            MemorySegment.copy(hostVT, ValueLayout.JAVA_DOUBLE, 0, h_VT, 0, n * n * 2);
            return processSVDResultComplex(m, n, h_S, h_U, h_VT);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex SVD failed", t); }
    }

    @Override
    public LUResult<E> lu(Matrix<E> a) {
        if (!isAvailable() || !USE_CUSOLVER) throw new UnsupportedOperationException(getName() + ": CUDA/cuSolver not available for lu()");
        if (isComplex(a)) return luComplex(a);
        int m = a.rows(); int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) m * n * 8, tracker);
            MemorySegment segIpiv = malloc((long) Math.min(m, n) * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toDoubleArray(a); double[] h_At = new double[m * n];
            for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) h_At[c * m + r] = h_A[r * n + c];
            checkCuda((int) CUDA_MEMCPY_H_TO_D.invokeExact(segA.address(), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) m * n * 8));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) CUSOLVER_DGETRF_BUFFER_SIZE.invokeExact(handle, m, n, segA, m, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCusolver((int) CUSOLVER_DGETRF.invokeExact(handle, m, n, segA, m, segWork, segIpiv, segInfo));
            double[] h_LU = new double[m * n]; MemorySegment hostLU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostLU, segA.address(), (long) m * n * 8));
            MemorySegment.copy(hostLU, ValueLayout.JAVA_DOUBLE, 0, h_LU, 0, m * n);
            double[] luData = new double[m * n]; for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) luData[r * n + c] = h_LU[c * m + r];
            int[] pivots = new int[Math.min(m, n)]; MemorySegment hostIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) pivots.length);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostIpiv, segIpiv.address(), (long) pivots.length * 4));
            MemorySegment.copy(hostIpiv, ValueLayout.JAVA_INT, 0, pivots, 0, pivots.length);
            
            // Split LU into L and U
            double[] lData = new double[m * n];
            double[] uData = new double[m * n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    double val = luData[i * n + j];
                    if (i > j) { lData[i * n + j] = val; uData[i * n + j] = 0.0; }
                    else if (i == j) { lData[i * n + j] = 1.0; uData[i * n + j] = val; }
                    else { lData[i * n + j] = 0.0; uData[i * n + j] = val; }
                }
            }
            
            double[] pData = new double[m];
            for (int i = 0; i < m; i++) pData[i] = (double) i;
            // Native pivots are 1-based indices for swaps. Simple P vector for now.
            for (int i = 0; i < pivots.length; i++) {
                int swapIdx = pivots[i] - 1;
                if (swapIdx != i && swapIdx < m) {
                    double tmp = pData[i]; pData[i] = pData[swapIdx]; pData[swapIdx] = tmp;
                }
            }
            
            return new LUResult<>(toMatrix(lData, m, n), toMatrix(uData, m, n), toVector(pData));
        } catch (Throwable t) { throw new RuntimeException("CUDA LU failed", t); }
    }

    private LUResult<E> luComplex(Matrix<E> a) {
        int m = a.rows(); int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) m * n * 16, tracker);
            MemorySegment segIpiv = malloc((long) Math.min(m, n) * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[m * n * 2];
            for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY_H_TO_D.invokeExact(segA.address(), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) m * n * 16));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) CUSOLVER_ZGETRF_BUFFER_SIZE.invokeExact(handle, m, n, segA, m, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCusolver((int) CUSOLVER_ZGETRF.invokeExact(handle, m, n, segA, m, segWork, segIpiv, segInfo));
            double[] h_LU = new double[m * n * 2]; MemorySegment hostLU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostLU, segA.address(), (long) m * n * 16));
            MemorySegment.copy(hostLU, ValueLayout.JAVA_DOUBLE, 0, h_LU, 0, m * n * 2);
            double[] luData = new double[m * n * 2]; for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) { luData[(r * n + c) * 2] = h_LU[(c * m + r) * 2]; luData[(r * n + c) * 2 + 1] = h_LU[(c * m + r) * 2 + 1]; }
            int[] pivots = new int[Math.min(m, n)]; MemorySegment hostIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) pivots.length);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostIpiv, segIpiv.address(), (long) pivots.length * 4));
            MemorySegment.copy(hostIpiv, ValueLayout.JAVA_INT, 0, pivots, 0, pivots.length);
            
            // Split LU into L and U for complex
            double[] lData = new double[m * n * 2];
            double[] uData = new double[m * n * 2];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    double rVal = luData[(i * n + j) * 2];
                    double iVal = luData[(i * n + j) * 2 + 1];
                    if (i > j) { 
                        lData[(i * n + j) * 2] = rVal; lData[(i * n + j) * 2 + 1] = iVal; 
                        uData[(i * n + j) * 2] = 0.0; uData[(i * n + j) * 2 + 1] = 0.0; 
                    } else if (i == j) { 
                        lData[(i * n + j) * 2] = 1.0; lData[(i * n + j) * 2 + 1] = 0.0; 
                        uData[(i * n + j) * 2] = rVal; uData[(i * n + j) * 2 + 1] = iVal; 
                    } else { 
                        lData[(i * n + j) * 2] = 0.0; lData[(i * n + j) * 2 + 1] = 0.0; 
                        uData[(i * n + j) * 2] = rVal; uData[(i * n + j) * 2 + 1] = iVal; 
                    }
                }
            }
            
            double[] pData = new double[m];
            for (int i = 0; i < m; i++) pData[i] = (double) i;
            for (int i = 0; i < pivots.length; i++) {
                int swapIdx = pivots[i] - 1;
                if (swapIdx != i && swapIdx < m) {
                    double tmp = pData[i]; pData[i] = pData[swapIdx]; pData[swapIdx] = tmp;
                }
            }

            return new org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult<E>(toMatrixComplex(lData, m, n), toMatrixComplex(uData, m, n), toVector(pData));
        } catch (Throwable t) { throw new RuntimeException("CUDA complex LU failed", t); }
    }

    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        if (!isAvailable() || !USE_CUSOLVER) throw new UnsupportedOperationException(getName() + ": CUDA/cuSolver not available for cholesky()");
        if (isComplex(a)) return choleskyComplex(a);
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 8, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toDoubleArray(a); double[] h_At = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];
            checkCuda((int) CUDA_MEMCPY_H_TO_D.invokeExact(segA.address(), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 8));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) CUSOLVER_DPOTRF_BUFFER_SIZE.invokeExact(handle, 0, n, segA, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCusolver((int) CUSOLVER_DPOTRF.invokeExact(handle, 0, n, segA, n, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segInfo));
            double[] h_L = new double[n * n]; MemorySegment hostL = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostL, segA.address(), (long) n * n * 8));
            MemorySegment.copy(hostL, ValueLayout.JAVA_DOUBLE, 0, h_L, 0, n * n);
            double[] lData = new double[n * n]; for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { if (r >= c) lData[r * n + c] = h_L[c * n + r]; else lData[r * n + c] = 0.0; }
            return new CholeskyResult<>(toMatrix(lData, n, n));
        } catch (Throwable t) { throw new RuntimeException("CUDA Cholesky failed", t); }
    }

    private CholeskyResult<E> choleskyComplex(Matrix<E> a) {
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 16, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY_H_TO_D.invokeExact(segA.address(), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) CUSOLVER_ZPOTRF_BUFFER_SIZE.invokeExact(handle, 0, n, segA, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCusolver((int) CUSOLVER_ZPOTRF.invokeExact(handle, 0, n, segA, n, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segInfo));
            double[] h_L = new double[n * n * 2]; MemorySegment hostL = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostL, segA.address(), (long) n * n * 16));
            MemorySegment.copy(hostL, ValueLayout.JAVA_DOUBLE, 0, h_L, 0, n * n * 2);
            double[] lData = new double[n * n * 2]; for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { if (r >= c) { lData[(r * n + c) * 2] = h_L[(c * n + r) * 2]; lData[(r * n + c) * 2 + 1] = h_L[(c * n + r) * 2 + 1]; } }
            return new CholeskyResult<>(toMatrixComplex(lData, n, n));
        } catch (Throwable t) { throw new RuntimeException("CUDA complex Cholesky failed", t); }
    }

    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        if (!isAvailable() || !USE_CUSOLVER) throw new UnsupportedOperationException(getName() + ": CUDA/cuSolver not available for eigen()");
        if (isComplex(a)) return eigenComplex(a);
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 8, tracker);
            MemorySegment segW = malloc((long) n * 8, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toDoubleArray(a); double[] h_At = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];
            checkCuda((int) CUDA_MEMCPY_H_TO_D.invokeExact(segA.address(), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 8));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) CUSOLVER_DSYEVJ_BUFFER_SIZE.invokeExact(handle, 1, 0, n, segA, n, segW, p_Lwork, MemorySegment.NULL));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCusolver((int) CUSOLVER_DSYEVJ.invokeExact(handle, 1, 0, n, segA, n, segW, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segInfo, MemorySegment.NULL));
            double[] h_W = new double[n]; MemorySegment hostW = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostW, segW.address(), (long) n * 8));
            MemorySegment.copy(hostW, ValueLayout.JAVA_DOUBLE, 0, h_W, 0, n);
            double[] h_V = new double[n * n]; MemorySegment hostV = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostV, segA.address(), (long) n * n * 8));
            MemorySegment.copy(hostV, ValueLayout.JAVA_DOUBLE, 0, h_V, 0, n * n);
            double[] vData = new double[n * n]; for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) vData[r * n + c] = h_V[c * n + r];
            return new EigenResult<>(toMatrix(vData, n, n), toMatrix(h_W, n, 1).getColumn(0));
        } catch (Throwable t) { throw new RuntimeException("CUDA Eigen failed", t); }
    }

    private EigenResult<E> eigenComplex(Matrix<E> a) {
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 16, tracker);
            MemorySegment segW = malloc((long) n * 8, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY_H_TO_D.invokeExact(segA.address(), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCusolver((int) CUSOLVER_ZHEEVD_BUFFER_SIZE.invokeExact(handle, 1, 0, n, segA, n, segW, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCusolver((int) CUSOLVER_ZHEEVD.invokeExact(handle, 1, 0, n, segA, n, segW, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segInfo));
            double[] h_W = new double[n]; MemorySegment hostW = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostW, segW.address(), (long) n * 8));
            MemorySegment.copy(hostW, ValueLayout.JAVA_DOUBLE, 0, h_W, 0, n);
            double[] h_V = new double[n * n * 2]; MemorySegment hostV = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(hostV, segA.address(), (long) n * n * 16));
            MemorySegment.copy(hostV, ValueLayout.JAVA_DOUBLE, 0, h_V, 0, n * n * 2);
            double[] vData = new double[n * n * 2]; for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { vData[(r * n + c) * 2] = h_V[(c * n + r) * 2]; vData[(r * n + c) * 2 + 1] = h_V[(c * n + r) * 2 + 1]; }
            return new EigenResult<>(toMatrixComplex(vData, n, n), toMatrix(h_W, n, 1).getColumn(0));
        } catch (Throwable t) { throw new RuntimeException("CUDA complex Eigen failed", t); }
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!isAvailable() || !USE_CUSOLVER) throw new UnsupportedOperationException(getName() + ": CUDA/cuSolver not available for solve()");
        int n = a.rows();
        Matrix<E> bMat = toMatrix(toDoubleVec(b), n, 1);
        Matrix<E> xMat = multiply(inverse(a), bMat);
        return xMat.getColumn(0);
    }

    public void solve(Matrix<E> a, Matrix<E> b, Matrix<E> x) {
        Matrix<E> invA = inverse(a);
        Matrix<E> res = multiply(invA, b);
        for (int i = 0; i < x.rows(); i++) for (int j = 0; j < x.cols(); j++) x.getStorage().set(i, j, res.get(i, j));
    }

    private static <E extends FieldElement<E>> boolean isComplex(Matrix<E> m) {
        Object zero = m.getScalarRing().zero();
        return zero instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private static <E extends FieldElement<E>> boolean isFloat(Matrix<E> m) {
        return m.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.real.RealFloat;
    }

    private double[] toDoubleArray(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        double[] data = new double[rows * cols];
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) data[i * cols + j] = getRealValue(m.get(i, j));
        return data;
    }

    private float[] toFloatArray(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        float[] data = new float[rows * cols];
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) data[i * cols + j] = (float) getRealValue(m.get(i, j));
        return data;
    }

    private double[] toComplexDoubleArray(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        double[] data = new double[rows * cols * 2];
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) {
            org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) m.get(i, j);
            data[(i * cols + j) * 2] = c.real();
            data[(i * cols + j) * 2 + 1] = c.imaginary();
        }
        return data;
    }

    private double[] toDoubleVec(Vector<E> v) {
        int n = v.dimension();
        double[] data = new double[n];
        for (int i = 0; i < n; i++) data[i] = getRealValue(v.get(i));
        return data;
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> toMatrix(double[] data, int rows, int cols) {
        Ring<E> ring = (Ring<E>) (Object) org.episteme.core.mathematics.sets.Reals.getInstance();
        E[] elements = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for(int i=0; i<data.length; i++) elements[i] = castScalar(data[i], ring);
        return (org.episteme.core.mathematics.linearalgebra.Matrix<E>)(Object) new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<E>(elements, rows, cols, ring);
    }
    
    @SuppressWarnings("unchecked")
    private Vector<E> toVector(double[] data) {
        Ring<E> ring = (Ring<E>) (Object) org.episteme.core.mathematics.sets.Reals.getInstance();
        E[] elements = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for(int i=0; i<data.length; i++) elements[i] = castScalar(data[i], ring);
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(elements),
            null, ring
        );
    }

    private Matrix<E> fromFloatArray(float[] data, int rows, int cols) {
        return (Matrix<E>) (Object) new org.episteme.core.mathematics.linearalgebra.matrices.SIMDRealFloatMatrix(rows, cols, data);
    }

    @SuppressWarnings("unchecked")
    private Matrix<E> toMatrixComplex(double[] data, int rows, int cols) {
        org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex> ring = (org.episteme.core.mathematics.structures.rings.Ring<org.episteme.core.mathematics.numbers.complex.Complex>) (Object) Complex.of(1.0, 0.0).getScalarRing();
        org.episteme.core.mathematics.numbers.complex.Complex[][] res = new org.episteme.core.mathematics.numbers.complex.Complex[rows][cols];
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) res[i][j] = org.episteme.core.mathematics.numbers.complex.Complex.of(data[(i * cols + j) * 2], data[(i * cols + j) * 2 + 1]);
        return (Matrix<E>) (Object) Matrix.of(res, ring);
    }

    private E castScalar(double val, Ring<E> ring) {
        Object zero = ring.zero();
        if (zero instanceof RealFloat) return (E) RealFloat.create((float) val);
        if (zero instanceof RealDouble) return (E) RealDouble.of(val);
        return (E) Real.of(val);
    }

    @SuppressWarnings("unchecked")
    private E toScalar(double val, Vector<E> context) {
        Ring<E> ring = context.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) return (E) Real.of(val);
        if (ring instanceof org.episteme.core.mathematics.sets.Complexes) return (E) Complex.of(val);
        // Fallback
        Object zero = ring.zero();
        if (zero instanceof Complex) return (E) Complex.of(val);
        return (E) Real.of(val);
    }

    private static void checkCuda(int result) {
        if (result != 0) {
            if (CUDA_GET_ERROR_STRING == null) throw new RuntimeException("CUDA Error " + result);
            try (Arena temp = Arena.ofConfined()) {
                MemorySegment pStr = temp.allocate(ValueLayout.ADDRESS);
                int status = (int) NativeSafe.invoke(CUDA_GET_ERROR_STRING, result, pStr);
                String msg = (status == 0) ? pStr.get(ValueLayout.ADDRESS, 0).reinterpret(1024).getString(0) : "Unknown CUDA Error";
                logger.error("CUDA Error {}: {}", result, msg);
                throw new RuntimeException("CUDA Error " + result + ": " + msg);
            } catch (Throwable t) { throw new RuntimeException("CUDA Error " + result, t); }
        }
    }

    private static void checkCublas(int result) {
        if (result != 0) {
            if (CUBLAS_STATUS_GET_STRING == null) throw new RuntimeException("CUBLAS Error " + result);
            try {
                MemorySegment seg = (MemorySegment) CUBLAS_STATUS_GET_STRING.invokeExact(result);
                String msg = seg.reinterpret(1024).getString(0);
                logger.error("CUBLAS Error {}: {}", result, msg);
                throw new RuntimeException("CUBLAS Error " + result + ": " + msg);
            } catch (Throwable t) { throw new RuntimeException("CUBLAS Error " + result, t); }
        }
    }

    private static void checkCusolver(int result) {
        if (result != 0) {
            if (CUSOLVER_STATUS_GET_STRING == null) throw new RuntimeException("CUSOLVER Error " + result);
            try {
                MemorySegment seg = (MemorySegment) CUSOLVER_STATUS_GET_STRING.invokeExact(result);
                String msg = seg.reinterpret(1024).getString(0);
                logger.error("CUSOLVER Error {}: {}", result, msg);
                throw new RuntimeException("CUSOLVER Error " + result + ": " + msg);
            } catch (Throwable t) { throw new RuntimeException("CUSOLVER Error " + result, t); }
        }
    }

    @Override
    public void synchronize() {
        if (!isAvailable()) return;
        try { checkCuda((int) CUDA_DEVICE_SYNCHRONIZE.invokeExact()); } catch (Throwable t) { logger.error("CUDA synchronize failed: {}", t.getMessage()); }
    }

    @Override
    public DeviceInfo[] getDevices() {
        if (!isAvailable()) return new DeviceInfo[0];
        return new DeviceInfo[]{new DeviceInfo("CUDA Dense Device", 8L * 1024 * 1024 * 1024, 128, "NVIDIA")};
    }

    @Override
    public void selectDevice(int deviceId) {
        // Simple implementation for now as we use the default device for Dense
        logger.info("Selected CUDA device: {}", deviceId);
    }

    @Override
    public long allocateGPUMemory(long sizeBytes) {
        // We return address directly for GPUBackend compliance, no tracking
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUDA_MALLOC.invokeExact(p, sizeBytes));
            return p.get(ValueLayout.ADDRESS, 0).address();
        } catch (Throwable t) { throw new RuntimeException("CUDA malloc failed", t); }
    }

    @Override
    public void freeGPUMemory(long pointer) {
        try { checkCuda((int) CUDA_FREE.invokeExact(pointer)); } catch (Throwable t) { logger.error("CUDA free failed", t); }
    }

    @Override
    public void copyToGPU(long gpuPointer, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {
        // Implementation for interface compliance — in practice we use FFM NativeRealDoubleMatrixStorage
    }

    @Override
    public void copyFromGPU(long gpuPointer, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {
        // Implementation for interface compliance
    }
    
    @Override
    public void matrixMultiply(java.nio.DoubleBuffer A, java.nio.DoubleBuffer B, java.nio.DoubleBuffer C, int m, int n, int k) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for matrixMultiply()");
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * 8, tracker);
            MemorySegment d_B = malloc((long) k * n * 8, tracker);
            MemorySegment d_C = malloc((long) m * n * 8, tracker);

            double[] h_A = new double[m * k]; A.get(h_A); A.rewind();
            double[] h_B = new double[k * n]; B.get(h_B); B.rewind();

            checkCuda((int) CUDA_MEMCPY_H_TO_D.invokeExact(d_A.address(), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_A), (long) m * k * 8));
            checkCuda((int) CUDA_MEMCPY_H_TO_D.invokeExact(d_B.address(), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_B), (long) k * n * 8));

            MemorySegment handle = createCublasHandle(tracker);
            // cuBLAS is Column-Major. A(m,k) Row-Major -> At(k,m) Column-Major.
            // C = A * B => Ct = Bt * At.
            // B(k,n) Row-Major -> Bt(n,k) Column-Major.
            // Ct(n,m) = Bt(n,k) * At(k,m).
            checkCublas((int) CUBLAS_DGEMM.invokeExact(handle, 0, 0, n, m, k, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), d_B, n, d_A, k, 
                arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), d_C, n));

            double[] h_C = new double[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) CUDA_MEMCPY_D_TO_H.invokeExact(segC, d_C.address(), (long) m * n * 8));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, h_C, 0, m * n);
            C.put(h_C); C.rewind();
        } catch (Throwable t) { throw new RuntimeException("CUDA matrixMultiply failed", t); }
    }

    @Override
    public HardwareAccelerator getAcceleratorType() { return HardwareAccelerator.GPU; }

    @Override
    public void shutdown() {
        logger.info("CUDADenseLinearAlgebraBackend shutting down...");
        try { 
            synchronize(); 
            if (staticCublasHandle != null) {
                CUBLAS_DESTROY.invokeExact(staticCublasHandle);
                staticCublasHandle = null;
            }
            if (staticCusolverHandle != null) {
                CUSOLVER_DESTROY.invokeExact(staticCusolverHandle);
                staticCusolverHandle = null;
            }
            IS_AVAILABLE = false;
            NativeFFMLoader.shutdown(); 
        } catch (Exception e) { 
            logger.warn("Error during CUDA backend shutdown: {}", e.getMessage()); 
        } catch (Throwable t) {
            logger.warn("Critical error during CUDA backend shutdown: {}", t.getMessage());
        }
    }

    @Override
    public java.util.Map<String, String> getMetadata() {
        java.util.Map<String, String> meta = new java.util.HashMap<>();
        meta.put("accelerator", "gpu"); meta.put("api", "cuda"); meta.put("precision", "fp64"); meta.put("vendor", "nvidia"); meta.put("solver", "cusolver"); meta.put("optimized_size", ">256");
        return meta;
    }

    @Override
    public double score(OperationContext context) {
        if (!IS_AVAILABLE) return -1;
        if (MathContext.getCurrent().getRealPrecision() == MathContext.RealPrecision.EXACT) return -1.0;
        
        // Strict domain boundary: reject sparse operations
        if (context.hasHint(OperationContext.Hint.SPARSE)) {
            return -1.0;
        }

        double base = 110.0;
        if (context.hasHint(OperationContext.Hint.MAT_INV) || context.hasHint(OperationContext.Hint.MAT_DET) || context.hasHint(OperationContext.Hint.MAT_SOLVE) || context.hasHint(OperationContext.Hint.MAT_QR) || context.hasHint(OperationContext.Hint.MAT_SVD) || context.hasHint(OperationContext.Hint.MAT_CHOLESKY) || context.hasHint(OperationContext.Hint.MAT_LU) || context.hasHint(OperationContext.Hint.MAT_EIGEN)) base += 10.0;
        if (context.getDataSize() < 256) base -= 200;
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 50;
        return base;
    }

    @Override
    public boolean isExplicitlyDisabled() {
        return Boolean.getBoolean("org.episteme.backend.cuda.disabled") || GPUBackend.super.isExplicitlyDisabled();
    }
    private double getRealValue(Object val) {
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

    private Complex getComplexValue(Object val) {
        if (val == null) return Complex.ZERO;
        if (val instanceof Complex c) return c;
        if (val instanceof Real r) return Complex.of(r.doubleValue());
        if (val instanceof Number n) return Complex.of(n.doubleValue());
        return Complex.of(getRealValue(val));
    }
}
