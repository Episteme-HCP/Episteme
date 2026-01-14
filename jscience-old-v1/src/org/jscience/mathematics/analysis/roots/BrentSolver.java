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

package org.jscience.mathematics.analysis.roots;

import org.jscience.mathematics.analysis.MappingException;
import org.jscience.mathematics.analysis.PrimitiveMapping;

/**
 * This class implements the Brent algorithm to compute the roots of
 * a function in an interval.
 * <p/>
 * This class is basically a translation in Java of a fortran
 * implementation found at netlib (<a
 * href="http://www.netlib.org/fmm/zeroin.f">zeroin.f</a>).
 *
 * @author L. Maisonobe
 * @version $Id: BrentSolver.java,v 1.2 2007-10-21 17:45:53 virtualcall Exp $
 */

public class BrentSolver implements RootsFinder {

    /**
     * IEEE 754 epsilon .
     */
    private static final double epsilon = Math.pow(2.0, -52);

    /**
     * Root found.
     */
    private double root;

    /**
     * Simple constructor.
     * Build a Brent solver
     */
    public BrentSolver() {
        root = Double.NaN;
    }

    /**
     * Solve a function in a given interval known to contain a root.
     *
     * @param function function for which a root should be found
     * @param checker  checker for the convergence of the function
     * @param maxIter  maximal number of iteration allowed
     * @param x0       abscissa of the lower bound of the interval
     * @param f0       value of the function the lower bound of the interval
     * @param x1       abscissa of the higher bound of the interval
     * @param f1       value of the function the higher bound of the interval
     * @return true if a root has been found in the given interval
     */
    public boolean findRoot(PrimitiveMapping function,
                            ConvergenceChecker checker,
                            int maxIter,
                            double x0, double f0, double x1, double f1)
            throws MappingException {

        double a = x0;
        double fa = f0;
        double b = x1;
        double fb = f1;

        double c = a;
        double fc = fa;

        double d = b - a;
        double e = d;

        double tolS;
        for (int iter = 0; iter < maxIter; ++iter) {

            if (Math.abs(fc) < Math.abs(fb)) {
                // invert points
                a = b;
                b = c;
                c = a;
                fa = fb;
                fb = fc;
                fc = fa;
            }

            tolS = 2 * epsilon * Math.abs(b);
            double xm = 0.5 * (c - b);

            // convergence test
            double xLow, fLow, xHigh, fHigh;
            if (b < c) {
                xLow = b;
                fLow = fb;
                xHigh = c;
                fHigh = fc;
            } else {
                xLow = c;
                fLow = fc;
                xHigh = b;
                fHigh = fb;
            }

            switch (checker.converged(xLow, fLow, xHigh, fHigh)) {
                case ConvergenceChecker.LOW:
                    root = xLow;
                    return true;
                case ConvergenceChecker.HIGH:
                    root = xHigh;
                    return true;
                default:
                    if ((Math.abs(xm) < tolS) || (Math.abs(fb) < Double.MIN_VALUE)) {
                        root = b;
                        return true;
                    }
            }

            if ((Math.abs(e) < tolS) || (Math.abs(fa) <= Math.abs(fb))) {
                // use bisection method
                d = xm;
                e = d;
            } else {
                // use secant method
                double p, q, r, s;
                s = fb / fa;
                if (Math.abs(a - c) < epsilon * Math.max(Math.abs(a), Math.abs(c))) {
                    // linear interpolation using only b and c points
                    p = 2.0 * xm * s;
                    q = 1.0 - s;
                } else {
                    // inverse quadratic interpolation using a, b and c points
                    q = fa / fc;
                    r = fb / fc;
                    p = s * (2.0 * xm * q * (q - r) - (b - a) * (r - 1.0));
                    q = (q - 1.0) * (r - 1.0) * (s - 1.0);
                }

                // signs adjustment
                if (p > 0.0) {
                    q = -q;
                } else {
                    p = -p;
                }

                // is interpolation acceptable ?
                if (((2.0 * p) < (3.0 * xm * q - Math.abs(tolS * q)))
                        &&
                        (p < Math.abs(0.5 * e * q))) {
                    e = d;
                    d = p / q;
                } else {
                    // no, we need to fall back to bisection
                    d = xm;
                    e = d;
                }
            }

            // complete step
            a = b;
            fa = fb;
            b += ((Math.abs(d) > tolS) ? d : (xm > 0.0 ? tolS : -tolS));
            fb = function.map(b);

            if (fb * fc > 0) {
                c = a;
                fc = fa;
                d = b - a;
                e = d;
            }

        }

        // we have exceeded the maximal number of iterations
        return false;

    }

    /**
     * Get the abscissa of the root.
     *
     * @return abscissa of the root
     */
    public double getRoot() {
        return root;
    }

}
