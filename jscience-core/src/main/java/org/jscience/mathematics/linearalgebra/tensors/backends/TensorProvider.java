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

import org.jscience.technical.backend.ComputeBackend;

/**
 * Service Provider Interface for tensor implementations.
 * <p>
 * Allows pluggable backends for tensor operations:
 * <ul>
 * <li><b>Native</b>: Pure Java implementation (default)</li>
 * <li><b>ND4J</b>: GPU-accelerated via CUDA</li>
 * <li><b>DJL</b>: PyTorch/TensorFlow/MXNet backends</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface TensorProvider extends ComputeBackend {

    /**
     * Creates a tensor with the given shape, filled with zeros.
     * 
     * @param <T>         the element type
     * @param elementType the class of field elements
     * @param shape       the tensor shape
     * @return zero-initialized tensor
     */
    <T> Tensor<T> zeros(Class<T> elementType, int... shape);

    /**
     * Creates a tensor with the given shape, filled with ones.
     * 
     * @param <T>         the element type
     * @param elementType the class of field elements
     * @param shape       the tensor shape
     * @return one-initialized tensor
     */
    <T> Tensor<T> ones(Class<T> elementType, int... shape);

    /**
     * Creates a tensor from a flat array and shape.
     * 
     * @param <T>   the element type
     * @param data  the flat data array
     * @param shape the tensor shape
     * @return tensor with given data
     */
    <T> Tensor<T> create(T[] data, int... shape);

    /**
     * Returns true if this provider supports GPU acceleration.
     * 
     * @return true if GPU is available
     */
    boolean supportsGPU();

    /**
     * Returns the name of this provider.
     * 
     * @return provider name (e.g., "Native", "ND4J", "DJL")
     */
    String getName();

    /**
     * Returns the priority of this provider (higher = preferred).
     * <p>
     * Used for automatic selection when multiple providers are available.
     * </p>
     * 
     * @return priority (0-100, default native = 50)
     */
    default int getPriority() {
        return 50;
    }
}

