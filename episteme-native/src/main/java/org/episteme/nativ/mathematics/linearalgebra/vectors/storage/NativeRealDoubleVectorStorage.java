/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.vectors.storage;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.DoubleBuffer;
import org.episteme.core.mathematics.linearalgebra.vectors.storage.RealDoubleVectorStorage;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.nativ.technical.backend.nativ.NativeSegmentProxy;

/**
 * A dense vector backed by off-heap native memory for 64-bit Reals.
 */
public class NativeRealDoubleVectorStorage implements RealDoubleVectorStorage, NativeSegmentProxy, AutoCloseable {

    private final MemorySegment data;
    private final int dimension;
    private final Arena arena;
    public NativeRealDoubleVectorStorage(int dimension, Arena arena) {
        this.dimension = dimension;
        this.arena = arena;
        this.data = org.episteme.nativ.technical.backend.nativ.NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, dimension);
        data.fill((byte) 0);
    }

    public NativeRealDoubleVectorStorage(int dimension) {
        this.dimension = dimension;
        this.arena = Arena.ofAuto();
        this.data = org.episteme.nativ.technical.backend.nativ.NativeSafe.allocate(arena, ValueLayout.JAVA_DOUBLE, dimension);
        data.fill((byte) 0);
    }

    public NativeRealDoubleVectorStorage(MemorySegment data, int dimension, Arena arena) {
        this.data = data;
        this.dimension = dimension;
        this.arena = arena;
    }

    @Override public MemorySegment segment() { return data; }
    @Override public Arena arena() { return arena; }

    @Override public int dimension() { return dimension; }

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
        // Arena.ofAuto() managed.
    }

    @Override
    public org.episteme.core.mathematics.linearalgebra.vectors.storage.VectorStorage<Real> copy() {
        NativeRealDoubleVectorStorage copy = new NativeRealDoubleVectorStorage(dimension);
        MemorySegment.copy(this.data, 0, copy.data, 0, (long) dimension * Double.BYTES);
        return copy;
    }
}


