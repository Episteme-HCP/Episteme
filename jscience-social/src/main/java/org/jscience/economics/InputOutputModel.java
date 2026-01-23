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

package org.jscience.economics;

/**
 * Implements the Leontief Input-Output model to analyze inter-industry dependencies 
 * and production requirements.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class InputOutputModel {

    private InputOutputModel() {}

    /**
     * Solves for total sector output X given technical requirement matrix A and final demand D.
     * X = (I - A)⁻¹ D
     */
    public static double[] solveTotalOutput(double[][] technicalCoefficients, double[] finalDemand) {
        int n = finalDemand.length;
        double[][] iminusA = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                iminusA[i][j] = (i == j ? 1.0 : 0.0) - technicalCoefficients[i][j];
            }
        }
        return solveSystem(iminusA, finalDemand);
    }

    /** Calculates the Leontief Inverse matrix. */
    public static double[][] calculateLeontiefInverse(double[][] a) {
        int n = a.length;
        double[][] iminusA = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                iminusA[i][j] = (i == j ? 1.0 : 0.0) - a[i][j];
            }
        }
        return invert(iminusA);
    }

    private static double[] solveSystem(double[][] m, double[] b) {
        int n = b.length;
        double[][] a = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(m[i], 0, a[i], 0, n);
            a[i][n] = b[i];
        }

        for (int i = 0; i < n; i++) {
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(a[j][i]) > Math.abs(a[pivot][i])) pivot = j;
            }
            double[] temp = a[i]; a[i] = a[pivot]; a[pivot] = temp;
            for (int j = i + 1; j < n; j++) {
                double factor = a[j][i] / a[i][i];
                for (int k = i; k <= n; k++) a[j][k] -= factor * a[i][k];
            }
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) sum += a[i][j] * x[j];
            x[i] = (a[i][n] - sum) / a[i][i];
        }
        return x;
    }

    private static double[][] invert(double[][] m) {
        int n = m.length;
        double[][] res = new double[n][n];
        for (int i = 0; i < n; i++) {
            double[] b = new double[n];
            b[i] = 1.0;
            double[] x = solveSystem(m, b);
            for (int j = 0; j < n; j++) res[j][i] = x[j];
        }
        return res;
    }
}
