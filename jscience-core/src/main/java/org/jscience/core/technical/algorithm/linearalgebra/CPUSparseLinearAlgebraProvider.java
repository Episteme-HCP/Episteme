/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.linearalgebra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.jscience.core.mathematics.structures.rings.Ring;

/**
 * Linear Algebra Provider for Sparse Matrices (CPU).
 * Refactored to align with the new technical algorithm architecture.
 */
public class CPUSparseLinearAlgebraProvider<E> extends CPUDenseLinearAlgebraProvider<E> {

    public CPUSparseLinearAlgebraProvider(Ring<E> ring) {
        super(ring);
    }

    public CPUSparseLinearAlgebraProvider() {
        super(null);
    }


    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (a instanceof SparseMatrix && b instanceof SparseMatrix) {
            return addSparse((SparseMatrix<E>) a, (SparseMatrix<E>) b);
        }
        return super.add(a, b);
    }

    @SuppressWarnings("unchecked")
    private SparseMatrix<E> addSparse(SparseMatrix<E> a, SparseMatrix<E> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) {
            throw new IllegalArgumentException("Matrix dimensions must match");
        }

        int rows = a.rows();
        int cols = a.cols();
        List<TreeMap<Integer, E>> rowMaps = new ArrayList<>();
        for (int i = 0; i < rows; i++) rowMaps.add(new TreeMap<>());

        Object[] aVals = a.getValues();
        int[] aCols = a.getColIndices();
        int[] aRowPtrs = a.getRowPointers();
        Object[] bVals = b.getValues();
        int[] bCols = b.getColIndices();
        int[] bRowPtrs = b.getRowPointers();

        IntStream.range(0, rows).parallel().forEach(row -> {
            TreeMap<Integer, E> rowMap = rowMaps.get(row);
            for (int i = aRowPtrs[row]; i < aRowPtrs[row + 1]; i++) {
                rowMap.put(aCols[i], (E) aVals[i]);
            }
            for (int i = bRowPtrs[row]; i < bRowPtrs[row + 1]; i++) {
                int col = bCols[i];
                E bVal = (E) bVals[i];
                E existing = rowMap.get(col);
                if (existing != null) {
                    E sum = ring.add(existing, bVal);
                    if (!sum.equals(ring.zero())) rowMap.put(col, sum);
                    else rowMap.remove(col);
                } else {
                    rowMap.put(col, bVal);
                }
            }
        });

        return buildCSRFromMaps(rowMaps, rows, cols);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a instanceof SparseMatrix && b instanceof SparseMatrix) {
            return multiplySparse((SparseMatrix<E>) a, (SparseMatrix<E>) b);
        }
        return super.multiply(a, b);
    }

    @SuppressWarnings("unchecked")
    private SparseMatrix<E> multiplySparse(SparseMatrix<E> a, SparseMatrix<E> b) {
        int resultRows = a.rows();
        int resultCols = b.cols();
        List<TreeMap<Integer, E>> rowMaps = new ArrayList<>();
        for (int i = 0; i < resultRows; i++) rowMaps.add(new TreeMap<>());

        Object[] aVals = a.getValues();
        int[] aCols = a.getColIndices();
        int[] aRowPtrs = a.getRowPointers();
        Object[] bVals = b.getValues();
        int[] bCols = b.getColIndices();
        int[] bRowPtrs = b.getRowPointers();

        IntStream.range(0, resultRows).parallel().forEach(i -> {
            TreeMap<Integer, E> rowMap = rowMaps.get(i);
            for (int aIdx = aRowPtrs[i]; aIdx < aRowPtrs[i + 1]; aIdx++) {
                int k = aCols[aIdx];
                E aVal = (E) aVals[aIdx];
                for (int bIdx = bRowPtrs[k]; bIdx < bRowPtrs[k + 1]; bIdx++) {
                    int j = bCols[bIdx];
                    E bVal = (E) bVals[bIdx];
                    E product = ring.multiply(aVal, bVal);
                    E existing = rowMap.get(j);
                    if (existing != null) rowMap.put(j, ring.add(existing, product));
                    else rowMap.put(j, product);
                }
            }
            rowMap.entrySet().removeIf(e -> e.getValue().equals(ring.zero()));
        });

        return buildCSRFromMaps(rowMaps, resultRows, resultCols);
    }

    private SparseMatrix<E> buildCSRFromMaps(List<TreeMap<Integer, E>> rowMaps, int rows, int cols) {
        org.jscience.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.jscience.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, ring.zero());
        for (int row = 0; row < rows; row++) {
            for (Map.Entry<Integer, E> entry : rowMaps.get(row).entrySet()) {
                storage.set(row, entry.getKey(), entry.getValue());
            }
        }
        return new SparseMatrix<E>(storage, ring);
    }

    @Override
    public String getName() {
        return "Java CPU (Sparse)";
    }
}
