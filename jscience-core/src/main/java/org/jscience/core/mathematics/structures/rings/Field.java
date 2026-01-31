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

package org.jscience.core.mathematics.structures.rings;

/**
 * A field is a commutative ring where every non-zero element has a
 * multiplicative inverse.
 * <p>
 * Fields are algebraic structures where addition, subtraction, multiplication,
 * and
 * division (except by zero) are all well-defined. They are fundamental in
 * mathematics,
 * appearing in linear algebra, number theory, and many applications.
 * </p>
 *
 * <h2>Mathematical Definition</h2>
 * <p>
 * A field (F, +, Ãƒâ€”) is a ring satisfying:
 * <ol>
 * <li>Commutativity of multiplication: a Ãƒâ€” b = b Ãƒâ€” a</li>
 * <li>Existence of unity: Ã¢Ë†Æ’ 1 Ã¢Ë†Ë† F such that 1 Ãƒâ€” a = a for all a</li>
 * <li>Multiplicative inverses: Ã¢Ë†â‚¬ a Ã¢Ë†Ë† F \ {0}, Ã¢Ë†Æ’ aÃ¢ÂÂ»Ã‚Â¹ such that a Ãƒâ€” aÃ¢ÂÂ»Ã‚Â¹ = 1</li>
 * </ol>
 * This means (F \ {0}, Ãƒâ€”) forms an abelian group.
 * </p>
 *
 * <h2>Examples</h2>
 * <ul>
 * <li>Ã¢â€žÅ¡ - Rational numbers</li>
 * <li>Ã¢â€žÂ - Real numbers</li>
 * <li>Ã¢â€žâ€š - Complex numbers</li>
 * <li>Ã°Ââ€Â½Ã¢â€šÅ¡ - Integers modulo prime p</li>
 * <li>Ã¢â€žÅ¡(Ã¢Ë†Å¡2) - Rationals extended with Ã¢Ë†Å¡2</li>
 * <li>Ã¢â€žÂ(x) - Rational functions over reals</li>
 * </ul>
 *
 * <h2>Not Fields</h2>
 * <ul>
 * <li>Ã¢â€žÂ¤ - Integers (no multiplicative inverses: 2Ã¢ÂÂ»Ã‚Â¹ Ã¢Ë†â€° Ã¢â€žÂ¤)</li>
 * <li>MÃ¢â€šâ€š(Ã¢â€žÂ) - 2Ãƒâ€”2 matrices (not commutative)</li>
 * <li>Ã¢â€žÂ¤/6Ã¢â€žÂ¤ - Integers mod 6 (zero divisors: 2 Ãƒâ€” 3 = 0 mod 6)</li>
 * </ul>
 *
 * <h2>Usage</h2>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Field<E> extends Ring<E> {

    /**
     * Returns the multiplicative inverse of a non-zero element.
     * <p>
     * For element a Ã¢â€°Â  0, returns aÃ¢ÂÂ»Ã‚Â¹ such that: a Ãƒâ€” aÃ¢ÂÂ»Ã‚Â¹ = aÃ¢ÂÂ»Ã‚Â¹ Ãƒâ€” a = 1
     * </p>
     * <p>
     * Examples:
     * <ul>
     * <li>In Ã¢â€žÅ¡: inverse(2/3) = 3/2</li>
     * <li>In Ã¢â€žÂ: inverse(5.0) = 0.2</li>
     * <li>In Ã¢â€žâ€š: inverse(3+4i) = (3-4i)/25</li>
     * </ul>
     * </p>
     * 
     * @param element the element to invert (must be non-zero)
     * @return the multiplicative inverse
     * @throws NullPointerException if element is null
     * @throws ArithmeticException  if element is zero
     * 
     * @see #divide(Object, Object)
     * @see #one()
     */
    E inverse(E element);

    /**
     * Returns the quotient of two elements (division).
     * <p>
     * Defined as: a ÃƒÂ· b = a Ãƒâ€” bÃ¢ÂÂ»Ã‚Â¹
     * </p>
     * 
     * @param dividend the dividend (numerator)
     * @param divisor  the divisor (denominator, must be non-zero)
     * @return dividend ÃƒÂ· divisor
     * @throws NullPointerException if either argument is null
     * @throws ArithmeticException  if divisor is zero
     * 
     * @see #inverse(Object)
     * @see #multiply(Object, Object)
     */
    default E divide(E dividend, E divisor) {
        return multiply(dividend, inverse(divisor));
    }

    /**
     * Fields always have unity (multiplicative identity).
     * 
     * @return always {@code true}
     */
    @Override
    default boolean hasUnity() {
        return true;
    }

    /**
     * Fields always have commutative multiplication.
     * 
     * @return always {@code true}
     */
    @Override
    default boolean isMultiplicationCommutative() {
        return true;
    }

    /**
     * Returns the characteristic of this field.
     * <p>
     * The characteristic is the smallest positive integer n such that:
     * <br>
     * 1 + 1 + ... + 1 (n times) = 0
     * <br>
     * If no such n exists, the characteristic is 0.
     * </p>
     * <p>
     * Examples:
     * <ul>
     * <li>char(Ã¢â€žÅ¡) = char(Ã¢â€žÂ) = char(Ã¢â€žâ€š) = 0</li>
     * <li>char(Ã°Ââ€Â½Ã¢â€šÅ¡) = p (for prime p)</li>
     * </ul>
     * </p>
     * 
     * @return the characteristic (0 for infinite fields, p for finite fields)
     */
    int characteristic();
}


