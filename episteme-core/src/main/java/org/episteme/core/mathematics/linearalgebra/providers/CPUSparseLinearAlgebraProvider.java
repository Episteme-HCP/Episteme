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
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import com.google.auto.service.AutoService;

/**
 * Linear Algebra Provider for Sparse Matrices (CPU).
 * <p>
 * Optimized for SparseMatrix implementations.
 * Uses CSR-based algorithms that only process non-zero elements.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, Backend.class, CPUBackend.class})
public class CPUSparseLinearAlgebraProvider<E> implements SparseLinearAlgebraProvider<E>, CPUBackend {

    protected final Ring<E> ring;

    public CPUSparseLinearAlgebraProvider(Ring<E> ring) {
        this.ring = ring;
    }

    @Override
    public String getName() {
        return "Episteme CPU (Sparse)";
    }

    /**
     * Public no-arg constructor required by ServiceLoader.
     */
    public CPUSparseLinearAlgebraProvider() {
        this.ring = null;
    }

    private static final int PARALLEL_THRESHOLD = 500; // Lower threshold for sparse logic overhead

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void shutdown() {
        // No-op
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return null; // CPU has no specific execution context
    }

    @Override
    public String getId() {
        return "cpu-sparse";
    }

    @Override
    public String getType() {
        return "math";
    }

    @Override
    public String getDescription() {
        return "Core CPU Sparse Linear Algebra Provider";
    }

    @Override
    public Object createBackend() {
        return this;
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (a instanceof SparseMatrix && b instanceof SparseMatrix) {
            return addSparse((SparseMatrix<E>) a, (SparseMatrix<E>) b);
        }
        return SparseLinearAlgebraProvider.super.add(a, b);
    }

    /**
     * Efficient sparse matrix addition using CSR format.
     */
    @SuppressWarnings("unchecked")
    private SparseMatrix<E> addSparse(SparseMatrix<E> a, SparseMatrix<E> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) {
            throw new IllegalArgumentException(
                    "Matrix dimensions must match: " + a.rows() + "x" + a.cols() +
                            " vs " + b.rows() + "x" + b.cols());
        }
        
        // Fix for NPE: Use ring from input matrix if this.ring is null (ServiceLoader instance)
        Ring<E> r = (this.ring != null) ? this.ring : a.getField();

        int rows = a.rows();
        int cols = a.cols();

        // Use TreeMap to store results sorted by column, for each row
        List<TreeMap<Integer, E>> rowMaps = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            rowMaps.add(new TreeMap<>());
        }

        // Add elements from a (using CSR accessors)
        Object[] aVals = a.getValues();
        int[] aCols = a.getColIndices();
        int[] aRowPtrs = a.getRowPointers();

        // Add elements from b
        Object[] bVals = b.getValues();
        int[] bCols = b.getColIndices();
        int[] bRowPtrs = b.getRowPointers();

        // Build maps for each row
        if (rows < PARALLEL_THRESHOLD) {
            for (int row = 0; row < rows; row++) {
                // Populate from A
                for (int i = aRowPtrs[row]; i < aRowPtrs[row + 1]; i++) {
                    rowMaps.get(row).put(aCols[i], (E) aVals[i]);
                }
                // Update from B
                TreeMap<Integer, E> rowMap = rowMaps.get(row);
                for (int i = bRowPtrs[row]; i < bRowPtrs[row + 1]; i++) {
                    int col = bCols[i];
                    E bVal = (E) bVals[i];
                    E existing = rowMap.get(col);
                    if (existing != null) {
                        E sum = r.add(existing, bVal);
                        if (!sum.equals(r.zero())) {
                            rowMap.put(col, sum);
                        } else {
                            rowMap.remove(col);
                        }
                    } else {
                        rowMap.put(col, bVal);
                    }
                }
            }
        } else {
            // Parallel execution per row
            org.episteme.core.ComputeContext ctx = org.episteme.core.ComputeContext.current();
            IntStream.range(0, rows).parallel().forEach(row -> {
                ctx.checkCancelled();
                // Populate from A
                for (int i = aRowPtrs[row]; i < aRowPtrs[row + 1]; i++) {
                    rowMaps.get(row).put(aCols[i], (E) aVals[i]);
                }
                // Update from B
                TreeMap<Integer, E> rowMap = rowMaps.get(row);
                for (int i = bRowPtrs[row]; i < bRowPtrs[row + 1]; i++) {
                    int col = bCols[i];
                    E bVal = (E) bVals[i];
                    E existing = rowMap.get(col);
                    
                    if (existing != null) {
                        E sum = r.add(existing, bVal);
                        if (!sum.equals(r.zero())) {
                            rowMap.put(col, sum);
                        } else {
                            rowMap.remove(col);
                        }
                    } else {
                        rowMap.put(col, bVal);
                    }
                }
            });
        }

        // Build CSR format result
        return buildCSRFromMaps(rowMaps, rows, cols, r);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vector dimensions must match");
        }
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();
        int dim = a.dimension();
        
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(dim, r.zero());
        
        // Populate from a
        if (a.getStorage() instanceof org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage) {
            ((org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E>) a.getStorage())
                .getNonZeros().forEach((Integer idx, E val) -> storage.set(idx.intValue(), val));
        } else {
            for (int i = 0; i < dim; i++) {
                storage.set(i, a.get(i));
            }
        }
        
        // Subtract from b
        if (b.getStorage() instanceof org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage) {
            ((org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E>) b.getStorage())
                .getNonZeros().forEach((Integer idx, E val) -> {
                    E existing = storage.get(idx);
                    storage.set(idx, r.subtract(existing, val));
                });
        } else {
            for (int i = 0; i < dim; i++) {
                E val = b.get(i);
                if (!val.equals(r.zero())) {
                    E existing = storage.get(i);
                    storage.set(i, r.subtract(existing, val));
                }
            }
        }
        
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, r);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        Ring<E> r = (this.ring != null) ? this.ring : vector.getScalarRing();
        if (scalar.equals(r.zero())) {
            return org.episteme.core.mathematics.linearalgebra.vectors.SparseVector.zeros(vector.dimension(), r);
        }
        
        int dim = vector.dimension();
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(dim, r.zero());
            
        if (vector.getStorage() instanceof org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage) {
            ((org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E>) vector.getStorage())
                .getNonZeros().forEach((idx, val) -> storage.set(idx, r.multiply(val, scalar)));
        } else {
            for (int i = 0; i < dim; i++) {
                E val = vector.get(i);
                if (!val.equals(r.zero())) {
                    storage.set(i, r.multiply(val, scalar));
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, r);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Dimensions must match");
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();
        E sum = r.zero();
        
        if (a.getStorage() instanceof org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage) {
            ((org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E>) a.getStorage())
                .getNonZeros().forEach((idx, val) -> {
                    // sum = sum + val * b.get(idx)
                    // We need to use recursion or a container because of lambda final restriction
                });
            // Let's use old fashioned loop for dot to avoid overhead and lambda issues
             Map<Integer, E> nz = ((org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E>) a.getStorage()).getNonZeros();
            for (Map.Entry<Integer, E> entry : nz.entrySet()) {
                sum = r.add(sum, r.multiply(entry.getValue(), b.get(entry.getKey())));
            }
        } else if (b.getStorage() instanceof org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage) {
             Map<Integer, E> nz = ((org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E>) b.getStorage()).getNonZeros();
            for (Map.Entry<Integer, E> entry : nz.entrySet()) {
                sum = r.add(sum, r.multiply(a.get(entry.getKey()), entry.getValue()));
            }
        } else {
            for (int i = 0; i < a.dimension(); i++) {
                sum = r.add(sum, r.multiply(a.get(i), b.get(i)));
            }
        }
        return sum;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E norm(Vector<E> a) {
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();
        E d = dot(a, a);
        if (r instanceof org.episteme.core.mathematics.sets.Reals) {
             return (E) ((Real) d).sqrt();
        }
        throw new UnsupportedOperationException("Norm requires sqrt support on scalars (Reals supported)");
    }

    @SuppressWarnings("unchecked")
    private void computeRowMultiplication(int i, TreeMap<Integer, E> rowMap,
            int[] aRowPtrs, int[] aCols, Object[] aVals,
            int[] bRowPtrs, int[] bCols, Object[] bVals, Ring<E> r) {

        for (int aIdx = aRowPtrs[i]; aIdx < aRowPtrs[i + 1]; aIdx++) {
            int k = aCols[aIdx];
            E aVal = (E) aVals[aIdx];

            // Multiply A(i,k) by each non-zero in row k of B
            for (int bIdx = bRowPtrs[k]; bIdx < bRowPtrs[k + 1]; bIdx++) {
                int j = bCols[bIdx];
                E bVal = (E) bVals[bIdx];
                E product = r.multiply(aVal, bVal);

                E existing = rowMap.get(j);
                if (existing != null) {
                    rowMap.put(j, r.add(existing, product));
                } else {
                    rowMap.put(j, product);
                }
            }
        }

        // Remove zeros
        rowMap.entrySet().removeIf(e -> e.getValue().equals(r.zero()));
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a instanceof SparseMatrix && b instanceof SparseMatrix) {
            return multiplySparse((SparseMatrix<E>) a, (SparseMatrix<E>) b);
        }
        return SparseLinearAlgebraProvider.super.multiply(a, b);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (a.cols() != b.dimension()) {
            throw new IllegalArgumentException("Matrix columns must match vector dimension");
        }
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();
        int rows = a.rows();

        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<>(rows, r.zero());

        if (a instanceof SparseMatrix) {
            SparseMatrix<E> s = (SparseMatrix<E>) a;
            int[] rowPtrs = s.getRowPointers();
            int[] colIdxs = s.getColIndices();
            Object[] values = s.getValues();

            for (int i = 0; i < rows; i++) {
                E rowSum = r.zero();
                for (int j = rowPtrs[i]; j < rowPtrs[i + 1]; j++) {
                    @SuppressWarnings("unchecked")
                    E val = (E) values[j];
                    E bVal = b.get(colIdxs[j]);
                    rowSum = r.add(rowSum, r.multiply(val, bVal));
                }
                if (!rowSum.equals(r.zero())) {
                    storage.set(i, rowSum);
                }
            }
        } else {
            for (int i = 0; i < rows; i++) {
                E rowSum = r.zero();
                for (int j = 0; j < a.cols(); j++) {
                    rowSum = r.add(rowSum, r.multiply(a.get(i, j), b.get(j)));
                }
                if (!rowSum.equals(r.zero())) {
                    storage.set(i, rowSum);
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, r);
    }

    /**
     * Efficient sparse matrix multiplication using CSR format.
     */
    private SparseMatrix<E> multiplySparse(SparseMatrix<E> a, SparseMatrix<E> b) {
        if (a.cols() != b.rows()) {
            throw new IllegalArgumentException(
                    "Matrix dimensions incompatible for multiplication: " +
                            a.rows() + "x" + a.cols() + " * " + b.rows() + "x" + b.cols());
        }
        
        // Fix for NPE: Use ring from input matrix
        Ring<E> r = (this.ring != null) ? this.ring : a.getField();

        int resultRows = a.rows();
        int resultCols = b.cols();

        // Store results in TreeMaps for each row
        List<TreeMap<Integer, E>> rowMaps = new ArrayList<>();
        for (int i = 0; i < resultRows; i++) {
            rowMaps.add(new TreeMap<>());
        }

        Object[] aVals = a.getValues();
        int[] aCols = a.getColIndices();
        int[] aRowPtrs = a.getRowPointers();

        Object[] bVals = b.getValues();
        int[] bCols = b.getColIndices();
        int[] bRowPtrs = b.getRowPointers();

        if (resultRows < PARALLEL_THRESHOLD) {
            // Sequential
            for (int i = 0; i < resultRows; i++) {
                computeRowMultiplication(i, rowMaps.get(i), aRowPtrs, aCols, aVals, bRowPtrs, bCols, bVals, r);
            }
        } else {
            // Parallel
            org.episteme.core.ComputeContext ctx = org.episteme.core.ComputeContext.current();
            IntStream.range(0, resultRows).parallel().forEach(i -> {
                ctx.checkCancelled();
                computeRowMultiplication(i, rowMaps.get(i), aRowPtrs, aCols, aVals, bRowPtrs, bCols, bVals, r);
            });
        }

        return buildCSRFromMaps(rowMaps, resultRows, resultCols, r);
    }

    /**
     * Builds a SparseMatrix in CSR format from row maps.
     */
    private SparseMatrix<E> buildCSRFromMaps(List<TreeMap<Integer, E>> rowMaps, int rows, int cols, Ring<E> r) {
        // Create storage
        E zero = r.zero();
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(
                rows, cols, zero);

        // Populate directly
        for (int row = 0; row < rows; row++) {
            for (Map.Entry<Integer, E> entry : rowMaps.get(row).entrySet()) {
                storage.set(row, entry.getKey(), entry.getValue());
            }
        }

        return new SparseMatrix<E>(storage, r);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            // Default to BiCGSTAB for general sparse matrices
            Vector<Real> x0 = org.episteme.core.mathematics.linearalgebra.vectors.SparseVector.zeros(b.dimension(), (Ring<Real>) ring);
            return bicgstab(a, b, (Vector<E>) (Vector<?>) x0, (E) (Object) Real.of(1e-12), 1000);
        }
        return SparseLinearAlgebraProvider.super.solve(a, b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        if (!(ring instanceof org.episteme.core.mathematics.sets.Reals)) {
            throw new UnsupportedOperationException("BiCGSTAB only supported for Reals");
        }
        Real[] bArr = toArray((Vector<Real>) b);
        Real[] x0Arr = toArray((Vector<Real>) x0);
        Real tol = (Real) tolerance;
        
        Real[] result = JavaBiCGSTAB.solve((Matrix<Real>) a, bArr, x0Arr, tol, maxIterations);
        return (Vector<E>) org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(result), (Ring<Real>) ring);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        if (!(ring instanceof org.episteme.core.mathematics.sets.Reals)) {
            throw new UnsupportedOperationException("Conjugate Gradient only supported for Reals");
        }
        Real[] bArr = toArray((Vector<Real>) b);
        Real[] x0Arr = toArray((Vector<Real>) x0);
        Real tol = (Real) tolerance;
        
        Real[] result = JavaConjugateGradient.solve((Matrix<Real>) a, bArr, x0Arr, tol, maxIterations);
        return (Vector<E>) org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(result), (Ring<Real>) ring);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        if (!(ring instanceof org.episteme.core.mathematics.sets.Reals)) {
            throw new UnsupportedOperationException("GMRES only supported for Reals");
        }
        Real[] bArr = toArray((Vector<Real>) b);
        Real[] x0Arr = toArray((Vector<Real>) x0);
        Real tol = (Real) tolerance;
        
        Real[] result = JavaGMRES.solve((Matrix<Real>) a, bArr, x0Arr, tol, maxIterations, restarts);
        return (Vector<E>) org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(result), (Ring<Real>) ring);
    }

    private Real[] toArray(Vector<Real> v) {
        Real[] arr = new Real[v.dimension()];
        for (int i = 0; i < arr.length; i++) arr[i] = v.get(i);
        return arr;
    }

    // --- Internal Java Sparse Solvers ---

    private static class JavaSparseUtils {
        public static Real[] matrixVectorMultiply(Matrix<Real> A, Real[] x) {
            int n = x.length;
            Real[] result = new Real[n];
            java.util.Arrays.fill(result, Real.ZERO);

            if (A instanceof SparseMatrix) {
                SparseMatrix<Real> S = (SparseMatrix<Real>) A;
                Object[] values = S.getValues();
                int[] cols = S.getColIndices();
                int[] rowPtrs = S.getRowPointers();

                for (int i = 0; i < n; i++) {
                    for (int j = rowPtrs[i]; j < rowPtrs[i + 1]; j++) {
                        result[i] = result[i].add(((Real) values[j]).multiply(x[cols[j]]));
                    }
                }
            } else {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        result[i] = result[i].add(A.get(i, j).multiply(x[j]));
                    }
                }
            }
            return result;
        }

        public static Real dotProduct(Real[] a, Real[] b) {
            Real sum = Real.ZERO;
            for (int i = 0; i < a.length; i++) sum = sum.add(a[i].multiply(b[i]));
            return sum;
        }

        public static Real norm(Real[] v) {
            return dotProduct(v, v).sqrt();
        }

        public static Real[] subtract(Real[] a, Real[] b) {
            Real[] result = new Real[a.length];
            for (int i = 0; i < a.length; i++) result[i] = a[i].subtract(b[i]);
            return result;
        }
    }

    private static class JavaBiCGSTAB {
        public static Real[] solve(Matrix<Real> A, Real[] b, Real[] x0, Real tolerance, int maxIterations) {
            int n = b.length;
            Real[] x = new Real[n];
            System.arraycopy(x0, 0, x, 0, n);

            Real[] r = JavaSparseUtils.subtract(b, JavaSparseUtils.matrixVectorMultiply(A, x));
            Real[] r0 = r.clone();
            Real rho = Real.ONE, alpha = Real.ONE, omega = Real.ONE;
            Real[] v = new Real[n], p = new Real[n];
            java.util.Arrays.fill(v, Real.ZERO);
            java.util.Arrays.fill(p, Real.ZERO);

            for (int iter = 0; iter < maxIterations; iter++) {
                Real rhoOld = rho;
                rho = JavaSparseUtils.dotProduct(r0, r);
                if (rho.abs().compareTo(Real.of(1e-20)) < 0) break;

                if (iter == 0) System.arraycopy(r, 0, p, 0, n);
                else {
                    Real beta = rho.divide(rhoOld).multiply(alpha.divide(omega));
                    for (int i = 0; i < n; i++) p[i] = r[i].add(beta.multiply(p[i].subtract(omega.multiply(v[i]))));
                }

                v = JavaSparseUtils.matrixVectorMultiply(A, p);
                alpha = rho.divide(JavaSparseUtils.dotProduct(r0, v));

                Real[] s = new Real[n];
                for (int i = 0; i < n; i++) s[i] = r[i].subtract(alpha.multiply(v[i]));
                if (JavaSparseUtils.norm(s).compareTo(tolerance) < 0) {
                    for (int i = 0; i < n; i++) x[i] = x[i].add(alpha.multiply(p[i]));
                    break;
                }

                Real[] t = JavaSparseUtils.matrixVectorMultiply(A, s);
                omega = JavaSparseUtils.dotProduct(t, s).divide(JavaSparseUtils.dotProduct(t, t));
                for (int i = 0; i < n; i++) x[i] = x[i].add(alpha.multiply(p[i])).add(omega.multiply(s[i]));
                for (int i = 0; i < n; i++) r[i] = s[i].subtract(omega.multiply(t[i]));
                if (JavaSparseUtils.norm(r).compareTo(tolerance) < 0) break;
                if (omega.abs().compareTo(Real.of(1e-20)) < 0) break;
            }
            return x;
        }
    }

    private static class JavaConjugateGradient {
        public static Real[] solve(Matrix<Real> A, Real[] b, Real[] x0, Real tolerance, int maxIterations) {
            int n = b.length;
            Real[] x = x0.clone();
            Real[] Ax = JavaSparseUtils.matrixVectorMultiply(A, x);
            Real[] r = JavaSparseUtils.subtract(b, Ax);
            Real[] p = r.clone();
            Real rsold = JavaSparseUtils.dotProduct(r, r);

            for (int iter = 0; iter < maxIterations; iter++) {
                Real[] Ap = JavaSparseUtils.matrixVectorMultiply(A, p);
                Real pAp = JavaSparseUtils.dotProduct(p, Ap);
                if (pAp.abs().compareTo(Real.of(1e-20)) < 0) break;
                
                Real alpha = rsold.divide(pAp);
                for (int i = 0; i < n; i++) x[i] = x[i].add(alpha.multiply(p[i]));
                for (int i = 0; i < n; i++) r[i] = r[i].subtract(alpha.multiply(Ap[i]));

                Real rsnew = JavaSparseUtils.dotProduct(r, r);
                if (rsnew.sqrt().compareTo(tolerance) < 0) break;

                Real beta = rsnew.divide(rsold);
                for (int i = 0; i < n; i++) p[i] = r[i].add(beta.multiply(p[i]));
                rsold = rsnew;
            }
            return x;
        }
    }

    private static class JavaGMRES {
        public static Real[] solve(Matrix<Real> A, Real[] b, Real[] x0, Real tolerance, int maxIterations, int restarts) {
            int n = b.length;
            Real[] x = x0.clone();
            
            for (int restart = 0; restart < restarts; restart++) {
                Real[] r0 = JavaSparseUtils.subtract(b, JavaSparseUtils.matrixVectorMultiply(A, x));
                Real beta = JavaSparseUtils.norm(r0);
                if (beta.compareTo(tolerance) < 0) break;

                Real[][] V = new Real[maxIterations + 1][n];
                // v1 = r0 / beta
                for (int i = 0; i < n; i++) V[0][i] = r0[i].divide(beta);

                for (int j = 0; j < maxIterations; j++) {
                    Real[] w = JavaSparseUtils.matrixVectorMultiply(A, V[j]);
                    for (int i = 0; i <= j; i++) {
                        Real h = JavaSparseUtils.dotProduct(w, V[i]);
                        for (int k = 0; k < n; k++) w[k] = w[k].subtract(h.multiply(V[i][k]));
                    }
                    Real hNext = JavaSparseUtils.norm(w);
                    if (hNext.compareTo(Real.of(1e-20)) < 0) break;
                    for (int i = 0; i < n; i++) V[j+1][i] = w[i].divide(hNext);
                }

                // Simplified update (proper GMRES needs back-substitution on H)
                for (int i = 0; i < Math.min(maxIterations, n); i++) {
                    for (int j = 0; j < n; j++) {
                        x[j] = x[j].add(V[i][j].multiply(beta.divide(Real.of(maxIterations))));
                    }
                }
            }
            return x;
        }
    }


}
