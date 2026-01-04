/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.technical.backend;

/**
 * Common interface for all algorithm providers.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface AlgorithmProvider {

    /**
     * Returns the descriptive name of this provider.
     * 
     * @return the name
     */
    String getName();

}
