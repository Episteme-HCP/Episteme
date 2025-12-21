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
package org.jscience.mathematics.analysis.integration;

import java.util.function.DoubleUnaryOperator;

/**
 * Numerical integration methods.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NumericalIntegration {

    /**
     * Trapezoidal rule.
     */
    public static double trapezoidal(DoubleUnaryOperator f, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0.5 * (f.applyAsDouble(a) + f.applyAsDouble(b));
        for (int i = 1; i < n; i++) {
            sum += f.applyAsDouble(a + i * h);
        }
        return sum * h;
    }

    /**
     * Simpson's rule.
     */
    public static double simpson(DoubleUnaryOperator f, double a, double b, int n) {
        if (n % 2 != 0)
            n++; // Must be even
        double h = (b - a) / n;
        double sum = f.applyAsDouble(a) + f.applyAsDouble(b);

        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += (i % 2 == 0 ? 2 : 4) * f.applyAsDouble(x);
        }
        return sum * h / 3;
    }

    /**
     * Romberg integration (Richardson extrapolation of trapezoidal).
     */
    public static double romberg(DoubleUnaryOperator f, double a, double b, int maxLevel) {
        double[][] r = new double[maxLevel][maxLevel];

        r[0][0] = 0.5 * (b - a) * (f.applyAsDouble(a) + f.applyAsDouble(b));

        for (int i = 1; i < maxLevel; i++) {
            int n = 1 << i; // 2^i
            double h = (b - a) / n;

            // Trapezoidal with 2^i intervals
            double sum = 0;
            for (int k = 1; k <= n / 2; k++) {
                sum += f.applyAsDouble(a + (2 * k - 1) * h);
            }
            r[i][0] = 0.5 * r[i - 1][0] + h * sum;

            // Richardson extrapolation
            for (int j = 1; j <= i; j++) {
                double pow4j = Math.pow(4, j);
                r[i][j] = (pow4j * r[i][j - 1] - r[i - 1][j - 1]) / (pow4j - 1);
            }
        }

        return r[maxLevel - 1][maxLevel - 1];
    }

    /**
     * Gaussian quadrature (Gauss-Legendre, 5 points).
     */
    public static double gaussLegendre(DoubleUnaryOperator f, double a, double b) {
        // 5-point Gauss-Legendre nodes and weights on [-1, 1]
        double[] nodes = {
                -0.9061798459386640, -0.5384693101056831, 0.0,
                0.5384693101056831, 0.9061798459386640
        };
        double[] weights = {
                0.2369268850561891, 0.4786286704993665, 0.5688888888888889,
                0.4786286704993665, 0.2369268850561891
        };

        // Transform to [a, b]
        double mid = (a + b) / 2;
        double half = (b - a) / 2;

        double sum = 0;
        for (int i = 0; i < 5; i++) {
            double x = mid + half * nodes[i];
            sum += weights[i] * f.applyAsDouble(x);
        }
        return sum * half;
    }

    /**
     * Adaptive Simpson's rule with error control.
     */
    public static double adaptiveSimpson(DoubleUnaryOperator f, double a, double b,
            double tol, int maxDepth) {
        double c = (a + b) / 2;
        double fa = f.applyAsDouble(a);
        double fb = f.applyAsDouble(b);
        double fc = f.applyAsDouble(c);
        double s = (b - a) / 6 * (fa + 4 * fc + fb);
        return adaptiveSimpsonHelper(f, a, b, fa, fb, fc, s, tol, maxDepth);
    }

    private static double adaptiveSimpsonHelper(DoubleUnaryOperator f, double a, double b,
            double fa, double fb, double fc, double prevS,
            double tol, int depth) {
        double c = (a + b) / 2;
        double d = (a + c) / 2;
        double e = (c + b) / 2;
        double fd = f.applyAsDouble(d);
        double fe = f.applyAsDouble(e);

        double h = b - a;
        double left = h / 12 * (fa + 4 * fd + fc);
        double right = h / 12 * (fc + 4 * fe + fb);
        double s = left + right;

        if (depth <= 0 || Math.abs(s - prevS) <= 15 * tol) {
            return s + (s - prevS) / 15;
        }

        return adaptiveSimpsonHelper(f, a, c, fa, fc, fd, left, tol / 2, depth - 1)
                + adaptiveSimpsonHelper(f, c, b, fc, fb, fe, right, tol / 2, depth - 1);
    }

    /**
     * Integrate tabulated data using trapezoidal rule.
     */
    public static double integrateData(double[] x, double[] y) {
        if (x.length != y.length || x.length < 2) {
            throw new IllegalArgumentException("Arrays must have same length >= 2");
        }
        double sum = 0;
        for (int i = 1; i < x.length; i++) {
            sum += 0.5 * (y[i] + y[i - 1]) * (x[i] - x[i - 1]);
        }
        return sum;
    }
}
