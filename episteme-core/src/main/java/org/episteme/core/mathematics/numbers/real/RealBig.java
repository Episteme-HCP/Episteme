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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Real number backed by arbitrary-precision {@link BigDecimal}.
 * Package-private implementation detail.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public @SuppressWarnings("unused")
final class RealBig extends Real {
    private static final Logger logger = LoggerFactory.getLogger(RealBig.class);

    private final BigDecimal value;

    private RealBig(BigDecimal value) {
        this.value = value;
    }

    public static RealBig create(BigDecimal value) {
        return new RealBig(value);
    }

    @Override
    public Real add(Real other) {
        if (other.isInfinite()) {
            return other;
        }
        return Real.of(value.add(other.bigDecimalValue()).toString());
    }

    @Override
    public Real subtract(Real other) {
        if (other.isInfinite()) {
            return other.negate();
        }
        return Real.of(value.subtract(other.bigDecimalValue()).toString());
    }

    @Override
    public Real multiply(Real other) {
        if (other.isInfinite()) {
            if (this.isZero()) {
                return Real.NaN; // 0 * infinity = NaN
            }
            // sign(this) * infinity
            return (this.value.signum() > 0) ? other : other.negate();
        }
        return Real.of(value.multiply(other.bigDecimalValue()).toString());
    }

    @Override
    public Real divide(Real other) {
        if (other.isInfinite()) {
            return Real.ZERO; // x / infinity = 0
        }
        return Real.of(value.divide(other.bigDecimalValue(), 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()).toString());
    }

    @Override
    public Real negate() {
        return RealBig.create(value.negate());
    }

    @Override
    public Real abs() {
        return RealBig.create(value.abs());
    }

    @Override
    public Real inverse() {
        return Real.of(BigDecimal.ONE.divide(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()).toString());
    }

    @Override
    public Real sqrt() {
        return Real.of(value.sqrt(org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()).toString());
    }

    @Override
    public Real pow(int exp) {
        return RealBig.create(value.pow(exp));
    }

    private BigDecimal computeTranscendental(String function, BigDecimal value) {
        try {
            TranscendentalProvider provider = org.episteme.core.technical.algorithm.AlgorithmManager.getProvider(TranscendentalProvider.class);
            if (provider != null && provider.isAvailable()) {
                return provider.compute(function, value, org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext());
            } else {
                logger.info("Transcendental provider missing or unavailable for function {}: {}", function, provider);
            }
        } catch (Exception e) {
            logger.error("Transcendental computation failed: {}", e.getMessage());
            // Fallback to double math
        }
        return null;
    }

    private BigDecimal computeTranscendental(String function, BigDecimal v1, BigDecimal v2) {
        try {
            TranscendentalProvider provider = org.episteme.core.technical.algorithm.AlgorithmManager.getProvider(TranscendentalProvider.class);
            if (provider != null && provider.isAvailable()) {
                return provider.compute(function, v1, v2, org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext());
            } else {
                logger.info("Transcendental provider missing or unavailable for function {}: {}", function, provider);
            }
        } catch (Exception e) {
            logger.error("Transcendental computation failed: {}", e.getMessage());
            // Fallback to double math
        }
        return null;
    }

    @Override
    public Real pow(double exponent) {
        BigDecimal res = computeTranscendental("pow", value, BigDecimal.valueOf(exponent));
        if (res != null) return RealBig.create(res);
        return Real.of(Math.pow(value.doubleValue(), exponent));
    }

    @Override
    public Real pow(Real exp) {
        if (exp.isInfinite()) {
            if (this.abs().compareTo(Real.ONE) > 0) {
                return (exp.sign() > 0) ? Real.POSITIVE_INFINITY : Real.ZERO;
            } else if (this.abs().compareTo(Real.ONE) < 0) {
                return (exp.sign() > 0) ? Real.ZERO : Real.POSITIVE_INFINITY;
            } else {
                return Real.NaN;
            }
        }
        BigDecimal res = computeTranscendental("pow", value, exp.bigDecimalValue());
        if (res != null) return RealBig.create(res);
        return Real.of(Math.pow(value.doubleValue(), exp.doubleValue()));
    }

    // --- Transcendental Functions ---

    @Override
    public Real exp() {
        BigDecimal res = computeTranscendental("exp", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.exp(value.doubleValue()));
    }

    @Override
    public Real log() {
        BigDecimal res = computeTranscendental("log", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.log(value.doubleValue()));
    }

    @Override
    public Real log10() {
        BigDecimal res = computeTranscendental("log10", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.log10(value.doubleValue()));
    }

    @Override
    public Real sin() {
        BigDecimal res = computeTranscendental("sin", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.sin(value.doubleValue()));
    }

    @Override
    public Real cos() {
        BigDecimal res = computeTranscendental("cos", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.cos(value.doubleValue()));
    }

    @Override
    public Real tan() {
        BigDecimal res = computeTranscendental("tan", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.tan(value.doubleValue()));
    }

    @Override
    public Real asin() {
        BigDecimal res = computeTranscendental("asin", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.asin(value.doubleValue()));
    }

    @Override
    public Real acos() {
        BigDecimal res = computeTranscendental("acos", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.acos(value.doubleValue()));
    }

    @Override
    public Real atan() {
        BigDecimal res = computeTranscendental("atan", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.atan(value.doubleValue()));
    }

    @Override
    public Real atan2(Real x) {
        BigDecimal res = computeTranscendental("atan2", value, x.bigDecimalValue());
        if (res != null) return RealBig.create(res);
        return Real.of(Math.atan2(value.doubleValue(), x.doubleValue()));
    }

    @Override
    public Real sinh() {
        BigDecimal res = computeTranscendental("sinh", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.sinh(value.doubleValue()));
    }

    @Override
    public Real cosh() {
        BigDecimal res = computeTranscendental("cosh", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.cosh(value.doubleValue()));
    }

    @Override
    public Real tanh() {
        BigDecimal res = computeTranscendental("tanh", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.tanh(value.doubleValue()));
    }

    @Override
    public Real asinh() {
        BigDecimal res = computeTranscendental("asinh", value);
        if (res != null) return RealBig.create(res);
        double d = value.doubleValue();
        return Real.of(Math.log(d + Math.sqrt(d * d + 1.0)));
    }

    @Override
    public Real acosh() {
        BigDecimal res = computeTranscendental("acosh", value);
        if (res != null) return RealBig.create(res);
        double d = value.doubleValue();
        return Real.of(Math.log(d + Math.sqrt(d * d - 1.0)));
    }

    @Override
    public Real atanh() {
        BigDecimal res = computeTranscendental("atanh", value);
        if (res != null) return RealBig.create(res);
        double d = value.doubleValue();
        return Real.of(0.5 * Math.log((1.0 + d) / (1.0 - d)));
    }

    @Override
    public Real cbrt() {
        BigDecimal res = computeTranscendental("cbrt", value);
        if (res != null) return RealBig.create(res);
        return Real.of(Math.cbrt(value.doubleValue()));
    }

    @Override
    public Real hypot(Real y) {
        BigDecimal res = computeTranscendental("hypot", value, y.bigDecimalValue());
        if (res != null) return RealBig.create(res);
        return Real.of(Math.hypot(value.doubleValue(), y.doubleValue()));
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
        // TODO: High-precision implementation
        return Real.of(Math.toDegrees(value.doubleValue()));
    }

    @Override
    public Real toRadians() {
        // TODO: High-precision implementation
        // Use pi from constants if available
        return Real.of(Math.toRadians(value.doubleValue()));
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
        return false; // BigDecimal cannot be NaN
    }

    @Override
    public boolean isInfinite() {
        return false; // BigDecimal cannot be infinite
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return value;
    }

    @Override
    public int compareTo(Real other) {
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
        return value.compareTo(((Real) obj).bigDecimalValue()) == 0;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int characteristic() {
        return 0; // Real numbers have characteristic 0 (infinite field)
    }
}


