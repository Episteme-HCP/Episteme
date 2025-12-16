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

/**
 * Natural number backed by arbitrary-precision {@link BigInteger}.
 * Package-private implementation detail - users work with {@link Natural}.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
final class NaturalBig extends Natural {

    private final BigInteger value;

    private NaturalBig(BigInteger value) {
        this.value = value;
    }

    public static NaturalBig of(BigInteger value) {
        return new NaturalBig(value);
    }

    @Override
    public Natural add(Natural other) {
        BigInteger result = value.add(other.bigIntegerValue());
        return Natural.of(result);
    }

    @Override
    public Natural subtract(Natural other) {
        BigInteger result = value.subtract(other.bigIntegerValue());
        if (result.signum() < 0) {
            throw new ArithmeticException("Result of subtraction is negative");
        }
        return Natural.of(result);
    }

    @Override
    public Natural multiply(Natural other) {
        BigInteger result = value.multiply(other.bigIntegerValue());
        return Natural.of(result);
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
    public Natural divide(Natural other) {
        if (other.isZero()) {
            throw new ArithmeticException("Division by zero");
        }
        BigInteger result = value.divide(other.bigIntegerValue());
        return Natural.of(result);
    }

    @Override
    public Natural modulo(Natural other) {
        if (other.isZero()) {
            throw new ArithmeticException("Modulo by zero");
        }
        BigInteger result = value.mod(other.bigIntegerValue());
        return Natural.of(result);
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
    public Natural one() {
        return Natural.ONE;
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        if (value.bitLength() > 63) {
            throw new ArithmeticException("Value too large for long: " + value);
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
    public boolean isMultiplicationCommutative() {
        return true;
    }

    @Override
    public BigInteger bigIntegerValue() {
        return value;
    }

    @Override
    public String description() {
        return "Natural (Arbitrary Precision)";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Natural element) {
        return element != null;
    }

    @Override
    public int compareTo(Natural other) {
        return value.compareTo(other.bigIntegerValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Natural))
            return false;

        Natural other = (Natural) obj;
        return value.equals(other.bigIntegerValue());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
