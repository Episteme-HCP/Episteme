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

import org.jscience.mathematics.structures.groups.AbelianGroup;

/**
 * A ring is an abelian group with a second binary operation (multiplication).
 * <p>
 * Rings are fundamental algebraic structures that generalize arithmetic.
 * They appear throughout mathematics, from number theory to algebraic geometry.
 * </p>
 *
 * <h2>Mathematical Definition</h2>
 * <p>
 * A ring (R, +, ×) consists of:
 * <ol>
 * <li>A set R</li>
 * <li>An addition operation + making (R, +) an abelian group</li>
 * <li>A multiplication operation × satisfying:
 * <ul>
 * <li><strong>Closure</strong>: ∀ a, b ∈ R: a × b ∈ R</li>
 * <li><strong>Associativity</strong>: (a × b) × c = a × (b × c)</li>
 * <li><strong>Distributivity</strong>: a × (b + c) = (a × b) + (a × c) and (a +
 * b) × c = (a × c) + (b × c)</li>
 * </ul>
 * </li>
 * </ol>
 * </p>
 *
 * <h2>Examples</h2>
 * <ul>
 * <li>(ℤ, +, ×) - Integers (commutative ring with unity)</li>
 * <li>(ℝ, +, ×) - Real numbers (field, thus also a ring)</li>
 * <li>(M₂(ℝ), +, ×) - 2×2 real matrices (non-commutative ring)</li>
 * <li>(ℤ/nℤ, +, ×) - Integers modulo n</li>
 * <li>(ℝ[x], +, ×) - Polynomials with real coefficients</li>
 * </ul>
 *
 * <h2>Special Types of Rings</h2>
 * <ul>
 * <li><strong>Commutative Ring</strong>: Multiplication commutes (a × b = b ×
 * a)</li>
 * <li><strong>Ring with Unity</strong>: Has multiplicative identity (1)</li>
 * <li><strong>Integral Domain</strong>: Commutative ring with unity, no zero
 * divisors</li>
 * <li><strong>Division Ring</strong>: Every non-zero element has multiplicative
 * inverse</li>
 * <li><strong>Field</strong>: Commutative division ring</li>
 * </ul>
 *
 * <h2>Usage</h2>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Ring<E> extends AbelianGroup<E>, Semiring<E> {

    /**
     * Tests whether this ring has a multiplicative identity.
     * <p>
     * Since this interface extends Semiring, it is generally expected to have
     * unity.
     * However, some definitions of Ring do not require it (Rng).
     * </p>
     * 
     * @return {@code true} if this ring has unity (element 1)
     * 
     * @see #one()
     */
    default boolean hasUnity() {
        return true;
    }
}