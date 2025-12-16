package org.jscience.mathematics.linearalgebra.vectors.storage;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for optimized double-based vector storage.
 */
public interface RealDoubleVectorStorage extends VectorStorage<Real> {

    /**
     * Gets the double value at the specified index.
     */
    double getDouble(int index);

    /**
     * Sets the double value at the specified index.
     */
    void setDouble(int index, double value);

    /**
     * Returns the underlying double array if available (optional operation).
     */
    double[] toDoubleArray();
}
