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
