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
import java.math.MathContext;
import ch.obermuhlner.math.big.BigDecimalMath;

/**
 * Real number backed by arbitrary-precision {@link BigDecimal}.
 * Package-private implementation detail.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class RealBig extends Real {

    public static final RealBig NaN = new RealBig(null);
    public static final RealBig ZERO = new RealBig(BigDecimal.ZERO);
    public static final RealBig ONE = new RealBig(BigDecimal.ONE);
    private final BigDecimal value;

    private RealBig(BigDecimal value) {
        this.value = value;
        if (value == null && this != NaN) {
             // This might happen if the constructor is called before NaN is initialized?
             // But RealBig.NaN is static final and usually initialized first.
        }
    }

    public static RealBig of(String s) {
        if (s == null || "NaN".equalsIgnoreCase(s) || "Infinity".equalsIgnoreCase(s) || "-Infinity".equalsIgnoreCase(s)) {
            return NaN;
        }
        return new RealBig(new BigDecimal(s));
    }

    public static RealBig create(BigDecimal value) {
        return new RealBig(value);
    }

    @Override
    public Real add(Real other) {
        if (this.isNaN() || other.isNaN()) return NaN;
        if (other.isInfinite()) {
            return other;
        }
        return RealBig.create(value.add(other.bigDecimalValue()));
    }

    @Override
    public Real subtract(Real other) {
        if (this.isNaN() || other.isNaN()) return NaN;
        if (other.isInfinite()) {
            return other.negate();
        }
        return RealBig.create(value.subtract(other.bigDecimalValue()));
    }

    @Override
    public Real multiply(Real other) {
        if (this.isNaN() || other.isNaN()) return NaN;
        if (other.isInfinite()) {
            if (this.isZero()) {
                return Real.NaN; // 0 * infinity = NaN
            }
            // sign(this) * infinity
            return (this.value.signum() > 0) ? other : other.negate();
        }
        return RealBig.create(value.multiply(other.bigDecimalValue()));
    }

    @Override
    public Real divide(Real other) {
        if (this.isNaN() || other.isNaN()) return NaN;
        if (other.isInfinite()) {
            return Real.ZERO; // x / infinity = 0
        }
        if (other.isZero()) {
            return Real.NaN; // x / 0 = NaN for now (could be Infinity if directed)
        }
        return RealBig.create(value.divide(other.bigDecimalValue(), 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real zero() {
        return ZERO;
    }

    @Override
    public Real one() {
        return ONE;
    }

    @Override
    public Real negate() {
        if (this.isNaN()) return NaN;
        return RealBig.create(value.negate());
    }

    @Override
    public Real abs() {
        if (this.isNaN()) return NaN;
        return RealBig.create(value.abs());
    }

    @Override
    public Real inverse() {
        if (this.isNaN()) return NaN;
        if (this.isZero()) return Real.NaN;
        return RealBig.create(BigDecimal.ONE.divide(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real sqrt() {
        if (this.isNaN()) return NaN;
        // Handle numerical noise: if slightly negative, clamp to zero
        if (value.signum() < 0) {
            // Check if it's very close to zero - use a relative epsilon based on current context precision
            java.math.MathContext mc = org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext();
            // A safer threshold: 10 * 10^-precision (e.g. 1e-33 for prec=34)
            // Or better, 1e-(precision-2) as already used but with more explicit logic.
            BigDecimal threshold = BigDecimal.valueOf(1, Math.max(0, mc.getPrecision() - 2)); 
            if (value.abs().compareTo(threshold) < 0) {
                return ZERO;
            }
            return Real.NaN;
        }
        return RealBig.create(value.sqrt(org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real pow(int exp) {
        if (this.isNaN()) return NaN;
        return RealBig.create(value.pow(exp));
    }

    @Override
    public Real pow(double exponent) {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.pow(value, BigDecimal.valueOf(exponent), 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real pow(Real exp) {
        if (this.isNaN() || exp.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.pow(value, exp.bigDecimalValue(), 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real exp() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.exp(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real log() {
        if (this.isNaN()) return NaN;
        if (this.isZero()) return Real.NEGATIVE_INFINITY;
        if (this.sign() < 0) return Real.NaN;
        return RealBig.create(BigDecimalMath.log(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real log10() {
        if (this.isNaN()) return NaN;
        if (this.isZero()) return Real.NEGATIVE_INFINITY;
        if (this.sign() < 0) return Real.NaN;
        return RealBig.create(BigDecimalMath.log10(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real sin() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.sin(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real cos() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.cos(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real tan() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.tan(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real asin() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.asin(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real acos() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.acos(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real atan() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.atan(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real atan2(Real x) {
        if (this.isNaN() || x.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.atan2(value, x.bigDecimalValue(), 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real sinh() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.sinh(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real cosh() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.cosh(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real tanh() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.tanh(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real asinh() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.asinh(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real acosh() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.acosh(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real atanh() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.atanh(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real cbrt() {
        if (this.isNaN()) return NaN;
        return RealBig.create(BigDecimalMath.pow(value, BigDecimal.ONE.divide(BigDecimal.valueOf(3), 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()), 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real hypot(Real y) {
        if (this.isNaN() || y.isNaN()) return NaN;
        MathContext mc = org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext();
        BigDecimal x2 = value.multiply(value, mc);
        BigDecimal y2 = y.bigDecimalValue().multiply(y.bigDecimalValue(), mc);
        return RealBig.create(x2.add(y2, mc).sqrt(mc));
    }

    @Override
    public Real ceil() {
        return RealBig.create(value.stripTrailingZeros().setScale(0, java.math.RoundingMode.CEILING));
    }

    @Override
    public Real floor() {
        return RealBig.create(value.stripTrailingZeros().setScale(0, java.math.RoundingMode.FLOOR));
    }

    @Override
    public Real round() {
        return RealBig.create(value.setScale(0, java.math.RoundingMode.HALF_UP));
    }

    @Override
    public Real toDegrees() {
        return this.multiply(RealBig.create(BigDecimal.valueOf(180))).divide(Real.PI);
    }

    @Override
    public Real toRadians() {
        return this.multiply(Real.PI).divide(RealBig.create(BigDecimal.valueOf(180)));
    }

    @Override
    public boolean isZero() {
        return value.signum() == 0;
    }

    @Override
    public boolean isOne() {
        return value.compareTo(BigDecimal.ONE) == 0;
    }

    @Override
    public boolean isNaN() {
        return value == null;
    }

    @Override
    public boolean isInfinite() {
        return false; // BigDecimal cannot be infinite
    }

    @Override
    public boolean isHighPrecision() {
        return true;
    }

    @Override
    public double doubleValue() {
        return value == null ? Double.NaN : value.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        if (value == null) return BigDecimal.ZERO; // Or throw? Most places check isNaN
        return value;
    }

    @Override
    public int compareTo(Real other) {
        if (this.isNaN() || other.isNaN()) return this.isNaN() ? (other.isNaN() ? 0 : 1) : -1;
        if (other.isInfinite()) {
            return (other.sign() > 0) ? -1 : 1; // Finite < +Inf, Finite > -Inf
        }
        return value.compareTo(other.bigDecimalValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Real))
            return false;
        Real other = (Real) obj;
        if (this.isNaN() || other.isNaN()) return false;
        return value.compareTo(other.bigDecimalValue()) == 0;
    }

    @Override
    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }

    @Override
    public String toString() {
        return value == null ? "NaN" : value.toString();
    }

    @Override
    public int characteristic() {
        return 0; // Real numbers have characteristic 0 (infinite field)
    }
}


