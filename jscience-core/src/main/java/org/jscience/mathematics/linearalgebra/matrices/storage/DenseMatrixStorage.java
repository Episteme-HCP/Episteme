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

import java.util.ArrayList;
import java.util.List;

/**
 * Dense row-major matrix storage.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DenseMatrixStorage<E> implements MatrixStorage<E> {

    private final List<List<E>> data;
    private final int rowsCount;
    private final int colsCount;

    public DenseMatrixStorage(List<List<E>> data) {
        this.data = new ArrayList<>();
        for (List<E> row : data) {
            this.data.add(new ArrayList<>(row));
        }
        this.rowsCount = data.size();
        this.colsCount = data.isEmpty() ? 0 : data.get(0).size();
    }

    public DenseMatrixStorage(int rows, int cols, E initialValue) {
        this.data = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<E> row = new ArrayList<>(cols);
            for (int j = 0; j < cols; j++) {
                row.add(initialValue);
            }
            this.data.add(row);
        }
        this.rowsCount = rows;
        this.colsCount = cols;
    }

    public DenseMatrixStorage(int rows, int cols, E[] flatData) {
        this.data = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<E> row = new ArrayList<>(cols);
            for (int j = 0; j < cols; j++) {
                row.add(flatData[i * cols + j]);
            }
            this.data.add(row);
        }
        this.rowsCount = rows;
        this.colsCount = cols;
    }

    @Override
    public E get(int row, int col) {
        return data.get(row).get(col);
    }

    @Override
    public void set(int row, int col, E value) {
        data.get(row).set(col, value);
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
        return new DenseMatrixStorage<>(data);
    }

    public List<List<E>> getData() {
        return data;
    }
}


