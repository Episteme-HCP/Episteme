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
 
    private static MemorySegment CUBLAS_HANDLE;
    private static MemorySegment CUSPARSE_HANDLE;

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

    private static MethodHandle lookup(SymbolLookup lookup, String name, FunctionDescriptor descriptor) {
        return NativeFFMLoader.findSymbol(lookup, name)
            .map(s -> LINKER.downcallHandle(s, descriptor))
            .orElse(null);
    }

    private static synchronized void ensureInitialized() {
        if (IS_AVAILABLE) return;

        try (Arena tempArena = Arena.ofConfined()) {
            // 1. Load CUDA Runtime
            Optional<SymbolLookup> cudaRtOpt = NativeFFMLoader.loadLibrary("cudart", tempArena);
            if (cudaRtOpt.isEmpty() && System.getProperty("os.name").toLowerCase().contains("linux")) {
                Path linuxPath = Path.of("/usr/local/cuda/lib64/libcudart.so");
                if (Files.exists(linuxPath)) {
                    cudaRtOpt = NativeFFMLoader.loadLibrary(linuxPath.toString(), tempArena);
                }
            }
            if (cudaRtOpt.isEmpty()) return;
            SymbolLookup cudart = cudaRtOpt.get();

            // 2. cuSPARSE
            Optional<SymbolLookup> cusparseOpt = NativeFFMLoader.loadLibrary("cusparse", tempArena);
            if (cusparseOpt.isEmpty() && System.getProperty("os.name").toLowerCase().contains("linux")) {
                Path linuxPath = Path.of("/usr/local/cuda/lib64/libcusparse.so");
                if (Files.exists(linuxPath)) {
                    cusparseOpt = NativeFFMLoader.loadLibrary(linuxPath.toString(), tempArena);
                }
            }
            if (cusparseOpt.isEmpty()) return;
            cusparse_lookup = cusparseOpt.get();

            // 3. cuBLAS
            Optional<SymbolLookup> cublasOpt = NativeFFMLoader.loadLibrary("cublas", tempArena);
            if (cublasOpt.isEmpty() && System.getProperty("os.name").toLowerCase().contains("linux")) {
                Path linuxPath = Path.of("/usr/local/cuda/lib64/libcublas.so");
                if (Files.exists(linuxPath)) {
                    cublasOpt = NativeFFMLoader.loadLibrary(linuxPath.toString(), tempArena);
                }
            }
            if (cublasOpt.isEmpty()) return;
            SymbolLookup cublas = cublasOpt.get();

            // 4. cuSolver
            Optional<SymbolLookup> cusolverOpt = NativeFFMLoader.loadLibrary("cusolver", tempArena);
            if (cusolverOpt.isEmpty() && System.getProperty("os.name").toLowerCase().contains("linux")) {
                Path linuxPath = Path.of("/usr/local/cuda/lib64/libcusolver.so");
                if (Files.exists(linuxPath)) {
                    cusolverOpt = NativeFFMLoader.loadLibrary(linuxPath.toString(), tempArena);
                }
            }
            if (cusolverOpt.isPresent()) {
                cusolver_lookup = cusolverOpt.get();
            }

            // 5. Bind Handles
            CUDA_MALLOC = lookup(cudart, "cudaMalloc", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
            CUDA_FREE = lookup(cudart, "cudaFree", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUDA_MEMCPY = lookup(cudart, "cudaMemcpy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
            CUDA_DEVICE_SYNCHRONIZE = lookup(cudart, "cudaDeviceSynchronize", FunctionDescriptor.of(ValueLayout.JAVA_INT));

            CUSPARSE_CREATE = lookup(cusparse_lookup, "cusparseCreate", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSPARSE_DESTROY = lookup(cusparse_lookup, "cusparseDestroy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSPARSE_CREATE_MAT_DESCR = lookup(cusparse_lookup, "cusparseCreateMatDescr", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            CUSPARSE_DESTROY_MAT_DESCR = lookup(cusparse_lookup, "cusparseDestroyMatDescr", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            
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
            
            CUBLAS_DNRM2 = lookup(cublas, "cublasDnrm2_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));

            CUBLAS_DAXPY = lookup(cublas, "cublasDaxpy_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

            CUBLAS_DSCAL = lookup(cublas, "cublasDscal_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            
            CUBLAS_DGEAM = lookup(cublas, "cublasDgeam_v2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, 
                ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

            if (cusolver_lookup != null) {
                CUSOLVER_SP_CREATE = lookup(cusolver_lookup, "cusolverSpCreate", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                CUSOLVER_SP_DESTROY = lookup(cusolver_lookup, "cusolverSpDestroy", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                CUSOLVER_SP_D_CSRLSV_LU = lookup(cusolver_lookup, "cusolverSpDcsrlsvlu", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, 
                    ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
            }

            // 6. Verify GPU and Start
            MethodHandle getDeviceCount = lookup(cudart, "cudaGetDeviceCount", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            if (getDeviceCount != null) {
                MemorySegment countPtr = tempArena.allocate(ValueLayout.JAVA_INT);
                int res = (int) getDeviceCount.invokeExact(countPtr);
                int count = countPtr.get(ValueLayout.JAVA_INT, 0);
                if (res != 0 || count <= 0) return;
                
                // Kickstart
                if (CUDA_FREE != null) {
                    checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.NULL));
                }

                // Global Handles
                MemorySegment chPtr = tempArena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUBLAS_CREATE.invokeExact(chPtr));
                CUBLAS_HANDLE = chPtr.get(ValueLayout.ADDRESS, 0);

                MemorySegment shPtr = tempArena.allocate(ValueLayout.ADDRESS);
                checkCuda((int) CUSPARSE_CREATE.invokeExact(shPtr));
                CUSPARSE_HANDLE = shPtr.get(ValueLayout.ADDRESS, 0);
            }

            IS_AVAILABLE = true;
            logger.info("Native CUDA Sparse Backend initialized successfully.");
        } catch (Throwable t) {
            t.printStackTrace();
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
            return;
        }

        try (Arena arena = Arena.ofConfined()) {
            long d_A = allocateGPUMemory((long) m * k * 8);
            long d_B = allocateGPUMemory((long) k * n * 8);
            long d_C = allocateGPUMemory((long) m * n * 8);

            copyToGPU(d_A, A, (long) m * k, arena);
            copyToGPU(d_B, B, (long) k * n, arena);

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);

            checkCuda((int) CUBLAS_DGEMM.invokeExact(CUBLAS_HANDLE, 0, 0, n, m, k, alpha, 
                NativeSafe.scavenge(MemorySegment.ofAddress(d_B), (long)k * n * 8, arena, "cublas_dgemm_b").segment(), n, 
                NativeSafe.scavenge(MemorySegment.ofAddress(d_A), (long)m * k * 8, arena, "cublas_dgemm_a").segment(), k, 
                beta, 
                NativeSafe.scavenge(MemorySegment.ofAddress(d_C), (long)m * n * 8, arena, "cublas_dgemm_c").segment(), n));

            copyFromGPU(d_C, C, (long) m * n, arena);

            checkCuda((int) CUDA_FREE.invokeExact(NativeSafe.scavenge(MemorySegment.ofAddress(d_A), 0, Arena.global(), "gpu_free_a").segment()));
            checkCuda((int) CUDA_FREE.invokeExact(NativeSafe.scavenge(MemorySegment.ofAddress(d_B), 0, Arena.global(), "gpu_free_b").segment()));
            checkCuda((int) CUDA_FREE.invokeExact(NativeSafe.scavenge(MemorySegment.ofAddress(d_C), 0, Arena.global(), "gpu_free_c").segment()));
        } catch (Throwable t) {
            logger.error("cuBLAS DGEMM failed: {}", t.getMessage());
        }
    }

    @Override
    public void selectDevice(int deviceId) {
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
        return org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider.super.multiply(a, b);
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

                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
                MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
                MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);
                
                checkCuda((int) CUSPARSE_SPMV_BUFFER_SIZE.invokeExact(CUSPARSE_HANDLE, 0, alpha, matA, vecX, beta, vecY, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr));
                long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
                long d_buffer = bufferSize > 0 ? allocateGPUMemory(bufferSize) : 0;

                checkCuda((int) CUSPARSE_SPMV.invokeExact(CUSPARSE_HANDLE, 0, alpha, matA, vecX, beta, vecY, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, MemorySegment.ofAddress(d_buffer)));

                double[] resHost = new double[m];
                MemorySegment h_res = arena.allocate(ValueLayout.JAVA_DOUBLE, m);
                checkCuda((int) CUDA_MEMCPY.invokeExact(h_res, MemorySegment.ofAddress(d_vecY), (long)m * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(h_res, ValueLayout.JAVA_DOUBLE, 0, resHost, 0, m);

                if (d_buffer != 0) { checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_buffer))); }

                return toSparseVector(resHost);
            } finally {
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrRowPtr)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrColIdx)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrVal)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_vecX)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_vecY)));
            }
        } catch (Throwable t) {
            logger.error("cuSPARSE SpMV failed: {}", t.getMessage());
            return multiplyDense(a, b.toMatrix()).getColumn(0);
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
            int[] rowPtrHost = a.getRowPointers();
            int[] colIdxHost = a.getColIndices();
            Object[] valsObj = a.getValues();
            double[] valsHost = new double[nnz];
            for (int i = 0; i < nnz; i++) valsHost[i] = ((Real) valsObj[i]).doubleValue();

            long d_csrRowPtr = allocateGPUMemory((long)(m + 1) * 4);
            long d_csrColIdx = allocateGPUMemory((long)nnz * 4);
            long d_csrVal = allocateGPUMemory((long)nnz * 8);
            long d_denseB = allocateGPUMemory((long)k * n * 8);
            long d_denseC = allocateGPUMemory((long)m * n * 8);

            try {
                MemorySegment h_rowPtr = arena.allocateFrom(ValueLayout.JAVA_INT, rowPtrHost);
                MemorySegment h_colIdx = arena.allocateFrom(ValueLayout.JAVA_INT, colIdxHost);
                MemorySegment h_vals = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, valsHost);
                MemorySegment h_denseB = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(b));

                checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrRowPtr), h_rowPtr, (long)(m + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrColIdx), h_colIdx, (long)nnz * 4, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrVal), h_vals, (long)nnz * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
                checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_denseB), h_denseB, (long)k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE));

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

                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
                MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
                MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);
                
                checkCuda((int) CUSPARSE_SPMM_BUFFER_SIZE.invokeExact(CUSPARSE_HANDLE, 0, 0, alpha, matA, matB, beta, matC, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr));
                long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
                long d_buffer = bufferSize > 0 ? allocateGPUMemory(bufferSize) : 0;

                checkCuda((int) CUSPARSE_SPMM.invokeExact(CUSPARSE_HANDLE, 0, 0, alpha, matA, matB, beta, matC, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, MemorySegment.ofAddress(d_buffer)));

                double[] resHost = new double[m * n];
                MemorySegment h_res = arena.allocate(ValueLayout.JAVA_DOUBLE, m * n);
                checkCuda((int) CUDA_MEMCPY.invokeExact(h_res, MemorySegment.ofAddress(d_denseC), (long)m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
                MemorySegment.copy(h_res, ValueLayout.JAVA_DOUBLE, 0, resHost, 0, m * n);

                if (d_buffer != 0) { checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_buffer))); }

                return toSparseMatrix(resHost, m, n);
            } finally {
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrRowPtr)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrColIdx)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrVal)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_denseB)));
                checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_denseC)));
            }
        } catch (Throwable t) {
            logger.error("cuSPARSE SpMM failed: {}", t.getMessage());
            throw new RuntimeException("CUDA SpMM failed", t);
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
                double[] array = new double[(int) count];
                hostBuf.duplicate().get(array);
                h_seg = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, array);
            }
            checkCuda((int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_ptr), h_seg, count * 8, CUDA_MEMCPY_HOST_TO_DEVICE));
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
            checkCuda((int) CUDA_MEMCPY.invokeExact(h_seg, MemorySegment.ofAddress(d_ptr), count * 8, CUDA_MEMCPY_DEVICE_TO_HOST));
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

    private Matrix<Real> toSparseMatrix(double[] data, int rows, int cols) {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, Real.ZERO);
        for (int i = 0; i < data.length; i++) {
            if (data[i] != 0.0) {
                storage.set(i / cols, i % cols, Real.of(data[i]));
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, Real.ZERO);
    }

    private Vector<Real> toSparseVector(double[] data) {
        int n = data.length;
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(n, Real.ZERO);
        for (int i = 0; i < n; i++) {
            if (data[i] != 0.0) {
                storage.set(i, Real.of(data[i]));
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, Reals.getInstance());
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

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, alphaVal);
            MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, betaVal);

            checkCuda((int) CUBLAS_DGEAM.invokeExact(CUBLAS_HANDLE, 0, 0, n, m, alpha, 
                MemorySegment.ofAddress(d_a).reinterpret((long) m * n * 8), n, 
                beta, 
                MemorySegment.ofAddress(d_b).reinterpret((long) m * n * 8), n, 
                MemorySegment.ofAddress(d_c).reinterpret((long) m * n * 8), n));

            double[] result = new double[m * n];
            copyFromGPU(d_c, DoubleBuffer.wrap(result), (long) m * n, arena);

            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_a)));
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_b)));
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_c)));

            return toSparseMatrix(result, m, n);
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

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, scalar.doubleValue());
            checkCuda((int) CUBLAS_DSCAL.invokeExact(CUBLAS_HANDLE, m * n, alpha, MemorySegment.ofAddress(d_a).reinterpret((long) m * n * 8), 1));

            double[] result = new double[m * n];
            copyFromGPU(d_a, DoubleBuffer.wrap(result), (long) m * n, arena);
            
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_a)));
            
            return toSparseMatrix(result, m, n);
        } catch (Throwable t) {
            throw new RuntimeException("cuBLAS scale failed", t);
        }
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
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

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
            checkCuda((int) CUBLAS_DAXPY.invokeExact(CUBLAS_HANDLE, n, alpha, 
                MemorySegment.ofAddress(d_b).reinterpret((long) n * 8), 1, 
                MemorySegment.ofAddress(d_a).reinterpret((long) n * 8), 1));

            double[] result = new double[n];
            copyFromGPU(d_a, DoubleBuffer.wrap(result), n, arena);
            
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_a)));
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_b)));
            
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

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, -1.0);
            checkCuda((int) CUBLAS_DAXPY.invokeExact(CUBLAS_HANDLE, n, alpha, 
                MemorySegment.ofAddress(d_b).reinterpret((long) n * 8), 1, 
                MemorySegment.ofAddress(d_a).reinterpret((long) n * 8), 1));

            double[] result = new double[n];
            copyFromGPU(d_a, DoubleBuffer.wrap(result), n, arena);
            
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_a)));
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_b)));
            
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

            MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, s.doubleValue());
            checkCuda((int) CUBLAS_DSCAL.invokeExact(CUBLAS_HANDLE, n, alpha, MemorySegment.ofAddress(d_v).reinterpret((long) n * 8), 1));

            double[] result = new double[n];
            copyFromGPU(d_v, DoubleBuffer.wrap(result), n, arena);
            
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_v)));
            
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

            MemorySegment resultPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
            checkCuda((int) CUBLAS_DDOT.invokeExact(CUBLAS_HANDLE, n, 
                MemorySegment.ofAddress(d_v1).reinterpret((long) n * 8), 1, 
                MemorySegment.ofAddress(d_v2).reinterpret((long) n * 8), 1, 
                resultPtr));

            double dot = resultPtr.get(ValueLayout.JAVA_DOUBLE, 0);
            
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_v1)));
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_v2)));
            
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

            MemorySegment resultPtr = arena.allocate(ValueLayout.JAVA_DOUBLE);
            checkCuda((int) CUBLAS_DNRM2.invokeExact(CUBLAS_HANDLE, n, 
                MemorySegment.ofAddress(d_v).reinterpret((long) n * 8), 1, 
                resultPtr));

            double norm = resultPtr.get(ValueLayout.JAVA_DOUBLE, 0);
            
            checkCuda((int) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_v)));
            
            return Real.of(norm);
        } catch (Throwable t) {
            throw new RuntimeException("cuBLAS Dnrm2 failed", t);
        }
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
        if (context.hasHint(Hint.DENSE)) return -1.0;
        double base = getPriority();
        if (context.hasHint(Hint.SPARSE)) base += 40.0;
        if (context.hasHint(Hint.GPU_RESIDENT)) base += 50.0;
        if (context.getDataSize() < 500) base -= 200.0;
        return base;
    }

    @Override
    public Vector<Real> bicgstab(Matrix<Real> a, Vector<Real> b, Vector<Real> x0, Real tolerance, int maxIterations) {
        throw new UnsupportedOperationException("CUDA BiCGSTAB implementation pending");
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

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        throw new UnsupportedOperationException("Native CUDA Inverse implementation pending");
    }

    @Override
    public Real determinant(Matrix<Real> a) {
        throw new UnsupportedOperationException("Native CUDA Determinant implementation pending");
    }
}
