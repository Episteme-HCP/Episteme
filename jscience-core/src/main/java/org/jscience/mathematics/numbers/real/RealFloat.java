/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

/**
 * Real number backed by a 32-bit {@code float}.
 * Package-private implementation detail.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
final class RealFloat extends Real {

    private final float value;

    private RealFloat(float value) {
        this.value = value;
    }

    public static RealFloat create(float value) {
        return new RealFloat(value);
    }

    @Override
    public Real add(Real other) {
        return Real.of(value + other.doubleValue());
    }

    @Override
    public Real subtract(Real other) {
        return Real.of(value - other.doubleValue());
    }

    @Override
    public Real multiply(Real other) {
        return Real.of(value * other.doubleValue());
    }

    @Override
    public Real divide(Real other) {
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

    @Override
    public int characteristic() {
        return 0; // Real numbers have characteristic 0 (infinite field)
    }
}