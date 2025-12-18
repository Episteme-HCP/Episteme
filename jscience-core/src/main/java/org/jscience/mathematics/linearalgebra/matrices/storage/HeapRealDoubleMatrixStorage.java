package org.jscience.mathematics.linearalgebra.matrices.storage;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.real.RealDouble;

/**
 * Heap-based storage for RealDoubleMatrix using a double[] array.
 */
public class HeapRealDoubleMatrixStorage implements RealDoubleMatrixStorage {

    private final double[] data;
    private final int rows;
    private final int cols;

    public HeapRealDoubleMatrixStorage(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows * cols];
    }

    public HeapRealDoubleMatrixStorage(double[] data, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = data;
    }

    @Override
    public double getDouble(int row, int col) {
        return data[row * cols + col];
    }

    @Override
    public void setDouble(int row, int col, double value) {
        data[row * cols + col] = value;
    }

    @Override
    public double[] toDoubleArray() {
        return data.clone();
    }

    @Override
    public Real get(int row, int col) {
        return RealDouble.create(getDouble(row, col));
    }

    @Override
    public void set(int row, int col, Real value) {
        setDouble(row, col, value.doubleValue());
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int cols() {
        return cols;
    }

    @Override
    public MatrixStorage<Real> clone() {
        return new HeapRealDoubleMatrixStorage(data.clone(), rows, cols);
    }
}
