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
package org.jscience.technical.backend;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages compute backends and provides access to the default backend.
 * <p>
 * Backends are auto-discovered via {@link java.util.ServiceLoader} and
 * registered
 * automatically at class initialization.
 * </p>
 * <p>
 * <b>Usage:</b>
 * 
 * <pre>
 * // Use default backend (highest priority available)
 * ComputeBackend backend = BackendManager.getDefault();
 * 
 * // Select specific backend
 * ComputeBackend cpu = BackendManager.select("CPU");
 * 
 * // List all backends
 * Collection&lt;String&gt; names = BackendManager.getAvailableBackendNames();
 * </pre>
 * </p>
 * 
 * @author Silvere Martin-Michiellot (silvere.martin@gmail.com)
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BackendManager {

    private static final Map<String, ComputeBackend> backends = new ConcurrentHashMap<>();
    private static ComputeBackend defaultBackend;

    static {
        // Auto-discover backends via ServiceLoader
        ServiceLoader<ComputeBackend> loader = ServiceLoader.load(ComputeBackend.class);
        for (ComputeBackend backend : loader) {
            registerBackend(backend);
        }

        // Select default backend (highest priority among available)
        defaultBackend = selectBestBackend();
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private BackendManager() {
    }

    /**
     * Returns the default compute backend.
     * <p>
     * The default backend is the highest-priority backend that is currently
     * available.
     * </p>
     * 
     * @return the default backend
     * @throws IllegalStateException if no backends are available
     */
    public static ComputeBackend getDefault() {
        if (defaultBackend == null) {
            throw new IllegalStateException("No compute backends available");
        }
        return defaultBackend;
    }

    /**
     * Selects a specific backend by name.
     * 
     * @param name the backend name
     * @return the backend, or null if not found
     */
    public static ComputeBackend select(String name) {
        return backends.get(name);
    }

    /**
     * Sets the default backend.
     * <p>
     * This allows users to override the automatic backend selection.
     * </p>
     * 
     * @param name the backend name
     * @throws IllegalArgumentException if backend not found or not available
     */
    public static void setDefault(String name) {
        ComputeBackend backend = backends.get(name);
        if (backend == null) {
            throw new IllegalArgumentException("Backend not found: " + name);
        }
        if (!backend.isAvailable()) {
            throw new IllegalArgumentException("Backend not available: " + name);
        }
        defaultBackend = backend;
    }

    /**
     * Registers a backend manually.
     * <p>
     * This is called automatically during class initialization, but can also be
     * used to register custom backends at runtime.
     * </p>
     * 
     * @param backend the backend to register
     */
    public static void registerBackend(ComputeBackend backend) {
        backends.put(backend.getName(), backend);
    }

    /**
     * Returns the names of all registered backends.
     * 
     * @return collection of backend names
     */
    public static Collection<String> getAvailableBackendNames() {
        return Collections.unmodifiableSet(backends.keySet());
    }

    /**
     * Returns all registered backends.
     * 
     * @return collection of backends
     */
    public static Collection<ComputeBackend> getAllBackends() {
        return Collections.unmodifiableCollection(backends.values());
    }

    /**
     * Selects the best available backend based on priority.
     * 
     * @return the backend with highest priority, or null if none available
     */
    private static ComputeBackend selectBestBackend() {
        return backends.values().stream()
                .filter(ComputeBackend::isAvailable)
                .max(Comparator.comparingInt(ComputeBackend::getPriority))
                .orElse(null);
    }
}
