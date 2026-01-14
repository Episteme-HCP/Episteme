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

package org.jscience.util;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Vector;


/**
 * A class representing a bag data structure (a list that does not permit
 * null).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Bag extends AbstractList {
    /** DOCUMENT ME! */
    private Vector vector;

/**
     * Creates a new Bag object.
     */
    public Bag() {
        vector = new Vector();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param element DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void add(int index, Object element) {
        if (element != null) {
            vector.add(index, element);
        } else {
            throw new IllegalArgumentException("Bag don't allow null element.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean add(Object o) {
        if (o != null) {
            return vector.add(o);
        } else {
            throw new IllegalArgumentException("Bags don't allow null element.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean addAll(int index, Collection c) {
        if (!c.contains(null)) {
            return vector.addAll(index, c);
        } else {
            throw new IllegalArgumentException("Bags don't allow null element.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object remove(int index) {
        return vector.remove(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param element DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Object set(int index, Object element) {
        if (element != null) {
            return vector.set(index, element);
        } else {
            throw new IllegalArgumentException("Bags don't allow null element.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(int index) {
        return vector.get(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return vector.size();
    }

    //it is expected that the other operations will rely on the methods defined here
}
