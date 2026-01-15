/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         4-Sep-2001    Made Coord a
 * PropertyOwner, hence X,Y,Z will be considered as such. amilanovic
 * 29-Mar-2002   Updated for the new package name.
 */
package org.jscience.ml.gml.infoset;

/**
 * Defines interface that every class representing the GML coord construct must
 * implement. Coord represents a coordinate 3-tuple.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface Coord extends CoordinateTuple, GMLConstruct,
    UnknownConstructOwner, PropertyOwner {
    // Coord properties
    /** DOCUMENT ME! */
    public static final String X_PROP = "X";

    /** DOCUMENT ME! */
    public static final String Y_PROP = "Y";

    /** DOCUMENT ME! */
    public static final String Z_PROP = "Z";
}
