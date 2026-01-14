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

/**
 * A class representing an element of a relation. This is also named a
 * tuple in the computer world.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class NAry extends Object {
    /** DOCUMENT ME! */
    private Object[] elems;

    //not null or empty although each of the Objects in elem can be null
/**
     * Creates a new NAry object.
     *
     * @param elems DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public NAry(Object[] elems) {
        if ((elems != null) && (elems.length > 0)) {
            this.elems = elems;
        } else {
            throw new IllegalArgumentException(
                "NAry doesn't accept null values or empty arrays.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension() {
        return elems.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getValue() {
        return elems;
    }

    //wil throw an error if i<0 or i>(getDimension()-1)
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue(int i) {
        return elems[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        NAry nAry;
        boolean result;
        int i;

        if (o instanceof NAry) {
            nAry = (NAry) o;

            if (nAry.getDimension() == elems.length) {
                result = true;
                i = 0;

                while ((i < elems.length) && result) {
                    result = elems[i].equals(nAry.getValue(i));
                }

                return result;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
