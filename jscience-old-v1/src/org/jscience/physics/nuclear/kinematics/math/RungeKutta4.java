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

package org.jscience.physics.nuclear.kinematics.math;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class RungeKutta4 {
    /**
     * DOCUMENT ME!
     */
    private double[] y;

    /**
     * DOCUMENT ME!
     */
    private double[] dydx;

    /**
     * DOCUMENT ME!
     */
    private double x;

    /**
     * DOCUMENT ME!
     */
    private double h;

    /**
     * DOCUMENT ME!
     */
    private DiffEquations derivs;

    /**
     * Creates a new RungeKutta4 object.
     *
     * @param de DOCUMENT ME!
     */
    public RungeKutta4(DiffEquations de) {
        derivs = de;
    }

    /**
     * See numerical recipes Section 16.1
     *
     * @param evaluateAt DOCUMENT ME!
     * @param initialValues DOCUMENT ME!
     * @param interval DOCUMENT ME!
     */
    public void setVariables(double evaluateAt, double[] initialValues,
        double interval) /*throws Exception*/ {
        //if (initialValues.length != dydx.length) {
        //throw new Exception("Dimensions don't match!");
        //}
        y = initialValues;
        x = evaluateAt;
        //dydx=initialDerivs;
        dydx = derivs.dydx(x, y);
        h = interval;
    }

    /**
     * Almost verbatim routine rk4 in Numerical Recipes.
     *
     * @return DOCUMENT ME!
     */
    public double[] step() {
        double[] dym = new double[y.length];
        double[] dyt = new double[y.length];
        double[] yt = new double[y.length];
        double[] yout = new double[y.length];
        double hh = h * 0.5;
        double h6 = h / 6.0;
        double xh = x + hh;

        for (int i = 0; i < y.length; i++)
            yt[i] = y[i] + (hh * dydx[i]);

        dyt = derivs.dydx(xh, yt);

        for (int i = 0; i < y.length; i++)
            yt[i] = y[i] + (hh * dyt[i]);

        dym = derivs.dydx(xh, yt);

        for (int i = 0; i < y.length; i++) {
            yt[i] = y[i] + (h * dym[i]);
            dym[i] += dyt[i];
        }

        dyt = derivs.dydx(x + h, yt);

        for (int i = 0; i < y.length; i++)
            yout[i] = y[i] + (h6 * (dydx[i] + dyt[i] + (2.0 * dym[i])));

        return yout;
    }

    /**
     * DOCUMENT ME!
     *
     * @param start DOCUMENT ME!
     * @param end DOCUMENT ME!
     * @param initValues DOCUMENT ME!
     * @param numberOfSteps DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] dumbIntegral(double start, double end, double[] initValues,
        int numberOfSteps) {
        /*System.out.println("dumbIntegral("+start+", "+end+", "+initValues[0]+", "+numberOfSteps
        +")");*/
        setVariables(start, initValues, (end - start) / numberOfSteps);

        for (int i = 0; i < numberOfSteps; i++) {
            //System.out.println("h: "+h+", x: "+x+", thickness: "+y[0]);
            y = step();
            x += h;
            dydx = derivs.dydx(x, y);
        }

        return y;
    }
}
