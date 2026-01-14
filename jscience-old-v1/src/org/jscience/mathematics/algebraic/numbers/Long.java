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

import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;

import java.io.Serializable;


/**
 * The Long class encapsulates 64 bits integer numbers.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 *
 * @see org.jscience.mathematics.algebraic.fields.IntegerRing
 */

//we use the MIN_VALUE and MAX_VALUE to store NEGATIVE_INFINITY and POSITIVE_INFINITY
//NaN could be stored using an extra boolean, but therefore this class would take more memory storage per long than the java.lang.Long class
//there is currently no LongField for Longs
public final class Long extends ComparableNumber<Long> implements Cloneable,
    Serializable, Ring.Member {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = 689348L;

    //The 0 as Long
    /**
     * DOCUMENT ME!
     */
    public static final Long ZERO = new Long(0);

    //The 1 as Long
    /**
     * DOCUMENT ME!
     */
    public static final Long ONE = new Long(1);

    //A constant holding the largest positive finite value of type long , 263-1.
    /**
     * DOCUMENT ME!
     */
    public static final long MAX_VALUE = java.lang.Long.MAX_VALUE;

    //A constant holding the largest negative finite value of type long , -263.
    /**
     * DOCUMENT ME!
     */
    public static final long MIN_VALUE = java.lang.Long.MIN_VALUE;

    //A constant holding a Not-a-Number (NaN) value of type long .
    /**
     * DOCUMENT ME!
     */
    public static final long NEGATIVE_INFINITY = MIN_VALUE;

    //A constant holding the positive infinity of type long actually set to MAX_VALUE.
    /**
     * DOCUMENT ME!
     */
    public static final long POSITIVE_INFINITY = MAX_VALUE;

    //The Class instance representing the primitive type long .
    /**
     * DOCUMENT ME!
     */
    public static final Class TYPE = ZERO.getClass();

    /**
     * DOCUMENT ME!
     */
    private static final Long LONG_NEGATIVE_INFINITY = new Long(NEGATIVE_INFINITY);

    /**
     * DOCUMENT ME!
     */
    private static final Long LONG_POSITIVE_INFINITY = new Long(POSITIVE_INFINITY);

    /**
     * DOCUMENT ME!
     */
    private final long x;

/**
     * Constructs an long number.
     */
    public Long(final long num) {
        x = num;
    }

/**
     * Constructs a long number.
     */
    public Long(final int num) {
        if (num == Integer.NEGATIVE_INFINITY) {
            x = Long.NEGATIVE_INFINITY;
        } else {
            if (num == Integer.POSITIVE_INFINITY) {
                x = Long.POSITIVE_INFINITY;
            } else {
                x = num;
            }
        }
    }

/**
     * Constructs a long number.
     */
    public Long(final Long num) {
        x = num.x;
    }

/**
     * Constructs a long number.
     */
    public Long(final Integer num) {
        x = num.value();
    }

/**
     * Constructs the long number represented by a string.
     *
     * @param s a string representing an integer number.
     * @throws NumberFormatException if the string does not contain a parsable number.
     */
    public Long(final String s) throws NumberFormatException {
        x = java.lang.Long.parseLong(s);
    }

    /**
     * Compares two long numbers for equality.
     *
     * @param obj an long number.
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj instanceof Long) {
            return x == ((Long) obj).x;
        } else {
            return false;
        }
    }

    /**
     * Compares two comparable numbers.
     *
     * @param obj a number.
     *
     * @return a negative value if <code>this&lt;obj</code>, zero if
     *         <code>this==obj</code>, and a positive value if
     *         <code>this&gt;obj</code>.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public int compareTo(ComparableNumber obj) throws IllegalArgumentException {
        if (obj instanceof Long) {
            return compareTo((Long) obj);
        } else if (obj instanceof Integer) {
            return compareTo(new Long((Integer) obj));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Compares two long numbers.
     *
     * @param obj an long number.
     *
     * @return a negative value if <code>this&lt;obj</code>, zero if
     *         <code>this==obj</code>, and a positive value if
     *         <code>this&gt;obj</code>.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public int compareTo(Long obj) throws IllegalArgumentException {
        return new Long(x - obj.x).signum();
    }

    /**
     * Returns a string representing the value of this long number.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        if (isInfinite()) {
            return "Infinity";
        } else {
            return java.lang.Long.toString(x);
        }
    }

    /**
     * Returns the long value.
     *
     * @return DOCUMENT ME!
     */
    public long value() {
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int intValue() {
        return (int) x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long longValue() {
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float floatValue() {
        return (float) x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double doubleValue() {
        return (double) x;
    }

    /**
     * Returns true if this number is even.
     *
     * @return DOCUMENT ME!
     */
    public boolean isEven() {
        return (x & 1) == 0;
    }

    /**
     * Returns true if this number is odd.
     *
     * @return DOCUMENT ME!
     */
    public boolean isOdd() {
        return (x & 1) == 1;
    }

    /**
     * Returns the abs of this number.
     *
     * @return DOCUMENT ME!
     */
    public Long abs() {
        if (x >= 0) {
            return new Long(x);
        } else {
            return new Long(-x);
        }
    }

    /**
     * Returns the sign of this number.
     *
     * @return DOCUMENT ME!
     */
    public int signum() {
        if (x > 0) {
            return 1;
        } else {
            if (x < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Returns the min of this number and another, according to
     * compareTo rules.
     *
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Long min(Long val) {
        if (compareTo(val) == -1) {
            return this;
        } else {
            return val;
        }
    }

    /**
     * Returns the max of this number and another, according to
     * compareTo rules.
     *
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Long max(Long val) {
        if (compareTo(val) == -1) {
            return val;
        } else {
            return this;
        }
    }

    /**
     * Returns a hash code for this Long object.
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return new java.lang.Long(x).hashCode();
    }

    /**
     * Returns true if this number is NaN.
     *
     * @return DOCUMENT ME!
     */
    public boolean isNaN() {
        return false;
    }

    /**
     * Returns true if this number is infinite.
     *
     * @return DOCUMENT ME!
     */
    public boolean isInfinite() {
        return (x == Long.POSITIVE_INFINITY) || (x == Long.NEGATIVE_INFINITY);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNegativeInfinity() {
        return x == NEGATIVE_INFINITY;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isPositiveInfinity() {
        return x == POSITIVE_INFINITY;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Long getNaN() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Long getNegativeInfinity() {
        return LONG_NEGATIVE_INFINITY;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Long getPositiveInfinity() {
        return LONG_POSITIVE_INFINITY;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Long getDistance(ComparableNumber n) {
        if (n instanceof Long) {
            return new Long(((Long) n).longValue() - x);
        } else if (n instanceof Integer) {
            return new Long(((Integer) n).longValue() - x);
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the negative of this number.
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member negate() {
        return new Long(-x);
    }

    /**
     * Returns the addition of this number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member add(final AbelianGroup.Member n) {
        if (n instanceof Long) {
            return add((Long) n);
        } else if (n instanceof Integer) {
            return add(new Long(((Integer) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this integer number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Long add(final Long n) {
        return new Long(x + n.x);
    }

    /**
     * Returns the subtraction of this number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member n) {
        if (n instanceof Long) {
            return subtract((Long) n);
        } else if (n instanceof Integer) {
            return subtract(new Long(((Integer) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this integer number and another.
     *
     * @param n an integer number.
     *
     * @return DOCUMENT ME!
     */
    public Long subtract(final Long n) {
        return new Long(x - n.x);
    }

    /**
     * Returns the multiplication of this number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Ring.Member multiply(final Ring.Member n) {
        if (n instanceof Long) {
            return multiply((Long) n);
        } else if (n instanceof Integer) {
            return multiply(new Long(((Integer) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this integer number and another.
     *
     * @param n an integer number.
     *
     * @return DOCUMENT ME!
     */
    public Long multiply(final Long n) {
        return new Long(x * n.x);
    }

    /**
     * Returns this integer number raised to the power of another.
     *
     * @param n an integer number.
     *
     * @return DOCUMENT ME!
     */
    public Long pow(final Long n) {
        if (n.x == 0) {
            return ONE;
        } else {
            long ans = x;

            for (int i = 1; i < n.x; i++) {
                ans *= x;
            }

            return new Long(ans);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new Long(this);
    }
}
