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

package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.numbers.ExactReal;


/**
 * DOCUMENT ME!
 *
 * @author b.dietrich
 */
public class ExactRealPolynomialRing implements Ring {
    /** DOCUMENT ME! */
    public static final ExactRealPolynomial ZERO = new ExactRealPolynomial(new ExactReal[] {
                ExactReal.ZERO
            });

    /** DOCUMENT ME! */
    public static final ExactRealPolynomial ONE = new ExactRealPolynomial(new ExactReal[] {
                ExactReal.ONE
            });

    /** DOCUMENT ME! */
    private static ExactRealPolynomialRing _instance;

/**
     * Creates a new instance of PolynomialRing
     */
    protected ExactRealPolynomialRing() {
    }

    /**
     * Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final ExactRealPolynomialRing getInstance() {
        if (_instance == null) {
            synchronized (ExactRealPolynomialRing.class) {
                if (_instance == null) {
                    _instance = new ExactRealPolynomialRing();
                }
            }
        }

        return _instance;
    }

    /**
     * Returns true if one member is the negative of the other.
     *
     * @param a a group member
     * @param b a group member
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
        if ((a instanceof ExactRealPolynomial) &&
                (b instanceof ExactRealPolynomial)) {
            ExactRealPolynomial p1 = (ExactRealPolynomial) a;
            ExactRealPolynomial p2 = (ExactRealPolynomial) b;

            return p1.add(p2).equals(ZERO);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns true if the member is the unit element.
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean isOne(Ring.Member r) {
        if (r instanceof ExactRealPolynomial) {
            return ((ExactRealPolynomial) r).isOne();
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns true if the member is the identity element of this
     * group.
     *
     * @param g a group member
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean isZero(AbelianGroup.Member g) {
        if (g instanceof ExactRealPolynomial) {
            return ((ExactRealPolynomial) g).isZero();
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the unit element.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member one() {
        return ONE;
    }

    /**
     * Returns the identity element.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member zero() {
        return ZERO;
    }

    /**
     * internal method for safe typecast
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    protected static ExactReal[] toExactReal(Field.Member[] f) {
        if (f == null) {
            return null;
        }

        int dim = f.length;

        ExactReal[] d = new ExactReal[dim];

        for (int k = 0; k < dim; k++) {
            if (f[k] instanceof ExactReal) {
                d[k] = (ExactReal) f[k];
            } else {
                throw new IllegalArgumentException("Expected ExactReal. Got (" +
                    k + ") " + f[k]);
            }
        }

        return d;
    }

    /**
     * internal method for safe typecast
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static ExactReal[] toExactReal(ExactReal[] d) {
        if (d == null) {
            return null;
        }

        int dim = d.length;
        ExactReal[] s = new ExactReal[dim];

        for (int k = 0; k < dim; k++) {
            s[k] = new ExactReal(d[k]);
        }

        return s;
    }
}
