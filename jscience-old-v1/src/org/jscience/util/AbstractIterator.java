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

import java.util.*;


/**
 * makes it very easy to implement either an Enumeration or an Iterator.
 * The only abstract method is <code>getNextObject()</code>. This method is to
 * supply the next object until it either returns null or throws an exception,
 * which marks that the last object was reached. In addition this class
 * supplies convenient static methods to convert an Enumeration to an Iterator
 * and vice versa.
 *
 * @author Holger Antelmann
 * @param <E> DOCUMENT ME!
 * @since 04/16/2004
 */
public abstract class AbstractIterator<E> implements Enumeration<E>,
        Iterator<E> {
    /**
     * DOCUMENT ME!
     */
    E last = null;

    /**
     * DOCUMENT ME!
     */
    boolean next = false;

    /**
     * if no more Element is available, this method is to return null
     * or throw any Exception
     *
     * @return DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    abstract protected E getNextObject() throws Exception;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public E next() {
        if (!next) {
            throw new NoSuchElementException();
        }

        return last;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public E nextElement() {
        return next();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasMoreElements() {
        return hasNext();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasNext() {
        try {
            last = getNextObject();

            if (last == null) {
                next = false;
            } else {
                next = true;
            }
        } catch (Exception ex) {
            next = false;
        }

        return next;
    }

    /**
     * throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * converts an Enumeration into an Iterator
     *
     * @param <F> DOCUMENT ME!
     * @param e   DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public static <F> Iterator<F> iterate(final Enumeration<F> e) {
        return new Iterator<F>() {
            public boolean hasNext() {
                return e.hasMoreElements();
            }

            public F next() {
                return e.nextElement();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * converts an Iterator into an Enumeration
     *
     * @param <F> DOCUMENT ME!
     * @param i   DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static <F> Enumeration<F> enumerate(final Iterator<F> i) {
        return new Enumeration<F>() {
            public boolean hasMoreElements() {
                return i.hasNext();
            }

            public F nextElement() {
                return i.next();
            }
        };
    }

    /**
     * sorts the given Enumeration by the natural order of its elements
     *
     * @param <F> DOCUMENT ME!
     * @param e   DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static <F extends Comparable<? super F>> Enumeration<F> sortEnumeration(
            Enumeration<F> e) {
        ArrayList<F> list = new ArrayList<F>();

        while (e.hasMoreElements()) {
            list.add(e.nextElement());
        }

        Collections.sort(list);

        return Collections.enumeration(list);
    }

    /**
     * reverses the given Enumeration
     *
     * @param <F> DOCUMENT ME!
     * @param e   DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static <F> Enumeration<F> reverseEnumeration(Enumeration<F> e) {
        ArrayList<F> list = new ArrayList<F>();

        while (e.hasMoreElements()) {
            list.add(e.nextElement());
        }

        Collections.reverse(list);

        return Collections.enumeration(list);
    }

    /**
     * DOCUMENT ME!
     *
     * @param <F> DOCUMENT ME!
     * @param i   DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static <F> List<F> list(Iterator<F> i) {
        ArrayList<F> list = new ArrayList<F>();

        while (i.hasNext()) {
            list.add(i.next());
        }

        return list;
    }

    /**
     * DOCUMENT ME!
     *
     * @param <F> DOCUMENT ME!
     * @param e   DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static <F> List<F> list(Enumeration<F> e) {
        return list(iterate(e));
    }
}
