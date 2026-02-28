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

package org.episteme.core.mathematics.linearalgebra.matrices.storage;

import java.util.List;

/**
 * Dense row-major matrix storage.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DenseMatrixStorage<E> implements MatrixStorage<E> {

    private final E[] data;
    private final int rowsCount;
    private final int colsCount;

    @SuppressWarnings("unchecked")
    public DenseMatrixStorage(List<List<E>> data) {
        this.rowsCount = data.size();
        this.colsCount = data.isEmpty() ? 0 : data.get(0).size();
        this.data = (E[]) new Object[rowsCount * colsCount];
        for (int i = 0; i < rowsCount; i++) {
            List<E> row = data.get(i);
            int rowOffset = i * colsCount;
            for (int j = 0; j < colsCount; j++) {
                this.data[rowOffset + j] = row.get(j);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public DenseMatrixStorage(int rows, int cols, E initialValue) {
        this.rowsCount = rows;
        this.colsCount = cols;
        this.data = (E[]) new Object[rows * cols];
        if (initialValue != null) {
            java.util.Arrays.fill(this.data, initialValue);
        }
    }

    public DenseMatrixStorage(int rows, int cols, E[] flatData) {
        this.rowsCount = rows;
        this.colsCount = cols;
        this.data = flatData;
    }

    @Override
    public E get(int row, int col) {
        return data[row * colsCount + col];
    }

    @Override
    public void set(int row, int col, E value) {
        data[row * colsCount + col] = value;
    }

    @Override
    public int rows() {
        return rowsCount;
    }

    @Override
    public int cols() {
        return colsCount;
    }

    @Override
    public MatrixStorage<E> clone() {
        return new DenseMatrixStorage<>(rowsCount, colsCount, data.clone());
    }

    public E[] getData() {
        return data;
    }
}



