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
package org.jscience.mathematics.spi;

import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Registry and factory for algorithm services.
 * <p>
 * This class decouples the interface of an algorithm (Service) from its
 * implementation (Provider).
 * </p>
 */
public class ComputeContext {

    private static final ComputeContext GLOBAL = new ComputeContext();
    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    private ComputeContext() {
        // Register defaults
        registerService(LinearAlgebraService.class, new DefaultLinearAlgebraProvider());
    }

    public static ComputeContext global() {
        return GLOBAL;
    }

    /**
     * Retrieves a service implementation.
     * 
     * @param serviceType The interface class of the service (e.g.,
     *                    LinearAlgebraService.class)
     * @param <S>         The type of the service
     * @return The service implementation
     */
    @SuppressWarnings("unchecked")
    public <S> S getService(Class<S> serviceType) {
        return (S) services.computeIfAbsent(serviceType, this::loadService);
    }

    private <S> S loadService(Class<S> serviceType) {
        ServiceLoader<S> loader = ServiceLoader.load(serviceType);
        // For now, return the first available provider or throw
        return loader.findFirst().orElseThrow(
                () -> new IllegalStateException("No provider found for service: " + serviceType.getName()));
    }

    /**
     * Registers a specific implementation for a service (useful for testing or
     * manual override).
     */
    public <S> void registerService(Class<S> serviceType, S implementation) {
        services.put(serviceType, implementation);
    }
}


