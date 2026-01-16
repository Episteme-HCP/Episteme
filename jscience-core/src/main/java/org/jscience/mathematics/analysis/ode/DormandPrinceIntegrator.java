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

package org.jscience.mathematics.analysis.ode;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Dormand-Prince 5(4) adaptive step-size ODE integrator.
 * <p>
 * Embedded Runge-Kutta method with error estimation for automatic step size
 * control.
 * Industry standard for general-purpose ODE solving.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DormandPrinceIntegrator {

    // Dormand-Prince 5(4) coefficients
    private static final Real[] C = {
            Real.ZERO, Real.of(1.0 / 5), Real.of(3.0 / 10), Real.of(4.0 / 5),
            Real.of(8.0 / 9), Real.ONE, Real.ONE
    };

    private static final Real[][] A = {
            {},
            { Real.of(1.0 / 5) },
            { Real.of(3.0 / 40), Real.of(9.0 / 40) },
            { Real.of(44.0 / 45), Real.of(-56.0 / 15), Real.of(32.0 / 9) },
            { Real.of(19372.0 / 6561), Real.of(-25360.0 / 2187), Real.of(64448.0 / 6561), Real.of(-212.0 / 729) },
            { Real.of(9017.0 / 3168), Real.of(-355.0 / 33), Real.of(46732.0 / 5247), Real.of(49.0 / 176),
                    Real.of(-5103.0 / 18656) },
            { Real.of(35.0 / 384), Real.ZERO, Real.of(500.0 / 1113), Real.of(125.0 / 192), Real.of(-2187.0 / 6784),
                    Real.of(11.0 / 84) }
    };

    // 5th order coefficients
    private static final Real[] B5 = {
            Real.of(35.0 / 384), Real.ZERO, Real.of(500.0 / 1113), Real.of(125.0 / 192),
            Real.of(-2187.0 / 6784), Real.of(11.0 / 84), Real.ZERO
    };

    // 4th order coefficients (for error estimation)
    private static final Real[] B4 = {
            Real.of(5179.0 / 57600), Real.ZERO, Real.of(7571.0 / 16695), Real.of(393.0 / 640),
            Real.of(-92097.0 / 339200), Real.of(187.0 / 2100), Real.of(1.0 / 40)
    };

    private final Real relTol;
    private final Real absTol;
    private final Real minStep;
    private final Real maxStep;

    public DormandPrinceIntegrator(Real relTol, Real absTol, Real minStep, Real maxStep) {
        this.relTol = relTol;
        this.absTol = absTol;
        this.minStep = minStep;
        this.maxStep = maxStep;
    }

    public DormandPrinceIntegrator(double relTol, double absTol, double minStep, double maxStep) {
        this(Real.of(relTol), Real.of(absTol), Real.of(minStep), Real.of(maxStep));
    }

    public DormandPrinceIntegrator() {
        this(1e-6, 1e-9, 1e-10, 1.0);
    }

    public Real[] integrate(
            java.util.function.BiFunction<Real, Real[], Real[]> f,
            Real t0, Real[] y0, Real tEnd) {

        int n = y0.length;
        Real t = t0;
        Real[] y = y0.clone();
        Real h = tEnd.subtract(t0).divide(Real.of(100.0)); // Initial step guess
        h = h.max(minStep).min(maxStep);

        Real[][] k = new Real[7][n];

        while (t.compareTo(tEnd) < 0) {
            // Adjust last step
            if (t.add(h).compareTo(tEnd) > 0) {
                h = tEnd.subtract(t);
            }

            // Compute k values
            k[0] = f.apply(t, y);

            for (int stage = 1; stage < 7; stage++) {
                Real[] yTemp = new Real[n];
                for (int i = 0; i < n; i++) {
                    yTemp[i] = y[i];
                    for (int j = 0; j < stage; j++) {
                        yTemp[i] = yTemp[i].add(h.multiply(A[stage][j]).multiply(k[j][i]));
                    }
                }
                k[stage] = f.apply(t.add(C[stage].multiply(h)), yTemp);
            }

            // Compute 5th and 4th order solutions
            Real[] y5 = new Real[n];
            Real[] y4 = new Real[n];
            for (int i = 0; i < n; i++) {
                y5[i] = y[i];
                y4[i] = y[i];
                for (int j = 0; j < 7; j++) {
                    y5[i] = y5[i].add(h.multiply(B5[j]).multiply(k[j][i]));
                    y4[i] = y4[i].add(h.multiply(B4[j]).multiply(k[j][i]));
                }
            }

            // Error estimation
            Real error = Real.ZERO;
            for (int i = 0; i < n; i++) {
                Real sci = absTol.add(relTol.multiply(y[i].abs().max(y5[i].abs())));
                Real err = y5[i].subtract(y4[i]).abs().divide(sci);
                error = error.max(err);
            }

            // Step size control
            if (error.compareTo(Real.ONE) <= 0) {
                // Accept step
                t = t.add(h);
                y = y5;
            }

            // Compute new step size
            // factor = 0.9 * (1/error)^0.2
            Real errorVal = error.max(Real.of(1e-10));
            double factorDouble = 0.9 * Math.pow(1.0 / errorVal.doubleValue(), 0.2);
            Real factor = Real.of(Math.max(0.1, Math.min(5.0, factorDouble)));
            h = h.multiply(factor).max(minStep).min(maxStep);
        }

        return y;
    }

    /**
     * Backward compatibility wrapper for double[].
     */
    public double[] integrate(
            java.util.function.BiFunction<Double, double[], double[]> f,
            double t0, double[] y0, double tEnd) {

        Real[] yReal = new Real[y0.length];
        for (int i = 0; i < y0.length; i++)
            yReal[i] = Real.of(y0[i]);

        Real[] result = integrate(
                (t, y) -> {
                    double[] yPrim = new double[y.length];
                    for (int i = 0; i < y.length; i++)
                        yPrim[i] = y[i].doubleValue();
                    double[] dy = f.apply(t.doubleValue(), yPrim);
                    Real[] dyReal = new Real[dy.length];
                    for (int i = 0; i < dy.length; i++)
                        dyReal[i] = Real.of(dy[i]);
                    return dyReal;
                },
                Real.of(t0), yReal, Real.of(tEnd));

        double[] lastY = new double[result.length];
        for (int i = 0; i < result.length; i++)
            lastY[i] = result[i].doubleValue();
        return lastY;
    }

    /**
     * Integrate with trajectory recording.
     */
    /**
     * Integrate with trajectory recording (Real version).
     */
    public java.util.List<Real[]> integrateWithHistory(
            java.util.function.BiFunction<Real, Real[], Real[]> f,
            Real t0, Real[] y0, Real tEnd) {

        java.util.List<Real[]> history = new java.util.ArrayList<>();
        int n = y0.length;
        Real t = t0;
        Real[] y = y0.clone();
        
        // Record initial state
        Real[] record = new Real[n + 1];
        record[0] = t;
        System.arraycopy(y, 0, record, 1, n);
        history.add(record);

        Real h = tEnd.subtract(t0).divide(Real.of(100.0));
        h = h.max(minStep).min(maxStep);

        Real[][] k = new Real[7][n];

        while (t.compareTo(tEnd) < 0) {
            if (t.add(h).compareTo(tEnd) > 0) {
                h = tEnd.subtract(t);
            }

            k[0] = f.apply(t, y);

            for (int stage = 1; stage < 7; stage++) {
                Real[] yTemp = new Real[n];
                for (int i = 0; i < n; i++) {
                    yTemp[i] = y[i];
                    for (int j = 0; j < stage; j++) {
                        yTemp[i] = yTemp[i].add(h.multiply(A[stage][j]).multiply(k[j][i]));
                    }
                }
                k[stage] = f.apply(t.add(C[stage].multiply(h)), yTemp);
            }

            Real[] y5 = new Real[n];
            Real[] y4 = new Real[n];
            for (int i = 0; i < n; i++) {
                y5[i] = y[i];
                y4[i] = y[i];
                for (int j = 0; j < 7; j++) {
                    y5[i] = y5[i].add(h.multiply(B5[j]).multiply(k[j][i]));
                    y4[i] = y4[i].add(h.multiply(B4[j]).multiply(k[j][i]));
                }
            }

            Real error = Real.ZERO;
            for (int i = 0; i < n; i++) {
                Real sci = absTol.add(relTol.multiply(y[i].abs().max(y5[i].abs())));
                Real err = y5[i].subtract(y4[i]).abs().divide(sci);
                error = error.max(err);
            }

            if (error.compareTo(Real.ONE) <= 0) {
                t = t.add(h);
                y = y5;

                record = new Real[n + 1];
                record[0] = t;
                System.arraycopy(y, 0, record, 1, n);
                history.add(record);
            }

            Real errorVal = error.max(Real.of(1e-10));
            double factorDouble = 0.9 * Math.pow(1.0 / errorVal.doubleValue(), 0.2);
            Real factor = Real.of(Math.max(0.1, Math.min(5.0, factorDouble)));
            h = h.multiply(factor).max(minStep).min(maxStep);
        }

        return history;
    }

    /**
     * Integrate with trajectory recording (Double wrapper).
     */
    public java.util.List<double[]> integrateWithHistory(
            java.util.function.BiFunction<Double, double[], double[]> f,
            double t0, double[] y0, double tEnd) {

        Real[] yReal = new Real[y0.length];
        for (int i = 0; i < y0.length; i++)
            yReal[i] = Real.of(y0[i]);

        java.util.List<Real[]> historyReal = integrateWithHistory(
                (t, y) -> {
                    double[] yPrim = new double[y.length];
                    for (int i = 0; i < y.length; i++)
                        yPrim[i] = y[i].doubleValue();
                    double[] dy = f.apply(t.doubleValue(), yPrim);
                    Real[] dyReal = new Real[dy.length];
                    for (int i = 0; i < dy.length; i++)
                        dyReal[i] = Real.of(dy[i]);
                    return dyReal;
                },
                Real.of(t0), yReal, Real.of(tEnd));

        java.util.List<double[]> historyDouble = new java.util.ArrayList<>();
        for (Real[] rec : historyReal) {
            double[] recD = new double[rec.length];
            for (int i = 0; i < rec.length; i++) {
                recD[i] = rec[i].doubleValue();
            }
            historyDouble.add(recD);
        }
        return historyDouble;
    }
}


