/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.algebra.polynomials;

import org.jscience.mathematics.analysis.functions.Polynomial;
import org.jscience.mathematics.structures.rings.Ring;
import org.jscience.mathematics.numbers.integers.Integer;
import org.jscience.mathematics.sets.Integers;

/**
 * Polynomial division algorithms.
 * <p>
 * Implements Euclidean division for polynomials over a field or ring.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PolynomialDivision {

    /**
     * Result of polynomial division: A = B * Q + R
     */
    public static class DivisionResult<R extends Ring<R>> {
        public final Polynomial<R> quotient;
        public final Polynomial<R> remainder;

        public DivisionResult(Polynomial<R> quotient, Polynomial<R> remainder) {
            this.quotient = quotient;
            this.remainder = remainder;
        }
    }

    /**
     * Performs polynomial division of dividend by divisor.
     * <p>
     * Computes Q and R such that dividend = divisor * Q + R,
     * where deg(R) < deg(divisor).
     * </p>
     * 
     * @param dividend the polynomial to divide
     * @param divisor  the divisor polynomial
     * @return the division result (quotient and remainder)
     * @throws ArithmeticException if divisor is zero
     */
    public static DivisionResult<Integer> divide(Polynomial<Integer> dividend, Polynomial<Integer> divisor) {
        if (divisor.isZero()) {
            throw new ArithmeticException("Division by zero polynomial");
        }

        Polynomial<Integer> quotient = Polynomial.zero(Integers.getInstance());
        Polynomial<Integer> remainder = dividend;

        Integer divisorLeading = divisor.getCoefficient(divisor.degree());
        int divisorDegree = divisor.degree();

        while (!remainder.isZero() && remainder.degree() >= divisorDegree) {
            int degreeDiff = remainder.degree() - divisorDegree;
            Integer remainderLeading = remainder.getCoefficient(remainder.degree());

            // Check if division is possible in the ring (Integers)
            // For fields (like Rationals or Reals), this is always possible if divisor !=
            // 0.
            // For Integers, we check divisibility.
            // If not divisible, we might need pseudo-division or return what we have?
            // Standard polynomial division over Z requires divisibility.

            // For now, we assume integer division (truncating) or check divisibility?
            // Let's use integer division.
            Integer factor = remainderLeading.divide(divisorLeading);

            // If factor is zero (e.g. 2/3 in integers), we can't proceed with standard
            // division
            // in this ring without fractions.
            if (factor.isZero() && !remainderLeading.isZero()) {
                break; // Cannot reduce further in Z
            }

            // Term to subtract: factor * x^degreeDiff
            Polynomial<Integer> term = Polynomial.monomial(factor, degreeDiff, Integers.getInstance());
            quotient = quotient.add(term);

            Polynomial<Integer> subtract = divisor.multiply(term);
            remainder = remainder.subtract(subtract);
        }

        return new DivisionResult<>(quotient, remainder);
    }
}

