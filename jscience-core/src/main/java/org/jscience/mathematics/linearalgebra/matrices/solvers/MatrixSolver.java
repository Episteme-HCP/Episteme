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

package org.jscience.mathematics.linearalgebra.matrices.solvers;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.SparseMatrix;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Unified solver for linear systems with automatic or explicit algorithm
 * selection.
 * <p>
 * Analyzes matrix properties and selects the optimal decomposition method.
 * Harmonized with
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MatrixSolver {

    /** Tolerance for numerical comparisons. */
    private static final Real EPSILON = Real.of(1e-10);

    /** Size threshold for switching to iterative methods. */
    private static final int ITERATIVE_SIZE_THRESHOLD = 1000;

    /**
     * Solver strategy/algorithm.
     */
    public enum Strategy {
        /** Automatic selection based on matrix analysis. */
        AUTO,
        /** LU decomposition - general square matrices. */
        LU,
        /** Cholesky decomposition - symmetric positive definite. */
        CHOLESKY,
        /** QR decomposition - overdetermined systems. */
        QR,
        /** SVD - robust for ill-conditioned/rank-deficient. */
        SVD,
        /** Conjugate Gradient - large sparse SPD matrices. */
        CONJUGATE_GRADIENT,
        /** BiCGSTAB - large sparse non-symmetric. */
        BICGSTAB,
        /** GMRES - large sparse non-symmetric. */
        GMRES
    }

    private MatrixSolver() {
        // Utility class
    }

    // ========== Solving linear systems ==========

    /**
     * Solves Ax = b with automatic algorithm selection.
     * 
     * @param A coefficient matrix
     * @param b right-hand side vector
     * @return solution vector x
     */
    public static Real[] solve(Matrix<Real> A, Real[] b) {
        return solve(A, b, Strategy.AUTO);
    }

    /**
     * Solves Ax = b with explicit algorithm selection.
     * 
     * @param A        coefficient matrix
     * @param b        right-hand side vector
     * @param strategy the algorithm to use
     * @return solution vector x
     */
    public static Real[] solve(Matrix<Real> A, Real[] b, Strategy strategy) {
        Strategy effectiveStrategy = strategy;
        if (strategy == Strategy.AUTO) {
            effectiveStrategy = recommend(A);
        }

        switch (effectiveStrategy) {
            case LU:
                return solveLU(A, b);
            case CHOLESKY:
                return solveCholesky(A, b);
            case QR:
                return solveQR(A, b);
            case SVD:
                return solveSVD(A, b);
            case CONJUGATE_GRADIENT:
                return solveCG(A, b);
            case BICGSTAB:
                return solveBiCGSTAB(A, b);
            case GMRES:
                return solveGMRES(A, b);
            default:
                return solveLU(A, b);
        }
    }

    // ========== Matrix analysis and recommendation ==========

    /**
     * Recommends the best solver strategy for the given matrix.
     * 
     * @param A the coefficient matrix
     * @return recommended strategy
     */
    public static Strategy recommend(Matrix<Real> A) {
        int m = A.rows();
        int n = A.cols();

        // Check for sparsity first (large sparse matrices need iterative methods)
        if (A instanceof SparseMatrix && m * n > ITERATIVE_SIZE_THRESHOLD * ITERATIVE_SIZE_THRESHOLD) {
            SparseMatrix<Real> sparse = (SparseMatrix<Real>) A;
            double density = (double) sparse.getNnz() / (m * n);
            if (density < 0.1) {
                // Very sparse - use iterative
                if (isSymmetric(A)) {
                    return Strategy.CONJUGATE_GRADIENT;
                } else {
                    return Strategy.BICGSTAB;
                }
            }
        }

        // Non-square: use QR for overdetermined
        if (m != n) {
            return m > n ? Strategy.QR : Strategy.SVD;
        }

        // Square matrix analysis
        if (isSymmetric(A)) {
            // Try Cholesky (will fail if not positive definite)
            return Strategy.CHOLESKY;
        }

        // Default to LU for general square matrices
        return Strategy.LU;
    }

    /**
     * Checks if a matrix is symmetric.
     * 
     * @param A the matrix to check
     * @return true if symmetric
     */
    public static boolean isSymmetric(Matrix<Real> A) {
        if (A.rows() != A.cols()) {
            return false;
        }
        int n = A.rows();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Real diff = A.get(i, j).subtract(A.get(j, i)).abs();
                if (diff.compareTo(EPSILON) > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if a matrix is likely positive definite using diagonal dominance
     * heuristic.
     * <p>
     * This is a quick check, not a proof. More robust check would require
     * attempting Cholesky and catching failure.
     * </p>
     * 
     * @param A symmetric matrix
     * @return true if likely positive definite
     */
    public static boolean isLikelyPositiveDefinite(Matrix<Real> A) {
        if (!isSymmetric(A)) {
            return false;
        }
        // Quick heuristic: check diagonal elements are positive
        int n = A.rows();
        for (int i = 0; i < n; i++) {
            if (A.get(i, i).compareTo(Real.ZERO) <= 0) {
                return false;
            }
        }
        return true;
    }

    // ========== Individual solver implementations ==========

    private static Real[] solveLU(Matrix<Real> A, Real[] b) {
        LUDecomposition lu = LUDecomposition.decompose(A);
        return lu.solve(b);
    }

    private static Real[] solveCholesky(Matrix<Real> A, Real[] b) {
        try {
            CholeskyDecomposition chol = CholeskyDecomposition.decompose(A);
            return chol.solve(b);
        } catch (IllegalArgumentException e) {
            // Not positive definite - fallback to LU
            return solveLU(A, b);
        }
    }

    private static Real[] solveQR(Matrix<Real> A, Real[] b) {
        QRDecomposition qr = QRDecomposition.decompose(A);
        return qr.solveLeastSquares(b);
    }

    private static Real[] solveSVD(Matrix<Real> A, Real[] b) {
        SVDDecomposition svd = SVDDecomposition.decompose(A);
        // Use pseudoinverse for solving
        Matrix<Real> pinv = svd.pseudoInverse(EPSILON);
        // x = A^+ * b
        int n = pinv.rows();
        Real[] x = new Real[n];
        for (int i = 0; i < n; i++) {
            x[i] = Real.ZERO;
            for (int j = 0; j < b.length; j++) {
                x[i] = x[i].add(pinv.get(i, j).multiply(b[j]));
            }
        }
        return x;
    }

    private static Real[] solveCG(Matrix<Real> A, Real[] b) {
        Real[] x0 = new Real[b.length];
        for (int i = 0; i < b.length; i++) {
            x0[i] = Real.ZERO;
        }
        return org.jscience.mathematics.linearalgebra.matrices.solvers.sparse.ConjugateGradient.solve(
                A, b, x0, EPSILON, b.length * 2);
    }

    private static Real[] solveBiCGSTAB(Matrix<Real> A, Real[] b) {
        Real[] x0 = new Real[b.length];
        for (int i = 0; i < b.length; i++) {
            x0[i] = Real.ZERO;
        }
        return org.jscience.mathematics.linearalgebra.matrices.solvers.sparse.BiCGSTAB.solve(
                A, b, x0, EPSILON, b.length * 2);
    }

    private static Real[] solveGMRES(Matrix<Real> A, Real[] b) {
        Real[] x0 = new Real[b.length];
        for (int i = 0; i < b.length; i++) {
            x0[i] = Real.ZERO;
        }
        return org.jscience.mathematics.linearalgebra.matrices.solvers.sparse.GMRES.solve(
                A, b, x0, EPSILON, Math.min(50, b.length), 5);
    }
}

