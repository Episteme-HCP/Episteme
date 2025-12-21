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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.mathematics.linearalgebra.matrices.storage;

import org.jscience.mathematics.structures.rings.Field;

/**
 * Diagonal matrix storage - only stores diagonal elements.
 * <p>
 * Provides O(n) storage instead of O(n²) for diagonal matrices.
 * </p>
 * 
 * @param <E> the element type
 * * @author Silvere Martin-Michiellot
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
