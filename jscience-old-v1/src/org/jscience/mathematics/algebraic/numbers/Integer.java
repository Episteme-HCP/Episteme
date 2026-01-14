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

import org.jscience.mathematics.algebraic.fields.IntegerRing;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;

import java.io.Serializable;


/**
 * The Integer class encapsulates integer numbers.
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 * @version 1.0
 *
 * @see org.jscience.mathematics.algebraic.fields.IntegerRing
 */

//we use the MIN_VALUE and MAX_VALUE to store NEGATIVE_INFINITY and POSITIVE_INFINITY
//NaN could be stored using an extra boolean, but therefore this class would take more memory storage per integer than the java.lang.Integer class
public final class Integer extends ComparableNumber<Integer>
    implements Cloneable, Serializable, Ring.Member {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = 6893485894391864141L;

    //The 0 as Integer
    /**
     * DOCUMENT ME!
     */
    public static final Integer ZERO = IntegerRing.ZERO;

    //The 1 as Integer
    /**
     * DOCUMENT ME!
     */
    public static final Integer ONE = IntegerRing.ONE;

    //A constant holding the largest positive finite value of type integer , 231-1.
    /**
     * DOCUMENT ME!
     */
    public static final int MAX_VALUE = java.lang.Integer.MAX_VALUE;

    //A constant holding the smallest positive nonzero value of type integer , -231.
    /**
     * DOCUMENT ME!
     */
    public static final int MIN_VALUE = java.lang.Integer.MIN_VALUE;

    //A constant holding a Not-a-Number (NaN) value of type integer .
    /**
     * DOCUMENT ME!
     */
    public static final int NEGATIVE_INFINITY = MIN_VALUE;

    //A constant holding the positive infinity of type integer actually set to MAX_VALUE.
    /**
     * DOCUMENT ME!
     */
    public static final int POSITIVE_INFINITY = MAX_VALUE;

    /**
     * DOCUMENT ME!
     */
    private static final Integer INTEGER_NEGATIVE_INFINITY = new Integer(NEGATIVE_INFINITY);

    /**
     * DOCUMENT ME!
     */
    private static final Integer INTEGER_POSITIVE_INFINITY = new Integer(POSITIVE_INFINITY);

    //The Class instance representing the primitive type integer .
    /**
     * DOCUMENT ME!
     */
    public static final Class TYPE = ZERO.getClass();

    /**
     * DOCUMENT ME!
     */
    private final int x;

/**
     * Constructs an integer number.
     */
    public Integer(final int num) {
        x = num;
    }

/**
     * Constructs an integer number.
     */
    public Integer(final Integer num) {
        x = num.value();
    }

/**
     * Constructs the integer number represented by a string.
     *
     * @param s a string representing an integer number.
     * @throws NumberFormatException if the string does not contain a parsable number.
     */
    public Integer(final String s) throws NumberFormatException {
        x = java.lang.Integer.parseInt(s);
    }

    /**
     * Compares two integer numbers for equality.
     *
     * @param obj an integer number.
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj instanceof Integer) {
            return x == ((Integer) obj).x;
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
        if (obj instanceof Integer) {
            return compareTo(new Integer((Integer) obj));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Compares two integer numbers.
     *
     * @param obj an integer number.
     *
     * @return a negative value if <code>this&lt;obj</code>, zero if
     *         <code>this==obj</code>, and a positive value if
     *         <code>this&gt;obj</code>.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public int compareTo(Integer obj) throws IllegalArgumentException {
        return new Integer(x - obj.x).signum();
    }

    /**
     * Returns a string representing the value of this integer number.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        if (isInfinite()) {
            return "Infinity";
        } else {
            return java.lang.Integer.toString(x);
        }
    }

    /**
     * Returns the integer value.
     *
     * @return DOCUMENT ME!
     */
    public int value() {
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int intValue() {
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long longValue() {
        return (long) x;
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
    public Integer abs() {
        if (x >= 0) {
            return new Integer(x);
        } else {
            return new Integer(-x);
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
    public Integer min(Integer val) {
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
    public Integer max(Integer val) {
        if (compareTo(val) == -1) {
            return val;
        } else {
            return this;
        }
    }

    /**
     * Returns a hash code for this Integer object.
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return new java.lang.Integer(x).hashCode();
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
        return (x == Integer.POSITIVE_INFINITY) ||
        (x == Integer.NEGATIVE_INFINITY);
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
    public Integer getNaN() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Integer getNegativeInfinity() {
        return INTEGER_NEGATIVE_INFINITY;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Integer getPositiveInfinity() {
        return INTEGER_POSITIVE_INFINITY;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Integer getDistance(ComparableNumber n) {
        if (n instanceof ExactInteger) {
            return new Integer(((ExactInteger) n).intValue() - x);
        } else if (n instanceof Integer) {
            return new Integer(((Integer) n).intValue() - x);
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
        return new Integer(-x);
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
        if (n instanceof Integer) {
            return add((Integer) n);
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
    public Integer add(final Integer n) {
        return new Integer(x + n.x);
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
        if (n instanceof Integer) {
            return subtract((Integer) n);
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
    public Integer subtract(final Integer n) {
        return new Integer(x - n.x);
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
        if (n instanceof Integer) {
            return multiply((Integer) n);
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
    public Integer multiply(final Integer n) {
        return new Integer(x * n.x);
    }

    /**
     * Returns this integer number raised to the power of another.
     *
     * @param n an integer number.
     *
     * @return DOCUMENT ME!
     */
    public Integer pow(final Integer n) {
        if (n.x == 0) {
            return Integer.ONE;
        } else {
            int ans = x;

            for (int i = 1; i < n.x; i++) {
                ans *= x;
            }

            return new Integer(ans);
        }
    }

    /*
    * ��̔񕉂�?�?���?ő���?� (Greatest Common Divider) ��Ԃ�?B
    * <p>
    * a �� 0 ��?�?��ɂ� b ��Ԃ�?B
    * </p>
    * <p>
    * b �� 0 ��?�?��ɂ� a ��Ԃ�?B
    * </p>
    *
    * @param a        ?�?�1 (���ł��BĂ͂Ȃ�Ȃ�)
    * @param b        ?�?�2 (���ł��BĂ͂Ȃ�Ȃ�)
    * @return        ?ő���?�
    * @see        #LCM(int, int)
    */
    public Integer gcd(final Integer val) {
        int a;
        int b;
        int c;

        a = x;
        b = val.x;

        while (b != 0) {
            c = a % b;
            a = b;
            b = c;
        }

        return new Integer(a);
    }

    /*
    * ��̔񕉂�?�?���?�?���{?� (Least Common Multiple) ��Ԃ�?B
    * <p>
    * a, b �̂����ꂩ?A���邢�͗��� 0 ��?�?��ɂ� 0 ��Ԃ�?B
    * </p>
    *
    * @param a        ?�?�1 (���ł��BĂ͂Ȃ�Ȃ�)
    * @param b        ?�?�2 (���ł��BĂ͂Ȃ�Ȃ�)
    * @return        ?ő���?�
    * @see        #GCD(int, int)
    */
    public Integer lcm(final Integer val) {
        int c;

        if ((c = this.gcd(val).x) == 0) {
            return ZERO;
        }

        if (x > val.x) {
            return new Integer((this.x / c) * val.x);
        } else {
            return new Integer((val.x / c) * this.x);
        }
    }

    //throws the remainder away
    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Integer integerDivision(Integer val) {
        return new Integer(x / val.x);
    }

    //or mod
    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Integer remainder(Integer val) {
        return new Integer(x % val.x);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new Integer(this);
    }
}
