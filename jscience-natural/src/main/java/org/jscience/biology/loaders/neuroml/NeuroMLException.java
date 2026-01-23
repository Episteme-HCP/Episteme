/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.neuroml;

/** Exception for NeuroML parsing errors. */
public class NeuroMLException extends Exception {
    public NeuroMLException(String message) { super(message); }
    public NeuroMLException(String message, Throwable cause) { super(message, cause); }
}
