package org.jscience.mathematics.linearalgebra.matrices.storage;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Optimized storage for double-precision real matrices.
 * Provides direct access to primitive doubles to avoid boxing overhead.
 */
public interface RealDoubleMatrixStorage extends MatrixStorage<org.jscience.mathematics.numbers.real.Real> {

    /**
     * Gets the value at the specified position as a primitive double.
     */
    double getDouble(int row, int col);

    /**
     * Sets the value at the specified position as a primitive double.
     */
    void setDouble(int row, int col, double value);

    /**
     * Copies the underlying data to a new double array.
     */
    double[] toDoubleArray();
}
