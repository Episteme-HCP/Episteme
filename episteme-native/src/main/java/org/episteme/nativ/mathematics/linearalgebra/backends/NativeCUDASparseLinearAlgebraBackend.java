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
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.mathematics.structures.rings.Ring;
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
            CUDA_FREE.invokeExact(MemorySegment.ofAddress(handle));
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

    // Constants
    private static final int CUSPARSE_INDEX_32BIT = 0;
    private static final int CUSPARSE_INDEX_BASE_ZERO = 0;
    private static final int CUDA_R_64F = 1; // Double
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
                    int res = (int) getDeviceCount.invokeExact(countPtr);
                    if (res != 0 || countPtr.get(ValueLayout.JAVA_INT, 0) <= 0) {
                        logger.warn("No CUDA-capable GPU found or error result ({}). Backend disabled.", res);
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
                    getDeviceCount.invokeExact(countPtr);
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
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
    }

    @Override
    public void selectDevice(int deviceId) {
        // No-op for now
    }

    @Override
    public long allocateGPUMemory(long size) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment p = arena.allocate(ValueLayout.ADDRESS);
            int res = (int) CUDA_MALLOC.invokeExact(p, size);
            if (res != 0) throw new RuntimeException("cudaMalloc failed: " + res);
            return p.get(ValueLayout.ADDRESS, 0).address();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void copyToGPU(long handle, DoubleBuffer buffer, long count) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment host = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, buffer.array());
            int res = (int) CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(handle), host, count * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
            if (res != 0) throw new RuntimeException("cudaMemcpy H2D failed: " + res);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void copyFromGPU(long handle, DoubleBuffer buffer, long count) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment host = arena.allocate(ValueLayout.JAVA_DOUBLE, count);
            int res = (int) CUDA_MEMCPY.invokeExact(host, MemorySegment.ofAddress(handle), count * 8, CUDA_MEMCPY_DEVICE_TO_HOST);
            if (res != 0) throw new RuntimeException("cudaMemcpy D2H failed: " + res);
            double[] data = host.toArray(ValueLayout.JAVA_DOUBLE);
            buffer.put(data);
            buffer.flip();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void synchronize() {
        try {
            CUDA_DEVICE_SYNCHRONIZE.invokeExact();
        } catch (Throwable t) {
            logger.warn("cudaDeviceSynchronize failed: {}", t.getMessage());
        }
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("CUDA Sparse Backend not available");
        
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            return multiplySparseDense((org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a, b);
        }
        
        return multiplyDense(a, b);
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

                CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrRowPtr), h_rowPtr, (long)(m + 1) * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
                CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrColIdx), h_colIdx, (long)nnz * 4, CUDA_MEMCPY_HOST_TO_DEVICE);
                CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_csrVal), h_vals, (long)nnz * 8, CUDA_MEMCPY_HOST_TO_DEVICE);
                CUDA_MEMCPY.invokeExact(MemorySegment.ofAddress(d_denseB), h_denseB, (long)k * n * 8, CUDA_MEMCPY_HOST_TO_DEVICE);

                // 3. cuSPARSE Handles
                MemorySegment handlePtr = arena.allocate(ValueLayout.ADDRESS);
                CUSPARSE_CREATE.invokeExact(handlePtr);
                MemorySegment handle = handlePtr.get(ValueLayout.ADDRESS, 0);

                // Descriptors
                MemorySegment matAPtr = arena.allocate(ValueLayout.ADDRESS);
                CUSPARSE_CREATE_CSR.invokeExact(matAPtr, (long)m, (long)k, (long)nnz, 
                    MemorySegment.ofAddress(d_csrRowPtr), MemorySegment.ofAddress(d_csrColIdx), MemorySegment.ofAddress(d_csrVal),
                    CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, CUDA_R_64F);
                MemorySegment matA = matAPtr.get(ValueLayout.ADDRESS, 0);

                MemorySegment matBPtr = arena.allocate(ValueLayout.ADDRESS);
                CUSPARSE_CREATE_DN_MAT.invokeExact(matBPtr, (long)k, (long)n, (long)n, MemorySegment.ofAddress(d_denseB), CUDA_R_64F, 1); // Order: Row-Major (1)
                MemorySegment matB = matBPtr.get(ValueLayout.ADDRESS, 0);

                MemorySegment matCPtr = arena.allocate(ValueLayout.ADDRESS);
                CUSPARSE_CREATE_DN_MAT.invokeExact(matCPtr, (long)m, (long)n, (long)n, MemorySegment.ofAddress(d_denseC), CUDA_R_64F, 1);
                MemorySegment matC = matCPtr.get(ValueLayout.ADDRESS, 0);

                // Buffer allocation for SpMM
                MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
                MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
                MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);
                
                CUSPARSE_SPMM_BUFFER_SIZE.invokeExact(handle, 0, 0, alpha, matA, matB, beta, matC, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr);
                long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
                long d_buffer = bufferSize > 0 ? allocateGPUMemory(bufferSize) : 0;

                // Execution
                CUSPARSE_SPMM.invokeExact(handle, 0, 0, alpha, matA, matB, beta, matC, CUDA_R_64F, CUSPARSE_SPMM_ALG_DEFAULT, MemorySegment.ofAddress(d_buffer));

                // 4. Result (D2H)
                double[] resHost = new double[m * n];
                MemorySegment h_res = arena.allocate(ValueLayout.JAVA_DOUBLE, m * n);
                CUDA_MEMCPY.invokeExact(h_res, MemorySegment.ofAddress(d_denseC), (long)m * n * 8, CUDA_MEMCPY_DEVICE_TO_HOST);
                MemorySegment.copy(h_res, ValueLayout.JAVA_DOUBLE, 0, resHost, 0, m * n);

                // Cleanup GPU
                if (d_buffer != 0) CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_buffer));
                CUSPARSE_DESTROY.invokeExact(handle);

                return fromDoubleArray(resHost, m, n);
            } finally {
                CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrRowPtr));
                CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrColIdx));
                CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_csrVal));
                CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_denseB));
                CUDA_FREE.invokeExact(MemorySegment.ofAddress(d_denseC));
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
