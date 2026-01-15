package org.jscience.mathematics.algebraic.numbers;

import org.jscience.mathematics.MachineEpsilon;
import org.jscience.mathematics.MathUtils;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.geometry.GeometryUtils;

import java.io.Serializable;


/**
 * The Float class encapsulates float numbers.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
 */

//there is currently no FloatField for floats
public final class Float extends ComparableNumber<Float> implements Cloneable,
    Serializable, Field.Member {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = 861668L;

    //The 0 as Float
    /**
     * DOCUMENT ME!
     */
    public static final Float ZERO = new Float(0);

    //The 1 as Float
    /**
     * DOCUMENT ME!
     */
    public static final Float ONE = new Float(1);

    //A constant holding the largest positive finite value of type float, (2-2-23)�2127.
    /**
     * DOCUMENT ME!
     */
    public static final float MAX_VALUE = java.lang.Float.MAX_VALUE;

    //A constant holding the smallest positive nonzero value of type float, 2-149.
    /**
     * DOCUMENT ME!
     */
    public static final float MIN_VALUE = java.lang.Float.MIN_VALUE;

    //A constant holding a Not-a-Number (NaN) value of type float.
    /**
     * DOCUMENT ME!
     */
    public static final float NaN = java.lang.Float.NaN;

    //A constant holding the negative infinity of type float.
    /**
     * DOCUMENT ME!
     */
    public static final float NEGATIVE_INFINITY = java.lang.Float.NEGATIVE_INFINITY;

    //A constant holding the positive infinity of type float.
    /**
     * DOCUMENT ME!
     */
    public static final float POSITIVE_INFINITY = java.lang.Float.POSITIVE_INFINITY;

    //The Class instance representing the primitive type float.
    /**
     * DOCUMENT ME!
     */
    public static final Class TYPE = ZERO.getClass();

    /**
     * DOCUMENT ME!
     */
    private static final Float FLOAT_NAN = new Float(NaN);

    /**
     * DOCUMENT ME!
     */
    private static final Float FLOAT_NEGATIVE_INFINITY = new Float(NEGATIVE_INFINITY);

    /**
     * DOCUMENT ME!
     */
    private static final Float FLOAT_POSITIVE_INFINITY = new Float(POSITIVE_INFINITY);

    /**
     * DOCUMENT ME!
     */
    private final float x;

/**
     * Constructs a float number.
     */
    public Float(final float num) {
        x = num;
    }

/**
     * Constructs a float number.
     */
    public Float(final long num) {
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
     * Constructs a float number.
     */
    public Float(final int num) {
        if (num == Integer.NEGATIVE_INFINITY) {
            x = Float.NEGATIVE_INFINITY;
        } else {
            if (num == Integer.POSITIVE_INFINITY) {
                x = Float.POSITIVE_INFINITY;
            } else {
                x = num;
            }
        }
    }

/**
     * Constructs a float number.
     */
    public Float(final Float num) {
        x = num.value();
    }

/**
     * Constructs a float number.
     */
    public Float(final Rational num) {
        x = (float) num.value();
    }

/**
     * Constructs a float number.
     */
    public Float(final Long num) {
        x = num.value();
    }

/**
     * Constructs a float number.
     */
    public Float(final Integer num) {
        x = num.value();
    }

/**
     * Constructs the float number represented by a string.
     *
     * @param s a string representing a float number.
     * @throws NumberFormatException if the string does not contain a parsable number.
     */
    public Float(final String s) throws NumberFormatException {
        x = java.lang.Float.parseFloat(s);
    }

    /**
     * Compares two float numbers for equality.
     *
     * @param obj a float number.
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj instanceof Float) {
            return x == ((Float) obj).x;
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
        if (obj instanceof Float) {
            return compareTo((Float) obj);
        } else if (obj instanceof Rational) {
            return compareTo(new Float((Rational) obj));
        } else if (obj instanceof Long) {
            return compareTo(new Float((Long) obj));
        } else if (obj instanceof Integer) {
            return compareTo(new Float((Integer) obj));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Compares two float numbers.
     *
     * @param value a float number.
     *
     * @return a negative value if <code>this&lt;obj</code>, zero if
     *         <code>this==obj</code>, and a positive value if
     *         <code>this&gt;obj</code>.
     */
    public int compareTo(Float value) {
        return new Float(x - value.x).signum();
    }

    /**
     * Returns a string representing the value of this float number.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        if (isInfinite()) {
            return "Infinity";
        } else {
            return java.lang.Float.toString(x);
        }
    }

    /**
     * Returns the float value.
     *
     * @return DOCUMENT ME!
     */
    public float value() {
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
        return x;
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
     * Returns the abs of this number.
     *
     * @return DOCUMENT ME!
     */
    public Float abs() {
        if (x >= 0) {
            return new Float(x);
        } else {
            return new Float(-x);
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
    public Float min(Float val) {
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
    public Float max(Float val) {
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
    public Float getNaN() {
        return FLOAT_NAN;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Float getNegativeInfinity() {
        return FLOAT_NEGATIVE_INFINITY;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Float getPositiveInfinity() {
        return FLOAT_POSITIVE_INFINITY;
    }

    /**
     * Returns a hash code for this Float object.
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return new java.lang.Float(x).hashCode();
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Float getDistance(ComparableNumber n) {
        if (n instanceof Float) {
            return new Float(((Float) n).x - x);
        } else if (n instanceof Rational) {
            return new Float(((Rational) n).floatValue() - x);
        } else if (n instanceof Long) {
            return new Float(((Long) n).floatValue() - x);
        } else if (n instanceof Integer) {
            return new Float(((Integer) n).floatValue() - x);
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
        return new Float(-x);
    }

    /**
     * Returns the inverse of this number.
     *
     * @return DOCUMENT ME!
     */
    public Field.Member inverse() {
        return new Float((float) (1.0 / x));
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
        if (n instanceof Float) {
            return add((Float) n);
        } else if (n instanceof Integer) {
            return add(new Float(((Integer) n)));
        }

        if (n instanceof Long) {
            return add(new Float(((Long) n)));
        } else if (n instanceof Rational) {
            return add(new Float(((Rational) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the addition of this float number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Float add(final Float n) {
        return new Float(x + n.x);
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
        if (n instanceof Float) {
            return subtract((Float) n);
        } else if (n instanceof Integer) {
            return subtract(new Float(((Integer) n)));
        } else if (n instanceof Long) {
            return subtract(new Float(((Long) n)));
        } else if (n instanceof Rational) {
            return subtract(new Float(((Rational) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the subtraction of this float number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Float subtract(final Float n) {
        return new Float(x - n.x);
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
        if (n instanceof Float) {
            return multiply((Float) n);
        } else if (n instanceof Integer) {
            return multiply(new Float(((Integer) n)));
        } else if (n instanceof Long) {
            return multiply(new Float(((Long) n)));
        } else if (n instanceof Rational) {
            return multiply(new Float(((Rational) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the multiplication of this float number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Float multiply(final Float n) {
        return new Float(x * n.x);
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
        if (n instanceof Float) {
            return divide((Float) n);
        }
        //Wouldn't it be nice if we could divide by Ring.Member instead ?
        //else if (n instanceof Integer)
        //    return divide(new Float(((Integer) n)));
        //else if (n instanceof Long)
        //    return divide(new Float(((Long) n)));
        else if (n instanceof Rational) {
            return divide(new Float(((Rational) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Returns the division of this float number and another.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Float divide(final Float n) {
        return new Float(x / n.x);
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
    public static Float exp(final Float x) {
        return new Float((float) Math.exp(x.x));
    }

    // LOG
    /**
     * Returns the natural logarithm (base e) of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Float log(final Float x) {
        return new Float((float) Math.log(x.x));
    }

    // SIN
    /**
     * Returns the trigonometric sine of an angle.
     *
     * @param x an angle that is measured in radians
     *
     * @return DOCUMENT ME!
     */
    public static Float sin(final Float x) {
        return new Float((float) Math.sin(x.x));
    }

    // COS
    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param x an angle that is measured in radians
     *
     * @return DOCUMENT ME!
     */
    public static Float cos(final Float x) {
        return new Float((float) Math.cos(x.x));
    }

    // TAN
    /**
     * Returns the trigonometric tangent of an angle.
     *
     * @param x an angle that is measured in radians
     *
     * @return DOCUMENT ME!
     */
    public static Float tan(final Float x) {
        return new Float((float) Math.tan(x.x));
    }

    // SINH
    /**
     * Returns the hyperbolic sine of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Float sinh(final Float x) {
        return new Float((float) MathUtils.sinh(x.x));
    }

    // COSH
    /**
     * Returns the hyperbolic cosine of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Float cosh(final Float x) {
        return new Float((float) MathUtils.cosh(x.x));
    }

    // TANH
    /**
     * Returns the hyperbolic tangent of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Float tanh(final Float x) {
        return new Float((float) MathUtils.tanh(x.x));
    }

    // INVERSE SIN
    /**
     * Returns the arc sine of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Float asin(final Float x) {
        return new Float((float) Math.asin(x.x));
    }

    // INVERSE COS
    /**
     * Returns the arc cosine of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Float acos(final Float x) {
        return new Float((float) Math.acos(x.x));
    }

    // INVERSE TAN
    /**
     * Returns the arc tangent of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Float atan(final Float x) {
        return new Float((float) Math.atan(x.x));
    }

    // INVERSE SINH
    /**
     * Returns the arc hyperbolic sine of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Float asinh(final Float x) {
        return new Float((float) MathUtils.asinh(x.x));
    }

    // INVERSE COSH
    /**
     * Returns the arc hyperbolic cosine of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Float acosh(final Float x) {
        return new Float((float) MathUtils.acosh(x.x));
    }

    // INVERSE TANH
    /**
     * Returns the arc hyperbolic tangent of a number.
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Float atanh(final Float x) {
        return new Float((float) MathUtils.atanh(x.x));
    }

    /**
     * ‚±‚Ì•¡‘f?�?‚Ì•„?†‚É‘Î‰ž‚µ‚½?u•¡‘f?�?‚ÌŠÛ‚ßŒë?·?v‚ð•Ô‚·?B
     *
     * @return ‚±‚Ì•¡‘f?�?‚Ì•„?†‚É‘Î‰ž‚µ‚½?u•¡‘f?�?‚ÌŠÛ‚ßŒë?·?v
     */
    public final Double getEpsilon() {
        double meps = MachineEpsilon.SINGLE / Math.sqrt(2.0);

        return new Double(GeometryUtils.copySign(meps, this.x));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new Float(this);
    }
}
