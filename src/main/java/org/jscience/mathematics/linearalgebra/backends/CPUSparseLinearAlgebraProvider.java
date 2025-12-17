/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.linearalgebra.backends;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.SparseMatrix;
import org.jscience.mathematics.structures.rings.Field;

/**
 * Linear Algebra Provider for Sparse Matrices (CPU).
 * <p>
 * Optimized for SparseMatrix implementations.
 * Uses CSR-based algorithms that only process non-zero elements.
 * </p>
 * 
 * @param <E> the type of scalar elements
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CPUSparseLinearAlgebraProvider<E> extends CPUDenseLinearAlgebraProvider<E> {

    public CPUSparseLinearAlgebraProvider(Field<E> field) {
        super(field);
    }

    private static final int PARALLEL_THRESHOLD = 500; // Lower threshold for sparse logic overhead

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (a instanceof SparseMatrix && b instanceof SparseMatrix) {
            return addSparse((SparseMatrix<E>) a, (SparseMatrix<E>) b);
        }
        return super.add(a, b);
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
                        E sum = field.add(existing, bVal);
                        if (!sum.equals(field.zero())) {
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
            IntStream.range(0, rows).parallel().forEach(row -> {
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
                    // Use a temporary variable to avoid map lookup race conditions?
                    // No, rowMap is unique per thread (per row).
                    if (existing != null) {
                        E sum = field.add(existing, bVal);
                        if (!sum.equals(field.zero())) {
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
        return buildCSRFromMaps(rowMaps, rows, cols);
    }

    @SuppressWarnings("unchecked")
    private void computeRowMultiplication(int i, TreeMap<Integer, E> rowMap,
            int[] aRowPtrs, int[] aCols, Object[] aVals,
            int[] bRowPtrs, int[] bCols, Object[] bVals) {

        for (int aIdx = aRowPtrs[i]; aIdx < aRowPtrs[i + 1]; aIdx++) {
            int k = aCols[aIdx];
            E aVal = (E) aVals[aIdx];

            // Multiply A(i,k) by each non-zero in row k of B
            for (int bIdx = bRowPtrs[k]; bIdx < bRowPtrs[k + 1]; bIdx++) {
                int j = bCols[bIdx];
                E bVal = (E) bVals[bIdx];
                E product = field.multiply(aVal, bVal);

                E existing = rowMap.get(j);
                if (existing != null) {
                    rowMap.put(j, field.add(existing, product));
                } else {
                    rowMap.put(j, product);
                }
            }
        }

        // Remove zeros
        rowMap.entrySet().removeIf(e -> e.getValue().equals(field.zero()));
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a instanceof SparseMatrix && b instanceof SparseMatrix) {
            return multiplySparse((SparseMatrix<E>) a, (SparseMatrix<E>) b);
        }
        return super.multiply(a, b);
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
                computeRowMultiplication(i, rowMaps.get(i), aRowPtrs, aCols, aVals, bRowPtrs, bCols, bVals);
            }
        } else {
            // Parallel
            IntStream.range(0, resultRows).parallel().forEach(i -> {
                computeRowMultiplication(i, rowMaps.get(i), aRowPtrs, aCols, aVals, bRowPtrs, bCols, bVals);
            });
        }

        return buildCSRFromMaps(rowMaps, resultRows, resultCols);
    }

    /**
     * Builds a SparseMatrix in CSR format from row maps.
     */
    private SparseMatrix<E> buildCSRFromMaps(List<TreeMap<Integer, E>> rowMaps, int rows, int cols) {
        // Create storage
        E zero = field.zero();
        org.jscience.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = new org.jscience.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(
                rows, cols, zero);

        // Populate directly
        for (int row = 0; row < rows; row++) {
            for (Map.Entry<Integer, E> entry : rowMaps.get(row).entrySet()) {
                storage.set(row, entry.getKey(), entry.getValue());
            }
        }

        return new SparseMatrix<>(storage, field);
    }
}
