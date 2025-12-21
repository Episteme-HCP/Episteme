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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.mathematics.numbers.integers;

import java.math.BigInteger;
import org.jscience.mathematics.structures.rings.Ring;
import org.jscience.mathematics.structures.rings.RingElement;

/**
 * Abstract base class for integers (ℤ = {..., -2, -1, 0, 1, 2, ...}).
 * <p>
 * Integers form a Ring under addition and multiplication.
 * This class provides a smart factory that automatically chooses the
 * most efficient backing implementation:
 * </p>
 * <ul>
 * <li>Small values (fit in {@code int}): Uses {@code int} for maximum
 * performance</li>
 * <li>Medium values (fit in {@code long}): Uses {@code long} for good
 * performance</li>
 * <li>Large values: Uses {@link BigInteger} for arbitrary precision</li>
 * </ul>
 * 
 * <h2>Usage</h2>
 * 
 * <pre>
 * {@code
 * // Smart factory chooses optimal backing
 * Integer small = Integer.of(100); // Uses int internally
 * Integer large = Integer.of(1_000_000_000_000L); // Uses long
 * Integer huge = Integer.of(new BigInteger("999999999999999999999")); // Uses BigInteger
 * }
 * </pre>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class Integer extends Number implements Ring<Integer>, Comparable<Integer>, RingElement<Integer> {

    /** The integer 0 */
    public static final Integer ZERO = IntegerInt.create(0);

    /** The integer 1 */
    public static final Integer ONE = IntegerInt.create(1);

    /** The integer -1 */
    public static final Integer MINUS_ONE = IntegerInt.create(-1);

    /**
     * Creates an integer from an int value.
     * 
     * @param value the value
     * @return the Integer instance
     */
    public static Integer of(int value) {
        if (value == 0)
            return ZERO;
        if (value == 1)
            return ONE;
        if (value == -1)
            return MINUS_ONE;
        return IntegerInt.create(value);
    }

    /**
     * Creates an integer from a long value.
     * Automatically selects optimal backing implementation.
     * 
     * @param value the value
     * @return the Integer instance
     */
    public static Integer of(long value) {
        if (value == 0)
            return ZERO;
        if (value == 1)
            return ONE;
        if (value == -1)
            return MINUS_ONE;

        // Smart delegation
        if (value >= java.lang.Integer.MIN_VALUE && value <= java.lang.Integer.MAX_VALUE) {
            return IntegerInt.create((int) value);
        } else {
            return IntegerLong.of(value);
        }
    }

    /**
     * Creates an integer from a BigInteger.
     * Automatically selects optimal backing implementation.
     * 
     * @param value the value
     * @return the Integer instance
     * @throws IllegalArgumentException if value is null
     */
    public static Integer of(BigInteger value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        // Try to use smaller representation if possible
        if (value.bitLength() < 31) { // < 31 bits fits in signed int (usually)
            // Safe check for int range
            try {
                return IntegerInt.create(value.intValueExact());
            } catch (ArithmeticException e) {
                // Fallthrough
            }
        }

        if (value.bitLength() < 63) {
            try {
                return IntegerLong.of(value.longValueExact());
            } catch (ArithmeticException e) {
                // Fallthrough
            }
        }

        // Fallback checks for exact boundaries if bitLength is close
        if (value.compareTo(BigInteger.valueOf(java.lang.Integer.MAX_VALUE)) <= 0
                && value.compareTo(BigInteger.valueOf(java.lang.Integer.MIN_VALUE)) >= 0) {
            return IntegerInt.create(value.intValue());
        }

        if (value.compareTo(BigInteger.valueOf(java.lang.Long.MAX_VALUE)) <= 0
                && value.compareTo(BigInteger.valueOf(java.lang.Long.MIN_VALUE)) >= 0) {
            return IntegerLong.of(value.longValue());
        }

        return IntegerBig.of(value);
    }

    /**
     * Creates an integer from a String representation.
     * 
     * @param value the string value
     * @return the Integer instance
     */
    public static Integer valueOf(String value) {
        return of(new BigInteger(value));
    }

    // Package-private constructor
    Integer() {
    }

    // --- Abstract operations ---

    /**
     * Adds another integer.
     * 
     * @param other the addend
     * @return this + other
     */
    public abstract Integer add(Integer other);

    /**
     * Subtracts another integer.
     * 
     * @param other the subtrahend
     * @return this - other
     */
    public abstract Integer subtract(Integer other);

    /**
     * Multiplies by another integer.
     * 
     * @param other the multiplicand
     * @return this × other
     */
    public abstract Integer multiply(Integer other);

    /**
     * Divides by another integer (integer division).
     * 
     * @param other the divisor
     * @return this / other (truncated towards zero)
     * @throws ArithmeticException if other is zero
     */
    public abstract Integer divide(Integer other);

    /**
     * Computes the remainder of division.
     * 
     * @param other the divisor
     * @return this % other
     * @throws ArithmeticException if other is zero
     */
    public abstract Integer remainder(Integer other);

    /**
     * Returns the negation of this integer.
     * 
     * @return -this
     */
    public abstract Integer negate();

    /**
     * Returns the absolute value of this integer.
     * 
     * @return |this|
     */
    public abstract Integer abs();

    /**
     * Returns true if this integer is zero.
     */
    public abstract boolean isZero();

    /**
     * Returns true if this integer is one.
     */
    public abstract boolean isOne();

    /**
     * Converts this integer to a long.
     * 
     * @return the long value
     * @throws ArithmeticException if the value doesn't fit in a long
     */
    public abstract long longValue();

    /**
     * Converts this integer to a BigInteger.
     * 
     * @return the BigInteger value (never null)
     */
    public abstract BigInteger bigIntegerValue();

    /**
     * Returns the remainder of the division of this integer by the specified
     * integer.
     * 
     * @param divisor the divisor
     * @return this % divisor
     * @throws ArithmeticException if divisor is zero
     */
    public abstract Integer mod(Integer divisor);

    // --- Standard methods ---

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    @Override
    public abstract int compareTo(Integer other);

    // --- Convenience Methods ---

    /**
     * Returns the signum function of this integer.
     * 
     * @return -1, 0 or 1 as the value of this integer is negative, zero or
     *         positive.
     */
    public int signum() {
        return compareTo(ZERO);
    }

    /**
     * Converts this integer to a double.
     * 
     * @return this integer as a double
     */
    public double doubleValue() {
        return bigIntegerValue().doubleValue();
    }

    /**
     * Returns the greatest common divisor of absolute value of this integer and
     * absolute value of other integer.
     * 
     * @param other the other integer
     * @return gcd(|this|, |other|)
     */
    public Integer gcd(Integer other) {
        return Integer.of(this.bigIntegerValue().gcd(other.bigIntegerValue()));
    }

    // --- RingElement Interface Implementation ---

    @Override
    public Integer zero() {
        return ZERO;
    }

    @Override
    public Integer one() {
        return ONE;
    }
}