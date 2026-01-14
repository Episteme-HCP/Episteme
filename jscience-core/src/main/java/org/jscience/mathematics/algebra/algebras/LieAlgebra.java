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

package org.jscience.mathematics.algebra.algebras;

import org.jscience.mathematics.algebra.Algebra;

/**
 * Represents a Lie Algebra.
 * <p>
 * A Lie Algebra is an algebra where the product (Lie bracket) satisfies:
 * <ul>
 * <li>Bilinearity</li>
 * <li>Alternativity: [x, x] = 0</li>
 * <li>Jacobi Identity: [x, [y, z]] + [y, [z, x]] + [z, [x, y]] = 0</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface LieAlgebra<E, S> extends Algebra<E, S> {

    /**
     * Returns the Lie bracket [a, b].
     * <p>
     * This corresponds to the {@link #multiply(Object, Object)} method of the
     * Algebra interface.
     * </p>
     * 
     * @param a the first element
     * @param b the second element
     * @return [a, b]
     */
    default E bracket(E a, E b) {
        return multiply(a, b);
    }

    // Note: Lie Algebras are generally non-associative, so they are not Rings in
    // the strict sense
    // if Ring implies associativity. However, in JScience Ring interface might not
    // strictly enforce associativity
    // or we accept it as a "Non-associative Ring" (Algebra).
    // The Ring interface usually implies (R, +, *) is a ring.
    // For Lie Algebra, * is the bracket.
}


