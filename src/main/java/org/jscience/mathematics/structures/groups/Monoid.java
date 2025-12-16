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

package org.jscience.mathematics.structures.groups;

/**
 * A monoid is a semigroup with an identity element.
 * <p>
 * A monoid (M, ∗, e) satisfies:
 * <ul>
 * <li><strong>Closure</strong>: ∀ a, b ∈ M: a ∗ b ∈ M</li>
 * <li><strong>Associativity</strong>: (a ∗ b) ∗ c = a ∗ (b ∗ c)</li>
 * <li><strong>Identity</strong>: ∃ e ∈ M: a ∗ e = e ∗ a = a</li>
 * </ul>
 * </p>
 * 
 * @param <E> the type of elements in this monoid
 * 
 * @see Magma
 * @see Group
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Monoid<E> extends Magma<E> {

    /**
     * Returns the identity element of this monoid.
     * <p>
     * Satisfies: e ∗ a = a ∗ e = a for all a.
     * </p>
     * 
     * @return the identity element
     */
    E identity();

    /**
     * Monoids are associative by definition.
     * 
     * @return always {@code true}
     */
    @Override
    default boolean isAssociative() {
        return true;
    }
}
