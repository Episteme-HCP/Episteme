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

package org.jscience.tests.mathematics.analysis.optimization;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class NelderMeadTest extends TestCase {
    /** DOCUMENT ME! */
    private int count;

/**
     * Creates a new NelderMeadTest object.
     *
     * @param name DOCUMENT ME!
     */
    public NelderMeadTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CostException DOCUMENT ME!
     * @throws NoConvergenceException DOCUMENT ME!
     */
    public void testRosenbrock() throws CostException, NoConvergenceException {
        CostFunction rosenbrock = new CostFunction() {
                public double cost(double[] x) {
                    ++count;

                    double a = x[1] - (x[0] * x[0]);
                    double b = 1.0 - x[0];

                    return (100 * a * a) + (b * b);
                }
            };

        count = 0;

        PointCostPair optimum = new NelderMead().minimizes(rosenbrock, 100,
                new ValueChecker(1.0e-3), new double[] { -1.2, 1.0 },
                new double[] { 3.5, -2.3 });

        assertTrue(count < 50);
        assertEquals(0.0, optimum.getCost(), 6.0e-4);
        assertEquals(1.0, optimum.getPoint()[0], 0.05);
        assertEquals(1.0, optimum.getPoint()[1], 0.05);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CostException DOCUMENT ME!
     * @throws NoConvergenceException DOCUMENT ME!
     */
    public void testPowell() throws CostException, NoConvergenceException {
        CostFunction powell = new CostFunction() {
                public double cost(double[] x) {
                    ++count;

                    double a = x[0] + (10 * x[1]);
                    double b = x[2] - x[3];
                    double c = x[1] - (2 * x[2]);
                    double d = x[0] - x[3];

                    return (a * a) + (5 * b * b) + (c * c * c * c) +
                    (10 * d * d * d * d);
                }
            };

        count = 0;

        PointCostPair optimum = new NelderMead().minimizes(powell, 200,
                new ValueChecker(1.0e-3), new double[] { 3.0, -1.0, 0.0, 1.0 },
                new double[] { 4.0, 0.0, 1.0, 2.0 });
        assertTrue(count < 150);
        assertEquals(0.0, optimum.getCost(), 6.0e-4);
        assertEquals(0.0, optimum.getPoint()[0], 0.07);
        assertEquals(0.0, optimum.getPoint()[1], 0.07);
        assertEquals(0.0, optimum.getPoint()[2], 0.07);
        assertEquals(0.0, optimum.getPoint()[3], 0.07);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(NelderMeadTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class ValueChecker implements ConvergenceChecker {
        /** DOCUMENT ME! */
        private double threshold;

/**
         * Creates a new ValueChecker object.
         *
         * @param threshold DOCUMENT ME!
         */
        public ValueChecker(double threshold) {
            this.threshold = threshold;
        }

        /**
         * DOCUMENT ME!
         *
         * @param simplex DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean converged(PointCostPair[] simplex) {
            PointCostPair smallest = simplex[0];
            PointCostPair largest = simplex[simplex.length - 1];

            return (largest.getCost() - smallest.getCost()) < threshold;
        }
    }
}
