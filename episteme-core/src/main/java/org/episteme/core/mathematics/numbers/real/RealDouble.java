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

/**
 * Real number backed by a 64-bit {@code double}.
 * Package-private implementation detail.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class RealDouble extends Real {

    private final double value;

    public static final org.episteme.core.mathematics.structures.rings.Ring<RealDouble> RING = (org.episteme.core.mathematics.structures.rings.Ring<RealDouble>) (Object) org.episteme.core.mathematics.sets.Reals.getInstance();

    private RealDouble(double value) {
        this.value = value;
    }

    public static RealDouble create(double value) {
        return new RealDouble(value);
    }

    public static RealDouble of(double value) {
        return new RealDouble(value);
    }

    @Override
    public Real add(Real other) {
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            return RealBig.create(BigDecimal.valueOf(value)).add(other);
        }
        return Real.of(value + other.doubleValue());
    }

    @Override
    public Real subtract(Real other) {
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            return RealBig.create(BigDecimal.valueOf(value)).subtract(other);
        }
        return Real.of(value - other.doubleValue());
    }

    @Override
    public Real multiply(Real other) {
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            return RealBig.create(BigDecimal.valueOf(value)).multiply(other);
        }
        return Real.of(value * other.doubleValue());
    }

    @Override
    public Real divide(Real other) {
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            return RealBig.create(BigDecimal.valueOf(value)).divide(other);
        }
        return Real.of(value / other.doubleValue());
    }

    @Override
    public Real negate() {
        return RealDouble.of(-value);
    }

    @Override
    public Real abs() {
        return RealDouble.of(Math.abs(value));
    }

    @Override
    public Real inverse() {
        return Real.of(1.0 / value);
    }

    @Override
    public Real sqrt() {
        return Real.of(Math.sqrt(value));
    }

    @Override
    public Real pow(int exp) {
        return Real.of(Math.pow(value, exp));
    }

    @Override
    public Real pow(Real exp) {
        return Real.of(Math.pow(value, exp.doubleValue()));
    }

    @Override
    public boolean isZero() {
        return value == 0.0;
    }

    @Override
    public boolean isOne() {
        return value == 1.0;
    }

    @Override
    public boolean isNaN() {
        return Double.isNaN(value);
    }

    @Override
    public boolean isInfinite() {
        return Double.isInfinite(value);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(value);
    }

    @Override
    public int compareTo(Real other) {
        return Double.compare(value, other.doubleValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Real))
            return false;
        return Double.compare(value, ((Real) obj).doubleValue()) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    // --- Transcendental Functions ---

    @Override
    public Real pow(double exponent) {
        return Real.of(Math.pow(value, exponent));
    }

    @Override
    public Real exp() {
        return Real.of(Math.exp(value));
    }

    @Override
    public Real log() {
        return Real.of(Math.log(value));
    }

    @Override
    public Real log10() {
        return Real.of(Math.log10(value));
    }

    @Override
    public Real sin() {
        return Real.of(Math.sin(value));
    }

    @Override
    public Real cos() {
        return Real.of(Math.cos(value));
    }

    @Override
    public Real tan() {
        return Real.of(Math.tan(value));
    }

    @Override
    public Real asin() {
        return Real.of(Math.asin(value));
    }

    @Override
    public Real acos() {
        return Real.of(Math.acos(value));
    }

    @Override
    public Real atan() {
        return Real.of(Math.atan(value));
    }

    @Override
    public Real atan2(Real x) {
        return Real.of(Math.atan2(value, x.doubleValue()));
    }

    @Override
    public Real sinh() {
        return Real.of(Math.sinh(value));
    }

    @Override
    public Real cosh() {
        return Real.of(Math.cosh(value));
    }

    @Override
    public Real tanh() {
        return Real.of(Math.tanh(value));
    }

    @Override
    public Real asinh() {
        return Real.of(Math.log(value + Math.sqrt(value * value + 1.0)));
    }

    @Override
    public Real acosh() {
        return Real.of(Math.log(value + Math.sqrt(value * value - 1.0)));
    }

    @Override
    public Real atanh() {
        return Real.of(0.5 * Math.log((1.0 + value) / (1.0 - value)));
    }

    @Override
    public Real cbrt() {
        return Real.of(Math.cbrt(value));
    }

    @Override
    public Real hypot(Real y) {
        return Real.of(Math.hypot(value, y.doubleValue()));
    }

    @Override
    public Real ceil() {
        return Real.of(Math.ceil(value));
    }

    @Override
    public Real floor() {
        return Real.of(Math.floor(value));
    }

    @Override
    public Real round() {
        return Real.of(Math.round(value));
    }

    @Override
    public Real toDegrees() {
        return Real.of(Math.toDegrees(value));
    }

    @Override
    public Real toRadians() {
        return Real.of(Math.toRadians(value));
    }

    @Override
    public int characteristic() {
        return 0; // Real numbers have characteristic 0 (infinite field)
    }
}


