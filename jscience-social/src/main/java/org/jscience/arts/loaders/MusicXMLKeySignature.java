/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.arts.loaders;

public class MusicXMLKeySignature {
    private int fifths;
    private String mode;

    public int getFifths() { return fifths; }
    public void setFifths(int fifths) { this.fifths = fifths; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
}
