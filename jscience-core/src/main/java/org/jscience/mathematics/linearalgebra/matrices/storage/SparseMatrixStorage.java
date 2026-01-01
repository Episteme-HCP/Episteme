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

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Sparse matrix storage using a Map.
 * <p>
 * Stores only non-default elements. Useful when the matrix is large and mostly
 * empty.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SparseMatrixStorage<E> implements MatrixStorage<E> {

    private final Map<Long, E> data;
    private final int rowsCount;
    private final int colsCount;
    private final E zeroValue;

    public SparseMatrixStorage(int rows, int cols, E zeroValue) {
        this.data = new HashMap<>();
        this.rowsCount = rows;
        this.colsCount = cols;
        this.zeroValue = zeroValue;
    }

    private SparseMatrixStorage(Map<Long, E> data, int rows, int cols, E zeroValue) {
        this.data = new HashMap<>(data);
        this.rowsCount = rows;
        this.colsCount = cols;
        this.zeroValue = zeroValue;
    }

    @Override
    public E get(int row, int col) {
        checkBounds(row, col);
        long key = key(row, col);
        return data.getOrDefault(key, zeroValue);
    }

    public int getNnz() {
        return data.size();
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
        return new SparseMatrixStorage<>(data, rowsCount, colsCount, zeroValue);
    }

    public Object[] getValuesCSR() {
        return buildCSR().values;
    }

    public int[] getColIndicesCSR() {
        return buildCSR().colIndices;
    }

    public int[] getRowPointersCSR() {
        return buildCSR().rowPointers;
    }

    private static class CSR {
        Object[] values;
        int[] colIndices;
        int[] rowPointers;
    }

    private CSR cachedCSR;

    private CSR buildCSR() {
        if (cachedCSR != null)
            return cachedCSR;

        // Sort entries by key (row-major order)
        List<Map.Entry<Long, E>> sortedEntries = new java.util.ArrayList<>(data.entrySet());
        sortedEntries.sort(Map.Entry.comparingByKey());

        int nnz = sortedEntries.size();
        Object[] values = new Object[nnz];
        int[] colIndices = new int[nnz];
        int[] rowPointers = new int[rowsCount + 1];

        int idx = 0;
        int currentRow = 0;
        rowPointers[0] = 0;

        for (Map.Entry<Long, E> entry : sortedEntries) {
            long key = entry.getKey();
            int row = (int) (key >>> 32);
            int col = (int) (key & 0xFFFFFFFFL);

            // Fill row pointers for empty rows or jumping rows
            while (currentRow < row) {
                currentRow++;
                rowPointers[currentRow] = idx;
            }

            values[idx] = entry.getValue();
            colIndices[idx] = col;
            idx++;
        }

        // Fill remaining row pointers
        while (currentRow < rowsCount) {
            currentRow++;
            rowPointers[currentRow] = idx;
        }

        cachedCSR = new CSR();
        cachedCSR.values = values;
        cachedCSR.colIndices = colIndices;
        cachedCSR.rowPointers = rowPointers;
        return cachedCSR;
    }

    @Override
    public void set(int row, int col, E value) {
        checkBounds(row, col);
        long key = key(row, col);
        if (value.equals(zeroValue)) {
            data.remove(key);
        } else {
            data.put(key, value);
        }
        cachedCSR = null; // Invalidate cache
    }

    private long key(int row, int col) {
        return ((long) row << 32) | (col & 0xFFFFFFFFL);
    }

    private void checkBounds(int row, int col) {
        if (row < 0 || row >= rowsCount || col < 0 || col >= colsCount) {
            throw new IndexOutOfBoundsException("Matrix index out of bounds: [" + row + "," + col + "]");
        }
    }
}


