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
public final class RealBig extends Real {
    private static final Logger logger = LoggerFactory.getLogger(RealBig.class);

    public static final RealBig NaN = new RealBig(null);
    private final BigDecimal value;

    private RealBig(BigDecimal value) {
        this.value = value;
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
        if (other.isInfinite()) {
            return other;
        }
        return RealBig.create(value.add(other.bigDecimalValue(), 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real subtract(Real other) {
        if (other.isInfinite()) {
            return other.negate();
        }
        return RealBig.create(value.subtract(other.bigDecimalValue(), 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
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
        return RealBig.create(value.multiply(other.bigDecimalValue(), 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real divide(Real other) {
        if (other.isInfinite()) {
            return Real.ZERO; // x / infinity = 0
        }
        return RealBig.create(value.divide(other.bigDecimalValue(), 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
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
        return RealBig.create(BigDecimal.ONE.divide(value, 
            org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
    }

    @Override
    public Real sqrt() {
        return RealBig.create(value.sqrt(org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
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
        throw new UnsupportedOperationException("Transcendental 'pow' failed or was unavailable (no fallback allowed)");
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
        throw new UnsupportedOperationException("Transcendental 'pow' failed or was unavailable (no fallback allowed)");
    }

    // --- Transcendental Functions ---

    @Override
    public Real exp() {
        BigDecimal res = computeTranscendental("exp", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'exp' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real log() {
        BigDecimal res = computeTranscendental("log", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'log' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real log10() {
        BigDecimal res = computeTranscendental("log10", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'log10' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real sin() {
        BigDecimal res = computeTranscendental("sin", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'sin' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real cos() {
        BigDecimal res = computeTranscendental("cos", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'cos' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real tan() {
        BigDecimal res = computeTranscendental("tan", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'tan' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real asin() {
        BigDecimal res = computeTranscendental("asin", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'asin' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real acos() {
        BigDecimal res = computeTranscendental("acos", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'acos' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real atan() {
        BigDecimal res = computeTranscendental("atan", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'atan' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real atan2(Real x) {
        BigDecimal res = computeTranscendental("atan2", value, x.bigDecimalValue());
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'atan2' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real sinh() {
        BigDecimal res = computeTranscendental("sinh", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'sinh' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real cosh() {
        BigDecimal res = computeTranscendental("cosh", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'cosh' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real tanh() {
        BigDecimal res = computeTranscendental("tanh", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'tanh' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real asinh() {
        BigDecimal res = computeTranscendental("asinh", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'asinh' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real acosh() {
        BigDecimal res = computeTranscendental("acosh", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'acosh' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real atanh() {
        BigDecimal res = computeTranscendental("atanh", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'atanh' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real cbrt() {
        BigDecimal res = computeTranscendental("cbrt", value);
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'cbrt' failed or was unavailable (no fallback allowed)");
    }

    @Override
    public Real hypot(Real y) {
        BigDecimal res = computeTranscendental("hypot", value, y.bigDecimalValue());
        if (res != null) return RealBig.create(res);
        throw new UnsupportedOperationException("Transcendental 'hypot' failed or was unavailable (no fallback allowed)");
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
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        if (value == null) return BigDecimal.ZERO; // Or throw? Most places check isNaN
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


