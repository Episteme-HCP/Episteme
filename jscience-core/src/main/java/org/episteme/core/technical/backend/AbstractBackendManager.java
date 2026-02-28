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

package org.episteme.core.technical.backend;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for managing a set of backends of a specific type.
 *
 * @param <T> The type of backend being managed
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public abstract class AbstractBackendManager<T extends Backend> {

    protected final Map<String, T> backends = new ConcurrentHashMap<>();
    protected T defaultBackend;
    protected final Class<T> backendClass;

    protected AbstractBackendManager(Class<T> backendClass) {
        this.backendClass = backendClass;
        refresh();
    }

    /**
     * Discovers and registers backends using ServiceLoader.
     */
    public void refresh() {
        ServiceLoader<T> loader = ServiceLoader.load(backendClass);
        for (T backend : loader) {
            managerRegister(backend);
        }
        if (defaultBackend == null) {
            defaultBackend = selectBestBackend();
        }
    }

    /**
     * Returns the current default backend instance.
     * 
     * @return the default backend
     */
    public T managerDefault() {
        if (defaultBackend == null) {
            throw new IllegalStateException("No backends available for " + backendClass.getSimpleName());
        }
        return defaultBackend;
    }

    /**
     * Selects a specific backend by name or ID.
     * 
     * @param name the backend name or ID
     * @return the backend, or null if not found
     */
    public T managerSelect(String name) {
        if (name == null) return null;
        T b = backends.get(name);
        if (b == null) {
            b = backends.values().stream()
                    .filter(p -> p.getId().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);
        }
        return b;
    }

    /**
     * Sets the default backend.
     * 
     * @param name the backend name or ID
     */
    public void managerSetDefault(String name) {
        T backend = managerSelect(name);
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
     * 
     * @param backend the backend to register
     */
    public void managerRegister(T backend) {
        backends.put(backend.getName(), backend);
    }

    /**
     * Returns all registered backends.
     * 
     * @return collection of backends
     */
    public Collection<T> managerAll() {
        return Collections.unmodifiableCollection(backends.values());
    }

    /**
     * Returns names of available backends.
     */
    public Collection<String> managerNames() {
        return Collections.unmodifiableSet(backends.keySet());
    }

    /**
     * Selects the best available backend based on priority.
     */
    protected T selectBestBackend() {
        return backends.values().stream()
                .filter(Backend::isAvailable)
                .max(Comparator.comparingInt(Backend::getPriority))
                .orElse(null);
    }
}
