/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.biopax;

/** BioPAX parsing exception. */
public class BioPAXException extends Exception {
    public BioPAXException(String message) { super(message); }
    public BioPAXException(String message, Throwable cause) { super(message, cause); }
}
