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
 *    y' = t^3 - t y
 * </pre>
 * with the initial condition y (0) = 0. The solution of this equation
 * is the following function :
 * <pre>
 *   y (t) = t^2 + 2 (ext (- t^2 / 2) - 1)
 * </pre>
 * </p>
 */
class TestProblem2
        extends TestProblemAbstract {

    /**
     * theoretical state
     */
    private double[] y;

    /**
     * Simple constructor.
     */
    public TestProblem2() {
        super();
        double[] y0 = {0.0};
        setInitialConditions(0.0, y0);
        setFinalConditions(1.0);
        double[] errorScale = {1.0};
        setErrorScale(errorScale);
        y = new double[y0.length];
    }

    /**
     * Copy constructor.
     *
     * @param problem problem to copy
     */
    public TestProblem2(TestProblem2 problem) {
        super(problem);
        y = new double[problem.y.length];
        System.arraycopy(problem.y, 0, y, 0, problem.y.length);
    }

    /**
     * Clone operation.
     *
     * @return a copy of the instance
     */
    public Object clone() {
        return new TestProblem2(this);
    }

    public void doComputeDerivatives(double t, double[] y, double[] yDot) {

        // compute the derivatives
        for (int i = 0; i < n; ++i)
            yDot[i] = t * (t * t - y[i]);

    }

    public double[] computeTheoreticalState(double t) {
        double t2 = t * t;
        double c = t2 + 2 * (Math.exp(-0.5 * t2) - 1);
        for (int i = 0; i < n; ++i) {
            y[i] = c;
        }
        return y;
    }

}
