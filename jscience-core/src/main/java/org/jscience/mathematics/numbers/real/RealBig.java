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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.numbers.real;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Real number backed by arbitrary-precision {@link BigDecimal}.
 * Package-private implementation detail.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
final class RealBig extends Real {

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
        // Use default MathContext for division to avoid infinite expansion
        return Real.of(value.divide(other.bigDecimalValue(), MathContext.DECIMAL128).toString());
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
        return Real.of(BigDecimal.ONE.divide(value, MathContext.DECIMAL128).toString());
    }

    @Override
    public Real sqrt() {
        // BigDecimal sqrt is available in Java 9+
        return Real.of(value.sqrt(MathContext.DECIMAL128).toString());
    }

    @Override
    public Real pow(int exp) {
        return Real.of(value.pow(exp).toString());
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
        return Real.of(Math.pow(value.doubleValue(), exp.doubleValue()));
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
