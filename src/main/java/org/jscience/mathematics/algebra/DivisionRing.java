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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.algebra;

/**
 * A division ring (also known as a skew field) is a ring in which every
 * non-zero
 * element has a multiplicative inverse.
 * <p>
 * Division rings satisfy all field axioms except for commutative
 * multiplication.
 * </p>
 * 
 * <h2>Mathematical Definition</h2>
 * <p>
 * A division ring (D, +, ×) is a ring with unity (1 ≠ 0) such that:
 * <ul>
 * <li>For every a ∈ D \ {0}, there exists a⁻¹ ∈ D such that a × a⁻¹ = a⁻¹ × a =
 * 1</li>
 * </ul>
 * </p>
 * 
 * <h2>Examples</h2>
 * <ul>
 * <li>ℍ - Quaternions (non-commutative)</li>
 * <li>Field - Commutative division ring</li>
 * </ul>
 * 
 * @param <E> the type of elements in this division ring
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * 
 * @see Ring
 * @see Field
 */
public interface DivisionRing<E> extends Ring<E> {

    /**
     * Returns the multiplicative inverse of a non-zero element.
     * <p>
     * For element a ≠ 0, returns a⁻¹ such that: a × a⁻¹ = a⁻¹ × a = 1
     * </p>
     * 
     * @param element the element to invert (must be non-zero)
     * @return the multiplicative inverse
     * @throws ArithmeticException if element is zero
     */
    E inverse(E element);

    /**
     * Returns the quotient of two elements (division).
     * <p>
     * Defined as: a ÷ b = a × b⁻¹
     * </p>
     * <p>
     * Note: In non-commutative rings, this is typically defined as right division.
     * For left division (b⁻¹ × a), explicit multiplication by inverse is
     * recommended.
     * </p>
     * 
     * @param dividend the dividend (numerator)
     * @param divisor  the divisor (denominator, must be non-zero)
     * @return dividend ÷ divisor
     * @throws ArithmeticException if divisor is zero
     */
    default E divide(E dividend, E divisor) {
        return multiply(dividend, inverse(divisor));
    }
}


