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

import org.jscience.mathematics.algebraic.algebras.CStarAlgebra;
import org.jscience.mathematics.algebraic.fields.ExactComplexField;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;

import java.io.Serializable;

/**
 * The ExactComplex class encapsulates complex numbers using ExactReals for real and imaginary part.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @planetmath Complex
 */
public final class ExactComplex extends Number implements Cloneable, Serializable, Field.Member, CStarAlgebra.Member {
    private static final long serialVersionUID = 12345L;

    private ExactReal re;
    private ExactReal im;
    /**
     * Caching.
     */
    private transient boolean isModCached = false;
    private transient double modCache;
    private transient boolean isArgCached = false;
    private transient double argCache;
    /**
     * The complex number 0+1i.
     */
    public static final ExactComplex I = ExactComplexField.I;

    /**
     * The ExactComplex number 1+0i.
     */
    public static final ExactComplex ONE = ExactComplexField.ONE;

    /**
     * The ExactComplex number 0+0i.
     */
    public static final ExactComplex ZERO = ExactComplexField.ZERO;

    /**
     * DOCUMENT ME!
     */
    public static final ExactComplex MINUS_ONE = ExactComplexField.MINUS_ONE;

    /**
     * DOCUMENT ME!
     */
    public static final ExactComplex MINUS_I = ExactComplexField.MINUS_I;

    /**
     * DOCUMENT ME!
     */
    public static final ExactComplex HALF = ExactComplexField.HALF;

    /**
     * DOCUMENT ME!
     */
    public static final ExactComplex MINUS_HALF = ExactComplexField.MINUS_HALF;

    /**
     * DOCUMENT ME!
     */
    public static final ExactComplex HALF_I = ExactComplexField.HALF_I;

    /**
     * DOCUMENT ME!
     */
    public static final ExactComplex MINUS_HALF_I = ExactComplexField.MINUS_HALF_I;

    /**
     * DOCUMENT ME!
     */
    public static final ExactComplex TWO = ExactComplexField.TWO;

    /**
     * DOCUMENT ME!
     */
    public static final ExactComplex MINUS_TWO = ExactComplexField.MINUS_TWO;

    /**
     * DOCUMENT ME!
     */
    public static final ExactComplex SQRT_HALF = ExactComplexField.SQRT_HALF;

    /**
     * DOCUMENT ME!
     */
    public static final ExactComplex SQRT_HALF_I = ExactComplexField.SQRT_HALF_I;

    /**
     * DOCUMENT ME!
     */
    public static final ExactComplex MINUS_SQRT_HALF_I = ExactComplexField.MINUS_SQRT_HALF_I;

    /**
     * This is the value of PI rounded to that same number of digits as given by Math.PI
     */
    public static final ExactComplex PI = ExactComplexField.PI;

    /**
     * This is the value of PI as imaginary rounded to that same number of digits as given by Math.PI
     */
    public static final ExactComplex PI_I = ExactComplexField.PI_I;

    /**
     * This is the value of PI/2 rounded to that same number of digits as given by Math.PI/2
     */
    public static final ExactComplex PI_2 = ExactComplexField.PI_2;

    /**
     * This is the value of -PI/2 rounded to that same number of digits as given by -Math.PI/2
     */
    public static final ExactComplex MINUS_PI_2 = ExactComplexField.MINUS_PI_2;

    /**
     * This is the value of PI/2 as imaginary rounded to that same number of digits as given by Math.PI/2
     */
    public static final ExactComplex PI_2_I = ExactComplexField.PI_2_I;

    /**
     * This is the value of -PI/2 as imaginary rounded to that same number of digits as given by -Math.PI/2
     */
    public static final ExactComplex MINUS_PI_2_I = ExactComplexField.MINUS_PI_2_I;

    //The Class instance representing the type.
    public static final Class TYPE = ZERO.getClass();

    /**
     * Constructs the complex number x+iy.
     *
     * @param x the real value of a complex number.
     * @param y the imaginary value of a complex number.
     */
    public ExactComplex(final ExactReal x, final ExactReal y) {
        re = x;
        im = y;
    }

    /**
     * Constructs the complex number x+iy.
     *
     * @param x the real value of a complex number.
     */
    public ExactComplex(final ExactReal x) {
        re = x;
        im = ExactReal.ZERO;
    }

    /**
     * Constructs the complex number x+iy.
     *
     * @param x the real value of a complex number.
     * @param y the imaginary value of a complex number.
     */
    public ExactComplex(final double x, final double y) {
        re = new ExactReal(x);
        im = new ExactReal(y);
    }

    /**
     * Constructs the complex number x+iy.
     *
     * @param x the real value of a complex number.
     */
    public ExactComplex(final double x) {
        re = new ExactReal(x);
        im = ExactReal.ZERO;
    }

//we could add many more constructors here although this list should cover most usages

    /**
     * Constructs the complex number x+iy.
     *
     * @param value the complex number to get values from
     */
    public ExactComplex(final ExactComplex value) {
        re = value.real();
        im = value.imag();
    }

    /**
     * Constructs the complex number x+iy.
     *
     * @param value the complex number to get values from
     */
    public ExactComplex(final Complex value) {
        re = new ExactReal(value.real());
        im = new ExactReal(value.imag());
    }

    /**
     * Constructs the complex number represented by a string.
     *
     * @param s a string representing a complex number.
     * @throws NumberFormatException if the string does not contain a parsable number.
     */
    public ExactComplex(final String s) throws NumberFormatException {
        final int iPos = s.indexOf('i');
        if (iPos == -1) {
            re = new ExactReal(s);
            im = new ExactReal(0.0);
        } else {
            String imStr;
            int signPos = s.indexOf('+', 1);
            if (signPos == -1)
                signPos = s.indexOf('-', 1);
            if (signPos == -1) {
                re = new ExactReal(0.0);
                imStr = s;
            } else {
                int expPos = s.indexOf('E', 1);
                if (expPos == -1)
                    expPos = s.indexOf('e', 1);
                if (signPos == expPos + 1) {
                    signPos = s.indexOf('+', 1);
                    if (signPos == -1)
                        signPos = s.indexOf('-', 1);
                }
                if (iPos < signPos) {
                    imStr = s.substring(0, signPos);
                    re = new ExactReal(s.substring(signPos + 1));
                } else {
                    re = new ExactReal(s.substring(0, signPos));
                    imStr = s.substring(signPos + 1);
                }
            }
            if (imStr.startsWith("i"))
                im = new ExactReal(imStr.substring(1));
            else if (imStr.endsWith("i"))
                im = new ExactReal(imStr.substring(0, imStr.length() - 1));
        }
    }

    /**
     * Creates a complex number with the given modulus and argument.
     *
     * @param mod the modulus of a complex number.
     * @param arg the argument of a complex number.
     */
    public static ExactComplex polar(final double mod, final double arg) {
        final ExactComplex z = new ExactComplex(mod * Math.cos(arg), mod * Math.sin(arg));
        z.modCache = mod;
        z.isModCached = true;
        z.argCache = arg;
        z.isArgCached = true;
        return z;
    }

    /**
     * Check if this number is NaN.
     */
    public boolean isNaN() {
        return (re == ExactReal.NaN) || (im == ExactReal.NaN);
    }

    /**
     * Check if this number is infinite whether on its imaginary or its real part.
     */
    public boolean isInfinite() {
        return (re == ExactReal.NEGATIVE_INFINITY) || (im == ExactReal.NEGATIVE_INFINITY) || (re == ExactReal.POSITIVE_INFINITY) || (im == ExactReal.POSITIVE_INFINITY);
    }

    /**
     * Compares two complex numbers for equality.
     *
     * @param obj a complex number.
     */
    public boolean equals(Object obj) {
        if (obj instanceof ExactComplex) {
            final ExactComplex z = (ExactComplex) obj;
            return re.equals(z.real()) && im.equals(z.imag());
        } else
            return false;
    }

    /**
     * Returns a string representing the value of this complex number.
     */
    public String toString() {
        if (isInfinite()) {
            return "Infinity";
        } else {
            final StringBuffer buf = new StringBuffer();
            buf.append(re.toString());
            if (im.doubleValue() >= 0.0)
                buf.append("+");
            buf.append(im.toString());
            buf.append("i");
            return buf.toString();
        }
    }

    //return the norm
    public int intValue() {
        return (int) norm();
        //return new Double(norm()).intValue();
    }

    //return the norm
    public long longValue() {
        return (long) norm();
        //return new Double(norm()).longValue();
    }

    //return the norm
    public float floatValue() {
        return (float) norm();
        //return new Double(norm()).floatValue();
    }

    //return the norm
    public double doubleValue() {
        return norm();
    }

    /**
     * Returns a hashcode for this complex number.
     */
    public int hashCode() {
        return (int) (Math.exp(mod()));
    }

    /**
     * Returns the real part of this complex number.
     */
    public ExactReal real() {
        return re;
    }

    /**
     * Returns the imaginary part of this complex number.
     */
    public ExactReal imag() {
        return im;
    }

    /**
     * Returns the modulus of this complex number.
     */
    public double mod() {
        if (isModCached)
            return modCache;
        modCache = mod(re.doubleValue(), im.doubleValue());
        isModCached = true;
        return modCache;
    }

    private static double mod(final double real, final double imag) {
        final double reAbs = Math.abs(real);
        final double imAbs = Math.abs(imag);
        if (reAbs == 0.0 && imAbs == 0.0)
            return 0.0;
        else if (reAbs < imAbs)
            return imAbs * Math.sqrt(1.0 + (real / imag) * (real / imag));
        else
            return reAbs * Math.sqrt(1.0 + (imag / real) * (imag / real));
    }

    /**
     * Returns the argument of this complex number.
     */
    public double arg() {
        if (isArgCached)
            return argCache;
        argCache = arg(re.doubleValue(), im.doubleValue());
        isArgCached = true;
        return argCache;
    }

    private static double arg(final double real, final double imag) {
        return Math.atan2(imag, real);
    }

    /**
     * Returns the C<sup>*</sup> norm.
     */
    public double norm() {
        return mod();
    }

//============
// OPERATIONS
//============

    /**
     * Returns the negative of this complex number.
     */
    public AbelianGroup.Member negate() {
        return new ExactComplex((ExactReal) re.negate(), (ExactReal) im.negate());
    }

    /**
     * Returns the inverse of this complex number.
     */
    public Field.Member inverse() {
        ExactReal denominator, real, imag;
        if (re.abs().compareTo(im.abs()) == -1) {
            real = re.divide(im);
            imag = new ExactReal(-1.0);
            denominator = re.multiply(real).add(im);
        } else {
            real = new ExactReal(1.0);
            imag = ((ExactReal) im.negate()).divide(re);
            denominator = re.subtract(im.multiply(imag));
        }
        return new ExactComplex(real.divide(denominator), imag.divide(denominator));
    }

    /**
     * Returns the involution of this complex number.
     */
    public CStarAlgebra.Member involution() {
        return conjugate();
    }

    /**
     * Returns the complex conjugate of this complex number.
     */
    public ExactComplex conjugate() {
        return new ExactComplex(re, (ExactReal) im.negate());
    }

// ADDITION

    /**
     * Returns the addition of this number and another.
     */
    public AbelianGroup.Member add(final AbelianGroup.Member x) {
        if (x instanceof ExactComplex)
            return add((ExactComplex) x);
        else if (x instanceof ExactReal)
            return addReal((ExactReal) x);
        else if (x instanceof ExactRational)
            return addReal(new ExactReal((ExactRational) x));
        else if (x instanceof ExactInteger)
            return addReal(new ExactReal((ExactInteger) x));
        else if (x instanceof Double)
            return addReal(new ExactReal((Double) x));
        else if (x instanceof Float)
            return addReal(new ExactReal((Float) x));
        else if (x instanceof Rational)
            return addReal(new ExactReal((Rational) x));
        else if (x instanceof Long)
            return addReal(new ExactReal((Long) x));
        else if (x instanceof Integer)
            return addReal(new ExactReal((Integer) x));
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the addition of this complex number and another.
     *
     * @param z a complex number.
     */
    public ExactComplex add(final ExactComplex z) {
        return new ExactComplex(re.add(z.re), im.add(z.im));
    }

    /**
     * Returns the addition of this complex number with a real part.
     *
     * @param real a real part.
     */
    public ExactComplex addReal(final ExactReal real) {
        return new ExactComplex(re.add(real), im);
    }

    /**
     * Returns the addition of this complex number with an imaginary part.
     *
     * @param imag an imaginary part.
     */
    public ExactComplex addImag(final ExactReal imag) {
        return new ExactComplex(re, im.add(imag));
    }

    /**
     * Returns the addition of this complex number with a real part.
     *
     * @param real a real part.
     */
    public ExactComplex addReal(final double real) {
        return new ExactComplex((ExactReal) re.add(new Double(real)), im);
    }

    /**
     * Returns the addition of this complex number with an imaginary part.
     *
     * @param imag an imaginary part.
     */
    public ExactComplex addImag(final double imag) {
        return new ExactComplex(re, (ExactReal) im.add(new Double(imag)));
    }

// SUBTRACTION

    /**
     * Returns the subtraction of this number and another.
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member x) {
        if (x instanceof ExactComplex)
            return subtract((ExactComplex) x);
        else if (x instanceof ExactReal)
            return subtractReal((ExactReal) x);
        else if (x instanceof ExactRational)
            return subtractReal(new ExactReal((ExactRational) x));
        else if (x instanceof ExactInteger)
            return subtractReal(new ExactReal((ExactInteger) x));
        else if (x instanceof Double)
            return subtractReal(new ExactReal((Double) x));
        else if (x instanceof Float)
            return subtractReal(new ExactReal((Float) x));
        else if (x instanceof Rational)
            return subtractReal(new ExactReal((Rational) x));
        else if (x instanceof Long)
            return subtractReal(new ExactReal((Long) x));
        else if (x instanceof Integer)
            return subtractReal(new ExactReal((Integer) x));
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the subtraction of this complex number and another.
     *
     * @param z a complex number.
     */
    public ExactComplex subtract(final ExactComplex z) {
        return new ExactComplex(re.subtract(z.re), im.subtract(z.im));
    }

    /**
     * Returns the subtraction of this complex number with a real part.
     *
     * @param real a real part.
     */
    public ExactComplex subtractReal(final ExactReal real) {
        return new ExactComplex(re.subtract(real), im);
    }

    /**
     * Returns the subtraction of this complex number with an imaginary part.
     *
     * @param imag an imaginary part.
     */
    public ExactComplex subtractImag(final ExactReal imag) {
        return new ExactComplex(re, im.subtract(imag));
    }

    /**
     * Returns the subtraction of this complex number with a real part.
     *
     * @param real a real part.
     */
    public ExactComplex subtractReal(final double real) {
        return new ExactComplex((ExactReal) re.subtract(new Double(real)), im);
    }

    /**
     * Returns the subtraction of this complex number with an imaginary part.
     *
     * @param imag an imaginary part.
     */
    public ExactComplex subtractImag(final double imag) {
        return new ExactComplex(re, (ExactReal) im.subtract(new Double(imag)));
    }

// MULTIPLICATION

    /**
     * Returns the multiplication of this number by a complex scalar.
     */
    public Module.Member scalarMultiply(final Ring.Member x) {
        return (ExactComplex) multiply(x);
    }

    /**
     * Returns the multiplication of this number and another.
     */
    public Ring.Member multiply(final Ring.Member x) {
        if (x instanceof ExactComplex)
            return multiply((ExactComplex) x);
        else if (x instanceof ExactReal)
            return multiply((ExactReal) x);
        else if (x instanceof ExactRational)
            return multiply(new ExactReal((ExactRational) x));
        else if (x instanceof ExactInteger)
            return multiply(new ExactReal((ExactInteger) x));
        else if (x instanceof Double)
            return multiply(new ExactReal((Double) x));
        else if (x instanceof Float)
            return multiply(new ExactReal((Float) x));
        else if (x instanceof Rational)
            return multiply(new ExactReal((Rational) x));
        else if (x instanceof Long)
            return multiply(new ExactReal((Long) x));
        else if (x instanceof Integer)
            return multiply(new ExactReal((Integer) x));
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the multiplication of this complex number and another.
     *
     * @param z a complex number.
     */
    public ExactComplex multiply(final ExactComplex z) {
        return new ExactComplex(re.multiply(z.re).subtract(im.multiply(z.im)), re.multiply(z.im).add(im.multiply(z.re)));
    }

    /**
     * Returns the multiplication of this complex number by a scalar.
     *
     * @param x a real number.
     */
    public ExactComplex multiply(final double x) {
        return new ExactComplex((ExactReal) re.multiply(new Double(x)), (ExactReal) im.multiply(new Double(x)));
    }

    /**
     * Returns the multiplication of this complex number by a scalar.
     *
     * @param x a real number.
     */
    public ExactComplex multiply(final ExactReal x) {
        return new ExactComplex(x.multiply(re), x.multiply(im));
    }

// DIVISION

    /**
     * Returns the division of this number by a complex scalar.
     */
    public VectorSpace.Member scalarDivide(final Field.Member x) {
        return (ExactComplex) divide(x);
    }

    /**
     * Returns the division of this number and another.
     */
    public Field.Member divide(final Field.Member x) {
        if (x instanceof ExactComplex)
            return divide((ExactComplex) x);
        else if (x instanceof ExactReal)
            return divide((ExactReal) x);
        else if (x instanceof ExactRational)
            return divide(new ExactReal((ExactRational) x));
            //Wouldn't it be nice if we could divide by Ring.Member instead ?
            //else if (x instanceof ExactInteger)
            //    return divide(new ExactReal((ExactInteger) x));
        else if (x instanceof Double)
            return divide(new ExactReal((Double) x));
        else if (x instanceof Float)
            return divide(new ExactReal((Float) x));
        else if (x instanceof Rational)
            return divide(new ExactReal((Rational) x));
            //else if (x instanceof Long)
            //    return divide(new ExactReal((Long) x));
            //else if (x instanceof Integer)
            //    return divide(new ExactReal((Integer) x));
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the division of this complex number by another.
     *
     * @param z a complex number.
     * @throws ArithmeticException If divide by zero.
     */
    public ExactComplex divide(final ExactComplex z) {
        final ExactReal denominator, real, imag, a;
        if (z.re.abs().compareTo(z.im.abs()) == -1) {
            a = z.re.divide(z.im);
            denominator = z.re.multiply(a).add(z.im);
            real = re.multiply(a).add(im);
            imag = im.multiply(a).subtract(re);
        } else {
            a = z.im.divide(z.re);
            denominator = z.re.add(z.im.multiply(a));
            real = re.add(im.multiply(a));
            imag = im.subtract(re.multiply(a));
        }
        return new ExactComplex(real.divide(denominator), imag.divide(denominator));
    }

    /**
     * Returns the division of this complex number by a scalar.
     *
     * @param x a real number.
     * @throws ArithmeticException If divide by zero.
     */
    public ExactComplex divide(final double x) {
        return new ExactComplex((ExactReal) re.divide(new Double(x)), (ExactReal) im.divide(new Double(x)));
    }

    /**
     * Returns the division of this complex number by a scalar.
     *
     * @param x a real number.
     * @throws ArithmeticException If divide by zero.
     */
    public ExactComplex divide(final ExactReal x) {
        return new ExactComplex(re.divide(x), im.divide(x));
    }

// POWER

    /**
     * Returns this complex number raised to the power of a scalar.
     *
     * @param x a real number.
     */
    public ExactComplex pow(final int x) {
        return polar(Math.pow(mod(), x), arg() * x);
    }

    /**
     * Returns the square of this complex number.
     */
    public ExactComplex sqr() {
        return new ExactComplex(re.multiply(re).subtract(im.multiply(im)), new ExactReal(2.0).multiply(re.multiply(im)));
    }

    /**
     * /**
     * Returns this complex number raised to the power of another.
     *
     * @param z a complex number.
     */
    //public ExactComplex pow(final ExactComplex z) {
    //}

    /**
     * Returns the square root of this complex number.
     */
    //public ExactComplex sqrt() {
    //}

//===========
// FUNCTIONS
//===========

// EXP

    /**
     * Returns the exponential number e (2.718...) raised to the power of a complex number.
     *
     * @param z a complex number.
     * @planetmath ExponentialFunction
     */
    //public static ExactComplex exp(final ExactComplex z) {
    //}

// LOG

    /**
     * Returns the natural logarithm (base e) of a complex number.
     *
     * @param z a complex number.
     * @planetmath NaturalLogarithm2
     */
    //public static ExactComplex log(final ExactComplex z) {
    //}

// SIN

    /**
     * Returns the trigonometric sine of a complex angle.
     *
     * @param z an angle that is measured in radians.
     */
    //public static ExactComplex sin(final ExactComplex z) {
    //}

// COS

    /**
     * Returns the trigonometric cosine of a complex angle.
     *
     * @param z an angle that is measured in radians.
     */
    //public static ExactComplex cos(final ExactComplex z) {
    //}

// TAN

    /**
     * Returns the trigonometric tangent of a complex angle.
     *
     * @param z an angle that is measured in radians.
     */
    //public static ExactComplex tan(final ExactComplex z) {
    //}

// SINH

    /**
     * Returns the hyperbolic sine of a complex number.
     *
     * @param z a complex number.
     */
    //public static ExactComplex sinh(final ExactComplex z) {
    //}

// COSH

    /**
     * Returns the hyperbolic cosine of a complex number.
     *
     * @param z a complex number.
     */
    //public static ExactComplex cosh(final ExactComplex z) {
    //}

// TANH

    /**
     * Returns the hyperbolic tangent of a complex number.
     *
     * @param z a complex number.
     */
    //public static ExactComplex tanh(final Complex z) {
    //}

// INVERSE SIN

    /**
     * Returns the arc sine of a complex number, in the range of
     * (-<img border=0 alt="pi" src="doc-files/pi.gif">/2 through <img border=0 alt="pi" src="doc-files/pi.gif">/2,
     * -<img border=0 alt="infinity" src="doc-files/infinity.gif"> through <img border=0 alt="infinity" src="doc-files/infinity.gif">).
     *
     * @param z a complex number.
     */
    //public static ExactComplex asin(final ExactComplex z) {
    //}

// INVERSE COS

    /**
     * Returns the arc cosine of a complex number, in the range of
     * (0.0 through <img border=0 alt="pi" src="doc-files/pi.gif">,
     * 0.0 through <img border=0 alt="infinity" src="doc-files/infinity.gif">).
     *
     * @param z a complex number.
     */
    //public static ExactComplex acos(final Complex z) {
    //}

// INVERSE TAN

    /**
     * Returns the arc tangent of a complex number, in the range of
     * (-<img border=0 alt="pi" src="doc-files/pi.gif">/2 through <img border=0 alt="pi" src="doc-files/pi.gif">/2,
     * -<img border=0 alt="infinity" src="doc-files/infinity.gif"> through <img border=0 alt="infinity" src="doc-files/infinity.gif">).
     *
     * @param z a complex number.
     */
    //public static ExactComplex atan(final ExactComplex z) {
    //}

// INVERSE SINH

    /**
     * Returns the arc hyperbolic sine of a complex number, in the range of
     * (-<img border=0 alt="infinity" src="doc-files/infinity.gif"> through <img border=0 alt="infinity" src="doc-files/infinity.gif">,
     * -<img border=0 alt="pi" src="doc-files/pi.gif">/2 through <img border=0 alt="pi" src="doc-files/pi.gif">/2).
     *
     * @param z a complex number.
     */
    //public static ExactComplex asinh(final ExactComplex z) {
    //}

// INVERSE COSH

    /**
     * Returns the arc hyperbolic cosine of a complex number, in the range of
     * (0.0 through <img border=0 alt="infinity" src="doc-files/infinity.gif">,
     * 0.0 through <img border=0 alt="pi" src="doc-files/pi.gif">).
     *
     * @param z a complex number.
     */
    //public static ExactComplex acosh(final ExactComplex z) {
    //}

// INVERSE TANH

    /**
     * Returns the arc hyperbolic tangent of a complex number, in the range of
     * (-<img border=0 alt="infinity" src="doc-files/infinity.gif"> through <img border=0 alt="infinity" src="doc-files/infinity.gif">,
     * -<img border=0 alt="pi" src="doc-files/pi.gif">/2 through <img border=0 alt="pi" src="doc-files/pi.gif">/2).
     *
     * @param z a complex number.
     */
    //public static ExactComplex atanh(final ExactComplex z) {
    //}
    public Object clone() {
        return new ExactComplex(this);
    }

}
