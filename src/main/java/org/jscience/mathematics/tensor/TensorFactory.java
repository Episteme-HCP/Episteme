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
package org.jscience.mathematics.tensor;

import org.jscience.mathematics.tensor.spi.TensorProvider;
import org.jscience.mathematics.tensor.spi.NativeTensorProvider;
import org.jscience.mathematics.algebra.Field;
import java.util.ServiceLoader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Factory for creating tensors using pluggable providers.
 * <p>
 * Automatically discovers tensor providers via Java SPI.
 * Defaults to {@link NativeTensorProvider} if no others are available.
 * </p>
 * 
 * <h2>Usage</h2>
 * 
 * <pre>
 * // Use default provider
 * Tensor&lt;Real&gt; t1 = TensorFactory.zeros(Real.class, 2, 3);
 * 
 * // Use specific provider
 * TensorProvider nd4j = TensorFactory.getProvider("ND4J");
 * Tensor&lt;Real&gt; t2 = nd4j.zeros(Real.class, 100, 100);
 * </pre>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class TensorFactory {

    private static final TensorProvider DEFAULT_PROVIDER = new NativeTensorProvider();
    private static TensorProvider cachedProvider;

    static {
        cachedProvider = discoverBestProvider();
    }

    private TensorFactory() {
        // Utility class
    }

    /**
     * Returns the default tensor provider.
     * <p>
     * Automatically selects the best available provider based on priority.
     * </p>
     * 
     * @return the default provider
     */
    public static TensorProvider getProvider() {
        return cachedProvider;
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
        ServiceLoader<TensorProvider> loader = ServiceLoader.load(TensorProvider.class);
        List<TensorProvider> providers = StreamSupport.stream(loader.spliterator(), false)
                .collect(Collectors.toList());

        // Always include native provider
        if (providers.stream().noneMatch(p -> p instanceof NativeTensorProvider)) {
            providers.add(DEFAULT_PROVIDER);
        }

        return providers;
    }

    private static TensorProvider discoverBestProvider() {
        List<TensorProvider> providers = getAllProviders();

        // Select provider with highest priority
        return providers.stream()
                .max((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()))
                .orElse(DEFAULT_PROVIDER);
    }

    // Convenience factory methods using default provider

    /**
     * Creates a zero-filled tensor.
     * 
     * @param <T>         the element type
     * @param elementType the class of field elements
     * @param shape       the tensor shape
     * @return zero tensor
     */
    public static <T extends Field<T>> Tensor<T> zeros(Class<T> elementType, int... shape) {
        return getProvider().zeros(elementType, shape);
    }

    /**
     * Creates a one-filled tensor.
     * 
     * @param <T>         the element type
     * @param elementType the class of field elements
     * @param shape       the tensor shape
     * @return one tensor
     */
    public static <T extends Field<T>> Tensor<T> ones(Class<T> elementType, int... shape) {
        return getProvider().ones(elementType, shape);
    }

    /**
     * Creates a tensor from data.
     * 
     * @param <T>   the element type
     * @param data  the flat data array
     * @param shape the tensor shape
     * @return tensor
     */
    public static <T extends Field<T>> Tensor<T> of(T[] data, int... shape) {
        return getProvider().create(data, shape);
    }
}

