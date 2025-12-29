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

package org.jscience.mathematics.linearalgebra.vectors.storage;

import org.jscience.mathematics.numbers.real.Real;
import java.nio.DoubleBuffer;

/**
 * Direct buffer implementation of RealDoubleVectorStorage.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
