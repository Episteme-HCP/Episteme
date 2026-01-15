package org.jscience.arts.hobbies;

import org.jscience.arts.ArtsConstants;

import org.jscience.util.Named;


/**
 * A class representing a hobby (a "for the fun" passion in life).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Hobby extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private int art;

/**
     * Creates a new Hobby object.
     *
     * @param name DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Hobby(String name) {
        if (name != null) {
            this.name = name;
            this.art = ArtsConstants.UNKNOWN;
        } else {
            throw new IllegalArgumentException(
                "The Hobby constructor can't have null arguments.");
        }
    }

/**
     * Creates a new Hobby object.
     *
     * @param name DOCUMENT ME!
     * @param art  DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Hobby(String name, int art) {
        if (name != null) {
            this.name = name;
            this.art = art;
        } else {
            throw new IllegalArgumentException(
                "The Hobby constructor can't have null arguments.");
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
    public int getArt() {
        return art;
    }

    //just in case you are not sure about the category
    /**
     * DOCUMENT ME!
     *
     * @param art DOCUMENT ME!
     */
    public void setArt(int art) {
        this.art = art;
    }
}
