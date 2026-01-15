/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.dom;

import org.jscience.ml.gml.infoset.GMLConstruct;
import org.jscience.ml.gml.util.GMLConstructIterator;

import java.util.Iterator;


/**
 * A sample implementation of the GMLConstructIterator interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class GMLConstructIteratorImpl implements GMLConstructIterator {
    /** DOCUMENT ME! */
    private Iterator iterator_;

/**
     * Constructs this iterator as a type-safe wrapper around another iterator.
     *
     * @param iterator DOCUMENT ME!
     */
    public GMLConstructIteratorImpl(Iterator iterator) {
        iterator_ = iterator;
    }

    /**
     * Convenience method that makes this iterator type-safe.
     *
     * @return DOCUMENT ME!
     */
    public GMLConstruct nextGMLConstruct() {
        return (GMLConstruct) next();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object next() {
        return iterator_.next();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasNext() {
        return iterator_.hasNext();
    }

    /**
     * DOCUMENT ME!
     */
    public void remove() {
        // can't do this
    }
}
