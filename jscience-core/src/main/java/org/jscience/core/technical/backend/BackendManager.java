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

package org.jscience.core.technical.backend;

import java.util.*;

/**
 * Global registry and manager for all discoverable backends.
 * Provides static access for backward compatibility and a general-purpose manager.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BackendManager extends AbstractBackendManager<Backend> {

    private static final BackendManager INSTANCE = new BackendManager();

    private BackendManager() {
        super(Backend.class);
    }

    /**
     * Returns the global singleton instance.
     */
    public static BackendManager getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the default backend.
     */
    public static Backend getDefault() {
        return INSTANCE.getDefault();
    }

    /**
     * Selects a backend by name.
     */
    public static Backend select(String name) {
        return INSTANCE.select(name);
    }

    /**
     * Sets the default backend.
     */
    public static void setDefault(String name) {
        INSTANCE.setDefault(name);
    }

    /**
     * Registers a backend.
     */
    public static void registerBackend(Backend backend) {
        INSTANCE.registerBackend(backend);
    }

    /**
     * Returns names of all registered backends.
     */
    public static Collection<String> getAvailableBackendNames() {
        return INSTANCE.getAvailableNames();
    }

    /**
     * Returns all registered backends.
     */
    public static Collection<Backend> getAllBackends() {
        return INSTANCE.getAllBackends();
    }
}
