/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.social.arts.music;

/**
 * Represents a musical duration.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public enum Duration {
    WHOLE(1.0), HALF(0.5), QUARTER(0.25), EIGHTH(0.125), SIXTEENTH(0.0625), THIRTY_SECOND(0.03125), SIXTY_FOURTH(0.015625);

    private final double value;

    Duration(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}

