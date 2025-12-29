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

package org.jscience.mathematics.analysis.differentiation;

import java.util.function.DoubleUnaryOperator;

/**
 * Numerical differentiation methods.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NumericalDifferentiation {

    /**
     * Forward difference: f'(x) ≈ (f(x+h) - f(x)) / h
     * First-order accurate: O(h)
     */
    public static double forwardDifference(DoubleUnaryOperator f, double x, double h) {
        return (f.applyAsDouble(x + h) - f.applyAsDouble(x)) / h;
    }

    /**
     * Backward difference: f'(x) ≈ (f(x) - f(x-h)) / h
     * First-order accurate: O(h)
     */
    public static double backwardDifference(DoubleUnaryOperator f, double x, double h) {
        return (f.applyAsDouble(x) - f.applyAsDouble(x - h)) / h;
    }

    /**
     * Central difference: f'(x) ≈ (f(x+h) - f(x-h)) / 2h
     * Second-order accurate: O(h²)
     */
    public static double centralDifference(DoubleUnaryOperator f, double x, double h) {
        return (f.applyAsDouble(x + h) - f.applyAsDouble(x - h)) / (2 * h);
    }

    /**
     * Five-point stencil: f'(x) ≈ (-f(x+2h) + 8f(x+h) - 8f(x-h) + f(x-2h)) / 12h
     * Fourth-order accurate: O(h⁴)
     */
    public static double fivePointStencil(DoubleUnaryOperator f, double x, double h) {
        return (-f.applyAsDouble(x + 2 * h) + 8 * f.applyAsDouble(x + h)
                - 8 * f.applyAsDouble(x - h) + f.applyAsDouble(x - 2 * h)) / (12 * h);
    }

    /**
     * Second derivative using central difference:
     * f''(x) ≈ (f(x+h) - 2f(x) + f(x-h)) / h²
     */
    public static double secondDerivative(DoubleUnaryOperator f, double x, double h) {
        return (f.applyAsDouble(x + h) - 2 * f.applyAsDouble(x) + f.applyAsDouble(x - h)) / (h * h);
    }

    /**
     * Richardson extrapolation for improved accuracy.
     */
    public static double richardsonExtrapolation(DoubleUnaryOperator f, double x,
            double h, int order) {
        double[] d = new double[order];

        for (int i = 0; i < order; i++) {
            double hi = h / Math.pow(2, i);
            d[i] = centralDifference(f, x, hi);
        }

        // Extrapolation
        for (int j = 1; j < order; j++) {
            double factor = Math.pow(4, j);
            for (int i = order - 1; i >= j; i--) {
                d[i] = (factor * d[i] - d[i - 1]) / (factor - 1);
            }
        }

        return d[order - 1];
    }

    /**
     * Compute gradient for multivariate function.
     * 
     * @param f Function f(x) -> y
     * @param x Point to evaluate
     * @param h Step size
     * @return Gradient vector
     */
    public static double[] gradient(java.util.function.Function<double[], Double> f,
            double[] x, double h) {
        int n = x.length;
        double[] grad = new double[n];
        double[] xPlus = x.clone();
        double[] xMinus = x.clone();

        for (int i = 0; i < n; i++) {
            xPlus[i] = x[i] + h;
            xMinus[i] = x[i] - h;
            grad[i] = (f.apply(xPlus) - f.apply(xMinus)) / (2 * h);
            xPlus[i] = x[i];
            xMinus[i] = x[i];
        }

        return grad;
    }

    /**
     * Compute Hessian matrix (second partial derivatives).
     */
    public static double[][] hessian(java.util.function.Function<double[], Double> f,
            double[] x, double h) {
        int n = x.length;
        double[][] H = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                double[] xpp = x.clone(), xpm = x.clone();
                double[] xmp = x.clone(), xmm = x.clone();

                xpp[i] += h;
                xpp[j] += h;
                xpm[i] += h;
                xpm[j] -= h;
                xmp[i] -= h;
                xmp[j] += h;
                xmm[i] -= h;
                xmm[j] -= h;

                H[i][j] = (f.apply(xpp) - f.apply(xpm) - f.apply(xmp) + f.apply(xmm))
                        / (4 * h * h);
                H[j][i] = H[i][j]; // Symmetric
            }
        }

        return H;
    }

    /**
     * Compute Jacobian matrix for vector-valued function.
     */
    public static double[][] jacobian(java.util.function.Function<double[], double[]> f,
            double[] x, double h) {
        double[] fx = f.apply(x);
        int m = fx.length;
        int n = x.length;
        double[][] J = new double[m][n];

        for (int j = 0; j < n; j++) {
            double[] xPlus = x.clone();
            xPlus[j] += h;
            double[] fPlus = f.apply(xPlus);

            for (int i = 0; i < m; i++) {
                J[i][j] = (fPlus[i] - fx[i]) / h;
            }
        }

        return J;
    }

    /**
     * Laplacian: ∇²f = ∂²f/∂x² + ∂²f/∂y² + ...
     */
    public static double laplacian(java.util.function.Function<double[], Double> f,
            double[] x, double h) {
        int n = x.length;
        double fx = f.apply(x);
        double sum = -2 * n * fx;

        for (int i = 0; i < n; i++) {
            double[] xPlus = x.clone(), xMinus = x.clone();
            xPlus[i] += h;
            xMinus[i] -= h;
            sum += f.apply(xPlus) + f.apply(xMinus);
        }

        return sum / (h * h);
    }
}
