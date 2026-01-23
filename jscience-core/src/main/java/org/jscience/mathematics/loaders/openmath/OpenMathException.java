/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.mathematics.loaders.openmath;

/**
 * Exception thrown when OpenMath parsing fails.
 */
public class OpenMathException extends Exception {
    public OpenMathException(String message) { super(message); }
    public OpenMathException(String message, Throwable cause) { super(message, cause); }
}
