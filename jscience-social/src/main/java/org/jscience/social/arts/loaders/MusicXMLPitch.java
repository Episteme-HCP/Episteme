/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.social.arts.loaders;

public class MusicXMLPitch {
    private String step;
    private int octave;
    private int alter;

    public String getStep() { return step; }
    public void setStep(String step) { this.step = step; }

    public int getOctave() { return octave; }
    public void setOctave(int octave) { this.octave = octave; }

    public int getAlter() { return alter; }
    public void setAlter(int alter) { this.alter = alter; }
}

