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

package org.jscience.chemistry.gui.extended.molecule;

import java.util.NoSuchElementException;
import java.util.Vector;


/**
 * Tripos RingVector class
 *
 * @author Mike Brusati (brusati@org.jscience.chemistry.gui.extended.com)
 *         Original Version.
 */
public class RingVector extends Vector {
/**
     * Default constructor
     */
    public RingVector() {
        super();
    }

/**
     * Capacity constructor
     *
     * @param cap initial capacity of vector
     */
    public RingVector(int cap) {
        super(cap, cap);
    }

/**
     * Capacity & increment constructor
     *
     * @param cap initial capacity of vector
     * @param inc increment factor
     */
    public RingVector(int cap, int inc) {
        super(cap, inc);
    }

/**
     * Copy constructor.
     *
     * @param rv RingVector to be copied
     */
    public RingVector(RingVector rv) {
        super(rv.size(), rv.size());

        for (int i = 0; i < rv.size(); i++) {
            Ring r = new Ring(rv.getRing(i));
            append(r);
        }
    }

    /**
     * Append a ring to the end of vector
     *
     * @param r ring to be appended
     */
    public final synchronized void append(Ring r) {
        addElement(r);
    }

    /**
     * Returns ring at specified index
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException if index >= capacity()
     */
    public final synchronized Ring getRing(int i)
        throws ArrayIndexOutOfBoundsException {
        return (Ring) elementAt(i);
    }

    /**
     * Set a ring at specified index; ring at index is replaced
     *
     * @param r ring to be set
     * @param i index to place ring
     */
    public final synchronized void set(Ring r, int i) {
        setElementAt(r, i);
    }

    /**
     * Insert a ring at specified index; other rings are shifted over
     *
     * @param r ring to be inserted
     * @param i index of insertion
     */
    public final synchronized void insert(Ring r, int i) {
        insertElementAt(r, i);
    }

    /**
     * Remove a ring; other rings are shifted down
     *
     * @param r ring to be removed
     *
     * @return <tt>true</tt> if ring was removed, else <tt>false</tt>
     */
    public final synchronized boolean remove(Ring r) {
        return removeElement(r);
    }

    /**
     * Remove ring at specified index; other rings are shifted down
     *
     * @param i index of ring
     *
     * @throws ArrayIndexOutOfBoundsException if index >= capacity()
     */
    public final synchronized void removeRing(int i)
        throws ArrayIndexOutOfBoundsException {
        removeElementAt(i);
    }

    /**
     * Returns first ring in RingVector
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException if size() == 0
     */
    public final synchronized Ring firstRing() throws NoSuchElementException {
        return (Ring) firstElement();
    }

    /**
     * Returns last ring in RingVector
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException if size() == 0
     */
    public final synchronized Ring lastRing() throws NoSuchElementException {
        return (Ring) lastElement();
    }

    /**
     * Compares this RingVector with another
     *
     * @param rv RingVector to compare with
     *
     * @return <tt>true</tt> if they are equal, else <tt>false</tt>
     */
    public boolean equals(RingVector rv) {
        if (rv == null) {
            return false;
        }

        if (size() != rv.size()) {
            return false;
        }

        for (int i = 0; i < size(); i++) {
            Ring r1 = getRing(i);
            Ring r2 = rv.getRing(i);

            if (!r1.equals(r2)) {
                return false;
            }
        }

        return true;
    }
} // end of class RingVector
