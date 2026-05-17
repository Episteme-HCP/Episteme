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

package org.episteme.core.mathematics.linearalgebra.vectors.storage;

import java.util.Arrays;
import org.episteme.core.mathematics.structures.rings.Ring;

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

    @SuppressWarnings("unchecked")
    public DenseVectorStorage(int dimension, E initialValue) {
        this.dimension = dimension;
        Class<?> componentType = Object.class;
        if (initialValue != null) {
            if (initialValue instanceof org.episteme.core.mathematics.numbers.real.Real) {
                componentType = org.episteme.core.mathematics.numbers.real.Real.class;
            } else if (initialValue instanceof org.episteme.core.mathematics.numbers.complex.Complex) {
                componentType = org.episteme.core.mathematics.numbers.complex.Complex.class;
            } else {
                componentType = initialValue.getClass();
            }
        }
        this.data = (E[]) java.lang.reflect.Array.newInstance(componentType, dimension);
        if (initialValue != null) {
            java.util.Arrays.fill(this.data, initialValue);
        }
    }

    public DenseVectorStorage(int dimension, Ring<E> ring) {
        this(dimension, ring.zero());
    }

    @SuppressWarnings("unchecked")
    public DenseVectorStorage(int dimension) {
        this.dimension = dimension;
        this.data = (E[]) new Object[dimension];
    }

    public DenseVectorStorage(E[] data) {
        this.dimension = data.length;
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public DenseVectorStorage(java.util.List<E> data) {
        this.dimension = data.size();
        if (data.isEmpty()) {
            this.data = (E[]) new Object[0];
        } else {
            E first = data.get(0);
            Class<?> componentType = first.getClass();
            if (first instanceof org.episteme.core.mathematics.numbers.real.Real) {
                componentType = org.episteme.core.mathematics.numbers.real.Real.class;
            } else if (first instanceof org.episteme.core.mathematics.numbers.complex.Complex) {
                componentType = org.episteme.core.mathematics.numbers.complex.Complex.class;
            }
            this.data = (E[]) java.lang.reflect.Array.newInstance(componentType, dimension);
            for (int i = 0; i < dimension; i++) this.data[i] = data.get(i);
        }
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



