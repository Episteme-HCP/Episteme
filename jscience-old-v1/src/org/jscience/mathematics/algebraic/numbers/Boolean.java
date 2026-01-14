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

package org.jscience.mathematics.algebraic.numbers;

import org.jscience.mathematics.algebraic.lattices.*;

import java.io.Serializable;


/**
 * The Boolean class encapsulates Boolean numbers.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 *
 * @see org.jscience.mathematics.algebraic.lattices.Lattice
 */

//Boolean class is defined as a BooleanLogic Member, which means that it has nothing to do with a BooleanRing Member
public final class Boolean extends ComparableNumber<Boolean>
    implements Cloneable, Serializable, BooleanLogic.Member {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = 5894391864168934841L;

    //The FALSE as Boolean
    /**
     * DOCUMENT ME!
     */
    public static final Boolean FALSE = BooleanLogic.FALSE;

    //The TRUE as Boolean
    /**
     * DOCUMENT ME!
     */
    public static final Boolean TRUE = BooleanLogic.TRUE;

    /**
     * DOCUMENT ME!
     */
    public static final Boolean NEGATIVE_INFINITY = null;

    /**
     * DOCUMENT ME!
     */
    public static final Boolean POSITIVE_INFINITY = null;

    /**
     * DOCUMENT ME!
     */
    public static final Boolean NaN = null;

    //The Class instance representing the primitive type boolean.
    /**
     * DOCUMENT ME!
     */
    public static final Class TYPE = TRUE.getClass();

    /**
     * DOCUMENT ME!
     */
    private final boolean x;

/**
     * Constructs a boolean number.
     */
    public Boolean(final boolean num) {
        x = num;
    }

/**
     * Constructs a boolean number.
     */
    public Boolean(final Boolean num) {
        x = num.value();
    }

/**
     * Constructs the boolean number represented by a string.
     *
     * @param s a string representing an integer number.
     * @throws NumberFormatException if the string does not contain a parsable number.
     */
    public Boolean(final String s) throws NumberFormatException {
        x = java.lang.Boolean.getBoolean(s);
    }

    /**
     * Compares two boolean numbers for equality.
     *
     * @param obj a boolean number.
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj instanceof Boolean) {
            return x == ((Boolean) obj).value();
        } else {
            return false;
        }
    }

    /**
     * Compares two boolean numbers.
     *
     * @param obj an boolean number.
     *
     * @return a negative value if <code>this&lt;obj</code>, zero if
     *         <code>this==obj</code>, and a positive value if
     *         <code>this&gt;obj</code>.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public int compareTo(ComparableNumber obj) throws IllegalArgumentException {
        if ((obj != null) && (obj instanceof Boolean)) {
            Boolean bool = (Boolean) obj;

            if (bool.value() == x) {
                return 0;
            } else {
                if (bool.value() == true) {
                    return 1;
                } else {
                    return -1;
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid object.");
        }
    }

    /**
     * Returns a string representing the value of this boolean number.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return java.lang.Boolean.toString(x);
    }

    /**
     * Returns the integer value.
     *
     * @return DOCUMENT ME!
     */
    public boolean value() {
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int intValue() {
        if (x == true) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long longValue() {
        return (long) intValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float floatValue() {
        return (float) intValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double doubleValue() {
        return (double) intValue();
    }

    /**
     * Returns the min of this number and another, according to
     * compareTo rules.
     *
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boolean min(Boolean val) {
        return new Boolean(x && val.x);
    }

    /**
     * Returns the max of this number and another, according to
     * compareTo rules.
     *
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boolean max(Boolean val) {
        return new Boolean(x || val.x);
    }

    /**
     * Returns a hash code for this Boolean object.
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return new java.lang.Boolean(x).hashCode();
    }

    /**
     * Returns FALSE if the values are equal, TRUE otherwise
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Boolean getDistance(ComparableNumber n) {
        if (!(n instanceof Boolean)) {
            throw new IllegalArgumentException();
        }

        if (((Boolean) n).value() == x) {
            return FALSE;
        } else {
            return TRUE;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNaN() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInfinite() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNegativeInfinity() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isPositiveInfinity() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boolean getNaN() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boolean getNegativeInfinity() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boolean getPositiveInfinity() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param that DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareTo(Boolean that) {
        if (this.x == that.x) {
            return 0;
        } else {
            if (this.x) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    /**
     * Returns the negation of this number.
     *
     * @return DOCUMENT ME!
     */
    public BooleanAlgebra.Member complement() {
        return new Boolean(!x);
    }

    /**
     * Returns the negation of this number.
     *
     * @return DOCUMENT ME!
     */
    public Boolean not() {
        return new Boolean(!x);
    }

    /**
     * Returns the disjunction of this number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Lattice.Member join(final JoinSemiLattice.Member n) {
        if (n instanceof Boolean) {
            return join((Boolean) n);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the disjunction of this boolean number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boolean join(final Boolean n) {
        return new Boolean(x || n.x);
    }

    /**
     * Returns the disjunction of this boolean number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boolean or(final Boolean n) {
        return new Boolean(x || n.x);
    }

    /**
     * Returns the conjunction of this number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Lattice.Member meet(final MeetSemiLattice.Member n) {
        if (n instanceof Boolean) {
            return meet((Boolean) n);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the conjunction of this boolean number and another.
     *
     * @param n an boolean number.
     *
     * @return DOCUMENT ME!
     */
    public Boolean meet(final Boolean n) {
        return new Boolean(x && n.x);
    }

    /**
     * Returns the conjunction of this boolean number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boolean and(final Boolean n) {
        return new Boolean(x && n.x);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new Boolean(this);
    }
}
