/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

import org.jscience.ml.gml.util.PropertyIterator;


/**
 * Defines the interface that must be implemented by every class that owns
 * properties (e.g. features, geometries). Note that the owning relationship
 * is a loose one, and depends on the level of abstraction. This class does
 * not limit its use to direct owners of features. Rather its semantics depend
 * on the context where it is used.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface PropertyOwner extends GMLConstructOwner {
    /**
     * Provides access to properties owned by this PropertyOwner.
     *
     * @return Property iterator that can be used for iterating on owned
     *         properties. The iterator cannot be null.
     */
    public PropertyIterator getPropertyIterator();
}
