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

/**
 * Integer backed by arbitrary-precision {@link BigInteger}.
 * Package-private implementation detail.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
final class IntegerBig extends Integer {

    private final BigInteger value;

    private IntegerBig(BigInteger value) {
        this.value = value;
    }

    public static IntegerBig of(BigInteger value) {
        return new IntegerBig(value);
    }

    @Override
    public Integer add(Integer other) {
        return Integer.of(value.add(other.bigIntegerValue()));
    }

    @Override
    public Integer subtract(Integer other) {
        return Integer.of(value.subtract(other.bigIntegerValue()));
    }

    @Override
    public Integer multiply(Integer other) {
        return Integer.of(value.multiply(other.bigIntegerValue()));
    }

    @Override
    public Integer divide(Integer other) {
        if (other.isZero()) {
            throw new ArithmeticException("Division by zero");
        }
        return Integer.of(value.divide(other.bigIntegerValue()));
    }

    @Override
    public Integer remainder(Integer other) {
        if (other.isZero()) {
            throw new ArithmeticException("Division by zero");
        }
        return Integer.of(value.remainder(other.bigIntegerValue()));
    }

    @Override
    public Integer mod(Integer m) {
        if (m.compareTo(Integer.ZERO) <= 0)
            throw new ArithmeticException("Modulus not positive");
        return Integer.of(value.mod(m.bigIntegerValue()));
    }

    @Override
    public Integer negate() {
        return Integer.of(value.negate());
    }

    @Override
    public Integer abs() {
        return Integer.of(value.abs());
    }

    @Override
    public boolean isZero() {
        return value.equals(BigInteger.ZERO);
    }

    @Override
    public boolean isOne() {
        return value.equals(BigInteger.ONE);
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        if (value.bitLength() > 63) {
            throw new ArithmeticException("Value too large for long");
        }
        return value.longValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public BigInteger bigIntegerValue() {
        return value;
    }

    @Override
    public Integer operate(Integer left, Integer right) {
        return left.add(right);
    }

    @Override
    public Integer inverse(Integer element) {
        return element.negate();
    }

    @Override
    public int compareTo(Integer other) {
        return value.compareTo(other.bigIntegerValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Integer))
            return false;
        Integer other = (Integer) obj;
        return value.equals(other.bigIntegerValue());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String description() {
        return "Integer (Arbitrary Precision)";
    }

    @Override
    public String toString() {
        return value.toString();
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
    public Integer one() {
        return Integer.ONE;
    }

    @Override
    public Integer multiply(Integer left, Integer right) {
        return left.multiply(right);
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return true;
    }
}