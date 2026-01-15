/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         4-Sep-2001    Made Coordinates an
 * UnknownConstructOwner, because it is possible that somebody adds child
 * elements. amilanovic         29-Mar-2002   Updated for the new package
 * name.
 */
package org.jscience.ml.gml.infoset;

import org.jscience.ml.gml.util.CoordinateTupleIterator;


/**
 * Defines the interface that every GML coordinates' construct must implement.
 * The GML coordinates' construct contains a list of coordinate tuples. A
 * single coordinate tuple may be represented using the GML coord construct.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface Coordinates extends GMLConstruct, UnknownConstructOwner {
    /**
     * Provides access to coordinate tuples in this coordinates'
     * construct.
     *
     * @return Returns an iterator to coordinate tuples. Cannot be null.
     */
    public CoordinateTupleIterator getCoordinateTupleIterator();
}
