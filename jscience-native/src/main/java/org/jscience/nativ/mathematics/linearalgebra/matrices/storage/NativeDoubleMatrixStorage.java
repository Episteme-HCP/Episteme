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

package org.jscience.nativ.mathematics.linearalgebra.matrices.storage;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import org.jscience.core.mathematics.linearalgebra.matrices.storage.RealDoubleMatrixStorage;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.numbers.real.RealDouble;

/**
 * A dense matrix backed by off-heap native memory.
 * <p>
 * This class provides zero-copy access for native libraries (BLAS, LAPACK)
 * via Project Panama's Foreign Function & Memory API.
 * </p>
 * <p>
 * Memory is allocated outside the Java heap, avoiding GC pressure for
 * large matrices and enabling direct handoff to C/C++ code.
 * </p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * try (Arena arena = Arena.ofConfined()) {
 *     NativeDoubleMatrixStorage A = new NativeDoubleMatrixStorage(1000, 1000, arena);
 *     NativeDoubleMatrixStorage B = new NativeDoubleMatrixStorage(1000, 1000, arena);
 *     NativeDoubleMatrixStorage C = new NativeDoubleMatrixStorage(1000, 1000, arena);
 *     
 *     // Fill matrices...
 *     A.set(0, 0, 1.0);
 *     
 *     // Pass to BLAS for multiplication
 *     BlasBackend.dgemm(A, B, C);
 * }
 * }</pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class NativeDoubleMatrixStorage implements RealDoubleMatrixStorage, AutoCloseable {

    private final MemorySegment data;
    private final int rows;
    private final int cols;
    private final Arena arena;
    private final boolean ownsArena;

    /**
     * Creates a new native matrix with the given dimensions.
     * <p>
     * The matrix is initialized to zero.
     * </p>
     *
     * @param rows  number of rows
     * @param cols  number of columns
     * @param arena the arena for memory allocation
     */
    public NativeDoubleMatrixStorage(int rows, int cols, Arena arena) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
        this.rows = rows;
        this.cols = cols;
        this.arena = arena;
        this.ownsArena = false;
        
        long size = (long) rows * cols * Double.BYTES;
        this.data = arena.allocate(size, ValueLayout.JAVA_DOUBLE.byteAlignment());
        // Zero-initialize
        data.fill((byte) 0);
    }

    /**
     * Creates a native matrix with its own confined arena.
     * <p>
     * The arena is closed when {@link #close()} is called.
     * </p>
     *
     * @param rows number of rows
     * @param cols number of columns
     */
    public NativeDoubleMatrixStorage(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
        this.rows = rows;
        this.cols = cols;
        this.arena = Arena.ofConfined();
        this.ownsArena = true;
        
        long size = (long) rows * cols * Double.BYTES;
        this.data = arena.allocate(size, ValueLayout.JAVA_DOUBLE.byteAlignment());
        data.fill((byte) 0);
    }

    /**
     * Wraps an existing memory segment as a matrix.
     *
     * @param data  the memory segment containing the matrix data
     * @param rows  number of rows
     * @param cols  number of columns
     * @param arena the arena that owns the segment
     */
    public NativeDoubleMatrixStorage(MemorySegment data, int rows, int cols, Arena arena) {
        this.data = data;
        this.rows = rows;
        this.cols = cols;
        this.arena = arena;
        this.ownsArena = false;
    }

    /**
     * Returns the underlying memory segment for native access.
     *
     * @return the memory segment containing matrix data in row-major order
     */
    public MemorySegment segment() {
        return data;
    }

    /**
     * Returns the number of rows.
     *
     * @return rows
     */
    public int rows() {
        return rows;
    }

    /**
     * Returns the number of columns.
     *
     * @return columns
     */
    public int cols() {
        return cols;
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
    public java.nio.DoubleBuffer getBuffer() {
        return data.asByteBuffer().asDoubleBuffer();
    }

    @Override
    public double[] toDoubleArray() {
        return toArray();
    }

    @Override
    public NativeDoubleMatrixStorage clone() {
        NativeDoubleMatrixStorage copy = new NativeDoubleMatrixStorage(rows, cols);
        MemorySegment.copy(this.data, 0, copy.data, 0, sizeBytes());
        return copy;
    }

    /**
     * Returns the total size in bytes.
     *
     * @return size in bytes
     */
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

    /**
     * Copies data from a Java array into this matrix.
     *
     * @param values row-major array of values
     */
    public void setAll(double[] values) {
        if (values.length != rows * cols) {
            throw new IllegalArgumentException("Array size mismatch");
        }
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_DOUBLE, 0, values.length);
    }

    /**
     * Copies data from this matrix into a Java array.
     *
     * @return row-major array of values
     */
    public double[] toArray() {
        double[] result = new double[rows * cols];
        MemorySegment.copy(data, ValueLayout.JAVA_DOUBLE, 0, result, 0, result.length);
        return result;
    }

    /**
     * Returns the leading dimension (for BLAS compatibility).
     * <p>
     * For row-major storage, this is the number of columns.
     * </p>
     *
     * @return leading dimension
     */
    public int leadingDimension() {
        return cols;
    }

    /**
     * Reshapes the matrix to new dimensions.
     * The total number of elements must remain the same.
     */
    public NativeDoubleMatrixStorage reshape(int newRows, int newCols) {
        if ((long) newRows * newCols != (long) rows * cols) {
            throw new IllegalArgumentException("Total number of elements must remain the same");
        }
        return new NativeDoubleMatrixStorage(data, newRows, newCols, arena);
    }

    /**
     * Creates a new transposed matrix.
     */
    public NativeDoubleMatrixStorage transpose() {
        NativeDoubleMatrixStorage result = new NativeDoubleMatrixStorage(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.setDouble(j, i, this.getDouble(i, j));
            }
        }
        return result;
    }

    private void checkBounds(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException(
                String.format("Index (%d, %d) out of bounds for matrix %dx%d", row, col, rows, cols)
            );
        }
    }

    @Override
    public void close() {
        if (ownsArena) {
            arena.close();
        }
    }

    @Override
    public String toString() {
        return String.format("NativeDoubleMatrixStorage[%dx%d, %d bytes]", rows, cols, sizeBytes());
    }
}






