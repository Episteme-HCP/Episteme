/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult;
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
    private static MethodHandle CUSOLVER_DSYEVD_BUFFER_SIZE;
    private static MethodHandle CUSOLVER_DSYEVD;
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
        
        // Disabling now handled by Backend.isAvailable() via isExplicitlyDisabled()

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
            if (CUBLAS_DGEMM == null) CUBLAS_DGEMM = lookup(cublas_lookup, "cublasDgemm", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT
            ));

            CUBLAS_DGEAM = lookup(cublas_lookup, "cublasDgeam_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT
            ));
            if (CUBLAS_DGEAM == null) CUBLAS_DGEAM = lookup(cublas_lookup, "cublasDgeam", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS,
                ValueLayout.JAVA_INT
            ));

            CUBLAS_DDOT = lookup(cublas_lookup, "cublasDdot_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            if (CUBLAS_DDOT == null) CUBLAS_DDOT = lookup(cublas_lookup, "cublasDdot", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            if (CUBLAS_DNRM2 == null) CUBLAS_DNRM2 = lookup(cublas_lookup, "cublasDnrm2", FunctionDescriptor.of(ValueLayout.JAVA_INT, 
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
            
            CUSOLVER_DSYEVD_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnDsyevd_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            CUSOLVER_DSYEVD = lookup(cusolver_lookup, "cusolverDnDsyevd", FunctionDescriptor.of(ValueLayout.JAVA_INT,
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

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
            CUSOLVER_ZORGQR_BUFFER_SIZE = lookup(cusolver_lookup, "cusolverDnZungqr_bufferSize", FunctionDescriptor.of(ValueLayout.JAVA_INT, // ZUNGQR for complex
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

            IS_AVAILABLE = true;
            logger.info("Native CUDA/cuBLAS/cuSolver Backend initialized successfully.");
        } catch (Throwable t) {
            logger.warn("Failed to initialize CUDA/cuBLAS Backend: {} - {}", t.getClass().getSimpleName(), t.getMessage());
            logger.debug("Stack trace:", t);
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

    @Override
    public String getId() {
        return "cuda-dense";
    }

    @Override
    public String getType() {
        return "math";
    }

    @Override
    public boolean isLoaded() { return IS_AVAILABLE; }


    @Override
    public String getEnvironmentInfo() {
        return IS_AVAILABLE ? "GPU (CUDA)" : "N/A";
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
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
    public String getName() { return "Native CUDA Dense Backend"; }

    @Override
    public int getPriority() { return 110; } // Higher than Native SIMD (90) and Native BLAS (100)

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) { 
        return ring instanceof org.episteme.core.mathematics.sets.Reals; 
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for transpose()");
        int m = a.rows();
        int n = a.cols();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(a));
            MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment d_C = arena.allocate(ValueLayout.ADDRESS);
            
            checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long)m * n * Double.BYTES));
            checkCuda((int) CUDA_MALLOC.invokeExact(d_C, (long)m * n * Double.BYTES));
            
            try {
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_A.get(ValueLayout.ADDRESS, 0), h_A, (long)m * n * Double.BYTES, CUDA_MEMCPY_HOST_TO_DEVICE));
                
                MemorySegment handle = arena.allocate(ValueLayout.ADDRESS);
                int res = (int) CUBLAS_CREATE.invokeExact(handle);
                checkCublas(res);
                
                try {
                    MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
                    MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
                    
                    // lda = n (rows of A in column-major memory)
                    // ldb = n
                    // ldc = n (rows of C in column-major memory)
                    checkCublas((int) CUBLAS_DGEAM.invokeExact(handle.get(ValueLayout.ADDRESS, 0), 
                        1, 0, n, m, alpha, d_A.get(ValueLayout.ADDRESS, 0), n, beta, MemorySegment.NULL, n, d_C.get(ValueLayout.ADDRESS, 0), n));
                    
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
            if (t instanceof UnsupportedOperationException) throw (UnsupportedOperationException) t;
            throw new UnsupportedOperationException("CUDA transpose failed: " + t.getMessage(), t);
        }
    }


    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException(getName() + ": CUDA not available for multiply()");

        if (isComplex(a) || isComplex(b)) {
            return multiplyComplex(a, b);
        }

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

            try {
                // Copy to Device
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_A), (long) m * k * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_B), (long) k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

                // cuBLAS Handle
                MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
                checkCublas((int) CUBLAS_CREATE.invokeExact(p_Handle));
                MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

                try {
                    // cuBLAS DGEMM Setup (Alpha, Beta)
                    MemorySegment segAlpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
                    MemorySegment segBeta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);

                    // Call DGEMM
                    checkCublas((int) CUBLAS_DGEMM.invokeExact(handle, CUBLAS_OP_N, CUBLAS_OP_N, n, m, k, segAlpha, d_B, n, d_A, k, segBeta, d_C, n));

                    // Copy back
                    double[] h_C = new double[m * n];
                    MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
                    checkCuda((int) CUDA_MEMCPY.invokeExact(segC, d_C, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                    MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, h_C, 0, m * n);
                    
                    Matrix<Real> result = fromDoubleArray(h_C, m, n);
                    org.episteme.core.util.PerformanceLogger.log("MatrixMultiply", "Dense/CUDA", System.nanoTime() - start);
                    return result;
                } finally {
                    checkCublas((int) CUBLAS_DESTROY.invokeExact(handle));
                }
            } finally {
                checkCuda((int) CUDA_FREE.invokeExact(d_A));
                checkCuda((int) CUDA_FREE.invokeExact(d_B));
                checkCuda((int) CUDA_FREE.invokeExact(d_C));
            }
        } catch (Throwable t) {
            if (t instanceof UnsupportedOperationException) throw (UnsupportedOperationException) t;
            throw new UnsupportedOperationException("CUDA multiply failed: " + t.getMessage(), t);
        }
    }

    private Matrix<Real> multiplyComplex(Matrix<Real> a, Matrix<Real> b) {
        long start = System.nanoTime();
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        try (Arena arena = Arena.ofConfined()) {
            double[] h_A = toComplexDoubleArray(a);
            double[] h_B = toComplexDoubleArray(b);

            MemorySegment p_A = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment p_B = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment p_C = arena.allocate(ValueLayout.ADDRESS);

            checkCuda((int) CUDA_MALLOC.invokeExact(p_A, (long) m * k * 16));
            checkCuda((int) CUDA_MALLOC.invokeExact(p_B, (long) k * n * 16));
            checkCuda((int) CUDA_MALLOC.invokeExact(p_C, (long) m * n * 16));

            MemorySegment d_A = p_A.get(ValueLayout.ADDRESS, 0);
            MemorySegment d_B = p_B.get(ValueLayout.ADDRESS, 0);
            MemorySegment d_C = p_C.get(ValueLayout.ADDRESS, 0);

            try {
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_A), (long) m * k * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_B), (long) k * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
                checkCublas((int) CUBLAS_CREATE.invokeExact(p_Handle));
                MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);

                try {
                    MemorySegment segAlpha = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);
                    segAlpha.set(ValueLayout.JAVA_DOUBLE, 0, 1.0);
                    segAlpha.set(ValueLayout.JAVA_DOUBLE, 8, 0.0);
                    
                    MemorySegment segBeta = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);
                    segBeta.set(ValueLayout.JAVA_DOUBLE, 0, 0.0);
                    segBeta.set(ValueLayout.JAVA_DOUBLE, 8, 0.0);

                    checkCublas((int) CUBLAS_ZGEMM.invokeExact(handle, CUBLAS_OP_N, CUBLAS_OP_N, n, m, k, segAlpha, d_B, n, d_A, k, segBeta, d_C, n));

                    double[] resData = new double[m * n * 2];
                    MemorySegment hostC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
                    checkCuda((int) CUDA_MEMCPY.invokeExact(hostC, d_C, (long) m * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
                    MemorySegment.copy(hostC, ValueLayout.JAVA_DOUBLE, 0, resData, 0, m * n * 2);

                    Matrix<Real> result = fromComplexDoubleArray(resData, m, n);
                    org.episteme.core.util.PerformanceLogger.log("MatrixMultiplyComplex", "Dense/CUDA", System.nanoTime() - start);
                    return result;
                } finally {
                    checkCublas((int) CUBLAS_DESTROY.invokeExact(handle));
                }
            } finally {
                checkCuda((int) CUDA_FREE.invokeExact(d_A));
                checkCuda((int) CUDA_FREE.invokeExact(d_B));
                checkCuda((int) CUDA_FREE.invokeExact(d_C));
            }
        } catch (Throwable t) {
            throw new RuntimeException("CUDA complex multiply failed", t);
        }
    }


    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for Matrix add()");
        if (isComplex(a) || (b != null && isComplex(b))) {
            return geamComplex(a, b, Real.of(1.0), Real.of(1.0));
        }
        return geam(a, b, 1.0, 1.0);
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for Matrix subtract()");
        if (isComplex(a) || (b != null && isComplex(b))) {
            return geamComplex(a, b, Real.of(1.0), Real.of(-1.0));
        }
        return geam(a, b, 1.0, -1.0);
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for Matrix scale()");
        if (isComplex(a)) {
            return geamComplex(a, null, scalar, Real.of(0.0));
        }
        return geam(a, null, scalar.doubleValue(), 0.0);
    }

    private Matrix<Real> geam(Matrix<Real> a, Matrix<Real> b, double alphaVal, double betaVal) {
        int m = a.rows(); int n = a.cols();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p_A = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment p_B = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment p_C = arena.allocate(ValueLayout.ADDRESS);

            checkCuda((int) CUDA_MALLOC.invokeExact(p_A, (long) m * n * 8));
            checkCuda((int) CUDA_MALLOC.invokeExact(p_C, (long) m * n * 8));
            
            MemorySegment d_A = p_A.get(ValueLayout.ADDRESS, 0);
            MemorySegment d_C = p_C.get(ValueLayout.ADDRESS, 0);
            MemorySegment d_B = d_A; // Fallback to safe valid pointer 
            if (b != null) {
                checkCuda((int) CUDA_MALLOC.invokeExact(p_B, (long) m * n * 8));
                d_B = p_B.get(ValueLayout.ADDRESS, 0);
                MemorySegment segB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, segB, (long) m * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            }

            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCublas((int) CUBLAS_CREATE.invokeExact(p_Handle));
            MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);
            
            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, alphaVal);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, betaVal);
            
            // DGEAM: C = alpha * op(A) + beta * op(B). Use col-major swap for row-major.
            checkCublas((int) CUBLAS_DGEAM.invokeExact(handle, CUBLAS_OP_N, CUBLAS_OP_N, n, m, alpha, d_A, n, beta, d_B, n, d_C, n));
            checkCuda((int) CUDA_DEVICE_SYNCHRONIZE.invokeExact());
            
            double[] h_C = new double[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n);
            checkCuda((int) CUDA_MEMCPY.invokeExact(segC, d_C, (long) m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
            MemorySegment.copy(segC, ValueLayout.JAVA_DOUBLE, 0, h_C, 0, m * n);
            
            int rD = (int) CUBLAS_DESTROY.invokeExact(p_Handle.get(ValueLayout.ADDRESS, 0));
            checkCublas(rD);
            int rF1 = (int) CUDA_FREE.invokeExact(d_A);
            checkCuda(rF1);
            if (b != null) {
                int rF2 = (int) CUDA_FREE.invokeExact(d_B);
                checkCuda(rF2);
            }
            int rF3 = (int) CUDA_FREE.invokeExact(d_C);
            checkCuda(rF3);
            
            return fromDoubleArray(h_C, m, n);
        } catch (Throwable t) {
            if (t instanceof UnsupportedOperationException) throw (UnsupportedOperationException) t;
            throw new UnsupportedOperationException("CUDA GEAM operation failed: " + t.getMessage(), t);
        }
    }

    private Matrix<Real> geamComplex(Matrix<Real> a, Matrix<Real> b, Real alphaReal, Real betaReal) {
        int m = a.rows(); int n = a.cols();
        try (Arena arena = Arena.ofConfined()) {
            double[] h_A = toComplexDoubleArray(a);
            double[] h_B = (b != null) ? toComplexDoubleArray(b) : null;

            MemorySegment p_A = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment p_B = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment p_C = arena.allocate(ValueLayout.ADDRESS);

            checkCuda((int) CUDA_MALLOC.invokeExact(p_A, (long) m * n * 16));
            checkCuda((int) CUDA_MALLOC.invokeExact(p_C, (long) m * n * 16));

            MemorySegment d_A = p_A.get(ValueLayout.ADDRESS, 0);
            MemorySegment d_C = p_C.get(ValueLayout.ADDRESS, 0);

            MemorySegment d_B_val = d_A; // Fallback
            try {
                MemorySegment d_A_ptr = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_A);
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, d_A_ptr, (long) m * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
                
                if (h_B != null) {
                    checkCuda((int) CUDA_MALLOC.invokeExact(p_B, (long) m * n * 16));
                    d_B_val = p_B.get(ValueLayout.ADDRESS, 0);
                    MemorySegment d_B_ptr = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_B);
                    checkCuda((int) CUDA_MEMCPY.invokeExact(d_B_val, d_B_ptr, (long) m * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
                }

                MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
                checkCublas((int) CUBLAS_CREATE.invokeExact(p_Handle));
                MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);
                
                try {
                    MemorySegment segAlpha = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);
                    if (((Object)alphaReal) instanceof org.episteme.core.mathematics.numbers.complex.Complex cv) {
                        segAlpha.set(ValueLayout.JAVA_DOUBLE, 0, cv.getReal().doubleValue());
                        segAlpha.set(ValueLayout.JAVA_DOUBLE, 8, cv.getImaginary().doubleValue());
                    } else {
                        segAlpha.set(ValueLayout.JAVA_DOUBLE, 0, alphaReal.doubleValue());
                        segAlpha.set(ValueLayout.JAVA_DOUBLE, 8, 0.0);
                    }

                    MemorySegment segBeta = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);
                    if (((Object)betaReal) instanceof org.episteme.core.mathematics.numbers.complex.Complex cv) {
                        segBeta.set(ValueLayout.JAVA_DOUBLE, 0, cv.getReal().doubleValue());
                        segBeta.set(ValueLayout.JAVA_DOUBLE, 8, cv.getImaginary().doubleValue());
                    } else {
                        segBeta.set(ValueLayout.JAVA_DOUBLE, 0, betaReal.doubleValue());
                        segBeta.set(ValueLayout.JAVA_DOUBLE, 8, 0.0);
                    }

                    checkCublas((int) CUBLAS_ZGEAM.invokeExact(handle, CUBLAS_OP_N, CUBLAS_OP_N, n, m, segAlpha, d_A, n, segBeta, d_B_val, n, d_C, n));
                    checkCuda((int) CUDA_DEVICE_SYNCHRONIZE.invokeExact());

                    double[] resData = new double[m * n * 2];
                    MemorySegment hostC = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
                    checkCuda((int) CUDA_MEMCPY.invokeExact(hostC, d_C, (long) m * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
                    MemorySegment.copy(hostC, ValueLayout.JAVA_DOUBLE, 0, resData, 0, m * n * 2);

                    return fromComplexDoubleArray(resData, m, n);
                } finally {
                    checkCublas((int) CUBLAS_DESTROY.invokeExact(handle));
                }
            } finally {
                checkCuda((int) CUDA_FREE.invokeExact(d_A));
                if (d_B_val != MemorySegment.NULL) checkCuda((int) CUDA_FREE.invokeExact(d_B_val));
                checkCuda((int) CUDA_FREE.invokeExact(d_C));
            }
        } catch (Throwable t) {
            throw new RuntimeException("CUDA complex GEAM failed", t);
        }
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> x) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for multiply(Mat, Vec)");
        // Treat x as n x 1 matrix
        int m = a.rows(); int k = a.cols();
        Matrix<Real> xMat = fromDoubleArray(toDoubleVec(x), k, 1);
        Matrix<Real> resMat = multiply(a, xMat);
        double[] resData = new double[m];
        for(int i=0; i<m; i++) resData[i] = resMat.get(i, 0).doubleValue();
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(resData);
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for Vector add()");
        Matrix<Real> ma = fromDoubleArray(toDoubleVec(a), 1, a.dimension());
        Matrix<Real> mb = fromDoubleArray(toDoubleVec(b), 1, b.dimension());
        Matrix<Real> mc = add(ma, mb);
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(toDoubleArray(mc));
    }

    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for Vector subtract()");
        Matrix<Real> ma = fromDoubleArray(toDoubleVec(a), 1, a.dimension());
        Matrix<Real> mb = fromDoubleArray(toDoubleVec(b), 1, b.dimension());
        Matrix<Real> mc = subtract(ma, mb);
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(toDoubleArray(mc));
    }

    @Override
    public Vector<Real> multiply(Vector<Real> v, Real s) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for Vector scale()");
        Matrix<Real> mv = fromDoubleArray(toDoubleVec(v), 1, v.dimension());
        Matrix<Real> mc = scale(s, mv);
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(toDoubleArray(mc));
    }

    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) {
        if (!isAvailable() || CUBLAS_DDOT == null) throw new UnsupportedOperationException(getName() + ": CUDA dot not available");
        if (isComplex(a)) return dotComplex(a, b);
        int n = a.dimension();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment d_A = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment d_B = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment d_Res = arena.allocate(ValueLayout.JAVA_DOUBLE);
            checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) n * 8));
            checkCuda((int) CUDA_MALLOC.invokeExact(d_B, (long) n * 8));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_A.get(ValueLayout.ADDRESS, 0), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(a)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_B.get(ValueLayout.ADDRESS, 0), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(b)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCublas((int) CUBLAS_CREATE.invokeExact(p_Handle));
            checkCublas((int) CUBLAS_DDOT.invokeExact(p_Handle.get(ValueLayout.ADDRESS, 0), n, d_A.get(ValueLayout.ADDRESS, 0), 1, d_B.get(ValueLayout.ADDRESS, 0), 1, d_Res));
            checkCuda((int) CUDA_DEVICE_SYNCHRONIZE.invokeExact());
            double res = d_Res.get(ValueLayout.JAVA_DOUBLE, 0);
            int rD2 = (int) CUBLAS_DESTROY.invokeExact(p_Handle.get(ValueLayout.ADDRESS, 0));
            checkCublas(rD2);
            int rF4 = (int) CUDA_FREE.invokeExact(d_A.get(ValueLayout.ADDRESS, 0));
            checkCuda(rF4);
            int rF5 = (int) CUDA_FREE.invokeExact(d_B.get(ValueLayout.ADDRESS, 0));
            checkCuda(rF5);
            return Real.of(res);
        } catch (Throwable t) {
            if (t instanceof UnsupportedOperationException) throw (UnsupportedOperationException) t;
            throw new UnsupportedOperationException("CUDA dot product failed: " + t.getMessage(), t);
        }
    }

    @Override
    public Real norm(Vector<Real> v) {
        if (!IS_AVAILABLE || CUBLAS_DNRM2 == null) throw new UnsupportedOperationException(getName() + ": CUDA norm not available");
        int n = v.dimension();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment d_V = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment d_Res = arena.allocate(ValueLayout.JAVA_DOUBLE);
            checkCuda((int) CUDA_MALLOC.invokeExact(d_V, (long) n * 8));
            checkCuda((int) CUDA_MEMCPY.invokeExact(d_V.get(ValueLayout.ADDRESS, 0), arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleVec(v)), (long) n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
            
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCublas((int) CUBLAS_CREATE.invokeExact(p_Handle));
            checkCublas((int) CUBLAS_DNRM2.invokeExact(p_Handle.get(ValueLayout.ADDRESS, 0), n, d_V.get(ValueLayout.ADDRESS, 0), 1, d_Res));
            checkCuda((int) CUDA_DEVICE_SYNCHRONIZE.invokeExact());
            double res = d_Res.get(ValueLayout.JAVA_DOUBLE, 0);
            int rD3 = (int) CUBLAS_DESTROY.invokeExact(p_Handle.get(ValueLayout.ADDRESS, 0));
            checkCublas(rD3);
            int rF6 = (int) CUDA_FREE.invokeExact(d_V.get(ValueLayout.ADDRESS, 0));
            checkCuda(rF6);
            return Real.of(res);
        } catch (Throwable t) {
            if (t instanceof UnsupportedOperationException) throw (UnsupportedOperationException) t;
            throw new UnsupportedOperationException("CUDA norm failed: " + t.getMessage(), t);
        }
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for solve()");

        if (isComplex(a) || (b != null && isComplex(b))) {
            return solveComplex(a, b);
        }

        int n = a.rows();
        if (n != a.cols()) throw new IllegalArgumentException("Matrix must be square for solve");
        if (b == null) throw new NullPointerException("Vector b cannot be null");
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
                if (!segA.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segA)); }
                if (!segB.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segB)); }
                if (!segIpiv.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segIpiv)); }
                if (!segInfo.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segInfo)); }
                if (!segWork.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segWork)); }
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            if (t instanceof UnsupportedOperationException) throw (UnsupportedOperationException) t;
            throw new UnsupportedOperationException("CUDA solve failed: " + t.getMessage(), t);
        }
    }

    private Vector<Real> solveComplex(Matrix<Real> a, Vector<Real> b) {
        int n = a.rows();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUSOLVER_CREATE.invokeExact(p_Handle));
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

                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) n * n * 16));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_B, (long) n * 16));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Ipiv, (long) n * 4));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segB = d_B.get(ValueLayout.ADDRESS, 0);
                segIpiv = d_Ipiv.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toComplexDoubleArray(a);
                double[] h_B = toComplexDoubleVec(b);
                double[] h_At = new double[n * n * 2];
                for (int r = 0; r < n; r++) {
                    for (int c = 0; c < n; c++) {
                        h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2];
                        h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1];
                    }
                }

                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(segB, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_B), (long) n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_ZGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 16));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_ZGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
                checkCuda((int) CUSOLVER_ZGETRS.invokeExact(handle, 0, n, 1, segA, n, segIpiv, segB, n, segInfo));

                double[] resData = new double[n * 2];
                MemorySegment hostB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * 2);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostB, segB, (long) n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostB, ValueLayout.JAVA_DOUBLE, 0, resData, 0, n * 2);

                return fromComplexDoubleVec(resData);
            } finally {
                if (!segA.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segA));
                if (!segB.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segB));
                if (!segIpiv.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segIpiv));
                if (!segInfo.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segInfo));
                if (!segWork.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segWork));
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            throw new RuntimeException("CUDA complex solve failed", t);
        }
    }


    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for inverse()");

        if (isComplex(a)) {
            return inverseComplex(a);
        }

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
                if (!segA.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segA)); }
                if (!segB.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segB)); }
                if (!segIpiv.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segIpiv)); }
                if (!segInfo.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segInfo)); }
                if (!segWork.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segWork)); }
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            logger.error("CUDA inverse failed: {}", t.getMessage());
            throw new RuntimeException("CUDA Inverse Operation Failed", t);
        }
    }

    private Matrix<Real> inverseComplex(Matrix<Real> a) {
        int n = a.rows();
        double[] identity = new double[n * n * 2];
        for (int i = 0; i < n; i++) {
            identity[(i * n + i) * 2] = 1.0;
            identity[(i * n + i) * 2 + 1] = 0.0;
        }
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
            checkCuda((int) CUSOLVER_CREATE.invokeExact(p_Handle));
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

                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) n * n * 16));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_B, (long) n * n * 16));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Ipiv, (long) n * 4));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segB = d_B.get(ValueLayout.ADDRESS, 0);
                segIpiv = d_Ipiv.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toComplexDoubleArray(a);
                double[] h_At = new double[n * n * 2];
                for (int r = 0; r < n; r++) {
                    for (int c = 0; c < n; c++) {
                        h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2];
                        h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1];
                    }
                }

                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(segB, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, identity), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_ZGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 16));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_ZGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));
                checkCuda((int) CUSOLVER_ZGETRS.invokeExact(handle, 0, n, n, segA, n, segIpiv, segB, n, segInfo));

                double[] resData = new double[n * n * 2];
                MemorySegment hostB = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostB, segB, (long) n * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostB, ValueLayout.JAVA_DOUBLE, 0, resData, 0, n * n * 2);

                double[] resDataT = new double[n * n * 2];
                for (int r = 0; r < n; r++) {
                    for (int c = 0; c < n; c++) {
                        resDataT[(r * n + c) * 2] = resData[(c * n + r) * 2];
                        resDataT[(r * n + c) * 2 + 1] = resData[(c * n + r) * 2 + 1];
                    }
                }

                return fromComplexDoubleArray(resDataT, n, n);
            } finally {
                if (!segA.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segA));
                if (!segB.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segB));
                if (!segIpiv.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segIpiv));
                if (!segInfo.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segInfo));
                if (!segWork.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segWork));
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            throw new RuntimeException("CUDA complex inverse failed", t);
        }
    }


    @Override
    public Real determinant(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for determinant()");

        if (isComplex(a)) {
            return determinantComplex(a);
        }

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
                if (!segA.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segA));
                if (!segIpiv.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segIpiv));
                if (!segInfo.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segInfo));
                if (!segWork.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segWork));
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            logger.error("CUDA determinant failed: {}", t.getMessage());
            throw new RuntimeException("CUDA Determinant Operation Failed", t);
        }
    }

    private Real determinantComplex(Matrix<Real> a) {
        int n = a.rows();
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
                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) n * n * 16));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Ipiv, (long) n * 4));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segIpiv = d_Ipiv.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toComplexDoubleArray(a);
                double[] h_At = new double[n * n * 2];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) {
                    h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2];
                    h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1];
                }

                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_ZGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 16));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

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
                for (int i = 0; i < n; i++) {
                    org.episteme.core.mathematics.numbers.complex.Complex diag = org.episteme.core.mathematics.numbers.complex.Complex.of(h_QR[(i * n + i) * 2], h_QR[(i * n + i) * 2 + 1]);
                    det = det.multiply(diag);
                }

                int swaps = 0;
                for (int i = 0; i < n; i++) if (h_Ipiv[i] != (i + 1)) swaps++;
                if (swaps % 2 != 0) det = det.negate();

                return (Real) (Object) det;
            } finally {
                if (!segA.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segA));
                if (!segIpiv.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segIpiv));
                if (!segInfo.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segInfo));
                if (!segWork.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segWork));
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            throw new RuntimeException("CUDA complex determinant failed", t);
        }
    }

    private LUResult<Real> luComplex(Matrix<Real> a) {
        int n = a.rows();
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
                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) n * n * 16));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Ipiv, (long) n * 4));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segIpiv = d_Ipiv.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toComplexDoubleArray(a);
                double[] h_At = new double[n * n * 2];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) {
                    h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2];
                    h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1];
                }

                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_ZGETRF_BUFFER_SIZE.invokeExact(handle, n, n, segA, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 16));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_ZGETRF.invokeExact(handle, n, n, segA, n, segWork, segIpiv, segInfo));

                double[] h_LU = new double[n * n * 2];
                MemorySegment hostLU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostLU, segA, (long) n * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostLU, ValueLayout.JAVA_DOUBLE, 0, h_LU, 0, n * n * 2);

                int[] h_Ipiv = new int[n];
                MemorySegment hostIpiv = arena.allocate(ValueLayout.JAVA_INT, (long) n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostIpiv, segIpiv, (long) n * 4, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostIpiv, ValueLayout.JAVA_INT, 0, h_Ipiv, 0, n);

                Real[][] L = new Real[n][n];
                Real[][] U = new Real[n][n];
                for (int r = 0; r < n; r++) {
                    for (int c = 0; c < n; c++) {
                        double re = h_LU[(c * n + r) * 2];
                        double im = h_LU[(c * n + r) * 2 + 1];
                        org.episteme.core.mathematics.numbers.complex.Complex val = org.episteme.core.mathematics.numbers.complex.Complex.of(re, im);
                        if (r > c) {
                            L[r][c] = (Real)(Object)val;
                            U[r][c] = (Real)(Object)org.episteme.core.mathematics.numbers.complex.Complex.ZERO;
                        } else if (r < c) {
                            L[r][c] = (Real)(Object)org.episteme.core.mathematics.numbers.complex.Complex.ZERO;
                            U[r][c] = (Real)(Object)val;
                        } else {
                            L[r][c] = (Real)(Object)org.episteme.core.mathematics.numbers.complex.Complex.ONE;
                            U[r][c] = (Real)(Object)val;
                        }
                    }
                }
                Real[] P = new Real[n];
                for (int i = 0; i < n; i++) P[i] = Real.of(h_Ipiv[i]);

                return new LUResult<>(
                    Matrix.of(L, Reals.getInstance()),
                    Matrix.of(U, Reals.getInstance()),
                    Vector.of(P, Reals.getInstance())
                );
            } finally {
                if (!segA.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segA));
                if (!segIpiv.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segIpiv));
                if (!segInfo.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segInfo));
                if (!segWork.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segWork));
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            throw new RuntimeException("CUDA complex LU failed", t);
        }
    }

    private QRResult<Real> qrComplex(Matrix<Real> a) {
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
                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) m * n * 16));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Tau, (long) k * 16));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segTau = d_Tau.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toComplexDoubleArray(a);
                double[] h_At = new double[m * n * 2];
                for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) {
                    h_At[(c * m + r) * 2] = h_A[(r * n + c) * 2];
                    h_At[(c * m + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1];
                }

                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) m * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_ZGEQRF_BUFFER_SIZE.invokeExact(handle, m, n, segA, m, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 16));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_ZGEQRF.invokeExact(handle, m, n, segA, m, segTau, segWork, lwork, segInfo));

                double[] h_QR = new double[m * n * 2];
                MemorySegment hostQR = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * n * 2);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostQR, segA, (long) m * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostQR, ValueLayout.JAVA_DOUBLE, 0, h_QR, 0, m * n * 2);

                Real[][] rMatrixData = new Real[m][n];
                for (int r = 0; r < m; r++) {
                    for (int c = 0; c < n; c++) {
                        if (c >= r) {
                            rMatrixData[r][c] = (Real)(Object)org.episteme.core.mathematics.numbers.complex.Complex.of(h_QR[(c * m + r) * 2], h_QR[(c * m + r) * 2 + 1]);
                        } else {
                            rMatrixData[r][c] = (Real)(Object)org.episteme.core.mathematics.numbers.complex.Complex.ZERO;
                        }
                    }
                }
                Matrix<Real> R = Matrix.of(rMatrixData, Reals.getInstance());

                checkCuda((int) CUSOLVER_ZORGQR_BUFFER_SIZE.invokeExact(handle, m, k, k, segA, m, segTau, p_Lwork));
                int lworkQ = p_Lwork.get(ValueLayout.JAVA_INT, 0);
                if (lworkQ > lwork) {
                    checkCuda((int) CUDA_FREE.invokeExact(segWork));
                    MemorySegment d_WorkQ = arena.allocate(ValueLayout.ADDRESS);
                    checkCuda((int) CUDA_MALLOC.invokeExact(d_WorkQ, (long) lworkQ * 16));
                    segWork = d_WorkQ.get(ValueLayout.ADDRESS, 0);
                    lwork = lworkQ;
                }
                checkCuda((int) CUSOLVER_ZORGQR.invokeExact(handle, m, k, k, segA, m, segTau, segWork, lwork, segInfo));

                double[] h_Q = new double[m * k * 2];
                MemorySegment hostQ = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * k * 2);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostQ, segA, (long) m * k * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostQ, ValueLayout.JAVA_DOUBLE, 0, h_Q, 0, m * k * 2);

                double[] h_QT = new double[m * k * 2];
                for (int r = 0; r < m; r++) for (int c = 0; c < k; c++) {
                    h_QT[(r * k + c) * 2] = h_Q[(c * m + r) * 2];
                    h_QT[(r * k + c) * 2 + 1] = h_Q[(c * m + r) * 2 + 1];
                }

                return new QRResult<>(fromComplexDoubleArray(h_QT, m, k), R);
            } finally {
                if (!segA.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segA));
                if (!segTau.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segTau));
                if (!segInfo.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segInfo));
                if (!segWork.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segWork));
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            throw new RuntimeException("CUDA complex QR failed", t);
        }
    }

    private SVDResult<Real> svdComplex(Matrix<Real> a) {
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

                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) m * n * 16));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_S, (long) k * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_U, (long) m * m * 16));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_VT, (long) n * n * 16));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segS = d_S.get(ValueLayout.ADDRESS, 0);
                segU = d_U.get(ValueLayout.ADDRESS, 0);
                segVT = d_VT.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toComplexDoubleArray(a);
                double[] h_At = new double[m * n * 2];
                for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) {
                    h_At[(c * m + r) * 2] = h_A[(r * n + c) * 2];
                    h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1];
                }

                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) m * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_ZGESVD_BUFFER_SIZE.invokeExact(handle, m, n, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 16));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_ZGESVD.invokeExact(handle, (byte)'A', (byte)'A', m, n, segA, m, segS, segU, m, segVT, n, segWork, lwork, MemorySegment.NULL, segInfo));

                double[] h_S = new double[k];
                MemorySegment hostS = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) k);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostS, segS, (long) k * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostS, ValueLayout.JAVA_DOUBLE, 0, h_S, 0, k);

                double[] h_U = new double[m * m * 2];
                MemorySegment hostU = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * m * 2);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostU, segU, (long) m * m * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostU, ValueLayout.JAVA_DOUBLE, 0, h_U, 0, m * m * 2);

                double[] h_VT = new double[n * n * 2];
                MemorySegment hostVT = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostVT, segVT, (long) n * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostVT, ValueLayout.JAVA_DOUBLE, 0, h_VT, 0, n * n * 2);

                double[] h_UT = new double[m * m * 2];
                for (int r = 0; r < m; r++) for (int c = 0; c < m; c++) {
                    h_UT[(r * m + c) * 2] = h_U[(c * m + r) * 2];
                    h_UT[(r * m + c) * 2 + 1] = h_U[(c * m + r) * 2 + 1];
                }

                double[] h_VTT = new double[n * n * 2];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) {
                    h_VTT[(r * n + c) * 2] = h_VT[(c * n + r) * 2];
                    h_VTT[(r * n + c) * 2 + 1] = h_VT[(c * n + r) * 2 + 1];
                }

                return new SVDResult<>(fromComplexDoubleArray(h_UT, m, m), fromDoubleVec(h_S), fromComplexDoubleArray(h_VTT, n, n));
            } finally {
                if (!segA.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segA));
                if (!segS.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segS));
                if (!segU.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segU));
                if (!segVT.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segVT));
                if (!segInfo.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segInfo));
                if (!segWork.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segWork));
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            throw new RuntimeException("CUDA complex SVD failed", t);
        }
    }

    private EigenResult<Real> eigenComplex(Matrix<Real> a) {
        int n = a.rows();
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
                checkCuda((int) CUDA_MALLOC.invokeExact(d_A, (long) n * n * 16));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_W, (long) n * 8));
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Info, (long) 4));

                segA = d_A.get(ValueLayout.ADDRESS, 0);
                segW = d_W.get(ValueLayout.ADDRESS, 0);
                segInfo = d_Info.get(ValueLayout.ADDRESS, 0);

                double[] h_A = toComplexDoubleArray(a);
                double[] h_At = new double[n * n * 2];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) {
                    h_At[(c * n + r) * 2] = h_A[(r * n + c) * 2];
                    h_At[(c * n + r) * 2 + 1] = h_A[(r * n + c) * 2 + 1];
                }

                checkCuda((int) CUDA_MEMCPY.invokeExact(segA, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_At), (long) n * n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));

                MemorySegment p_Lwork = arena.allocate(ValueLayout.JAVA_INT);
                checkCuda((int) CUSOLVER_ZHEEVD_BUFFER_SIZE.invokeExact(handle, 1, 0, n, segA, n, segW, p_Lwork));
                int lwork = p_Lwork.get(ValueLayout.JAVA_INT, 0);

                MemorySegment d_Work = arena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUDA_MALLOC.invokeExact(d_Work, (long) lwork * 16));
                segWork = d_Work.get(ValueLayout.ADDRESS, 0);

                checkCuda((int) CUSOLVER_ZHEEVD.invokeExact(handle, 1, 0, n, segA, n, segW, segWork, lwork, segInfo));

                double[] h_W = new double[n];
                MemorySegment hostW = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * 8);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostW, segW, (long) n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostW, ValueLayout.JAVA_DOUBLE, 0, h_W, 0, n);

                double[] h_V = new double[n * n * 2];
                MemorySegment hostV = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) n * n * 2);
                checkCuda((int) CUDA_MEMCPY.invokeExact(hostV, segA, (long) n * n * 16, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(hostV, ValueLayout.JAVA_DOUBLE, 0, h_V, 0, n * n * 2);

                double[] h_VT = new double[n * n * 2];
                for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) {
                    h_VT[(r * n + c) * 2] = h_V[(c * n + r) * 2];
                    h_VT[(r * n + c) * 2 + 1] = h_V[(c * n + r) * 2 + 1];
                }

                return new EigenResult<>(fromComplexDoubleArray(h_VT, n, n), fromDoubleVec(h_W));
            } finally {
                if (!segA.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segA));
                if (!segW.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segW));
                if (!segInfo.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segInfo));
                if (!segWork.equals(MemorySegment.NULL)) checkCuda((int) CUDA_FREE.invokeExact(segWork));
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            throw new RuntimeException("CUDA complex Eigen failed", t);
        }
    }




    private boolean isComplex(Matrix<Real> m) {
        if (m.rows() == 0 || m.cols() == 0) return false;
        return ((Object)m.get(0, 0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private boolean isComplex(Vector<Real> v) {
        if (v.dimension() == 0) return false;
        return ((Object)v.get(0)) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private Real dotComplex(Vector<Real> a, Vector<Real> b) {
        int n = a.dimension();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p_A = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment p_B = arena.allocate(ValueLayout.ADDRESS);
            MemorySegment p_Res = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);
            checkCuda((int) CUDA_MALLOC.invokeExact(p_A, (long) n * 16));
            checkCuda((int) CUDA_MALLOC.invokeExact(p_B, (long) n * 16));
            
            MemorySegment d_A = p_A.get(ValueLayout.ADDRESS, 0);
            MemorySegment d_B = p_B.get(ValueLayout.ADDRESS, 0);

            try {
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_A, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleVec(a)), (long) n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(d_B, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleVec(b)), (long) n * 16, CUDA_MEMCPY_HOST_TO_DEVICE));
                
                MemorySegment p_Handle = arena.allocate(ValueLayout.ADDRESS);
                checkCublas((int) CUBLAS_CREATE.invokeExact(p_Handle));
                MemorySegment handle = p_Handle.get(ValueLayout.ADDRESS, 0);
                
                try {
                    checkCublas((int) CUBLAS_ZDOTU.invokeExact(handle, n, d_A, 1, d_B, 1, p_Res));
                    double re = p_Res.get(ValueLayout.JAVA_DOUBLE, 0);
                    double im = p_Res.get(ValueLayout.JAVA_DOUBLE, 8);
                    return (Real) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(re, im);
                } finally {
                    checkCublas((int) CUBLAS_DESTROY.invokeExact(handle));
                }
            } finally {
                checkCuda((int) CUDA_FREE.invokeExact(d_A));
                checkCuda((int) CUDA_FREE.invokeExact(d_B));
            }
        } catch (Throwable t) {
            throw new RuntimeException("CUDA complex dot failed", t);
        }
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

    private double[] toComplexDoubleArray(Matrix<Real> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] data = new double[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Real val = m.get(i, j);
                if (((Object)val) instanceof org.episteme.core.mathematics.numbers.complex.Complex cv) {
                    data[(i * cols + j) * 2] = cv.getReal().doubleValue();
                    data[(i * cols + j) * 2 + 1] = cv.getImaginary().doubleValue();
                } else {
                    data[(i * cols + j) * 2] = val.doubleValue();
                    data[(i * cols + j) * 2 + 1] = 0.0;
                }
            }
        }
        return data;
    }


    private Matrix<Real> fromDoubleArray(double[] data, int rows, int cols) {
        Real[] reals = new Real[data.length];
        for(int i=0; i<data.length; i++) reals[i] = Real.of(data[i]);
        return new DenseMatrix<Real>(reals, rows, cols, Reals.getInstance());
    }

    private Matrix<Real> fromComplexDoubleArray(double[] data, int rows, int cols) {
        Real[] reals = new Real[rows * cols];
        for (int i = 0; i < rows * cols; i++) {
            reals[i] = (Real) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
        }
        return new DenseMatrix<Real>(reals, rows, cols, Reals.getInstance());
    }

    private Vector<Real> fromDoubleVec(double[] data) {
        return org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(data);
    }

    private Vector<Real> fromComplexDoubleVec(double[] data) {
        int dim = data.length / 2;
        Real[] reals = new Real[dim];
        for (int i = 0; i < dim; i++) {
            reals[i] = (Real) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(data[i * 2], data[i * 2 + 1]);
        }
        return org.episteme.core.mathematics.linearalgebra.Vector.of(reals, org.episteme.core.mathematics.sets.Reals.getInstance());
    }

    private double[] toDoubleVec(Vector<Real> v) {
        double[] d = new double[v.dimension()];
        for (int i = 0; i < d.length; i++) d[i] = v.get(i).doubleValue();
        return d;
    }

    private double[] toComplexDoubleVec(Vector<Real> v) {
        int dim = v.dimension();
        double[] data = new double[dim * 2];
        for (int i = 0; i < dim; i++) {
            Real val = v.get(i);
            if (((Object)val) instanceof org.episteme.core.mathematics.numbers.complex.Complex cv) {
                data[i * 2] = cv.getReal().doubleValue();
                data[i * 2 + 1] = cv.getImaginary().doubleValue();
            } else {
                data[i * 2] = val.doubleValue();
                data[i * 2 + 1] = 0.0;
            }
        }
        return data;
    }

    @Override
    public QRResult<Real> qr(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for qr()");

        if (isComplex(a)) {
            return qrComplex(a);
        }

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
                    checkCuda((int) CUDA_FREE.invokeExact(segWork));
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

                return new QRResult<Real>(Q, R);

            } finally {
                if (!segA.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segA)); }
                if (!segTau.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segTau)); }
                if (!segInfo.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segInfo)); }
                if (!segWork.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segWork)); }
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            logger.error("CUDA QR failed: {}", t.getMessage());
            throw new RuntimeException("CUDA QR Operation Failed", t);
        }
    }


    @Override
    public SVDResult<Real> svd(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for svd()");

        if (isComplex(a)) {
            return svdComplex(a);
        }

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

                return new SVDResult<Real>(
                    fromDoubleArray(uData, m, m),
                    fromDoubleVec(h_S),
                    fromDoubleArray(vData, n, n)
                );

            } finally {
                if (!segA.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segA)); }
                if (!segS.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segS)); }
                if (!segU.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segU)); }
                if (!segVT.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segVT)); }
                if (!segInfo.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segInfo)); }
                if (!segWork.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segWork)); }
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            logger.error("CUDA SVD failed: {}", t.getMessage());
            throw new RuntimeException("CUDA SVD Operation Failed", t);
        }
    }


    // toDoubleVec is already defined above at line 868 (duplicated at line 1090)
    // Removing the duplicated copy.

    @Override public String getNativeLibraryName() { return "cuda"; }
    @Override public DeviceInfo[] getDevices() { return new DeviceInfo[0]; }
    @Override public void selectDevice(int deviceId) { }
    @Override
    public org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult<Real> cholesky(Matrix<Real> a) {
        if (!IS_AVAILABLE || CUSOLVER_DPOTRF == null) throw new UnsupportedOperationException(getName() + ": CUDA not available for cholesky()");

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

                return new CholeskyResult<Real>(fromDoubleArray(lData, n, n));
            } finally {
                if (!segA.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segA)); }
                if (!segInfo.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segInfo)); }
                if (!segWork.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segWork)); }
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            logger.error("CUDA Cholesky failed: {}", t.getMessage());
            throw new RuntimeException("CUDA Cholesky Operation Failed", t);
        }
    }


    @Override
    public LUResult<Real> lu(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for lu()");

        if (isComplex(a)) {
            return luComplex(a);
        }

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

                return new LUResult<Real>(
                    fromDoubleArray(lData, n, n),
                    fromDoubleArray(uData, n, n),
                    fromDoubleVec(pData)
                );
            } finally {
                if (!segA.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segA)); }
                if (!segIpiv.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segIpiv)); }
                if (!segInfo.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segInfo)); }
                if (!segWork.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segWork)); }
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            logger.error("CUDA LU failed: {}", t.getMessage());
            throw new RuntimeException("CUDA LU Operation Failed", t);
        }
    }

    @Override
    public EigenResult<Real> eigen(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": CUDA not available for eigen()");

        if (isComplex(a)) {
            return eigenComplex(a);
        }

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

                return new EigenResult<Real>(
                    fromDoubleArray(vData, n, n),
                    fromDoubleVec(h_W)
                );
            } finally {
                if (!segA.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segA)); }
                if (!segW.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segW)); }
                if (!segInfo.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segInfo)); }
                if (!segWork.equals(MemorySegment.NULL)) { checkCuda((int) CUDA_FREE.invokeExact(segWork)); }
                checkCublas((int) CUSOLVER_DESTROY.invokeExact(handle));
            }
        } catch (Throwable t) {
            logger.error("CUDA Eigen failed: {}", t.getMessage());
            throw new RuntimeException("CUDA Eigen Operation Failed", t);
        }
    }


    @Override
    public void copyToGPU(long handle, java.nio.DoubleBuffer buffer, long count) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment host = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, buffer.array());
            // Use scavenge-protected segment instead of raw reinterpretation
            MemorySegment device = NativeSafe.scavenge(MemorySegment.ofAddress(handle), count * 8, arena, "cuda_device_query").segment();
            NativeSafe.invoke(CUDA_MEMCPY, device, host, count * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
        } catch (Throwable t) {
            logger.error("Failed to copy to GPU: {}", t.getMessage());
            throw new RuntimeException("CUDA Copy To GPU Failed", t);
        }
    }

    @Override
    public void copyFromGPU(long handle, java.nio.DoubleBuffer buffer, long count) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, count);
            MemorySegment device = MemorySegment.ofAddress(handle).reinterpret(count * 8, arena, null);
            NativeSafe.invoke(CUDA_MEMCPY, host, device, count * 8, CUDA_MEMCPY_DEVICE_TO_HOST);
            MemorySegment.copy(host, ValueLayout.JAVA_DOUBLE, 0, buffer.array(), 0, (int) count);
        } catch (Throwable t) {
            logger.error("Failed to copy from GPU: {}", t.getMessage());
            throw new RuntimeException("CUDA Copy From GPU Failed", t);
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
        if (handle == 0) return;
        try {
            // Reinterpret with size 0 to satisfy type, but the address is what matters for free
            NativeSafe.invoke(CUDA_FREE, MemorySegment.ofAddress(handle));
        } catch (Throwable t) {
            logger.error("Failed to free GPU memory at 0x{}: {}", Long.toHexString(handle), t.getMessage());
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
    public void shutdown() {
        logger.info("CUDADenseLinearAlgebraBackend shutting down...");
        try {
            synchronize();
            // We only shutdown the loader if this is the primary user 
            // but for now global shutdown is fine as we are exiting.
            NativeFFMLoader.shutdown();
        } catch (Exception e) {
            logger.warn("Error during CUDA backend shutdown: {}", e.getMessage());
        }
    }

    @Override
    public java.util.Map<String, String> getMetadata() {
        java.util.Map<String, String> meta = new java.util.HashMap<>();
        meta.put("accelerator", "gpu");
        meta.put("api", "cuda");
        meta.put("precision", "fp64");
        meta.put("vendor", "nvidia");
        meta.put("solver", "cusolver");
        meta.put("optimized_size", ">256");
        return meta;
    }

    private static void checkCuda(int result) {
        if (result != 0) {
            if (CUDA_GET_ERROR_STRING == null) {
                throw new RuntimeException("CUDA Error " + result + " (Error string lookup not initialized)");
            }
            try {
                MemorySegment seg = (MemorySegment) CUDA_GET_ERROR_STRING.invokeExact(result);
                String msg = seg.reinterpret(1024).getString(0);
                logger.error("CUDA Error {}: {}", result, msg);
                throw new RuntimeException("CUDA Error " + result + ": " + msg);
            } catch (Throwable t) {
                throw new RuntimeException("CUDA Error " + result, t);
            }
        }
    }

    private static void checkCublas(int result) {
        if (result != 0) {
            if (CUBLAS_STATUS_GET_STRING == null) {
                throw new RuntimeException("CUBLAS Error " + result + " (Error string lookup not initialized)");
            }
            try {
                MemorySegment seg = (MemorySegment) CUBLAS_STATUS_GET_STRING.invokeExact(result);
                String msg = seg.reinterpret(1024).getString(0);
                logger.error("CUBLAS Error {}: {}", result, msg);
                throw new RuntimeException("CUBLAS Error " + result + ": " + msg);
            } catch (Throwable t) {
                throw new RuntimeException("CUBLAS Error " + result, t);
            }
        }
    }
}
