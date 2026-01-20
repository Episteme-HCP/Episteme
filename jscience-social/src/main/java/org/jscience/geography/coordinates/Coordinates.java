//this class is under GPL
/**
 @brief Declaration of coordinate base classes.
 */
package org.jscience.geography.coordinates;

/**
 * The Coordinates interface is the base class for all coordinate types..
 *
 * @see Coord2D, Coord3D
 */
public interface Coordinates {
    /// Returns an array containing the coordinate component values.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getValues();
}
