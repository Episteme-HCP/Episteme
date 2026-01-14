/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.arts.musical;

/**
 * A class representing a musical event. Although this is a basic system,
 * it can nevertheless be useful to code actual music pieces.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Note extends Object {
    /** DOCUMENT ME! */
    private Instrument instrument;

    /** DOCUMENT ME! */
    private double note; //the frequency of the note, or for percussion the hit drum (using the conventional notation for drums)

    /** DOCUMENT ME! */
    private double duration;

    /** DOCUMENT ME! */
    private String comment; //the maestro comments, for example play softly

    /** DOCUMENT ME! */
    private String voice; //the text pronounced

/**
     * Creates a new Note object.
     *
     * @param instrument DOCUMENT ME!
     * @param note       DOCUMENT ME!
     * @param duration   DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Note(Instrument instrument, double note, double duration) {
        if (instrument != null) {
            this.instrument = instrument;
            this.note = note;
            this.duration = duration;
            this.comment = null;
            this.voice = null;
        } else {
            throw new IllegalArgumentException(
                "The Note constructor can't have a null instrument.");
        }
    }

/**
     * Creates a new Note object.
     *
     * @param instrument DOCUMENT ME!
     * @param note       DOCUMENT ME!
     * @param duration   DOCUMENT ME!
     * @param comment    DOCUMENT ME!
     * @param voice      DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Note(Instrument instrument, double note, double duration,
        String comment, String voice) {
        if (instrument != null) {
            this.instrument = instrument;
            this.note = note;
            this.duration = duration;
            this.comment = comment;
            this.voice = voice;
        } else {
            throw new IllegalArgumentException(
                "The Note constructor can't have a null instrument.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getNote() {
        return note;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDuration() {
        return duration;
    }

    //may be null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComment() {
        return comment;
    }

    //may be null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getVoice() {
        return voice;
    }
}
