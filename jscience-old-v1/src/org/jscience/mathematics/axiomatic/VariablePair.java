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

package org.jscience.mathematics.axiomatic;


//author: Greg Bush
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class VariablePair {
    /** DOCUMENT ME! */
    private int a;

    /** DOCUMENT ME! */
    private int b;

/**
     * Creates a new VariablePair object.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     */
    public VariablePair(int a, int b) {
        if (a < b) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getComplement(int x) {
        if (this.a == x) {
            return this.b;
        }

        if (this.b == x) {
            return this.a;
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     */
    public void increment(int offset) {
        offset++;
        this.a += offset;
        this.b += offset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     */
    public void decrement(int offset) {
        offset++;
        this.a -= offset;
        this.b -= offset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        VariablePair other = (VariablePair) obj;

        return (this.a == other.a) && (this.b == other.b);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return (this.a << 16) ^ this.b;
    }
}
