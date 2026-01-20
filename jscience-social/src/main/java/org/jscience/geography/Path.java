package org.jscience.geography;

import org.jscience.geography.coordinates.Coord;

import org.jscience.util.Positioned;


/**
 * A class representing a path or track.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//every path (from somewhere to somewhere)
//railroads and roads mostly
//you should feed this class with valid coordinates
//also look into org.jscience.geometry.Polyline3D
public class Path extends Object implements Positioned {
    /** DOCUMENT ME! */
    private Coord[] coords;

    //this defines a path which may be contain loops
    /**
     * Creates a new Path object.
     *
     * @param coords DOCUMENT ME!
     */
    public Path(Coord[] coords) {
        if ((coords != null) && (coords.length > 0)) {
            this.coords = coords;
        } else {
            throw new IllegalArgumentException(
                "The Path constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Path getReversePath() {
        Coord[] resultCoords;

        resultCoords = new Coord[coords.length];

        for (int i = 0; i < coords.length; i++) {
            resultCoords[i] = coords[coords.length - 1 - i];
        }

        return new Path(resultCoords);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Coord[] getCoords() {
        return coords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IndexOutOfBoundsException DOCUMENT ME!
     */
    public Coord getCoordAtI(int i) {
        if ((i >= 0) && (i < coords.length)) {
            return coords[i];
        } else {
            throw new IndexOutOfBoundsException(
                "You can only get the coordinates from 0 to getSize()-1.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize() {
        return coords.length;
    }

    //true iff there is only one point
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDot() {
        return coords.length == 1;
    }

    //true iff the first and last coordinates are the same
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isClosed() {
        return coords[0].equals(coords[coords.length - 1]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getPosition() {
        return this;
    }
}
