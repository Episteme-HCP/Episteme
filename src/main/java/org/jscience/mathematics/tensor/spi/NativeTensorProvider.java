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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.tensor.spi;

import org.jscience.mathematics.tensor.Tensor;
import org.jscience.mathematics.tensor.DenseTensor;
import org.jscience.mathematics.algebra.Field;
import org.jscience.mathematics.number.Real;

/**
 * Native (pure Java) tensor provider.
 * <p>
 * Uses {@link DenseTensor} for all operations. Suitable for:
 * <ul>
 * <li>Small to medium tensors</li>
 * <li>Type-safe operations with Field&lt;T&gt;</li>
 * <li>CPU-only environments</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class NativeTensorProvider implements TensorProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Field<T>> Tensor<T> zeros(Class<T> elementType, int... shape) {
        int size = 1;
        for (int dim : shape) {
            size *= dim;
        }

        T[] data = (T[]) new Field[size];

        // Initialize with zero() from field
        try {
            T zero = elementType.getDeclaredConstructor().newInstance().zero();
            for (int i = 0; i < size; i++) {
                data[i] = zero;
            }
        } catch (Exception e) {
            // Fallback for types without default constructor
            if (Real.class.equals(elementType)) {
                for (int i = 0; i < size; i++) {
                    data[i] = (T) Real.ZERO;
                }
            } else {
                throw new IllegalArgumentException("Cannot create zero for type: " + elementType, e);
            }
        }

        return new DenseTensor<>(data, shape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Field<T>> Tensor<T> ones(Class<T> elementType, int... shape) {
        int size = 1;
        for (int dim : shape) {
            size *= dim;
        }

        T[] data = (T[]) new Field[size];

        // Initialize with one() from field
        try {
            T one = elementType.getDeclaredConstructor().newInstance().one();
            for (int i = 0; i < size; i++) {
                data[i] = one;
            }
        } catch (Exception e) {
            // Fallback for types without default constructor
            if (Real.class.equals(elementType)) {
                for (int i = 0; i < size; i++) {
                    data[i] = (T) Real.ONE;
                }
            } else {
                throw new IllegalArgumentException("Cannot create one for type: " + elementType, e);
            }
        }

        return new DenseTensor<>(data, shape);
    }

    @Override
    public <T extends Field<T>> Tensor<T> create(T[] data, int... shape) {
        return new DenseTensor<>(data, shape);
    }

    @Override
    public boolean supportsGPU() {
        return false;
    }

    @Override
    public String getName() {
        return "Native";
    }

    @Override
    public int getPriority() {
        return 50; // Default priority
    }
}

