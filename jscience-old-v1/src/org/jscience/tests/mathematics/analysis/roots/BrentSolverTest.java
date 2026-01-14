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

package org.jscience.tests.mathematics.analysis.roots;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jscience.tests.mathematics.analysis.functions.FunctionException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class BrentSolverTest extends TestCase {
/**
     * Creates a new BrentSolverTest object.
     *
     * @param name DOCUMENT ME!
     */
    public BrentSolverTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws FunctionException DOCUMENT ME!
     */
    public void testAlefeldPotraShi() throws FunctionException {
        TestProblem[] problems = TestProblem.getAPSProblems();
        BrentSolver solver = new BrentSolver();

        for (int i = 0; i < problems.length; ++i) {
            TestProblem p = problems[i];
            double tol = 1.0e-10 * Math.abs(p.getExpectedRoot());
            assertTrue(solver.findRoot(p, new Checker(tol), 1000, p.getA(),
                    p.valueAt(p.getA()), p.getB(), p.valueAt(p.getB())));
            assertTrue(p.checkResult(solver.getRoot(), tol));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(BrentSolverTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class Checker implements ConvergenceChecker {
        /** DOCUMENT ME! */
        private double tolerance;

/**
         * Creates a new Checker object.
         *
         * @param tolerance DOCUMENT ME!
         */
        public Checker(double tolerance) {
            this.tolerance = tolerance;
        }

        /**
         * DOCUMENT ME!
         *
         * @param xLow DOCUMENT ME!
         * @param fLow DOCUMENT ME!
         * @param xHigh DOCUMENT ME!
         * @param fHigh DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int converged(double xLow, double fLow, double xHigh,
            double fHigh) {
            return (Math.abs(xHigh - xLow) <= tolerance)
            ? ((Math.abs(fLow) <= Math.abs(fHigh)) ? ConvergenceChecker.LOW
                                                   : ConvergenceChecker.HIGH)
            : ConvergenceChecker.NONE;
        }
    }
}
