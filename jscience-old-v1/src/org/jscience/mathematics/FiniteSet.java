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

package org.jscience.mathematics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A set containing a finite number of elements. This class provides a
 * bridge between <code>java.util.Set</code> and
 * <code>org.jscience.mathematics.Set</code>.
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 * @version 1.1
 */

//we don't check if members of the sets actually implement Member, although we should
//we could also use the 1.5 generics feature here
public final class FiniteSet extends Object implements org.jscience.mathematics.Set {
    /** DOCUMENT ME! */
    public final static FiniteSet EMPTY = new FiniteSet();

    /** DOCUMENT ME! */
    private final Set elements;

/**
     * Constructs a finite set.
     */
    private FiniteSet() {
        elements = new HashSet();
    }

/**
     * Constructs a finite set.
     *
     * @param set a set of elements
     */
    private FiniteSet(Set set) {
        elements = set;
    }

/**
     * Constructs a finite set.
     *
     * @param set a set of elements
     */
    public FiniteSet(FiniteSet set) {
        elements = set.elements;
    }

/**
     * Constructs a finite set.
     *
     * @param elem a element for the set
     */
    public FiniteSet(Object elem) {
        elements = new HashSet();
        elements.add(elem);
    }

    /**
     * Returns the cardinality.
     *
     * @return DOCUMENT ME!
     */
    public int cardinality() {
        return elements.size();
    }

    /**
     * Returns true if this set contains no elements.
     *
     * @return true if this set contains no elements.
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Performs the union of this set with another.
     *
     * @param set a set.
     *
     * @return the union of the two sets.
     */
    public org.jscience.mathematics.Set union(org.jscience.mathematics.Set set) {
        Set union = new HashSet(elements);
        union.addAll(((FiniteSet) set).elements);

        return new FiniteSet(union);
    }

    /**
     * Performs the intersection of this set with another.
     *
     * @param set a set.
     *
     * @return the intersection of the two sets.
     */
    public org.jscience.mathematics.Set intersection(
        org.jscience.mathematics.Set set) {
        Set intersection = new HashSet(elements);
        intersection.retainAll(((FiniteSet) set).elements);

        return new FiniteSet(intersection);
    }

    /**
     * Returns true if this set contains the specified element.
     *
     * @param o - element whose presence in this set is to be tested.
     *
     * @return true if this set contains the specified element.
     */
    public boolean contains(Object o) {
        return elements.contains(o);
    }

    /**
     * Removes the specified element from this set if it is present
     * (optional operation).
     *
     * @param o - object to be removed from this set, if present.
     *
     * @return true if the set contained the specified element.
     */
    public boolean remove(Object o) {
        return elements.remove(o);
    }

    /**
     * Compares the specified object with this set for equality.
     *
     * @param s - Object to be compared for equality with this set.
     *
     * @return true if the specified Object is equal to this set.
     */
    public boolean equals(Object s) {
        return (s != null) && (s instanceof FiniteSet) &&
        elements.equals(((FiniteSet) s).elements);
    }

    /**
     * Returns the hash code value for this set.
     *
     * @return the hash code value for this set.
     */
    public int hashCode() {
        return elements.hashCode();
    }

    /**
     * Returns a string representing this set.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return elements.toString();
    }

    /**
     * Returns an iterator over the elements in this set.
     *
     * @return an iterator over the elements in this set.
     */
    public Iterator iterator() {
        return elements.iterator();
    }

    /**
     * Returns the elements of this set.
     *
     * @return DOCUMENT ME!
     */
    public Set getElements() {
        return elements;

        //may be we should return Collections.unmodifiableSet(elements);
    }

    /**
     * Returns an array containing all of the elements in this set.
     *
     * @return an array containing all of the elements in this set.
     */
    public Object[] toArray() {
        return elements.toArray();
    }
}
