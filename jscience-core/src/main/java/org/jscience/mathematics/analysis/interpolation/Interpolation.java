/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.mathematics.analysis.interpolation;

/**
 * Interpolation methods.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Interpolation {

    /**
     * Linear interpolation.
     */
    public static double linear(double[] x, double[] y, double xi) {
        int i = findInterval(x, xi);
        double t = (xi - x[i]) / (x[i + 1] - x[i]);
        return y[i] + t * (y[i + 1] - y[i]);
    }

    /**
     * Lagrange polynomial interpolation.
     */
    public static double lagrange(double[] x, double[] y, double xi) {
        int n = x.length;
        double result = 0;

        for (int i = 0; i < n; i++) {
            double term = y[i];
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (xi - x[j]) / (x[i] - x[j]);
                }
            }
            result += term;
        }

        return result;
    }

    /**
     * Newton's divided difference interpolation.
     */
    public static double newton(double[] x, double[] y, double xi) {
        int n = x.length;
        double[][] dd = new double[n][n];

        for (int i = 0; i < n; i++) {
            dd[i][0] = y[i];
        }

        for (int j = 1; j < n; j++) {
            for (int i = 0; i < n - j; i++) {
                dd[i][j] = (dd[i + 1][j - 1] - dd[i][j - 1]) / (x[i + j] - x[i]);
            }
        }

        double result = dd[0][0];
        double product = 1;
        for (int j = 1; j < n; j++) {
            product *= (xi - x[j - 1]);
            result += dd[0][j] * product;
        }

        return result;
    }

    /**
     * Cubic spline interpolation (natural boundary conditions).
     */
    public static CubicSpline cubicSpline(double[] x, double[] y) {
        return new CubicSpline(x, y);
    }

    /**
     * Cubic spline class.
     */
    public static class CubicSpline {
        private final double[] x, a, b, c, d;
        private final int n;

        public CubicSpline(double[] x, double[] y) {
            this.n = x.length - 1;
            this.x = x.clone();
            this.a = y.clone();
            this.b = new double[n];
            this.c = new double[n + 1];
            this.d = new double[n];

            double[] h = new double[n];
            for (int i = 0; i < n; i++) {
                h[i] = x[i + 1] - x[i];
            }

            // Tridiagonal system for c
            double[] alpha = new double[n];
            for (int i = 1; i < n; i++) {
                alpha[i] = 3 * (a[i + 1] - a[i]) / h[i] - 3 * (a[i] - a[i - 1]) / h[i - 1];
            }

            double[] l = new double[n + 1];
            double[] mu = new double[n + 1];
            double[] z = new double[n + 1];
            l[0] = 1;

            for (int i = 1; i < n; i++) {
                l[i] = 2 * (x[i + 1] - x[i - 1]) - h[i - 1] * mu[i - 1];
                mu[i] = h[i] / l[i];
                z[i] = (alpha[i] - h[i - 1] * z[i - 1]) / l[i];
            }

            l[n] = 1;
            c[n] = 0;

            for (int j = n - 1; j >= 0; j--) {
                c[j] = z[j] - mu[j] * c[j + 1];
                b[j] = (a[j + 1] - a[j]) / h[j] - h[j] * (c[j + 1] + 2 * c[j]) / 3;
                d[j] = (c[j + 1] - c[j]) / (3 * h[j]);
            }
        }

        public double evaluate(double xi) {
            int i = findInterval(x, xi);
            if (i >= n)
                i = n - 1;
            double dx = xi - x[i];
            return a[i] + b[i] * dx + c[i] * dx * dx + d[i] * dx * dx * dx;
        }

        public double derivative(double xi) {
            int i = findInterval(x, xi);
            if (i >= n)
                i = n - 1;
            double dx = xi - x[i];
            return b[i] + 2 * c[i] * dx + 3 * d[i] * dx * dx;
        }
    }

    /**
     * Bilinear interpolation for 2D grid.
     */
    public static double bilinear(double[] x, double[] y, double[][] z,
            double xi, double yi) {
        int i = findInterval(x, xi);
        int j = findInterval(y, yi);

        double tx = (xi - x[i]) / (x[i + 1] - x[i]);
        double ty = (yi - y[j]) / (y[j + 1] - y[j]);

        return (1 - tx) * (1 - ty) * z[i][j]
                + tx * (1 - ty) * z[i + 1][j]
                + (1 - tx) * ty * z[i][j + 1]
                + tx * ty * z[i + 1][j + 1];
    }

    /**
     * Nearest neighbor interpolation.
     */
    public static double nearestNeighbor(double[] x, double[] y, double xi) {
        int minIndex = 0;
        double minDist = Math.abs(xi - x[0]);

        for (int i = 1; i < x.length; i++) {
            double dist = Math.abs(xi - x[i]);
            if (dist < minDist) {
                minDist = dist;
                minIndex = i;
            }
        }

        return y[minIndex];
    }

    private static int findInterval(double[] x, double xi) {
        int lo = 0, hi = x.length - 2;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            if (xi < x[mid]) {
                hi = mid - 1;
            } else if (xi >= x[mid + 1]) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }
        return Math.max(0, Math.min(x.length - 2, lo));
    }
}
