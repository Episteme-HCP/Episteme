package org.jscience.mathematics.linearalgebra.matrices.storage;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.real.RealDouble;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;

/**
 * Off-heap storage for RealDoubleMatrix using java.nio.DoubleBuffer.
 * Facilitates zero-copy sharing with native libraries.
 */
public class DirectRealDoubleMatrixStorage implements RealDoubleMatrixStorage {

    private final DoubleBuffer buffer;
    private final int rows;
    private final int cols;

    public DirectRealDoubleMatrixStorage(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.buffer = ByteBuffer.allocateDirect(rows * cols * 8)
                .order(ByteOrder.nativeOrder())
                .asDoubleBuffer();
    }

    public DirectRealDoubleMatrixStorage(DoubleBuffer buffer, int rows, int cols) {
        if (!buffer.isDirect()) {
            throw new IllegalArgumentException("Buffer must be direct");
        }
        this.buffer = buffer;
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public double getDouble(int row, int col) {
        return buffer.get(row * cols + col);
    }

    @Override
    public void setDouble(int row, int col, double value) {
        buffer.put(row * cols + col, value);
    }

    @Override
    public double[] toDoubleArray() {
        double[] array = new double[rows * cols];
        buffer.duplicate().get(array); // Zero copy read into array
        return array;
    }

    public DoubleBuffer getBuffer() {
        return buffer;
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
        DirectRealDoubleMatrixStorage copy = new DirectRealDoubleMatrixStorage(rows, cols);
        // define a bulk put
        DoubleBuffer src = this.buffer.duplicate();
        src.rewind();
        copy.buffer.put(src);
        return copy;
    }
}
