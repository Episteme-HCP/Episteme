/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.mathematics.numbers.real;

import java.math.BigDecimal;
import org.jscience.mathematics.context.MathContext;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.structures.rings.FieldElement;

/**
 * Abstract base class for real numbers (ℝ).
 * <p>
 * Real numbers form a Field under addition and multiplication.
 * This class provides a smart factory that chooses the backing implementation
 * based on the current {@link org.jscience.mathematics.context.MathContext}
 * preference.
 * </p>
 * <ul>
 * <li><b>FAST</b>: Uses {@code float} (32-bit)</li>
 * <li><b>NORMAL</b>: Uses {@code double} (64-bit) - Default</li>
 * <li><b>EXACT</b>: Uses {@link BigDecimal} (arbitrary precision)</li>
 * </ul>
 * 
 * <h2>Usage</h2>
 * 
 * <pre>
 * {@code
 * // Default (NORMAL precision -> double)
 * Real pi = Real.of(3.14159);
 * }
 * </pre>
 * 
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class Real extends Number implements Comparable<Real>, Field<Real>, FieldElement<Real> {

    private static final class Constants {
        private static final Real ZERO = RealDouble.create(0.0);
        private static final Real ONE = RealDouble.create(1.0);
        private static final Real NaN = RealDouble.create(Double.NaN);
        private static final Real PI = RealDouble.create(Math.PI);
        private static final Real E = RealDouble.create(Math.E);
    }

    /** The real number 0 */
    public static final Real ZERO = Constants.ZERO;

    /** The real number 1 */
    public static final Real ONE = Constants.ONE;

    /** The real number NaN */
    public static final Real NaN = Constants.NaN;

    /** The real number PI */
    public static final Real PI = Constants.PI;

    /** The real number E */
    public static final Real E = Constants.E;

    /** The real number 2 */
    public static final Real TWO = RealDouble.create(2.0);

    /** 2π - commonly used in angular calculations */
    public static final Real TWO_PI = RealDouble.create(2.0 * Math.PI);

    /** π/2 - quarter turn in radians */
    public static final Real HALF_PI = RealDouble.create(Math.PI / 2.0);

    /** Positive infinity */
    public static final Real POSITIVE_INFINITY = RealDouble.create(Double.POSITIVE_INFINITY);

    /** Negative infinity */
    public static final Real NEGATIVE_INFINITY = RealDouble.create(Double.NEGATIVE_INFINITY);

    /** Natural logarithm of 2 */
    public static final Real LN2 = RealDouble.create(Math.log(2.0));

    /** Natural logarithm of 10 */
    public static final Real LN10 = RealDouble.create(Math.log(10.0));

    /**
     * Creates a real number from a double value.
     * Uses current MathContext to decide implementation.
     * 
     * @param value the value
     * @return the Real instance
     */
    public static Real of(double value) {
        if (value == 0.0)
            return ZERO;
        if (value == 1.0)
            return ONE;
        if (Double.isNaN(value))
            return NaN;

        switch (MathContext.getCurrent().getRealPrecision()) {
            case FAST:
                return RealFloat.create((float) value);
            case EXACT:
                return RealBig.create(BigDecimal.valueOf(value));
            case NORMAL:
            default:
                return RealDouble.create(value);
        }
    }

    /**
     * Creates a real number from a float value.
     * Uses current MathContext to decide implementation.
     * 
     * @param value the value
     * @return the Real instance
     */
    public static Real of(float value) {
        if (value == 0.0f)
            return ZERO;
        if (value == 1.0f)
            return ONE;
        if (Float.isNaN(value))
            return NaN;

        switch (MathContext.getCurrent().getRealPrecision()) {
            case FAST:
                return RealFloat.create(value);
            case EXACT:
                return RealBig.create(BigDecimal.valueOf(value));
            case NORMAL:
            default:
                return RealDouble.create(value);
        }
    }

    /**
     * Creates a real number from a BigDecimal value.
     * 
     * @param value the value
     * @return the Real instance
     * @throws IllegalArgumentException if value is null
     */
    public static Real of(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) == 0)
            return ZERO;
        if (value.compareTo(BigDecimal.ONE) == 0)
            return ONE;

        switch (MathContext.getCurrent().getRealPrecision()) {
            case FAST:
                return RealFloat.create(value.floatValue());
            case EXACT:
                return RealBig.create(value);
            case NORMAL:
            default:
                return RealDouble.create(value.doubleValue());
        }
    }

    /**
     * Creates a real number from a String representation.
     * 
     * @param value the string value
     * @return the Real instance
     */
    public static Real of(String value) {
        switch (MathContext.getCurrent().getRealPrecision()) {
            case FAST:
                return RealFloat.create(Float.parseFloat(value));
            case EXACT:
                return RealBig.create(new BigDecimal(value));
            case NORMAL:
            default:
                return RealDouble.create(Double.parseDouble(value));
        }
    }

    // Package-private constructor
    Real() {
    }

    // --- Abstract operations ---

    public abstract Real add(Real other);

    public abstract Real subtract(Real other);

    public abstract Real multiply(Real other);

    public abstract Real divide(Real other);

    public abstract Real negate();

    public abstract Real abs();

    public abstract Real inverse();

    public abstract Real sqrt();

    public abstract Real pow(int exp);

    public abstract Real pow(Real exp);

    public abstract boolean isZero();

    public abstract boolean isOne();

    public abstract boolean isNaN();

    public abstract boolean isInfinite();

    public abstract double doubleValue();

    public abstract BigDecimal bigDecimalValue();

    // --- Standard methods ---

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    @Override
    public abstract int compareTo(Real other);

    // --- Transcendental Functions ---

    /**
     * Returns the sign of this number (-1, 0, or 1).
     * 
     * @return -1 if negative, 0 if zero, 1 if positive
     */
    public int sign() {
        int cmp = this.compareTo(Real.ZERO);
        return cmp > 0 ? 1 : (cmp < 0 ? -1 : 0);
    }

    /**
     * Returns this number raised to a power.
     * 
     * @param exponent the exponent
     * @return this^exponent
     */
    public Real pow(double exponent) {
        return Real.of(Math.pow(this.doubleValue(), exponent));
    }

    /**
     * Returns e raised to this number.
     * 
     * @return e^this
     */
    public Real exp() {
        return Real.of(Math.exp(this.doubleValue()));
    }

    /**
     * Returns the natural logarithm of this number.
     * 
     * @return ln(this)
     */
    public Real log() {
        return Real.of(Math.log(this.doubleValue()));
    }

    /**
     * Returns the base-10 logarithm of this number.
     * 
     * @return log₁₀(this)
     */
    public Real log10() {
        return Real.of(Math.log10(this.doubleValue()));
    }

    /**
     * Returns the sine of this number (in radians).
     * 
     * @return sin(this)
     */
    public Real sin() {
        return Real.of(Math.sin(this.doubleValue()));
    }

    /**
     * Returns the cosine of this number (in radians).
     * 
     * @return cos(this)
     */
    public Real cos() {
        return Real.of(Math.cos(this.doubleValue()));
    }

    /**
     * Returns the tangent of this number (in radians).
     * 
     * @return tan(this)
     */
    public Real tan() {
        return Real.of(Math.tan(this.doubleValue()));
    }

    /**
     * Returns the arcsine of this number.
     * 
     * @return arcsin(this) in radians
     */
    public Real asin() {
        return Real.of(Math.asin(this.doubleValue()));
    }

    /**
     * Returns the arccosine of this number.
     * 
     * @return arccos(this) in radians
     */
    public Real acos() {
        return Real.of(Math.acos(this.doubleValue()));
    }

    /**
     * Returns the arctangent of this number.
     * 
     * @return arctan(this) in radians
     */
    public Real atan() {
        return Real.of(Math.atan(this.doubleValue()));
    }

    /**
     * Returns the angle theta from the conversion of rectangular coordinates (x, y)
     * to polar coordinates (r, theta).
     * 
     * @param x the abscissa coordinate
     * @return the theta component of the point (r, theta)
     */
    public Real atan2(Real x) {
        return Real.of(Math.atan2(this.doubleValue(), x.doubleValue()));
    }

    /**
     * Returns the hyperbolic sine of this number.
     * 
     * @return sinh(this)
     */
    public Real sinh() {
        return Real.of(Math.sinh(this.doubleValue()));
    }

    /**
     * Returns the hyperbolic cosine of this number.
     * 
     * @return cosh(this)
     */
    public Real cosh() {
        return Real.of(Math.cosh(this.doubleValue()));
    }

    /**
     * Returns the hyperbolic tangent of this number.
     * 
     * @return tanh(this)
     */
    public Real tanh() {
        return Real.of(Math.tanh(this.doubleValue()));
    }

    /**
     * Returns the inverse hyperbolic sine of this number.
     * 
     * @return asinh(this)
     */
    public Real asinh() {
        double x = this.doubleValue();
        return Real.of(Math.log(x + Math.sqrt(x * x + 1.0)));
    }

    /**
     * Returns the inverse hyperbolic cosine of this number.
     * 
     * @return acosh(this)
     */
    public Real acosh() {
        double x = this.doubleValue();
        return Real.of(Math.log(x + Math.sqrt(x * x - 1.0)));
    }

    /**
     * Returns the inverse hyperbolic tangent of this number.
     * 
     * @return atanh(this)
     */
    public Real atanh() {
        double x = this.doubleValue();
        return Real.of(0.5 * Math.log((1.0 + x) / (1.0 - x)));
    }

    /**
     * Returns the cube root of this number.
     * 
     * @return cbrt(this)
     */
    public Real cbrt() {
        return Real.of(Math.cbrt(this.doubleValue()));
    }

    /**
     * Returns sqrt(x^2 + y^2) without intermediate overflow or underflow.
     * 
     * @param y the other value
     * @return sqrt(this^2 + y^2)
     */
    public Real hypot(Real y) {
        return Real.of(Math.hypot(this.doubleValue(), y.doubleValue()));
    }

    /**
     * Returns the smallest (closest to negative infinity) double value that is
     * greater than or equal to the argument and is equal to a mathematical integer.
     * 
     * @return ceil(this)
     */
    public Real ceil() {
        return Real.of(Math.ceil(this.doubleValue()));
    }

    /**
     * Returns the largest (closest to positive infinity) double value that is
     * less than or equal to the argument and is equal to a mathematical integer.
     * 
     * @return floor(this)
     */
    public Real floor() {
        return Real.of(Math.floor(this.doubleValue()));
    }

    /**
     * Returns the closest long to the argument, with ties rounding to positive
     * infinity.
     * 
     * @return round(this)
     */
    public Real round() {
        return Real.of(Math.round(this.doubleValue()));
    }

    /**
     * Converts an angle measured in radians to an approximately equivalent angle
     * measured in degrees.
     * 
     * @return this converted to degrees
     */
    public Real toDegrees() {
        return Real.of(Math.toDegrees(this.doubleValue()));
    }

    /**
     * Converts an angle measured in degrees to an approximately equivalent angle
     * measured in radians.
     * 
     * @return this converted to radians
     */
    public Real toRadians() {
        return Real.of(Math.toRadians(this.doubleValue()));
    }

    /**
     * Returns the minimum of this and another number.
     * 
     * @param other the other number
     * @return min(this, other)
     */
    public Real min(Real other) {
        return this.compareTo(other) <= 0 ? this : other;
    }

    /**
     * Returns the maximum of this and another number.
     * 
     * @param other the other number
     * @return max(this, other)
     */
    public Real max(Real other) {
        return this.compareTo(other) >= 0 ? this : other;
    }

    /**
     * Returns the remainder of this number divided by another.
     * 
     * @param other the divisor
     * @return this % other
     */
    public Real remainder(Real other) {
        return Real.of(this.doubleValue() % other.doubleValue());
    }

    /**
     * Returns the modulo of this number (always non-negative).
     * 
     * @param other the divisor
     * @return ((this % other) + other) % other
     */
    public Real mod(Real other) {
        double result = this.doubleValue() % other.doubleValue();
        if (result < 0)
            result += other.doubleValue();
        return Real.of(result);
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public int intValue() {
        return (int) doubleValue();
    }

    @Override
    public long longValue() {
        return (long) doubleValue();
    }

    @Override
    public int characteristic() {
        return 0; // Real numbers have characteristic 0 (infinite field)
    }

    // --- Field Interface Implementation ---
    @Override
    public Real operate(Real left, Real right) {
        return left.add(right);
    }

    @Override
    public Real add(Real left, Real right) {
        return left.add(right);
    }

    @Override
    public Real zero() {
        return ZERO;
    }

    @Override
    public Real subtract(Real left, Real right) {
        return left.subtract(right);
    }

    @Override
    public Real negate(Real element) {
        return element.negate();
    }

    @Override
    public Real one() {
        return ONE;
    }

    @Override
    public Real multiply(Real left, Real right) {
        return left.multiply(right);
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true;
    }

    @Override
    public Real inverse(Real element) {
        return element.inverse();
    }

    @Override
    public boolean contains(Real element) {
        return element != null;
    }

    @Override
    public String description() {
        return "Real Numbers (\u211d)";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}