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

import java.util.Iterator;


/**
 * This interface defines a mathematical set. The name duplicates the java.util.Set and shares much the same intent although this class is meant to have the genuine meaning. You may use this interface to define finite or infinite sets.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @planetmath Set
 */
public interface Set {
    /**
     * Returns the number of elements in this set (its cardinality).
     *
     * @return the number of elements in this set (its cardinality).
     */
    public int cardinality();

    /**
     * Returns true if this set contains no elements.
     *
     * @return true if this set contains no elements.
     */
    public boolean isEmpty();

    /**
     * Returns an iterator over the elements in this set (optional
     * operation, as it is meaningless for infinite sets).
     *
     * @return an iterator over the elements in this set.
     */
    public Iterator iterator();

    /**
     * Performs the union of this set with another.
     *
     * @param set a set.
     *
     * @return the union of the two sets.
     */
    public Set union(Set set);

    /**
     * Performs the intersection of this set with another.
     *
     * @param set a set.
     *
     * @return the intersection of the two sets.
     */
    public Set intersection(Set set);

    /**
     * Removes the specified element from this set if it is present
     * (optional operation).
     *
     * @param o - object to be removed from this set, if present.
     *
     * @return true if the set contained the specified element.
     */
    public boolean remove(Object o);

    /**
     * Returns true if this set contains the specified element.
     *
     * @param o - element whose presence in this set is to be tested.
     *
     * @return true if this set contains the specified element.
     */
    public boolean contains(Object o);

    /**
     * Compares the specified object with this set for equality.
     *
     * @param o - Object to be compared for equality with this set.
     *
     * @return true if the specified Object is equal to this set.
     */
    public boolean equals(Object o);

    /**
     * Returns the hash code value for this set.
     *
     * @return the hash code value for this set.
     */
    public int hashCode();

    /**
     * Returns an array containing all of the elements in this set
     * (optional operation, as it is meaningless for infinite sets).
     *
     * @return an array containing all of the elements in this set.
     */
    public Object[] toArray();
}
