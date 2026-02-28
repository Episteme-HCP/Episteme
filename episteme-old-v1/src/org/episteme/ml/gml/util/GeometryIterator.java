/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.episteme.ml.gml.util;

import org.episteme.ml.gml.infoset.Geometry;

import java.util.Iterator;


/**
 * Defines the interface every geometry iterator must implement.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface GeometryIterator extends Iterator {
    /**
     * Convenience method that makes this iterator type-safe.
     *
     * @return DOCUMENT ME!
     */
    public Geometry nextGeometry();
}
