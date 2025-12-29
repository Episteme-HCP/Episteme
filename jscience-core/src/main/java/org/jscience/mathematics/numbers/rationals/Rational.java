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

package org.jscience.mathematics.numbers.rationals;

import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.numbers.integers.Integer;
import org.jscience.mathematics.structures.rings.FieldElement;

/**
 * Represents a rational number (ℚ), defined as the quotient of two integers.
 * <p>
 * Rational numbers form a Field under addition and multiplication.
 * They are always stored in reduced form (gcd(numerator, denominator) = 1)
 * with a positive denominator.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Rational extends Number implements Comparable<Rational>, Field<Rational>, FieldElement<Rational> {

    private final Integer numerator;
    private final Integer denominator;

    public static final Rational ZERO = new Rational(Integer.ZERO, Integer.ONE);
    public static final Rational ONE = new Rational(Integer.ONE, Integer.ONE);

    private Rational(Integer numerator, Integer denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Rational of(Integer numerator, Integer denominator) {
        if (denominator.equals(Integer.ZERO)) {
            throw new ArithmeticException("Division by zero");
        }
        if (denominator.signum() < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }
        Integer gcd = numerator.gcd(denominator);
        return new Rational(numerator.divide(gcd), denominator.divide(gcd));
    }

    public static Rational of(long numerator, long denominator) {
        return of(Integer.of(numerator), Integer.of(denominator));
    }

    public static Rational of(int numerator, int denominator) {
        return of(Integer.of(numerator), Integer.of(denominator));
    }

    public static Rational of(long value) {
        return of(Integer.of(value), Integer.ONE);
    }

    public static Rational of(int value) {
        return of(Integer.of(value), Integer.ONE);
    }

    public Integer getNumerator() {
        return numerator;
    }

    public Integer getDenominator() {
        return denominator;
    }

    // --- Field Implementation ---

    // --- Field Implementation (Removed from Scalar, kept as methods) ---

    public Rational operate(Rational a, Rational b) {
        return a.add(b);
    }

    public Rational add(Rational a, Rational b) {
        return a.add(b);
    }

    public Rational add(Rational other) {
        // a/b + c/d = (ad + bc) / bd
        Integer newNum = this.numerator.multiply(other.denominator)
                .add(this.denominator.multiply(other.numerator));
        Integer newDen = this.denominator.multiply(other.denominator);
        return of(newNum, newDen);
    }

    public Rational zero() {
        return ZERO;
    }

    public Rational negate(Rational a) {
        return a.negate();
    }

    public Rational negate() {
        return new Rational(numerator.negate(), denominator);
    }

    public Rational subtract(Rational a, Rational b) {
        return a.subtract(b);
    }

    public Rational subtract(Rational other) {
        return add(other.negate());
    }

    public Rational multiply(Rational a, Rational b) {
        return a.multiply(b);
    }

    public Rational multiply(Rational other) {
        return of(this.numerator.multiply(other.numerator),
                this.denominator.multiply(other.denominator));
    }

    public Rational one() {
        return ONE;
    }

    public boolean isMultiplicationCommutative() {
        return true;
    }

    public Rational inverse(Rational a) {
        return a.inverse();
    }

    public Rational inverse() {
        if (numerator.equals(Integer.ZERO)) {
            throw new ArithmeticException("Cannot invert zero");
        }
        return of(denominator, numerator);
    }

    public Rational divide(Rational a, Rational b) {
        return a.divide(b);
    }

    public Rational divide(Rational other) {
        return multiply(other.inverse());
    }

    // --- Comparable Implementation ---

    @Override
    public int compareTo(Rational other) {
        // a/b vs c/d <=> ad vs bc (since b,d > 0)
        Integer ad = this.numerator.multiply(other.denominator);
        Integer bc = this.denominator.multiply(other.numerator);
        return ad.compareTo(bc);
    }

    // --- Number Implementation ---

    @Override
    public int intValue() {
        return (int) doubleValue();
    }

    @Override
    public long longValue() {
        return (long) doubleValue();
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public double doubleValue() {
        return numerator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public String toString() {
        if (denominator.equals(Integer.ONE)) {
            return numerator.toString();
        }
        return numerator + "/" + denominator;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Rational))
            return false;
        Rational other = (Rational) obj;
        return numerator.equals(other.numerator) && denominator.equals(other.denominator);
    }

    @Override
    public int hashCode() {
        return 31 * numerator.hashCode() + denominator.hashCode();
    }

    // --- Field Interface Implementation ---
    @Override
    public int characteristic() {
        return 0; // Rational numbers have characteristic 0
    }

    @Override
    public boolean contains(Rational element) {
        return element != null;
    }

    @Override
    public String description() {
        return "Rational Numbers (\u211a)";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public Rational abs() {
        return (numerator.signum() < 0) ? negate() : this;
    }
}
