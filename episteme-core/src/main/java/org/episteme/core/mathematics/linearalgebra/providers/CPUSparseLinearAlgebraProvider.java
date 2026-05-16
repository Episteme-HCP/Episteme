/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.core.mathematics.linearalgebra.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.mathematics.linearalgebra.backends.LinearAlgebraBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.sparse.*;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import com.google.auto.service.AutoService;

/**
 * Linear Algebra Provider for Sparse Matrices (CPU).
 * <p>
 * Optimized for SparseMatrix implementations.
 * Uses CSR-based algorithms that only process non-zero elements.
 * </p>
 */
@AutoService({LinearAlgebraBackend.class, LinearAlgebraProvider.class, Backend.class})
public class CPUSparseLinearAlgebraProvider<E> implements LinearAlgebraBackend<E>, SparseLinearAlgebraProvider<E>, CPUBackend {

    protected final Ring<E> ring;

    public CPUSparseLinearAlgebraProvider(Ring<E> ring) {
        this.ring = ring;
    }

    public CPUSparseLinearAlgebraProvider() {
        this(null);
    }


    @Override
    public LUResult<E> lu(Matrix<E> a) {
        @SuppressWarnings("unchecked")
        LUResult<E> result = GenericLU.decompose(a, (Field<E>) (Object) a.getScalarRing(), this);
        return result;
    }

    @Override
    public QRResult<E> qr(Matrix<E> a) {
        @SuppressWarnings("unchecked")
        QRResult<E> result = GenericQR.decompose(a, (Field<E>) (Object) a.getScalarRing(), this);
        return result;
    }

    public CholeskyResult<E> cholesky(Matrix<E> a) {
        @SuppressWarnings("unchecked")
        CholeskyResult<E> result = GenericCholesky.decompose(a, (Field<E>) (Object) a.getScalarRing(), this);
        return result;
    }

    public SVDResult<E> svd(Matrix<E> a) {
        @SuppressWarnings("unchecked")
        SVDResult<E> result = GenericSVD.decompose(a, (Field<E>) (Object) a.getScalarRing(), this);
        return result;
    }

    public EigenResult<E> eigen(Matrix<E> a) {
        @SuppressWarnings("unchecked")
        EigenResult<E> result = GenericEigen.decompose(a, (Field<E>) (Object) a.getScalarRing(), this);
        return result;
    }

    @Override
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        @SuppressWarnings("unchecked")
        Vector<E> result = GenericLU.solve(lu, b, (Field<E>) (Object) b.getScalarRing(), this);
        return result;
    }

    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        @SuppressWarnings("unchecked")
        Vector<E> result = GenericQR.solve(qr, b, (Field<E>) (Object) b.getScalarRing(), this);
        return result;
    }

    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        @SuppressWarnings("unchecked")
        Vector<E> result = GenericCholesky.solve(cholesky, b, (Field<E>) (Object) b.getScalarRing(), this);
        return result;
    }

    @Override
    public String getName() {
        return "Episteme CPU (Sparse)";
    }

    private SparseMatrix<E> ensureSparse(Matrix<E> a, Ring<E> r) {
        if (a instanceof SparseMatrix) return (SparseMatrix<E>) a;
        return toSparse(a, r);
    }


    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void shutdown() {}

    @Override
    public double score(OperationContext context) {
        if (!isAvailable()) return -1.0;
        if (context.hasHint(OperationContext.Hint.DENSE)) return -1.0;
        double base = getPriority();
        if (context.hasHint(OperationContext.Hint.SPARSE)) base += 20.0;
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            base += 1000.0;
        }
        return base;
    }

    @Override
    public java.util.Map<String, String> getMetadata() {
        return java.util.Map.of("capabilities", "Transpose,Add,Subtract,Scale,Multiply,Inverse,Determinant,Solve,Dot,Norm,LU,QR,Cholesky,SVD,Eigen,Exp,Sin,Cos,Tan,Log,Log10");
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        LUResult<E> lu = lu(a);
        Ring<E> r = a.getScalarRing();
        Field<E> f = (Field<E>) r;
        E det = f.one();
        for (int i = 0; i < a.rows(); i++) det = f.multiply(det, lu.U().get(i, i));
        
        // Account for permutation parity (swaps)
        Vector<E> P = lu.P();
        int n = P.dimension();
        boolean[] visited = new boolean[n];
        int swaps = 0;
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                int count = 0;
                int j = i;
                while (!visited[j]) {
                    visited[j] = true;
                    Object pVal = P.get(j);
                    int pIdx;
                    if (pVal instanceof Number) pIdx = ((Number) pVal).intValue();
                    else if (pVal instanceof org.episteme.core.mathematics.numbers.real.Real) pIdx = (int) ((org.episteme.core.mathematics.numbers.real.Real) pVal).doubleValue();
                    else pIdx = i; // Fallback
                    j = pIdx;
                    count++;
                }
                if (count > 0) swaps += (count - 1);
            }
        }
        if (swaps % 2 != 0) det = f.negate(det);
        
        return det;
    }


    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> inverse(Matrix<E> a) {
        if (a.rows() != a.cols()) {
            Matrix<E> at = transpose(a);
            if (a.rows() > a.cols()) {
                return multiply(inverse(multiply(at, a)), at);
            } else {
                return multiply(at, inverse(multiply(a, at)));
            }
        }
        int n = a.rows();
        Field<E> f = (Field<E>) (Object) a.getScalarRing();
        Class<?> componentType = f.zero().getClass();
        if (f.zero() instanceof Real) componentType = Real.class;
        else if (f.zero() instanceof Complex) componentType = Complex.class;

        E[] invData = (E[]) (Object) java.lang.reflect.Array.newInstance(componentType, n * n);
        LUResult<E> lu = lu(a);
        for (int j = 0; j < n; j++) {
            E[] e_j = (E[]) java.lang.reflect.Array.newInstance(componentType, n);
            java.util.Arrays.fill(e_j, f.zero());
            e_j[j] = f.one();
            Vector<E> col = solve(lu, org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(e_j), f));
            for (int i = 0; i < n; i++) invData[i * n + j] = col.get(i);
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<E>(invData, n, n, f);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solveTriangular(Matrix<E> A, Vector<E> b, boolean upper, boolean transpose, boolean conjugate, boolean unit) {
        if (A.rows() != A.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (A.rows() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        
        int n = A.rows();
        Ring<E> r = A.getScalarRing();
        Field<E> f = (Field<E>) r;
        
        SparseMatrix<E> s = ensureSparse(A, r);
        int[] rowPtr = s.getRowPointers();
        int[] colIdx = s.getColIndices();
        Object[] values = s.getValues();
        
        E[] x = (E[]) new Object[n];
        
        if (!transpose) {
            if (upper) {
                // Backward substitution
                for (int i = n - 1; i >= 0; i--) {
                    E sum = f.zero();
                    E diag = unit ? f.one() : null;
                    for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                        int j = colIdx[k];
                        if (j == i && !unit) {
                            E v = (E) values[k];
                            diag = v;
                        } else if (j > i) {
                            E v = (E) values[k];
                            E matVal = (conjugate && !transpose) ? conjugate(v) : v;
                            sum = f.add(sum, f.multiply(matVal, x[j]));
                        }
                    }
                    if (!unit && (diag == null || diag.equals(f.zero()))) throw new ArithmeticException("Singular triangular matrix at row " + i);
                    E val;
                    if (unit) val = f.subtract(b.get(i), sum);
                    else val = f.divide(f.subtract(b.get(i), sum), diag);
                    x[i] = (conjugate && !transpose) ? conjugate(val) : val;
                }
            } else {
                // Forward substitution
                for (int i = 0; i < n; i++) {
                    E sum = f.zero();
                    E diag = unit ? f.one() : null;
                    for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                        int j = colIdx[k];
                        if (j == i && !unit) {
                            E v = (E) values[k];
                            diag = v;
                        } else if (j < i) {
                            E v = (E) values[k];
                            sum = f.add(sum, f.multiply(v, x[j]));
                        }
                    }
                    if (!unit && (diag == null || diag.equals(f.zero()))) throw new ArithmeticException("Singular triangular matrix at row " + i);
                    E val;
                    if (unit) val = f.subtract(b.get(i), sum);
                    else val = f.divide(f.subtract(b.get(i), sum), diag);
                    x[i] = (conjugate && !transpose) ? conjugate(val) : val;
                }
            }
        } else {
            // Transposed solve: A^T x = b
            if (upper) {
                // A is Upper, A^T is Lower. Solve L x = b
                for (int i = 0; i < n; i++) x[i] = b.get(i);
                for (int i = 0; i < n; i++) {
                    E diag = unit ? f.one() : null;
                    for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                        if (colIdx[k] == i) {
                            E v = (E) values[k];
                            diag = v;
                            break;
                        }
                    }
                    if (!unit && (diag == null || diag.equals(f.zero()))) throw new ArithmeticException("Singular triangular matrix at row " + i);
                    if (!unit) x[i] = f.divide(x[i], diag);
                    for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                        int j = colIdx[k];
                        if (j > i) {
                            // Update following elements
                            // This is not efficient for CSR A^T solve, but okay for sparse.
                            // Actually, a better way for A^T solve is needed if performance is critical.
                        }
                    }
                    // Correcting transposed CSR solve:
                    // For A^T x = b, if A is CSR, we can't easily iterate by rows of A^T.
                    // But we can iterate by rows of A.
                }
                // Let's use a simpler (though slightly less efficient for CSR) transposed solve
                // by just using the transpose matrix.
                return solveTriangular(transpose(A), b, !upper, false, conjugate, unit);
            } else {
                return solveTriangular(transpose(A), b, !upper, false, conjugate, unit);
            }
        }
        
        return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(x), f);
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() { return null; }

    @Override
    public String getId() { return "cpu-sparse"; }

    @Override
    public String getType() { return "math"; }

    @Override
    public String getDescription() { return "Core CPU Sparse Linear Algebra Provider"; }

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        return true;
    }

    @Override
    public Object createBackend() { return this; }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        Ring<E> r = a.getScalarRing();
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(a.dimension(), r.zero());
        // Simple dense iteration over possibly sparse inputs - SparseVectorStorage handles efficiency
        for (int i = 0; i < a.dimension(); i++) {
            E val = r.add(a.get(i), b.get(i));
            if (!val.equals(r.zero())) storage.set(i, val);
        }
        return wrap(new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, r));
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        Ring<E> r = a.getScalarRing();
        return wrap(addSparse(ensureSparse(a, r), ensureSparse(b, r)));
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        Ring<E> r = a.getScalarRing();
        return wrap(addSparse(ensureSparse(a, r), negateSparse(ensureSparse(b, r))));
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        Ring<E> r = a.getScalarRing();
        return wrap(multiplySparse(ensureSparse(a, r), ensureSparse(b, r)));
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        Ring<E> r = a.getScalarRing();
        SparseMatrix<E> s = ensureSparse(a, r);
        int rows = s.rows(); int cols = s.cols();
        List<TreeMap<Integer, E>> rowMaps = new ArrayList<>();
        for (int i = 0; i < cols; i++) rowMaps.add(new TreeMap<>());
        int[] rowPtrs = s.getRowPointers();
        int[] colIdxs = s.getColIndices();
        Object[] values = s.getValues();
        for (int i = 0; i < rows; i++) {
            for (int m = rowPtrs[i]; m < rowPtrs[i+1]; m++) {
                @SuppressWarnings("unchecked")
                E value = (E) values[m];
                rowMaps.get(colIdxs[m]).put(i, value);
            }
        }
        return wrap(buildCSRFromMaps(rowMaps, cols, rows, r));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        Ring<E> r = a.getScalarRing();
        if (scalar.equals(r.zero())) return wrap(new SparseMatrix<>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(a.rows(), a.cols(), r.zero()), r));
        SparseMatrix<E> s = ensureSparse(a, r);
        int[] rowPtrs = s.getRowPointers();
        int[] colIdxs = s.getColIndices();
        Object[] values = s.getValues();
        int nnz = s.getNnz();
        Object[] scaledValues = new Object[nnz];
        for (int i = 0; i < nnz; i++) {
            E val = (E) (Object) values[i];
            scaledValues[i] = r.multiply(val, scalar);
        }
        return wrap(new SparseMatrix<>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(s.rows(), s.cols(), r.zero(), rowPtrs, colIdxs, scaledValues), r));
    }

    @SuppressWarnings("unchecked")
    private SparseMatrix<E> addSparse(SparseMatrix<E> a, SparseMatrix<E> b) {
        Ring<E> r = a.getScalarRing();
        int rows = a.rows(); int cols = a.cols();
        List<TreeMap<Integer, E>> rowMaps = new ArrayList<>();
        for (int i = 0; i < rows; i++) rowMaps.add(new TreeMap<>());
        int[] aPtrs = a.getRowPointers(); int[] aCols = a.getColIndices(); Object[] aVals = a.getValues();
        int[] bPtrs = b.getRowPointers(); int[] bCols = b.getColIndices(); Object[] bVals = b.getValues();
        IntStream.range(0, rows).parallel().forEach(row -> {
            TreeMap<Integer, E> map = rowMaps.get(row);
            for (int i = aPtrs[row]; i < aPtrs[row+1]; i++) {
                E v = (E) aVals[i];
                map.put(aCols[i], v);
            }
            for (int i = bPtrs[row]; i < bPtrs[row+1]; i++) {
                E existing = map.get(bCols[i]);
                E bv = (E) bVals[i];
                E sum = (existing == null) ? bv : r.add(existing, bv);
                if (sum.equals(r.zero())) map.remove(bCols[i]); else map.put(bCols[i], sum);
            }
        });
        return buildCSRFromMaps(rowMaps, rows, cols, r);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        Ring<E> r = a.getScalarRing();
        int dim = a.dimension();
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(dim, r.zero());
        for (int i = 0; i < dim; i++) {
            E val = r.subtract(a.get(i), b.get(i));
            if (!val.equals(r.zero())) storage.set(i, val);
        }
        return wrap(new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, r));
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        Ring<E> r = vector.getScalarRing();
        int dim = vector.dimension();
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(dim, r.zero());
        for (int i = 0; i < dim; i++) {
            E val = r.multiply(vector.get(i), scalar);
            if (!val.equals(r.zero())) storage.set(i, val);
        }
        return wrap(new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, r));
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        Ring<E> r = a.getScalarRing();
        E sum = r.zero();
        
        // Optimize for sparse storage
        if (a.getStorage() instanceof org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> sa && 
            b.getStorage() instanceof org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> sb) {
            
            Map<Integer, E> mapA = sa.getNonZeros();
            Map<Integer, E> mapB = sb.getNonZeros();
            
            // Iterate over the smaller map for efficiency
            if (mapA.size() < mapB.size()) {
                for (Map.Entry<Integer, E> entry : mapA.entrySet()) {
                    E valB = mapB.get(entry.getKey());
                    if (valB != null) {
                        sum = r.add(sum, r.multiply(conjugate(entry.getValue()), valB));
                    }
                }
            } else {
                for (Map.Entry<Integer, E> entry : mapB.entrySet()) {
                    E valA = mapA.get(entry.getKey());
                    if (valA != null) {
                        sum = r.add(sum, r.multiply(conjugate(valA), entry.getValue()));
                    }
                }
            }
        } else if (a.getStorage() instanceof org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> sa) {
            for (Map.Entry<Integer, E> entry : sa.getNonZeros().entrySet()) {
                sum = r.add(sum, r.multiply(conjugate(entry.getValue()), b.get(entry.getKey())));
            }
        } else if (b.getStorage() instanceof org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> sb) {
            for (Map.Entry<Integer, E> entry : sb.getNonZeros().entrySet()) {
                sum = r.add(sum, r.multiply(conjugate(a.get(entry.getKey())), entry.getValue()));
            }
        } else {
            // Dense fallback
            for (int i = 0; i < a.dimension(); i++) {
                sum = r.add(sum, r.multiply(conjugate(a.get(i)), b.get(i)));
            }
        }
        return sum;
    }

    @Override
    public E norm(Vector<E> a) {
        E d = dot(a, a);
        @SuppressWarnings("unchecked")
        E res = sqrt(d, (Field<E>) (Object) a.getScalarRing());
        return res;
    }

    private E conjugate(E element) {
        if (element instanceof Complex) {
            @SuppressWarnings("unchecked")
            E res = (E) ((Complex) element).conjugate();
            return res;
        }
        return element;
    }

    private E sqrt(E element, Field<E> f) {
        if (element instanceof Real) {
            @SuppressWarnings("unchecked")
            E res = (E) ((Real) element).sqrt();
            return res;
        }
        if (element instanceof Complex) {
            @SuppressWarnings("unchecked")
            E res = (E) ((Complex) element).sqrt();
            return res;
        }
        try {
            java.lang.reflect.Method m = element.getClass().getMethod("sqrt");
            @SuppressWarnings("unchecked")
            E res = (E) m.invoke(element);
            return res;
        } catch (Exception e) {}
        return element;
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        Ring<E> r = a.getScalarRing();
        SparseMatrix<E> s = ensureSparse(a, r);
        int rows = s.rows();
        int[] rowPtrs = s.getRowPointers();
        int[] colIdxs = s.getColIndices();
        Object[] values = s.getValues();
        
        org.episteme.core.mathematics.linearalgebra.vectors.storage.VectorStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(rows, r.zero());
            
        for (int i = 0; i < rows; i++) {
            E sum = r.zero();
            for (int k = rowPtrs[i]; k < rowPtrs[i+1]; k++) {
                @SuppressWarnings("unchecked")
                E val = (E) values[k];
                sum = r.add(sum, r.multiply(val, b.get(colIdxs[k])));
            }
            if (!sum.equals(r.zero())) storage.set(i, sum);
        }
        return wrap(new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, r));
    }

    private SparseMatrix<E> multiplySparse(SparseMatrix<E> a, SparseMatrix<E> b) {
        Ring<E> r = a.getScalarRing();
        int rows = a.rows(); int cols = b.cols();
        List<TreeMap<Integer, E>> rowMaps = new ArrayList<>();
        for (int i = 0; i < rows; i++) rowMaps.add(new TreeMap<>());
        IntStream.range(0, rows).parallel().forEach(i -> {
            TreeMap<Integer, E> map = rowMaps.get(i);
            for (int k = 0; k < a.cols(); k++) {
                E av = a.get(i, k);
                if (!av.equals(r.zero())) {
                    for (int j = 0; j < cols; j++) {
                        E bv = b.get(k, j);
                        if (!bv.equals(r.zero())) {
                           E prod = r.multiply(av, bv);
                           E existing = map.get(j);
                           map.put(j, (existing == null) ? prod : r.add(existing, prod));
                        }
                    }
                }
            }
        });
        return buildCSRFromMaps(rowMaps, rows, cols, r);
    }

    private SparseMatrix<E> negateSparse(SparseMatrix<E> a) {
        Ring<E> r = a.getScalarRing();
        SparseMatrix<E> result = (SparseMatrix<E>) scale(r.negate(r.one()), a);
        return result;
    }

    private SparseMatrix<E> toSparse(Matrix<E> a, Ring<E> r) {
        List<TreeMap<Integer, E>> rowMaps = new ArrayList<>();
        for (int i = 0; i < a.rows(); i++) {
            TreeMap<Integer, E> map = new TreeMap<>();
            for (int j = 0; j < a.cols(); j++) {
                E val = a.get(i, j);
                if (!val.equals(r.zero())) map.put(j, val);
            }
            rowMaps.add(map);
        }
        return buildCSRFromMaps(rowMaps, a.rows(), a.cols(), r);
    }

    private SparseMatrix<E> buildCSRFromMaps(List<TreeMap<Integer, E>> rowMaps, int rows, int cols, Ring<E> r) {
        return new SparseMatrix<>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, r.zero(), rowMaps), r);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (a.rows() == a.cols()) {
            Field<E> f = (Field<E>) a.getScalarRing();
            
            // Check for triangularity to avoid iterative solver breakdowns
            boolean isLower = true;
            boolean isUpper = true;
            SparseMatrix<E> s = ensureSparse(a, (Ring<E>) f);
            int[] rowPtr = s.getRowPointers();
            int[] colIdx = s.getColIndices();
            for (int i = 0; i < s.rows(); i++) {
                for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                    int j = colIdx[k];
                    if (j > i) isLower = false;
                    if (j < i) isUpper = false;
                }
                if (!isLower && !isUpper) break;
            }
            
            if (isLower) return solveTriangular(a, b, false, false, false, false);
            if (isUpper) return solveTriangular(a, b, true, false, false, false);

            E tol;
            org.episteme.core.mathematics.context.NumericalConfiguration config = org.episteme.core.mathematics.context.MathContext.getNumericalConfiguration();
            if (f.one() instanceof Real) {
                tol = (E) Real.of(config.getEpsilonDouble());
            } else if (f.one() instanceof Complex) {
                tol = (E) Complex.of(config.getEpsilonDouble(), 0);
            } else if (f.one() instanceof org.episteme.core.mathematics.numbers.real.RealBig) {
                tol = (E) org.episteme.core.mathematics.numbers.real.RealBig.create(java.math.BigDecimal.valueOf(config.getEpsilonDouble()));
            } else {
                tol = f.zero();
            }

            if (a.rows() < 100) {
                return GenericLU.solve(GenericLU.decompose(a, f, this), b, f, this);
            }

            return bicgstab(a, b, org.episteme.core.mathematics.linearalgebra.vectors.SparseVector.zeros(b.dimension(), f), tol, config.getMaxIterations());
        }
        // Rectangular solve
        if (a.rows() > a.cols()) {
            // Overdetermined: x = (A^T A)^-1 A^T b
            Matrix<E> at = transpose(a);
            return solve(multiply(at, a), multiply(at, b));
        } else {
            // Underdetermined: x = A^T (A A^T)^-1 b (minimum norm solution)
            Matrix<E> at = transpose(a);
            return multiply(at, solve(multiply(a, at), b));
        }
    }


    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        Vector<E> result = GenericSparseSolvers.bicgstab(this, a, b, x0, tolerance, maxIterations, (Field<E>) a.getScalarRing());
        return result;
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        Vector<E> result = GenericSparseSolvers.conjugateGradient(this, a, b, x0, tolerance, maxIterations, (Field<E>) b.getScalarRing());
        return result;
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        Vector<E> result = wrap(GenericSparseSolvers.gmres(this, a, b, x0, tolerance, maxIterations, restarts, (Field<E>) b.getScalarRing()));
        return result;
    }

    private Matrix<E> wrap(Matrix<E> m) {
        if (m instanceof GenericMatrix) return ((GenericMatrix<E>) m).withProvider(this);
        return m;
    }

    private Vector<E> wrap(Vector<E> v) {
        if (v instanceof org.episteme.core.mathematics.linearalgebra.vectors.GenericVector) return ((org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>) v).withProvider(this);
        return v;
    }

    @Override
    public Vector<E> normalize(Vector<E> a) {
        E n = norm(a);
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            if (n instanceof org.episteme.core.mathematics.numbers.real.Real r && r.isZero()) return a;
            E invNorm = field.inverse(n);
            return multiply(a, invNorm);
        }
        throw new UnsupportedOperationException("Normalization requires a Field for inversion.");
    }

    @Override
    public Vector<E> cross(Vector<E> a, Vector<E> b) {
        if (a.dimension() != 3 || b.dimension() != 3) {
            throw new ArithmeticException("Cross product is only defined for 3D vectors.");
        }
        Ring<E> ring = a.getScalarRing();
        E u1 = a.get(0), u2 = a.get(1), u3 = a.get(2);
        E v1 = b.get(0), v2 = b.get(1), v3 = b.get(2);
        
        E c1 = ring.subtract(ring.multiply(u2, v3), ring.multiply(u3, v2)); 
        E c2 = ring.subtract(ring.multiply(u3, v1), ring.multiply(u1, v3)); 
        E c3 = ring.subtract(ring.multiply(u1, v2), ring.multiply(u2, v1));
        
        return wrap(org.episteme.core.mathematics.linearalgebra.vectors.SparseVector.of(java.util.Arrays.asList(c1, c2, c3), ring));
    }

    @Override
    @SuppressWarnings("unchecked")
    public E angle(Vector<E> a, Vector<E> b) {
        Ring<E> ring = a.getScalarRing();
        E dAB = dot(a, b);
        E nA = norm(a);
        E nB = norm(b);
        E denom = ring.multiply(nA, nB);

        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            double dot = ((org.episteme.core.mathematics.numbers.real.Real)dAB).doubleValue();
            double n1 = ((org.episteme.core.mathematics.numbers.real.Real)nA).doubleValue();
            double n2 = ((org.episteme.core.mathematics.numbers.real.Real)nB).doubleValue();
            
            if (n1 == 0 || n2 == 0) return ring.zero(); 

            double crossNormSq = Math.max(0, (n1 * n1 * n2 * n2) - (dot * dot));
            double theta = Math.atan2(Math.sqrt(crossNormSq), dot);
            
            Object zero = ring.zero();
            if (zero instanceof org.episteme.core.mathematics.numbers.real.RealFloat) {
                E result = (E) org.episteme.core.mathematics.numbers.real.RealFloat.of((float)theta);
                return result;
            }
            E result = (E) org.episteme.core.mathematics.numbers.real.Real.of(theta);
            return result;
        }
        
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            if (ring.zero().equals(nA) || ring.zero().equals(nB)) return ring.zero();
            E cosTheta = field.divide(dAB, denom);
            
            if (cosTheta instanceof org.episteme.core.mathematics.numbers.real.Real r) {
                double val = r.doubleValue();
                if (val > 1.0) cosTheta = (E) org.episteme.core.mathematics.numbers.real.Real.of(1.0);
                else if (val < -1.0) cosTheta = (E) org.episteme.core.mathematics.numbers.real.Real.of(-1.0);
            }

            E[][] data = (E[][]) new Object[][]{{cosTheta}};
            return acos(Matrix.of(data, ring)).get(0, 0);
        }
        throw new UnsupportedOperationException("Angle calculation requires a Field for division.");
    }

    @Override
    public Vector<E> projection(Vector<E> a, Vector<E> b) {
        E dAB = dot(a, b);
        E dBB = dot(b, b);
        Ring<E> ring = a.getScalarRing();
        if (ring instanceof org.episteme.core.mathematics.structures.rings.Field<E> field) {
            E scalar = field.divide(dAB, dBB);
            return b.multiply(scalar);
        }
        throw new UnsupportedOperationException("Projection requires a Field for division.");
    }

    @Override public Matrix<E> exp(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).exp(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).exp(); return res; } throw new UnsupportedOperationException("exp not supported"); }); }
    @Override public Matrix<E> log(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).log(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).log(); return res; } throw new UnsupportedOperationException("log not supported"); }); }
    @Override public Matrix<E> log10(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).log10(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).log10(); return res; } throw new UnsupportedOperationException("log10 not supported"); }); }
    @Override public Matrix<E> sin(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).sin(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).sin(); return res; } throw new UnsupportedOperationException("sin not supported"); }); }
    @Override public Matrix<E> cos(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).cos(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).cos(); return res; } throw new UnsupportedOperationException("cos not supported"); }); }
    @Override public Matrix<E> tan(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).tan(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).tan(); return res; } throw new UnsupportedOperationException("tan not supported"); }); }
    @Override public Matrix<E> asin(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).asin(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).asin(); return res; } throw new UnsupportedOperationException("asin not supported"); }); }
    @Override public Matrix<E> acos(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).acos(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).acos(); return res; } throw new UnsupportedOperationException("acos not supported"); }); }
    @Override public Matrix<E> atan(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).atan(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).atan(); return res; } throw new UnsupportedOperationException("atan not supported"); }); }
    @Override public Matrix<E> sinh(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).sinh(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).sinh(); return res; } throw new UnsupportedOperationException("sinh not supported"); }); }
    @Override public Matrix<E> cosh(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).cosh(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).cosh(); return res; } throw new UnsupportedOperationException("cosh not supported"); }); }
    @Override public Matrix<E> tanh(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).tanh(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).tanh(); return res; } throw new UnsupportedOperationException("tanh not supported"); }); }
    @Override public Matrix<E> asinh(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).asinh(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).asinh(); return res; } throw new UnsupportedOperationException("asinh not supported"); }); }
    @Override public Matrix<E> acosh(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).acosh(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).acosh(); return res; } throw new UnsupportedOperationException("acosh not supported"); }); }
    @Override public Matrix<E> atanh(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).atanh(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).atanh(); return res; } throw new UnsupportedOperationException("atanh not supported"); }); }
    @Override public Matrix<E> pow(Matrix<E> a, E exponent) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real && exponent instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).pow((org.episteme.core.mathematics.numbers.real.Real)exponent); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex && exponent instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).pow((org.episteme.core.mathematics.numbers.complex.Complex)exponent); return res; } throw new UnsupportedOperationException("pow not supported"); }); }
    @Override public Matrix<E> sqrt(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).sqrt(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).sqrt(); return res; } throw new UnsupportedOperationException("sqrt not supported"); }); }
    @Override public Matrix<E> cbrt(Matrix<E> a) { return a.map(val -> { if (val instanceof org.episteme.core.mathematics.numbers.real.Real) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.real.Real)val).cbrt(); return res; } if (val instanceof org.episteme.core.mathematics.numbers.complex.Complex) { @SuppressWarnings("unchecked") E res = (E)((org.episteme.core.mathematics.numbers.complex.Complex)val).cbrt(); return res; } throw new UnsupportedOperationException("cbrt not supported"); }); }

    @Override
    public E trace(Matrix<E> a) {
        Ring<E> r = a.getScalarRing();
        E sum = r.zero();
        for (int i = 0; i < Math.min(a.rows(), a.cols()); i++) {
            E val = a.get(i, i);
            if (val != null) sum = r.add(sum, val);
        }
        return sum;
    }

    // Removed super fallbacks for transcendental operations to allow AlgorithmManager to handle them.



}
