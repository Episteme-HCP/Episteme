/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.distributed;

/**
 * Enumeration of precision modes supported by the JScience distributed tasks.
 */
public enum PrecisionMode {
    /** High precision using JScience Real types. */
    REALS,
    /** High performance using Java double primitives. */
    PRIMITIVES
}
