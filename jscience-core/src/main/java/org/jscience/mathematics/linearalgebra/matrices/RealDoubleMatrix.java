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

package org.jscience.mathematics.linearalgebra.matrices;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage;
import org.jscience.mathematics.linearalgebra.matrices.storage.HeapRealDoubleMatrixStorage;
import org.jscience.mathematics.linearalgebra.matrices.storage.DirectRealDoubleMatrixStorage;

import org.jscience.mathematics.numbers.real.Real;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.linearalgebra.LinearAlgebraRegistry;

/**
 * A specialized Matrix implementation for Doubles with SIMD and Native
 * optimization.
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RealDoubleMatrix extends GenericMatrix<Real> {

    private final RealDoubleMatrixStorage doubleStorage;

    /**
     * Internal constructor.
     */
    protected RealDoubleMatrix(RealDoubleMatrixStorage storage) {
        super(storage,
                LinearAlgebraRegistry.getMatrixProvider(org.jscience.mathematics.sets.Reals.getInstance()),
                org.jscience.mathematics.sets.Reals.getInstance());
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

    public Field<Real> getField() {
        return org.jscience.mathematics.sets.Reals.getInstance();
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
        if (doubleStorage instanceof DirectRealDoubleMatrixStorage) {
            return ((DirectRealDoubleMatrixStorage) doubleStorage).getBuffer();
        } else {
            return java.nio.DoubleBuffer.wrap(doubleStorage.toDoubleArray());
        }
    }

    // --- Optimized Operations ---

    @Override
    public Matrix<Real> multiply(Matrix<Real> other) {
        if (other instanceof RealDoubleMatrix) {
            return multiply((RealDoubleMatrix) other);
        }
        // Fallback to generic (slow)
        return super.multiply(other);
    }

    /**
     * Optimized matrix multiplication (GEMM).
     * 
     * @param other the other RealDoubleMatrix
     * @return result as RealDoubleMatrix
     */
    public RealDoubleMatrix multiply(RealDoubleMatrix other) {
        if (this.cols() != other.rows()) {
            throw new IllegalArgumentException("Matrix dimensions mismatch for multiplication");
        }

        int m = this.rows();
        int n = other.cols();
        int p = this.cols();

        // Use Heap storage for result by default for Java access speed
        // Unless both inputs are direct? For now default to Heap for safety.
        HeapRealDoubleMatrixStorage resStorage = new HeapRealDoubleMatrixStorage(m, n);

        // Extremely naive loop for now, providers should replace this
        for (int i = 0; i < m; i++) {
            for (int k = 0; k < p; k++) {
                double valA = this.doubleStorage.getDouble(i, k);
                for (int j = 0; j < n; j++) {
                    double valB = other.doubleStorage.getDouble(k, j);
                    double current = resStorage.getDouble(i, j);
                    resStorage.setDouble(i, j, current + valA * valB);
                }
            }
        }

        return new RealDoubleMatrix(resStorage);
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
}
