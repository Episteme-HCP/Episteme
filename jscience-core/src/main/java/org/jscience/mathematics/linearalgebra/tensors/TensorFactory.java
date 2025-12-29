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

package org.jscience.mathematics.linearalgebra.tensors;

import org.jscience.mathematics.linearalgebra.tensors.backends.TensorProvider;
import org.jscience.mathematics.linearalgebra.tensors.backends.CPUDenseTensorProvider;

import java.util.List;

/**
 * Factory for creating tensors using pluggable providers.
 * <p>
 * Automatically discovers tensor providers via Java SPI.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TensorFactory {

    /**
     * Storage format for tensors.
     * Harmonized with
     * {@link org.jscience.mathematics.linearalgebra.matrices.MatrixFactory.Storage}
     * and
     * {@link org.jscience.mathematics.linearalgebra.VectorFactory.Storage}.
     */
    public enum Storage {
        /** Automatic selection based on data sparsity. */
        AUTO,
        /** Dense storage. Optimal for full tensors. */
        DENSE,
        /** Sparse storage. Optimal for tensors with many zeros. */
        SPARSE
    }

    private TensorFactory() {
        // Utility class
    }

    /**
     * Returns the current tensor provider from ComputeContext.
     * 
     * @return the provider
     */
    /**
     * Returns the current tensor provider from LinearAlgebraRegistry.
     * 
     * @return the provider
     */
    public static TensorProvider getProvider() {
        return org.jscience.mathematics.linearalgebra.LinearAlgebraRegistry.getTensorProvider();
    }

    /**
     * Returns a provider by name.
     * 
     * @param name the provider name (e.g., "Native", "ND4J")
     * @return the provider
     * @throws IllegalArgumentException if provider not found
     */
    public static TensorProvider getProvider(String name) {
        for (TensorProvider provider : getAllProviders()) {
            if (provider.getName().equalsIgnoreCase(name)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Tensor provider not found: " + name);
    }

    /**
     * Returns all available providers.
     * 
     * @return list of providers
     */
    public static List<TensorProvider> getAllProviders() {
        return org.jscience.mathematics.linearalgebra.LinearAlgebraRegistry.getTensorProviders();
    }

    // ========== Convenience factory methods ==========

    /**
     * Creates a zero-filled tensor with automatic storage selection.
     * 
     * @param <T>         the element type
     * @param elementType the class of field elements
     * @param shape       the tensor shape
     * @return zero tensor
     */
    public static <T> Tensor<T> zeros(Class<T> elementType, int... shape) {
        return zeros(elementType, Storage.AUTO, shape);
    }

    /**
     * Creates a zero-filled tensor with explicit storage selection.
     * 
     * @param <T>         the element type
     * @param elementType the class of field elements
     * @param storage     the storage format
     * @param shape       the tensor shape
     * @return zero tensor
     */
    public static <T> Tensor<T> zeros(Class<T> elementType, Storage storage, int... shape) {
        // For now, delegate to provider; in future, select dense/sparse provider
        return getProvider().zeros(elementType, shape);
    }

    /**
     * Creates a one-filled tensor with automatic storage selection.
     * 
     * @param <T>         the element type
     * @param elementType the class of field elements
     * @param shape       the tensor shape
     * @return one tensor
     */
    public static <T> Tensor<T> ones(Class<T> elementType, int... shape) {
        return ones(elementType, Storage.AUTO, shape);
    }

    /**
     * Creates a one-filled tensor with explicit storage selection.
     * 
     * @param <T>         the element type
     * @param elementType the class of field elements
     * @param storage     the storage format
     * @param shape       the tensor shape
     * @return one tensor
     */
    public static <T> Tensor<T> ones(Class<T> elementType, Storage storage, int... shape) {
        return getProvider().ones(elementType, shape);
    }

    /**
     * Creates a tensor from data with automatic storage selection.
     * 
     * @param <T>   the element type
     * @param data  the flat data array
     * @param shape the tensor shape
     * @return tensor
     */
    public static <T> Tensor<T> of(T[] data, int... shape) {
        return of(data, Storage.AUTO, shape);
    }

    /**
     * Creates a tensor from data with explicit storage selection.
     * 
     * @param <T>     the element type
     * @param data    the flat data array
     * @param storage the storage format
     * @param shape   the tensor shape
     * @return tensor
     */
    public static <T> Tensor<T> of(T[] data, Storage storage, int... shape) {
        return getProvider().create(data, shape);
    }
}