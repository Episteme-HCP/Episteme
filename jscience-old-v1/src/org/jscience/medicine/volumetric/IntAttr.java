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

package org.jscience.medicine.volumetric;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class IntAttr extends Attr {
    /** DOCUMENT ME! */
    int value;

    /** DOCUMENT ME! */
    int initValue;

/**
     * Creates a new IntAttr object.
     *
     * @param label     DOCUMENT ME!
     * @param initValue DOCUMENT ME!
     */
    IntAttr(String label, int initValue) {
        super(label);
        this.initValue = initValue;
        this.value = initValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param stringValue DOCUMENT ME!
     */
    public void set(String stringValue) {
        int newValue = Integer.valueOf(stringValue).intValue();
        set(newValue);
    }

    /**
     * DOCUMENT ME!
     *
     * @param newValue DOCUMENT ME!
     */
    public void set(int newValue) {
        value = newValue;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        value = initValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return name + " " + numFormat.format(value);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getValue() {
        return value;
    }
}
