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

package org.jscience.mathematics.structures.sets;

/**
 * Represents a set with an infinite number of elements.
 * <p>
 * Infinite sets cannot be iterated over completely and do not have a finite
 * size.
 * Examples include the set of natural numbers ℕ, real numbers ℝ, etc.
 * </p>
 * 
 * @param <E> the type of elements in this set
 * 
 * @see Set
 * @see FiniteSet
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface InfiniteSet<E> extends Set<E> {

    /**
     * Returns {@code true} if this set is countable (i.e., its elements can be put
     * in one-to-one correspondence with the natural numbers).
     * <p>
     * Examples:
     * <ul>
     * <li>Countable: ℕ (Natural numbers), ℤ (Integers), ℚ (Rational numbers)</li>
     * <li>Uncountable: ℝ (Real numbers), ℂ (Complex numbers)</li>
     * </ul>
     * </p>
     * 
     * @return {@code true} if this set is countable
     */
    boolean isCountable();
}
