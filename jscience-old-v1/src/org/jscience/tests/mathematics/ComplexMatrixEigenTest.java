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

package org.jscience.tests.mathematics;

/**
 * Testcase for complex eigenvalue/vector methods.
 *
 * @author Mark Hale
 */
public class ComplexMatrixEigenTest extends junit.framework.TestCase {
    /** DOCUMENT ME! */
    private final int N = 5;

    /** DOCUMENT ME! */
    private ComplexSquareMatrix sqmat;

    /** DOCUMENT ME! */
    private double[] eval;

    /** DOCUMENT ME! */
    private ComplexVector[] evec;

/**
     * Creates a new ComplexMatrixEigenTest object.
     *
     * @param name DOCUMENT ME!
     */
    public ComplexMatrixEigenTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        junit.textui.TestRunner.run(ComplexMatrixEigenTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setUp() {
        org.jscience.GlobalSettings.ZERO_TOL = 1.0e-6;
        sqmat = MatrixToolkit.randomComplexSquareMatrix(N);
        sqmat = (ComplexSquareMatrix) sqmat.add(sqmat.hermitianAdjoint()); // make hermitian
        eval = new double[N];
        evec = new ComplexVector[N];

        try {
            eval = LinearMath.eigenSolveHermitian(sqmat, evec);
        } catch (MaximumIterationsExceededException e) {
            fail(e.toString());
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void testEigenvectors() {
        for (int i = 0; i < N; i++)
            assertEquals(sqmat.multiply(evec[i]),
                evec[i].scalarMultiply(eval[i]));
    }

    /**
     * DOCUMENT ME!
     */
    public void testTrace() {
        double tr = 0.0;

        for (int i = 0; i < N; i++)
            tr += eval[i];

        assertEquals(sqmat.trace(), new Complex(tr, 0.0));
    }

    /**
     * DOCUMENT ME!
     */
    public void testDet() {
        double det = 1.0;

        for (int i = 0; i < N; i++)
            det *= eval[i];

        assertEquals(sqmat.det(), new Complex(det, 0.0));
    }
}
