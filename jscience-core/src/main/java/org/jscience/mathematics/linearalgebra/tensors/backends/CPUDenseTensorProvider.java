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

package org.jscience.mathematics.linearalgebra.tensors.backends;

import org.jscience.mathematics.linearalgebra.tensors.Tensor;
import org.jscience.mathematics.linearalgebra.tensors.DenseTensor;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.technical.backend.ExecutionContext;

/**
 * Native (pure Java) tensor provider.
 * <li>Small to medium tensors</li>
 * <li>Type-safe operations with Field&lt;T&gt;</li>
 * <li>CPU-only environments</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CPUDenseTensorProvider implements TensorProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        int size = 1;
        for (int dim : shape) {
            size *= dim;
        }

        T[] data;
        try {
            data = (T[]) java.lang.reflect.Array.newInstance(elementType, size);
        } catch (Exception e) {
            data = (T[]) new Object[size];
        }

        // Initialize with zero() from ring/field
        try {
            if (org.jscience.mathematics.structures.rings.Ring.class.isAssignableFrom(elementType)) {
                org.jscience.mathematics.structures.rings.Ring<?> ringElem = (org.jscience.mathematics.structures.rings.Ring<?>) elementType
                        .getDeclaredConstructor().newInstance();
                T zero = (T) ringElem.zero();
                for (int i = 0; i < size; i++) {
                    data[i] = zero;
                }
            } else if (Real.class.equals(elementType)) {
                for (int i = 0; i < size; i++) {
                    data[i] = (T) Real.ZERO;
                }
            } else {
                // Try default constructor generic fallback

                // Check if it has zero method via reflection?
                // For now, only Ring supported or types with explicit support.
            }
        } catch (Exception e) {
            // Fallback for types without default constructor
            if (Real.class.equals(elementType)) {
                for (int i = 0; i < size; i++) {
                    data[i] = (T) Real.ZERO;
                }
            } else {
                // throw new IllegalArgumentException("Cannot create zero for type: " +
                // elementType, e);
                // Just return null-filled array if we can't determine zero?
                // Or throw.
            }
        }

        return new DenseTensor<>(data, shape);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        int size = 1;
        for (int dim : shape) {
            size *= dim;
        }

        T[] data;
        try {
            data = (T[]) java.lang.reflect.Array.newInstance(elementType, size);
        } catch (Exception e) {
            data = (T[]) new Object[size];
        }

        // Initialize with one() from ring/field
        try {
            if (org.jscience.mathematics.structures.rings.Ring.class.isAssignableFrom(elementType)) {
                org.jscience.mathematics.structures.rings.Ring<?> ringElem = (org.jscience.mathematics.structures.rings.Ring<?>) elementType
                        .getDeclaredConstructor().newInstance();
                T one = (T) ringElem.one();
                for (int i = 0; i < size; i++) {
                    data[i] = one;
                }
            } else if (Real.class.equals(elementType)) {
                for (int i = 0; i < size; i++) {
                    data[i] = (T) Real.ONE;
                }
            }
        } catch (Exception e) {
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
    public <T> Tensor<T> create(T[] data, int... shape) {
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

    @Override
    public ExecutionContext createContext() {
        // CPU default execution context
        return null;
    }

    @Override
    public boolean supportsParallelOps() {
        return true;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}

