/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.optimization;

import org.jscience.mathematics.numbers.real.Real;

/**
 * BFGS (Broyden-Fletcher-Goldfarb-Shanno) quasi-Newton optimization.
 * <p>
 * Efficient unconstrained optimization without computing Hessian directly.
 * Uses rank-2 updates to approximate inverse Hessian.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BFGSOptimizer {

    /**
     * Minimizes a multivariate function using BFGS.
     * 
     * @param f             Objective function (Real[] -> Real)
     * @param gradient      Gradient of f (Real[] -> Real[])
     * @param initialGuess  Starting point
     * @param tolerance     Convergence tolerance (gradient norm)
     * @param maxIterations Maximum iterations
     * @return Approximate minimum point
     */
    public static Real[] minimize(
            java.util.function.Function<Real[], Real> f,
            java.util.function.Function<Real[], Real[]> gradient,
            Real[] initialGuess,
            double tolerance,
            int maxIterations) {

        int n = initialGuess.length;
        Real[] x = initialGuess.clone();

        // Initialize inverse Hessian approximation to identity
        double[][] H = new double[n][n];
        for (int i = 0; i < n; i++) {
            H[i][i] = 1.0;
        }

        Real[] g = gradient.apply(x);

        for (int iter = 0; iter < maxIterations; iter++) {
            // Check convergence
            double gradNorm = 0;
            for (Real gi : g) {
                gradNorm += gi.doubleValue() * gi.doubleValue();
            }
            gradNorm = Math.sqrt(gradNorm);

            if (gradNorm < tolerance) {
                return x;
            }

            // Compute search direction: p = -H * g
            double[] p = new double[n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    p[i] -= H[i][j] * g[j].doubleValue();
                }
            }

            // Line search with backtracking
            double alpha = backtrackingLineSearch(f, gradient, x, p, 0.0001, 0.9);

            // Update x: x_new = x + alpha * p
            Real[] xNew = new Real[n];
            for (int i = 0; i < n; i++) {
                xNew[i] = x[i].add(Real.of(alpha * p[i]));
            }

            Real[] gNew = gradient.apply(xNew);

            // Compute s = x_new - x, y = g_new - g
            double[] s = new double[n];
            double[] y = new double[n];
            for (int i = 0; i < n; i++) {
                s[i] = xNew[i].subtract(x[i]).doubleValue();
                y[i] = gNew[i].subtract(g[i]).doubleValue();
            }

            // Compute rho = 1 / (y^T * s)
            double ys = 0;
            for (int i = 0; i < n; i++) {
                ys += y[i] * s[i];
            }

            if (Math.abs(ys) < 1e-15) {
                // Skip update if curvature condition not satisfied
                x = xNew;
                g = gNew;
                continue;
            }

            double rho = 1.0 / ys;

            // BFGS update: H = (I - rho*s*y^T) * H * (I - rho*y*s^T) + rho*s*s^T
            double[][] HNew = new double[n][n];

            // Compute H * y
            double[] Hy = new double[n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Hy[i] += H[i][j] * y[j];
                }
            }

            // Compute y^T * H * y
            double yHy = 0;
            for (int i = 0; i < n; i++) {
                yHy += y[i] * Hy[i];
            }

            // Sherman-Morrison formula simplified
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    HNew[i][j] = H[i][j]
                            - rho * (s[i] * Hy[j] + Hy[i] * s[j])
                            + rho * rho * yHy * s[i] * s[j]
                            + rho * s[i] * s[j];
                }
            }

            H = HNew;
            x = xNew;
            g = gNew;
        }

        return x;
    }

    private static double backtrackingLineSearch(
            java.util.function.Function<Real[], Real> f,
            java.util.function.Function<Real[], Real[]> gradient,
            Real[] x, double[] p,
            double c1, double c2) {

        double alpha = 1.0;
        double fx = f.apply(x).doubleValue();
        Real[] g = gradient.apply(x);

        // Compute directional derivative
        double gp = 0;
        for (int i = 0; i < x.length; i++) {
            gp += g[i].doubleValue() * p[i];
        }

        for (int i = 0; i < 20; i++) {
            Real[] xNew = new Real[x.length];
            for (int j = 0; j < x.length; j++) {
                xNew[j] = x[j].add(Real.of(alpha * p[j]));
            }

            double fNew = f.apply(xNew).doubleValue();

            // Armijo condition
            if (fNew <= fx + c1 * alpha * gp) {
                return alpha;
            }

            alpha *= 0.5;
        }

        return alpha;
    }
}


