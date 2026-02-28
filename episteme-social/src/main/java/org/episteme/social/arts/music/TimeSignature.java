/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.social.arts.music;

import java.io.Serializable;

/**
 * Represents a musical time signature (e.g., 4/4).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public record TimeSignature(int beatsPerMeasure, int beatValue) implements Serializable {
    @Override
    public String toString() { 
        return beatsPerMeasure + "/" + beatValue; 
    }
}

