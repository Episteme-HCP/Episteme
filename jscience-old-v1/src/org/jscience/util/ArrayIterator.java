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

import java.lang.reflect.Array;

import java.util.NoSuchElementException;


/**
 * This Iterator is used to iterate over Arrays.
 */
public class ArrayIterator implements java.util.Iterator {
    /** DOCUMENT ME! */
    private Object array;

    /** DOCUMENT ME! */
    private int position;

    /** DOCUMENT ME! */
    private int length;

/**
     * create an Iterator for the Array array.
     *
     * @param array java.lang.Object
     * @throws UnsupportedOperationException if array is not an Array
     */
    public ArrayIterator(Object array) {
        if (!array.getClass().isArray()) {
            throw new UnsupportedOperationException("ArrayIterator must be " +
                "initialized with an Array to iterate over.");
        }

        this.array = array;
        position = 0;
        length = Array.getLength(array);
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In
     * other words, returns <tt>true</tt> if <tt>next</tt> would return an
     * element rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext() {
        return (position < length);
    }

    /**
     * Returns the next element in the interation.
     *
     * @return the next element in the interation.
     *
     * @throws NoSuchElementException iteration has no more elements.
     */
    public synchronized Object next() throws NoSuchElementException {
        try {
            Object result = Array.get(array, position);
            position++;

            return result;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    /**
     * Removes from the underlying collection the last element returned
     * by the iterator (optional operation).  This method can be called only
     * once per call to <tt>next</tt>.  The behavior of an iterator is
     * unspecified if the underlying collection is modified while the
     * iteration is in progress in any way other than by calling this method.
     *
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *         is not supported by this Iterator.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
