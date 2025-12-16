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

package org.jscience.mathematics.structures.rings;

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
 * A field (F, +, ×) is a ring satisfying:
 * <ol>
 * <li>Commutativity of multiplication: a × b = b × a</li>
 * <li>Existence of unity: ∃ 1 ∈ F such that 1 × a = a for all a</li>
 * <li>Multiplicative inverses: ∀ a ∈ F \ {0}, ∃ a⁻¹ such that a × a⁻¹ = 1</li>
 * </ol>
 * This means (F \ {0}, ×) forms an abelian group.
 * </p>
 * 
 * <h2>Examples</h2>
 * <ul>
 * <li>ℚ - Rational numbers</li>
 * <li>ℝ - Real numbers</li>
 * <li>ℂ - Complex numbers</li>
 * <li>𝔽ₚ - Integers modulo prime p</li>
 * <li>ℚ(√2) - Rationals extended with √2</li>
 * <li>ℝ(x) - Rational functions over reals</li>
 * </ul>
 * 
 * <h2>Not Fields</h2>
 * <ul>
 * <li>ℤ - Integers (no multiplicative inverses: 2⁻¹ ∉ ℤ)</li>
 * <li>M₂(ℝ) - 2×2 matrices (not commutative)</li>
 * <li>ℤ/6ℤ - Integers mod 6 (zero divisors: 2 × 3 = 0 mod 6)</li>
 * </ul>
 * 
 * <h2>Usage</h2>
 * 
 * <pre>{@code
 * // Rational number field
 * Field<Rational> rationals = RationalField.getInstance();
 * 
 * Rational a = new Rational(3, 4); // 3/4
 * Rational b = new Rational(2, 5); // 2/5
 * 
 * // Field operations
 * Rational sum = rationals.add(a, b); // 23/20
 * Rational product = rationals.multiply(a, b); // 6/20 = 3/10
 * Rational inverse = rationals.inverse(a); // 4/3
 * Rational quotient = rationals.divide(a, b); // 15/8
 * 
 * // Field axioms
 * Rational one = rationals.one(); // 1
 * Rational check = rationals.multiply(a, inverse); // 3/4 × 4/3 = 1
 * assert check.equals(one);
 * }</pre>
 * 
 * <h2>Scientific Applications</h2>
 * <p>
 * Fields are essential for:
 * <ul>
 * <li><strong>Linear Algebra</strong>: Vector spaces defined over fields</li>
 * <li><strong>Differential Equations</strong>: Solutions in field of
 * functions</li>
 * <li><strong>Quantum Mechanics</strong>: Complex Hilbert spaces</li>
 * <li><strong>Signal Processing</strong>: Fourier transforms over ℂ</li>
 * <li><strong>Cryptography</strong>: Finite fields 𝔽ₚ</li>
 * </ul>
 * </p>
 * 
 * @param <E> the type of elements in this field
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.0
 * @since 1.0
 * 
 * @see Ring
 * @see AbelianGroup
 */
public interface Field<E> extends Ring<E> {

    /**
     * Returns the multiplicative inverse of a non-zero element.
     * <p>
     * For element a ≠ 0, returns a⁻¹ such that: a × a⁻¹ = a⁻¹ × a = 1
     * </p>
     * <p>
     * Examples:
     * <ul>
     * <li>In ℚ: inverse(2/3) = 3/2</li>
     * <li>In ℝ: inverse(5.0) = 0.2</li>
     * <li>In ℂ: inverse(3+4i) = (3-4i)/25</li>
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
     * Defined as: a ÷ b = a × b⁻¹
     * </p>
     * 
     * @param dividend the dividend (numerator)
     * @param divisor  the divisor (denominator, must be non-zero)
     * @return dividend ÷ divisor
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
     * <li>char(ℚ) = char(ℝ) = char(ℂ) = 0</li>
     * <li>char(𝔽ₚ) = p (for prime p)</li>
     * </ul>
     * </p>
     * 
     * @return the characteristic (0 for infinite fields, p for finite fields)
     */
    int characteristic();
}
