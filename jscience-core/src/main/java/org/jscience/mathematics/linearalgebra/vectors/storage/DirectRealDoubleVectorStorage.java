package org.jscience.mathematics.linearalgebra.vectors.storage;

import org.jscience.mathematics.numbers.real.Real;
import java.nio.DoubleBuffer;

/**
 * Direct buffer implementation of RealDoubleVectorStorage.
 * Used for interop with native libraries or GPU buffers.
 */
public class DirectRealDoubleVectorStorage implements RealDoubleVectorStorage {

    private final DoubleBuffer buffer;
    private final int dimension;

    public DirectRealDoubleVectorStorage(int dimension) {
        this.dimension = dimension;
        this.buffer = java.nio.ByteBuffer.allocateDirect(dimension * 8).asDoubleBuffer();
    }

    public DirectRealDoubleVectorStorage(DoubleBuffer buffer) {
        this.dimension = buffer.capacity();
        this.buffer = buffer;
    }

    @Override
    public int dimension() {
        return dimension;
    }

    @Override
    public Real get(int i) {
        return Real.of(buffer.get(i));
    }

    @Override
    public void set(int i, Real value) {
        buffer.put(i, value.doubleValue());
    }

    @Override
    public double getDouble(int index) {
        return buffer.get(index);
    }

    @Override
    public void setDouble(int index, double value) {
        buffer.put(index, value);
    }

    @Override
    public double[] toDoubleArray() {
        double[] arr = new double[dimension];
        // Duplicate buffer to avoid moving position of original
        DoubleBuffer dup = buffer.duplicate();
        dup.rewind();
        dup.get(arr);
        return arr;
    }

    @Override
    public VectorStorage<Real> copy() {
        DoubleBuffer newBuf = java.nio.ByteBuffer.allocateDirect(dimension * 8).asDoubleBuffer();
        DoubleBuffer dup = buffer.duplicate();
        dup.rewind();
        newBuf.put(dup);
        return new DirectRealDoubleVectorStorage(newBuf);
    }
}
