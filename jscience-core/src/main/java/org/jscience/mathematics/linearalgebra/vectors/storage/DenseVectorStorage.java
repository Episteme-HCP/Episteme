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

import java.util.Arrays;

/**
 * Dense storage for vectors using a standard array.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DenseVectorStorage<E> implements VectorStorage<E> {
    private final E[] data;
    private final int dimension;

    public DenseVectorStorage(int dimension) {
        this.dimension = dimension;
        // Generic array creation requires reflection or Object[] cast.
        // For storage, we might just store Object[] if E is not reified, but here we
        // assume caller might provide array or we cast.
        // Actually, to be type-safe without passing Class<E>, we might have to use
        // Object[] and cast on get.
        // However, GenericVector usually knows the class.
        // For simplicity in this refactor, we'll use Object[] internally and cast.
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) new Object[dimension];
        this.data = arr;
    }

    public DenseVectorStorage(E[] data) {
        this.dimension = data.length;
        this.data = data;
    }

    public DenseVectorStorage(java.util.List<E> data) {
        this.dimension = data.size();
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) data.toArray();
        this.data = arr;
    }

    @Override
    public int dimension() {
        return dimension;
    }

    @Override
    public E get(int index) {
        return data[index];
    }

    @Override
    public void set(int index, E value) {
        data[index] = value;
    }

    @Override
    public VectorStorage<E> copy() {
        return new DenseVectorStorage<>(Arrays.copyOf(data, dimension));
    }
}


