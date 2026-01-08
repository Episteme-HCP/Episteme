/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
