/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
