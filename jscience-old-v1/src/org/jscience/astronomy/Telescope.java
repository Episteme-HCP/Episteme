package org.jscience.astronomy;

import javax.vecmath.Vector3f;


/**
 * The Telescope class provides support for surface based telescopes.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Telescope extends Station {
    //the orientation they are pointing at
    /**
     * DOCUMENT ME!
     */
    private Vector3f orientation;

    /**
     * Creates a new Telescope object.
     *
     * @param name DOCUMENT ME!
     * @param longitude DOCUMENT ME!
     * @param latitude DOCUMENT ME!
     * @param height DOCUMENT ME!
     * @param orientation DOCUMENT ME!
     */
    public Telescope(String name, double longitude, double latitude,
        double height, Vector3f orientation) {
        super(name, longitude, latitude, height);
        this.orientation = orientation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3f getOrientation() {
        return orientation;
    }
}
