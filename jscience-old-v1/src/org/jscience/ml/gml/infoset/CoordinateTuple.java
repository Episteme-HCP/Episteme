/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

/**
 * Defines the interface that every GML coordinate tuple must implement. A
 * coordinate tuple does NOT have a corresponding GML element. It is an
 * abstraction of a coordinate tuple, which may appear in coord and
 * coordinates GML constructs. Coord, which is a coordinate tuple with a
 * corresponding GML element, extends this interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface CoordinateTuple {
    // defines the indices for the first three coordinates (x,y,z)
    /** DOCUMENT ME! */
    public static final int X_INDEX = 0;

    /** DOCUMENT ME! */
    public static final int Y_INDEX = 1;

    /** DOCUMENT ME! */
    public static final int Z_INDEX = 2;

    /**
     * Convenience method to retrieve the first coordinate.
     *
     * @return Cannot be null.
     */
    public Coordinate getX();

    /**
     * Convenience method to retrieve the second coordinate.
     *
     * @return Can be null if Y-coordinate is not present.
     */
    public Coordinate getY();

    /**
     * Convenience method to retrieve the third coordinate.
     *
     * @return Can be null if Z-coordinate is not present.
     */
    public Coordinate getZ();

    /**
     * Returns the n-th coordinate of this coordinate tuple.
     *
     * @param index Represents an index into the coordinate tuple. The lowest
     *        index is 0.
     *
     * @return Returns null if the index is out of range.
     */
    public Coordinate getCoordinate(int index);
}
