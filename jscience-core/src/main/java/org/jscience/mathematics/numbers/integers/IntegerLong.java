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
 * Integer backed by a 64-bit {@code long}.
 * Package-private implementation detail.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
final class IntegerLong extends Integer {

    private final long value;

    private IntegerLong(long value) {
        this.value = value;
    }

    public static IntegerLong of(long value) {
        return new IntegerLong(value);
    }

    @Override
    public Integer add(Integer other) {
        long otherValue = other.longValue();

        if (MathContext.getCurrent().isOverflowCheckingEnabled()) {
            // Check for overflow
            if ((otherValue > 0 && value > java.lang.Long.MAX_VALUE - otherValue) ||
                    (otherValue < 0 && value < java.lang.Long.MIN_VALUE - otherValue)) {
                return IntegerBig.of(BigInteger.valueOf(value).add(BigInteger.valueOf(otherValue)));
            }
        }

        long result = value + otherValue;
        // Try to downgrade
        if (result >= java.lang.Integer.MIN_VALUE && result <= java.lang.Integer.MAX_VALUE) {
            return IntegerInt.create((int) result);
        }
        return IntegerLong.of(result);
    }

    @Override
    public Integer subtract(Integer other) {
        long otherValue = other.longValue();

        if (MathContext.getCurrent().isOverflowCheckingEnabled()) {
            // Check for overflow
            if ((otherValue < 0 && value > java.lang.Long.MAX_VALUE + otherValue) ||
                    (otherValue > 0 && value < java.lang.Long.MIN_VALUE + otherValue)) {
                return IntegerBig.of(BigInteger.valueOf(value).subtract(BigInteger.valueOf(otherValue)));
            }
        }

        long result = value - otherValue;
        // Try to downgrade
        if (result >= java.lang.Integer.MIN_VALUE && result <= java.lang.Integer.MAX_VALUE) {
            return IntegerInt.create((int) result);
        }
        return IntegerLong.of(result);
    }

    @Override
    public Integer multiply(Integer other) {
        long otherValue = other.longValue();

        if (MathContext.getCurrent().isOverflowCheckingEnabled()) {
            // Check overflow
            // Simple check: if result / other != value
            if (otherValue != 0) {
                long r = value * otherValue;
                if (r / otherValue != value) {
                    return IntegerBig.of(BigInteger.valueOf(value).multiply(BigInteger.valueOf(otherValue)));
                }
            }
        }

        long result = value * otherValue;
        // Try to downgrade
        if (result >= java.lang.Integer.MIN_VALUE && result <= java.lang.Integer.MAX_VALUE) {
            return IntegerInt.create((int) result);
        }
        return IntegerLong.of(result);
    }

    @Override
    public Integer divide(Integer other) {
        if (other.isZero()) {
            throw new ArithmeticException("Division by zero");
        }
        long otherValue = other.longValue();

        // Special case: MIN_VALUE / -1 overflows long
        if (value == java.lang.Long.MIN_VALUE && otherValue == -1) {
            return IntegerBig.of(BigInteger.valueOf(value).negate());
        }

        long result = value / otherValue;
        if (result >= java.lang.Integer.MIN_VALUE && result <= java.lang.Integer.MAX_VALUE) {
            return IntegerInt.create((int) result);
        }
        return IntegerLong.of(result);
    }

    @Override
    public Integer remainder(Integer other) {
        if (other.isZero()) {
            throw new ArithmeticException("Division by zero");
        }
        long result = value % other.longValue();
        if (result >= java.lang.Integer.MIN_VALUE && result <= java.lang.Integer.MAX_VALUE) {
            return IntegerInt.create((int) result);
        }
        return IntegerLong.of(result);
    }

    @Override
    public Integer mod(Integer m) {
        if (m.compareTo(Integer.ZERO) <= 0)
            throw new ArithmeticException("Modulus not positive");
        return Integer.of(bigIntegerValue().mod(m.bigIntegerValue()));
    }

    @Override
    public Integer negate() {
        if (value == java.lang.Long.MIN_VALUE) {
            return IntegerBig.of(BigInteger.valueOf(value).negate());
        }
        long result = -value;
        if (result >= java.lang.Integer.MIN_VALUE && result <= java.lang.Integer.MAX_VALUE) {
            return IntegerInt.create((int) result);
        }
        return IntegerLong.of(result);
    }

    @Override
    public Integer abs() {
        if (value == java.lang.Long.MIN_VALUE) {
            return IntegerBig.of(BigInteger.valueOf(value).abs());
        }
        long result = Math.abs(value);
        if (result >= java.lang.Integer.MIN_VALUE && result <= java.lang.Integer.MAX_VALUE) {
            return IntegerInt.create((int) result);
        }
        return IntegerLong.of(result);
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
    public int compareTo(Integer other) {
        if (other instanceof IntegerInt) {
            return java.lang.Long.compare(value, ((IntegerInt) other).longValue());
        }
        if (other instanceof IntegerLong) {
            return java.lang.Long.compare(value, ((IntegerLong) other).value);
        }
        return this.bigIntegerValue().compareTo(other.bigIntegerValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Integer))
            return false;
        Integer other = (Integer) obj;
        if (other instanceof IntegerInt || other instanceof IntegerLong) {
            return value == other.longValue();
        }
        return this.bigIntegerValue().equals(other.bigIntegerValue());
    }

    @Override
    public int hashCode() {
        return java.lang.Long.hashCode(value);
    }

    @Override
    public String toString() {
        return java.lang.Long.toString(value);
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
    public Integer inverse(Integer element) {
        return element.negate();
    }

    @Override
    public Integer operate(Integer left, Integer right) {
        return left.add(right);
    }

    @Override
    public String description() {
        return "Integer (64-bit)";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Integer element) {
        return element != null;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true;
    }

    @Override
    public Integer one() {
        return Integer.ONE;
    }

    @Override
    public Integer multiply(Integer left, Integer right) {
        return left.multiply(right);
    }
}

