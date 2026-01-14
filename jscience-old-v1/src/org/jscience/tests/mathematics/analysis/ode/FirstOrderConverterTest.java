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

package org.jscience.tests.mathematics.analysis.ode;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class FirstOrderConverterTest extends TestCase {
/**
     * Creates a new FirstOrderConverterTest object.
     *
     * @param name DOCUMENT ME!
     */
    public FirstOrderConverterTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void testDoubleDimension() {
        for (int i = 1; i < 10; ++i) {
            SecondOrderDifferentialEquations eqn2 = new Equations(i, 0.2);
            FirstOrderConverter eqn1 = new FirstOrderConverter(eqn2);
            assertTrue(eqn1.getDimension() == (2 * eqn2.getDimension()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testDecreasingSteps()
        throws DerivativeException, IntegratorException {
        double previousError = Double.NaN;

        for (int i = 0; i < 10; ++i) {
            double step = Math.pow(2.0, -(i + 1));
            double error = integrateWithSpecifiedStep(4.0, 0.0, 1.0, step) -
                Math.sin(4.0);

            if (i > 0) {
                assertTrue(Math.abs(error) < Math.abs(previousError));
            }

            previousError = error;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testSmallStep() throws DerivativeException, IntegratorException {
        double error = integrateWithSpecifiedStep(4.0, 0.0, 1.0, 1.0e-4) -
            Math.sin(4.0);
        assertTrue(Math.abs(error) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testBigStep() throws DerivativeException, IntegratorException {
        double error = integrateWithSpecifiedStep(4.0, 0.0, 1.0, 0.5) -
            Math.sin(4.0);
        assertTrue(Math.abs(error) > 0.1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(FirstOrderConverterTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @param omega DOCUMENT ME!
     * @param t0 DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param step DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    private double integrateWithSpecifiedStep(double omega, double t0,
        double t, double step) throws DerivativeException, IntegratorException {
        double[] y0 = new double[2];
        y0[0] = Math.sin(omega * t0);
        y0[1] = omega * Math.cos(omega * t0);

        ClassicalRungeKuttaIntegrator i = new ClassicalRungeKuttaIntegrator(step);
        double[] y = new double[2];
        i.integrate(new FirstOrderConverter(new Equations(1, omega)), t0, y0,
            t, y);

        return y[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class Equations implements SecondOrderDifferentialEquations {
        /** DOCUMENT ME! */
        private int n;

        /** DOCUMENT ME! */
        private double omega2;

/**
         * Creates a new Equations object.
         *
         * @param n     DOCUMENT ME!
         * @param omega DOCUMENT ME!
         */
        public Equations(int n, double omega) {
            this.n = n;
            omega2 = omega * omega;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getDimension() {
            return n;
        }

        /**
         * DOCUMENT ME!
         *
         * @param t DOCUMENT ME!
         * @param y DOCUMENT ME!
         * @param yDot DOCUMENT ME!
         * @param yDDot DOCUMENT ME!
         */
        public void computeSecondDerivatives(double t, double[] y,
            double[] yDot, double[] yDDot) {
            for (int i = 0; i < n; ++i) {
                yDDot[i] = -omega2 * y[i];
            }
        }
    }
}
