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

package org.jscience.mathematics.linearalgebra.vectors.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * Sparse storage for vectors using a Map.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SparseVectorStorage<E> implements VectorStorage<E> {
    private final Map<Integer, E> data;
    private final int dimension;
    private final E zero;

    public SparseVectorStorage(int dimension, E zero) {
        this.dimension = dimension;
        this.data = new HashMap<>(); // Could use a more efficient primitive map if E was Double
        this.zero = zero;
    }

    // Copy constructor
    private SparseVectorStorage(int dimension, E zero, Map<Integer, E> data) {
        this.dimension = dimension;
        this.zero = zero;
        this.data = new HashMap<>(data);
    }

    @Override
    public int dimension() {
        return dimension;
    }

    @Override
    public E get(int index) {
        return data.getOrDefault(index, zero);
    }

    @Override
    public void set(int index, E value) {
        if (value.equals(zero)) {
            data.remove(index);
        } else {
            data.put(index, value);
        }
    }

    @Override
    public VectorStorage<E> copy() {
        return new SparseVectorStorage<>(dimension, zero, data);
    }
}


