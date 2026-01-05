/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.io;

/**
 * Interface for components that read/load resources.
 * Replaces InputLoader.
 */
public interface ResourceReader<T> extends ResourceIO<T> {

    /**
     * Loads a resource by its identifier.
     */
    T load(String resourceId) throws Exception;

    @Override
    default boolean isInput() {
        return true;
    }
}
