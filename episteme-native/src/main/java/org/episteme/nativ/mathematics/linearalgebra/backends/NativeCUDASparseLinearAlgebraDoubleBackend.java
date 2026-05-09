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
import org.episteme.core.mathematics.numbers.real.RealDouble;
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
 * Native CUDA Sparse Linear Algebra Backend for Double precision.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, GPUBackend.class})
public class NativeCUDASparseLinearAlgebraDoubleBackend<E extends FieldElement<E>> implements SparseLinearAlgebraProvider<E>, NativeBackend, GPUBackend {
    private static final Logger logger = LoggerFactory.getLogger(NativeCUDASparseLinearAlgebraDoubleBackend.class);

    @Override public boolean isLoaded() { return isAvailable(); }
    @Override public String getNativeLibraryName() { return "cuda"; }
    
    private static final int CUDA_R_64F = 1; // Double precision in CUDA
    private static final int CUSPARSE_INDEX_32BIT = 2; // CUSPARSE_INDEX_32I
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
        return zero instanceof RealDouble || zero instanceof Complex || (zero instanceof Real r && !r.isFast());
    }

    @Override public String getId() { return "cuda-sparse-double"; }
    @Override public String getName() { return "Native CUDA Sparse Linear Algebra Double Backend"; }
    @Override public int getPriority() { return 100; }
    @Override public void shutdown() {}

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> x) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        if (isComplex(a)) return multiplyComplex(a, x);
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = ensureSparse(a);
        int m = sa.rows(); int k = sa.cols(); int nnz = sa.getNnz();

        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_rowPtr = malloc((long)(m + 1) * 4, tracker);
            MemorySegment d_colIdx = malloc((long)nnz * 4, tracker);
            MemorySegment d_val = malloc((long)nnz * 8, tracker);
            MemorySegment d_x = malloc((long)k * 8, tracker);
            MemorySegment d_y = malloc((long)m * 8, tracker);

            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_rowPtr, arena.allocateFrom(ValueLayout.JAVA_INT, sa.getRowPointers()), (long)(m + 1) * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_colIdx, arena.allocateFrom(ValueLayout.JAVA_INT, sa.getColIndices()), (long)nnz * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_val, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(sa)), (long)nnz * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_x, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toDoubleArray(x)), (long)k * 8, CUDAManager.CUDA_MEMCPY_H_TO_D));

            spmv_internal(m, k, nnz, d_rowPtr, d_colIdx, d_val, d_x, d_y, arena, tracker);

            double[] h_y = new double[m];
            MemorySegment hostY = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostY, d_y, (long) m * 8, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostY, ValueLayout.JAVA_DOUBLE, 0, h_y, 0, m);
            return toVector(h_y, (Ring<E>) sa.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA double SpMV failed", t); }
    }

    private Vector<E> multiplyComplex(Matrix<E> a, Vector<E> x) {
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = ensureSparse(a);
        int m = sa.rows(); int k = sa.cols(); int nnz = sa.getNnz();
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_rowPtr = malloc((long)(m + 1) * 4, tracker);
            MemorySegment d_colIdx = malloc((long)nnz * 4, tracker);
            MemorySegment d_val = malloc((long)nnz * 16, tracker);
            MemorySegment d_x = malloc((long)k * 16, tracker);
            MemorySegment d_y = malloc((long)m * 16, tracker);

            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_rowPtr, arena.allocateFrom(ValueLayout.JAVA_INT, sa.getRowPointers()), (long)(m + 1) * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_colIdx, arena.allocateFrom(ValueLayout.JAVA_INT, sa.getColIndices()), (long)nnz * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_val, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleArray(sa)), (long)nnz * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_x, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, toComplexDoubleVec(x)), (long)k * 16, CUDAManager.CUDA_MEMCPY_H_TO_D));

            spmv_internal(m, k, nnz, d_rowPtr, d_colIdx, d_val, d_x, d_y, arena, tracker, 5); // 5 = CUDA_C_64F

            double[] h_y = new double[m * 2];
            MemorySegment hostY = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) m * 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostY, d_y, (long) m * 16, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostY, ValueLayout.JAVA_DOUBLE, 0, h_y, 0, m * 2);
            return toVectorComplex(h_y, (Ring<E>) sa.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA complex SpMV failed", t); }
    }

    private void spmv_internal(int m, int k, int nnz, MemorySegment d_rowPtr, MemorySegment d_colIdx, MemorySegment d_val, MemorySegment d_x, MemorySegment d_y, Arena arena, ResourceTracker tracker) throws Throwable {
        spmv_internal(m, k, nnz, d_rowPtr, d_colIdx, d_val, d_x, d_y, arena, tracker, 1); // Default to double (CUDA_R_64F = 1)
    }

    private void spmv_internal(int m, int k, int nnz, MemorySegment d_rowPtr, MemorySegment d_colIdx, MemorySegment d_val, MemorySegment d_x, MemorySegment d_y, Arena arena, ResourceTracker tracker, int dataType) throws Throwable {
        MemorySegment matAPtr = arena.allocate(ValueLayout.ADDRESS);
        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_CSR, matAPtr, (long)m, (long)k, (long)nnz, d_rowPtr, d_colIdx, d_val, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_32BIT, CUSPARSE_INDEX_BASE_ZERO, dataType));
        MemorySegment matA = matAPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(matA, s -> { try { NativeSafe.invoke(CUDAManager.CUSPARSE_DESTROY_SP_MAT, s); } catch (Throwable t) {} });

        MemorySegment vecXPtr = arena.allocate(ValueLayout.ADDRESS);
        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_DN_VEC, vecXPtr, (long)k, d_x, dataType));
        MemorySegment vecX = vecXPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(vecX, s -> { try { NativeSafe.invoke(CUDAManager.CUSPARSE_DESTROY_DN_VEC, s); } catch (Throwable t) {} });

        MemorySegment vecYPtr = arena.allocate(ValueLayout.ADDRESS);
        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_CREATE_DN_VEC, vecYPtr, (long)m, d_y, dataType));
        MemorySegment vecY = vecYPtr.get(ValueLayout.ADDRESS, 0);
        tracker.track(vecY, s -> { try { NativeSafe.invoke(CUDAManager.CUSPARSE_DESTROY_DN_VEC, s); } catch (Throwable t) {} });

        MemorySegment alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0);
        MemorySegment beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0);
        if (dataType == 5) {
            alpha = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 1.0, 0.0);
            beta = arena.allocateFrom(ValueLayout.JAVA_DOUBLE, 0.0, 0.0);
        }
        
        MemorySegment bufferSizePtr = arena.allocate(ValueLayout.JAVA_LONG);
        MemorySegment handle = CUDAManager.getCusparseHandle();
        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_SPMV_BUFFER_SIZE, handle, 0, alpha, matA, vecX, beta, vecY, dataType, CUSPARSE_SPMM_ALG_DEFAULT, bufferSizePtr));
        long bufferSize = bufferSizePtr.get(ValueLayout.JAVA_LONG, 0);
        MemorySegment d_buffer = malloc(bufferSize, tracker);

        checkCusparse((int) NativeSafe.invoke(CUDAManager.CUSPARSE_SPMV, handle, 0, alpha, matA, vecX, beta, vecY, dataType, CUSPARSE_SPMM_ALG_DEFAULT, d_buffer));
    }

    private MemorySegment malloc(long size, ResourceTracker tracker) {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment p = temp.allocate(ValueLayout.ADDRESS);
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

    private double[] toDoubleArray(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> m) {
        Object[] vals = m.getValues();
        double[] data = new double[vals.length];
        for (int i = 0; i < vals.length; i++) data[i] = ((Number) vals[i]).doubleValue();
        return data;
    }

    private double[] toDoubleArray(Vector<E> v) {
        int dim = v.dimension();
        double[] data = new double[dim];
        for (int i = 0; i < dim; i++) data[i] = ((Number) v.get(i)).doubleValue();
        return data;
    }

    private Vector<E> toVector(double[] data, Ring<E> ring) {
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), data.length);
        for (int i = 0; i < data.length; i++) values[i] = (E) RealDouble.of(data[i]);
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(values), (LinearAlgebraProvider<E>) this, ring);
    }

    private org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> ensureSparse(Matrix<E> A) {
        if (A instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) return (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) A;
        int rows = A.rows(); int cols = A.cols(); Ring<E> ring = A.getScalarRing();
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, ring.zero());
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) {
            E val = A.get(i, j);
            if (val != null && !ring.zero().equals(val)) storage.set(i, j, val);
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, (LinearAlgebraProvider<E>) this, ring);
    }

    private boolean isComplex(Matrix<E> a) {
        return a.getScalarRing().zero() instanceof Complex;
    }

    private double[] toComplexDoubleArray(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> m) {
        Object[] vals = m.getValues();
        double[] data = new double[vals.length * 2];
        for (int i = 0; i < vals.length; i++) {
            Complex c = (Complex) vals[i];
            data[i * 2] = ((Number) c.real()).doubleValue();
            data[i * 2 + 1] = ((Number) c.imaginary()).doubleValue();
        }
        return data;
    }

    private double[] toComplexDoubleVec(Vector<E> v) {
        int dim = v.dimension();
        double[] data = new double[dim * 2];
        for (int i = 0; i < dim; i++) {
            Complex c = (Complex) v.get(i);
            data[i * 2] = ((Number) c.real()).doubleValue();
            data[i * 2 + 1] = ((Number) c.imaginary()).doubleValue();
        }
        return data;
    }

    private Vector<E> toVectorComplex(double[] data, Ring<E> ring) {
        int dim = data.length / 2;
        E[] values = (E[]) java.lang.reflect.Array.newInstance(ring.zero().getClass(), dim);
        for (int i = 0; i < dim; i++) {
            values[i] = (E) Complex.of(RealDouble.of(data[i * 2]), RealDouble.of(data[i * 2 + 1]));
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(values), (LinearAlgebraProvider<E>) this, ring);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        try {
            // Default to BiCGSTAB for general sparse matrices
            return bicgstab(a, b, null, (E) RealDouble.of(1e-10), 1000);
        } catch (Throwable t) {
            logger.warn("BiCGSTAB failed, falling back to CG: {}", t.getMessage());
            return conjugateGradient(a, b, null, (E) RealDouble.of(1e-10), 1000);
        }
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = ensureSparse(a);
        int n = sa.rows(); int nnz = sa.getNnz();
        double tol = ((Number) tolerance).doubleValue();
        boolean isComplex = isComplex(a);
        int elemSize = isComplex ? 16 : 8;

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

            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_rowPtr, arena.allocateFrom(ValueLayout.JAVA_INT, sa.getRowPointers()), (long)(n + 1) * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_colIdx, arena.allocateFrom(ValueLayout.JAVA_INT, sa.getColIndices()), (long)nnz * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_val, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, isComplex ? toComplexDoubleArray(sa) : toDoubleArray(sa)), (long)nnz * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_b, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, isComplex ? toComplexDoubleVec(b) : toDoubleArray(b)), (long)n * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));

            if (x0 != null) {
                checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_x, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, isComplex ? toComplexDoubleVec(x0) : toDoubleArray(x0)), (long)n * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));
            } else {
                checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMSET, d_x, 0, (long)n * elemSize));
            }

            int dataType = isComplex ? 5 : 1; // 5=CUDA_C_64F, 1=CUDA_R_64F
            MemorySegment handle = CUDAManager.getCublasHandle();
            MemorySegment res = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);

            // r = b - Ax
            spmv_internal(n, n, nnz, d_rowPtr, d_colIdx, d_val, d_x, d_Ap, arena, tracker, dataType);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_r, d_b, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_D));
            gpuSaxpy(n, d_Ap, d_r, isComplex ? Complex.of(-1, 0) : -1.0, isComplex);
            
            // p = r
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_p, d_r, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_D));

            double rsOld = gpuDot(n, d_r, d_r, res, isComplex).real();
            double tolSq = tol * tol;

            for (int i = 0; i < maxIterations; i++) {
                spmv_internal(n, n, nnz, d_rowPtr, d_colIdx, d_val, d_p, d_Ap, arena, tracker, dataType);
                Complex pAp = gpuDot(n, d_p, d_Ap, res, isComplex);
                
                Complex alpha = isComplex ? Complex.of(rsOld, 0).divide(pAp) : Complex.of(rsOld / pAp.real(), 0);
                
                gpuSaxpy(n, d_p, d_x, isComplex ? alpha : alpha.real(), isComplex);
                gpuSaxpy(n, d_Ap, d_r, isComplex ? alpha.negate() : -alpha.real(), isComplex);
                
                double rsNew = gpuDot(n, d_r, d_r, res, isComplex).real();
                if (rsNew < tolSq) break;
                
                double beta = rsNew / rsOld;
                gpuScale(n, d_p, isComplex ? Complex.of(beta, 0) : beta, isComplex);
                gpuSaxpy(n, d_r, d_p, isComplex ? Complex.of(1, 0) : 1.0, isComplex);
                rsOld = rsNew;
            }

            double[] h_x = new double[n * (isComplex ? 2 : 1)];
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, h_x), d_x, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment hostX = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) h_x.length);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostX, d_x, (long) n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostX, ValueLayout.JAVA_DOUBLE, 0, h_x, 0, h_x.length);
            return isComplex ? toVectorComplex(h_x, (Ring<E>) sa.getScalarRing()) : toVector(h_x, (Ring<E>) sa.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA CG failed", t); }
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = ensureSparse(a);
        int n = sa.rows(); int nnz = sa.getNnz();
        double tol = ((Number) tolerance).doubleValue();
        boolean isComplex = isComplex(a);
        int elemSize = isComplex ? 16 : 8;

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

            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_rowPtr, arena.allocateFrom(ValueLayout.JAVA_INT, sa.getRowPointers()), (long)(n + 1) * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_colIdx, arena.allocateFrom(ValueLayout.JAVA_INT, sa.getColIndices()), (long)nnz * 4, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_val, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, isComplex ? toComplexDoubleArray(sa) : toDoubleArray(sa)), (long)nnz * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_b, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, isComplex ? toComplexDoubleVec(b) : toDoubleArray(b)), (long)n * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));

            if (x0 != null) {
                checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_x, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, isComplex ? toComplexDoubleVec(x0) : toDoubleArray(x0)), (long)n * elemSize, CUDAManager.CUDA_MEMCPY_H_TO_D));
            } else {
                checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMSET, d_x, 0, (long)n * elemSize));
            }

            int dataType = isComplex ? 5 : 1;
            MemorySegment res = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);

            // r = b - Ax
            spmv_internal(n, n, nnz, d_rowPtr, d_colIdx, d_val, d_x, d_v, arena, tracker, dataType);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_r, d_b, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_D));
            gpuSaxpy(n, d_v, d_r, isComplex ? Complex.of(-1, 0) : -1.0, isComplex);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_r0, d_r, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_D));

            Complex rho = Complex.of(1, 0);
            Complex alpha = Complex.of(1, 0);
            Complex omega = Complex.of(1, 0);

            for (int i = 0; i < maxIterations; i++) {
                Complex rhoNext = gpuDot(n, d_r0, d_r, res, isComplex);
                Complex beta = rhoNext.divide(rho).multiply(alpha.divide(omega));
                rho = rhoNext;

                gpuSaxpy(n, d_v, d_p, isComplex ? omega.negate() : -omega.real(), isComplex);
                gpuScale(n, d_p, isComplex ? beta : beta.real(), isComplex);
                gpuSaxpy(n, d_r, d_p, isComplex ? Complex.of(1, 0) : 1.0, isComplex);

                spmv_internal(n, n, nnz, d_rowPtr, d_colIdx, d_val, d_p, d_v, arena, tracker, dataType);
                Complex dotV = gpuDot(n, d_r0, d_v, res, isComplex);
                alpha = rho.divide(dotV);

                checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_s, d_r, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_D));
                gpuSaxpy(n, d_v, d_s, isComplex ? alpha.negate() : -alpha.real(), isComplex);

                spmv_internal(n, n, nnz, d_rowPtr, d_colIdx, d_val, d_s, d_t, arena, tracker, dataType);
                Complex tDotS = gpuDot(n, d_t, d_s, res, isComplex);
                Complex tDotT = gpuDot(n, d_t, d_t, res, isComplex);
                omega = tDotS.divide(tDotT);

                gpuSaxpy(n, d_p, d_x, isComplex ? alpha : alpha.real(), isComplex);
                gpuSaxpy(n, d_s, d_x, isComplex ? omega : omega.real(), isComplex);

                checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_r, d_s, (long)n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_D));
                gpuSaxpy(n, d_t, d_r, isComplex ? omega.negate() : -omega.real(), isComplex);

                if (gpuDot(n, d_r, d_r, res, isComplex).real() < tol * tol) break;
            }

            double[] h_x = new double[n * (isComplex ? 2 : 1)];
            MemorySegment hostX = arena.allocate(ValueLayout.JAVA_DOUBLE, (long) h_x.length);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, hostX, d_x, (long) n * elemSize, CUDAManager.CUDA_MEMCPY_D_TO_H));
            MemorySegment.copy(hostX, ValueLayout.JAVA_DOUBLE, 0, h_x, 0, h_x.length);
            return isComplex ? toVectorComplex(h_x, (Ring<E>) sa.getScalarRing()) : toVector(h_x, (Ring<E>) sa.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException("CUDA BiCGSTAB failed", t); }
    }

    private void gpuSaxpy(int n, MemorySegment d_x, MemorySegment d_y, Object alpha, boolean isComplex) throws Throwable {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment aSeg = isComplex ? temp.allocateFrom(ValueLayout.JAVA_DOUBLE, ((Complex)alpha).real(), ((Complex)alpha).imaginary()) : temp.allocateFrom(ValueLayout.JAVA_DOUBLE, ((Number)alpha).doubleValue());
            NativeSafe.invoke(isComplex ? CUDAManager.CUBLAS_ZAXPY : CUDAManager.CUBLAS_DAXPY, CUDAManager.getCublasHandle(), n, aSeg, d_x, 1, d_y, 1);
        }
    }

    private void gpuScale(int n, MemorySegment d_x, Object s, boolean isComplex) throws Throwable {
        try (Arena temp = Arena.ofConfined()) {
            MemorySegment sSeg = isComplex ? temp.allocateFrom(ValueLayout.JAVA_DOUBLE, ((Complex)s).real(), ((Complex)s).imaginary()) : temp.allocateFrom(ValueLayout.JAVA_DOUBLE, ((Number)s).doubleValue());
            NativeSafe.invoke(isComplex ? CUDAManager.CUBLAS_ZSCAL : CUDAManager.CUBLAS_DSCAL, CUDAManager.getCublasHandle(), n, sSeg, d_x, 1);
        }
    }

    private Complex gpuDot(int n, MemorySegment d_x, MemorySegment d_y, MemorySegment d_res, boolean isComplex) throws Throwable {
        NativeSafe.invoke(isComplex ? CUDAManager.CUBLAS_ZDOTU : CUDAManager.CUBLAS_DDOT, CUDAManager.getCublasHandle(), n, d_x, 1, d_y, 1, d_res);
        if (isComplex) return Complex.of(d_res.getAtIndex(ValueLayout.JAVA_DOUBLE, 0), d_res.getAtIndex(ValueLayout.JAVA_DOUBLE, 1));
        return Complex.of(d_res.get(ValueLayout.JAVA_DOUBLE, 0), 0);
    }

    @Override public Vector<E> add(Vector<E> a, Vector<E> b) { return SparseLinearAlgebraProvider.super.add(a, b); }
    @Override public Vector<E> subtract(Vector<E> a, Vector<E> b) { return SparseLinearAlgebraProvider.super.subtract(a, b); }
    @Override public E dot(Vector<E> a, Vector<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = a.dimension();
        boolean isComplex = isComplex(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_a = malloc((long)n * (isComplex ? 16 : 8), tracker);
            MemorySegment d_b = malloc((long)n * (isComplex ? 16 : 8), tracker);
            MemorySegment d_res = arena.allocate(ValueLayout.JAVA_DOUBLE, 2);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_a, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, isComplex ? toComplexDoubleVec(a) : toDoubleArray(a)), (long)n * (isComplex ? 16 : 8), CUDAManager.CUDA_MEMCPY_H_TO_D));
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_b, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, isComplex ? toComplexDoubleVec(b) : toDoubleArray(b)), (long)n * (isComplex ? 16 : 8), CUDAManager.CUDA_MEMCPY_H_TO_D));
            return (E) castToRing(gpuDot(n, d_a, d_b, d_res, isComplex), (Ring<E>) a.getScalarRing());
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    @Override public E norm(Vector<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " not available");
        int n = a.dimension();
        boolean isComplex = isComplex(a);
        try (ResourceTracker tracker = new ResourceTracker()) {
            Arena arena = tracker.track(Arena.ofConfined(), Arena::close);
            MemorySegment d_a = malloc((long)n * (isComplex ? 16 : 8), tracker);
            MemorySegment d_res = arena.allocate(ValueLayout.JAVA_DOUBLE);
            checkCuda((int) NativeSafe.invoke(CUDAManager.CUDA_MEMCPY, d_a, arena.allocateFrom(ValueLayout.JAVA_DOUBLE, isComplex ? toComplexDoubleVec(a) : toDoubleArray(a)), (long)n * (isComplex ? 16 : 8), CUDAManager.CUDA_MEMCPY_H_TO_D));
            NativeSafe.invoke(isComplex ? CUDAManager.CUBLAS_DZNRM2 : CUDAManager.CUBLAS_DNRM2, CUDAManager.getCublasHandle(), n, d_a, 1, d_res);
            return (E) RealDouble.of(d_res.get(ValueLayout.JAVA_DOUBLE, 0));
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    private Object castToRing(Complex c, Ring<E> ring) {
        if (ring.zero() instanceof Complex) return c;
        return RealDouble.of(c.real());
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
        throw new UnsupportedOperationException("Matrix multiply for DoubleBuffer not implemented in sparse backend");
    }

    private boolean isComplex(Matrix<E> m) { return m.getScalarRing().zero() instanceof Complex; }
    private boolean isComplex(Vector<E> v) { return v.getScalarRing().zero() instanceof Complex; }
}
