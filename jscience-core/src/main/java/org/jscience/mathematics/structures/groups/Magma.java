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
package org.jscience.mathematics.structures.groups;

import org.jscience.mathematics.structures.sets.Set;

/**
 * A magma is a set equipped with a binary operation.
 * <p>
 * This is the simplest algebraic structure with an operation. The operation
 * is not required to be associative, commutative, or have an identity element.
 * </p>
 * 
 * <h2>Mathematical Definition</h2>
 * <p>
 * A magma (S, ∗) consists of:
 * <ul>
 * <li>A set S</li>
 * <li>A binary operation ∗ : S × S → S</li>
 * </ul>
 * The only requirement is closure: for all a, b ∈ S, a ∗ b ∈ S.
 * </p>
 * 
 * <h2>Examples</h2>
 * <ul>
 * <li>(ℕ, −) - Natural numbers with subtraction (not a semigroup, not
 * associative)</li>
 * <li>(ℤ, ÷) - Integers with division (not closed, only a partial magma)</li>
 * <li>(ℝ, +) - Real numbers with addition (actually a group)</li>
 * </ul>
 * 
 * <h2>Hierarchy</h2>
 * 
 * <pre>
 * Magma (closure)
 *   ↓
 * Semigroup (+ associativity)
 *   ↓
 * Monoid (+ identity)
 *   ↓
 * Group (+ inverse)
 * </pre>
 * 
 * <h2>Usage</h2>
 * 
 * <pre>{@code
 * // Simple magma - natural number subtraction
 * Magma<Integer> naturalSub = new Magma<>() {
 *     public Integer operate(Integer a, Integer b) {
 *         int result = a - b;
 *         return result >= 0 ? result : 0; // Keep in ℕ
 *     }
 * };
 * 
 * Integer result = naturalSub.operate(5, 3); // 2
 * Integer result2 = naturalSub.operate(3, 5); // 0 (forced closure)
 * }</pre>
 * 
 * @param <E> the type of elements in this magma
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Magma<E> extends Set<E> {

    /**
     * Performs the binary operation on two elements.
     * <p>
     * This is the fundamental operation of a magma. The result must be
     * an element of this magma (closure property).
     * </p>
     * <p>
     * <strong>Properties</strong>: None required (not necessarily associative or
     * commutative)
     * </p>
     * 
     * @param a the first operand
     * @param b the second operand
     * @return the result of a ∗ b
     * @throws NullPointerException     if either operand is null
     * @throws IllegalArgumentException if either operand is not in this magma
     * 
     * @see #isAssociative()
     * @see #isCommutative()
     */
    E operate(E a, E b);

    /**
     * Tests whether this magma's operation is associative.
     * <p>
     * An operation ∗ is associative if for all a, b, c:
     * <br>
     * (a ∗ b) ∗ c = a ∗ (b ∗ c)
     * </p>
     * <p>
     * If this returns {@code true}, this magma is actually a {@link Semigroup}.
     * </p>
     * 
     * @return {@code true} if the operation is associative
     * 
     * @see Semigroup
     */
    default boolean isAssociative() {
        return false; // Default: not required for general magma
    }

    /**
     * Tests whether this magma's operation is commutative.
     * <p>
     * An operation ∗ is commutative if for all a, b:
     * <br>
     * a ∗ b = b ∗ a
     * </p>
     * 
     * @return {@code true} if the operation is commutative
     */
    default boolean isCommutative() {
        return false; // Default: not required
    }
}