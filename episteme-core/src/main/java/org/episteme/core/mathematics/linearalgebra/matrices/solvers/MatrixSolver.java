/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices.solvers;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.technical.algorithm.AlgorithmManager;

/**
 * Unified solver for linear systems with automatic or explicit algorithm selection.
 * <p>
 * Analyzes matrix properties and selects the optimal decomposition method.
 * Delegates to the active {@link LinearAlgebraProvider} for computations.
 * </p>
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

    /**
     * Solves Ax = b with automatic algorithm selection.
     */
    public static Real[] solve(Matrix<Real> A, Real[] b) {
        return solve(A, b, Strategy.AUTO);
    }

    /**
     * Solves Ax = b with explicit algorithm selection.
     */
    @SuppressWarnings("unchecked")
    public static Real[] solve(Matrix<Real> A, Real[] b, Strategy strategy) {
        Strategy effectiveStrategy = (strategy == Strategy.AUTO) ? recommend(A) : strategy;
        LinearAlgebraProvider<Real> provider = (LinearAlgebraProvider<Real>) AlgorithmManager.getProvider(LinearAlgebraProvider.class);
        Vector<Real> bVec = Vector.of(java.util.Arrays.asList(b), Reals.getInstance());

        switch (effectiveStrategy) {
            case LU:
                return toRealArray(provider.solve(provider.lu(A), bVec));
            case CHOLESKY:
                try {
                    return toRealArray(provider.solve(provider.cholesky(A), bVec));
                } catch (IllegalArgumentException e) {
                    return toRealArray(provider.solve(provider.lu(A), bVec));
                }
            case QR:
                return toRealArray(provider.solve(provider.qr(A), bVec));
            case SVD:
                // Pseudoinverse approach via provider
                Matrix<Real> pinv = provider.inverse(A);
                return toRealArray(provider.multiply(pinv, bVec));
            case CONJUGATE_GRADIENT: return solveCG(A, b);
            case BICGSTAB: return solveBiCGSTAB(A, b);
            case GMRES: return solveGMRES(A, b);
            default:
                return toRealArray(provider.solve(A, bVec));
        }
    }

    private static Real[] toRealArray(Vector<Real> vec) {
        int n = vec.dimension();
        Real[] res = new Real[n];
        for (int i = 0; i < n; i++) res[i] = vec.get(i);
        return res;
    }

    /**
     * Recommends the best solver strategy for the given matrix.
     */
    public static Strategy recommend(Matrix<Real> A) {
        int m = A.rows();
        int n = A.cols();

        if (A instanceof SparseMatrix && m * n > ITERATIVE_SIZE_THRESHOLD * ITERATIVE_SIZE_THRESHOLD) {
            SparseMatrix<Real> sparse = (SparseMatrix<Real>) A;
            double density = (double) sparse.getNnz() / (m * n);
            if (density < 0.1) {
                return isSymmetric(A) ? Strategy.CONJUGATE_GRADIENT : Strategy.BICGSTAB;
            }
        }

        if (m != n) return m > n ? Strategy.QR : Strategy.SVD;
        if (isSymmetric(A)) return Strategy.CHOLESKY;
        return Strategy.LU;
    }

    public static boolean isSymmetric(Matrix<Real> A) {
        if (A.rows() != A.cols()) return false;
        int n = A.rows();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (A.get(i, j).subtract(A.get(j, i)).abs().compareTo(EPSILON) > 0) return false;
            }
        }
        return true;
    }

    private static Real[] solveCG(Matrix<Real> A, Real[] b) {
        Real[] x0 = new Real[b.length];
        java.util.Arrays.fill(x0, Real.ZERO);
        return org.episteme.core.mathematics.linearalgebra.matrices.solvers.sparse.ConjugateGradient.solve(
                A, b, x0, EPSILON, b.length * 2);
    }

    private static Real[] solveBiCGSTAB(Matrix<Real> A, Real[] b) {
        Real[] x0 = new Real[b.length];
        java.util.Arrays.fill(x0, Real.ZERO);
        return org.episteme.core.mathematics.linearalgebra.matrices.solvers.sparse.BiCGSTAB.solve(
                A, b, x0, EPSILON, b.length * 2);
    }

    private static Real[] solveGMRES(Matrix<Real> A, Real[] b) {
        Real[] x0 = new Real[b.length];
        java.util.Arrays.fill(x0, Real.ZERO);
        return org.episteme.core.mathematics.linearalgebra.matrices.solvers.sparse.GMRES.solve(
                A, b, x0, EPSILON, Math.min(50, b.length), 5);
    }
}
