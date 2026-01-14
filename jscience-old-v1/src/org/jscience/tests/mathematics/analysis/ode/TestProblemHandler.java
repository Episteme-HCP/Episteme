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

/**
 * This class is used to handle steps for the test problems integrated
 * during the junit tests for the ODE integrators.
 */
class TestProblemHandler implements StepHandler {
    /** Associated problem. */
    private TestProblemAbstract problem;

    /** Maximal error encountered during the integration. */
    private double maxError;

    /** Error at the end of the integration. */
    private double lastError;

    /** Time at the end of integration. */
    private double lastTime;

/**
     * Simple constructor.
     *
     * @param problem problem for which steps should be handled
     */
    public TestProblemHandler(TestProblemAbstract problem) {
        this.problem = problem;
        reset();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean requiresDenseOutput() {
        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        maxError = 0;
        lastError = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param interpolator DOCUMENT ME!
     * @param isLast DOCUMENT ME!
     *
     * @throws DerivativeException DOCUMENT ME!
     */
    public void handleStep(StepInterpolator interpolator, boolean isLast)
        throws DerivativeException {
        double pT = interpolator.getPreviousTime();
        double cT = interpolator.getCurrentTime();
        double[] errorScale = problem.getErrorScale();

        // store the error at the last step
        if (isLast) {
            double[] interpolatedY = interpolator.getInterpolatedState();
            double[] theoreticalY = problem.computeTheoreticalState(cT);

            for (int i = 0; i < interpolatedY.length; ++i) {
                double error = Math.abs(interpolatedY[i] - theoreticalY[i]);

                if (error > lastError) {
                    lastError = error;
                }
            }

            lastTime = cT;
        }

        // walk through the step
        for (int k = 0; k <= 20; ++k) {
            double time = pT + ((k * (cT - pT)) / 20);
            interpolator.setInterpolatedTime(time);

            double[] interpolatedY = interpolator.getInterpolatedState();
            double[] theoreticalY = problem.computeTheoreticalState(interpolator.getInterpolatedTime());

            // update the errors
            for (int i = 0; i < interpolatedY.length; ++i) {
                double error = errorScale[i] * Math.abs(interpolatedY[i] -
                        theoreticalY[i]);

                if (error > maxError) {
                    maxError = error;
                }
            }
        }
    }

    /**
     * Get the maximal error encountered during integration.
     *
     * @return maximal error
     */
    public double getMaximalError() {
        return maxError;
    }

    /**
     * Get the error at the end of the integration.
     *
     * @return error at the end of the integration
     */
    public double getLastError() {
        return lastError;
    }

    /**
     * Get the time at the end of the integration.
     *
     * @return time at the end of the integration.
     */
    public double getLastTime() {
        return lastTime;
    }
}
