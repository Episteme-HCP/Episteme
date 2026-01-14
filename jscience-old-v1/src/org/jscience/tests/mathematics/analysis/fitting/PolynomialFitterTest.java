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

package org.jscience.tests.mathematics.analysis.fitting;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jscience.tests.mathematics.analysis.estimation.EstimationException;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class PolynomialFitterTest extends TestCase {
/**
     * Creates a new PolynomialFitterTest object.
     *
     * @param name DOCUMENT ME!
     */
    public PolynomialFitterTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EstimationException DOCUMENT ME!
     */
    public void testNoError() throws EstimationException {
        Random randomizer = new Random(64925784252L);

        for (int degree = 0; degree < 10; ++degree) {
            Polynom p = new Polynom(degree);

            for (int i = 0; i <= degree; ++i) {
                p.initCoeff(i, randomizer.nextGaussian());
            }

            PolynomialFitter fitter = new PolynomialFitter(degree, 10, 1.0e-7,
                    1.0e-10, 1.0e-10);

            for (int i = 0; i <= degree; ++i) {
                fitter.addWeightedPair(1.0, (double) i, p.valueAt((double) i));
            }

            Polynom fitted = new Polynom(fitter.fit());

            for (double x = -1.0; x < 1.0; x += 0.01) {
                double error = Math.abs(p.valueAt(x) - fitted.valueAt(x)) / (1.0 +
                    Math.abs(p.valueAt(x)));
                assertTrue(Math.abs(error) < 1.0e-5);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EstimationException DOCUMENT ME!
     */
    public void testSmallError() throws EstimationException {
        Random randomizer = new Random(53882150042L);

        for (int degree = 0; degree < 10; ++degree) {
            Polynom p = new Polynom(degree);

            for (int i = 0; i <= degree; ++i) {
                p.initCoeff(i, randomizer.nextGaussian());
            }

            PolynomialFitter fitter = new PolynomialFitter(degree, 10, 1.0e-7,
                    1.0e-10, 1.0e-10);

            for (double x = -1.0; x < 1.0; x += 0.01) {
                fitter.addWeightedPair(1.0, x,
                    p.valueAt(x) + (0.1 * randomizer.nextGaussian()));
            }

            Polynom fitted = new Polynom(fitter.fit());

            for (double x = -1.0; x < 1.0; x += 0.01) {
                double error = Math.abs(p.valueAt(x) - fitted.valueAt(x)) / (1.0 +
                    Math.abs(p.valueAt(x)));
                assertTrue(Math.abs(error) < 0.1);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EstimationException DOCUMENT ME!
     */
    public void testUnsolvableProblem() throws EstimationException {
        Random randomizer = new Random(1248788532L);

        for (int degree = 0; degree < 10; ++degree) {
            Polynom p = new Polynom(degree);

            for (int i = 1; i <= degree; ++i) {
                p.initCoeff(i, randomizer.nextGaussian());
            }

            PolynomialFitter fitter = new PolynomialFitter(degree, 10, 1.0e-7,
                    1.0e-10, 1.0e-10);

            // reusing the same point over and over again does not bring
            // information, the problem cannot be solved in this case for
            // degrees greater than 1 (but one point is sufficient for
            // degree 0)
            for (double x = -1.0; x < 1.0; x += 0.01) {
                fitter.addWeightedPair(1.0, 0.0, p.valueAt(0.0));
            }

            boolean gotIt = false;

            try {
                fitter.fit();
            } catch (EstimationException e) {
                gotIt = true;
            }

            assertTrue(((degree == 0) && !gotIt) || ((degree > 0) && gotIt));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(PolynomialFitterTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class Polynom {
        /** DOCUMENT ME! */
        private double[] coeffs;

/**
         * Creates a new Polynom object.
         *
         * @param degree DOCUMENT ME!
         */
        public Polynom(int degree) {
            coeffs = new double[degree + 1];

            for (int i = 0; i < coeffs.length; ++i) {
                coeffs[i] = 0.0;
            }
        }

/**
         * Creates a new Polynom object.
         *
         * @param coeffs DOCUMENT ME!
         */
        public Polynom(double[] coeffs) {
            this.coeffs = coeffs;
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         * @param c DOCUMENT ME!
         */
        public void initCoeff(int i, double c) {
            coeffs[i] = c;
        }

        /**
         * DOCUMENT ME!
         *
         * @param x DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double valueAt(double x) {
            double y = coeffs[coeffs.length - 1];

            for (int i = coeffs.length - 2; i >= 0; --i) {
                y = (y * x) + coeffs[i];
            }

            return y;
        }
    }
}
