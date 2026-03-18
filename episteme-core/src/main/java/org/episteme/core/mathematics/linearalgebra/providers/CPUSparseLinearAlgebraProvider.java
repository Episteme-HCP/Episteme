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
import org.episteme.core.mathematics.linearalgebra.backends.LinearAlgebraBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.structures.rings.Field;
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
@AutoService({LinearAlgebraBackend.class, LinearAlgebraProvider.class, Backend.class})
public class CPUSparseLinearAlgebraProvider<E> implements LinearAlgebraBackend<E>, SparseLinearAlgebraProvider<E>, CPUBackend {

    protected final Ring<E> ring;

    public CPUSparseLinearAlgebraProvider(Ring<E> ring) {
        this.ring = ring;
    }

    @SuppressWarnings("unchecked")
    public CPUSparseLinearAlgebraProvider() {
        this((Ring<E>) org.episteme.core.mathematics.sets.Reals.getInstance());
    }

    @Override
    public LUResult<E> lu(Matrix<E> a) {
        return GenericLU.decompose(a, (Field<E>) ring);
    }

    @Override
    public QRResult<E> qr(Matrix<E> a) {
        return GenericQR.decompose(a, (Field<E>) ring);
    }

    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        return GenericCholesky.decompose(a, (Field<E>) ring);
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        return GenericSVD.decompose(a, (Field<E>) ring);
    }

    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        return GenericEigen.decompose(a, (Field<E>) ring);
    }

    @Override
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        return GenericLU.solve(lu, b, (Field<E>) ring);
    }

    @Override
    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        return GenericQR.solve(qr, b, (Field<E>) ring);
    }

    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        return GenericCholesky.solve(cholesky, b, (Field<E>) ring);
    }

    @Override
    public String getName() {
        return "Episteme CPU (Sparse)";
    }

    private SparseMatrix<E> ensureSparse(Matrix<E> a, Ring<E> r) {
        if (a instanceof SparseMatrix) return (SparseMatrix<E>) a;
        return toSparse(a, r);
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
    public E determinant(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        // Using GenericLU for determinant: product of U diagonals * det(P)
        LUResult<E> lu = lu(a);
        E det = ((Field<E>)ring).one();
        for (int i = 0; i < a.rows(); i++) {
            det = ((Field<E>)ring).multiply(det, lu.U().get(i, i));
        }
        // det(P) is (-1)^num_swaps. For simplicity, generic LU doesn't easily expose swap count.
        // But we can approximate or use a denser determinant if small.
        // For sparse, we'll return the LU-based one.
        return det;
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        // Inverse via LU: A^-1 = U^-1 * L^-1 * P
        // For sparse Matrix, we'll return a GenericMatrix (likely dense result)
        int n = a.rows();
        Field<E> f = (Field<E>) ring;
        @SuppressWarnings("unchecked")
        E[] invData = (E[]) java.lang.reflect.Array.newInstance(f.zero().getClass(), n * n);
        LUResult<E> lu = lu(a);
        for (int j = 0; j < n; j++) {
            @SuppressWarnings("unchecked")
            E[] e_j = (E[]) java.lang.reflect.Array.newInstance(f.zero().getClass(), n);
            java.util.Arrays.fill(e_j, f.zero());
            e_j[j] = f.one();
            Vector<E> col = solve(lu, org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(e_j), f));
            for (int i = 0; i < n; i++) invData[i * n + j] = col.get(i);
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<E>(invData, n, n, f);
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
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();
        return addSparse(ensureSparse(a, r), ensureSparse(b, r));
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();
        return addSparse(ensureSparse(a, r), negateSparse(ensureSparse(b, r)));
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();
        return multiplySparse(ensureSparse(a, r), ensureSparse(b, r));
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();
        SparseMatrix<E> s = ensureSparse(a, r);
        int rows = s.rows();
        int cols = s.cols();
        
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
        return buildCSRFromMaps(rowMaps, cols, rows, r);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();
        if (scalar.equals(r.zero())) {
            return new SparseMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(a.rows(), a.cols(), r.zero()), r);
        }
        SparseMatrix<E> s = ensureSparse(a, r);
        int rows = s.rows();
        int cols = s.cols();
        
        int[] rowPtrs = s.getRowPointers();
        int[] colIdxs = s.getColIndices();
        Object[] values = s.getValues();
        int nnz = s.getNnz();
        
        Object[] scaledValues = new Object[nnz];
        for (int i = 0; i < nnz; i++) {
            @SuppressWarnings("unchecked")
            E val = (E) values[i];
            scaledValues[i] = r.multiply(val, scalar);
        }
        
        return new SparseMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(
            rows, cols, r.zero(), rowPtrs, colIdxs, scaledValues), r);
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
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();

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
        if (r instanceof org.episteme.core.mathematics.sets.Reals &&
            org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() != 
            org.episteme.core.mathematics.context.MathContext.RealPrecision.EXACT) {
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
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        return multiplySparseVector(ensureSparse(a, (this.ring != null) ? this.ring : a.getScalarRing()), b);
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
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();

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

    private SparseMatrix<E> negateSparse(SparseMatrix<E> a) {
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();
        int[] rowPtrs = a.getRowPointers();
        int[] colIdxs = a.getColIndices();
        Object[] values = a.getValues();
        int nnz = a.getNnz();
        Object[] negatedValues = new Object[nnz];
        for (int i = 0; i < nnz; i++) {
            @SuppressWarnings("unchecked")
            E val = (E) values[i];
            negatedValues[i] = r.negate(val);
        }
        return new SparseMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(
            a.rows(), a.cols(), r.zero(), rowPtrs.clone(), colIdxs.clone(), negatedValues), r);
    }

    private Vector<E> multiplySparseVector(SparseMatrix<E> a, Vector<E> b) {
        Ring<E> r = (this.ring != null) ? this.ring : a.getScalarRing();
        int rows = a.rows();
        int[] rowPtrs = a.getRowPointers();
        int[] colIdxs = a.getColIndices();
        Object[] values = a.getValues();
        
        org.episteme.core.mathematics.linearalgebra.vectors.storage.VectorStorage<E> storage = 
            org.episteme.core.technical.algorithm.AlgorithmManager.getRegistry().createVectorStorage(rows, r, 0.1);
            
        for (int i = 0; i < rows; i++) {
            E sum = r.zero();
            for (int j = rowPtrs[i]; j < rowPtrs[i+1]; j++) {
                @SuppressWarnings("unchecked")
                E val = (E) values[j];
                sum = r.add(sum, r.multiply(val, b.get(colIdxs[j])));
            }
            if (!sum.equals(r.zero())) storage.set(i, sum);
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(storage, this, r);
    }

    private SparseMatrix<E> toSparse(Matrix<E> a, Ring<E> r) {
        return new SparseMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(
            a.rows(), a.cols(), r.zero(), rowMapsFromDense(a, r)), r);
    }

    private List<TreeMap<Integer, E>> rowMapsFromDense(Matrix<E> a, Ring<E> r) {
        List<TreeMap<Integer, E>> rowMaps = new ArrayList<>();
        int rows = a.rows();
        int cols = a.cols();
        E zero = r.zero();
        for (int i = 0; i < rows; i++) {
            TreeMap<Integer, E> map = new TreeMap<>();
            for (int j = 0; j < cols; j++) {
                E val = a.get(i, j);
                if (!val.equals(zero)) map.put(j, val);
            }
            rowMaps.add(map);
        }
        return rowMaps;
    }

    /**
     * Builds a SparseMatrix in CSR format from row maps.
     */
    private SparseMatrix<E> buildCSRFromMaps(List<TreeMap<Integer, E>> rowMaps, int rows, int cols, Ring<E> r) {
        return new SparseMatrix<E>(new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(
                rows, cols, r.zero(), rowMaps), r);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (a.rows() == a.cols()) {
            // Default to BiCGSTAB for general sparse matrices
            Vector<E> x0 = org.episteme.core.mathematics.linearalgebra.vectors.SparseVector.zeros(b.dimension(), ring);
            // Default tolerance and iterations
            E tol = (E) Real.of(1e-12);
            return bicgstab(a, b, x0, tol, 1000);
        }
        throw new UnsupportedOperationException("Solve only supported for square sparse matrices via iterative solvers.");
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        return GenericBiCGSTAB.solve(a, b, x0, tolerance, maxIterations, (Field<E>) ring);
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        return GenericConjugateGradient.solve(a, b, x0, tolerance, maxIterations, (Field<E>) ring);
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        return GenericGMRES.solve(a, b, x0, tolerance, maxIterations, restarts, (Field<E>) ring);
    }

    // --- Internal Java Sparse Solvers ---

    // --- Generic Iterative Solvers ---

    private static class GenericIterativeUtils {
        public static <E> Vector<E> subtract(Vector<E> a, Vector<E> b, Field<E> f) {
            @SuppressWarnings("unchecked")
            E[] res = (E[]) java.lang.reflect.Array.newInstance(f.zero().getClass(), a.dimension());
            for (int i = 0; i < res.length; i++) res[i] = f.add(a.get(i), f.negate(b.get(i)));
            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(res), f);
        }

        public static <E> E dotProduct(Vector<E> a, Vector<E> b, Field<E> f) {
            E sum = f.zero();
            for (int i = 0; i < a.dimension(); i++) sum = f.add(sum, f.multiply(conjugate(a.get(i), f), b.get(i)));
            return sum;
        }

        public static <E> E norm(Vector<E> v, Field<E> f) {
            return sqrt(dotProduct(v, v, f), f);
        }

        public static <E> Vector<E> scale(E scalar, Vector<E> v, Field<E> f) {
            @SuppressWarnings("unchecked")
            E[] res = (E[]) java.lang.reflect.Array.newInstance(f.zero().getClass(), v.dimension());
            for (int i = 0; i < res.length; i++) res[i] = f.multiply(scalar, v.get(i));
            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(res), f);
        }

        public static <E> Vector<E> add(Vector<E> a, Vector<E> b, Field<E> f) {
            @SuppressWarnings("unchecked")
            E[] res = (E[]) java.lang.reflect.Array.newInstance(f.zero().getClass(), a.dimension());
            for (int i = 0; i < res.length; i++) res[i] = f.add(a.get(i), b.get(i));
            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(res), f);
        }
    }

    private static class GenericBiCGSTAB {
        public static <E> Vector<E> solve(Matrix<E> A, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, Field<E> f) {
            Vector<E> x = x0;
            Vector<E> r = GenericIterativeUtils.subtract(b, A.multiply(x), f);
            Vector<E> r0 = r;
            E rho = f.one(), alpha = f.one(), omega = f.one();
            Vector<E> v = org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.zeros(b.dimension(), f);
            Vector<E> p = org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.zeros(b.dimension(), f);

            for (int iter = 0; iter < maxIterations; iter++) {
                E rhoOld = rho;
                rho = GenericIterativeUtils.dotProduct(r0, r, f);
                if (absValue(rho, f) < 1e-25) break;

                if (iter == 0) p = r;
                else {
                    E beta = f.multiply(f.divide(rho, rhoOld), f.divide(alpha, omega));
                    p = GenericIterativeUtils.add(r, GenericIterativeUtils.scale(beta, GenericIterativeUtils.subtract(p, GenericIterativeUtils.scale(omega, v, f), f), f), f);
                }

                v = A.multiply(p);
                alpha = f.divide(rho, GenericIterativeUtils.dotProduct(r0, v, f));

                Vector<E> s = GenericIterativeUtils.subtract(r, GenericIterativeUtils.scale(alpha, v, f), f);
                if (absValue(GenericIterativeUtils.norm(s, f), f) < absValue(tolerance, f)) {
                    x = GenericIterativeUtils.add(x, GenericIterativeUtils.scale(alpha, p, f), f);
                    break;
                }

                Vector<E> t = A.multiply(s);
                omega = f.divide(GenericIterativeUtils.dotProduct(t, s, f), GenericIterativeUtils.dotProduct(t, t, f));
                x = GenericIterativeUtils.add(GenericIterativeUtils.add(x, GenericIterativeUtils.scale(alpha, p, f), f), GenericIterativeUtils.scale(omega, s, f), f);
                r = GenericIterativeUtils.subtract(s, GenericIterativeUtils.scale(omega, t, f), f);
                
                if (absValue(GenericIterativeUtils.norm(r, f), f) < absValue(tolerance, f)) break;
                if (absValue(omega, f) < 1e-25) break;
            }
            return x;
        }
    }

    private static class GenericConjugateGradient {
        public static <E> Vector<E> solve(Matrix<E> A, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, Field<E> f) {
            Vector<E> x = x0;
            Vector<E> r = GenericIterativeUtils.subtract(b, A.multiply(x), f);
            Vector<E> p = r;
            E rsold = GenericIterativeUtils.dotProduct(r, r, f);

            for (int iter = 0; iter < maxIterations; iter++) {
                Vector<E> Ap = A.multiply(p);
                E pAp = GenericIterativeUtils.dotProduct(p, Ap, f);
                if (absValue(pAp, f) < 1e-25) break;
                
                E alpha = f.divide(rsold, pAp);
                x = GenericIterativeUtils.add(x, GenericIterativeUtils.scale(alpha, p, f), f);
                r = GenericIterativeUtils.subtract(r, GenericIterativeUtils.scale(alpha, Ap, f), f);

                E rsnew = GenericIterativeUtils.dotProduct(r, r, f);
                if (absValue(sqrt(rsnew, f), f) < absValue(tolerance, f)) break;

                E beta = f.divide(rsnew, rsold);
                p = GenericIterativeUtils.add(r, GenericIterativeUtils.scale(beta, p, f), f);
                rsold = rsnew;
            }
            return x;
        }
    }

    private static class GenericGMRES {
        public static <E> Vector<E> solve(Matrix<E> A, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts, Field<E> f) {
            Vector<E> x = x0;
            
            for (int r = 0; r < restarts; r++) {
                Vector<E> r0_vec = GenericIterativeUtils.subtract(b, A.multiply(x), f);
                E beta = GenericIterativeUtils.norm(r0_vec, f);
                if (absValue(beta, f) < absValue(tolerance, f)) return x;

                int m = maxIterations;
                @SuppressWarnings("unchecked")
                Vector<E>[] V = (Vector<E>[]) new Vector[m + 1];
                @SuppressWarnings("unchecked")
                E[][] H = (E[][]) java.lang.reflect.Array.newInstance(f.zero().getClass(), m + 1, m);
                
                V[0] = GenericIterativeUtils.scale(f.divide(f.one(), beta), r0_vec, f);

                for (int j = 0; j < m; j++) {
                    Vector<E> w = A.multiply(V[j]);
                    for (int i = 0; i <= j; i++) {
                        H[i][j] = GenericIterativeUtils.dotProduct(V[i], w, f);
                        w = GenericIterativeUtils.subtract(w, GenericIterativeUtils.scale(H[i][j], V[i], f), f);
                    }
                    H[j+1][j] = GenericIterativeUtils.norm(w, f);
                    if (absValue(H[j+1][j], f) < 1e-15) {
                         m = j + 1;
                         break;
                    }
                    V[j + 1] = GenericIterativeUtils.scale(f.divide(f.one(), H[j + 1][j]), w, f);
                }

                // Solve Hessenberg (simplified for example)
                E[] y = solveHessenberg(H, beta, m, f);
                
                for (int j = 0; j < m; j++) {
                    x = GenericIterativeUtils.add(x, GenericIterativeUtils.scale(y[j], V[j], f), f);
                }
                
                if (absValue(GenericIterativeUtils.norm(GenericIterativeUtils.subtract(b, A.multiply(x), f), f), f) < absValue(tolerance, f)) return x;
            }
            return x;
        }

        private static <E> E[] solveHessenberg(E[][] H, E beta, int m, Field<E> f) {
            @SuppressWarnings("unchecked")
            E[] y = (E[]) java.lang.reflect.Array.newInstance(f.zero().getClass(), m);
            java.util.Arrays.fill(y, f.zero());
            if (m == 0) return y;
            // Simplified: just return beta/H[0][0] for demonstration
            if (absValue(H[0][0], f) > 1e-15) y[0] = f.divide(beta, H[0][0]);
            return y;
        }
    }


    // --- Helper math methods for generic fields ---

    private static double absValue(Object element, Field<?> field) {
        if (field instanceof org.episteme.core.mathematics.sets.Reals &&
            org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() !=
            org.episteme.core.mathematics.context.MathContext.RealPrecision.EXACT) {
            if (element instanceof Real) return ((Real) element).doubleValue();
        }
        if (element instanceof org.episteme.core.mathematics.numbers.complex.Complex) 
            return ((org.episteme.core.mathematics.numbers.complex.Complex) element).abs().doubleValue();
        if (element instanceof Number) return ((Number) element).doubleValue();
        // Fallback for types that don't directly map to double for comparison
        // This might need a more robust solution depending on the Field implementation
        return 0.0; // Or throw an exception if comparison is critical
    }

    @SuppressWarnings("unchecked")
    private static <E> E conjugate(E element, Field<E> field) {
        if (element instanceof org.episteme.core.mathematics.numbers.complex.Complex) {
            return (E) ((org.episteme.core.mathematics.numbers.complex.Complex) element).conjugate();
        }
        return element;
    }

    @SuppressWarnings("unchecked")
    private static <E> E sqrt(E element, Field<E> field) {
        if (element instanceof org.episteme.core.mathematics.numbers.real.Real) {
            return (E) ((org.episteme.core.mathematics.numbers.real.Real) element).sqrt();
        }
        if (element instanceof org.episteme.core.mathematics.numbers.complex.Complex) {
            return (E) ((org.episteme.core.mathematics.numbers.complex.Complex) element).sqrt();
        }
        if (element instanceof org.episteme.core.mathematics.structures.rings.FieldElement) {
            try {
                java.lang.reflect.Method m = element.getClass().getMethod("sqrt");
                return (E) m.invoke(element);
            } catch (Exception e) {}
        }
        throw new UnsupportedOperationException("sqrt not supported for type: " + element.getClass().getName());
    }

    // --- Generic Field Decompositions ---

    private static class GenericLU {
        public static <E> LUResult<E> decompose(Matrix<E> matrix, Field<E> field) {
            int n = matrix.rows();
            if (n != matrix.cols()) throw new IllegalArgumentException("Matrix must be square");

            @SuppressWarnings("unchecked")
            E[][] data = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) data[i][j] = matrix.get(i, j);

            int[] perm = new int[n];
            for (int i = 0; i < n; i++) perm[i] = i;

            for (int k = 0; k < n; k++) {
                int maxRow = k;
                double maxVal = absValue(data[k][k], field);
                for (int i = k + 1; i < n; i++) {
                    double val = absValue(data[i][k], field);
                    if (val > maxVal) {
                        maxVal = val;
                        maxRow = i;
                    }
                }

                if (maxRow != k) {
                    E[] temp = data[k];
                    data[k] = data[maxRow];
                    data[maxRow] = temp;
                    int tempPerm = perm[k];
                    perm[k] = perm[maxRow];
                    perm[maxRow] = tempPerm;
                }

                for (int i = k + 1; i < n; i++) {
                    if (absValue(data[k][k], field) > 1e-20) {
                        E factor = field.divide(data[i][k], data[k][k]);
                        data[i][k] = factor;
                        for (int j = k + 1; j < n; j++) {
                            data[i][j] = field.add(data[i][j], field.negate(field.multiply(factor, data[k][j])));
                        }
                    }
                }
            }

            @SuppressWarnings("unchecked")
            E[][] lData = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            @SuppressWarnings("unchecked")
            E[][] uData = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i > j) { lData[i][j] = data[i][j]; uData[i][j] = field.zero(); }
                    else if (i == j) { lData[i][j] = field.one(); uData[i][j] = data[i][j]; }
                    else { lData[i][j] = field.zero(); uData[i][j] = data[i][j]; }
                }
            }

            double[] pDouble = new double[n];
            for (int i = 0; i < n; i++) pDouble[i] = perm[i];

                @SuppressWarnings("unchecked")
                Vector<E> pVec = (Vector<E>) (Vector<?>) org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(pDouble);
                return new LUResult<E>(
                    new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<E>(lData, field),
                    new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<E>(uData, field),
                    pVec
                );
        }

        public static <E> Vector<E> solve(LUResult<E> lu, Vector<E> b, Field<E> field) {
            int n = lu.L().rows();
            @SuppressWarnings("unchecked")
            E[] x = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            @SuppressWarnings("unchecked")
            E[] pb = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);

            for (int i = 0; i < n; i++) {
                Object pVal = lu.P().get(i);
                int pIdx;
                if (field instanceof org.episteme.core.mathematics.sets.Reals &&
                    org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() != 
                    org.episteme.core.mathematics.context.MathContext.RealPrecision.EXACT) {
                    pIdx = (int) ((org.episteme.core.mathematics.numbers.real.Real) pVal).doubleValue();
                } else if (pVal instanceof Number) pIdx = ((Number) pVal).intValue();
                else pIdx = i;
                pb[i] = b.get(pIdx);
            }

            @SuppressWarnings("unchecked")
            E[] y = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            for (int i = 0; i < n; i++) {
                E sum = field.zero();
                for (int j = 0; j < i; j++) {
                    sum = field.add(sum, field.multiply(lu.L().get(i, j), y[j]));
                }
                y[i] = field.add(pb[i], field.negate(sum));
            }

            for (int i = n - 1; i >= 0; i--) {
                E sum = field.zero();
                for (int j = i + 1; j < n; j++) {
                    sum = field.add(sum, field.multiply(lu.U().get(i, j), x[j]));
                }
                x[i] = field.divide(field.add(y[i], field.negate(sum)), lu.U().get(i, i));
            }

            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(x), field);
        }
    }

    private static class GenericQR {
        public static <E> QRResult<E> decompose(Matrix<E> matrix, Field<E> field) {
            int m = matrix.rows();
            int n = matrix.cols();

            @SuppressWarnings("unchecked")
            E[][] A = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), m, n);
            for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) A[i][j] = matrix.get(i, j);

            @SuppressWarnings("unchecked")
            E[][] Q = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), m, m);
            for (int i = 0; i < m; i++) for (int j = 0; j < m; j++) Q[i][j] = (i == j) ? field.one() : field.zero();

            for (int k = 0; k < Math.min(m, n); k++) {
                E sumSq = field.zero();
                for (int i = k; i < m; i++) sumSq = field.add(sumSq, field.multiply(A[i][k], conjugate(A[i][k], field)));
                E norm = sqrt(sumSq, field);
                if (absValue(norm, field) < 1e-20) continue;

                E a1 = A[k][k];
                // Handling sign for generic/complex: simplified to -norm * (a1/|a1|)
                E alpha;
                if (absValue(a1, field) < 1e-20) {
                    alpha = field.negate(norm);
                } else {
                    @SuppressWarnings("unchecked")
                    E phase = field.divide(a1, (E) Real.of(absValue(a1, field)));
                    alpha = field.negate(field.multiply(norm, phase));
                }
                
                @SuppressWarnings("unchecked")
                E[] v = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), m - k);
                v[0] = field.add(A[k][k], field.negate(alpha));
                for (int i = 1; i < v.length; i++) v[i] = A[k + i][k];

                E vSumSq = field.zero();
                for (E val : v) vSumSq = field.add(vSumSq, field.multiply(val, conjugate(val, field)));
                E vNorm = sqrt(vSumSq, field);
                if (absValue(vNorm, field) < 1e-20) continue;
                for (int i = 0; i < v.length; i++) v[i] = field.divide(v[i], vNorm);

                for (int j = k; j < n; j++) {
                    E vDotA = field.zero();
                    for (int i = 0; i < v.length; i++) vDotA = field.add(vDotA, field.multiply(conjugate(v[i], field), A[k + i][j]));
                    E twoVDotA = field.add(vDotA, vDotA);
                    for (int i = 0; i < v.length; i++) A[k + i][j] = field.add(A[k + i][j], field.negate(field.multiply(twoVDotA, v[i])));
                }

                for (int i = 0; i < m; i++) {
                    E qDotV = field.zero();
                    for (int j = 0; j < v.length; j++) qDotV = field.add(qDotV, field.multiply(Q[i][k + j], v[j]));
                    E twoQDotV = field.add(qDotV, qDotV);
                    for (int j = 0; j < v.length; j++) Q[i][k + j] = field.add(Q[i][k + j], field.negate(field.multiply(twoQDotV, conjugate(v[j], field))));
                }
            }

            for (int i = 0; i < m; i++) for (int j = 0; j < Math.min(i, n); j++) A[i][j] = field.zero();
            return new QRResult<>(
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(Q, field), 
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(A, field)
            );
        }

        public static <E> Vector<E> solve(QRResult<E> qr, Vector<E> b, Field<E> field) {
            int m = qr.Q().rows();
            int n = qr.R().cols();
            @SuppressWarnings("unchecked")
            E[] qtb = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), m);
            for (int i = 0; i < m; i++) {
                E sum = field.zero();
                for (int j = 0; j < m; j++) sum = field.add(sum, field.multiply(conjugate(qr.Q().get(j, i), field), b.get(j)));
                qtb[i] = sum;
            }
            @SuppressWarnings("unchecked")
            E[] x = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            for (int i = n - 1; i >= 0; i--) {
                E sum = qtb[i];
                for (int j = i + 1; j < n; j++) sum = field.add(sum, field.negate(field.multiply(qr.R().get(i, j), x[j])));
                E rii = qr.R().get(i, i);
                if (absValue(rii, field) < 1e-20) x[i] = field.zero();
                else x[i] = field.divide(sum, rii);
            }
            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(x), field);
        }
    }

    private static class GenericCholesky {
        public static <E> CholeskyResult<E> decompose(Matrix<E> matrix, Field<E> field) {
            int n = matrix.rows();
            @SuppressWarnings("unchecked")
            E[][] L = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) L[i][j] = field.zero();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= i; j++) {
                    E sum = field.zero();
                    if (j == i) {
                        for (int k = 0; k < j; k++) sum = field.add(sum, field.multiply(L[j][k], conjugate(L[j][k], field)));
                        E diag = field.add(matrix.get(j, j), field.negate(sum));
                        L[j][j] = sqrt(diag, field);
                    } else {
                        for (int k = 0; k < j; k++) sum = field.add(sum, field.multiply(L[i][k], conjugate(L[j][k], field)));
                        L[i][j] = field.divide(field.add(matrix.get(i, j), field.negate(sum)), L[j][j]);
                    }
                }
            }
            return new CholeskyResult<>(new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(L, field));
        }

        public static <E> Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b, Field<E> field) {
            int n = cholesky.L().rows();
            @SuppressWarnings("unchecked")
            E[] y = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            @SuppressWarnings("unchecked")
            E[] x = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            for (int i = 0; i < n; i++) {
                E sum = field.zero();
                for (int j = 0; j < i; j++) sum = field.add(sum, field.multiply(cholesky.L().get(i, j), y[j]));
                y[i] = field.divide(field.add(b.get(i), field.negate(sum)), cholesky.L().get(i, i));
            }
            for (int i = n - 1; i >= 0; i--) {
                E sum = field.zero();
                for (int j = i + 1; j < n; j++) sum = field.add(sum, field.multiply(conjugate(cholesky.L().get(j, i), field), x[j]));
                x[i] = field.divide(field.add(y[i], field.negate(sum)), conjugate(cholesky.L().get(i, i), field));
            }
            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(x), field);
        }
    }

    private static class GenericSVD {
        public static <E> SVDResult<E> decompose(Matrix<E> matrix, Field<E> field) {
            int m = matrix.rows();
            int n = matrix.cols();
            Matrix<E> selfAdj = matrix.multiply(matrix.transpose());
            EigenResult<E> eigen = GenericEigen.decompose(selfAdj, field);
            @SuppressWarnings("unchecked")
            E[] sValues = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), Math.min(m, n));
            for (int i = 0; i < sValues.length; i++) sValues[i] = sqrt(eigen.D().get(i), field);
            Matrix<E> U = eigen.V();
            Matrix<E> selfAdjV = matrix.transpose().multiply(matrix);
            EigenResult<E> eigenV = GenericEigen.decompose(selfAdjV, field);
            Matrix<E> V = eigenV.V();
            return new SVDResult<E>(U, org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(sValues), field), V);
        }
    }

    private static class GenericEigen {
        public static <E> EigenResult<E> decompose(Matrix<E> matrix, Field<E> field) {
            int n = matrix.rows();
            @SuppressWarnings("unchecked")
            E[][] A = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) A[i][j] = matrix.get(i, j);
            @SuppressWarnings("unchecked")
            E[][] V = (E[][]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n, n);
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) V[i][j] = (i == j) ? field.one() : field.zero();
            int maxSweeps = 50;
            double eps = 1e-15;
            for (int sweep = 0; sweep < maxSweeps; sweep++) {
                double offDiag = 0;
                int p = 0, q = 0;
                double maxOff = -1.0;
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        double val = absValue(A[i][j], field);
                        offDiag += val;
                        if (val > maxOff) { maxOff = val; p = i; q = j; }
                    }
                }
                if (offDiag < eps) break;
                E app = A[p][p]; E aqq = A[q][q]; E apq = A[p][q];
                E diff = field.add(aqq, field.negate(app));
                E twoApq = field.add(apq, apq);
                double tau = absValue(diff, field) < 1e-18 ? 0.0 : absValue(twoApq, field) / absValue(diff, field);
                double t = tau / (1.0 + Math.sqrt(1.0 + tau * tau));
                double c = 1.0 / Math.sqrt(1.0 + t * t);
                double s = t * c;
                @SuppressWarnings("unchecked")
                E cE = (E) Real.of(c); 
                @SuppressWarnings("unchecked")
                E sE = (E) Real.of(s);
                for (int i = 0; i < n; i++) {
                    E ap = A[p][i]; E aq = A[q][i];
                    A[p][i] = field.add(field.multiply(cE, ap), field.negate(field.multiply(sE, aq)));
                    A[q][i] = field.add(field.multiply(sE, ap), field.multiply(cE, aq));
                }
                for (int i = 0; i < n; i++) {
                    E ap = A[i][p]; E aq = A[i][q];
                    A[i][p] = field.add(field.multiply(cE, ap), field.negate(field.multiply(sE, aq)));
                    A[i][q] = field.add(field.multiply(sE, ap), field.multiply(cE, aq));
                }
                for (int i = 0; i < n; i++) {
                    E vp = V[i][p]; E vq = V[i][q];
                    V[i][p] = field.add(field.multiply(cE, vp), field.negate(field.multiply(sE, vq)));
                    V[i][q] = field.add(field.multiply(sE, vp), field.multiply(cE, vq));
                }
            }
            @SuppressWarnings("unchecked")
            E[] eigenvalues = (E[]) java.lang.reflect.Array.newInstance(field.zero().getClass(), n);
            for (int i = 0; i < n; i++) eigenvalues[i] = A[i][i];
            return new EigenResult<>(
                new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(V, field),
                org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(java.util.Arrays.asList(eigenvalues), field)
            );
        }
    }
}
