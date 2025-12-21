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
package org.jscience.mathematics.structures.lattices;

import org.jscience.mathematics.structures.sets.Set;

/**
 * A lattice is a set equipped with two commutative, associative and idempotent
 * binary operations: join (∨) and meet (∧), connected by absorption laws.
 * 
 * <h2>Mathematical Definition</h2>
 * <p>
 * A lattice (L, ∨, ∧) satisfies:
 * <ul>
 * <li>(L, ∨) is a semilattice (Join)</li>
 * <li>(L, ∧) is a semilattice (Meet)</li>
 * <li><strong>Absorption Laws</strong>:
 * <ul>
 * <li>a ∨ (a ∧ b) = a</li>
 * <li>a ∧ (a ∨ b) = a</li>
 * </ul>
 * </li>
 * </ul>
 * </p>
 * 
 * <h2>Examples</h2>
 * <ul>
 * <li>Boolean Algebra ({0,1}, OR, AND)</li>
 * <li>Power Set (P(S), ∪, ∩)</li>
 * <li>Natural Numbers (ℕ, max, min)</li>
 * <li>Divisibility (ℕ, lcm, gcd)</li>
 * </ul>
 * 
 * @param <E> the type of elements
 * 
 * @see BooleanAlgebra * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Lattice<E> extends Set<E> {

    /**
     * The join operation (∨, least upper bound).
     * 
     * @param a first operand
     * @param b second operand
     * @return a ∨ b
     */
    E join(E a, E b);

    /**
     * The meet operation (∧, greatest lower bound).
     * 
     * @param a first operand
     * @param b second operand
     * @return a ∧ b
     */
    E meet(E a, E b);
}