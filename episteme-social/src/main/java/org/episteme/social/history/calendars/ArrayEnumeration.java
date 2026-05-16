/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.history.calendars;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * An {@link Enumeration} implementation that wraps an array.
 * Provides a simple way to iterate over array elements using the legacy Enumeration interface.
 *
 * @param <E> the type of elements in the array
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ArrayEnumeration<E> implements Enumeration<E> {

    /** The current index in the array. */
    private int index;

    /** The underlying array. */
    private final E[] array;

    /**
     * Creates a new ArrayEnumeration for the specified array.
     *
     * @param array the array to enumerate (must not be null)
     * @throws NullPointerException if array is null
     */
    public ArrayEnumeration(E[] array) {
        if (array == null) {
            throw new NullPointerException("Array cannot be null");
        }
        this.array = array;
        this.index = 0;
    }

    /**
     * Returns the next element in the enumeration.
     *
     * @return the next element
     * @throws NoSuchElementException if no more elements exist
     */
    @Override
    public E nextElement() {
        if (!hasMoreElements()) {
            throw new NoSuchElementException("No more elements in array");
        }
        return array[index++];
    }

    /**
     * Checks if there are more elements to enumerate.
     *
     * @return true if more elements exist
     */
    @Override
    public boolean hasMoreElements() {
        return index < array.length;
    }
}

