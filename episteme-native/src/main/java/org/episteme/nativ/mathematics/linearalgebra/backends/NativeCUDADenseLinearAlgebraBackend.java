/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.DoubleBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.technical.algorithm.AutoTuningManager;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;

import org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, GPUBackend.class})
public class NativeCUDADenseLinearAlgebraBackend implements LinearAlgebraProvider<Real>, NativeBackend, GPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(NativeCUDADenseLinearAlgebraBackend.class);
    private static boolean IS_AVAILABLE = false;
    private static final Linker LINKER = Linker.nativeLinker();

    // Native Library Lookups
    private static SymbolLookup cublas_lookup;
    private static SymbolLookup cusolver_lookup;

    // CUDA/cuBLAS Handles
    private static MethodHandle CUBLAS_CREATE;
    private static MethodHandle CUBLAS_DESTROY;
    private static MethodHandle CUBLAS_DGEMM;
    private static MethodHandle CUBLAS_DGEAM;
    private static MethodHandle CUDA_MALLOC;
    private static MethodHandle CUDA_FREE;
    private static MethodHandle CUDA_MEMCPY;
    private static MethodHandle CUDA_DEVICE_SYNCHRONIZE;
    private static MethodHandle CUBLAS_DDOT;
    private static MethodHandle CUBLAS_DNRM2;
    private static MethodHandle CUBLAS_ZGEMM;
    private static MethodHandle CUBLAS_ZGEAM;
    private static MethodHandle CUBLAS_ZDOTU;

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
    private static MethodHandle CUSOLVER_DSYEVJ_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_DSYEVJ;
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
    private static MethodHandle CUDA_GET_DEVICE_COUNT;
    private static MethodHandle CUDA_GET_ERROR_STRING;
    private static MethodHandle CUBLAS_STATUS_GET_STRING;
    private static MethodHandle CU_CTX_GET_CURRENT;
    private static MethodHandle CU_CTX_GET_DEVICE;

    // Constants
    private static final int CUBLAS_OP_N = 0;
    private static final int CUDA_MEMCPY_HOST_TO_DEVICE = 1;
    private static final int CUDA_MEMCPY_DEVICE_TO_HOST = 2;

    private static synchronized void ensureInitialized() {
        if (IS_AVAILABLE) return;
        
        try (Arena tempArena = Arena.ofConfined()) {
             // Try loading cudart
             Optional<SymbolLookup> cudaRtOpt = NativeFFMLoader.loadLibrary("cudart", Arena.global());
            if (cudaRtOpt.isEmpty() && System.getProperty("os.name").toLowerCase().contains("linux")) {
                Path linuxPath = Path.of("/usr/local/cuda/lib64/libcudart.so");
                if (Files.exists(linuxPath)) {
                    cudaRtOpt = NativeFFMLoader.loadLibrary(linuxPath.toString(), Arena.global());
                }
            }
            SymbolLookup cudart = cudaRtOpt.orElse(null);

            // Try loading cublas
            Optional<SymbolLookup> cublasOptLocal = NativeFFMLoader.loadLibrary("cublas", Arena.global());
            if (cublasOptLocal.isEmpty() && System.getProperty("os.name").toLowerCase().contains("linux")) {
                Path linuxPath = Path.of("/usr/local/cuda/lib64/libcublas.so");
                if (Files.exists(linuxPath)) {
                    cublasOptLocal = NativeFFMLoader.loadLibrary(linuxPath.toString(), Arena.global());
                }
            }
            cublas_lookup = cublasOptLocal.orElse(null);

            // Try loading cusolver
            Optional<SymbolLookup> cusolverOpt = NativeFFMLoader.loadLibrary("cusolver", Arena.global());
            if (cusolverOpt.isEmpty() && System.getProperty("os.name").toLowerCase().contains("linux")) {
                Path linuxPath = Path.of("/usr/local/cuda/lib64/libcusolver.so");
                if (Files.exists(linuxPath)) {
                    cusolverOpt = NativeFFMLoader.loadLibrary(linuxPath.toString(), Arena.global());
                }
            }
            cusolver_lookup = cusolverOpt.orElse(null);

            if (cudart == null || cublas_lookup == null || cusolver_lookup == null) {
                logger.warn("Native CUDA/cuBLAS/cuSolver libraries not found (cudart={}, cublas={}, cusolver={})", 
                    cudart != null, cublas_lookup != null, cusolver_lookup != null);
                return;
            }

            // Bind basic symbols
            CUDA_GET_DEVICE_COUNT = lookup(cudart, "cudaGetDeviceCount", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUDA_GET_ERROR_STRING = lookup(cudart, "cudaGetErrorString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            CUBLAS_STATUS_GET_STRING = lookup(cublas_lookup, "cublasGetStatusString", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

            // Try loading CUDA driver
            Optional<SymbolLookup> cudaDrvOpt = NativeFFMLoader.loadLibrary("cuda", Arena.global());
            if (cudaDrvOpt.isEmpty()) cudaDrvOpt = NativeFFMLoader.loadLibrary("nvcuda", Arena.global());
            SymbolLookup cudaDrv = cudaDrvOpt.orElse(null);
            
            if (cudaDrv != null) {
                CU_CTX_GET_CURRENT = lookup(cudaDrv, "cuCtxGetCurrent", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                CU_CTX_GET_DEVICE = lookup(cudaDrv, "cuCtxGetDevice", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            }

            if (CUDA_GET_DEVICE_COUNT != null) {
                MemorySegment countPtr = tempArena.allocate(ValueLayout.JAVA_INT);
                int cudaResult = (int) CUDA_GET_DEVICE_COUNT.invokeExact(countPtr);
                int deviceCount = countPtr.get(ValueLayout.JAVA_INT, 0);
                if (cudaResult != 0 || deviceCount <= 0) {
                    logger.warn("No CUDA-capable GPU devices found (result={}, count={}). Backend disabled.", cudaResult, deviceCount);
                    return;
                }
                logger.info("Found {} CUDA-capable GPU device(s).", deviceCount);

                // KICKSTART: Force CUDA context creation
                CUDA_FREE = lookup(cudart, "cudaFree", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                if (CUDA_FREE != null) {
                    int rFree = (int) CUDA_FREE.invokeExact(MemorySegment.NULL);
                    if (rFree != 0) logger.warn("CUDA kickstart returned code {}", rFree);
                }
            }
            
            // CUDA Runtime Memory Management
            CUDA_MALLOC = lookup(cudart, "cudaMalloc", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            CUDA_MEMCPY = lookup(cudart, "cudaMemcpy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
            CUDA_DEVICE_SYNCHRONIZE = lookup(cudart, "cudaDeviceSynchronize", FunctionDescriptor.of(ValueLayout.JAVA_INT));
            
            if (CUDA_MALLOC == null || CUDA_FREE == null || CUDA_MEMCPY == null) {
                logger.warn("Required CUDA runtime symbols missing. Backend disabled.");
                return;
            }

            // cuBLAS Core
            CUBLAS_CREATE = lookup(cublas_lookup, "cublasCreate_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            if (CUBLAS_CREATE == null) CUBLAS_CREATE = lookup(cublas_lookup, "cublasCreate", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUBLAS_DESTROY = lookup(cublas_lookup, "cublasDestroy_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            if (CUBLAS_DESTROY == null) CUBLAS_DESTROY = lookup(cublas_lookup, "cublasDestroy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
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

            CUBLAS_DDOT = lookup(cublas_lookup, "cublasDdot_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUBLAS_DNRM2 = lookup(cublas_lookup, "cublasDnrm2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

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
            CUBLAS_ZDOTU = lookup(cublas_lookup, "cublasZdotu_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            // cuSolver Core
            CUSOLVER_CREATE = lookup(cusolver_lookup, "cusolverDnCreate", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_DESTROY = lookup(cusolver_lookup, "cusolverDnDestroy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
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

            CUSOLVER_ZGETRF_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnZgetrf_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSOLVER_ZGETRF = lookup(cusolver_lookup, "cusolverDnZgetrf", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
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

            IS_AVAILABLE = true;
            logger.info("Native CUDA/cuBLAS/cuSolver Backend initialized successfully.");
        } catch (Throwable t) {
            logger.warn("Failed to initialize CUDA/cuBLAS Backend: {} - {}", t.getClass().getSimpleName(), t.getMessage());
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
            if (CU_CTX_GET_CURRENT != null && (int)CU_CTX_GET_CURRENT.invokeExact(pCtx) == 0) {
                ctxHandle = pCtx.get(ValueLayout.ADDRESS, 0).address();
            }
            MemorySegment pDev = arena.allocate(ValueLayout.JAVA_INT);
            if (CU_CTX_GET_DEVICE != null && (int)CU_CTX_GET_DEVICE.invokeExact(pDev) == 0) {
                devId = pDev.get(ValueLayout.JAVA_INT, 0);
            }
            return org.episteme.nativ.technical.backend.gpu.cuda.CUDAExecutionContext.fromNative(ctxHandle, devId);
        } catch (Throwable t) {
            return new org.episteme.nativ.technical.backend.gpu.cuda.CUDAExecutionContext(null, null);
        }
    }

    private MemorySegment malloc(long size, ResourceTracker tracker) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUDA_MALLOC.invokeExact(p, size));
            MemorySegment d = p.get(ValueLayout.ADDRESS, 0);
            return tracker.track(d, ptr -> {
                try { checkCuda((int) CUDA_FREE.invokeExact(ptr)); } catch (Throwable t) { logger.error("Failed to free GPU memory: {}", t.getMessage()); }
            });
        } catch (Throwable t) { throw new RuntimeException("CUDA malloc failed", t); }
    }

    private MemorySegment createCublasHandle(ResourceTracker tracker) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
            checkCublas((int) CUBLAS_CREATE.invokeExact(p));
            MemorySegment h = p.get(ValueLayout.ADDRESS, 0);
            return tracker.track(h, handle -> {
                try { checkCublas((int) CUBLAS_DESTROY.invokeExact(handle)); } catch (Throwable t) { logger.error("Failed to destroy cuBLAS handle: {}", t.getMessage()); }
            });
        } catch (Throwable t) { throw new RuntimeException("cuBLAS create failed", t); }
    }

    private MemorySegment createCusolverHandle(ResourceTracker tracker) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUSOLVER_CREATE.invokeExact(p));
            MemorySegment h = p.get(ValueLayout.ADDRESS, 0);
            return tracker.track(h, handle -> {
                try { checkCuda((int) CUSOLVER_DESTROY.invokeExact(handle)); } catch (Throwable t) { logger.error("Failed to destroy cuSolver handle: {}", t.getMessage()); }
            });
        } catch (Throwable t) { throw new RuntimeException("cuSolver create failed", t); }
    }

    @Override
    public String getName() {
        return "Native CUDA Dense Linear Algebra Backend";
    }
    @Override public int getPriority() { return 110; }
    @Override public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) { return ring instanceof org.episteme.core.mathematics.sets.Reals; }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for transpose()");
        int rows = a.rows(); int cols = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) rows * cols * 8, tracker);
            MemorySegment d_C = malloc((long) rows * cols * 8, tracker);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a)), (long) rows * cols * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_DGEAM.invokeExact(handle, 1, 0, cols, rows, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), d_A, rows, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), d_A, cols, d_C, cols));
            double[] result = new double[rows * cols];
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) rows * cols);
            checkCuda((int) CUDA_MEMCPY.invokeExact(host, d_C, (long) rows * cols * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, result, 0, rows * cols);
            return fromDoubleArray(result, cols, rows);
        } catch (Throwable t) { throw new RuntimeException("CUDA transpose failed", t); }
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for multiply()");
        if (isComplex(a) || isComplex(b)) return multiplyComplex(a, b);
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        if (k != b.rows()) throw new IllegalArgumentException("Dimension mismatch");
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * 8, tracker);
            MemorySegment d_B = malloc((long) k * n * 8, tracker);
            MemorySegment d_C = malloc((long) m * n * 8, tracker);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a)), (long) m * k * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b)), (long) k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_DGEMM.invokeExact(handle, 0, 0, n, m, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), d_B, n, d_A, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), d_C, n));
            double[] h_C = new double[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(segC, d_C, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, h_C, 0, m * n);
            return fromDoubleArray(h_C, m, n);
        } catch (Throwable t) { throw new RuntimeException("CUDA multiply failed", t); }
    }

    private Matrix<Real> multiplyComplex(Matrix<Real> a, Matrix<Real> b) {
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * 16, tracker);
            MemorySegment d_B = malloc((long) k * n * 16, tracker);
            MemorySegment d_C = malloc((long) m * n * 16, tracker);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(a)), (long) m * k * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(b)), (long) k * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_ZGEMM.invokeExact(handle, 0, 0, n, m, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0), d_B, n, d_A, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0), d_C, n));
            double[] resultData = new double[m * n * 2];
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            checkCuda((int) CUDA_MEMCPY.invokeExact(host, d_C, (long) m * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, resultData, 0, m * n * 2);
            return fromComplexDoubleArray(resultData, m, n);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex multiply failed", t); }
    }

    @Override public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) { if (isComplex(a)) return geamComplex(a, b, Real.of(1.0), Real.of(1.0)); return geam(a, b, 1.0, 1.0); }
    @Override public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) { if (isComplex(a)) return geamComplex(a, b, Real.of(1.0), Real.of(-1.0)); return geam(a, b, 1.0, -1.0); }
    @Override public Matrix<Real> scale(Real scalar, Matrix<Real> a) { if (isComplex(a)) return geamComplex(a, null, scalar, Real.of(0.0)); return geam(a, null, scalar.doubleValue(), 0.0); }

    private Matrix<Real> geam(Matrix<Real> a, Matrix<Real> b, double alphaVal, double betaVal) {
        int m = a.rows(); int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 8, tracker);
            MemorySegment d_C = malloc((long) m * n * 8, tracker);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a)), (long) m * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment d_B = (b != null) ? malloc((long) m * n * 8, tracker) : d_A;
            if (b != null) checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b)), (long) m * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_DGEAM.invokeExact(handle, 0, 0, n, m, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, alphaVal), d_A, n, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, betaVal), d_B, n, d_C, n));
            double[] h_C = new double[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(segC, d_C, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, h_C, 0, m * n);
            return fromDoubleArray(h_C, m, n);
        } catch (Throwable t) { throw new RuntimeException("CUDA GEAM failed", t); }
    }

    private Matrix<Real> geamComplex(Matrix<Real> a, Matrix<Real> b, Real alphaReal, Real betaReal) {
        int m = a.rows(); int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * n * 16, tracker);
            MemorySegment d_B = malloc((long) m * n * 16, tracker);
            MemorySegment d_C = malloc((long) m * n * 16, tracker);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(a)), (long) m * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            if (b != null) checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(b)), (long) m * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_ZGEAM.invokeExact(handle, 0, 0, n, m, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0), d_A, n, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, b != null ? 1.0 : 0.0, 0.0), d_B, n, d_C, n));
            double[] resultData = new double[m * n * 2];
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            checkCuda((int) CUDA_MEMCPY.invokeExact(host, d_C, (long) m * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, resultData, 0, m * n * 2);
            return fromComplexDoubleArray(resultData, m, n);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex GEAM failed", t); }
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> x) {
        int m = a.rows(); int k = a.cols();
        Matrix<Real> xMat = fromDoubleArray(toDoubleVec(x), k, 1);
        Matrix<Real> resMat = multiply(a, xMat);
        double[] resData = new double[m];
        for(int i=0; i<m; i++) resData[i] = resMat.get(i, 0).doubleValue();
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(resData);
    }

    @Override public Vector<Real> add(Vector<Real> a, Vector<Real> b) { Matrix<Real> mc = add(fromDoubleArray(toDoubleVec(a), 1, a.dimension()), fromDoubleArray(toDoubleVec(b), 1, b.dimension())); return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(toDoubleArray(mc)); }
    @Override public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) { Matrix<Real> mc = subtract(fromDoubleArray(toDoubleVec(a), 1, a.dimension()), fromDoubleArray(toDoubleVec(b), 1, b.dimension())); return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(toDoubleArray(mc)); }
    @Override public Vector<Real> multiply(Vector<Real> v, Real s) { Matrix<Real> mc = scale(s, fromDoubleArray(toDoubleVec(v), 1, v.dimension())); return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(toDoubleArray(mc)); }

    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) {
        if (isComplex(a)) return dotComplex(a, b);
        int n = a.dimension();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * 8, tracker);
            MemorySegment d_B = malloc((long) n * 8, tracker);
            MemorySegment p_Res = arena.allocate(ValueLayout.JAVA_DOUBLE);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(a)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(b)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_DDOT.invokeExact(handle, n, d_A, 1, d_B, 1, p_Res));
            return Real.of(p_Res.get(ValueLayout.JAVA_DOUBLE, 0));
        } catch (Throwable t) { throw new RuntimeException("CUDA dot failed", t); }
    }

    @Override
    public Real norm(Vector<Real> v) {
        int n = v.dimension();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_V = malloc((long) n * 8, tracker);
            MemorySegment p_Res = arena.allocate(ValueLayout.JAVA_DOUBLE);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_V, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(v)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_DNRM2.invokeExact(handle, n, d_V, 1, p_Res));
            return Real.of(p_Res.get(ValueLayout.JAVA_DOUBLE, 0));
        } catch (Throwable t) { throw new RuntimeException("CUDA norm failed", t); }
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for solve()");
        if (isComplex(a)) return solveComplex(a, b);
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 8, tracker);
            MemorySegment segB = malloc((long) n * 8, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toDoubleArray(a); double[] h_At = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(segB, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(b)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_DGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCuda((int) CUSOLVER_DGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
            checkCuda((int) CUSOLVER_DGETRS.invokeExact(handle, 0, n, 1, segA, n, segIpiv, segB, n, segInfo));
            double[] h_X = new double[n];
            MemorySegment hostX = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostX, segB, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostX, ValueLayout.JAVA_DOUBLE, 0, h_X, 0, n);
            return fromDoubleVec(h_X);
        } catch (Throwable t) { throw new RuntimeException("CUDA solve failed", t); }
    }

    private Vector<Real> solveComplex(Matrix<Real> a, Vector<Real> b) {
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 16, tracker);
            MemorySegment segB = malloc((long) n * 16, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(segB, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleVec(b)), (long) n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_ZGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCuda((int) CUSOLVER_ZGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
            checkCuda((int) CUSOLVER_ZGETRS.invokeExact(handle, 0, n, 1, segA, n, segIpiv, segB, n, segInfo));
            double[] resData = new double[n * 2];
            MemorySegment hostB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * 2);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostB, segB, (long) n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostB, ValueLayout.JAVA_DOUBLE, 0, resData, 0, n * 2);
            return fromComplexDoubleVec(resData);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex solve failed", t); }
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for inverse()");
        if (isComplex(a)) return inverseComplex(a);
        int n = a.rows(); double[] identity = new double[n * n]; for (int i = 0; i < n; i++) identity[i * n + i] = 1.0;
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 8, tracker);
            MemorySegment segB = malloc((long) n * n * 8, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toDoubleArray(a); double[] h_At = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(segB, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, identity), (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_DGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCuda((int) CUSOLVER_DGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
            checkCuda((int) CUSOLVER_DGETRS.invokeExact(handle, 0, n, n, segA, n, segIpiv, segB, n, segInfo));
            double[] h_InvT = new double[n * n];
            MemorySegment hostInv = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostInv, segB, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostInv, ValueLayout.JAVA_DOUBLE, 0, h_InvT, 0, n * n);
            double[] h_Inv = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_Inv[r * n + c] = h_InvT[c * n + r];
            return fromDoubleArray(h_Inv, n, n);
        } catch (Throwable t) { throw new RuntimeException("CUDA inverse failed", t); }
    }

    private Matrix<Real> inverseComplex(Matrix<Real> a) {
        int n = a.rows(); double[] identity = new double[n * n * 2]; for (int i = 0; i < n; i++) identity[(i * n + i) * 2] = 1.0;
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 16, tracker);
            MemorySegment segB = malloc((long) n * n * 16, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(segB, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, identity), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_ZGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCuda((int) CUSOLVER_ZGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
            checkCuda((int) CUSOLVER_ZGETRS.invokeExact(handle, 0, n, n, segA, n, segIpiv, segB, n, segInfo));
            double[] resData = new double[n * n * 2];
            MemorySegment hostB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostB, segB, (long) n * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostB, ValueLayout.JAVA_DOUBLE, 0, resData, 0, n * n * 2);
            double[] resDataT = new double[n * n * 2];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { resDataT[(r * n + c) * 2] = resData[(c * n + r) * 2]; resDataT[(r * n + c) * 2 + 1] = resData[(c * n + r) * 2 + 1]; }
            return fromComplexDoubleArray(resDataT, n, n);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex inverse failed", t); }
    }

    @Override
    public Real determinant(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for determinant()");
        if (isComplex(a)) return determinantComplex(a);
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 8, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toDoubleArray(a); double[] h_At = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_DGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCuda((int) CUSOLVER_DGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
            double[] h_LU = new double[n * n];
            MemorySegment hostLU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostLU, segA, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostLU, ValueLayout.JAVA_DOUBLE, 0, h_LU, 0, n * n);
            int[] h_Ipiv = new int[n];
            MemorySegment hostIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostIpiv, segIpiv, (long) n * 4, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostIpiv, ValueLayout.JAVA_INT, 0, h_Ipiv, 0, n);
            double det = 1.0; for (int i = 0; i < n; i++) { det *= h_LU[i * n + i]; if (h_Ipiv[i] != i + 1) det = -det; }
            return Real.of(det);
        } catch (Throwable t) { throw new RuntimeException("CUDA determinant failed", t); }
    }

    private Real determinantComplex(Matrix<Real> a) {
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 16, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_ZGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCuda((int) CUSOLVER_ZGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
            double[] h_QR = new double[n * n * 2];
            MemorySegment hostQR = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostQR, segA, (long) n * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostQR, ValueLayout.JAVA_DOUBLE, 0, h_QR, 0, n * n * 2);
            int[] h_Ipiv = new int[n];
            MemorySegment hostIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostIpiv, segIpiv, (long) n * 4, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostIpiv, ValueLayout.JAVA_INT, 0, h_Ipiv, 0, n);
            org.episteme.core.mathematics.numbers.complex.Complex det = org.episteme.core.mathematics.numbers.complex.Complex.ONE;
            for (int i = 0; i < n; i++) det = det.multiply(org.episteme.core.mathematics.numbers.complex.Complex.of(h_QR[(i * n + i) * 2], h_QR[(i * n + i) * 2 + 1]));
            int swaps = 0; for (int i = 0; i < n; i++) if (h_Ipiv[i] != (i + 1)) swaps++;
            if (swaps % 2 != 0) det = det.negate();
            return (Real) (Object) det;
        } catch (Throwable t) { throw new RuntimeException("CUDA complex determinant failed", t); }
    }

    private SVDResult<Real> processSVDResult(int m, int n, double[] h_S, double[] h_U, double[] h_VT) {
        int k = Math.min(m, n);
        double[] h_Ur = new double[m * m]; for (int r = 0; r < m; r++) for (int c = 0; c < m; c++) h_Ur[r * m + c] = h_U[c * m + r];
        double[] h_VTr = new double[n * n]; for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_VTr[r * n + c] = h_VT[c * n + r];
        return new SVDResult<>(fromDoubleArray(h_Ur, m, m), fromDoubleArray(h_S, k, 1).getColumn(0), fromDoubleArray(h_VTr, n, n));
    }

    private SVDResult<Real> processSVDResultComplex(int m, int n, double[] h_S, double[] h_U, double[] h_VT) {
        int k = Math.min(m, n);
        double[] h_Ur = new double[m * m * 2]; for (int r = 0; r < m; r++) for (int c = 0; c < m; c++) { h_Ur[(r * m + c) * 2] = h_U[(c * m + r) * 2]; h_Ur[(r * m + c) * 2 + 1] = h_U[(c * m + r) * 2 + 1]; }
        double[] h_VTr = new double[n * n * 2]; for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_VTr[(r * n + c) * 2] = h_VT[(c * n + r) * 2]; h_VTr[(r * n + c) * 2 + 1] = h_VT[(c * n + r) * 2 + 1]; }
        return new SVDResult<>(fromComplexDoubleArray(h_Ur, m, m), fromDoubleArray(h_S, k, 1).getColumn(0), fromComplexDoubleArray(h_VTr, n, n));
    }

    @Override
    public QRResult<Real> qr(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for qr()");
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
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) m * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_DGEQRF_BUFFER_SIZE.invokeExact(handle, m, n, segA, m, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCuda((int) CUSOLVER_DGEQRF.invokeExact(handle, m, n, segA, m, segTau, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segInfo));
            double[] h_QR = new double[m * n];
            MemorySegment hostQR = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostQR, segA, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostQR, ValueLayout.JAVA_DOUBLE, 0, h_QR, 0, m * n);
            double[] rData = new double[m * n]; for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) { if (c >= r) rData[r * n + c] = h_QR[c * m + r]; else rData[r * n + c] = 0.0; }
            Matrix<Real> R = fromDoubleArray(rData, m, n);
            checkCuda((int) CUSOLVER_DORGQR_BUFFER_SIZE.invokeExact(handle, m, k, k, segA, m, segTau, p_Lwork));
            int lworkQ = p_Lwork.get(ValueLayout.JAVA_INT, 0); segWork = malloc((long) lworkQ * 8, tracker);
            checkCuda((int) CUSOLVER_DORGQR.invokeExact(handle, m, k, k, segA, m, segTau, segWork, lworkQ, segInfo));
            double[] h_Q = new double[m * k];
            MemorySegment hostQ = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * k);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostQ, segA, (long) m * k * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostQ, ValueLayout.JAVA_DOUBLE, 0, h_Q, 0, m * k);
            double[] qData = new double[m * k]; for (int r = 0; r < m; r++) for (int c = 0; c < k; c++) qData[r * k + c] = h_Q[c * m + r];
            return new QRResult<>(fromDoubleArray(qData, m, k), R);
        } catch (Throwable t) { throw new RuntimeException("CUDA QR failed", t); }
    }

    @Override
    public SVDResult<Real> svd(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for svd()");
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
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) m * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_DGESVD_BUFFER_SIZE.invokeExact(handle, m, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCuda((int) CUSOLVER_DGESVD.invokeExact(handle, (byte)'A', (byte)'A', m, n, segA, m, segS, segU, m, segVT, n, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), MemorySegment.NULL, segInfo));
            double[] h_S = new double[Math.min(m, n)]; double[] h_U = new double[m * m]; double[] h_VT = new double[n * n];
            MemorySegment hostS = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) h_S.length); MemorySegment hostU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) h_U.length); MemorySegment hostVT = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) h_VT.length);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostS, segS, (long) h_S.length * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostU, segU, (long) h_U.length * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostVT, segVT, (long) h_VT.length * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostS, ValueLayout.JAVA_DOUBLE, 0, h_S, 0, h_S.length); MemorySegment.copy(hostU, ValueLayout.JAVA_DOUBLE, 0, h_U, 0, h_U.length); MemorySegment.copy(hostVT, ValueLayout.JAVA_DOUBLE, 0, h_VT, 0, h_VT.length);
            return processSVDResult(m, n, h_S, h_U, h_VT);
        } catch (Throwable t) { throw new RuntimeException("CUDA SVD failed", t); }
    }

    @Override
    public CholeskyResult<Real> cholesky(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for cholesky()");
        if (isComplex(a)) return choleskyComplex(a);
        int n = a.rows(); if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 8, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toDoubleArray(a); double[] h_At = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_DPOTRF_BUFFER_SIZE.invokeExact(handle, 0, n, segA, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCuda((int) CUSOLVER_DPOTRF.invokeExact(handle, 0, n, segA, n, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segInfo));
            double[] h_L = new double[n * n];
            MemorySegment hostL = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostL, segA, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostL, ValueLayout.JAVA_DOUBLE, 0, h_L, 0, n * n);
            double[] lData = new double[n * n]; for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { if (r >= c) lData[r * n + c] = h_L[c * n + r]; else lData[r * n + c] = 0.0; }
            return new CholeskyResult<>(fromDoubleArray(lData, n, n));
        } catch (Throwable t) { throw new RuntimeException("CUDA Cholesky failed", t); }
    }

    @Override
    public LUResult<Real> lu(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for lu()");
        if (isComplex(a)) return luComplex(a);
        int n = a.rows(); if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 8, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toDoubleArray(a); double[] h_At = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_DGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCuda((int) CUSOLVER_DGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
            double[] h_LU = new double[n * n];
            MemorySegment hostLU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostLU, segA, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostLU, ValueLayout.JAVA_DOUBLE, 0, h_LU, 0, n * n);
            int[] h_Ipiv = new int[n];
            MemorySegment hostIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostIpiv, segIpiv, (long) n * 4, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostIpiv, ValueLayout.JAVA_INT, 0, h_Ipiv, 0, n);
            double[] lData = new double[n * n]; double[] uData = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { double val = h_LU[c * n + r]; if (r > c) lData[r * n + c] = val; else if (r == c) { lData[r * n + c] = 1.0; uData[r * n + c] = val; } else uData[r * n + c] = val; }
            int[] perm = new int[n]; for (int i = 0; i < n; i++) perm[i] = i;
            for (int i = 0; i < n; i++) { int j = h_Ipiv[i] - 1; if (i != j) { int tmp = perm[i]; perm[i] = perm[j]; perm[j] = tmp; } }
            double[] pData = new double[n]; for (int i = 0; i < n; i++) pData[i] = perm[i];
            return new LUResult<>(fromDoubleArray(lData, n, n), fromDoubleArray(uData, n, n), fromDoubleVec(pData));
        } catch (Throwable t) { throw new RuntimeException("CUDA LU failed", t); }
    }

    @Override
    public EigenResult<Real> eigen(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for eigen()");
        if (isComplex(a)) return eigenComplex(a);
        int n = a.rows(); if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 8, tracker);
            MemorySegment segW = malloc((long) n * 8, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toDoubleArray(a); double[] h_At = new double[n * n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) h_At[c * n + r] = h_A[r * n + c];
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_DSYEVJ_BUFFER_SIZE.invokeExact(handle, 1, 0, n, segA, n, segW, p_Lwork, MemorySegment.NULL));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 8, tracker);
            checkCuda((int) CUSOLVER_DSYEVJ.invokeExact(handle, 1, 0, n, segA, n, segW, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), MemorySegment.NULL, segInfo));
            double[] h_W = new double[n]; double[] h_V = new double[n * n];
            MemorySegment hostW = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n); MemorySegment hostV = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostW, segW, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostV, segA, (long) n * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostW, ValueLayout.JAVA_DOUBLE, 0, h_W, 0, n); MemorySegment.copy(hostV, ValueLayout.JAVA_DOUBLE, 0, h_V, 0, n * n);
            double[] vData = new double[n * n]; for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) vData[r * n + c] = h_V[c * n + r];
            return new EigenResult<>(fromDoubleArray(vData, n, n), fromDoubleVec(h_W));
        } catch (Throwable t) { throw new RuntimeException("CUDA Eigen failed", t); }
    }

    private CholeskyResult<Real> choleskyComplex(Matrix<Real> a) {
        int n = a.rows(); if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 16, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_ZPOTRF_BUFFER_SIZE.invokeExact(handle, 0, n, segA, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCuda((int) CUSOLVER_ZPOTRF.invokeExact(handle, 0, n, segA, n, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segInfo));
            double[] h_L = new double[n * n * 2];
            MemorySegment hostL = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostL, segA, (long) n * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostL, ValueLayout.JAVA_DOUBLE, 0, h_L, 0, n * n * 2);
            Real[][] lMatrixData = new Real[n][n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) {
                if (r >= c) lMatrixData[r][c] = (Real)(Object)org.episteme.core.mathematics.numbers.complex.Complex.of(h_L[(c * n + r) * 2], h_L[(c * n + r) * 2 + 1]);
                else lMatrixData[r][c] = (Real)(Object)org.episteme.core.mathematics.numbers.complex.Complex.ZERO;
            }
            return new CholeskyResult<>(Matrix.of(lMatrixData, Reals.getInstance()));
        } catch (Throwable t) { throw new RuntimeException("CUDA complex Cholesky failed", t); }
    }

    private LUResult<Real> luComplex(Matrix<Real> a) {
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 16, tracker);
            MemorySegment segIpiv = malloc((long) n * 4, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_ZGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCuda((int) CUSOLVER_ZGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
            double[] h_LU = new double[n * n * 2];
            MemorySegment hostLU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostLU, segA, (long) n * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostLU, ValueLayout.JAVA_DOUBLE, 0, h_LU, 0, n * n * 2);
            int[] h_Ipiv = new int[n];
            MemorySegment hostIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostIpiv, segIpiv, (long) n * 4, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostIpiv, ValueLayout.JAVA_INT, 0, h_Ipiv, 0, n);
            Real[][] L = new Real[n][n]; Real[][] U = new Real[n][n];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) {
                double re = h_LU[(c * n + r) * 2]; double im = h_LU[(c * n + r) * 2 + 1];
                org.episteme.core.mathematics.numbers.complex.Complex val = org.episteme.core.mathematics.numbers.complex.Complex.of(re, im);
                if (r > c) { L[r][c] = (Real)(Object)val; U[r][c] = (Real)(Object)org.episteme.core.mathematics.numbers.complex.Complex.ZERO; }
                else if (r < c) { L[r][c] = (Real)(Object)org.episteme.core.mathematics.numbers.complex.Complex.ZERO; U[r][c] = (Real)(Object)val; }
                else { L[r][c] = (Real)(Object)org.episteme.core.mathematics.numbers.complex.Complex.ONE; U[r][c] = (Real)(Object)val; }
            }
            Real[] P = new Real[n]; for (int i = 0; i < n; i++) P[i] = Real.of(h_Ipiv[i]);
            return new LUResult<>(Matrix.of(L, Reals.getInstance()), Matrix.of(U, Reals.getInstance()), Vector.of(P, Reals.getInstance()));
        } catch (Throwable t) { throw new RuntimeException("CUDA complex LU failed", t); }
    }

    private QRResult<Real> qrComplex(Matrix<Real> a) {
        int m = a.rows(); int n = a.cols(); int k = Math.min(m, n);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) m * n * 16, tracker);
            MemorySegment segTau = malloc((long) k * 16, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[m * n * 2];
            for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) { h_At[(c * m + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * m + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) m * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_ZGEQRF_BUFFER_SIZE.invokeExact(handle, m, n, segA, m, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCuda((int) CUSOLVER_ZGEQRF.invokeExact(handle, m, n, segA, m, segTau, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segInfo));
            double[] h_QR = new double[m * n * 2];
            MemorySegment hostQR = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostQR, segA, (long) m * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostQR, ValueLayout.JAVA_DOUBLE, 0, h_QR, 0, m * n * 2);
            Real[][] rMatrixData = new Real[m][n];
            for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) {
                if (c >= r) rMatrixData[r][c] = (Real)(Object)org.episteme.core.mathematics.numbers.complex.Complex.of(h_QR[(c * m + r) * 2], h_QR[(c * m + r) * 2 + 1]);
                else rMatrixData[r][c] = (Real)(Object)org.episteme.core.mathematics.numbers.complex.Complex.ZERO;
            }
            Matrix<Real> R = Matrix.of(rMatrixData, Reals.getInstance());
            checkCuda((int) CUSOLVER_ZORGQR_BUFFER_SIZE.invokeExact(handle, m, k, k, segA, m, segTau, p_Lwork));
            int lworkQ = p_Lwork.get(ValueLayout.JAVA_INT, 0); segWork = malloc((long) lworkQ * 16, tracker);
            checkCuda((int) CUSOLVER_ZORGQR.invokeExact(handle, m, k, k, segA, m, segTau, segWork, lworkQ, segInfo));
            double[] h_Q = new double[m * k * 2];
            MemorySegment hostQ = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * k * 2);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostQ, segA, (long) m * k * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostQ, ValueLayout.JAVA_DOUBLE, 0, h_Q, 0, m * k * 2);
            double[] h_QT = new double[m * k * 2];
            for (int r = 0; r < m; r++) for (int c = 0; c < k; c++) { h_QT[(r * k + c) * 2] = h_Q[(c * m + r) * 2]; h_QT[(r * k + c) * 2 + 1] = h_Q[(c * m + r) * 2 + 1]; }
            return new QRResult<>(fromComplexDoubleArray(h_QT, m, k), R);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex QR failed", t); }
    }

    private SVDResult<Real> svdComplex(Matrix<Real> a) {
        int m = a.rows(); int n = a.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) m * n * 16, tracker);
            MemorySegment segS = malloc((long) Math.min(m, n) * 8, tracker);
            MemorySegment segU = malloc((long) m * m * 16, tracker);
            MemorySegment segVT = malloc((long) n * n * 16, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[m * n * 2];
            for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) { h_At[(c * m + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * m + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) m * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_ZGESVD_BUFFER_SIZE.invokeExact(handle, m, n, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            MemorySegment segRwork = malloc((long) Math.min(m, n) * 8 * 5, tracker);
            checkCuda((int) CUSOLVER_ZGESVD.invokeExact(handle, (byte)'A', (byte)'A', m, n, segA, m, segS, segU, m, segVT, n, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segRwork, segInfo));
            double[] h_S = new double[Math.min(m, n)]; double[] h_U = new double[m * m * 2]; double[] h_VT = new double[n * n * 2];
            MemorySegment hostS = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) h_S.length); MemorySegment hostU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) h_U.length); MemorySegment hostVT = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) h_VT.length);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostS, segS, (long) h_S.length * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostU, segU, (long) h_U.length * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostVT, segVT, (long) h_VT.length * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostS, ValueLayout.JAVA_DOUBLE, 0, h_S, 0, h_S.length); MemorySegment.copy(hostU, ValueLayout.JAVA_DOUBLE, 0, h_U, 0, h_U.length); MemorySegment.copy(hostVT, ValueLayout.JAVA_DOUBLE, 0, h_VT, 0, h_VT.length);
            return processSVDResultComplex(m, n, h_S, h_U, h_VT);
        } catch (Throwable t) { throw new RuntimeException("CUDA complex SVD failed", t); }
    }

    private EigenResult<Real> eigenComplex(Matrix<Real> a) {
        int n = a.rows();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment handle = createCusolverHandle(tracker);
            MemorySegment segA = malloc((long) n * n * 16, tracker);
            MemorySegment segW = malloc((long) n * 8, tracker);
            MemorySegment segInfo = malloc((long) 4, tracker);
            double[] h_A = toComplexDoubleArray(a); double[] h_At = new double[n * n * 2];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2]; h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1]; }
            checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
            checkCuda((int) CUSOLVER_ZHEEVD_BUFFER_SIZE.invokeExact(handle, 1, 0, n, segA, n, segW, p_Lwork));
            MemorySegment segWork = malloc((long) p_Lwork.get(ValueLayout.JAVA_INT, 0) * 16, tracker);
            checkCuda((int) CUSOLVER_ZHEEVD.invokeExact(handle, 1, 0, n, segA, n, segW, segWork, p_Lwork.get(ValueLayout.JAVA_INT, 0), segInfo));
            double[] h_W = new double[n]; double[] h_V = new double[n * n * 2];
            MemorySegment hostW = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * 8); MemorySegment hostV = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostW, segW, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            checkCuda((int) CUDA_MEMCPY.invokeExact(hostV, segA, (long) n * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(hostW, ValueLayout.JAVA_DOUBLE, 0, h_W, 0, n); MemorySegment.copy(hostV, ValueLayout.JAVA_DOUBLE, 0, h_V, 0, n * n * 2);
            double[] h_VT = new double[n * n * 2];
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { h_VT[(r * n + c) * 2] = h_V[(c * n + r) * 2]; h_VT[(r * n + c) * 2 + 1] = h_V[(c * n + r) * 2 + 1]; }
            return new EigenResult<>(fromComplexDoubleArray(h_VT, n, n), fromDoubleVec(h_W));
        } catch (Throwable t) { throw new RuntimeException("CUDA complex Eigen failed", t); }
    }

    private boolean isComplex(Matrix<Real> m) { return m.rows() > 0 && m.cols() > 0 && ((Object)m.get(0, 0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex; }
    private boolean isComplex(Vector<Real> v) { return v.dimension() > 0 && ((Object)v.get(0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex; }

    private Real dotComplex(Vector<Real> a, Vector<Real> b) {
        int n = a.dimension();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) n * 16, tracker); MemorySegment d_B = malloc((long) n * 16, tracker); MemorySegment p_Res = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleVec(a)), (long) n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleVec(b)), (long) n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_ZDOTU.invokeExact(handle, n, d_A, 1, d_B, 1, p_Res));
            return (Real) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(p_Res.get(ValueLayout.JAVA_DOUBLE, 0), p_Res.get(ValueLayout.JAVA_DOUBLE, 8));
        } catch (Throwable t) { throw new RuntimeException("CUDA complex dot failed", t); }
    }

    private double[] toDoubleArray(Matrix<Real> m) {
        int r = m.rows(); int c = m.cols(); double[] d = new double[r * c];
        for(int i=0; i<r; i++) for(int j=0; j<c; j++) d[i*c + j] = m.get(i, j).doubleValue();
        return d;
    }

    private double[] toComplexDoubleArray(Matrix<Real> m) {
        int r = m.rows(); int c = m.cols(); double[] d = new double[r * c * 2];
        for (int i = 0; i < r; i++) for (int j = 0; j < c; j++) {
            Real val = m.get(i, j);
            if (((Object)val) instanceof org.episteme.core.mathematics.numbers.complex.Complex cv) { d[(i * c + j) * 2] = cv.getReal().doubleValue(); d[(i * c + j) * 2 + 1] = cv.getImaginary().doubleValue(); }
            else { d[(i * c + j) * 2] = val.doubleValue(); d[(i * c + j) * 2 + 1] = 0.0; }
        }
        return d;
    }

    private Matrix<Real> fromDoubleArray(double[] data, int rows, int cols) {
        Real[] reals = new Real[data.length]; for(int i=0; i<data.length; i++) reals[i] = Real.of(data[i]);
        return new DenseMatrix<>(reals, rows, cols, Reals.getInstance());
    }

    private Matrix<Real> fromComplexDoubleArray(double[] data, int rows, int cols) {
        Real[] reals = new Real[rows * cols]; for (int i = 0; i < rows * cols; i++) reals[i] = (Real) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
        return new DenseMatrix<>(reals, rows, cols, Reals.getInstance());
    }

    private Vector<Real> fromDoubleVec(double[] data) { return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(data); }

    private Vector<Real> fromComplexDoubleVec(double[] data) {
        int dim = data.length / 2; Real[] reals = new Real[dim];
        for (int i = 0; i < dim; i++) reals[i] = (Real) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
        return Vector.of(reals, Reals.getInstance());
    }

    private double[] toDoubleVec(Vector<Real> v) { double[] d = new double[v.dimension()]; for (int i = 0; i < d.length; i++) d[i] = v.get(i).doubleValue(); return d; }

    private double[] toComplexDoubleVec(Vector<Real> v) {
        int dim = v.dimension(); double[] d = new double[dim * 2];
        for (int i = 0; i < dim; i++) {
            Real val = v.get(i);
            if (((Object)val) instanceof org.episteme.core.mathematics.numbers.complex.Complex cv) { d[i * 2] = cv.getReal().doubleValue(); d[i * 2 + 1] = cv.getImaginary().doubleValue(); }
            else { d[i * 2] = val.doubleValue(); d[i * 2 + 1] = 0.0; }
        }
        return d;
    }


    @Override
    public double score(OperationContext context) {
        if (!IS_AVAILABLE) return -1;
        if (MathContext.getCurrent().getRealPrecision() == MathContext.RealPrecision.EXACT) return -1.0;
        double base = AutoTuningManager.getDynamicScore(getName(), context.getDimensionality(), getPriority());
        if (context.hasHint(OperationContext.Hint.MAT_INV) || context.hasHint(OperationContext.Hint.MAT_DET) || context.hasHint(OperationContext.Hint.MAT_SOLVE) || context.hasHint(OperationContext.Hint.MAT_QR) || context.hasHint(OperationContext.Hint.MAT_SVD) || context.hasHint(OperationContext.Hint.MAT_CHOLESKY) || context.hasHint(OperationContext.Hint.MAT_LU) || context.hasHint(OperationContext.Hint.MAT_EIGEN)) base += 10.0;
        if (context.getDataSize() < 256) base -= 200;
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 50;
        return base;
    }

    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.GPU; }

    @Override
    public void shutdown() {
        logger.info("CUDADenseLinearAlgebraBackend shutting down...");
        try { synchronize(); NativeFFMLoader.shutdown(); } catch (Exception e) { logger.warn("Error during CUDA backend shutdown: {}", e.getMessage()); }
    }

    @Override
    public java.util.Map<String, String> getMetadata() {
        java.util.Map<String, String> meta = new java.util.HashMap<>();
        meta.put("accelerator", "gpu"); meta.put("api", "cuda"); meta.put("precision", "fp64"); meta.put("vendor", "nvidia"); meta.put("solver", "cusolver"); meta.put("optimized_size", ">256");
        return meta;
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
            if (CUBLAS_STATUS_GET_STRING == null) throw new RuntimeException("CUBLAS Error " + result);
            try {
                MemorySegment seg = (MemorySegment) CUBLAS_STATUS_GET_STRING.invokeExact(result);
                String msg = seg.reinterpret(1024).getString(0);
                logger.error("CUBLAS Error {}: {}", result, msg);
                throw new RuntimeException("CUBLAS Error " + result + ": " + msg);
            } catch (Throwable t) { throw new RuntimeException("CUBLAS Error " + result, t); }
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
        try (ResourceTracker tracker = new ResourceTracker()) { // Dummy tracker, we return handle
             // This is a bit awkward as GPUBackend returns long, but we usually track differently
             // For compliance with GPUBackend, we'll return the raw address
             try (Arena temp = Arena.ofConfined()) {
                MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(p, sizeBytes));
                return p.get(ValueLayout.ADDRESS, 0).address();
             } catch (Throwable t) { throw new RuntimeException(t); }
        }
    }

    @Override
    public void freeGPUMemory(long gpuHandle) {
        try { checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(gpuHandle))); } catch (Throwable t) { logger.error("CUDA free failed: {}", t.getMessage()); }
    }

    @Override
    public void copyToGPU(long gpuHandle, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {
        try {
            MemorySegment h_seg = MemorySegment.ofBuffer(hostBuffer);
            checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(gpuHandle), h_seg, sizeBytes, CUDA_MEMCPY_HOST_TO_DEVICE));
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public void copyFromGPU(long gpuHandle, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {
        try {
            MemorySegment h_seg = MemorySegment.ofBuffer(hostBuffer);
            checkCuda((int) CUDA_MEMCPY.invokeExact(h_seg, MemorySegment.ofAddress(gpuHandle), sizeBytes, CUDA_MEMCPY_DEVICE_TO_HOST));
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public void matrixMultiply(java.nio.DoubleBuffer A, java.nio.DoubleBuffer B, java.nio.DoubleBuffer C, int m, int n, int k) {
        int rowsA = m; int colsA = k; int rowsB = k; int colsB = n;
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) rowsA * colsA * 8, tracker);
            MemorySegment d_B = malloc((long) rowsB * colsB * 8, tracker);
            MemorySegment d_C = malloc((long) m * n * 8, tracker);
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, MemorySegment.ofBuffer(A), (long) rowsA * colsA * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, MemorySegment.ofBuffer(B), (long) rowsB * colsB * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            MemorySegment handle = createCublasHandle(tracker);
            checkCublas((int) CUBLAS_DGEMM.invokeExact(handle, 0, 0, n, m, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0), d_B, n, d_A, k, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0), d_C, n));
            checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofBuffer(C), d_C, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
        } catch (Throwable t) { throw new RuntimeException(t); }
    }
}

