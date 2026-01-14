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

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a relation in n dimensions, or R(n). A unary realtion
 * is the typical set.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//should implement Cloneable
//should be updated to Java 1.5 generics
public interface Relation extends Set {
    //the fundamental number of elements n
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension();

    //object must be a Nary element whose dimension equals getDimension();
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean add(Object o);

    //refuse null element
    /**
     * DOCUMENT ME!
     *
     * @param nAry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean add(NAry nAry);

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean add(Object[] o);

    /**
     * DOCUMENT ME!
     */
    public void clear();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone();

    //signature kept for compatibility
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Object o);

    /**
     * DOCUMENT ME!
     *
     * @param nAry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(NAry nAry);

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Object[] o);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEmpty();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator();

    //signature kept for compatibility
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean remove(Object o);

    /**
     * DOCUMENT ME!
     *
     * @param nAry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean remove(NAry nAry);

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean remove(Object[] o);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size();

    //r must be a Nary element whose dimension equals getDimension();
    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean addAll(Relation r);

    //collection must be a Relation
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean addAll(Collection c);

    //r must be a Nary element whose dimension equals getDimension();
    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean containsAll(Relation r);

    //collection must be a Relation
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean containsAll(Collection c);

    //r must be a Nary element whose dimension equals getDimension();
    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean removeAll(Relation r);

    //collection must be a Relation
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean removeAll(Collection c);

    //r must be a Nary element whose dimension equals getDimension();
    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean retainAll(Relation r);

    //collection must be a Relation
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean retainAll(Collection c);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] toArray();

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] toArray(NAry[] a);

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] toArray(Object[] a);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString();

    //all elements must be the same
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o);

    //looks at the nth dimension of each element if the object can be found
    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Object object, int i);

    //returns the Set of elements that appear in any dimension
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAll();

    //returns the Set of elements that appear in dimension i
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set get(int i);

    //which returns the Set of NAry with object appearing in the dimension i of the Relation
    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getElements(Object object, int i);

    //I don't know if there is any definition of Reflexion for nAry relation
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFunction(int i);
}
