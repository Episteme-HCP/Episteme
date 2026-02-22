/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.core.mathematics.linearalgebra.matrices;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.HeapRealDoubleMatrixStorage;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.DirectRealDoubleMatrixStorage;

import org.jscience.core.mathematics.numbers.real.Real;

import org.jscience.core.mathematics.structures.rings.Field;
import org.jscience.core.ComputeContext;

/**
 * A specialized Matrix implementation for Doubles with SIMD and Native
 * optimization.
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RealDoubleMatrix extends GenericMatrix<Real> implements AutoCloseable {

    private final RealDoubleMatrixStorage doubleStorage;

    /**
     * Internal constructor.
     */
    protected RealDoubleMatrix(RealDoubleMatrixStorage storage) {
        super(storage,
                ComputeContext.current().getLinearAlgebraProvider(org.jscience.core.mathematics.sets.Reals.getInstance()),
                org.jscience.core.mathematics.sets.Reals.getInstance());
        this.doubleStorage = storage;
    }

    /**
     * Creates a RealDoubleMatrix from a 2D double array (Heap Storage).
     * 
     * @param values the values
     * @return the matrix
     */
    public static RealDoubleMatrix of(double[][] values) {
        int rows = values.length;
        int cols = rows > 0 ? values[0].length : 0;
        double[] data = new double[rows * cols];

        for (int i = 0; i < rows; i++) {
            System.arraycopy(values[i], 0, data, i * cols, cols);
        }

        return new RealDoubleMatrix(new HeapRealDoubleMatrixStorage(data, rows, cols));
    }

    /**
     * Creates a RealDoubleMatrix from a flattened double array.
     */
    public static RealDoubleMatrix of(double[] data, int rows, int cols) {
        return new RealDoubleMatrix(new HeapRealDoubleMatrixStorage(data.clone(), rows, cols));
    }

    /**
     * Creates a RealDoubleMatrix intended for direct/native usage.
     */
    public static RealDoubleMatrix direct(int rows, int cols) {
        return new RealDoubleMatrix(new DirectRealDoubleMatrixStorage(rows, cols));
    }

    /**
     * Creates a RealDoubleMatrix from a specialized storage.
     */
    public static RealDoubleMatrix of(RealDoubleMatrixStorage storage) {
        return new RealDoubleMatrix(storage);
    }

    public Field<Real> getField() {
        return org.jscience.core.mathematics.sets.Reals.getInstance();
    }

    public boolean isDirect() {
        return doubleStorage instanceof DirectRealDoubleMatrixStorage;
    }

    public RealDoubleMatrixStorage getDoubleStorage() {
        return doubleStorage;
    }

    public void set(int row, int col, double value) {
        doubleStorage.setDouble(row, col, value);
    }

    public java.nio.DoubleBuffer getBuffer() {
        return doubleStorage.getBuffer();
    }

    /**
     * Returns the elements of this matrix as a flattened double array.
     */
    public double[] toDoubleArray() {
        return doubleStorage.toDoubleArray();
    }

    /**
     * Returns the elements of this matrix as a 2D double array.
     */
    public double[][] to2DDoubleArray() {
        int rows = rows();
        int cols = cols();
        double[][] res = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res[i][j] = doubleStorage.getDouble(i, j);
            }
        }
        return res;
    }

    // --- Optimized Operations ---

    @Override
    public Matrix<Real> multiply(Matrix<Real> other) {
        if (other instanceof RealDoubleMatrix) {
            return multiply((RealDoubleMatrix) other);
        }
        return provider.multiply(this, other);
    }

    /**
     * Optimized matrix multiplication (GEMM).
     */
    public RealDoubleMatrix multiply(RealDoubleMatrix other) {
        Matrix<Real> result = provider.multiply(this, other);
        if (result instanceof RealDoubleMatrix) {
            return (RealDoubleMatrix) result;
        }
        // Conversion if provider returned a GenericMatrix or other type
        double[][] data = new double[result.rows()][result.cols()];
        for (int i = 0; i < result.rows(); i++) {
            for (int j = 0; j < result.cols(); j++) {
                data[i][j] = result.get(i, j).doubleValue();
            }
        }
        return RealDoubleMatrix.of(data);
    }

    @Override
    public Matrix<Real> add(Matrix<Real> other) {
        if (other instanceof RealDoubleMatrix) {
            RealDoubleMatrix b = (RealDoubleMatrix) other;
            if (this.rows() != b.rows() || this.cols() != b.cols()) {
                throw new IllegalArgumentException("Dimensions mismatch");
            }
            // Result type matches this type (Heap vs Direct)
            RealDoubleMatrixStorage resStorage;
            if (this.isDirect()) {
                resStorage = new DirectRealDoubleMatrixStorage(rows(), cols());
            } else {
                resStorage = new HeapRealDoubleMatrixStorage(rows(), cols());
            }

            for (int i = 0; i < rows(); i++) {
                for (int j = 0; j < cols(); j++) {
                    resStorage.setDouble(i, j, this.doubleStorage.getDouble(i, j) + b.doubleStorage.getDouble(i, j));
                }
            }
            return new RealDoubleMatrix(resStorage);
        }
        return super.add(other);
    }
    @Override
    public Matrix<Real> inverse() {
        return provider.inverse(this);
    }
    
    @Override
    public Vector<Real> multiply(Vector<Real> vector) {
        return provider.multiply(this, vector);
    }

    @Override
    public void close() {
        doubleStorage.close();
    }
}



