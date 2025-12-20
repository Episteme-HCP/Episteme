/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.util.id;

/**
 * Interface for identity generation strategies.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public interface IdGenerator {

    /**
     * Generates a new unique identifier.
     * 
     * @return a unique string ID.
     */
    String generate();
}
