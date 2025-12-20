/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.arts.music;

/**
 * Represents a musical instrument.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Instrument {

    public enum Family {
        STRING, WOODWIND, BRASS, PERCUSSION, KEYBOARD, ELECTRONIC
    }

    public enum Type {
        // Strings
        VIOLIN, VIOLA, CELLO, DOUBLE_BASS, GUITAR, HARP,
        // Woodwinds
        FLUTE, CLARINET, OBOE, BASSOON, SAXOPHONE,
        // Brass
        TRUMPET, TROMBONE, FRENCH_HORN, TUBA,
        // Percussion
        DRUMS, TIMPANI, XYLOPHONE, MARIMBA, TRIANGLE,
        // Keyboard
        PIANO, ORGAN, HARPSICHORD, SYNTHESIZER,
        // Electronic
        ELECTRIC_GUITAR, ELECTRIC_BASS, DRUM_MACHINE
    }

    private final String name;
    private final Family family;
    private final Type type;
    private int lowestMidiNote;
    private int highestMidiNote;
    private boolean transposing;
    private String tuning;

    public Instrument(String name, Family family, Type type) {
        this.name = name;
        this.family = family;
        this.type = type;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Family getFamily() {
        return family;
    }

    public Type getType() {
        return type;
    }

    public int getLowestMidiNote() {
        return lowestMidiNote;
    }

    public int getHighestMidiNote() {
        return highestMidiNote;
    }

    public boolean isTransposing() {
        return transposing;
    }

    public String getTuning() {
        return tuning;
    }

    // Setters
    public void setRange(int lowest, int highest) {
        this.lowestMidiNote = lowest;
        this.highestMidiNote = highest;
    }

    public void setTransposing(boolean transposing) {
        this.transposing = transposing;
    }

    public void setTuning(String tuning) {
        this.tuning = tuning;
    }

    /**
     * Checks if the instrument can play a note.
     */
    public boolean canPlay(Note note) {
        int midi = note.getMidiNote();
        return midi >= lowestMidiNote && midi <= highestMidiNote;
    }

    /**
     * Returns range in semitones.
     */
    public int getRange() {
        return highestMidiNote - lowestMidiNote;
    }

    @Override
    public String toString() {
        return String.format("%s (%s/%s)", name, family, type);
    }

    // Common instruments
    public static Instrument piano() {
        Instrument i = new Instrument("Piano", Family.KEYBOARD, Type.PIANO);
        i.setRange(21, 108); // A0 to C8
        return i;
    }

    public static Instrument violin() {
        Instrument i = new Instrument("Violin", Family.STRING, Type.VIOLIN);
        i.setRange(55, 103); // G3 to G7
        i.setTuning("G-D-A-E");
        return i;
    }

    public static Instrument guitar() {
        Instrument i = new Instrument("Acoustic Guitar", Family.STRING, Type.GUITAR);
        i.setRange(40, 88); // E2 to E6
        i.setTuning("E-A-D-G-B-E");
        return i;
    }
}
