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

package org.jscience.mathematics.optimization;

import java.util.function.BiFunction;

/**
 * Levenberg-Marquardt algorithm for nonlinear least squares.
 * <p>
 * Combines Gauss-Newton and gradient descent for robust curve fitting.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LevenbergMarquardt {

    private final double tolerance;
    private final int maxIterations;
    private double lambda = 0.01; // Damping parameter
    private double lambdaUp = 10.0; // Factor to increase lambda
    private double lambdaDown = 0.1; // Factor to decrease lambda

    public LevenbergMarquardt(double tolerance, int maxIterations) {
        this.tolerance = tolerance;
        this.maxIterations = maxIterations;
    }

    public LevenbergMarquardt() {
        this(1e-8, 100);
    }

    /**
     * Fit a model to data using Levenberg-Marquardt.
     * 
     * @param model   Function (params, x) -> predicted y
     * @param xData   Independent variable data points
     * @param yData   Observed values
     * @param params0 Initial parameter guess
     * @return Optimized parameters
     */
    public double[] fit(BiFunction<double[], Double, Double> model,
            double[] xData, double[] yData, double[] params0) {
        int n = xData.length;
        int p = params0.length;
        double[] params = params0.clone();
        double[] residuals = new double[n];
        double[][] jacobian = new double[n][p];

        double prevCost = computeCost(model, xData, yData, params, residuals);

        for (int iter = 0; iter < maxIterations; iter++) {
            // Compute Jacobian
            computeJacobian(model, xData, params, jacobian);

            // Compute J^T * J and J^T * r
            double[][] jtj = new double[p][p];
            double[] jtr = new double[p];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < p; j++) {
                    jtr[j] += jacobian[i][j] * residuals[i];
                    for (int k = 0; k < p; k++) {
                        jtj[j][k] += jacobian[i][j] * jacobian[i][k];
                    }
                }
            }

            // Add damping: (J^T J + ÃŽÂ»I)
            for (int j = 0; j < p; j++) {
                jtj[j][j] *= (1 + lambda);
            }

            // Solve for delta
            double[] delta = solveLinear(jtj, jtr);

            // Try new parameters
            double[] newParams = new double[p];
            for (int j = 0; j < p; j++) {
                newParams[j] = params[j] + delta[j];
            }

            double newCost = computeCost(model, xData, yData, newParams, residuals);

            if (newCost < prevCost) {
                // Accept step, decrease lambda
                params = newParams;
                lambda *= lambdaDown;

                if (Math.abs(prevCost - newCost) < tolerance) {
                    break; // Converged
                }
                prevCost = newCost;
            } else {
                // Reject step, increase lambda
                lambda *= lambdaUp;
                // Recompute residuals with old params
                computeCost(model, xData, yData, params, residuals);
            }
        }

        return params;
    }

    private double computeCost(BiFunction<double[], Double, Double> model,
            double[] xData, double[] yData, double[] params,
            double[] residuals) {
        double cost = 0;
        for (int i = 0; i < xData.length; i++) {
            double predicted = model.apply(params, xData[i]);
            residuals[i] = yData[i] - predicted;
            cost += residuals[i] * residuals[i];
        }
        return cost;
    }

    private void computeJacobian(BiFunction<double[], Double, Double> model,
            double[] xData, double[] params, double[][] jacobian) {
        double h = 1e-8;
        int n = xData.length;
        int p = params.length;

        for (int j = 0; j < p; j++) {
            double[] paramsPlusH = params.clone();
            paramsPlusH[j] += h;

            for (int i = 0; i < n; i++) {
                double f0 = model.apply(params, xData[i]);
                double f1 = model.apply(paramsPlusH, xData[i]);
                jacobian[i][j] = (f1 - f0) / h;
            }
        }
    }

    private double[] solveLinear(double[][] A, double[] b) {
        int n = b.length;
        double[][] aug = new double[n][n + 1];

        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, aug[i], 0, n);
            aug[i][n] = b[i];
        }

        // Gaussian elimination with partial pivoting
        for (int k = 0; k < n; k++) {
            int maxRow = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(aug[i][k]) > Math.abs(aug[maxRow][k]))
                    maxRow = i;
            }
            double[] temp = aug[k];
            aug[k] = aug[maxRow];
            aug[maxRow] = temp;

            if (Math.abs(aug[k][k]) < 1e-15)
                continue;

            for (int i = k + 1; i < n; i++) {
                double factor = aug[i][k] / aug[k][k];
                for (int j = k; j <= n; j++)
                    aug[i][j] -= factor * aug[k][j];
            }
        }

        // Back substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = aug[i][n];
            for (int j = i + 1; j < n; j++)
                x[i] -= aug[i][j] * x[j];
            if (Math.abs(aug[i][i]) > 1e-15)
                x[i] /= aug[i][i];
        }
        return x;
    }

    // --- Convenience methods for common models ---

    /**
     * Fit exponential: y = a * exp(b * x)
     */
    public double[] fitExponential(double[] xData, double[] yData) {
        BiFunction<double[], Double, Double> model = (p, x) -> p[0] * Math.exp(p[1] * x);
        return fit(model, xData, yData, new double[] { 1.0, 0.1 });
    }

    /**
     * Fit Gaussian: y = a * exp(-((x-b)^2) / (2*c^2))
     */
    public double[] fitGaussian(double[] xData, double[] yData) {
        BiFunction<double[], Double, Double> model = (p, x) -> p[0]
                * Math.exp(-Math.pow(x - p[1], 2) / (2 * p[2] * p[2]));
        // Initial guess: amplitude=max(y), mean=mean(x), sigma=std(x)
        double maxY = 0, meanX = 0;
        for (int i = 0; i < xData.length; i++) {
            maxY = Math.max(maxY, yData[i]);
            meanX += xData[i];
        }
        meanX /= xData.length;
        return fit(model, xData, yData, new double[] { maxY, meanX, 1.0 });
    }

    /**
     * Fit power law: y = a * x^b
     */
    public double[] fitPowerLaw(double[] xData, double[] yData) {
        BiFunction<double[], Double, Double> model = (p, x) -> p[0] * Math.pow(x, p[1]);
        return fit(model, xData, yData, new double[] { 1.0, 1.0 });
    }
}


