/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.linearalgebra.matrices.storage;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealFloat;

/**
 * Heap-based storage for single-precision real matrices.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class HeapRealFloatMatrixStorage implements RealFloatMatrixStorage {

    private final float[] data;
    private final int rows;
    private final int cols;

    public HeapRealFloatMatrixStorage(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new float[rows * cols];
    }

    public HeapRealFloatMatrixStorage(float[] data, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = data;
    }

    @Override
    public float getFloat(int row, int col) {
        return data[row * cols + col];
    }

    @Override
    public java.nio.FloatBuffer getBuffer() {
        return java.nio.FloatBuffer.wrap(data);
    }

    @Override
    public void setFloat(int row, int col, float value) {
        data[row * cols + col] = value;
    }

    @Override
    public float[] toFloatArray() {
        return data.clone();
    }

    @Override
    public Real get(int row, int col) {
        return RealFloat.create(getFloat(row, col));
    }

    @Override
    public void set(int row, int col, Real value) {
        setFloat(row, col, value.floatValue());
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int cols() {
        return cols;
    }

    @Override
    public MatrixStorage<Real> clone() {
        return new HeapRealFloatMatrixStorage(data.clone(), rows, cols);
    }

    /**
     * Returns the underlying data array without cloning.
     * Exercise caution as modifications to this array will affect the storage.
     * 
     * @return the raw float data array
     */
    public float[] getData() {
        return data;
    }
}
