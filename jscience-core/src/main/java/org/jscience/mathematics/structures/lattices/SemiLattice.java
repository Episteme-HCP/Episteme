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

package org.jscience.mathematics.structures.lattices;

import org.jscience.mathematics.structures.groups.Magma;

/**
 * A set equipped with a commutative, associative and idempotent binary
 * operation.
 * <p>
 * This structure represents half of a lattice. The operation is typically
 * called "join" (Ã¢Ë†Â¨) or "meet" (Ã¢Ë†Â§).
 * </p>
 *
 * <h2>Mathematical Definition</h2>
 * <p>
 * A semilattice (S, Ã¢â‚¬Â¢) satisfies:
 * <ul>
 * <li><strong>Associativity</strong>: (a Ã¢â‚¬Â¢ b) Ã¢â‚¬Â¢ c = a Ã¢â‚¬Â¢ (b Ã¢â‚¬Â¢ c)</li>
 * <li><strong>Commutativity</strong>: a Ã¢â‚¬Â¢ b = b Ã¢â‚¬Â¢ a</li>
 * <li><strong>Idempotency</strong>: a Ã¢â‚¬Â¢ a = a</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface SemiLattice<E> extends Magma<E> {

    /**
     * The binary operation (join or meet).
     */
    E operate(E a, E b);

    default boolean isAssociative() {
        return true;
    }

    default boolean isCommutative() {
        return true;
    }

    /**
     * Tests for idempotency (a Ã¢â‚¬Â¢ a = a).
     * 
     * @return always true for semilattices
     */
    default boolean isIdempotent() {
        return true;
    }
}

