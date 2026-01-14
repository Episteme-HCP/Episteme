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

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ContinuousOutputModelTest extends TestCase {
    /** DOCUMENT ME! */
    TestProblem3 pb;

    /** DOCUMENT ME! */
    FirstOrderIntegrator integ;

/**
     * Creates a new ContinuousOutputModelTest object.
     *
     * @param name DOCUMENT ME!
     */
    public ContinuousOutputModelTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testBoundaries()
        throws DerivativeException, IntegratorException {
        integ.setStepHandler(new ContinuousOutputModel());
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);

        try {
            ContinuousOutputModel cm = (ContinuousOutputModel) integ.getStepHandler();
            cm.setInterpolatedTime((2.0 * pb.getInitialTime()) -
                pb.getFinalTime());
            fail("an exception should have been thrown");
        } catch (IllegalArgumentException iae) {
        } catch (Exception e) {
            fail("wrong exception caught");
        }

        try {
            ContinuousOutputModel cm = (ContinuousOutputModel) integ.getStepHandler();
            cm.setInterpolatedTime((2.0 * pb.getFinalTime()) -
                pb.getInitialTime());
            fail("an exception should have been thrown");
        } catch (IllegalArgumentException iae) {
        } catch (Exception e) {
            fail("wrong exception caught");
        }

        ContinuousOutputModel cm = (ContinuousOutputModel) integ.getStepHandler();
        cm.setInterpolatedTime(0.5 * (pb.getFinalTime() - pb.getInitialTime()));
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testRandomAccess()
        throws DerivativeException, IntegratorException {
        ContinuousOutputModel cm = new ContinuousOutputModel();
        integ.setStepHandler(cm);
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);

        Random random = new Random(347588535632L);
        double maxError = 0.0;

        for (int i = 0; i < 1000; ++i) {
            double r = random.nextDouble();
            double time = (r * pb.getInitialTime()) +
                ((1.0 - r) * pb.getFinalTime());
            cm.setInterpolatedTime(time);

            double[] interpolatedY = cm.getInterpolatedState();
            double[] theoreticalY = pb.computeTheoreticalState(time);
            double dx = interpolatedY[0] - theoreticalY[0];
            double dy = interpolatedY[1] - theoreticalY[1];
            double error = (dx * dx) + (dy * dy);

            if (error > maxError) {
                maxError = error;
            }
        }

        assertTrue(maxError < 1.0e-9);
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param reference DOCUMENT ME!
     */
    public void checkValue(double value, double reference) {
        assertTrue(Math.abs(value - reference) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(ContinuousOutputModelTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    public void setUp() {
        pb = new TestProblem3(0.9);

        double minStep = 0;
        double maxStep = pb.getFinalTime() - pb.getInitialTime();
        integ = new DormandPrince54Integrator(minStep, maxStep, 1.0e-8, 1.0e-8);
    }

    /**
     * DOCUMENT ME!
     */
    public void tearDown() {
        pb = null;
        integ = null;
    }
}
