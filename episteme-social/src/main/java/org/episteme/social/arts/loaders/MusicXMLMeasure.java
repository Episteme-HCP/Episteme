/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.social.arts.loaders;

import java.util.ArrayList;
import java.util.List;

public class MusicXMLMeasure {
    private int number;
    private MusicXMLTimeSignature timeSignature;
    private MusicXMLKeySignature keySignature;
    private List<MusicXMLNote> notes = new ArrayList<>();

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public MusicXMLTimeSignature getTimeSignature() { return timeSignature; }
    public void setTimeSignature(MusicXMLTimeSignature timeSignature) { this.timeSignature = timeSignature; }

    public MusicXMLKeySignature getKeySignature() { return keySignature; }
    public void setKeySignature(MusicXMLKeySignature keySignature) { this.keySignature = keySignature; }

    public List<MusicXMLNote> getNotes() { return notes; }
    public void addNote(MusicXMLNote note) { notes.add(note); }
}

