package org.jscience.mathematics.linearalgebra.vectors.storage;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Heap-based implementation of RealDoubleVectorStorage using a double array.
 */
public class HeapRealDoubleVectorStorage implements RealDoubleVectorStorage {

    private final double[] data;
    private final int dimension;

    public HeapRealDoubleVectorStorage(int dimension) {
        this.dimension = dimension;
        this.data = new double[dimension];
    }

    public HeapRealDoubleVectorStorage(double[] data) {
        this.dimension = data.length;
        this.data = data;
    }

    @Override
    public int dimension() {
        return dimension;
    }

    @Override
    public Real get(int i) {
        return Real.of(data[i]);
    }

    @Override
    public void set(int i, Real value) {
        data[i] = value.doubleValue();
    }

    @Override
    public double getDouble(int index) {
        return data[index];
    }

    @Override
    public void setDouble(int index, double value) {
        data[index] = value;
    }

    @Override
    public double[] toDoubleArray() {
        // Return internal array copy or reference?
        // For safety usually copy, but for speed reference?
        // Let's implement copy to be safe, or just generic clone()
        return data.clone();
    }

    @Override
    public VectorStorage<Real> copy() {
        return new HeapRealDoubleVectorStorage(data.clone());
    }
}
