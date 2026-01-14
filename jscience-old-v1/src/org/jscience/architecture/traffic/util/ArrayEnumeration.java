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

package org.jscience.architecture.traffic.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;


/**
 * This enumeration can walk 1 or 2 dimensional arrays
 *
 * @author Joep Moritz
 * @version 1.0
 */
public class ArrayEnumeration implements Enumeration {
    /** DOCUMENT ME! */
    Object[][] ar;

    /** DOCUMENT ME! */
    int i;

    /** DOCUMENT ME! */
    int j;

/**
     * Creates a new ArrayEnumeration object.
     *
     * @param _ar DOCUMENT ME!
     */
    public ArrayEnumeration(Object[] _ar) {
        ar = new Object[1][];
        ar[0] = _ar;
        i = 0;
        j = 0;
    }

/**
     * Creates a new ArrayEnumeration object.
     *
     * @param _ar DOCUMENT ME!
     */
    public ArrayEnumeration(Object[][] _ar) {
        ar = _ar;
        i = 0;
        j = 0;
    }

/**
     * Creates a new ArrayEnumeration object.
     *
     * @param _ar1 DOCUMENT ME!
     * @param _ar2 DOCUMENT ME!
     */
    public ArrayEnumeration(Object[] _ar1, Object[] _ar2) {
        ar = new Object[2][];
        ar[0] = _ar1;
        ar[1] = _ar2;
        i = 0;
        j = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasMoreElements() {
        return (i < ar.length) && (j < ar[i].length);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public Object nextElement() throws NoSuchElementException {
        if (!hasMoreElements()) {
            throw new NoSuchElementException();
        }

        Object o = ar[i][j++];

        if (j >= ar[i].length) {
            i++;
            j = 0;
        }

        return o;
    }
}
