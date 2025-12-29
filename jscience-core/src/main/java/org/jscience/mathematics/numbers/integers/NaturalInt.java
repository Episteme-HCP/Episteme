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

package org.jscience.mathematics.numbers.integers;

import java.math.BigInteger;
import org.jscience.mathematics.context.MathContext;

/**
 * Natural number backed by a 32-bit {@code int}.
 * Package-private implementation detail - users work with {@link Natural}.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
final class NaturalInt extends Natural {

    private final int value;

    private NaturalInt(int value) {
        this.value = value;
    }

    static NaturalInt create(int value) {
        return new NaturalInt(value);
    }

    @Override
    public Natural add(Natural other) {
        if (other instanceof NaturalInt) {
            int otherValue = ((NaturalInt) other).value;

            // Check overflow (safe mode)
            if (MathContext.getCurrent().isOverflowCheckingEnabled()) {
                // Pre-check: will addition overflow?
                if ((value > 0 && otherValue > java.lang.Integer.MAX_VALUE - value)) {
                    // Promote to long
                    return NaturalLong.of((long) value + (long) otherValue);
                }
            }

            return NaturalInt.create(value + otherValue);
        }

        // Other type - promote to common type
        return Natural.of(this.bigIntegerValue().add(other.bigIntegerValue()));
    }

    @Override
    public Natural subtract(Natural other) {
        if (other instanceof NaturalInt) {
            int otherValue = ((NaturalInt) other).value;
            if (value < otherValue) {
                throw new ArithmeticException(
                        "Result of subtraction is negative: " + value + " - " + otherValue);
            }
            return NaturalInt.create(value - otherValue);
        }

        // Other type - throw (this is smaller)
        throw new ArithmeticException("Result of subtraction is negative");
    }

    @Override
    public Natural multiply(Natural other) {
        if (other instanceof NaturalInt) {
            int otherValue = ((NaturalInt) other).value;

            // Check overflow
            if (MathContext.getCurrent().isOverflowCheckingEnabled()) {
                // Will multiplication overflow?
                if (value != 0 && otherValue > java.lang.Integer.MAX_VALUE / value) {
                    // Promote to long
                    return NaturalLong.of((long) value * (long) otherValue);
                }
            }

            return NaturalInt.create(value * otherValue);
        }

        // Other type - promote
        return Natural.of(this.bigIntegerValue().multiply(other.bigIntegerValue()));
    }

    @Override
    public Natural divide(Natural other) {
        if (other.isZero()) {
            throw new ArithmeticException("Division by zero");
        }

        if (other instanceof NaturalInt) {
            return NaturalInt.create(value / ((NaturalInt) other).value);
        }

        // Dividing by larger number = 0
        return NaturalInt.create(0);
    }

    @Override
    public Natural modulo(Natural other) {
        if (other.isZero()) {
            throw new ArithmeticException("Modulo by zero");
        }

        if (other instanceof NaturalInt) {
            return NaturalInt.create(value % ((NaturalInt) other).value);
        }

        // this < other, so this mod other = this
        return this;
    }

    @Override
    public boolean isZero() {
        return value == 0;
    }

    @Override
    public boolean isOne() {
        return value == 1;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public BigInteger bigIntegerValue() {
        return BigInteger.valueOf(value);
    }

    @Override
    public int compareTo(Natural other) {
        if (other instanceof NaturalInt) {
            return java.lang.Integer.compare(value, ((NaturalInt) other).value);
        } else if (other instanceof NaturalLong) {
            return Long.compare(value, ((NaturalLong) other).longValue());
        } else {
            return this.bigIntegerValue().compareTo(other.bigIntegerValue());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Natural))
            return false;

        Natural other = (Natural) obj;
        if (other instanceof NaturalInt) {
            return value == ((NaturalInt) other).value;
        }

        return this.bigIntegerValue().equals(other.bigIntegerValue());
    }

    @Override
    public int hashCode() {
        return java.lang.Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return (double) value;
    }

    @Override
    public Natural operate(Natural left, Natural right) {
        return left.add(right);
    }

    @Override
    public Natural multiply(Natural left, Natural right) {
        return left.multiply(right);
    }

    @Override
    public Natural one() {
        return Natural.ONE;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true;
    }

    @Override
    public String description() {
        return "Natural (32-bit)";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Natural element) {
        return element != null;
    }
}