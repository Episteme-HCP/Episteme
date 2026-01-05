/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.io;

/**
 * Interface for writing/exporting resources.
 * Replaces OutputLoader.
 */
public interface ResourceWriter<T> extends ResourceIO<T> {

    /**
     * Saves the resource to a destination.
     */
    void save(T resource, String destination) throws Exception;

    @Override
    default boolean isOutput() {
        return true;
    }
}
