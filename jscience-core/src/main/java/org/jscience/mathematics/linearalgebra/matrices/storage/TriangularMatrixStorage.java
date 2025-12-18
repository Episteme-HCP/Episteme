/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot
 */
package org.jscience.mathematics.linearalgebra.matrices.storage;

/**
 * Storage for lower or upper triangular matrices.
 * <p>
 * Saves memory by storing only the non-zero triangle.
 * </p>
 */
public class TriangularMatrixStorage<E> implements MatrixStorage<E> {

    private final E[] data;
    private final int n;
    private final boolean upper;
    private final E zero;

    @SuppressWarnings("unchecked")
    public TriangularMatrixStorage(int n, boolean upper, E zero) {
        this.n = n;
        this.upper = upper;
        this.zero = zero;
        // Size = n*(n+1)/2
        this.data = (E[]) new Object[n * (n + 1) / 2];
        // Initialize with zero? Or assumed null until set?
        // Let's fill with zero for safety if E is not nullable primitive wrapper
        for (int i = 0; i < data.length; i++) {
            data[i] = zero;
        }
    }

    // Copy constructor
    private TriangularMatrixStorage(E[] data, int n, boolean upper, E zero) {
        this.data = data.clone();
        this.n = n;
        this.upper = upper;
        this.zero = zero;
    }

    @Override
    public E get(int row, int col) {
        if (upper) {
            if (row > col)
                return zero;
            return data[indexUpper(row, col)];
        } else {
            if (col > row)
                return zero;
            return data[indexLower(row, col)];
        }
    }

    @Override
    public void set(int row, int col, E value) {
        if (upper) {
            if (row > col) {
                if (!value.equals(zero))
                    throw new IllegalArgumentException("Cannot set non-zero value in zero region of Triangular Matrix");
                return;
            }
            data[indexUpper(row, col)] = value;
        } else {
            if (col > row) {
                if (!value.equals(zero))
                    throw new IllegalArgumentException("Cannot set non-zero value in zero region of Triangular Matrix");
                return;
            }
            data[indexLower(row, col)] = value;
        }
    }

    private int indexLower(int row, int col) {
        // Row major lower triangle:
        // i*(i+1)/2 + j
        return row * (row + 1) / 2 + col;
    }

    private int indexUpper(int row, int col) {
        // Row major upper triangle is tricky.
        // Often easier to map to lower logic or use standard formula:
        // For upper, index = i*N + j - sum(0..i)
        // = i*N + j - i*(i+1)/2
        // But verifying bounds is key.
        return row * n + col - (row * (row + 1) / 2);
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
        return new TriangularMatrixStorage<>(data, n, upper, zero);
    }
}
