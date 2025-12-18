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

import org.jscience.mathematics.structures.groups.AbelianMonoid;

/**
 * A semiring (or rig) is an algebraic structure similar to a ring, but without
 * the requirement that each element must have an additive inverse.
 * <p>
 * A semiring (R, +, ×) consists of:
 * <ul>
 * <li>(R, +) is a commutative monoid with identity element 0.</li>
 * <li>(R, ×) is a monoid with identity element 1.</li>
 * <li>Multiplication distributes over addition.</li>
 * <li>Multiplication by 0 annihilates R: 0 × a = a × 0 = 0.</li>
 * </ul>
 * </p>
 * <p>
 * The term "rig" is a pun on "ring": a ring without "n" (negative).
 * </p>
 * 
 * <h2>Examples</h2>
 * <ul>
 * <li>(ℕ, +, ×) - Natural numbers (including 0)</li>
 * <li>(Boolean Algebra, ∨, ∧) - Booleans with OR as addition, AND as
 * multiplication</li>
 * <li>(Languages, ∪, ·) - Formal languages with union and concatenation</li>
 * </ul>
 * 
 * @param <E> the type of elements in this semiring
 * 
 * @see Ring
 * @see AbelianMonoid
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Semiring<E> extends AbelianMonoid<E> {

    /**
     * Returns the product of two elements.
     * <p>
     * Multiplication must be associative and distribute over addition.
     * </p>
     * 
     * @param a the first factor
     * @param b the second factor
     * @return a × b
     */
    E multiply(E a, E b);

    /**
     * Returns the multiplicative identity (one element).
     * <p>
     * Satisfies: 1 × a = a × 1 = a for all elements a.
     * </p>
     * 
     * @return the multiplicative identity
     */
    E one();

    /**
     * Tests whether multiplication is commutative in this semiring.
     * 
     * @return {@code true} if multiplication commutes
     */
    boolean isMultiplicationCommutative();

    /**
     * Returns the nth power of an element.
     * 
     * @param element the base
     * @param n       the exponent (must be non-negative)
     * @return element^n
     */
    default E pow(E element, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Negative exponents require Field (or Division Ring)");
        }
        if (n == 0) {
            return one();
        }

        E result = element;
        for (int i = 1; i < n; i++) {
            result = multiply(result, element);
        }
        return result;
    }
}
