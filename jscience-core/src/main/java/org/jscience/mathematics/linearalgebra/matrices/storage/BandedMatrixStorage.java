/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot
 */
package org.jscience.mathematics.linearalgebra.matrices.storage;

/**
 * Storage for banded matrices.
 * <p>
 * Stores a set of diagonals defined by lower and upper bandwidths.
 * Storage structure is often row-major rectangular array where rows correspond
 * to diagonals
 * or compact diagonal storage (CDS). Here we use a flattened array for
 * simplicity mapping (i,j).
 * </p>
 */
public class BandedMatrixStorage<E> implements MatrixStorage<E> {

    private final int n;
    private final int lowerBandwidth;
    private final int upperBandwidth;
    private final E[] data;
    private final E zero;

    @SuppressWarnings("unchecked")
    public BandedMatrixStorage(int n, int lowerBandwidth, int upperBandwidth, E zero) {
        this.n = n;
        this.lowerBandwidth = lowerBandwidth;
        this.upperBandwidth = upperBandwidth;
        this.zero = zero;

        // Total bandwidth = kl + ku + 1
        // Size roughly (kl + ku + 1) * n
        // We use a simple indexing: mapping band k to row index in data array
        // data[i][j] where i is diagonal index relative to main, j is column index?
        // Let's use standard BLAS banded storage layout (column-major usually, but we
        // do Java row-major equivalent)
        // Let's align by columns for diagonals.
        // Storing as (kl + ku + 1) rows, n columns.
        // Element a(i,j) stored at data[(ku + i - j) * n + j]
        // Requirement: max(0, j-ku) <= i <= min(n-1, j+kl)

        int bandwidth = lowerBandwidth + upperBandwidth + 1;
        this.data = (E[]) new Object[bandwidth * n];
        for (int i = 0; i < data.length; i++)
            data[i] = zero;
    }

    private BandedMatrixStorage(E[] data, int n, int lower, int upper, E zero) {
        this.data = data.clone();
        this.n = n;
        this.lowerBandwidth = lower;
        this.upperBandwidth = upper;
        this.zero = zero;
    }

    @Override
    public E get(int row, int col) {
        if (col < row - lowerBandwidth || col > row + upperBandwidth) {
            return zero;
        }
        return data[index(row, col)];
    }

    @Override
    public void set(int row, int col, E value) {
        if (col < row - lowerBandwidth || col > row + upperBandwidth) {
            if (!value.equals(zero))
                throw new IllegalArgumentException("Cannot set non-zero value outside bands");
            return;
        }
        data[index(row, col)] = value;
    }

    private int index(int row, int col) {
        // Mapping a(i,j) to storage
        // Row in storage = upperBandwidth + i - j
        // Col in storage = j
        int storageRow = upperBandwidth + row - col;
        return storageRow * n + col;
    }

    @Override
    public int rows() {
        return n;
    }

    @Override
    public int cols() {
        return n;
    }

    @Override
    public MatrixStorage<E> clone() {
        return new BandedMatrixStorage<>(data, n, lowerBandwidth, upperBandwidth, zero);
    }
}
