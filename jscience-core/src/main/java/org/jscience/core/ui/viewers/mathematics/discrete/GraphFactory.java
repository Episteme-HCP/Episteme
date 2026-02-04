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

package org.jscience.core.ui.viewers.mathematics.discrete;

import org.jscience.core.technical.backend.Backend;
import org.jscience.core.ui.viewers.mathematics.discrete.backends.JavaFXGraphBackendProvider;

import java.util.Collection;
import java.util.Optional;

/**
 * Factory for creating graphs using SPI-based backend discovery.
 * Auto-detects best available backend if not specified.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GraphFactory {

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
     * Creates a graph with default/AUTO backend.
     */
    public static Object createGraph(String title) {
        Backend provider = getBackendProvider();
        if (provider != null) {
            return provider.createBackend();
        }

        // Ultimate fallback
        return new JavaFXGraphBackendProvider().createBackend();
    }

    /**
     * Gets the best available graph backend provider.
     */
    private static Backend getBackendProvider() {
        if (selectedBackendId != null) {
             GraphBackend b = GraphBackendManager.getInstance().select(selectedBackendId);
             if (b != null && b.isAvailable()) {
                return b;
             }
        }
        return GraphBackendManager.getInstance().getActiveBackend();
    }

    /**
     * Returns all discovered graph backend providers.
     */
    public static Collection<GraphBackend> getAvailableBackends() {
        return GraphBackendManager.getInstance().getAllBackends();
    }

    /**
     * Checks if a specific backend is available.
     */
    public static boolean isBackendAvailable(String backendId) {
        GraphBackend b = GraphBackendManager.getInstance().select(backendId);
        return b != null && b.isAvailable();
    }
}
