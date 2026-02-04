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

package org.jscience.natural.physics.quantum;

import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.BackendDiscovery;
import org.jscience.core.technical.backend.quantum.QuantumBackend;
import org.jscience.natural.physics.quantum.backends.PythonQuantumBackendProvider;

import java.util.List;
import java.util.Optional;

/**
 * Factory for creating quantum backends using SPI-based discovery.
 * Auto-detects best available backend if not specified.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QuantumFactory {

    private static String selectedBackendId = null; // null = AUTO

    /**
     * Sets the preferred backend by ID.
     * 
     * @param backendId Backend ID or null for AUTO
     */
    public static void setBackend(String backendId) {
        selectedBackendId = backendId;
    }

    /**
     * Gets the currently selected backend ID.
     */
    public static String getSelectedBackendId() {
        return selectedBackendId;
    }

    /**
     * Creates a quantum backend with default/AUTO settings.
     */
    public static QuantumBackend createBackend() {
        Backend provider = getBackendProvider();
        if (provider != null) {
            Object backend = provider.createBackend();
            if (backend instanceof QuantumBackend) {
                return (QuantumBackend) backend;
            }
        }

        // Fallback
        return new PythonQuantumBackendProvider();
    }

    /**
     * Gets the best available quantum backend provider.
     */
    private static Backend getBackendProvider() {
        if (selectedBackendId != null) {
            Optional<Backend> specific = BackendDiscovery.getInstance()
                    .getProvider(BackendDiscovery.TYPE_QUANTUM, selectedBackendId);
            if (specific.isPresent() && specific.get().isAvailable()) {
                return specific.get();
            }
        }

        // Auto-select best available (sorted by priority)
        Optional<Backend> best = BackendDiscovery.getInstance()
                .getBestProvider(BackendDiscovery.TYPE_QUANTUM);
        return best.orElse(null);
    }

    /**
     * Returns all discovered quantum backend providers.
     */
    public static List<Backend> getAvailableBackends() {
        return BackendDiscovery.getInstance()
                .getProvidersByType(BackendDiscovery.TYPE_QUANTUM);
    }

    /**
     * Checks if a specific backend is available.
     */
    public static boolean isBackendAvailable(String backendId) {
        Optional<Backend> provider = BackendDiscovery.getInstance()
                .getProvider(BackendDiscovery.TYPE_QUANTUM, backendId);
        return provider.isPresent() && provider.get().isAvailable();
    }
}
