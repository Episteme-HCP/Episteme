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
package org.jscience.mathematics.number;

import java.math.BigDecimal;
import org.jscience.mathematics.context.MathContext;
import org.jscience.mathematics.algebra.Field;

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
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class Real extends Number implements Comparable<Real>, Field<Real> {

    private static final class Constants {
        private static final Real ZERO = RealDouble.of(0.0);
        private static final Real ONE = RealDouble.of(1.0);
        private static final Real NaN = RealDouble.of(Double.NaN);
    }

    /** The real number 0 */
    public static final Real ZERO = Constants.ZERO;

    /** The real number 1 */
    public static final Real ONE = Constants.ONE;

    /** The real number NaN */
    public static final Real NaN = Constants.NaN;

    /** The real number 2 */
    public static final Real TWO = RealDouble.of(2.0);

    /** Positive infinity */
    public static final Real POSITIVE_INFINITY = RealDouble.of(Double.POSITIVE_INFINITY);

    /** Negative infinity */
    public static final Real NEGATIVE_INFINITY = RealDouble.of(Double.NEGATIVE_INFINITY);

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
                return RealFloat.of((float) value);
            case EXACT:
                return RealBigDecimal.of(BigDecimal.valueOf(value));
            case NORMAL:
            default:
                return RealDouble.of(value);
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
                return RealFloat.of(Float.parseFloat(value));
            case EXACT:
                return RealBigDecimal.of(new BigDecimal(value));
            case NORMAL:
            default:
                return RealDouble.of(Double.parseDouble(value));
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
}
