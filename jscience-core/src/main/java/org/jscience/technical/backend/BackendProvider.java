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

package org.jscience.technical.backend;

/**
 * Base interface for discoverable backend providers.
 * Used by ServiceLoader to dynamically discover available backends
 * (plotting, molecular visualization, computation, etc.).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public interface BackendProvider {

    /**
     * Returns the backend type category (e.g., "plotting", "molecular", "tensor",
     * "math").
     */
    String getType();

    /**
     * Returns the unique identifier for this backend (e.g., "javafx", "jmol",
     * "cuda").
     */
    String getId();

    /**
     * Returns the display name for UI presentation.
     */
    String getName();

    /**
     * Returns a description of the backend.
     */
    String getDescription();

    /**
     * Checks if this backend is currently available (libraries loaded, etc.).
     */
    boolean isAvailable();

    /**
     * Returns the priority for auto-selection (higher = preferred).
     * Used when multiple backends are available.
     */
    default int getPriority() {
        return 0;
    }

    /**
     * Creates and returns the backend instance (or returns self if it is the
     * backend).
     * 
     * @return The backend implementation object
     */
    Object createBackend();
}
