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

package org.jscience.mathematics.analysis.interpolation;

/**
 * Cubic spline interpolation for smooth curve fitting.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CubicSplineInterpolator {

    private final double[] x;
    private final double[] y;
    private final double[] a, b, c, d; // Spline coefficients
    private final int n;

    /**
     * Constructs a natural cubic spline through the given points.
     * 
     * @param x x-coordinates (must be strictly increasing)
     * @param y y-coordinates
     */
    public CubicSplineInterpolator(double[] x, double[] y) {
        if (x == null || y == null || x.length != y.length || x.length < 2) {
            throw new IllegalArgumentException("Invalid input arrays");
        }

        this.x = x.clone();
        this.y = y.clone();
        this.n = x.length - 1;

        a = new double[n];
        b = new double[n];
        c = new double[n + 1];
        d = new double[n];

        computeCoefficients();
    }

    private void computeCoefficients() {
        // Natural cubic spline (second derivative = 0 at endpoints)

        for (int i = 0; i < n; i++) {
            a[i] = y[i];
        }

        double[] h = new double[n];
        for (int i = 0; i < n; i++) {
            h[i] = x[i + 1] - x[i];
            if (h[i] <= 0) {
                throw new IllegalArgumentException("x values must be strictly increasing");
            }
        }

        // Solve tridiagonal system for c coefficients
        double[] alpha = new double[n];
        for (int i = 1; i < n; i++) {
            alpha[i] = (3.0 / h[i]) * (y[i + 1] - y[i]) - (3.0 / h[i - 1]) * (y[i] - y[i - 1]);
        }

        double[] l = new double[n + 1];
        double[] mu = new double[n + 1];
        double[] z = new double[n + 1];

        l[0] = 1.0;
        mu[0] = 0.0;
        z[0] = 0.0;

        for (int i = 1; i < n; i++) {
            l[i] = 2.0 * (x[i + 1] - x[i - 1]) - h[i - 1] * mu[i - 1];
            mu[i] = h[i] / l[i];
            z[i] = (alpha[i] - h[i - 1] * z[i - 1]) / l[i];
        }

        l[n] = 1.0;
        z[n] = 0.0;
        c[n] = 0.0;

        for (int j = n - 1; j >= 0; j--) {
            c[j] = z[j] - mu[j] * c[j + 1];
            b[j] = (y[j + 1] - y[j]) / h[j] - h[j] * (c[j + 1] + 2.0 * c[j]) / 3.0;
            d[j] = (c[j + 1] - c[j]) / (3.0 * h[j]);
        }
    }

    /**
     * Interpolates the value at the given point.
     * 
     * @param xval the x-coordinate
     * @return interpolated y-value
     */
    public double interpolate(double xval) {
        // Find the appropriate interval
        int i = 0;
        if (xval >= x[n]) {
            i = n - 1;
        } else {
            while (i < n && x[i + 1] < xval) {
                i++;
            }
        }

        // Evaluate the spline polynomial for this interval
        double dx = xval - x[i];
        return a[i] + b[i] * dx + c[i] * dx * dx + d[i] * dx * dx * dx;
    }

    /**
     * Returns the derivative at the given point.
     */
    public double derivative(double xval) {
        int i = 0;
        if (xval >= x[n]) {
            i = n - 1;
        } else {
            while (i < n && x[i + 1] < xval) {
                i++;
            }
        }

        double dx = xval - x[i];
        return b[i] + 2 * c[i] * dx + 3 * d[i] * dx * dx;
    }

    /**
     * Returns the second derivative at the given point.
     */
    public double secondDerivative(double xval) {
        int i = 0;
        if (xval >= x[n]) {
            i = n - 1;
        } else {
            while (i < n && x[i + 1] < xval) {
                i++;
            }
        }

        double dx = xval - x[i];
        return 2 * c[i] + 6 * d[i] * dx;
    }
}


