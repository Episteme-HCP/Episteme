/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

import org.jscience.ml.gml.util.GeometryIterator;


/**
 * Defines the interface that must be implemented by every class that "owns"
 * geometries (e.g. properties, geometry collections). Note that the owning
 * relationship is a loose one, and depends on the level of abstraction. This
 * class does not limit its use to direct owners of geometries. Rather its
 * semantics depend on the context where it is used.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface GeometryOwner extends GMLConstructOwner {
    /**
     * Provides access to geometries owned by this GeometryOwner.
     *
     * @return GeometryIterator that can be used for iterating on geometries.
     *         It is never null.
     */
    public GeometryIterator getGeometryIterator();
}
