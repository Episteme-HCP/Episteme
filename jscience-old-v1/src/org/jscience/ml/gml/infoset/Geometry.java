/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

import org.jscience.ml.gml.util.CoordinateTupleIterator;
import org.jscience.ml.gml.util.GeometryIterator;


/**
 * Defines the interface every GML geometry must implement.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface Geometry extends GMLConstruct, PropertyOwner,
    UnknownConstructOwner // direct ownership
 {
    // known attribute names (GML v2.06)
    /** DOCUMENT ME! */
    public static final String GID_ATTR = "gid";

    /** DOCUMENT ME! */
    public static final String SRS_NAME_ATTR = "srsName";

    /**
     * Returns the name of the Spatial Reference System (SRS)
     *
     * @return Null if the SRS is unspecified.
     */
    public String getSRSName();

    /**
     * Returns the Id of this geometry, presumably specified with gid
     * attribute.
     *
     * @return Null if no Id attribute is specified.
     */
    public String getId();

    /**
     * A convenience method to gain access to geometry coordinates.
     *
     * @return Iterator to coordinate tuples. It is never null.
     */
    public CoordinateTupleIterator getCoordinateTupleIterator();

    /**
     * A convenience method to gain access to inner boundaries if
     * available.
     *
     * @return Iterator to inner boundaries. It is never null.
     */
    public GeometryIterator getInnerBoundaryIterator();
}
