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
    @SuppressWarnings("unchecked")
    public LUResult<E> lu(Matrix<E> a) {
        return GenericLU.decompose(a, (Field<E>) (Object) a.getScalarRing(), this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public QRResult<E> qr(Matrix<E> a) {
        return GenericQR.decompose(a, (Field<E>) (Object) a.getScalarRing(), this);
    }

    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        return GenericCholesky.decompose(a, (Field<E>) a.getScalarRing(), this);
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        return GenericSVD.decompose(a, (Field<E>) a.getScalarRing(), this);
    }

    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        return GenericEigen.decompose(a, (Field<E>) a.getScalarRing(), this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        return GenericLU.solve(lu, b, (Field<E>) (Object) b.getScalarRing(), this);
    }

    @Override
    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        return GenericQR.solve(qr, b, (Field<E>) b.getScalarRing(), this);
    }

    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        return GenericCholesky.solve(cholesky, b, (Field<E>) b.getScalarRing(), this);
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
                @SuppressWarnings("unchecked") E value = (E) values[m];
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
        for (int i = 0; i < nnz; i++) scaledValues[i] = r.multiply((E) (Object) values[i], scalar);
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
            for (int i = aPtrs[row]; i < aPtrs[row+1]; i++) map.put(aCols[i], (E) aVals[i]);
            for (int i = bPtrs[row]; i < bPtrs[row+1]; i++) {
                E existing = map.get(bCols[i]);
                E sum = (existing == null) ? (E) bVals[i] : r.add(existing, (E) bVals[i]);
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
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        E d = dot(a, a);
        return sqrt(d, (Field<E>) (Object) a.getScalarRing());
    }

    @SuppressWarnings("unchecked")
    private E conjugate(E element) {
        if (element instanceof Complex c) {
            return (E) c.conjugate();
        }
        return element;
    }

    @SuppressWarnings("unchecked")
    private E sqrt(E element, Field<E> f) {
        if (element instanceof Real) {
            return (E) ((Real) element).sqrt();
        }
        if (element instanceof Complex) {
            return (E) ((Complex) element).sqrt();
        }
        try {
            java.lang.reflect.Method m = element.getClass().getMethod("sqrt");
            return (E) m.invoke(element);
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
                @SuppressWarnings("unchecked") E val = (E) values[k];
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
        return (SparseMatrix<E>) scale(r.negate(r.one()), a);
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
            E tol;
            if (f.one() instanceof Real) tol = (E) Real.of(1e-12);
            else if (f.one() instanceof Complex) tol = (E) Complex.of(1e-12, 0);
            else if (f.one() instanceof org.episteme.core.mathematics.numbers.real.RealBig) tol = (E) org.episteme.core.mathematics.numbers.real.RealBig.create(new java.math.BigDecimal("1e-12"));
            else tol = f.zero(); // Fallback

            return bicgstab(a, b, org.episteme.core.mathematics.linearalgebra.vectors.SparseVector.zeros(b.dimension(), f), tol, 1000);
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
        return GenericSparseSolvers.bicgstab(this, a, b, x0, tolerance, maxIterations, (Field<E>) a.getScalarRing());
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        return GenericSparseSolvers.conjugateGradient(this, a, b, x0, tolerance, maxIterations, (Field<E>) b.getScalarRing());
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        return wrap(GenericSparseSolvers.gmres(this, a, b, x0, tolerance, maxIterations, restarts, (Field<E>) b.getScalarRing()));
    }

    private Matrix<E> wrap(Matrix<E> m) {
        if (m instanceof GenericMatrix) return ((GenericMatrix<E>) m).withProvider(this);
        return m;
    }

    private Vector<E> wrap(Vector<E> v) {
        if (v instanceof org.episteme.core.mathematics.linearalgebra.vectors.GenericVector) return ((org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>) v).withProvider(this);
        return v;
    }

    @Override public Matrix<E> exp(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.exp(a)); }
    @Override public Matrix<E> log(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.log(a)); }
    @Override public Matrix<E> log10(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.log10(a)); }
    @Override public Matrix<E> sin(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.sin(a)); }
    @Override public Matrix<E> cos(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.cos(a)); }
    @Override public Matrix<E> tan(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.tan(a)); }
    @Override public Matrix<E> asin(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.asin(a)); }
    @Override public Matrix<E> acos(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.acos(a)); }
    @Override public Matrix<E> atan(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.atan(a)); }
    @Override public Matrix<E> sinh(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.sinh(a)); }
    @Override public Matrix<E> cosh(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.cosh(a)); }
    @Override public Matrix<E> tanh(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.tanh(a)); }
    @Override public Matrix<E> sqrt(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.sqrt(a)); }
    @Override public Matrix<E> cbrt(Matrix<E> a) { return wrap(SparseLinearAlgebraProvider.super.cbrt(a)); }
    @Override public Matrix<E> pow(Matrix<E> a, E exponent) { return wrap(SparseLinearAlgebraProvider.super.pow(a, exponent)); }


}
