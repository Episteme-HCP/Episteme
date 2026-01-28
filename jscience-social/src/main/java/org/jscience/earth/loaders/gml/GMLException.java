/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth.loaders.gml;

/**
 * Exception thrown for GML parsing or processing errors.
 */
public class GMLException extends Exception {
    private static final long serialVersionUID = 1L;

    public GMLException(String message) {
        super(message);
    }

    public GMLException(String message, Throwable cause) {
        super(message, cause);
    }
}
