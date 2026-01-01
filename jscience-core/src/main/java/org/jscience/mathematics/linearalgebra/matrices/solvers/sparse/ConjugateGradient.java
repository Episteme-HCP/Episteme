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

package org.jscience.mathematics.linearalgebra.matrices.solvers.sparse;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Conjugate Gradient solver.
 * <p>
 * Solves Ax = b where A is symmetric positive definite.
 * Efficient for large sparse matrices: O(n) memory vs O(nÃ‚Â²) for direct methods.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ConjugateGradient {

    /**
     * Solves Ax = b using Conjugate Gradient.
     * 
     * @param A             symmetric positive definite matrix
     * @param b             right-hand side vector
     * @param x0            initial guess (can be zero vector)
     * @param tolerance     convergence tolerance
     * @param maxIterations maximum iterations
     * @return approximate solution
     */
    public static Real[] solve(Matrix<Real> A, Real[] b, Real[] x0, Real tolerance, int maxIterations) {
        int n = b.length;
        Real[] x = new Real[n];
        Real[] r = new Real[n];
        Real[] p = new Real[n];

        // Initialize x = x0
        System.arraycopy(x0, 0, x, 0, n);

        // r = b - Ax
        Real[] Ax = matrixVectorMultiply(A, x);
        for (int i = 0; i < n; i++) {
            r[i] = b[i].subtract(Ax[i]);
        }

        // p = r
        System.arraycopy(r, 0, p, 0, n);

        Real rsold = dotProduct(r, r);

        for (int iter = 0; iter < maxIterations; iter++) {
            // Ap = A * p
            Real[] Ap = matrixVectorMultiply(A, p);

            // alpha = rsold / (p^T * Ap)
            Real pAp = dotProduct(p, Ap);
            Real alpha = rsold.divide(pAp);

            // x = x + alpha * p
            for (int i = 0; i < n; i++) {
                x[i] = x[i].add(alpha.multiply(p[i]));
            }

            // r = r - alpha * Ap
            for (int i = 0; i < n; i++) {
                r[i] = r[i].subtract(alpha.multiply(Ap[i]));
            }

            Real rsnew = dotProduct(r, r);

            // Check convergence
            if (rsnew.sqrt().compareTo(tolerance) < 0) {
                break;
            }

            // beta = rsnew / rsold
            Real beta = rsnew.divide(rsold);

            // p = r + beta * p
            for (int i = 0; i < n; i++) {
                p[i] = r[i].add(beta.multiply(p[i]));
            }

            rsold = rsnew;
        }

        return x;
    }

    private static Real[] matrixVectorMultiply(Matrix<Real> A, Real[] x) {
        int n = x.length;
        Real[] result = new Real[n];

        for (int i = 0; i < n; i++) {
            result[i] = Real.ZERO;
            for (int j = 0; j < n; j++) {
                result[i] = result[i].add(A.get(i, j).multiply(x[j]));
            }
        }

        return result;
    }

    private static Real dotProduct(Real[] a, Real[] b) {
        Real sum = Real.ZERO;
        for (int i = 0; i < a.length; i++) {
            sum = sum.add(a[i].multiply(b[i]));
        }
        return sum;
    }
}


