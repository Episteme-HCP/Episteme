/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.arts.music;

import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a musical measure (bar).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Measure implements Serializable {
    private static final long serialVersionUID = 1L;

    @Attribute
    private int number;

    @Attribute
    private TimeSignature timeSignature;

    @Attribute
    private KeySignature keySignature;

    private final List<Note> notes = new ArrayList<>();

    public Measure() {
    }

    public Measure(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public TimeSignature getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(TimeSignature timeSignature) {
        this.timeSignature = timeSignature;
    }

    public KeySignature getKeySignature() {
        return keySignature;
    }

    public void setKeySignature(KeySignature keySignature) {
        this.keySignature = keySignature;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }
}
