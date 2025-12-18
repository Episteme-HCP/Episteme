/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot
 */
package org.jscience.mathematics.linearalgebra.matrices.storage;

/**
 * Storage for tridiagonal matrices.
 * <p>
 * Stores only main diagonal, super-diagonal, and sub-diagonal.
 * </p>
 */
public class TridiagonalMatrixStorage<E> implements MatrixStorage<E> {

    private final E[] lower; // sub-diagonal (n-1)
    private final E[] main; // main diagonal (n)
    private final E[] upper; // super-diagonal (n-1)
    private final int n;
    private final E zero;

    @SuppressWarnings("unchecked")
    public TridiagonalMatrixStorage(int n, E zero) {
        this.n = n;
        this.zero = zero;
        this.lower = (E[]) new Object[n - 1];
        this.main = (E[]) new Object[n];
        this.upper = (E[]) new Object[n - 1];

        for (int i = 0; i < n; i++)
            main[i] = zero;
        for (int i = 0; i < n - 1; i++) {
            lower[i] = zero;
            upper[i] = zero;
        }
    }

    private TridiagonalMatrixStorage(E[] l, E[] m, E[] u, int n, E zero) {
        this.lower = l.clone();
        this.main = m.clone();
        this.upper = u.clone();
        this.n = n;
        this.zero = zero;
    }

    @Override
    public E get(int row, int col) {
        if (row == col)
            return main[row];
        if (row == col + 1)
            return lower[col]; // sub-diagonal, col is smaller
        if (row == col - 1)
            return upper[row]; // super-diagonal, row is smaller
        return zero;
    }

    @Override
    public void set(int row, int col, E value) {
        if (row == col) {
            main[row] = value;
        } else if (row == col + 1) {
            lower[col] = value;
        } else if (row == col - 1) {
            upper[row] = value;
        } else {
            if (!value.equals(zero))
                throw new IllegalArgumentException("Cannot set non-zero value outside tridiagonal bands");
        }
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
        return new TridiagonalMatrixStorage<>(lower, main, upper, n, zero);
    }
}
