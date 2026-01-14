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
public class StepNormalizerTest extends TestCase {
    /** DOCUMENT ME! */
    TestProblem3 pb;

    /** DOCUMENT ME! */
    FirstOrderIntegrator integ;

    /** DOCUMENT ME! */
    boolean lastSeen;

/**
     * Creates a new StepNormalizerTest object.
     *
     * @param name DOCUMENT ME!
     */
    public StepNormalizerTest(String name) {
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
        double range = pb.getFinalTime() - pb.getInitialTime();
        setLastSeen(false);
        integ.setStepHandler(new StepNormalizer(range / 10.0,
                new FixedStepHandler() {
                private boolean firstCall = true;

                public void handleStep(double t, double[] y, boolean isLast) {
                    if (firstCall) {
                        checkValue(t, pb.getInitialTime());
                        firstCall = false;
                    }

                    if (isLast) {
                        setLastSeen(true);
                        checkValue(t, pb.getFinalTime());
                    }
                }
            }));
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);
        assertTrue(lastSeen);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     * @throws IntegratorException DOCUMENT ME!
     */
    public void testBeforeEnd() throws DerivativeException, IntegratorException {
        final double range = pb.getFinalTime() - pb.getInitialTime();
        setLastSeen(false);
        integ.setStepHandler(new StepNormalizer(range / 10.5,
                new FixedStepHandler() {
                public void handleStep(double t, double[] y, boolean isLast) {
                    if (isLast) {
                        setLastSeen(true);
                        checkValue(t, pb.getFinalTime() - (range / 21.0));
                    }
                }
            }));
        integ.integrate(pb, pb.getInitialTime(), pb.getInitialState(),
            pb.getFinalTime(), new double[pb.getDimension()]);
        assertTrue(lastSeen);
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
     * @param lastSeen DOCUMENT ME!
     */
    public void setLastSeen(boolean lastSeen) {
        this.lastSeen = lastSeen;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(StepNormalizerTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    public void setUp() {
        pb = new TestProblem3(0.9);

        double minStep = 0;
        double maxStep = pb.getFinalTime() - pb.getInitialTime();
        integ = new DormandPrince54Integrator(minStep, maxStep, 10.e-8, 1.0e-8);
        lastSeen = false;
    }

    /**
     * DOCUMENT ME!
     */
    public void tearDown() {
        pb = null;
        integ = null;
    }
}
