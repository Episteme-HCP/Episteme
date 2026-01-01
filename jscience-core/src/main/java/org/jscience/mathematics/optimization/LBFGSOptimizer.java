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

import java.util.function.Function;

/**
 * L-BFGS (Limited-memory BFGS) optimizer.
 * <p>
 * Memory-efficient version of BFGS for large-scale optimization.
 * Stores only m previous gradients/positions instead of full Hessian.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LBFGSOptimizer {

    private final int m; // Number of corrections to store
    private final double tolerance;
    private final int maxIterations;

    public LBFGSOptimizer(int m, double tolerance, int maxIterations) {
        this.m = m;
        this.tolerance = tolerance;
        this.maxIterations = maxIterations;
    }

    public LBFGSOptimizer() {
        this(10, 1e-6, 1000);
    }

    /**
     * Minimize a function using L-BFGS.
     * 
     * @param f        Objective function
     * @param gradient Gradient of f
     * @param x0       Initial guess
     * @return Approximate minimum
     */
    public double[] minimize(
            Function<double[], Double> f,
            Function<double[], double[]> gradient,
            double[] x0) {

        int n = x0.length;
        double[] x = x0.clone();
        double[] g = gradient.apply(x);

        // Storage for L-BFGS updates
        double[][] s = new double[m][n]; // x_{k+1} - x_k
        double[][] y = new double[m][n]; // g_{k+1} - g_k
        double[] rho = new double[m]; // 1 / (y^T * s)
        double[] alpha = new double[m];

        int k = 0;

        for (int iter = 0; iter < maxIterations; iter++) {
            // Check convergence
            double gradNorm = norm(g);
            if (gradNorm < tolerance) {
                break;
            }

            // Compute search direction using two-loop recursion
            double[] q = g.clone();

            // First loop (backward)
            int incr = Math.min(k, m);
            for (int i = incr - 1; i >= 0; i--) {
                int idx = (k - 1 - i) % m;
                if (idx < 0)
                    idx += m;

                alpha[idx] = rho[idx] * dot(s[idx], q);
                for (int j = 0; j < n; j++) {
                    q[j] -= alpha[idx] * y[idx][j];
                }
            }

            // Initial Hessian approximation: H_0 = gamma * I
            double gamma = 1.0;
            if (k > 0) {
                int idx = (k - 1) % m;
                gamma = dot(s[idx], y[idx]) / dot(y[idx], y[idx]);
            }

            double[] r = new double[n];
            for (int j = 0; j < n; j++) {
                r[j] = gamma * q[j];
            }

            // Second loop (forward)
            for (int i = 0; i < incr; i++) {
                int idx = (k - incr + i) % m;
                if (idx < 0)
                    idx += m;

                double beta = rho[idx] * dot(y[idx], r);
                for (int j = 0; j < n; j++) {
                    r[j] += s[idx][j] * (alpha[idx] - beta);
                }
            }

            // Search direction: p = -H * g
            double[] p = new double[n];
            for (int j = 0; j < n; j++) {
                p[j] = -r[j];
            }

            // Line search
            double stepSize = backtrackingLineSearch(f, gradient, x, p, g);

            // Update position
            double[] xNew = new double[n];
            for (int j = 0; j < n; j++) {
                xNew[j] = x[j] + stepSize * p[j];
            }

            double[] gNew = gradient.apply(xNew);

            // Store s and y
            int idx = k % m;
            for (int j = 0; j < n; j++) {
                s[idx][j] = xNew[j] - x[j];
                y[idx][j] = gNew[j] - g[j];
            }

            double ys = dot(s[idx], y[idx]);
            if (Math.abs(ys) > 1e-15) {
                rho[idx] = 1.0 / ys;
            } else {
                rho[idx] = 1e15;
            }

            x = xNew;
            g = gNew;
            k++;
        }

        return x;
    }

    private double backtrackingLineSearch(
            Function<double[], Double> f,
            Function<double[], double[]> gradient,
            double[] x, double[] p, double[] g) {

        double alpha = 1.0;
        double c1 = 1e-4;
        double fx = f.apply(x);
        double gp = dot(g, p);

        for (int i = 0; i < 20; i++) {
            double[] xNew = new double[x.length];
            for (int j = 0; j < x.length; j++) {
                xNew[j] = x[j] + alpha * p[j];
            }

            if (f.apply(xNew) <= fx + c1 * alpha * gp) {
                return alpha;
            }

            alpha *= 0.5;
        }

        return alpha;
    }

    private static double dot(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }

    private static double norm(double[] v) {
        return Math.sqrt(dot(v, v));
    }
}


