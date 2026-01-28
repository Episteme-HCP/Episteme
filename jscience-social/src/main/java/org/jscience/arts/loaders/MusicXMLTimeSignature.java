/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.arts.loaders;

public class MusicXMLTimeSignature {
    private int beats;
    private int beatType;

    public int getBeats() { return beats; }
    public void setBeats(int beats) { this.beats = beats; }

    public int getBeatType() { return beatType; }
    public void setBeatType(int beatType) { this.beatType = beatType; }
}
