/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.nativ;

import org.jscience.core.technical.backend.Backend;

/**
 * Marker interface for Native backends.
 * 'nativ' is used to avoid collision with the Java 'native' keyword,
 * matching the 'jscience-native' module naming convention.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public interface NativeBackend extends Backend {
    
    /**
     * Checks if the native library is loaded and available.
     */
    boolean isLoaded();
}


