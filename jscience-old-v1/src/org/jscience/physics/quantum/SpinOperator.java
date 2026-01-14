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

import org.jscience.mathematics.algebraic.algebras.LieAlgebra;
import org.jscience.mathematics.algebraic.algebras.su2Dim2;
import org.jscience.mathematics.algebraic.algebras.su2Dim3;
import org.jscience.mathematics.algebraic.matrices.AbstractComplexSquareMatrix;
import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The SpinOperator class provides an object for encapsulating spin
 * operators.
 *
 * @author Mark Hale
 * @version 2.0
 */
public final class SpinOperator extends Operator {
    /** DOCUMENT ME! */
    private static final LieAlgebra spin1_2 = su2Dim2.getInstance();

    /** DOCUMENT ME! */
    private static final LieAlgebra spin1 = su2Dim3.getInstance();

    /** Spin 1/2 operator (x). */
    public static final SpinOperator X1_2 = new SpinOperator(spin1_2.basis()[0]);

    /** Spin 1/2 operator (y). */
    public static final SpinOperator Y1_2 = new SpinOperator(spin1_2.basis()[1]);

    /** Spin 1/2 operator (z). */
    public static final SpinOperator Z1_2 = new SpinOperator(spin1_2.basis()[2]);

    /** Spin 1 operator (x). */
    public static final SpinOperator X1 = new SpinOperator(spin1.basis()[0]);

    /** Spin 1 operator (y). */
    public static final SpinOperator Y1 = new SpinOperator(spin1.basis()[1]);

    /** Spin 1 operator (z). */
    public static final SpinOperator Z1 = new SpinOperator(spin1.basis()[2]);

/**
     * Constructs a spin operator.
     *
     * @param spinMatrix DOCUMENT ME!
     */
    private SpinOperator(AbstractComplexSquareMatrix spinMatrix) {
        super(spinMatrix);
    }

    /**
     * Returns true if this operator is self-adjoint.
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelfAdjoint() {
        return true;
    }

    /**
     * Returns true if this operator is unitary.
     *
     * @return DOCUMENT ME!
     */
    public boolean isUnitary() {
        return true;
    }

    /**
     * Returns the trace.
     *
     * @return DOCUMENT ME!
     */
    public Complex trace() {
        return Complex.ZERO;
    }
}
