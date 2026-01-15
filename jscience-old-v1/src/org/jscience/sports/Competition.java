package org.jscience.sports;

import org.jscience.util.Named;


/**
 * A class representing a competition (where various matchs in various
 * sports occur).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Competition extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Match[] matches;

    /** DOCUMENT ME! */
    private String[] prizes; //from the first to the last one, description and money prize

/**
     * Creates a new Competition object.
     *
     * @param name    DOCUMENT ME!
     * @param prizes  DOCUMENT ME!
     * @param matches DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Competition(String name, String[] prizes, Match[] matches) {
        if ((name != null) && (prizes != null) && (matches != null)) {
            this.name = name;
            this.matches = matches;
            this.prizes = prizes;
        } else {
            throw new IllegalArgumentException(
                "The Competition constructor can't have null arguments.");
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
    public String[] getPrizes() {
        return prizes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Match[] getMatches() {
        return matches;
    }
}
