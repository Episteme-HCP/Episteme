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

import org.jscience.tests.mathematics.analysis.estimation.EstimationException;
import org.jscience.tests.mathematics.analysis.fitting.PolynomialFitter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class GillIntegratorTest extends TestCase {
/**
     * Creates a new GillIntegratorTest object.
     *
     * @param name DOCUMENT ME!
     */
    public GillIntegratorTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void testDimensionCheck() {
        try {
            TestProblem1 pb = new TestProblem1();
            new GillIntegrator(0.01).integrate(pb, 0.0,
                new double[pb.getDimension() + 10], 1.0,
                new double[pb.getDimension() + 10]);
            fail("an exception should have been thrown");
        } catch (DerivativeException de) {
            fail("wrong exception caught");
        } catch (IntegratorException ie) {
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
        TestProblemAbstract[] problems = TestProblemFactory.getProblems();

        for (int k = 0; k < problems.length; ++k) {
            double previousError = Double.NaN;

            for (int i = 5; i < 10; ++i) {
                TestProblemAbstract pb = (TestProblemAbstract) problems[k].clone();
                double step = (pb.getFinalTime() - pb.getInitialTime()) * Math.pow(2.0,
                        -i);

                FirstOrderIntegrator integ = new GillIntegrator(step);
                TestProblemHandler handler = new TestProblemHandler(pb);
                integ.setStepHandler(handler);

                SwitchingFunction[] functions = pb.getSwitchingFunctions();

                if (functions != null) {
                    for (int l = 0; l < functions.length; ++l) {
                        integ.addSwitchingFunction(functions[l],
                            Double.POSITIVE_INFINITY, 1.0e-6 * step);
                    }
                }

                integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
                    pb.getFinalTime(), new double[pb.getDimension()]);

                double error = handler.getMaximalError();

                if (i > 5) {
                    assertTrue(error < Math.abs(previousError));
                }

                previousError = error;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws EstimationException DOCUMENT ME!
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testOrder()
        throws EstimationException, DerivativeException, IntegratorException {
        PolynomialFitter fitter = new PolynomialFitter(1, 10, 1.0e-7, 1.0e-10,
                1.0e-10);

        TestProblemAbstract[] problems = TestProblemFactory.getProblems();

        for (int k = 0; k < problems.length; ++k) {
            for (int i = 0; i < 10; ++i) {
                TestProblemAbstract pb = (TestProblemAbstract) problems[k].clone();
                double step = (pb.getFinalTime() - pb.getInitialTime()) * Math.pow(2.0,
                        -(i + 1));

                FirstOrderIntegrator integ = new GillIntegrator(step);
                TestProblemHandler handler = new TestProblemHandler(pb);
                integ.setStepHandler(handler);

                SwitchingFunction[] functions = pb.getSwitchingFunctions();

                if (functions != null) {
                    for (int l = 0; l < functions.length; ++l) {
                        integ.addSwitchingFunction(functions[l],
                            Double.POSITIVE_INFINITY, 1.0e-6 * step);
                    }
                }

                integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
                    pb.getFinalTime(), new double[pb.getDimension()]);

                fitter.addWeightedPair(1.0, Math.log(Math.abs(step)),
                    Math.log(handler.getLastError()));
            }

            // this is an order 4 method
            double[] coeffs = fitter.fit();
            assertTrue(coeffs[1] > 3.2);
            assertTrue(coeffs[1] < 4.8);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testSmallStep() throws DerivativeException, IntegratorException {
        TestProblem1 pb = new TestProblem1();
        double step = (pb.getFinalTime() - pb.getInitialTime()) * 0.001;

        FirstOrderIntegrator integ = new GillIntegrator(step);
        TestProblemHandler handler = new TestProblemHandler(pb);
        integ.setStepHandler(handler);
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);

        assertTrue(handler.getLastError() < 2.0e-13);
        assertTrue(handler.getMaximalError() < 4.0e-12);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testBigStep() throws DerivativeException, IntegratorException {
        TestProblem1 pb = new TestProblem1();
        double step = (pb.getFinalTime() - pb.getInitialTime()) * 0.2;

        FirstOrderIntegrator integ = new GillIntegrator(step);
        TestProblemHandler handler = new TestProblemHandler(pb);
        integ.setStepHandler(handler);
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);

        assertTrue(handler.getLastError() > 0.0004);
        assertTrue(handler.getMaximalError() > 0.005);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testKepler() throws DerivativeException, IntegratorException {
        final TestProblem3 pb = new TestProblem3(0.9);
        double step = (pb.getFinalTime() - pb.getInitialTime()) * 0.0003;

        FirstOrderIntegrator integ = new GillIntegrator(step);
        integ.setStepHandler(new StepHandler() {
                private double maxError = 0;

                public boolean requiresDenseOutput() {
                    return false;
                }

                public void reset() {
                    maxError = 0;
                }

                public void handleStep(StepInterpolator interpolator,
                    boolean isLast) {
                    double[] interpolatedY = interpolator.getInterpolatedState();
                    double[] theoreticalY = pb.computeTheoreticalState(interpolator.getCurrentTime());
                    double dx = interpolatedY[0] - theoreticalY[0];
                    double dy = interpolatedY[1] - theoreticalY[1];
                    double error = (dx * dx) + (dy * dy);

                    if (error > maxError) {
                        maxError = error;
                    }

                    if (isLast) {
                        // even with more than 1000 evaluations per period,
                        // RK4 is not able to integrate such an eccentric
                        // orbit with a good accuracy
                        assertTrue(maxError > 0.001);
                    }
                }
            });
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(GillIntegratorTest.class);
    }
}
