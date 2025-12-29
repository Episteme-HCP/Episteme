/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import java.util.List;

/**
 * Symmetric matrix storage - stores only upper triangle.
 * <p>
 * For symmetric matrices A where A[i,j] = A[j,i], this provides
 * 50% memory savings by storing only the upper triangular portion.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SymmetricMatrixStorage<E> implements MatrixStorage<E> {

    private final E[] upperTriangle;
    private final int size;
    private final Field<E> field;

    @SuppressWarnings("unchecked")
    public SymmetricMatrixStorage(List<List<E>> data, Field<E> field) {
        this.size = data.size();
        this.field = field;

        // Calculate size: n*(n+1)/2 elements in upper triangle
        int storageSize = (size * (size + 1)) / 2;
        this.upperTriangle = (E[]) new Object[storageSize];

        // Store upper triangle in row-major order
        int index = 0;
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                upperTriangle[index++] = data.get(i).get(j);
            }
        }
    }

    @Override
    public E get(int row, int col) {
        // Exploit symmetry: A[i,j] = A[j,i]
        if (row > col) {
            int temp = row;
            row = col;
            col = temp;
        }

        // Index in packed upper triangle
        int index = (row * size) - (row * (row - 1)) / 2 + (col - row);
        return upperTriangle[index];
    }

    @Override
    public void set(int row, int col, E value) {
        if (row > col) {
            int temp = row;
            row = col;
            col = temp;
        }

        int index = (row * size) - (row * (row - 1)) / 2 + (col - row);
        upperTriangle[index] = value;
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
    public MatrixStorage<E> clone() {
        @SuppressWarnings("unchecked")
        E[] copy = (E[]) new Object[upperTriangle.length];
        System.arraycopy(upperTriangle, 0, copy, 0, upperTriangle.length);

        // Reconstruct from copy
        SymmetricMatrixStorage<E> cloned = new SymmetricMatrixStorage<>(null, field);
        System.arraycopy(copy, 0, cloned.upperTriangle, 0, copy.length);
        return cloned;
    }

    /**
     * Private constructor for internal use by {@link #clone()}.
     * Creates an empty storage that will be populated by the clone method.
     */
    @SuppressWarnings({ "unchecked", "unused" })
    private SymmetricMatrixStorage(Object dummy, Field<E> field) {
        this.field = field;
        this.size = 0; // Will be set by caller
        this.upperTriangle = (E[]) new Object[0];
    }
}
