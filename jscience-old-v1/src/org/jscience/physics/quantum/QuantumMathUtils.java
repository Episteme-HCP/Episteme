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

package org.jscience.physics.quantum;

import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The Quantum math library. This class cannot be subclassed or
 * instantiated because all methods are static.
 *
 * @author Mark Hale
 * @version 1.5
 */
public final class QuantumMathUtils extends Object {
/**
     * Creates a new QuantumMathUtils object.
     */
    private QuantumMathUtils() {
    }

    // COMMUTATOR
    /**
     * Returns the commutator [A,B].
     *
     * @param A an operator
     * @param B an operator
     *
     * @return DOCUMENT ME!
     */
    public static Operator commutator(final Operator A, final Operator B) {
        return (A.multiply(B)).subtract(B.multiply(A));
    }

    // ANTICOMMUTATOR
    /**
     * Returns the anticommutator {A,B}.
     *
     * @param A an operator
     * @param B an operator
     *
     * @return DOCUMENT ME!
     */
    public static Operator anticommutator(final Operator A, final Operator B) {
        return (A.multiply(B)).add(B.multiply(A));
    }

    // EXPECTATION VALUES
    /**
     * Returns the expectation value.
     *
     * @param op an operator
     * @param ket a ket vector
     *
     * @return DOCUMENT ME!
     */
    public static Complex expectation(final Operator op, final KetVector ket) {
        return ket.toBraVector().multiply(op).multiply(ket);
    }

    /**
     * Returns the expectation value.
     *
     * @param dm a density matrix
     * @param op an operator
     *
     * @return DOCUMENT ME!
     */
    public static Complex expectation(final DensityMatrix dm, final Operator op) {
        return dm.multiply(op).trace();
    }

    // PROBABILITIES
    /**
     * Returns the probability.
     *
     * @param p a projector
     * @param ket a ket vector
     *
     * @return DOCUMENT ME!
     */
    public static Complex probability(final Projector p, final KetVector ket) {
        return ket.toBraVector().multiply(p).multiply(ket);
    }

    /**
     * Returns the probability.
     *
     * @param dm a density matrix
     * @param p a projector
     *
     * @return DOCUMENT ME!
     */
    public static Complex probability(final DensityMatrix dm, final Projector p) {
        return dm.multiply(p).trace();
    }
}
