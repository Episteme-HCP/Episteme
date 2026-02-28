/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.episteme.ml.gml.util;

import org.episteme.ml.gml.infoset.CoordinateTuple;

import java.util.Iterator;


/**
 * Defines the interface that every coordinate tuple iterator must implement.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface CoordinateTupleIterator extends Iterator {
    /**
     * Convenience method that makes this iterator type-safe.
     *
     * @return DOCUMENT ME!
     */
    public CoordinateTuple nextCoordinateTuple();
}
