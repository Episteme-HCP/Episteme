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

package org.jscience.mathematics.statistics.fitting;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Polynomial regression: y = a₀ + a₁x + a₂x² + ... + aₙxⁿ.
 * <p>
 * Uses normal equations with LU decomposition for stability.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PolynomialRegression {

    private final double[] coefficients;
    private final int degree;
    private final double rSquared;

    private PolynomialRegression(double[] coefficients, double rSquared) {
        this.coefficients = coefficients;
        this.degree = coefficients.length - 1;
        this.rSquared = rSquared;
    }

    /**
     * Fits a polynomial of given degree to the data.
     * 
     * @param x      Independent variable values
     * @param y      Dependent variable values
     * @param degree Polynomial degree (1 = linear, 2 = quadratic, etc.)
     * @return PolynomialRegression model
     */
    public static PolynomialRegression fit(double[] x, double[] y, int degree) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("x and y must have same length");
        }
        if (x.length <= degree) {
            throw new IllegalArgumentException("Need more data points than polynomial degree");
        }

        int n = x.length;
        int m = degree + 1;

        // Build Vandermonde-like matrix X^T * X
        double[][] XtX = new double[m][m];
        double[] XtY = new double[m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += Math.pow(x[k], i + j);
                }
                XtX[i][j] = sum;
            }
            double sumY = 0;
            for (int k = 0; k < n; k++) {
                sumY += Math.pow(x[k], i) * y[k];
            }
            XtY[i] = sumY;
        }

        // Solve using Gaussian elimination with partial pivoting
        double[] coeffs = gaussianElimination(XtX, XtY);

        // Compute R-squared
        double meanY = 0;
        for (double yi : y)
            meanY += yi;
        meanY /= n;

        double ssTot = 0, ssRes = 0;
        for (int i = 0; i < n; i++) {
            double predicted = evaluate(coeffs, x[i]);
            ssTot += (y[i] - meanY) * (y[i] - meanY);
            ssRes += (y[i] - predicted) * (y[i] - predicted);
        }
        double rSquared = 1.0 - (ssRes / ssTot);

        return new PolynomialRegression(coeffs, rSquared);
    }

    private static double[] gaussianElimination(double[][] A, double[] b) {
        int n = b.length;
        double[][] aug = new double[n][n + 1];

        // Create augmented matrix
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, aug[i], 0, n);
            aug[i][n] = b[i];
        }

        // Forward elimination with partial pivoting
        for (int k = 0; k < n; k++) {
            // Find pivot
            int maxRow = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(aug[i][k]) > Math.abs(aug[maxRow][k])) {
                    maxRow = i;
                }
            }
            double[] temp = aug[k];
            aug[k] = aug[maxRow];
            aug[maxRow] = temp;

            // Eliminate
            for (int i = k + 1; i < n; i++) {
                double factor = aug[i][k] / aug[k][k];
                for (int j = k; j <= n; j++) {
                    aug[i][j] -= factor * aug[k][j];
                }
            }
        }

        // Back substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = aug[i][n];
            for (int j = i + 1; j < n; j++) {
                x[i] -= aug[i][j] * x[j];
            }
            x[i] /= aug[i][i];
        }

        return x;
    }

    private static double evaluate(double[] coeffs, double x) {
        double result = 0;
        double xPow = 1;
        for (double coeff : coeffs) {
            result += coeff * xPow;
            xPow *= x;
        }
        return result;
    }

    /**
     * Predicts y for given x.
     */
    public double predict(double x) {
        return evaluate(coefficients, x);
    }

    public Real predict(Real x) {
        return Real.of(predict(x.doubleValue()));
    }

    // --- Accessors ---
    public double[] getCoefficients() {
        return coefficients.clone();
    }

    public int getDegree() {
        return degree;
    }

    public double getRSquared() {
        return rSquared;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("y = ");
        for (int i = 0; i < coefficients.length; i++) {
            if (i > 0 && coefficients[i] >= 0)
                sb.append(" + ");
            else if (i > 0)
                sb.append(" ");
            sb.append(String.format("%.4f", coefficients[i]));
            if (i == 1)
                sb.append("*x");
            else if (i > 1)
                sb.append("*x^").append(i);
        }
        sb.append(String.format(" (R² = %.4f)", rSquared));
        return sb.toString();
    }
}
