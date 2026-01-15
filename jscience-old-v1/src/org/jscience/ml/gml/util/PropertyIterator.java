/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.util;

import org.jscience.ml.gml.infoset.Property;

import java.util.Iterator;


/**
 * Defines the interface every property iterator must implement.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface PropertyIterator extends Iterator {
    /**
     * Convenience method that makes this iterator type-safe.
     *
     * @return DOCUMENT ME!
     */
    public Property nextProperty();
}
