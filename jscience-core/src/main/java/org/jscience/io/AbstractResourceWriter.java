/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.io;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

/**
 * Abstract base for resource writers.
 * Provides basic buffering support but no caching/fallback logic (Output only).
 */
public abstract class AbstractResourceWriter<T> implements ResourceWriter<T> {

    /**
     * Prepares an OutputStream with buffering.
     */
    protected OutputStream createBufferedStream(OutputStream out) {
        if (out instanceof BufferedOutputStream) {
            return out;
        }
        return new BufferedOutputStream(out);
    }

    // Subclasses implement save(T, String)
}
