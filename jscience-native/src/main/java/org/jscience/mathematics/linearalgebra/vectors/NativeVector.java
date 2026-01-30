/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.mathematics.linearalgebra.vectors;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.DoubleBuffer;
import org.jscience.mathematics.linearalgebra.vectors.storage.RealDoubleVectorStorage;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.matrices.NativeMatrix;

/**
 * A dense vector backed by off-heap native memory.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class NativeVector implements RealDoubleVectorStorage, AutoCloseable {

    private final MemorySegment data;
    private final int dimension;
    private final Arena arena;
    private final boolean ownsArena;

    public NativeVector(int dimension, Arena arena) {
        this.dimension = dimension;
        this.arena = arena;
        this.ownsArena = false;
        this.data = arena.allocate((long) dimension * Double.BYTES, ValueLayout.JAVA_DOUBLE.byteAlignment());
        data.fill((byte) 0);
    }

    public NativeVector(int dimension) {
        this.dimension = dimension;
        this.arena = Arena.ofConfined();
        this.ownsArena = true;
        this.data = arena.allocate((long) dimension * Double.BYTES, ValueLayout.JAVA_DOUBLE.byteAlignment());
        data.fill((byte) 0);
    }

    public MemorySegment segment() {
        return data;
    }

    @Override
    public int dimension() {
        return dimension;
    }

    @Override
    public Real get(int index) {
        return Real.of(getDouble(index));
    }

    @Override
    public void set(int index, Real value) {
        setDouble(index, value.doubleValue());
    }

    @Override
    public double getDouble(int index) {
        return data.get(ValueLayout.JAVA_DOUBLE, (long) index * Double.BYTES);
    }

    @Override
    public void setDouble(int index, double value) {
        data.set(ValueLayout.JAVA_DOUBLE, (long) index * Double.BYTES, value);
    }

    @Override
    public double[] toDoubleArray() {
        double[] result = new double[dimension];
        MemorySegment.copy(data, ValueLayout.JAVA_DOUBLE, 0, result, 0, dimension);
        return result;
    }

    @Override
    public DoubleBuffer getBuffer() {
        return data.asByteBuffer().asDoubleBuffer();
    }

    @Override
    public void close() {
        if (ownsArena) {
            arena.close();
        }
    }

    /**
     * Views this vector as a matrix.
     */
    public NativeMatrix asMatrix(int rows, int cols) {
        if ((long) rows * cols != dimension) {
            throw new IllegalArgumentException("Dimension mismatch");
        }
        return new NativeMatrix(data, rows, cols, arena);
    }

    @Override
    public org.jscience.mathematics.linearalgebra.vectors.storage.VectorStorage<Real> copy() {
        NativeVector copy = new NativeVector(dimension);
        MemorySegment.copy(this.data, 0, copy.data, 0, (long) dimension * Double.BYTES);
        return copy;
    }
}
