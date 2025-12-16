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

package org.jscience.mathematics.numbers.integers;

import java.math.BigInteger;
import org.jscience.mathematics.context.MathContext;

/**
 * Integer backed by a 32-bit {@code int}.
 * Package-private implementation detail.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
final class IntegerInt extends Integer {

    private final int value;

    private IntegerInt(int value) {
        this.value = value;
    }

    static IntegerInt create(int value) {
        return new IntegerInt(value);
    }

    @Override
    public Integer add(Integer other) {
        if (other instanceof IntegerInt) {
            int otherValue = ((IntegerInt) other).value;

            if (MathContext.getCurrent().isOverflowCheckingEnabled()) {
                // Check for overflow: (b > 0 && a > MAX - b) || (b < 0 && a < MIN - b)
                if ((otherValue > 0 && value > java.lang.Integer.MAX_VALUE - otherValue) ||
                        (otherValue < 0 && value < java.lang.Integer.MIN_VALUE - otherValue)) {
                    return IntegerLong.of((long) value + (long) otherValue);
                }
            }

            return IntegerInt.create(value + otherValue);
        }
        return Integer.of(this.bigIntegerValue().add(other.bigIntegerValue()));
    }

    @Override
    public Integer subtract(Integer other) {
        if (other instanceof IntegerInt) {
            int otherValue = ((IntegerInt) other).value;

            if (MathContext.getCurrent().isOverflowCheckingEnabled()) {
                // Check for overflow: (b < 0 && a > MAX + b) || (b > 0 && a < MIN + b)
                // Note: -b can overflow if b is MIN_VALUE, so use long for check or careful
                // logic
                if ((otherValue < 0 && value > java.lang.Integer.MAX_VALUE + otherValue) ||
                        (otherValue > 0 && value < java.lang.Integer.MIN_VALUE + otherValue)) {
                    return IntegerLong.of((long) value - (long) otherValue);
                }
            }

            return IntegerInt.create(value - otherValue);
        }
        return Integer.of(this.bigIntegerValue().subtract(other.bigIntegerValue()));
    }

    @Override
    public Integer multiply(Integer other) {
        if (other instanceof IntegerInt) {
            int otherValue = ((IntegerInt) other).value;

            if (MathContext.getCurrent().isOverflowCheckingEnabled()) {
                long res = (long) value * (long) otherValue;
                if (res > java.lang.Integer.MAX_VALUE || res < java.lang.Integer.MIN_VALUE) {
                    return IntegerLong.of(res);
                }
            }

            return IntegerInt.create(value * otherValue);
        }
        return Integer.of(this.bigIntegerValue().multiply(other.bigIntegerValue()));
    }

    @Override
    public Integer divide(Integer other) {
        if (other.isZero()) {
            throw new ArithmeticException("Division by zero");
        }
        if (other instanceof IntegerInt) {
            int otherValue = ((IntegerInt) other).value;
            // Special case: MIN_VALUE / -1 overflows int
            if (value == java.lang.Integer.MIN_VALUE && otherValue == -1) {
                return IntegerLong.of((long) value / -1L);
            }
            return IntegerInt.create(value / otherValue);
        }
        return Integer.of(this.bigIntegerValue().divide(other.bigIntegerValue()));
    }

    @Override
    public Integer remainder(Integer other) {
        if (other.isZero()) {
            throw new ArithmeticException("Division by zero");
        }
        if (other instanceof IntegerInt) {
            return IntegerInt.create(value % ((IntegerInt) other).value);
        }
        return Integer.of(this.bigIntegerValue().remainder(other.bigIntegerValue()));
    }

    @Override
    public Integer negate() {
        if (value == java.lang.Integer.MIN_VALUE) {
            return IntegerLong.of(-(long) value);
        }
        return IntegerInt.create(-value);
    }

    @Override
    public Integer abs() {
        if (value == java.lang.Integer.MIN_VALUE) {
            return IntegerLong.of(-(long) value);
        }
        return IntegerInt.create(Math.abs(value));
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
            return java.lang.Integer.compare(value, ((IntegerInt) other).value);
        }
        if (other instanceof IntegerLong) {
            return java.lang.Long.compare(value, ((IntegerLong) other).longValue());
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
        if (other instanceof IntegerInt) {
            return value == ((IntegerInt) other).value;
        }
        return this.bigIntegerValue().equals(other.bigIntegerValue());
    }

    @Override
    public int hashCode() {
        return java.lang.Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return java.lang.Integer.toString(value);
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
    public Integer inverse(Integer element) {
        return element.negate();
    }

    @Override
    public Integer operate(Integer left, Integer right) {
        return left.add(right);
    }

    @Override
    public String description() {
        return "Integer (32-bit)";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Integer mod(Integer m) {
        if (m.compareTo(Integer.ZERO) <= 0)
            throw new ArithmeticException("Modulus not positive");
        return Integer.of(bigIntegerValue().mod(m.bigIntegerValue()));
    }

    @Override
    public boolean contains(Integer element) {
        return element != null;
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true; // Integer multiplication is commutative
    }

    @Override
    public Integer one() {
        return IntegerInt.create(1);
    }

    @Override
    public Integer multiply(Integer left, Integer right) {
        return left.multiply(right);
    }
}
