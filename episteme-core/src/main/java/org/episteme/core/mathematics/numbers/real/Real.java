/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.numbers.real;

import java.math.BigDecimal;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.structures.rings.FieldElement;
import org.episteme.core.technical.algorithm.AlgorithmManager;

/**
 * Abstract base class for real numbers (Ã¢â€žÂ).
 * <p>
 * Real numbers form a Field under addition and multiplication.
 * This class provides a smart factory that chooses the backing implementation
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class Real extends Number implements Comparable<Real>, Field<Real>, FieldElement<Real> {

    private static final long serialVersionUID = 1L;

    // --- Structural Identity Methods (resolving interface conflicts) ---
    @Override
    public Real zero() {
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() == org.episteme.core.mathematics.context.MathContext.RealPrecision.EXACT) {
            return RealBig.create(java.math.BigDecimal.ZERO);
        }
        return zeroE();
    }

    @Override
    public Real one() {
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() == org.episteme.core.mathematics.context.MathContext.RealPrecision.EXACT) {
            return RealBig.create(java.math.BigDecimal.ONE);
        }
        return oneE();
    }

    /** The real number 0 */
    public static Real zeroE() { 
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            return RealConstants.BIG_ZERO;
        }
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() == org.episteme.core.mathematics.context.MathContext.RealPrecision.FAST) {
            return RealFloat.create(0.0f);
        }
        return RealConstants.DOUBLE_ZERO; 
    }
    public static final Real ZERO = RealConstants.ZERO;

    /** The real number 1 */
    public static Real oneE() { 
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            return RealConstants.BIG_ONE;
        }
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().getRealPrecision() == org.episteme.core.mathematics.context.MathContext.RealPrecision.FAST) {
            return RealFloat.create(1.0f);
        }
        return RealConstants.DOUBLE_ONE; 
    }
    public static final Real ONE = RealConstants.ONE;

    /** The real number NaN */
    public static Real nanE() { 
        return RealConstants.NaN; 
    }
    public static final Real NaN = RealConstants.NaN;

    /** The real number PI */
    public static Real piE() { 
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            Real res = AlgorithmManager.getProvider(RealProvider.class).getConstant("pi");
            if (res != null) return res;
            return RealConstants.BIG_PI;
        }
        return RealConstants.PI; 
    }
    public static final Real PI = RealConstants.PI;

    /** The real number E */
    public static Real eE() { 
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            Real res = AlgorithmManager.getProvider(RealProvider.class).getConstant("e");
            if (res != null) return res;
            return RealConstants.BIG_E;
        }
        return RealConstants.E;
    }
    public static final Real E = RealConstants.E;


    /** The real number 2 */
    public static Real twoE() { return RealConstants.TWO; }
    public static final Real TWO = RealConstants.TWO;

    /** 2Ãâ‚¬ - commonly used in angular calculations */
    public static Real twoPiE() { return RealConstants.TWO_PI; }
    public static final Real TWO_PI = RealConstants.TWO_PI;

    /** Ãâ‚¬/2 - quarter turn in radians */
    public static Real halfPiE() { return RealConstants.HALF_PI; }
    public static final Real HALF_PI = RealConstants.HALF_PI;

    /** Positive infinity */
    public static Real positiveInfinityE() { return RealConstants.POSITIVE_INFINITY; }
    public static final Real POSITIVE_INFINITY = RealConstants.POSITIVE_INFINITY;

    /** Negative infinity */
    public static Real negativeInfinityE() { return RealConstants.NEGATIVE_INFINITY; }
    public static final Real NEGATIVE_INFINITY = RealConstants.NEGATIVE_INFINITY;

    /** Natural logarithm of 2 */
    public static Real ln2E() { return RealConstants.LN2; }
    public static final Real LN2 = RealConstants.LN2;

    /** Natural logarithm of 10 */
    public static Real ln10E() { return RealConstants.LN10; }
    public static final Real LN10 = RealConstants.LN10;

    /**
     * Creates a real number from a double value.
     * Uses current MathContext to decide implementation.
     * 
     * @param value the value
     * @return the Real instance
     */
    public static Real of(double value) {
        if (MathContext.getCurrent().getRealPrecision() != MathContext.RealPrecision.EXACT) {
            if (value == 0.0)
                return zeroE();
            if (value == 1.0)
                return oneE();
        }
        if (Double.isNaN(value))
            return nanE();
        if (Double.isInfinite(value)) {
            return value > 0 ? positiveInfinityE() : negativeInfinityE();
        }

        MathContext.RealPrecision precision = MathContext.getCurrent().getRealPrecision();
        if (precision == MathContext.RealPrecision.FAST) {
            return RealFloat.create((float) value);
        } else if (precision == MathContext.RealPrecision.EXACT) {
            return AlgorithmManager.getProvider(RealProvider.class).create(BigDecimal.valueOf(value));
        } else {
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
        if (MathContext.getCurrent().getRealPrecision() != MathContext.RealPrecision.EXACT) {
            if (value == 0.0f)
                return ZERO;
            if (value == 1.0f)
                return ONE;
        }
        if (Float.isNaN(value))
            return NaN;
        if (Float.isInfinite(value)) {
            return value > 0 ? POSITIVE_INFINITY : NEGATIVE_INFINITY;
        }

        MathContext.RealPrecision precision = MathContext.getCurrent().getRealPrecision();
        if (precision == MathContext.RealPrecision.FAST) {
            return RealFloat.create(value);
        } else if (precision == MathContext.RealPrecision.EXACT) {
            return AlgorithmManager.getProvider(RealProvider.class).create(BigDecimal.valueOf(value));
        } else {
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
        if (MathContext.getCurrent().getRealPrecision() != MathContext.RealPrecision.EXACT) {
            if (value.compareTo(BigDecimal.ZERO) == 0)
                return ZERO;
            if (value.compareTo(BigDecimal.ONE) == 0)
                return ONE;
        }

        MathContext.RealPrecision precision = MathContext.getCurrent().getRealPrecision();
        switch (precision) {
            case FAST:
                return RealFloat.create(value.floatValue());
            case EXACT:
                return AlgorithmManager.getProvider(RealProvider.class).create(value);
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
        if (value == null || value.isEmpty()) return Real.ZERO;
        String v = value.trim();
        if (v.equalsIgnoreCase("NaN")) return Real.NaN;
        if (v.equalsIgnoreCase("Infinity") || v.equalsIgnoreCase("+Infinity")) return Real.POSITIVE_INFINITY;
        if (v.equalsIgnoreCase("-Infinity")) return Real.NEGATIVE_INFINITY;

        MathContext.RealPrecision precision = MathContext.getCurrent().getRealPrecision();
        if (precision == MathContext.RealPrecision.FAST) {
            return RealFloat.create(Float.parseFloat(v));
        } else if (precision == MathContext.RealPrecision.EXACT) {
            return AlgorithmManager.getProvider(RealProvider.class).of(v);
        } else {
            return RealDouble.create(Double.parseDouble(v));
        }
    }

    public static Real valueOf(String value) {
        return of(value);
    }

    /**
     * Creates a real number from an int value.
     * 
     * @param value the value
     * @return the Real instance
     */
    public static Real of(int value) {
        return of((double) value);
    }

    /**
     * Creates a real number from a long value.
     * 
     * @param value the value
     * @return the Real instance
     */
    public static Real of(long value) {
        return of((double) value);
    }


    // Protected constructor to allow subclassing across packages
    protected Real() {
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
    
    public boolean isFast() { return false; }
    public boolean isHighPrecision() { return false; }

    public boolean isPositive() {
        return sign() > 0;
    }

    public boolean isNegative() {
        return sign() < 0;
    }

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
    public abstract Real pow(double exponent);

    /**
     * Returns e raised to this number.
     * 
     * @return e^this
     */
    public abstract Real exp();

    /**
     * Returns the natural logarithm of this number.
     * 
     * @return ln(this)
     */
    public abstract Real log();

    /**
     * Returns the base-10 logarithm of this number.
     * 
     * @return logÃ¢â€šÂÃ¢â€šâ‚¬(this)
     */
    public abstract Real log10();

    /**
     * Returns the sine of this number (in radians).
     * 
     * @return sin(this)
     */
    public abstract Real sin();

    /**
     * Returns the cosine of this number (in radians).
     * 
     * @return cos(this)
     */
    public abstract Real cos();

    /**
     * Returns the tangent of this number (in radians).
     * 
     * @return tan(this)
     */
    public abstract Real tan();

    /**
     * Returns the arcsine of this number.
     * 
     * @return arcsin(this) in radians
     */
    public abstract Real asin();

    /**
     * Returns the arccosine of this number.
     * 
     * @return arccos(this) in radians
     */
    public abstract Real acos();

    /**
     * Returns the arctangent of this number.
     * 
     * @return arctan(this) in radians
     */
    public abstract Real atan();

    /**
     * Returns the angle theta from the conversion of rectangular coordinates (x, y)
     * to polar coordinates (r, theta).
     * 
     * @param x the abscissa coordinate
     * @return the theta component of the point (r, theta)
     */
    public abstract Real atan2(Real x);

    /**
     * Returns the hyperbolic sine of this number.
     * 
     * @return sinh(this)
     */
    public abstract Real sinh();

    /**
     * Returns the hyperbolic cosine of this number.
     * 
     * @return cosh(this)
     */
    public abstract Real cosh();

    /**
     * Returns the hyperbolic tangent of this number.
     * 
     * @return tanh(this)
     */
    public abstract Real tanh();

    /**
     * Returns the inverse hyperbolic sine of this number.
     * 
     * @return asinh(this)
     */
    public abstract Real asinh();

    /**
     * Returns the inverse hyperbolic cosine of this number.
     * 
     * @return acosh(this)
     */
    public abstract Real acosh();

    /**
     * Returns the inverse hyperbolic tangent of this number.
     * 
     * @return atanh(this)
     */
    public abstract Real atanh();

    /**
     * Returns the cube root of this number.
     * 
     * @return cbrt(this)
     */
    public abstract Real cbrt();

    /**
     * Returns sqrt(x^2 + y^2) without intermediate overflow or underflow.
     * 
     * @param y the other value
     * @return sqrt(this^2 + y^2)
     */
    public abstract Real hypot(Real y);

    /**
     * Returns the smallest (closest to negative infinity) double value that is
     * greater than or equal to the argument and is equal to a mathematical integer.
     * 
     * @return ceil(this)
     */
    public abstract Real ceil();

    /**
     * Returns the largest (closest to positive infinity) double value that is
     * less than or equal to the argument and is equal to a mathematical integer.
     * 
     * @return floor(this)
     */
    public abstract Real floor();

    /**
     * Returns the closest long to the argument, with ties rounding to positive
     * infinity.
     * 
     * @return round(this)
     */
    public abstract Real round();

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
    public Real multiply(Real left, Real right) {
        return left.multiply(right);
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true;
    }

    @Override
    public Real negate(Real element) {
        return element.negate();
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

    public static org.episteme.core.mathematics.structures.rings.Ring<Real> ring() {
        return org.episteme.core.mathematics.sets.Reals.getInstance();
    }


    /**
     * Returns the ring structure for real numbers.
     * @return this instance (as it implements Field<Real>)
     */
    public org.episteme.core.mathematics.structures.rings.Ring<Real> getScalarRing() {
        return this;
    }

}


