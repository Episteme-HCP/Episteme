/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend.native_;

import org.jscience.technical.backend.BackendProvider;

/**
 * Marker interface for Native backends.
 * The underscore is used to strict avoidance of collision with the Java 'native' keyword.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public interface NativeBackend extends BackendProvider {
    
    /**
     * Checks if the native library is loaded and available.
     */
    boolean isLoaded();
}
