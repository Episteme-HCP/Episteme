/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.computing.loaders.pmml;

/** PMML parsing exception. */
public class PMMLException extends Exception {
    public PMMLException(String message) { super(message); }
    public PMMLException(String message, Throwable cause) { super(message, cause); }
}
