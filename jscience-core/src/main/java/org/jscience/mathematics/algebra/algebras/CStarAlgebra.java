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

package org.jscience.mathematics.algebra.algebras;

/**
 * Represents a C*-algebra (C-star algebra).
 * <p>
 * A C*-algebra is a Banach algebra with an involution * satisfying:
 * ||x*x|| = ||x||Ã‚Â²
 * </p>
 * <p>
 * C*-algebras are fundamental in quantum mechanics and functional analysis.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface CStarAlgebra<E, F> extends BanachAlgebra<E, F> {

    /**
     * Returns the involution (adjoint) of an element.
     * For matrices, this is the conjugate transpose.
     */
    E involution(E element);

    /**
     * Checks if an element is self-adjoint (Hermitian): x* = x
     */
    default boolean isSelfAdjoint(E element) {
        return element.equals(involution(element));
    }

    /**
     * Checks if an element is normal: x*x = xx*
     */
    default boolean isNormal(E element) {
        E xstar = involution(element);
        return multiply(xstar, element).equals(multiply(element, xstar));
    }

    /**
     * Checks if an element is unitary: x*x = xx* = 1
     */
    default boolean isUnitary(E element) {
        E xstar = involution(element);
        E id = identity();
        return multiply(xstar, element).equals(id) && multiply(element, xstar).equals(id);
    }

    /**
     * Checks if an element is positive: x = y*y for some y
     */
    boolean isPositive(E element);

    /**
     * Returns the spectral radius of an element.
     */
    double spectralRadius(E element);
}


