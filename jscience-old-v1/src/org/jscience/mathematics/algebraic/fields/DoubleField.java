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

import org.jscience.mathematics.MathConstants;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.Double;


/**
 * The DoubleField class encapsulates the field of real numbers.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class DoubleField extends Object implements Field {
    /** DOCUMENT ME! */
    public final static Double ZERO = new Double(0.0);

    /** DOCUMENT ME! */
    public final static Double ONE = new Double(1.0);

    /** DOCUMENT ME! */
    public final static Double PI = new Double(Math.PI);

    /** DOCUMENT ME! */
    public final static Double E = new Double(Math.E);

    /** DOCUMENT ME! */
    public final static Double GAMMA = new Double(MathConstants.GAMMA);

    /** DOCUMENT ME! */
    public final static Double NEGATIVE_INFINITY = new Double(Double.NEGATIVE_INFINITY);

    /** DOCUMENT ME! */
    public final static Double POSITIVE_INFINITY = new Double(Double.POSITIVE_INFINITY);

    /** DOCUMENT ME! */
    public final static Double NaN = new Double(java.lang.Double.NaN);

    /** DOCUMENT ME! */
    private static DoubleField _instance;

/**
     * Constructs a field of real numbers.
     */
    private DoubleField() {
    }

    /**
     * Constructs a field of real numbers. Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final DoubleField getInstance() {
        if (_instance == null) {
            synchronized (DoubleField.class) {
                if (_instance == null) {
                    _instance = new DoubleField();
                }
            }
        }

        return _instance;
    }

    /**
     * Returns the real number zero.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member zero() {
        return ZERO;
    }

    /**
     * Returns true if the real number is equal to zero.
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isZero(AbelianGroup.Member g) {
        return ZERO.equals(g);
    }

    /**
     * Returns true if one real number is the negative of the other.
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
     * Returns the real number one.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member one() {
        return ONE;
    }

    /**
     * Returns true if the real number is equal to one.
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOne(Ring.Member r) {
        return ONE.equals(r);
    }

    /**
     * Returns true if one real number is the inverse of the other.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInverse(Field.Member a, Field.Member b) {
        return ONE.equals(a.multiply(b));
    }
}
