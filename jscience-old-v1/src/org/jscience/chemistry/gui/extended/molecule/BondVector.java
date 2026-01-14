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
 * Tripos BondVector class
 *
 * @author Jason Plurad (jplurad@org.jscience.chemistry.gui.extended.com)
 * @version 0.5 29 Jul 96
 *
 * @see Bond
 */
public class BondVector extends Vector {
/**
     * Default constructor
     */
    public BondVector() {
        super();
    }

/**
     * Capacity constructor
     *
     * @param cap initial capacity of vector
     */
    public BondVector(int cap) {
        super(cap, cap);
    }

/**
     * Capacity & increment constructor
     *
     * @param cap initial capacity of vector
     * @param inc increment factor
     */
    public BondVector(int cap, int inc) {
        super(cap, inc);
    }

/**
     * Copy constructor.
     *
     * @param bv BondVector to be copied
     */
    public BondVector(BondVector bv) {
        super(bv.size(), bv.size());

        for (int i = 0; i < bv.size(); i++) {
            Bond b = new Bond(bv.getBond(i));
            append(b);
        }
    }

    /**
     * Append a bond to the end of vector
     *
     * @param a bond to be appended
     */
    public final synchronized void append(Bond a) {
        addElement(a);
    }

    /**
     * Returns bond at specified index
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException if index >= capacity()
     */
    public final synchronized Bond getBond(int i)
        throws ArrayIndexOutOfBoundsException {
        return (Bond) elementAt(i);
    }

    /**
     * Set a bond at specified index; bond at index is replaced
     *
     * @param a bond to be set
     * @param i index to place bond
     */
    public final synchronized void set(Bond a, int i) {
        setElementAt(a, i);
    }

    /**
     * Insert a bond at specified index; other bonds are shifted over
     *
     * @param a bond to be inserted
     * @param i index of insertion
     */
    public final synchronized void insert(Bond a, int i) {
        insertElementAt(a, i);
    }

    /**
     * Remove a bond; other bonds are shifted down
     *
     * @param a bond to be removed
     *
     * @return <tt>true</tt> if bond was removed, else <tt>false</tt>
     */
    public final synchronized boolean removeBond(Bond a) {
        return removeElement(a);
    }

    /**
     * Remove bond at specified index; other bonds are shifted down
     *
     * @param i index of bond
     *
     * @throws ArrayIndexOutOfBoundsException if index >= capacity()
     */
    public final synchronized void removeBond(int i)
        throws ArrayIndexOutOfBoundsException {
        removeElementAt(i);
    }

    /**
     * Returns first bond in BondVector
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException if size() == 0
     */
    public final synchronized Bond firstBond() throws NoSuchElementException {
        return (Bond) firstElement();
    }

    /**
     * Returns last bond in BondVector
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException if size() == 0
     */
    public final synchronized Bond lastBond() throws NoSuchElementException {
        return (Bond) lastElement();
    }

    /**
     * Compares this BondVector with another
     *
     * @param av BondVector to compare with
     *
     * @return <tt>true</tt> if they are equal, else <tt>false</tt>
     */
    public boolean equals(BondVector av) {
        if (av == null) {
            return false;
        }

        if (size() != av.size()) {
            return false;
        }

        for (int i = 0; i < size(); i++) {
            Bond a1 = getBond(i);
            Bond a2 = av.getBond(i);

            if (!a1.equals(a2)) {
                return false;
            }
        }

        return true;
    }
} // end of class BondVector
