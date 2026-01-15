/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.util;

import org.jscience.ml.gml.infoset.UnknownConstruct;

import java.util.Iterator;


/**
 * Defines the interface that every iterator over unknown constructs should
 * implement.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface UnknownConstructIterator extends Iterator {
    /**
     * Convenience method that makes this iterator type-safe.
     *
     * @return DOCUMENT ME!
     */
    public UnknownConstruct nextUnknownConstruct();
}
