/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.RealFloat;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.structures.rings.FieldElement;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.gpu.GPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.ResourceTracker;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.episteme.nativ.technical.backend.gpu.cuda.CUDAManager;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Native CUDA Dense Linear Algebra Backend for Float precision.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, GPUBackend.class})
public class NativeCUDADenseLinearAlgebraFloatBackend<E extends FieldElement<E>> implements LinearAlgebraProvider<E>, NativeBackend, GPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(NativeCUDADenseLinearAlgebraFloatBackend.class);

    @Override public boolean isLoaded() { return isAvailable(); }
    @Override public String getNativeLibraryName() { return "cuda"; }
    
    @Override
    public boolean isAvailable() {
        if (isExplicitlyDisabled()) return false;
        return CUDAManager.isAvailable();
    }

    public boolean isExplicitlyDisabled() {
        return Boolean.getBoolean("episteme.backend.native.disabled") ||
               Boolean.getBoolean("episteme.backend.cuda.disabled") || 
               Boolean.getBoolean("episteme.backend.gpu.disabled") ||
               Boolean.getBoolean("episteme.backend.linear-algebra.disabled") ||
               Boolean.getBoolean("episteme.backend.linear-algebra-cuda.disabled");
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        Object zero = ring.zero();
        return zero instanceof RealFloat || (zero instanceof org.episteme.core.mathematics.numbers.complex.Complex c && c.getReal() instanceof RealFloat);
    }

    @Override public String getId() { return "cuda-dense-float"; }
    @Override public String getName() { return "Native CUDA Dense Linear Algebra Float Backend"; }
    @Override public int getPriority() { return 115; }
    @Override public void shutdown() {}

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return multiplyComplex(a, b);
        
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * 4, tracker);
            MemorySegment d_B = malloc((long) k * n * 4, tracker);
            MemorySegment d_C = malloc((long) m * n * 4, tracker);
            
            float[] hA = toFloatArray(a);
            float[] hB = toFloatArray(b);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A.address(), arena.allocateFrom(ValueLayout.JAVA_FLOAT, hA), (long) m * k * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B.address(), arena.allocateFrom(ValueLayout.JAVA_FLOAT, hB), (long) k * n * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_SGEMM, handle, 0, 0, n, m, k, 
                arena.allocateFrom(ValueLayout.JAVA_FLOAT, 1.0f), d_B, n, d_A, k, 
                arena.allocateFrom(ValueLayout.JAVA_FLOAT, 0.0f), d_C, n));
            
            float[] result = new float[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m * n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segC, d_C.address(), (long) m * n * 4, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segC, ValueLayout.JAVA_FLOAT, 0, result, 0, m * n);
            
            return fromFloatArray(result, m, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("CUDA float multiply failed", t);
        }
    }

    private Matrix<E> multiplyComplex(Matrix<E> a, Matrix<E> b) {
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * 8, tracker);
            MemorySegment d_B = malloc((long) k * n * 8, tracker);
            MemorySegment d_C = malloc((long) m * n * 8, tracker);
            float[] hA = toComplexFloatArray(a);
            float[] hB = toComplexFloatArray(b);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_A, arena.allocateFrom(ValueLayout.JAVA_FLOAT, hA), (long) m * k * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_B, arena.allocateFrom(ValueLayout.JAVA_FLOAT, hB), (long) k * n * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_CGEMM, handle, 0, 0, n, m, k, 
                arena.allocateFrom(ValueLayout.JAVA_FLOAT, new float[]{1.0f, 0.0f}), d_B, n, d_A, k, 
                arena.allocateFrom(ValueLayout.JAVA_FLOAT, new float[]{0.0f, 0.0f}), d_C, n));
            float[] result = new float[m * n * 2];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m * n * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segC, d_C, (long) m * n * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segC, ValueLayout.JAVA_FLOAT, 0, result, 0, m * n * 2);
            return fromComplexFloatArray(result, m, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    private MemorySegment malloc(long size, ResourceTracker tracker) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MALLOC, p, size));
            MemorySegment d = p.get(ValueLayout.ADDRESS, 0);
            return tracker.track(d, ptr -> {
                try { NativeSafe.invoke(CUDAManager.CUDA_FREE, ptr.address()); } catch (Throwable t) {}
            });
        } catch (Throwable t) { throw new RuntimeException("CUDA malloc failed", t); }
    }

    private void checkCuda(int status) {
        if (status != 0) throw new RuntimeException("CUDA error: " + status);
    }

    private void checkCublas(int status) {
        if (status != 0) throw new RuntimeException("cuBLAS error: " + status);
    }

    private boolean isComplex(Matrix<E> m) {
        return m.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private float[] toFloatArray(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        float[] data = new float[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = ((Number) m.get(i, j)).floatValue();
            }
        }
        return data;
    }

    private float[] toComplexFloatArray(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        float[] data = new float[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) m.get(i, j);
                data[(i * cols + j) * 2] = c.getReal().floatValue();
                data[(i * cols + j) * 2 + 1] = c.getImaginary().floatValue();
            }
        }
        return data;
    }

    private Matrix<E> fromFloatArray(float[] data, int rows, int cols, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for (int i = 0; i < data.length; i++) values[i] = (E) (Object) RealFloat.create(data[i]);
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(values, rows, cols, ring);
    }

    private Matrix<E> fromComplexFloatArray(float[] data, int rows, int cols, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), rows * cols);
        for (int i = 0; i < rows * cols; i++) {
            values[i] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(RealFloat.create(data[i * 2]), RealFloat.create(data[i * 2 + 1]));
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(values, rows, cols, ring);
    }

    @Override public void close() {}
    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.GPU; }
    @Override public String getType() { return "math"; }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.nativ.technical.backend.gpu.cuda.CUDAExecutionContext();
    }

    @Override
    public org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[] getDevices() {
        if (!isAvailable()) return new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[0];
        return new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo[]{
            new org.episteme.core.technical.backend.gpu.GPUBackend.DeviceInfo(
                "CUDA Device", 0, 0, "CUDA")
        };
    }

    @Override
    public void selectDevice(int deviceId) {}

    @Override
    public long allocateGPUMemory(long sizeBytes) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MALLOC, p, sizeBytes));
            return p.get(ValueLayout.ADDRESS, 0).address();
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public void copyToGPU(long gpuHandle, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, MemorySegment.ofAddress(gpuHandle), MemorySegment.ofBuffer(hostBuffer), sizeBytes, CUDAManager.CUDA_MEMCPY_H_TO_D));
    }

    @Override
    public void copyFromGPU(long gpuHandle, java.nio.DoubleBuffer hostBuffer, long sizeBytes) {
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, MemorySegment.ofBuffer(hostBuffer), MemorySegment.ofAddress(gpuHandle), sizeBytes, CUDAManager.CUDA_MEMCPY_D_TO_H));
    }

    @Override
    public void freeGPUMemory(long gpuHandle) {
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_FREE, MemorySegment.ofAddress(gpuHandle)));
    }

    @Override
    public void synchronize() {
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_DEVICE_SYNCHRONIZE));
    }

    @Override
    public void matrixMultiply(java.nio.DoubleBuffer A, java.nio.DoubleBuffer B, java.nio.DoubleBuffer C, int m, int n, int k) {
        throw new UnsupportedOperationException("Matrix multiply for DoubleBuffer not implemented in float backend");
    }
}
