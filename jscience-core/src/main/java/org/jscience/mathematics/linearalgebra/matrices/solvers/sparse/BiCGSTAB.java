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
 * BiCGSTAB (BiConjugate Gradient Stabilized) solver.
 * <p>
 * Works for general non-symmetric matrices.
 * Often faster than GMRES for well-conditioned systems.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BiCGSTAB {

    /**
     * Solves Ax = b using BiCGSTAB.
     * 
     * @param A             matrix (can be non-symmetric)
     * @param b             right-hand side
     * @param x0            initial guess
     * @param tolerance     convergence tolerance
     * @param maxIterations maximum iterations
     * @return approximate solution
     */
    public static Real[] solve(Matrix<Real> A, Real[] b, Real[] x0, Real tolerance, int maxIterations) {
        int n = b.length;
        Real[] x = new Real[n];
        System.arraycopy(x0, 0, x, 0, n);

        // r0 = b - Ax0
        Real[] r = subtract(b, matrixVectorMultiply(A, x));
        Real[] r0 = r.clone(); // Arbitrary vector for BiCG

        Real rho = Real.ONE;
        Real alpha = Real.ONE;
        Real omega = Real.ONE;

        Real[] v = new Real[n];
        Real[] p = new Real[n];

        for (int i = 0; i < n; i++) {
            v[i] = Real.ZERO;
            p[i] = Real.ZERO;
        }

        for (int iter = 0; iter < maxIterations; iter++) {
            Real rhoOld = rho;
            rho = dotProduct(r0, r);

            if (rho.abs().compareTo(Real.of(1e-20)) < 0) {
                break; // Breakdown
            }

            if (iter == 0) {
                System.arraycopy(r, 0, p, 0, n);
            } else {
                Real beta = rho.divide(rhoOld).multiply(alpha.divide(omega));

                // p = r + beta * (p - omega * v)
                for (int i = 0; i < n; i++) {
                    p[i] = r[i].add(beta.multiply(p[i].subtract(omega.multiply(v[i]))));
                }
            }

            // v = A * p
            v = matrixVectorMultiply(A, p);

            alpha = rho.divide(dotProduct(r0, v));

            // s = r - alpha * v
            Real[] s = new Real[n];
            for (int i = 0; i < n; i++) {
                s[i] = r[i].subtract(alpha.multiply(v[i]));
            }

            // Check if already converged
            if (norm(s).compareTo(tolerance) < 0) {
                // x = x + alpha * p
                for (int i = 0; i < n; i++) {
                    x[i] = x[i].add(alpha.multiply(p[i]));
                }
                break;
            }

            // t = A * s
            Real[] t = matrixVectorMultiply(A, s);

            omega = dotProduct(t, s).divide(dotProduct(t, t));

            // x = x + alpha * p + omega * s
            for (int i = 0; i < n; i++) {
                x[i] = x[i].add(alpha.multiply(p[i])).add(omega.multiply(s[i]));
            }

            // r = s - omega * t
            for (int i = 0; i < n; i++) {
                r[i] = s[i].subtract(omega.multiply(t[i]));
            }

            // Check convergence
            if (norm(r).compareTo(tolerance) < 0) {
                break;
            }

            if (omega.abs().compareTo(Real.of(1e-20)) < 0) {
                break; // Breakdown
            }
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

    private static Real norm(Real[] v) {
        return dotProduct(v, v).sqrt();
    }

    private static Real[] subtract(Real[] a, Real[] b) {
        Real[] result = new Real[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i].subtract(b[i]);
        }
        return result;
    }
}
