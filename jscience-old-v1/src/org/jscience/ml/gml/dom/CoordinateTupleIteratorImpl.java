/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.dom;

import org.jscience.ml.gml.infoset.CoordinateTuple;
import org.jscience.ml.gml.util.CoordinateTupleIterator;

import java.util.Iterator;


/**
 * A sample implementation of the CoordinateTupleIterator i/f.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class CoordinateTupleIteratorImpl implements CoordinateTupleIterator {
    // the underlying iterator
    /** DOCUMENT ME! */
    private Iterator iterator_;

/**
     * Constructs this iterator as a type-safe wrapper around another iterator.
     *
     * @param iterator DOCUMENT ME!
     */
    public CoordinateTupleIteratorImpl(Iterator iterator) {
        iterator_ = iterator;
    }

    /**
     * Convenience method that makes this iterator type-safe.
     *
     * @return DOCUMENT ME!
     */
    public CoordinateTuple nextCoordinateTuple() {
        return (CoordinateTuple) next();
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
     *
     * @return DOCUMENT ME!
     */
    public Object next() {
        return iterator_.next();
    }

    /**
     * DOCUMENT ME!
     */
    public void remove() {
        // can't do this
    }
}
