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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.real.RealDouble;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;

/**
 * Off-heap storage for RealDoubleMatrix using java.nio.DoubleBuffer.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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


