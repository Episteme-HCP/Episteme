package org.jscience.mathematics.linearalgebra.matrices.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Dense row-major matrix storage.
 * 
 * @param <E> the element type
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
