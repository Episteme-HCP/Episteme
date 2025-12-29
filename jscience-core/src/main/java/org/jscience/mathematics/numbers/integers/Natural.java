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
import org.jscience.mathematics.structures.rings.Semiring;

/**
 * Abstract base class for natural numbers (ℕ = {0, 1, 2, ...}).
 * <p>
 * Natural numbers form a semiring under addition and multiplication.
 * This class provides a smart factory that automatically chooses the
 * most efficient backing implementation:
 * </p>
 * <ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class Natural extends Number implements Semiring<Natural>, Comparable<Natural> {

    private static final class Constants {
        private static final Natural ZERO = NaturalInt.create(0);
        private static final Natural ONE = NaturalInt.create(1);
    }

    /** The natural number 0 */
    public static final Natural ZERO = Constants.ZERO;

    /** The natural number 1 */
    public static final Natural ONE = Constants.ONE;

    /**
     * Creates a natural number from an int value.
     * 
     * @param value the value (must be non-negative)
     * @return the Natural instance
     * @throws IllegalArgumentException if value is negative
     */
    public static Natural of(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Natural numbers cannot be negative: " + value);
        }
        if (value == 0)
            return ZERO;
        if (value == 1)
            return ONE;
        return NaturalInt.create(value);
    }

    /**
     * Creates a natural number from a long value.
     * Automatically selects optimal backing implementation.
     * 
     * @param value the value (must be non-negative)
     * @return the Natural instance
     * @throws IllegalArgumentException if value is negative
     */
    public static Natural of(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("Natural numbers cannot be negative: " + value);
        }
        if (value == 0)
            return ZERO;
        if (value == 1)
            return ONE;

        // Smart delegation
        if (value <= java.lang.Integer.MAX_VALUE) {
            return NaturalInt.create((int) value);
        } else {
            return NaturalLong.of(value);
        }
    }

    /**
     * Creates a natural number from a BigInteger.
     * Automatically selects optimal backing implementation.
     * 
     * @param value the value (must be non-negative)
     * @return the Natural instance
     * @throws IllegalArgumentException if value is negative or null
     */
    public static Natural of(BigInteger value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (value.signum() < 0) {
            throw new IllegalArgumentException("Natural numbers cannot be negative: " + value);
        }
        if (value.equals(BigInteger.ZERO))
            return ZERO;
        if (value.equals(BigInteger.ONE))
            return ONE;

        // Try to use smaller representation if possible
        if (value.bitLength() <= 31) {
            return NaturalInt.create(value.intValue());
        } else if (value.bitLength() <= 63) {
            return NaturalLong.of(value.longValue());
        } else {
            return NaturalBig.of(value);
        }
    }

    // Package-private constructor (only subclasses in this package)
    Natural() {
    }

    // --- Abstract operations (implemented by subclasses) ---

    /**
     * Adds another natural number.
     * 
     * @param other the addend
     * @return this + other
     */
    public abstract Natural add(Natural other);

    /**
     * Subtracts another natural number.
     * 
     * @param other the subtrahend
     * @return this - other
     * @throws ArithmeticException if the result would be negative
     */
    public abstract Natural subtract(Natural other);

    /**
     * Multiplies by another natural number.
     * 
     * @param other the multiplicand
     * @return this × other
     */
    public abstract Natural multiply(Natural other);

    /**
     * Divides by another natural number (integer division).
     * 
     * @param other the divisor
     * @return this ÷ other (rounded down)
     * @throws ArithmeticException if other is zero
     */
    public abstract Natural divide(Natural other);

    /**
     * Computes this modulo another natural number.
     * 
     * @param other the modulus
     * @return this mod other
     * @throws ArithmeticException if other is zero
     */
    public abstract Natural modulo(Natural other);

    /**
     * Returns true if this natural number is zero.
     */
    public abstract boolean isZero();

    /**
     * Returns true if this natural number is one.
     */
    public abstract boolean isOne();

    /**
     * Converts this natural number to a long.
     * 
     * @return the long value
     * @throws ArithmeticException if the value doesn't fit in a long
     */
    public abstract long longValue();

    /**
     * Converts this natural number to a BigInteger.
     * 
     * @return the BigInteger value (never null)
     */
    public abstract BigInteger bigIntegerValue();

    /**
     * Alias for {@link #bigIntegerValue()} for compatibility.
     * 
     * @return the BigInteger value (never null)
     */
    public BigInteger toBigInteger() {
        return bigIntegerValue();
    }

    // --- Standard methods ---

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    @Override
    public abstract int compareTo(Natural other);
}