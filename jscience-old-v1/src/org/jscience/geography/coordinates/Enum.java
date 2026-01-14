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

package org.jscience.geography.coordinates;

/**
 * The base enumeration from which all enumeration classes are inherited.
 *
 * @author David Shen
 */
public abstract class Enum {
    /** DOCUMENT ME! */
    private String _enumStr;

    /** DOCUMENT ME! */
    private int _enumInt;

/**
     * Creates a new Enum object.
     *
     * @param enumInt DOCUMENT ME!
     * @param enumStr DOCUMENT ME!
     */
    protected Enum(int enumInt, String enumStr) {
        _enumInt = enumInt;
        _enumStr = new String(enumStr);
    }

    /// Returns the string for the enumerant name
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new String(_enumStr);
    }

    /// returns the integer code value for the enumerant
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int toInt() {
        return _enumInt;
    }
}
