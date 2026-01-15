package org.jscience.geography;

import org.jscience.util.Named;
import org.jscience.util.Positioned;


/**
 * A class representing a geographical spot, namely a feature. It is meant
 * to be used primarily to define places like human settlements, that is
 * places with a name.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Place extends Object implements Named, Positioned {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Boundary boundary;

/**
     * Creates a new Place object.
     *
     * @param name     DOCUMENT ME!
     * @param boundary DOCUMENT ME!
     */
    public Place(String name, Boundary boundary) {
        if ((name != null) && (name.length() > 0) && (boundary != null)) {
            this.name = name;
            this.boundary = boundary;
        } else {
            throw new IllegalArgumentException(
                "The Place constructor can't have null or empty arguments.");
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
    public Boundary getBoundary() {
        return boundary;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getPosition() {
        return boundary.getPosition();
    }
}
