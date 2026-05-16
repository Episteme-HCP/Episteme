/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.matrices.storage;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealDouble;
import org.episteme.nativ.technical.backend.nativ.NativeSegmentProxy;

/**
 * A dense matrix backed by off-heap native memory for 64-bit Reals.
 * <p>
 * This class provides zero-copy access for native libraries (BLAS, LAPACK)
 * via Project Panama's Foreign Function & Memory API.
 */
public class NativeRealDoubleMatrixStorage implements RealDoubleMatrixStorage, NativeSegmentProxy, AutoCloseable {

    private final MemorySegment data;
    private final int rows;
    private final int cols;
    private final Arena arena;
    private final boolean ownsArena;

    public NativeRealDoubleMatrixStorage(int rows, int cols, Arena arena) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
        this.rows = rows;
        this.cols = cols;
        this.arena = arena;
        this.ownsArena = false;
        
        long size = (long) rows * cols * Double.BYTES;
        this.data = arena.allocate(size, ValueLayout.JAVA_DOUBLE.byteAlignment());
        data.fill((byte) 0);
    }

    public NativeRealDoubleMatrixStorage(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
        this.rows = rows;
        this.cols = cols;
        this.arena = Arena.ofAuto();
        this.ownsArena = false;
        
        long size = (long) rows * cols * Double.BYTES;
        this.data = arena.allocate(size, ValueLayout.JAVA_DOUBLE.byteAlignment());
        data.fill((byte) 0);
    }

    public NativeRealDoubleMatrixStorage(MemorySegment data, int rows, int cols, Arena arena) {
        this.data = data;
        this.rows = rows;
        this.cols = cols;
        this.arena = arena;
        this.ownsArena = false;
    }

    @Override public MemorySegment segment() { return data; }
    @Override public Arena arena() { return arena; }

    @Override public int rows() { return rows; }
    @Override public int cols() { return cols; }

    @Override
    public Real get(int row, int col) {
        return RealDouble.create(getDouble(row, col));
    }

    @Override
    public void set(int row, int col, Real value) {
        setDouble(row, col, value.doubleValue());
    }

    @Override
    public java.nio.DoubleBuffer getBuffer() {
        return data.asByteBuffer().asDoubleBuffer();
    }

    @Override
    public double[] toDoubleArray() {
        return toArray();
    }

    @Override
    public NativeRealDoubleMatrixStorage clone() {
        NativeRealDoubleMatrixStorage copy = new NativeRealDoubleMatrixStorage(rows, cols);
        MemorySegment.copy(this.data, 0, copy.data, 0, sizeBytes());
        return copy;
    }

    public long sizeBytes() {
        return (long) rows * cols * Double.BYTES;
    }

    @Override
    public double getDouble(int row, int col) {
        checkBounds(row, col);
        long offset = ((long) row * cols + col) * Double.BYTES;
        return data.get(ValueLayout.JAVA_DOUBLE, offset);
    }

    @Override
    public void setDouble(int row, int col, double value) {
        checkBounds(row, col);
        long offset = ((long) row * cols + col) * Double.BYTES;
        data.set(ValueLayout.JAVA_DOUBLE, offset, value);
    }

    public void setAll(double[] values) {
        if (values.length != rows * cols) {
            throw new IllegalArgumentException("Array size mismatch");
        }
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_DOUBLE, 0, values.length);
    }

    public double[] toArray() {
        double[] result = new double[rows * cols];
        MemorySegment.copy(data, ValueLayout.JAVA_DOUBLE, 0, result, 0, result.length);
        return result;
    }

    public int leadingDimension() {
        return cols;
    }

    public NativeRealDoubleMatrixStorage reshape(int newRows, int newCols) {
        if ((long) newRows * newCols != (long) rows * cols) {
            throw new IllegalArgumentException("Total number of elements must remain the same");
        }
        return new NativeRealDoubleMatrixStorage(data, newRows, newCols, arena);
    }

    public NativeRealDoubleMatrixStorage transpose() {
        NativeRealDoubleMatrixStorage result = new NativeRealDoubleMatrixStorage(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.setDouble(j, i, this.getDouble(i, j));
            }
        }
        return result;
    }

    private void checkBounds(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
    }

    @Override
    public void close() {
        // Arena.ofAuto() managed.
    }
}


