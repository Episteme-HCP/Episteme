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
    
    @Override
    public boolean isAvailable() {
        if (isExplicitlyDisabled()) return false;
        return CUDAManager.isAvailable();
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        return ring.zero() instanceof RealFloat;
    }

    @Override public String getId() { return "cuda-dense-float"; }
    @Override public String getName() { return "Native CUDA Dense Linear Algebra Float Backend"; }
    @Override public int getPriority() { return 115; }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int m = a.rows(); int k = a.cols(); int n = b.cols();
        
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_A = malloc((long) m * k * 4, tracker);
            MemorySegment d_B = malloc((long) k * n * 4, tracker);
            MemorySegment d_C = malloc((long) m * n * 4, tracker);
            
            float[] hA = toFloatArray(a);
            float[] hB = toFloatArray(b);
            
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY_H_TO_D, d_A.address(), arena.allocateFrom(ValueLayout.JAVA_FLOAT, hA), (long) m * k * 4));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY_H_TO_D, d_B.address(), arena.allocateFrom(ValueLayout.JAVA_FLOAT, hB), (long) k * n * 4));
            
            MemorySegment handle = CUDAManager.getCublasHandle();
            checkCublas((int) NativeSafe.invoke(CUDAManager.CUBLAS_SGEMM, handle, 0, 0, n, m, k, 
                arena.allocateFrom(ValueLayout.JAVA_FLOAT, 1.0f), d_B, n, d_A, k, 
                arena.allocateFrom(ValueLayout.JAVA_FLOAT, 0.0f), d_C, n));
            
            float[] result = new float[m * n];
            MemorySegment segC = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m * n);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY_D_TO_H, segC, d_C.address(), (long) m * n * 4));
            MemorySegment.copy(segC, ValueLayout.JAVA_FLOAT, 0, result, 0, m * n);
            
            return fromFloatArray(result, m, n, (Ring<E>) a.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("CUDA float multiply failed", t);
        }
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

    private Matrix<E> fromFloatArray(float[] data, int rows, int cols, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for (int i = 0; i < data.length; i++) values[i] = (E) RealFloat.create(data[i]);
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(values, rows, cols, ring);
    }

    @Override public void close() {}
    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.GPU; }
    @Override public String getType() { return "math"; }
}
