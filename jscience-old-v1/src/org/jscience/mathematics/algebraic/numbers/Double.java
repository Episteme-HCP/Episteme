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

import org.jscience.mathematics.MachineEpsilon;
import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.fields.DoubleField;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.geometry.GeometryUtils;

import java.io.Serializable;


/**
 * The Double class encapsulates double numbers.
 *
 * @author Mark Hale, Silvere Martin-Michiellot
 * @version 1.1
 *
 * @see org.jscience.mathematics.algebraic.fields.DoubleField
 */
public final class Double extends ComparableNumber<Double> implements Cloneable,
    Serializable, Field.Member {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = 8616680319093653108L;

    //The 0 as Double
    /**
     * DOCUMENT ME!
     */
    public static final Double ZERO = DoubleField.ZERO;

    //The 1 as Double
    /**
     * DOCUMENT ME!
     */
    public static final Double ONE = DoubleField.ONE;

    //A constant holding the largest positive finite value of type double, (2-2-52)�21023.
    /**
     * DOCUMENT ME!
     */
    public static final double MAX_VALUE = java.lang.Double.MAX_VALUE;

    //A constant holding the smallest positive nonzero value of type double, 2-1074.
    /**
     * DOCUMENT ME!
     */
    public static final double MIN_VALUE = java.lang.Double.MIN_VALUE;

    //A constant holding a Not-a-Number (NaN) value of type double.
    /**
     * DOCUMENT ME!
     */
    public static final double NaN = java.lang.Double.NaN;

    //A constant holding the negative infinity of type double.
    /**
     * DOCUMENT ME!
     */
    public static final double NEGATIVE_INFINITY = java.lang.Double.NEGATIVE_INFINITY;

    //A constant holding the positive infinity of type double.
    /**
     * DOCUMENT ME!
     */
    public static final double POSITIVE_INFINITY = java.lang.Double.POSITIVE_INFINITY;

    //The Class instance representing the primitive type double.
    /**
     * DOCUMENT ME!
     */
    public static final Class TYPE = ZERO.getClass();

    /**
     * DOCUMENT ME!
     */
    private static final Double DOUBLE_NEGATIVE_INFINITY = new Double(NEGATIVE_INFINITY);

    /**
     * DOCUMENT ME!
     */
    private static final Double DOUBLE_POSITIVE_INFINITY = new Double(POSITIVE_INFINITY);

    /**
     * DOCUMENT ME!
     */
    private static final Double DOUBLE_NAN = new Double(NaN);

    /**
     * DOCUMENT ME!
     */
    private final double x;

/**
     * Constructs a double number.
     */
    public Double(final double num) {
        x = num;
    }

/**
     * Constructs a double number.
     */
    public Double(final float num) {
        if (num == Long.NEGATIVE_INFINITY) {
            x = Float.NEGATIVE_INFINITY;
        } else {
            if (num == Long.POSITIVE_INFINITY) {
                x = Float.POSITIVE_INFINITY;
            } else {
                x = num;
            }
        }
    }

/**
     * Constructs a double number.
     */
    public Double(final long num) {
        if (num == Long.NEGATIVE_INFINITY) {
            x = Double.NEGATIVE_INFINITY;
        } else {
            if (num == Long.POSITIVE_INFINITY) {
                x = Double.POSITIVE_INFINITY;
            } else {
                x = num;
            }
        }
    }

/**
     * Constructs a double number.
     */
    public Double(final int num) {
        if (num == Integer.NEGATIVE_INFINITY) {
            x = Double.NEGATIVE_INFINITY;
        } else {
            if (num == Integer.POSITIVE_INFINITY) {
                x = Double.POSITIVE_INFINITY;
            } else {
                x = num;
            }
        }
    }

/**
     * Constructs a double number.
     */
    public Double(final Double num) {
        x = num.x;
    }

/**
     * Constructs a double number.
     */
    public Double(final Float num) {
        x = num.value();
    }

/**
     * Constructs a double number.
     */
    public Double(final Rational num) {
        x = num.value();
    }

/**
     * Constructs a double number.
     */
    public Double(final Long num) {
        x = num.value();
    }

/**
     * Constructs a double number.
     */
    public Double(final Integer num) {
        x = num.value();
    }

/**
     * Constructs the double number represented by a string.
     *
     * @param s a string representing a double number.
     * @throws NumberFormatException if the string does not contain a parsable number.
     */
    public Double(final String s) throws NumberFormatException {
        x = java.lang.Double.parseDouble(s);
    }

    /**
     * Compares two double numbers for equality.
     *
     * @param obj a double number.
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj instanceof Double) {
            return x == ((Double) obj).x;
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
        if (obj instanceof Double) {
            return compareTo((Double) obj);
        } else if (obj instanceof Float) {
            return compareTo(new Double((Float) obj));
        } else if (obj instanceof Rational) {
            return compareTo(new Double((Rational) obj));
        } else if (obj instanceof Long) {
            return compareTo(new Double((Long) obj));
        } else if (obj instanceof Integer) {
            return compareTo(new Double((Integer) obj));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Compares two double numbers.
     *
     * @param obj a double number.
     *
     * @return a negative value if <code>this&lt;obj</code>, zero if
     *         <code>this==obj</code>, and a positive value if
     *         <code>this&gt;obj</code>.
     */
    public int compareTo(Double obj) {
        return new Double(x - obj.x).signum();
    }

    /**
     * Returns a string representing the value of this double number.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        if (isInfinite()) {
            return "Infinity";
        } else {
            return java.lang.Double.toString(x);
        }
    }

    /**
     * Returns the double value.
     *
     * @return DOCUMENT ME!
     */
    public double value() {
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
        return x;
    }

    /**
     * Returns the abs of this number.
     *
     * @return DOCUMENT ME!
     */
    public Double abs() {
        if (x >= 0) {
            return new Double(x);
        } else {
            return new Double(-x);
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
    public Double min(Double val) {
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
    public Double max(Double val) {
        if (compareTo(val) == -1) {
            return val;
        } else {
            return this;
        }
    }

    /**
     * Returns true if this number is NaN.
     *
     * @return DOCUMENT ME!
     */
    public boolean isNaN() {
        return (x == NaN);
    }

    /**
     * Returns true if this number is infinite.
     *
     * @return DOCUMENT ME!
     */
    public boolean isInfinite() {
        return (x == POSITIVE_INFINITY) || (x == NEGATIVE_INFINITY);
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
    public Double getNaN() {
        return DOUBLE_NAN;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Double getNegativeInfinity() {
        return DOUBLE_NEGATIVE_INFINITY;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Double getPositiveInfinity() {
        return DOUBLE_POSITIVE_INFINITY;
    }

    /**
     * Returns a hash code for this Double object.
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return new java.lang.Double(x).hashCode();
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Double getDistance(ComparableNumber n) {
        if (n instanceof Double) {
            return new Double(((Double) n).x - x);
        } else if (n instanceof Float) {
            return new Double(((Float) n).doubleValue() - x);
        } else if (n instanceof Rational) {
            return new Double(((Rational) n).doubleValue() - x);
        } else if (n instanceof Long) {
            return new Double(((Long) n).doubleValue() - x);
        } else if (n instanceof Integer) {
            return new Double(((Integer) n).doubleValue() - x);
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
        return new Double(-x);
    }

    /**
     * Returns the inverse of this number.
     *
     * @return DOCUMENT ME!
     */
    public Field.Member inverse() {
        return new Double(1.0 / x);
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
        if (n instanceof Double) {
            return add((Double) n);
        } else if (n instanceof Integer) {
            return add(new Double(((Integer) n)));
        } else if (n instanceof Long) {
            return add(new Double(((Long) n)));
        } else if (n instanceof Rational) {
            return add(new Double(((Rational) n)));
        } else if (n instanceof Float) {
            return add(new Double(((Float) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this double number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Double add(final Double n) {
        return new Double(x + n.x);
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
        if (n instanceof Double) {
            return subtract((Double) n);
        } else if (n instanceof Integer) {
            return subtract(new Double(((Integer) n)));
        } else if (n instanceof Long) {
            return subtract(new Double(((Long) n)));
        } else if (n instanceof Rational) {
            return subtract(new Double(((Rational) n)));
        } else if (n instanceof Float) {
            return subtract(new Double(((Float) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this double number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Double subtract(final Double n) {
        return new Double(x - n.x);
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
        if (n instanceof Double) {
            return multiply((Double) n);
        } else if (n instanceof Integer) {
            return multiply(new Double(((Integer) n)));
        } else if (n instanceof Long) {
            return multiply(new Double(((Long) n)));
        } else if (n instanceof Rational) {
            return multiply(new Double(((Rational) n)));
        } else if (n instanceof Float) {
            return multiply(new Double(((Float) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this double number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Double multiply(final Double n) {
        return new Double(x * n.x);
    }

    /**
     * Returns the division of this number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Field.Member divide(final Field.Member n) {
        if (n instanceof Double) {
            return divide((Double) n);
        } else if (n instanceof Float) {
            return divide(new Double(((Float) n)));
        }
        //Wouldn't it be nice if we could divide by Ring.Member instead ?
        //else if (n instanceof Integer)
        //    return divide(new Double(((Integer) n)));
        //else if (n instanceof Long)
        //    return divide(new Double(((Long) n)));
        else if (n instanceof Rational) {
            return divide(new Double(((Rational) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the division of this double number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Double divide(final Double n) {
        return new Double(x / n.x);
    }

    //===========
    // FUNCTIONS
    //===========

    // EXP
    /**
     * Returns the exponential number e(2.718...) raised to the power
     * of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Double exp(final Double x) {
        return new Double(Math.exp(x.x));
    }

    // LOG
    /**
     * Returns the natural logarithm (base e) of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Double log(final Double x) {
        return new Double(Math.log(x.x));
    }

    // SIN
    /**
     * Returns the trigonometric sine of an angle.
     *
     * @param x an angle that is measured in radians
     *
     * @return DOCUMENT ME!
     */
    public static Double sin(final Double x) {
        return new Double(Math.sin(x.x));
    }

    // COS
    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param x an angle that is measured in radians
     *
     * @return DOCUMENT ME!
     */
    public static Double cos(final Double x) {
        return new Double(Math.cos(x.x));
    }

    // TAN
    /**
     * Returns the trigonometric tangent of an angle.
     *
     * @param x an angle that is measured in radians
     *
     * @return DOCUMENT ME!
     */
    public static Double tan(final Double x) {
        return new Double(Math.tan(x.x));
    }

    // SINH
    /**
     * Returns the hyperbolic sine of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Double sinh(final Double x) {
        return new Double(MathUtils.sinh(x.x));
    }

    // COSH
    /**
     * Returns the hyperbolic cosine of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Double cosh(final Double x) {
        return new Double(MathUtils.cosh(x.x));
    }

    // TANH
    /**
     * Returns the hyperbolic tangent of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Double tanh(final Double x) {
        return new Double(MathUtils.tanh(x.x));
    }

    // INVERSE SIN
    /**
     * Returns the arc sine of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Double asin(final Double x) {
        return new Double(Math.asin(x.x));
    }

    // INVERSE COS
    /**
     * Returns the arc cosine of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Double acos(final Double x) {
        return new Double(Math.acos(x.x));
    }

    // INVERSE TAN
    /**
     * Returns the arc tangent of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Double atan(final Double x) {
        return new Double(Math.atan(x.x));
    }

    // INVERSE SINH
    /**
     * Returns the arc hyperbolic sine of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Double asinh(final Double x) {
        return new Double(MathUtils.asinh(x.x));
    }

    // INVERSE COSH
    /**
     * Returns the arc hyperbolic cosine of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Double acosh(final Double x) {
        return new Double(MathUtils.acosh(x.x));
    }

    // INVERSE TANH
    /**
     * Returns the arc hyperbolic tangent of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Double atanh(final Double x) {
        return new Double(MathUtils.atanh(x.x));
    }

    /**
     * ‚±‚Ì•¡‘f?�?‚Ì•„?†‚É‘Î‰ž‚µ‚½?u•¡‘f?�?‚ÌŠÛ‚ßŒë?·?v‚ð•Ô‚·?B
     *
     * @return ‚±‚Ì•¡‘f?�?‚Ì•„?†‚É‘Î‰ž‚µ‚½?u•¡‘f?�?‚ÌŠÛ‚ßŒë?·?v
     */
    public final Double getEpsilon() {
        double meps = MachineEpsilon.DOUBLE / Math.sqrt(2.0);

        return new Double(GeometryUtils.copySign(meps, this.x));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new Double(this);
    }
}
