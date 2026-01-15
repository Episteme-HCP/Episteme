package org.jscience.arts.musical;

import org.jscience.util.Named;


/**
 * A class representing an instrument.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Instrument extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private int kind;

/**
     * Creates a new Instrument object.
     *
     * @param name DOCUMENT ME!
     * @param kind DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Instrument(String name, int kind) {
        if (name != null) {
            this.name = name;
            this.kind = kind;
        } else {
            throw new IllegalArgumentException(
                "The Instrument constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKind() {
        return kind;
    }
}
