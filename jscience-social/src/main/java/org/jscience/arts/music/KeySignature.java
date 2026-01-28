/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.arts.music;

import java.io.Serializable;

/**
 * Represents a musical key signature.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class KeySignature implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int fifths;
    private final String mode;

    public KeySignature(int fifths, String mode) {
        this.fifths = fifths;
        this.mode = mode != null ? mode : "major";
    }

    public int getFifths() { return fifths; }
    public String getMode() { return mode; }
    
    // Common keys as constants
    public static final KeySignature C_MAJOR = new KeySignature(0, "major");
    public static final KeySignature G_MAJOR = new KeySignature(1, "major");
    public static final KeySignature D_MAJOR = new KeySignature(2, "major");
    public static final KeySignature A_MAJOR = new KeySignature(3, "major");
    public static final KeySignature E_MAJOR = new KeySignature(4, "major");
    public static final KeySignature B_MAJOR = new KeySignature(5, "major");
    public static final KeySignature F_SHARP_MAJOR = new KeySignature(6, "major");
    public static final KeySignature C_SHARP_MAJOR = new KeySignature(7, "major");
    public static final KeySignature F_MAJOR = new KeySignature(-1, "major");
    public static final KeySignature B_FLAT_MAJOR = new KeySignature(-2, "major");
    public static final KeySignature E_FLAT_MAJOR = new KeySignature(-3, "major");
    public static final KeySignature A_FLAT_MAJOR = new KeySignature(-4, "major");
    public static final KeySignature D_FLAT_MAJOR = new KeySignature(-5, "major");
    public static final KeySignature G_FLAT_MAJOR = new KeySignature(-6, "major");
    public static final KeySignature C_FLAT_MAJOR = new KeySignature(-7, "major");
    
    public static final KeySignature A_MINOR = new KeySignature(0, "minor");
    public static final KeySignature E_MINOR = new KeySignature(1, "minor");
    public static final KeySignature B_MINOR = new KeySignature(2, "minor");
    public static final KeySignature F_SHARP_MINOR = new KeySignature(3, "minor");
    public static final KeySignature C_SHARP_MINOR = new KeySignature(4, "minor");
    public static final KeySignature G_SHARP_MINOR = new KeySignature(5, "minor");
    public static final KeySignature D_SHARP_MINOR = new KeySignature(6, "minor");
    public static final KeySignature A_SHARP_MINOR = new KeySignature(7, "minor");
    public static final KeySignature D_MINOR = new KeySignature(-1, "minor");
    public static final KeySignature G_MINOR = new KeySignature(-2, "minor");
    public static final KeySignature C_MINOR = new KeySignature(-3, "minor");
    public static final KeySignature F_MINOR = new KeySignature(-4, "minor");
    public static final KeySignature B_FLAT_MINOR = new KeySignature(-5, "minor");
    public static final KeySignature E_FLAT_MINOR = new KeySignature(-6, "minor");
    public static final KeySignature A_FLAT_MINOR = new KeySignature(-7, "minor");

    @Override
    public String toString() {
        return "KeySignature{fifths=" + fifths + ", mode='" + mode + "'}";
    }
}
