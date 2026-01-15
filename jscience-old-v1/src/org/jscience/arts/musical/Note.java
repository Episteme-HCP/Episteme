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
