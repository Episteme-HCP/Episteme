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
 * This class is used in the junit tests for the ODE integrators.
 * <p/>
 * <p>This specific problem is the following differential equation :
 * <pre>
 *    y1'' = -y1/r^3  y1 (0) = 1-e  y1' (0) = 0
 *    y2'' = -y2/r^3  y2 (0) = 0    y2' (0) =sqrt((1+e)/(1-e))
 *    r = sqrt (y1^2 + y2^2), e = 0.9
 * </pre>
 * This is a two-body problem in the plane which can be solved by
 * Kepler's equation
 * <pre>
 *   y1 (t) = ...
 * </pre>
 * </p>
 */
class TestProblem3
        extends TestProblemAbstract {

    /**
     * Eccentricity
     */
    double e;

    /**
     * theoretical state
     */
    private double[] y;

    /**
     * Simple constructor.
     *
     * @param e eccentricity
     */
    public TestProblem3(double e) {
        super();
        this.e = e;
        double[] y0 = {1 - e, 0, 0, Math.sqrt((1 + e) / (1 - e))};
        setInitialConditions(0.0, y0);
        setFinalConditions(20.0);
        double[] errorScale = {1.0, 1.0, 1.0, 1.0};
        setErrorScale(errorScale);
        y = new double[y0.length];
    }

    /**
     * Simple constructor.
     */
    public TestProblem3() {
        this(0.1);
    }

    /**
     * Copy constructor.
     *
     * @param problem problem to copy
     */
    public TestProblem3(TestProblem3 problem) {
        super(problem);
        e = problem.e;
        y = new double[problem.y.length];
        System.arraycopy(problem.y, 0, y, 0, problem.y.length);
    }

    /**
     * Clone operation.
     *
     * @return a copy of the instance
     */
    public Object clone() {
        return new TestProblem3(this);
    }

    public void doComputeDerivatives(double t, double[] y, double[] yDot) {

        // current radius
        double r2 = y[0] * y[0] + y[1] * y[1];
        double invR3 = 1 / (r2 * Math.sqrt(r2));

        // compute the derivatives
        yDot[0] = y[2];
        yDot[1] = y[3];
        yDot[2] = -invR3 * y[0];
        yDot[3] = -invR3 * y[1];

    }

    public double[] computeTheoreticalState(double t) {

        // solve Kepler's equation
        double E = t;
        double d = 0;
        double corr = 0;
        do {
            double f2 = e * Math.sin(E);
            double f0 = d - f2;
            double f1 = 1 - e * Math.cos(E);
            double f12 = f1 + f1;
            corr = f0 * f12 / (f1 * f12 - f0 * f2);
            d -= corr;
            E = t + d;
        } while (Math.abs(corr) > 1.0e-12);

        double cosE = Math.cos(E);
        double sinE = Math.sin(E);

        y[0] = cosE - e;
        y[1] = Math.sqrt(1 - e * e) * sinE;
        y[2] = -sinE / (1 - e * cosE);
        y[3] = Math.sqrt(1 - e * e) * cosE / (1 - e * cosE);

        return y;
    }

}
