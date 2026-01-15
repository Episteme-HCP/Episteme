/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.util;

import org.jscience.ml.gml.infoset.GMLConstruct;

import java.util.Iterator;


/**
 * Defines an interface for an iterator on GML constructs.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface GMLConstructIterator extends Iterator {
    /**
     * Convenience method that makes this iterator type-safe.
     *
     * @return DOCUMENT ME!
     */
    public GMLConstruct nextGMLConstruct();
}
