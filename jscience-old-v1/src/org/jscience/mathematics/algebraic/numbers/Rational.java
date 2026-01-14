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

//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
package org.jscience.mathematics.algebraic.numbers;

import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.RationalField;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;

import java.io.Serializable;


/**
 * DOCUMENT ME!
 *
 * @author Mark E. Shoulson, Silvere Martin-Michiellot
 * @version 1.1
 */

//we use the MIN_VALUE and MAX_VALUE to store NEGATIVE_INFINITY and POSITIVE_INFINITY
//NaN could be stored using an extra boolean, but therefore this class would take more memory storage per rational than needed
public final class Rational extends ComparableNumber<Rational> implements Cloneable,
    Serializable, Field.Member {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 6893481234L;

    /** DOCUMENT ME! */
    public static final Rational ZERO = RationalField.ZERO;

    /** DOCUMENT ME! */
    public static final Rational ONE = RationalField.ONE;

    //A constant holding the largest positive finite value of type Rational , 263-1.
    /** DOCUMENT ME! */
    public static final long MAX_VALUE = java.lang.Long.MAX_VALUE;

    //A constant holding the smallest positive nonzero value of type long , -263.
    /** DOCUMENT ME! */
    public static final long MIN_VALUE = java.lang.Long.MIN_VALUE;

    //A constant holding a Not-a-Number (NaN) value of type long .
    //public static final long NaN = ???;
    //A constant holding the negative infinity of type long actually set to MIN_VALUE.
    /** DOCUMENT ME! */
    public static final long NEGATIVE_INFINITY = MIN_VALUE;

    //A constant holding the positive infinity of type long actually set to MAX_VALUE.
    /** DOCUMENT ME! */
    public static final long POSITIVE_INFINITY = MAX_VALUE;

    //The Class instance representing the primitive type long .
    /** DOCUMENT ME! */
    public static final Class TYPE = ZERO.getClass();

    /** DOCUMENT ME! */
    private static final Rational RATIONAL_NEGATIVE_INFINITY = new Rational(NEGATIVE_INFINITY);

    /** DOCUMENT ME! */
    private static final Rational RATIONAL_POSITIVE_INFINITY = new Rational(POSITIVE_INFINITY);

    /** DOCUMENT ME! */
    private long numerator;

    /** DOCUMENT ME! */
    private long denominator;

    /** DOCUMENT ME! */
    private byte sign;

/**
     * Find the numerator and the denominator using the Euclidean algorithm.
     * The resulting rational is reduced.
     *
     * @param x DOCUMENT ME!
     */
    public Rational(double x) {
        this(1, 0);

        Rational r = ZERO;
        double s = x;

        for (;;) {
            if (Math.abs(s) > (double) Long.MAX_VALUE) {
                return;
            }

            int f = (int) Math.floor(s);
            Rational r0 = r;
            r = new Rational(numerator, denominator);

            long n = (numerator * f) + r0.numerator;

            if (Math.abs(n) > Long.MAX_VALUE) {
                return;
            }

            numerator = (long) n;
            n = (denominator * f) + r0.denominator;

            if (Math.abs(n) > Long.MAX_VALUE) {
                return;
            }

            denominator = (long) n;

            if (x == doubleValue()) {
                return;
            }

            s = 1 / (s - f);
        }
    }

/**
     * Creates a new Rational object.
     *
     * @param l  DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     */
    public Rational(final long l, final long l1) {
        numerator = l;
        denominator = l1;
        sign = 1;
        normalize();
    }

/**
     * Creates a new Rational object.
     *
     * @param l DOCUMENT ME!
     */
    public Rational(final long l) {
        this(l, 1L);
    }

/**
     * Constructs a Rational number.
     *
     * @param num DOCUMENT ME!
     */
    public Rational(final int num) {
        if (num == Integer.NEGATIVE_INFINITY) {
            numerator = Long.NEGATIVE_INFINITY;
        } else {
            if (num == Integer.POSITIVE_INFINITY) {
                numerator = Long.POSITIVE_INFINITY;
            } else {
                numerator = Long.NEGATIVE_INFINITY;
            }
        }

        denominator = 11;
        sign = 1;
        normalize();
    }

/**
     * Creates a new Rational object.
     *
     * @param rational DOCUMENT ME!
     */
    public Rational(final Rational rational) {
        this(rational.numerator * (long) rational.sign, rational.denominator);
    }

/**
     * Constructs a Rational number.
     *
     * @param num DOCUMENT ME!
     */
    public Rational(final Long num) {
        this(num.value());
    }

/**
     * Constructs a Rational number.
     *
     * @param num DOCUMENT ME!
     */
    public Rational(final Integer num) {
        this(num.value());
    }

    /**
     * Compares two rational numbers for equality.
     *
     * @param obj a Rational number.
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        Rational rational;

        if ((obj != null) && (obj instanceof Rational)) {
            rational = (Rational) obj;

            return (sign == rational.sign) &&
            (numerator == rational.numerator) &&
            (denominator == rational.denominator);
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
        if (obj instanceof Rational) {
            return compareTo((Rational) obj);
        } else if (obj instanceof Long) {
            return compareTo(new Rational((Long) obj));
        } else if (obj instanceof Integer) {
            return compareTo(new Rational((Integer) obj));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * Compares two Rational numbers.
     *
     * @param obj an Rational number.
     *
     * @return a negative value if <code>this&lt;obj</code>, zero if
     *         <code>this==obj</code>, and a positive value if
     *         <code>this&gt;obj</code>.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public int compareTo(Rational obj) throws IllegalArgumentException {
        Rational rational1;
        rational1 = obj.subtract(this);

        if (rational1.numerator == 0) {
            return 0;
        } else {
            return rational1.sign;
        }
    }

    //this is meant as denominator == 1
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInteger() {
        return denominator == 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        if (isInfinite()) {
            return "Infinity";
        } else {
            if (isInteger()) {
                return new Long(numerator).toString();
            } else {
                return ((sign >= 0) ? "" : "-") + numerator + "/" +
                denominator;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static long gcd(long l, long l1) {
        if (l < l1) {
            long l2 = l;
            l = l1;
            l1 = l2;
        }

        if (l1 < 1L) {
            return l;
        }

        long l4 = l1;

        for (long l3 = l1; l3 > 0L;) {
            l4 = l1;
            l3 = l % l1;
            l = l1;
            l1 = l3;
        }

        return l4;
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static long lcm(long l, long l1) {
        long l2 = gcd(l, l1);

        return l * (l1 / l2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static byte signum(long l) {
        return ((byte) ((l >= 0L) ? 1 : (-1)));
    }

    /**
     * DOCUMENT ME!
     *
     * @throws ArithmeticException DOCUMENT ME!
     */
    private synchronized void normalize() {
        if (denominator == 0L) {
            throw new ArithmeticException(
                "The Rational with a denominator equal to zero is not defined.");
        }

        if (numerator == 0L) {
            denominator = 1L;
            sign = 1;

            return;
        }

        if (signum(numerator) != signum(denominator)) {
            sign *= -1;
        }

        numerator = Math.abs(numerator);
        denominator = Math.abs(denominator);

        if (numerator > 0L) {
            long l = gcd(numerator, denominator);
            numerator /= l;
            denominator /= l;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return This is an approximated value of the Rational
     */
    public double value() {
        return doubleValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double doubleValue() {
        return ((double) sign * (double) numerator) / (double) denominator;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int intValue() {
        return (int) (((long) sign * numerator) / denominator);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float floatValue() {
        return ((float) sign * (float) numerator) / (float) denominator;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long longValue() {
        return ((long) sign * numerator) / denominator;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long floor() {
        long l = numerator / denominator;

        if ((sign < 0) && (denominator != 1L)) {
            l++;
        }

        return l * (long) sign;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long ceil() {
        long l = numerator / denominator;

        if ((sign > 0) && (denominator != 1L)) {
            l++;
        }

        return l * (long) sign;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rational mod(Rational rational) {
        Rational rational1 = divide(rational);
        rational1 = rational.multiply(new Rational(rational1.floor()));

        return subtract(rational1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rational mod(long l) {
        return mod(new Rational(l));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getNumerator() {
        return numerator;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getDenominator() {
        return denominator;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rational fractionalPart() {
        Rational rational = new Rational(this);
        long l = rational.floor();
        rational.numerator *= rational.sign;
        rational.sign = 1;
        rational.numerator -= (rational.denominator * l);
        rational.normalize();

        return rational;
    }

    /**
     * Returns the abs of this number.
     *
     * @return DOCUMENT ME!
     */
    public Rational abs() {
        if (sign == -1) {
            return new Rational(numerator, denominator);
        } else {
            return new Rational(this);
        }
    }

    /**
     * Returns the sign of this number.
     *
     * @return DOCUMENT ME!
     */
    public int signum() {
        if (numerator == 0) {
            return sign;
        } else {
            return 0;
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
    public Rational min(Rational val) {
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
    public Rational max(Rational val) {
        if (compareTo(val) == -1) {
            return val;
        } else {
            return this;
        }
    }

    /**
     * Returns a hash code for this Rational object.
     *
     * @return DOCUMENT ME!
     */

    //I am not sure this hashCode is really sound, please check it
    public int hashCode() {
        return intValue();
    }

    /**
     * Returns true if this number is NaN.
     *
     * @return DOCUMENT ME!
     */
    public boolean isNaN() {
        return false;

        //should be return (numerator == Rational.NaN)||(denominator == Rational.NaN);
        //||(denominator == 0L) which should not happen
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDefined() {
        return !isInfinite();
    }

    /**
     * Returns true if this number is infinite.
     *
     * @return DOCUMENT ME!
     */
    public boolean isInfinite() {
        return ((numerator == Rational.POSITIVE_INFINITY) ||
        (numerator == Rational.NEGATIVE_INFINITY)) &&
        ((denominator != Rational.POSITIVE_INFINITY) &&
        (denominator != Rational.NEGATIVE_INFINITY));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNegativeInfinity() {
        return (numerator == Rational.NEGATIVE_INFINITY) &&
        ((denominator != Rational.POSITIVE_INFINITY) &&
        (denominator != Rational.NEGATIVE_INFINITY));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isPositiveInfinity() {
        return (numerator == Rational.POSITIVE_INFINITY) &&
        ((denominator != Rational.POSITIVE_INFINITY) &&
        (denominator != Rational.NEGATIVE_INFINITY));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rational getNaN() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rational getNegativeInfinity() {
        return RATIONAL_NEGATIVE_INFINITY;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rational getPositiveInfinity() {
        return RATIONAL_POSITIVE_INFINITY;
    }

    /**
     * Returns true if this number is quasi zero, which means the
     * number may be different from Rational.ZERO although probably still
     * leading to division by zero results if inverted
     *
     * @return DOCUMENT ME!
     */
    public boolean isQuasiZero() {
        return ((denominator == Rational.POSITIVE_INFINITY) ||
        (denominator == Rational.NEGATIVE_INFINITY)) &&
        ((numerator != Rational.POSITIVE_INFINITY) &&
        (numerator != Rational.NEGATIVE_INFINITY));
    }

    /**
     * Returns true if this number is defined as the quotient of two
     * infinites
     *
     * @return DOCUMENT ME!
     */
    public boolean isQuasiNaN() {
        return ((numerator == Rational.POSITIVE_INFINITY) ||
        (numerator == Rational.NEGATIVE_INFINITY)) &&
        ((denominator == Rational.POSITIVE_INFINITY) ||
        (denominator != Rational.NEGATIVE_INFINITY));
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Rational getDistance(ComparableNumber n) {
        if (n instanceof Rational) {
            return new Rational(((Rational) n)).subtract(this);
        } else if (n instanceof Long) {
            return new Rational(((Long) n)).subtract(this);
        } else if (n instanceof Integer) {
            return new Rational(((Integer) n)).subtract(this);
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
        return new Rational(numerator * (long) (-sign), numerator);
    }

    /**
     * Returns the inverse of this number.
     *
     * @return DOCUMENT ME!
     */
    public Field.Member inverse() {
        return new Rational(denominator * (long) sign, numerator);
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
        if (n instanceof Rational) {
            return add((Rational) n);
        } else if (n instanceof Integer) {
            return add(new Rational(((Integer) n)));
        } else if (n instanceof Long) {
            return add(new Rational(((Long) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param rational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rational add(Rational rational) {
        long l = floor();
        long l1 = rational.floor();
        Rational rational1 = fractionalPart();
        Rational rational2 = rational.fractionalPart();
        long l2 = l + l1;
        Rational rational3 = new Rational(1);
        rational3.denominator = lcm(rational1.denominator, rational2.denominator);

        long l3 = gcd(rational1.denominator, rational2.denominator);
        rational3.numerator = (rational1.numerator * (rational2.denominator / l3)) +
            (rational2.numerator * (rational1.denominator / l3));
        rational3.normalize();
        rational3.numerator += (l2 * rational3.denominator);
        rational3.normalize();

        return rational3;
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
        if (n instanceof Rational) {
            return subtract((Rational) n);
        } else if (n instanceof Integer) {
            return subtract(new Rational(((Integer) n)));
        } else if (n instanceof Long) {
            return subtract(new Rational(((Long) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param rational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rational subtract(Rational rational) {
        Rational rational1 = new Rational(rational);
        rational1.sign *= -1;

        return add(rational1);
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
        if (n instanceof Rational) {
            return multiply((Rational) n);
        } else if (n instanceof Integer) {
            return multiply(new Rational(((Integer) n)));
        } else if (n instanceof Long) {
            return multiply(new Rational(((Long) n)));
        } else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param rational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rational multiply(Rational rational) {
        Rational rational1 = new Rational(this);

        if ((numerator == 0L) || (rational.numerator == 0L)) {
            return new Rational(0L);
        } else {
            long l = gcd(rational.numerator, rational1.denominator);
            long l1 = gcd(rational.denominator, rational1.numerator);
            rational1.numerator /= l1;
            rational1.numerator *= (((long) rational.sign * rational.numerator) / l);
            rational1.denominator /= l;
            rational1.denominator *= (rational.denominator / l1);
            rational1.normalize();

            return rational1;
        }
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
        if (n instanceof Rational) {
            return divide((Rational) n);
        }
        //Wouldn't it be nice if we could divide by Ring.Member instead ?
        //else if (n instanceof Integer)
        //    return divide(new Rational(((Integer) n)));
        //else if (n instanceof Long)
        //    return divide(new Rational(((Long) n)));
        else {
            throw new IllegalArgumentException(
                "Member class not recognised by this method.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param rational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rational divide(Rational rational) {
        return (Rational) multiply(rational.inverse());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    /*  public static void main(String[] args) {
          int i = 0;
          Rational rational2 = null;
          long l = java.lang.Long.parseLong(args[i++]);
          long l1 = java.lang.Long.parseLong(args[i++]);
          char c = args[i++].charAt(0);
          long l2 = java.lang.Long.parseLong(args[i++]);
          long l3 = java.lang.Long.parseLong(args[i++]);
          Rational rational = new Rational(l, l1);
          Rational rational1 = new Rational(l2, l3);
    
          switch (c) {
              case 43: // '+'
                  rational2 = rational.add(rational1);
    
                  break;
    
              case 45: // '-'
                  rational2 = rational.subtract(rational1);
    
                  break;
    
              case 42: // '*'
                  rational2 = rational.multiply(rational1);
    
                  break;
    
              case 47: // '/'
                  rational2 = rational.divide(rational1);
    
                  break;
    
              case 37: // '%'
                  rational2 = rational.mod(rational1);
    
                  break;
          }
    
          System.out.print(rational);
          System.out.print(c);
          System.out.print(rational1);
          System.out.print("=");
          System.out.println(rational2);
          System.out.println(gcd(l, l1));
          //System.out.println("\n" + rational.gt(rational1) + "\t" +
          //        rational.gte(rational1) + "\n" + rational.lt(rational1) + "\t" +
          //        rational.lte(rational1));
      }     */
    public Object clone() {
        return new Rational(this);
    }
}
