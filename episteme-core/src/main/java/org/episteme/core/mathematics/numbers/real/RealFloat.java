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
 * Real number backed by a 32-bit {@code float}.
 * Package-private implementation detail.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class RealFloat extends Real {

    private final float value;

    public static final org.episteme.core.mathematics.structures.rings.Ring<RealFloat> RING = (org.episteme.core.mathematics.structures.rings.Ring<RealFloat>) (Object) org.episteme.core.mathematics.sets.Reals.getInstance();

    private RealFloat(float value) {
        this.value = value;
    }

    public static RealFloat create(float value) {
        return new RealFloat(value);
    }

    @Override
    public Real add(Real other) {
        org.episteme.core.mathematics.context.MathContext ctx = org.episteme.core.mathematics.context.MathContext.getCurrent();
        if (ctx.isHighPrecision()) {
            return RealBig.create(BigDecimal.valueOf(value)).add(other);
        }
        if (ctx.isDoubleOrHigherPrecision()) {
            return Real.of(value + other.doubleValue());
        }
        // FAST mode: Stay in float if other is also float
        if (other instanceof RealFloat) {
            return Real.of(value + ((RealFloat) other).value);
        }
        return Real.of(value + other.doubleValue());
    }

    @Override
    public Real subtract(Real other) {
        org.episteme.core.mathematics.context.MathContext ctx = org.episteme.core.mathematics.context.MathContext.getCurrent();
        if (ctx.isHighPrecision()) {
            return RealBig.create(BigDecimal.valueOf(value)).subtract(other);
        }
        if (ctx.isDoubleOrHigherPrecision()) {
            return Real.of(value - other.doubleValue());
        }
        if (other instanceof RealFloat) {
            return Real.of(value - ((RealFloat) other).value);
        }
        return Real.of(value - other.doubleValue());
    }

    @Override
    public Real multiply(Real other) {
        org.episteme.core.mathematics.context.MathContext ctx = org.episteme.core.mathematics.context.MathContext.getCurrent();
        if (ctx.isHighPrecision()) {
            return RealBig.create(BigDecimal.valueOf(value)).multiply(other);
        }
        if (ctx.isDoubleOrHigherPrecision()) {
            return Real.of(value * other.doubleValue());
        }
        if (other instanceof RealFloat) {
            return Real.of(value * ((RealFloat) other).value);
        }
        return Real.of(value * other.doubleValue());
    }

    @Override
    public Real divide(Real other) {
        org.episteme.core.mathematics.context.MathContext ctx = org.episteme.core.mathematics.context.MathContext.getCurrent();
        if (ctx.isHighPrecision()) {
            return RealBig.create(BigDecimal.valueOf(value)).divide(other);
        }
        if (ctx.isDoubleOrHigherPrecision()) {
            return Real.of(value / other.doubleValue());
        }
        if (other instanceof RealFloat) {
            return Real.of(value / ((RealFloat) other).value);
        }
        return Real.of(value / other.doubleValue());
    }

    @Override
    public Real negate() {
        return RealFloat.create(-value);
    }

    @Override
    public Real abs() {
        return RealFloat.create(Math.abs(value));
    }

    @Override
    public Real inverse() {
        return Real.of(1.0f / value);
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
        return value == 0.0f;
    }

    @Override
    public boolean isOne() {
        return value == 1.0f;
    }

    @Override
    public boolean isNaN() {
        return Float.isNaN(value);
    }

    @Override
    public boolean isInfinite() {
        return Float.isInfinite(value);
    }

    @Override
    public boolean isFast() {
        return true;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(value);
    }

    @Override
    public int compareTo(Real other) {
        return Float.compare(value, (float) other.doubleValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Real))
            return false;
        return Float.compare(value, (float) ((Real) obj).doubleValue()) == 0;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    @Override
    public String toString() {
        return Float.toString(value);
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
        double d = (double) value;
        return Real.of(Math.log(d + Math.sqrt(d * d + 1.0)));
    }

    @Override
    public Real acosh() {
        double d = (double) value;
        return Real.of(Math.log(d + Math.sqrt(d * d - 1.0)));
    }

    @Override
    public Real atanh() {
        double d = (double) value;
        return Real.of(0.5 * Math.log((1.0 + d) / (1.0 - d)));
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


