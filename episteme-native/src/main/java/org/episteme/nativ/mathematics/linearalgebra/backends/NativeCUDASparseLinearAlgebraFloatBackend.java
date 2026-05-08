/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
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
 * Native CUDA Sparse Linear Algebra Backend for Float precision.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, GPUBackend.class})
public class NativeCUDASparseLinearAlgebraFloatBackend<E extends FieldElement<E>> implements SparseLinearAlgebraProvider<E>, NativeBackend, GPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(NativeCUDASparseLinearAlgebraFloatBackend.class);
    
    private static final int CUDA_R_32F = 0; // Float precision in CUDA
    private static final int CUSPARSE_INDEX_32BIT = 1;
    private static final int CUSPARSE_INDEX_BASE_ZERO = 0;
    private static final int CUSPARSE_SPMM_ALG_DEFAULT = 0;

    @Override
    public boolean isAvailable() {
        if (isExplicitlyDisabled()) return false;
        return CUDAManager.isAvailable();
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        return ring.zero() instanceof RealFloat;
    }

    @Override public String getId() { return "cuda-sparse-float"; }
    @Override public String getName() { return "Native CUDA Sparse Linear Algebra Float Backend"; }
    @Override public int getPriority() { return 110; }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> x) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (!(a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix)) throw new IllegalArgumentException("Matrix must be sparse");
        
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) a;
        int m = sa.rows();
        int k = sa.cols();
        int nnz = sa.getNnz();

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_rowPtr = malloc((long)(m + 1) * 4, tracker);
            MemorySegment d_colIdx = malloc((long)nnz * 4, tracker);
            MemorySegment d_val = malloc((long)nnz * 4, tracker);
            MemorySegment d_x = malloc((long)k * 4, tracker);
            MemorySegment d_y = malloc((long)m * 4, tracker);

            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_rowPtr, arena.allocateFrom(ValueLayout.JAVA_INT, sa.getRowPointers()), (long)(m + 1) * 4, 1));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_colIdx, arena.allocateFrom(ValueLayout.JAVA_INT, sa.getColIndices()), (long)nnz * 4, 1));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_val, arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(sa)), (long)nnz * 4, 1));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_x, arena.allocateFrom(ValueLayout.JAVA_FLOAT, toFloatArray(x)), (long)k * 4, 1));

            spmv_internal(m, k, nnz, d_rowPtr, d_colIdx, d_val, d_x, d_y, arena, tracker);

            float[] h_y = new float[m];
            MemorySegment segY = arena.allocate(ValueLayout.JAVA_FLOAT, (long) m);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segY, d_y, (long) m * 4, 2));
            MemorySegment.copy(segY, ValueLayout.JAVA_FLOAT, 0, h_y, 0, m);

            return toVector(h_y, (Ring<E>) sa.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("CUDA float SpMV failed", t);
        }
    }

    private void spmv_internal(int m, int k, int nnz, MemorySegment d_rowPtr, MemorySegment d_colIdx, MemorySegment d_val, MemorySegment d_x, MemorySegment d_y, Arena arena, ResourceTracker tracker) throws Throwable {
        MemorySegment matAPtr = arena.allocate(ValueLayout.ADDRESS);
        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_CSR, matAPtr, (long)m, (long)k, (long)nnz, d_rowPtr, d_colIdx, d_val, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, CUDA_R_32F));
        MemorySegment matA = matAPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(matA, s -> { try { NativeSafe.invoke(CUDAManager.CUSPARSE_DESTROY_SP_MAT, s); } catch (Throwable t) {} });

        MemorySegment vecXPtr = arena.allocate(ValueLayout.ADDRESS);
        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_DN_VEC, vecXPtr, (long)k, d_x, CUDA_R_32F));
        MemorySegment vecX = vecXPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(vecX, s -> { try { NativeSafe.invoke(CUDAManager.CUSPARSE_DESTROY_DN_VEC, s); } catch (Throwable t) {} });

        MemorySegment vecYPtr = arena.allocate(ValueLayout.ADDRESS);
        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_DN_VEC, vecYPtr, (long)m, d_y, CUDA_R_32F));
        MemorySegment vecY = vecYPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(vecY, s -> { try { NativeSafe.invoke(CUDAManager.CUSPARSE_DESTROY_DN_VEC, s); } catch (Throwable t) {} });

        MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_FLOAT, 1.0f);
        MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_FLOAT, 0.0f);
        MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);

        MemorySegment handle = CUDAManager.getCusparseHandle();
        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_SPMV_BUFFER_SIZE, handle, 0, alpha, matA, vecX, beta, vecY, CUDA_R_32F, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr));
        long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
        MemorySegment d_buffer = malloc(bufferSize, tracker);

        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_SPMV, handle, 0, alpha, matA, vecX, beta, vecY, CUDA_R_32F, CUSPARSE_SPMM_ALG_DEFAULT, d_buffer));
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

    private void checkCusparse(int status) {
        if (status != 0) throw new RuntimeException("cuSPARSE error: " + status);
    }

    private float[] toFloatArray(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> m) {
        Object[] vals = m.getValues();
        float[] data = new float[vals.length];
        for (int i = 0; i < vals.length; i++) data[i] = ((Number) vals[i]).floatValue();
        return data;
    }

    private float[] toFloatArray(Vector<E> v) {
        int dim = v.dimension();
        float[] data = new float[dim];
        for (int i = 0; i < dim; i++) data[i] = ((Number) v.get(i)).floatValue();
        return data;
    }

    private Vector<E> toVector(float[] data, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for (int i = 0; i < data.length; i++) values[i] = (E) RealFloat.create(data[i]);
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(values), null, ring);
    }

    @Override public void close() {}
    @Override public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() { return org.episteme.core.technical.backend.HardwareAccelerator.GPU; }
    @Override public String getType() { return "math"; }
}
