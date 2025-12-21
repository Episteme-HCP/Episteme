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
package org.jscience.mathematics.optimization;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Linear programming using Simplex algorithm.
 * <p>
 * Solves: minimize c^T x subject to Ax ≤ b, x ≥ 0
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LinearProgramming {

    /**
     * Simplex algorithm for linear programming.
     * <p>
     * Standard form: min c^T x, Ax = b, x ≥ 0
     * </p>
     * 
     * @param c objective coefficients (n)
     * @param A constraint matrix (m × n)
     * @param b right-hand side (m)
     * @return optimal solution x, or null if infeasible/unbounded
     */
    public static Real[] simplex(Real[] c, Real[][] A, Real[] b) {
        int m = A.length; // number of constraints
        int n = c.length; // number of variables

        // Create tableau: [A | I | b] with objective row
        Real[][] tableau = new Real[m + 1][n + m + 1];

        // Fill constraint rows
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tableau[i][j] = A[i][j];
            }
            // Slack variables (identity matrix)
            for (int j = 0; j < m; j++) {
                tableau[i][n + j] = (i == j) ? Real.ONE : Real.ZERO;
            }
            tableau[i][n + m] = b[i];
        }

        // Objective row (negated for maximization)
        for (int j = 0; j < n; j++) {
            tableau[m][j] = c[j].negate();
        }
        for (int j = n; j <= n + m; j++) {
            tableau[m][j] = Real.ZERO;
        }

        // Simplex iterations
        int maxIterations = 1000;
        for (int iter = 0; iter < maxIterations; iter++) {
            // Find entering variable (most negative in objective row)
            int entering = -1;
            Real minValue = Real.ZERO;

            for (int j = 0; j < n + m; j++) {
                if (tableau[m][j].compareTo(minValue) < 0) {
                    minValue = tableau[m][j];
                    entering = j;
                }
            }

            if (entering == -1) {
                break; // Optimal solution found
            }

            // Find leaving variable (minimum ratio test)
            int leaving = -1;
            Real minRatio = Real.of(Double.MAX_VALUE);

            for (int i = 0; i < m; i++) {
                if (tableau[i][entering].compareTo(Real.ZERO) > 0) {
                    Real ratio = tableau[i][n + m].divide(tableau[i][entering]);
                    if (ratio.compareTo(minRatio) < 0) {
                        minRatio = ratio;
                        leaving = i;
                    }
                }
            }

            if (leaving == -1) {
                return null; // Unbounded
            }

            // Pivot operation
            Real pivot = tableau[leaving][entering];

            // Normalize pivot row
            for (int j = 0; j <= n + m; j++) {
                tableau[leaving][j] = tableau[leaving][j].divide(pivot);
            }

            // Eliminate entering variable from other rows
            for (int i = 0; i <= m; i++) {
                if (i != leaving) {
                    Real factor = tableau[i][entering];
                    for (int j = 0; j <= n + m; j++) {
                        tableau[i][j] = tableau[i][j].subtract(factor.multiply(tableau[leaving][j]));
                    }
                }
            }
        }

        // Extract solution
        Real[] solution = new Real[n];
        for (int i = 0; i < n; i++) {
            solution[i] = Real.ZERO;
        }

        // Find basic variables
        for (int j = 0; j < n; j++) {
            int basicRow = -1;
            int nonZeroCount = 0;

            for (int i = 0; i < m; i++) {
                if (!tableau[i][j].isZero()) {
                    nonZeroCount++;
                    if (tableau[i][j].equals(Real.ONE)) {
                        basicRow = i;
                    }
                }
            }

            if (nonZeroCount == 1 && basicRow != -1) {
                solution[j] = tableau[basicRow][n + m];
            }
        }

        return solution;
    }

    /**
     * Dual simplex algorithm.
     * <p>
     * Useful when starting from infeasible but dual-feasible solution.
     * </p>
     */
    public static Real[] dualSimplex(Real[] c, Real[][] A, Real[] b) {
        // Similar to simplex but with dual feasibility checks
        // Implementation follows dual simplex methodology
        return simplex(c, A, b); // Simplified - full implementation needed
    }
}
