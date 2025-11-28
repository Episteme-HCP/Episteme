/*
 * JScience Reimagined - Unified Scientific Computing Framework
 * Copyright (c) 2025 Silvere Martin-Michiellot
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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

import java.math.BigInteger;
import org.jscience.mathematics.context.MathContext;

/**
 * Natural number backed by a 64-bit {@code long}.
 * Package-private implementation detail - users work with {@link Natural}.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
final class NaturalLong extends Natural {

    private final long value;

    private NaturalLong(long value) {
        this.value = value;
    }

    public static NaturalLong of(long value) {
        return new NaturalLong(value);
    }

    @Override
    public Natural add(Natural other) {
        long otherValue = other.longValue();

        // Check overflow
        if (MathContext.getCurrent().isOverflowCheckingEnabled()) {
            if (value > 0 && otherValue > Long.MAX_VALUE - value) {
                // Promote to BigInteger
                return Natural.of(BigInteger.valueOf(value).add(BigInteger.valueOf(otherValue)));
            }
        }

        return NaturalLong.of(value + otherValue);
    }

    @Override
    public Natural subtract(Natural other) {
        long otherValue = other.longValue();
        if (value < otherValue) {
            throw new ArithmeticException(
                    "Result of subtraction is negative: " + value + " - " + otherValue);
        }

        long result = value - otherValue;
        // Try to downgrade to int if possible
        if (result <= java.lang.Integer.MAX_VALUE) {
            return NaturalInt.of((int) result);
        }
        return NaturalLong.of(result);
    }

    @Override
    public Natural multiply(Natural other) {
        long otherValue = other.longValue();

        // Check overflow
        if (MathContext.getCurrent().isOverflowCheckingEnabled()) {
            if (value != 0 && otherValue > Long.MAX_VALUE / value) {
                // Promote to BigInteger
                return Natural.of(BigInteger.valueOf(value).multiply(BigInteger.valueOf(otherValue)));
            }
        }

        return NaturalLong.of(value * otherValue);
    }

    @Override
    public Natural divide(Natural other) {
        if (other.isZero()) {
            throw new ArithmeticException("Division by zero");
        }

        long result = value / other.longValue();
        if (result <= java.lang.Integer.MAX_VALUE) {
            return NaturalInt.of((int) result);
        }
        return NaturalLong.of(result);
    }

    @Override
    public Natural modulo(Natural other) {
        if (other.isZero()) {
            throw new ArithmeticException("Modulo by zero");
        }

        long result = value % other.longValue();
        if (result <= java.lang.Integer.MAX_VALUE) {
            return NaturalInt.of((int) result);
        }
        return NaturalLong.of(result);
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
            return Long.compare(value, other.longValue());
        } else if (other instanceof NaturalLong) {
            return Long.compare(value, ((NaturalLong) other).value);
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
        if (other instanceof NaturalInt || other instanceof NaturalLong) {
            return value == other.longValue();
        }
        return this.bigIntegerValue().equals(other.bigIntegerValue());
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int intValue() {
        return (int) value;
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
        return "Natural (64-bit)";
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
