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
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealFloat;
import org.episteme.core.mathematics.numbers.complex.Complex;
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

    @Override public boolean isLoaded() { return isAvailable(); }
    @Override public String getNativeLibraryName() { return "cuda"; }
    
    private static final int CUDA_R_32F = 0; // Float precision in CUDA
    private static final int CUSPARSE_INDEX_32BIT = 1;
    private static final int CUSPARSE_INDEX_BASE_ZERO = 0;
    private static final int CUSPARSE_SPMM_ALG_DEFAULT = 0;
    private static final int CUSPARSE_ORDER_ROW = 2;

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
        return zero instanceof RealFloat || (zero instanceof Complex c && c.getReal() instanceof RealFloat);
    }

    @Override public String getId() { return "cuda-sparse-float"; }
    @Override public String getName() { return "Native CUDA Sparse Linear Algebra Float Backend"; }
    @Override public int getPriority() { return 110; }
    @Override public void shutdown() {}

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> x) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return multiplyComplex(a, x);
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = ensureSparse(a);
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

            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_rowPtr, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_INT, sa.getRowPointers()), (long)(m + 1) * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_colIdx, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_INT, sa.getColIndices()), (long)nnz * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_val, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, toFloatArray(sa)), (long)nnz * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_x, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, toFloatArray(x)), (long)k * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));

            spmv_internal(m, k, nnz, d_rowPtr, d_colIdx, d_val, d_x, d_y, arena, tracker, CUDA_R_32F);

            float[] h_y = new float[m];
            MemorySegment segY = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) m);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segY, d_y, (long) m * 4, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segY, ValueLayout.JAVA_FLOAT, 0, h_y, 0, m);

            return toVector(h_y, (Ring<E>) sa.getScalarRing());
        } catch (Throwable t) {
            throw new RuntimeException("CUDA float SpMV failed", t);
        }
    }

    private Vector<E> multiplyComplex(Matrix<E> a, Vector<E> x) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = ensureSparse(a);
        int m = sa.rows(); int k = sa.cols(); int nnz = sa.getNnz();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_rowPtr = malloc((long)(m + 1) * 4, tracker);
            MemorySegment d_colIdx = malloc((long)nnz * 4, tracker);
            MemorySegment d_val = malloc((long)nnz * 8, tracker);
            MemorySegment d_x = malloc((long)k * 8, tracker);
            MemorySegment d_y = malloc((long)m * 8, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_rowPtr, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_INT, sa.getRowPointers()), (long)(m + 1) * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_colIdx, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_INT, sa.getColIndices()), (long)nnz * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_val, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, toComplexFloatArray(sa)), (long)nnz * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_x, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, toComplexFloatVec(x)), (long)k * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            spmv_internal(m, k, nnz, d_rowPtr, d_colIdx, d_val, d_x, d_y, arena, tracker, 6); // 6 = CUDA_C_32F
            float[] h_y = new float[m * 2];
            MemorySegment segY = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) m * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, segY, d_y, (long) m * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(segY, ValueLayout.JAVA_FLOAT, 0, h_y, 0, m * 2);
            return toVectorComplex(h_y, (Ring<E>) sa.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    private void spmv_internal(int m, int k, int nnz, MemorySegment d_rowPtr, MemorySegment d_colIdx, MemorySegment d_val, MemorySegment d_x, MemorySegment d_y, Arena arena, ResourceTracker tracker, int dataType) throws Throwable {
        MemorySegment matAPtr = NativeSafe.allocate(arena, ValueLayout.ADDRESS);
        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_CSR, matAPtr, (long)m, (long)k, (long)nnz, d_rowPtr, d_colIdx, d_val, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, dataType));
        MemorySegment matA = matAPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(matA, s -> { try { NativeSafe.invoke(CUDAManager.CUSPARSE_DESTROY_SP_MAT, s); } catch (Throwable t) {} });

        MemorySegment vecXPtr = NativeSafe.allocate(arena, ValueLayout.ADDRESS);
        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_DN_VEC, vecXPtr, (long)k, d_x, dataType));
        MemorySegment vecX = vecXPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(vecX, s -> { try { NativeSafe.invoke(CUDAManager.CUSPARSE_DESTROY_DN_VEC, s); } catch (Throwable t) {} });

        MemorySegment vecYPtr = NativeSafe.allocate(arena, ValueLayout.ADDRESS);
        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_DN_VEC, vecYPtr, (long)m, d_y, dataType));
        MemorySegment vecY = vecYPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(vecY, s -> { try { NativeSafe.invoke(CUDAManager.CUSPARSE_DESTROY_DN_VEC, s); } catch (Throwable t) {} });

        MemorySegment alpha = NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, 1.0f);
        MemorySegment beta = NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, 0.0f);
        if (dataType == 6) { // CUDA_C_32F
            alpha = NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, 1.0f, 0.0f);
            beta = NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, 0.0f, 0.0f);
        }
        MemorySegment bufferSizePtr = NativeSafe.allocate(arena, ValueLayout.JAVA_LONG);

        MemorySegment handle = CUDAManager.getCusparseHandle();
        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_SPMV_BUFFER_SIZE, handle, 0, alpha, matA, vecX, beta, vecY, dataType, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr));
        long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
        MemorySegment d_buffer = malloc(bufferSize, tracker);

        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_SPMV, handle, 0, alpha, matA, vecX, beta, vecY, dataType, CUSPARSE_SPMM_ALG_DEFAULT, d_buffer));
    }

    private MemorySegment malloc(long size, ResourceTracker tracker) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = NativeSafe.allocate(temp, ValueLayout.ADDRESS);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MALLOC, p, size));
            MemorySegment d = p.get(ValueLayout.ADDRESS, 0);
            return tracker.track(d, ptr -> {
                try { NativeSafe.invoke(CUDAManager.CUDA_FREE, ptr); } catch (Throwable t) {}
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
        for (int i = 0; i < vals.length; i++) data[i] = getRealValue(vals[i]);
        return data;
    }

    private float[] toFloatArrayGeneric(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        float[] data = new float[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i * cols + j] = getRealValue(m.get(i, j));
            }
        }
        return data;
    }

    private float[] toComplexFloatArrayGeneric(Matrix<E> m) {
        int rows = m.rows(); int cols = m.cols();
        float[] data = new float[rows * cols * 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Complex c = getComplex(m.get(i, j));
                data[(i * cols + j) * 2] = (float) c.real();
                data[(i * cols + j) * 2 + 1] = (float) c.imaginary();
            }
        }
        return data;
    }

    private float[] toFloatArray(Vector<E> v) {
        int dim = v.dimension();
        float[] data = new float[dim];
        for (int i = 0; i < dim; i++) data[i] = getRealValue(v.get(i));
        return data;
    }

    private org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> ensureSparse(Matrix<E> A) {
        if (A instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) return (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) A;
        int rows = A.rows(); int cols = A.cols(); Ring<E> ring = A.getScalarRing();
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, ring.zero());
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) {
            E val = A.get(i, j);
            if (val != null && !ring.zero().equals(val)) storage.set(i, j, val);
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, ring);
    }

    private Vector<E> toVector(float[] data, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for (int i = 0; i < data.length; i++) values[i] = (E) (Object) RealFloat.create(data[i]);
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(values), null, ring);
    }

    private boolean isComplex(Matrix<E> m) {
        return m.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private float[] toComplexFloatArray(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> m) {
        Object[] vals = m.getValues();
        float[] data = new float[vals.length * 2];
        for (int i = 0; i < vals.length; i++) {
            org.episteme.core.mathematics.numbers.complex.Complex c = getComplex(vals[i]);
            data[i * 2] = (float) c.real();
            data[i * 2 + 1] = (float) c.imaginary();
        }
        return data;
    }

    private float[] toComplexFloatVec(Vector<E> v) {
        int dim = v.dimension();
        float[] data = new float[dim * 2];
        for (int i = 0; i < dim; i++) {
            org.episteme.core.mathematics.numbers.complex.Complex c = getComplex(v.get(i));
            data[i * 2] = (float) c.real();
            data[i * 2 + 1] = (float) c.imaginary();
        }
        return data;
    }

    private Vector<E> toVectorComplex(float[] data, Ring<E> ring) {
        int dim = data.length / 2;
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), dim);
        for (int i = 0; i < dim; i++) {
            values[i] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(RealFloat.create(data[i * 2]), RealFloat.create(data[i * 2 + 1]));
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(values), null, ring);
    }

    @Override
    public Matrix<E> toDense(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = ensureSparse(a);
        int m = sa.rows(); int n = sa.cols(); int nnz = sa.getNnz();
        boolean isComplex = isComplex(sa);
        int elemSize = isComplex ? 8 : 4;
        int dataType = isComplex ? 4 : 0; // CUDA_C_32F : CUDA_R_32F

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_rowPtr = malloc((long)(m + 1) * 4, tracker);
            MemorySegment d_colIdx = malloc((long)nnz * 4, tracker);
            MemorySegment d_val = malloc((long)nnz * elemSize, tracker);
            MemorySegment d_dense = malloc((long)m * n * elemSize, tracker);

            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_rowPtr, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_INT, sa.getRowPointers()), (long)(m + 1) * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_colIdx, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_INT, sa.getColIndices()), (long)nnz * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_val, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, isComplex ? toComplexFloatArray(sa) : toFloatArray(sa)), (long)nnz * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));

            MemorySegment handle = CUDAManager.getCusparseHandle();
            MemorySegment spMatDescr = NativeSafe.allocate(arena, ValueLayout.ADDRESS);
            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_CSR, spMatDescr, (long)m, (long)n, (long)nnz, d_rowPtr, d_colIdx, d_val, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, dataType));
            
            MemorySegment dnMatDescr = NativeSafe.allocate(arena, ValueLayout.ADDRESS);
            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_DN_MAT, dnMatDescr, (long)m, (long)n, (long)n, d_dense, dataType, CUSPARSE_ORDER_ROW));

            MemorySegment bufferSizePtr = NativeSafe.allocate(arena, ValueLayout.JAVA_LONG);
            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_SPARSE_TO_DENSE_BUFFER_SIZE, handle, spMatDescr.get(ValueLayout.ADDRESS, 0), dnMatDescr.get(ValueLayout.ADDRESS, 0), 0, bufferSizePtr));
            long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
            MemorySegment d_buffer = malloc(bufferSize, tracker);

            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_SPARSE_TO_DENSE, handle, spMatDescr.get(ValueLayout.ADDRESS, 0), dnMatDescr.get(ValueLayout.ADDRESS, 0), 0, d_buffer));

            MemorySegment h_seg = NativeSafe.allocate(arena, (long)m * n * elemSize);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, h_seg, d_dense, (long)m * n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_H));
            float[] resultArr = h_seg.toArray(ValueLayout.JAVA_FLOAT);

            if (isComplex) {
                return (Matrix<E>) createComplexDenseMatrix(resultArr, m, n, (Ring<E>) sa.getScalarRing());
            } else {
                // Return a generic dense matrix for float (or specialized if exists)
                return (Matrix<E>) createRealDenseMatrix(resultArr, m, n, (Ring<E>) sa.getScalarRing());
            }
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public Matrix<E> fromDense(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int m = a.rows(); int n = a.cols();
        boolean isComplex = isComplex(a);
        int elemSize = isComplex ? 8 : 4;
        int dataType = isComplex ? 4 : 0;

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_dense = malloc((long)m * n * elemSize, tracker);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_dense, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, isComplex ? toComplexFloatArrayGeneric(a) : toFloatArrayGeneric(a)), (long)m * n * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));

            MemorySegment handle = CUDAManager.getCusparseHandle();
            MemorySegment dnMatDescr = NativeSafe.allocate(arena, ValueLayout.ADDRESS);
            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_DN_MAT, dnMatDescr, (long)m, (long)n, (long)n, d_dense, dataType, CUSPARSE_ORDER_ROW));

            MemorySegment spMatDescr = NativeSafe.allocate(arena, ValueLayout.ADDRESS);
            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_CSR, spMatDescr, (long)m, (long)n, 0L, MemorySegment.NULL, MemorySegment.NULL, MemorySegment.NULL, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, dataType));

            MemorySegment bufferSizePtr = NativeSafe.allocate(arena, ValueLayout.JAVA_LONG);
            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_DENSE_TO_SPARSE_BUFFER_SIZE, handle, dnMatDescr.get(ValueLayout.ADDRESS, 0), spMatDescr.get(ValueLayout.ADDRESS, 0), 0, bufferSizePtr));
            long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
            MemorySegment d_buffer = malloc(bufferSize, tracker);

            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_DENSE_TO_SPARSE, handle, dnMatDescr.get(ValueLayout.ADDRESS, 0), spMatDescr.get(ValueLayout.ADDRESS, 0), 0, d_buffer));
            
            MemorySegment rowsPtr = NativeSafe.allocate(arena, ValueLayout.JAVA_LONG);
            MemorySegment colsPtr = NativeSafe.allocate(arena, ValueLayout.JAVA_LONG);
            MemorySegment nnzPtr = NativeSafe.allocate(arena, ValueLayout.JAVA_LONG);
            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_GET_SIZE, spMatDescr.get(ValueLayout.ADDRESS, 0), rowsPtr, colsPtr, nnzPtr));
            long nnz = nnzPtr.get(ValueLayout.JAVA_LONG, 0);

            MemorySegment d_rowPtr = malloc((long)(m + 1) * 4, tracker);
            MemorySegment d_colIdx = malloc((long)nnz * 4, tracker);
            MemorySegment d_val = malloc((long)nnz * elemSize, tracker);

            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_SET_POINTERS, spMatDescr.get(ValueLayout.ADDRESS, 0), d_rowPtr, d_colIdx, d_val));
            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_DENSE_TO_SPARSE, handle, dnMatDescr.get(ValueLayout.ADDRESS, 0), spMatDescr.get(ValueLayout.ADDRESS, 0), 1, d_buffer));

            int[] h_rowPtr = d_rowPtr.toArray(ValueLayout.JAVA_INT);
            int[] h_colIdx = d_colIdx.toArray(ValueLayout.JAVA_INT);
            float[] h_val = d_val.toArray(ValueLayout.JAVA_FLOAT);

            Ring<E> ring = (Ring<E>) a.getScalarRing();
            if (isComplex) {
                 Complex[] complexValues = new Complex[(int)nnz];
                 for (int i = 0; i < nnz; i++) complexValues[i] = Complex.of(h_val[i*2], h_val[i*2+1]);
                 return (Matrix<E>) new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(
                     new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(m, n, (E) Complex.ZERO, h_rowPtr, h_colIdx, (E[]) complexValues),
                     this, ring);
            } else {
                 org.episteme.core.mathematics.numbers.real.RealFloat[] realValues = new org.episteme.core.mathematics.numbers.real.RealFloat[(int)nnz];
                 for (int i = 0; i < nnz; i++) realValues[i] = org.episteme.core.mathematics.numbers.real.RealFloat.create(h_val[i]);
                 return (Matrix<E>) new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(
                     new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(m, n, (E) org.episteme.core.mathematics.numbers.real.RealFloat.ZERO, h_rowPtr, h_colIdx, (E[]) realValues),
                     this, ring);
            }
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    private Matrix<Complex> createComplexDenseMatrix(float[] data, int m, int n, Ring<E> ring) {
        Complex[][] complexData = new Complex[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int idx = (i * n + j) * 2;
                complexData[i][j] = Complex.of(data[idx], data[idx + 1]);
            }
        }
        Complex[] flatData = new Complex[m * n];
        for (int i = 0; i < m; i++) System.arraycopy(complexData[i], 0, flatData, i * n, n);
        return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>(
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<Complex>(m, n, flatData),
            (LinearAlgebraProvider<Complex>) (Object) new NativeCUDADenseLinearAlgebraFloatBackend(), (Ring<Complex>) (Object) ring);
    }

    private Matrix<E> createRealDenseMatrix(float[] data, int m, int n, Ring<E> ring) {
        org.episteme.core.mathematics.numbers.real.RealFloat[][] realData = new org.episteme.core.mathematics.numbers.real.RealFloat[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                realData[i][j] = org.episteme.core.mathematics.numbers.real.RealFloat.create(data[i * n + j]);
            }
        }
        org.episteme.core.mathematics.numbers.real.RealFloat[] flatData = new org.episteme.core.mathematics.numbers.real.RealFloat[m * n];
        for (int i = 0; i < m; i++) System.arraycopy(realData[i], 0, flatData, i * n, n);
        return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(m, n, (E[]) flatData),
            (LinearAlgebraProvider<E>) (Object) new NativeCUDADenseLinearAlgebraFloatBackend(), ring);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        try {
            return bicgstab(a, b, null, (E) RealFloat.of(1e-6f), 1000);
        } catch (Throwable t) {
            logger.warn("BiCGSTAB failed, falling back to CG: {}", t.getMessage());
            return conjugateGradient(a, b, null, (E) RealFloat.of(1e-6f), 1000);
        }
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = ensureSparse(a);
        int n = sa.rows(); int nnz = sa.getNnz();
        float tol = ((Number) tolerance).floatValue();
        boolean isComplex = isComplex(a);
        int elemSize = isComplex ? 8 : 4;

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_rowPtr = malloc((long)(n + 1) * 4, tracker);
            MemorySegment d_colIdx = malloc((long)nnz * 4, tracker);
            MemorySegment d_val = malloc((long)nnz * elemSize, tracker);
            MemorySegment d_b = malloc((long)n * elemSize, tracker);
            MemorySegment d_x = malloc((long)n * elemSize, tracker);
            MemorySegment d_r = malloc((long)n * elemSize, tracker);
            MemorySegment d_p = malloc((long)n * elemSize, tracker);
            MemorySegment d_Ap = malloc((long)n * elemSize, tracker);

            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_rowPtr, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_INT, sa.getRowPointers()), (long)(n + 1) * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_colIdx, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_INT, sa.getColIndices()), (long)nnz * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_val, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, isComplex ? toComplexFloatArray(sa) : toFloatArray(sa)), (long)nnz * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_b, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, isComplex ? toComplexFloatVec(b) : toFloatArray(b)), (long)n * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));

            if (x0 != null) {
                checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_x, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, isComplex ? toComplexFloatVec(x0) : toFloatArray(x0)), (long)n * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));
            } else {
                checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMSET, d_x, 0, (long)n * elemSize));
            }

            int dataType = isComplex ? 6 : 0; // 6=CUDA_C_32F, 0=CUDA_R_32F
            MemorySegment res = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, 2);

            spmv_internal(n, n, nnz, d_rowPtr, d_colIdx, d_val, d_x, d_Ap, arena, tracker, dataType);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_r, d_b, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_D));
            gpuSaxpy(n, d_Ap, d_r, isComplex ? Complex.of(-1, 0) : -1.0f, isComplex);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_p, d_r, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_D));

            double rsOld = gpuDot(n, d_r, d_r, res, isComplex).real();
            double tolSq = tol * tol;

            for (int i = 0; i < maxIterations; i++) {
                spmv_internal(n, n, nnz, d_rowPtr, d_colIdx, d_val, d_p, d_Ap, arena, tracker, dataType);
                Complex pAp = gpuDot(n, d_p, d_Ap, res, isComplex);
                Complex alpha = isComplex ? Complex.of(rsOld, 0).divide(pAp) : Complex.of(rsOld / pAp.real(), 0);
                gpuSaxpy(n, d_p, d_x, isComplex ? alpha : (float)alpha.real(), isComplex);
                gpuSaxpy(n, d_Ap, d_r, isComplex ? alpha.negate() : -(float)alpha.real(), isComplex);
                double rsNew = gpuDot(n, d_r, d_r, res, isComplex).real();
                if (rsNew < tolSq) break;
                double beta = rsNew / rsOld;
                gpuScale(n, d_p, isComplex ? Complex.of(beta, 0) : (float)beta, isComplex);
                gpuSaxpy(n, d_r, d_p, isComplex ? Complex.of(1, 0) : 1.0f, isComplex);
                rsOld = rsNew;
            }

            float[] h_x = new float[n * (isComplex ? 2 : 1)];
            MemorySegment hostX = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) h_x.length);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostX, d_x, (long) n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostX, ValueLayout.JAVA_FLOAT, 0, h_x, 0, h_x.length);
            return isComplex ? toVectorComplex(h_x, (Ring<E>) sa.getScalarRing()) : toVector(h_x, (Ring<E>) sa.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA Float CG failed", t); }
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = ensureSparse(a);
        int n = sa.rows(); int nnz = sa.getNnz();
        float tol = ((Number) tolerance).floatValue();
        boolean isComplex = isComplex(a);
        int elemSize = isComplex ? 8 : 4;

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_rowPtr = malloc((long)(n + 1) * 4, tracker);
            MemorySegment d_colIdx = malloc((long)nnz * 4, tracker);
            MemorySegment d_val = malloc((long)nnz * elemSize, tracker);
            MemorySegment d_b = malloc((long)n * elemSize, tracker);
            MemorySegment d_x = malloc((long)n * elemSize, tracker);
            MemorySegment d_r = malloc((long)n * elemSize, tracker);
            MemorySegment d_r0 = malloc((long)n * elemSize, tracker);
            MemorySegment d_p = malloc((long)n * elemSize, tracker);
            MemorySegment d_v = malloc((long)n * elemSize, tracker);
            MemorySegment d_s = malloc((long)n * elemSize, tracker);
            MemorySegment d_t = malloc((long)n * elemSize, tracker);

            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_rowPtr, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_INT, sa.getRowPointers()), (long)(n + 1) * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_colIdx, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_INT, sa.getColIndices()), (long)nnz * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_val, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, isComplex ? toComplexFloatArray(sa) : toFloatArray(sa)), (long)nnz * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_b, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, isComplex ? toComplexFloatVec(b) : toFloatArray(b)), (long)n * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));

            if (x0 != null) {
                checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_x, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, isComplex ? toComplexFloatVec(x0) : toFloatArray(x0)), (long)n * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));
            } else {
                checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMSET, d_x, 0, (long)n * elemSize));
            }

            int dataType = isComplex ? 6 : 0;
            MemorySegment res = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, 2);

            spmv_internal(n, n, nnz, d_rowPtr, d_colIdx, d_val, d_x, d_v, arena, tracker, dataType);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_r, d_b, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_D));
            gpuSaxpy(n, d_v, d_r, isComplex ? Complex.of(-1, 0) : -1.0f, isComplex);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_r0, d_r, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_D));

            Complex rho = Complex.of(1, 0);
            Complex alpha = Complex.of(1, 0);
            Complex omega = Complex.of(1, 0);

            for (int i = 0; i < maxIterations; i++) {
                Complex rhoNext = gpuDot(n, d_r0, d_r, res, isComplex);
                Complex beta = rhoNext.divide(rho).multiply(alpha.divide(omega));
                rho = rhoNext;

                gpuSaxpy(n, d_v, d_p, isComplex ? omega.negate() : -(float)omega.real(), isComplex);
                gpuScale(n, d_p, isComplex ? beta : (float)beta.real(), isComplex);
                gpuSaxpy(n, d_r, d_p, isComplex ? Complex.of(1, 0) : 1.0f, isComplex);

                spmv_internal(n, n, nnz, d_rowPtr, d_colIdx, d_val, d_p, d_v, arena, tracker, dataType);
                Complex dotV = gpuDot(n, d_r0, d_v, res, isComplex);
                alpha = rho.divide(dotV);

                checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_s, d_r, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_D));
                gpuSaxpy(n, d_v, d_s, isComplex ? alpha.negate() : -(float)alpha.real(), isComplex);

                spmv_internal(n, n, nnz, d_rowPtr, d_colIdx, d_val, d_s, d_t, arena, tracker, dataType);
                Complex tDotS = gpuDot(n, d_t, d_s, res, isComplex);
                Complex tDotT = gpuDot(n, d_t, d_t, res, isComplex);
                omega = tDotS.divide(tDotT);

                gpuSaxpy(n, d_p, d_x, isComplex ? alpha : (float)alpha.real(), isComplex);
                gpuSaxpy(n, d_s, d_x, isComplex ? omega : (float)omega.real(), isComplex);

                checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_r, d_s, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_D));
                gpuSaxpy(n, d_t, d_r, isComplex ? omega.negate() : -(float)omega.real(), isComplex);

                if (gpuDot(n, d_r, d_r, res, isComplex).real() < tol * tol) break;
            }

            float[] h_x = new float[n * (isComplex ? 2 : 1)];
            MemorySegment hostX = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, (long) h_x.length);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostX, d_x, (long) n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostX, ValueLayout.JAVA_FLOAT, 0, h_x, 0, h_x.length);
            return isComplex ? toVectorComplex(h_x, (Ring<E>) sa.getScalarRing()) : toVector(h_x, (Ring<E>) sa.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA BiCGSTAB failed", t); }
    }

    private void gpuSaxpy(int n, MemorySegment d_x, MemorySegment d_y, Object alpha, boolean isComplex) throws Throwable {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment aSeg = isComplex ? NativeSafe.allocateFrom(temp, ValueLayout.JAVA_FLOAT, (float)((Complex)alpha).real(), (float)((Complex)alpha).imaginary()) : NativeSafe.allocateFrom(temp, ValueLayout.JAVA_FLOAT, ((Number)alpha).floatValue());
            NativeSafe.invoke(isComplex ? CUDAManager.CUBLAS_CAXPY : CUDAManager.CUBLAS_SAXPY, CUDAManager.getCublasHandle(), n, aSeg, d_x, 1, d_y, 1);
        }
    }

    private void gpuScale(int n, MemorySegment d_x, Object s, boolean isComplex) throws Throwable {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment sSeg = isComplex ? NativeSafe.allocateFrom(temp, ValueLayout.JAVA_FLOAT, (float)((Complex)s).real(), (float)((Complex)s).imaginary()) : NativeSafe.allocateFrom(temp, ValueLayout.JAVA_FLOAT, ((Number)s).floatValue());
            NativeSafe.invoke(isComplex ? CUDAManager.CUBLAS_CSCAL : CUDAManager.CUBLAS_SSCAL, CUDAManager.getCublasHandle(), n, sSeg, d_x, 1);
        }
    }

    private Complex gpuDot(int n, MemorySegment d_x, MemorySegment d_y, MemorySegment d_res, boolean isComplex) throws Throwable {
        NativeSafe.invoke(isComplex ? CUDAManager.CUBLAS_CDOT : CUDAManager.CUBLAS_SDOT, CUDAManager.getCublasHandle(), n, d_x, 1, d_y, 1, d_res);
        if (isComplex) return Complex.of(d_res.getAtIndex(ValueLayout.JAVA_FLOAT, 0), d_res.getAtIndex(ValueLayout.JAVA_FLOAT, 1));
        return Complex.of(d_res.get(ValueLayout.JAVA_FLOAT, 0), 0);
    }

    @Override public Vector<E> add(Vector<E> a, Vector<E> b) { return SparseLinearAlgebraProvider.super.add(a, b); }
    @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { return SparseLinearAlgebraProvider.super.subtract(a, b); }
    @Override public E dot(Vector<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = a.dimension();
        boolean isComplex = isComplex(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_a = malloc((long)n * (isComplex ? 8 : 4), tracker);
            MemorySegment d_b = malloc((long)n * (isComplex ? 8 : 4), tracker);
            MemorySegment d_res = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT, 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_a, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, isComplex ? toComplexFloatVec(a) : toFloatArray(a)), (long)n * (isComplex ? 8 : 4), CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_b, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, isComplex ? toComplexFloatVec(b) : toFloatArray(b)), (long)n * (isComplex ? 8 : 4), CUDAManager.CUDA_MEMCPY_H_TO_D));
            return (E) castToRing(gpuDot(n, d_a, d_b, d_res, isComplex), (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public E norm(Vector<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = a.dimension();
        boolean isComplex = isComplex(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_a = malloc((long)n * (isComplex ? 8 : 4), tracker);
            MemorySegment d_res = NativeSafe.allocate(arena, ValueLayout.JAVA_FLOAT);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_a, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, isComplex ? toComplexFloatVec(a) : toFloatArray(a)), (long)n * (isComplex ? 8 : 4), CUDAManager.CUDA_MEMCPY_H_TO_D));
            NativeSafe.invoke(isComplex ? CUDAManager.CUBLAS_SCNRM2 : CUDAManager.CUBLAS_SNRM2, CUDAManager.getCublasHandle(), n, d_a, 1, d_res);
            return (E) (Object) RealFloat.of(d_res.get(ValueLayout.JAVA_FLOAT, 0));
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override
    public E trace(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = ensureSparse(a);
        int n = Math.min(sa.rows(), sa.cols());
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        Object[] values = sa.getValues();
        
        if (isComplex(sa)) {
            Complex sum = Complex.ZERO;
            for (int i = 0; i < n; i++) {
                for (int j = rowPtr[i]; j < rowPtr[i + 1]; j++) {
                    if (colIdx[j] == i) {
                        sum = sum.add(getComplex(values[j]));
                        break;
                    }
                }
            }
            return (E) (Object) sum;
        } else {
            float sum = 0.0f;
            for (int i = 0; i < n; i++) {
                for (int j = rowPtr[i]; j < rowPtr[i + 1]; j++) {
                    if (colIdx[j] == i) {
                        sum += ((Number) values[j]).floatValue();
                        break;
                    }
                }
            }
            return (E) (Object) RealFloat.of(sum);
        }
    }

    @Override
    public Matrix<E> conjugateTranspose(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = ensureSparse(a);
        int m = sa.rows(); int n = sa.cols(); int nnz = sa.getNnz();
        boolean isComplex = isComplex(sa);
        int elemSize = isComplex ? 8 : 4;
        int dataType = isComplex ? 6 : 0;

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_rowPtr = malloc((long)(m + 1) * 4, tracker);
            MemorySegment d_colIdx = malloc((long)nnz * 4, tracker);
            MemorySegment d_val = malloc((long)nnz * elemSize, tracker);
            
            MemorySegment d_cscRowPtr = malloc((long)(n + 1) * 4, tracker);
            MemorySegment d_cscColIdx = malloc((long)nnz * 4, tracker);
            MemorySegment d_cscVal = malloc((long)nnz * elemSize, tracker);

            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_rowPtr, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_INT, sa.getRowPointers()), (long)(m + 1) * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_colIdx, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_INT, sa.getColIndices()), (long)nnz * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_val, NativeSafe.allocateFrom( arena, ValueLayout.JAVA_FLOAT, isComplex ? toComplexFloatArray(sa) : toFloatArray(sa)), (long)nnz * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));

            MemorySegment handle = CUDAManager.getCusparseHandle();
            MemorySegment bufferSizePtr = NativeSafe.allocate(arena, ValueLayout.JAVA_LONG);
            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CSR2CSC_BUFFER_SIZE, handle, m, n, nnz, d_rowPtr, d_colIdx, d_val, d_cscRowPtr, d_cscColIdx, d_cscVal, dataType, 1, bufferSizePtr));
            long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
            MemorySegment d_buffer = malloc(bufferSize, tracker);

            checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CSR2CSC, handle, m, n, nnz, d_val, d_rowPtr, d_colIdx, d_cscVal, d_cscColIdx, d_cscRowPtr, dataType, 1, 0, d_buffer));

            int[] h_cscRowPtr = new int[n + 1];
            int[] h_cscColIdx = new int[nnz];
            float[] h_cscVal = new float[nnz * (isComplex ? 2 : 1)];

            clEnqueueReadBuffer_dummy_float(d_cscRowPtr, h_cscRowPtr, (long)(n + 1) * 4, arena);
            clEnqueueReadBuffer_dummy_float(d_cscColIdx, h_cscColIdx, (long)nnz * 4, arena);
            clEnqueueReadBuffer_dummy_float(d_cscVal, h_cscVal, (long)nnz * elemSize, arena);

            Ring<E> ring = (Ring<E>) sa.getScalarRing();
            E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), nnz);
            for (int i = 0; i < nnz; i++) {
                if (isComplex) {
                    Complex c = Complex.of(RealFloat.create(h_cscVal[i * 2]), RealFloat.create(h_cscVal[i * 2 + 1]));
                    values[i] = (E) (Object) c.conjugate();
                } else {
                    values[i] = (E) (Object) RealFloat.create(h_cscVal[i]);
                }
            }
            
            org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
                new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(n, m, ring.zero(), h_cscRowPtr, h_cscColIdx, values);
            return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, ring);
        } catch (Throwable t) { throw new RuntimeException("CUDA sparse conjugate transpose failed", t); }
    }

    private void clEnqueueReadBuffer_dummy_float(MemorySegment d_ptr, Object host_ptr, long size, Arena arena) throws Throwable {
        MemorySegment hostSeg = NativeSafe.allocate(arena, size);
        checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostSeg, d_ptr, size, CUDAManager.CUDA_MEMCPY_D_TO_H));
        if (host_ptr instanceof int[] ia) MemorySegment.copy(hostSeg, ValueLayout.JAVA_INT, 0, ia, 0, ia.length);
        else if (host_ptr instanceof float[] fa) MemorySegment.copy(hostSeg, ValueLayout.JAVA_FLOAT, 0, fa, 0, fa.length);
    }

    private Object castToRing(Complex c, Ring<E> ring) {
        if (ring.zero() instanceof Complex) return c;
        return RealFloat.of((float)c.real());
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
            MemorySegment p = NativeSafe.allocate(temp, ValueLayout.ADDRESS);
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
        throw new UnsupportedOperationException("Matrix multiply for DoubleBuffer not supported in float backend");
    }

    private float getRealValue(Object val) {
        if (val instanceof Number n) return n.floatValue();
        if (val instanceof Real r) return r.floatValue();
        if (val instanceof Complex c) return (float) c.real();
        return 0.0f;
    }

    private Complex getComplex(Object val) {
        if (val instanceof Complex c) return c;
        if (val instanceof Number n) return Complex.of(RealFloat.of(n.floatValue()));
        if (val instanceof Real r) return Complex.of(RealFloat.of(r.floatValue()));
        throw new IllegalArgumentException("Cannot convert to complex: " + val.getClass());
    }

    private boolean isComplex(Vector<E> v) { return v.getScalarRing().zero() instanceof Complex; }
}



