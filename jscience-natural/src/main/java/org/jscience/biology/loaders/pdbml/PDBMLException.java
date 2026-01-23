/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.pdbml;

/** PDBML parsing exception. */
public class PDBMLException extends Exception {
    public PDBMLException(String message) { super(message); }
    public PDBMLException(String message, Throwable cause) { super(message, cause); }
}
