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

package org.jscience.mathematics.algebraic.fields;

import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.Integer;


/**
 * The IntegerRing class encapsulates the ring of integer numbers.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class IntegerRing extends Object implements Ring {
    /** DOCUMENT ME! */
    public final static Integer ZERO = new Integer(0);

    /** DOCUMENT ME! */
    public final static Integer ONE = new Integer(1);

    /** DOCUMENT ME! */
    public final static Integer NEGATIVE_INFINITY = new Integer(Integer.NEGATIVE_INFINITY);

    /** DOCUMENT ME! */
    public final static Integer POSITIVE_INFINITY = new Integer(Integer.POSITIVE_INFINITY);

    /** DOCUMENT ME! */
    private static IntegerRing _instance;

/**
     * Constructs a ring of integer numbers.
     */
    private IntegerRing() {
    }

    /**
     * Constructs a ring of integer numbers. Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final IntegerRing getInstance() {
        if (_instance == null) {
            synchronized (IntegerRing.class) {
                if (_instance == null) {
                    _instance = new IntegerRing();
                }
            }
        }

        return _instance;
    }

    /**
     * Returns the integer number zero.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member zero() {
        return ZERO;
    }

    /**
     * Returns true if the integer number is equal to zero.
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isZero(AbelianGroup.Member g) {
        return ZERO.equals(g);
    }

    /**
     * Returns true if one integer number is the negative of the other.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
        return ZERO.equals(a.add(b));
    }

    /**
     * Returns the integer number one.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member one() {
        return ONE;
    }

    /**
     * Returns true if the integer number is equal to one.
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOne(Ring.Member r) {
        return ONE.equals(r);
    }
}
