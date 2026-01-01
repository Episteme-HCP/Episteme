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

package org.jscience.mathematics.structures.rings;

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
 * A division ring (D, +, Ãƒâ€”) is a ring with unity (1 Ã¢â€°Â  0) such that:
 * <ul>
 * <li>For every a Ã¢Ë†Ë† D \ {0}, there exists aÃ¢ÂÂ»Ã‚Â¹ Ã¢Ë†Ë† D such that a Ãƒâ€” aÃ¢ÂÂ»Ã‚Â¹ = aÃ¢ÂÂ»Ã‚Â¹ Ãƒâ€” a =
 * 1</li>
 * </ul>
 * </p>
 *
 * <h2>Examples</h2>
 * <ul>
 * <li>Ã¢â€žÂ - Quaternions (non-commutative)</li>
 * <li>Field - Commutative division ring</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface DivisionRing<E> extends Ring<E> {

    /**
     * Returns the multiplicative inverse of a non-zero element.
     * <p>
     * For element a Ã¢â€°Â  0, returns aÃ¢ÂÂ»Ã‚Â¹ such that: a Ãƒâ€” aÃ¢ÂÂ»Ã‚Â¹ = aÃ¢ÂÂ»Ã‚Â¹ Ãƒâ€” a = 1
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
     * Defined as: a ÃƒÂ· b = a Ãƒâ€” bÃ¢ÂÂ»Ã‚Â¹
     * </p>
     * <p>
     * Note: In non-commutative rings, this is typically defined as right division.
     * For left division (bÃ¢ÂÂ»Ã‚Â¹ Ãƒâ€” a), explicit multiplication by inverse is
     * recommended.
     * </p>
     * 
     * @param dividend the dividend (numerator)
     * @param divisor  the divisor (denominator, must be non-zero)
     * @return dividend ÃƒÂ· divisor
     * @throws ArithmeticException if divisor is zero
     */
    default E divide(E dividend, E divisor) {
        return multiply(dividend, inverse(divisor));
    }
}

