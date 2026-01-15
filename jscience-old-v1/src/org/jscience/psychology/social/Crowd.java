package org.jscience.psychology.social;

import org.jscience.biology.Population;
import org.jscience.biology.Species;


/**
 * A class representing a crowd or a sub population in which people share a
 * common reason for gathering but no other relation apart proximity.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Crowd extends Population {
    /** DOCUMENT ME! */
    private String motive;

/**
     * Creates a new Crowd object.
     *
     * @param species DOCUMENT ME!
     * @param motive  DOCUMENT ME!
     */
    public Crowd(Species species, String motive) {
        super(species);

        if (motive != null) {
            this.motive = motive;
        } else {
            throw new IllegalArgumentException("You can't set a null motive.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMotive() {
        return motive;
    }
}
