/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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

/**
 * Heap-based implementation of RealDoubleVectorStorage using a double array. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
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
