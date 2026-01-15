/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

import org.jscience.ml.gml.util.UnknownConstructIterator;


/**
 * Defines the interface that an owner of unknown constructs must implement.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface UnknownConstructOwner extends GMLConstructOwner {
    /**
     * Provides access to unknown constructs owned by this GML
     * construct.
     *
     * @return Iterator that can be used for iterating on owned unknown
     *         constructs. The iterator cannot be null.
     */
    public UnknownConstructIterator getUnknownConstructIterator();
}
