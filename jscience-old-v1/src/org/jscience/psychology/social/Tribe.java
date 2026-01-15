package org.jscience.psychology.social;

import org.jscience.geography.Place;

import org.jscience.sociology.Culture;

import org.jscience.util.Named;


/**
 * A class representing the basic facts about an organized human group.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Tribe extends HumanGroup implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Culture culture;

/**
     * Creates a new Tribe object.
     *
     * @param name    DOCUMENT ME!
     * @param culture DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Tribe(String name, Culture culture) {
        if ((name != null) && (name.length() > 0) && (culture != null)) {
            this.name = name;
            this.culture = culture;
        } else {
            throw new IllegalArgumentException(
                "The Tribe constructor can't have null or empty arguments.");
        }
    }

/**
     * Creates a new Tribe object.
     *
     * @param name            DOCUMENT ME!
     * @param formalTerritory DOCUMENT ME!
     * @param culture         DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Tribe(String name, Place formalTerritory, Culture culture) {
        super(formalTerritory);

        if ((name != null) && (name.length() > 0) && (culture != null)) {
            this.name = name;
            this.culture = culture;
        } else {
            throw new IllegalArgumentException(
                "The Tribe constructor can't have null or empty arguments.");
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
     * @param name DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setName(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException(
                "The name can't be null or empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Culture getCulture() {
        return culture;
    }

    /**
     * DOCUMENT ME!
     *
     * @param culture DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setCulture(Culture culture) {
        if ((culture != null)) {
            this.culture = culture;
        } else {
            throw new IllegalArgumentException("The culture must be non null.");
        }
    }
}
