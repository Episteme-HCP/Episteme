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
import org.jscience.mathematics.algebraic.numbers.ExactInteger;


/**
 * The BooleanRing class encapsulates the ring of (false,true) boolean
 * numbers.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this is kind of duplicate class with the org.jscience.mathematics.algebraic.lattices.BooleanLogic class
//actually you can built up a BooleanLogic instance with a BooleanRing instance and vice versa
public final class BooleanRing extends Object implements Ring {
    /** DOCUMENT ME! */
    public final static ExactInteger ZERO = new ExactInteger(0);

    /** DOCUMENT ME! */
    public final static ExactInteger ONE = new ExactInteger(1);

    /** DOCUMENT ME! */
    public final static ExactInteger FALSE = ZERO;

    /** DOCUMENT ME! */
    public final static ExactInteger TRUE = ONE;

    /** DOCUMENT ME! */
    private static BooleanRing _instance;

/**
     * Constructs a ring of boolean numbers.
     */
    private BooleanRing() {
    }

    /**
     * Constructs a ring of boolean numbers. Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final BooleanRing getInstance() {
        if (_instance == null) {
            synchronized (BooleanRing.class) {
                if (_instance == null) {
                    _instance = new BooleanRing();
                }
            }
        }

        return _instance;
    }

    /**
     * Returns the boolean number zero.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member zero() {
        return ZERO;
    }

    /**
     * Returns true if the boolean number is equal to zero.
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isZero(AbelianGroup.Member g) {
        return ZERO.equals(g);
    }

    /**
     * Returns true if one boolean number is the negative of the other.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
        return a.equals(b.negate());
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

    //convenience methods to match with the more traditional vocabulary associated with boolean numbers
    /**
     * Returns the boolean number zero.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member falseValue() {
        return FALSE;
    }

    /**
     * Returns true if the boolean number is equal to false.
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFalse(AbelianGroup.Member g) {
        return FALSE.equals(g);
    }

    /**
     * Returns true if one boolean number is the complement of the
     * other.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isComplement(AbelianGroup.Member a, AbelianGroup.Member b) {
        return a.equals(b.negate());
    }

    /**
     * Returns the boolean number true.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member trueValue() {
        return TRUE;
    }

    /**
     * Returns true if the boolean number is equal to true.
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isTrue(Ring.Member r) {
        return TRUE.equals(r);
    }
}
