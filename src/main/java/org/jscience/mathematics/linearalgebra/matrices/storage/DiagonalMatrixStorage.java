package org.jscience.mathematics.linearalgebra.matrices.storage;

import org.jscience.mathematics.structures.rings.Field;

/**
 * Diagonal matrix storage - only stores diagonal elements.
 * <p>
 * Provides O(n) storage instead of O(n²) for diagonal matrices.
 * </p>
 * 
 * @param <E> the element type
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DiagonalMatrixStorage<E> implements MatrixStorage<E> {

    private final E[] diagonal;
    private final E zero;
    private final int size;

    @SuppressWarnings("unchecked")
    public DiagonalMatrixStorage(E[] diagonal, Field<E> field) {
        this.diagonal = (E[]) new Object[diagonal.length];
        System.arraycopy(diagonal, 0, this.diagonal, 0, diagonal.length);
        this.zero = field.zero();
        this.size = diagonal.length;
    }

    @Override
    public E get(int row, int col) {
        if (row == col && row < size) {
            return diagonal[row];
        }
        return zero;
    }

    @Override
    public void set(int row, int col, E value) {
        if (row == col && row < size) {
            diagonal[row] = value;
        } else if (!value.equals(zero)) {
            throw new UnsupportedOperationException(
                    "Cannot set non-diagonal element in DiagonalStorage");
        }
    }

    @Override
    public int rows() {
        return size;
    }

    @Override
    public int cols() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MatrixStorage<E> clone() {
        E[] copy = (E[]) new Object[size];
        System.arraycopy(diagonal, 0, copy, 0, size);
        return new DiagonalMatrixStorage<>(copy, null); // Field not needed for clone
    }

    public E[] getDiagonal() {
        return diagonal;
    }
}
