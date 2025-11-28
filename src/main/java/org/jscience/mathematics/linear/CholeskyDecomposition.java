package org.jscience.mathematics.linear;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Matrix;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.sets.Reals;
import java.util.List;
import java.util.ArrayList;

/**
 * Cholesky decomposition: A = LL^T for symmetric positive definite matrices.
 * <p>
 * More efficient than LU for SPD matrices: O(n³/3) vs O(2n³/3).
 * Numerically stable, preserves positive definiteness.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class CholeskyDecomposition {

    private final Matrix<Real> L; // Lower triangular

    private CholeskyDecomposition(Matrix<Real> L) {
        this.L = L;
    }

    /**
     * Computes Cholesky decomposition A = LL^T.
     * <p>
     * Algorithm: L[i][j] = (A[i][j] - Σ L[i][k]*L[j][k]) / L[j][j]
     * </p>
     * 
     * @param matrix symmetric positive definite matrix
     * @return Cholesky decomposition
     * @throws IllegalArgumentException if matrix not SPD
     */
    public static CholeskyDecomposition decompose(Matrix<Real> matrix) {
        int n = matrix.rows();

        if (n != matrix.cols()) {
            throw new IllegalArgumentException("Matrix must be square");
        }

        Real[][] L = new Real[n][n];

        // Initialize to zero
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                L[i][j] = Real.ZERO;
            }
        }

        // Cholesky-Banachiewicz algorithm
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                Real sum = Real.ZERO;

                if (j == i) {
                    // Diagonal elements
                    for (int k = 0; k < j; k++) {
                        sum = sum.add(L[j][k].multiply(L[j][k]));
                    }

                    Real diag = matrix.get(j, j).subtract(sum);
                    if (diag.compareTo(Real.ZERO) <= 0) {
                        throw new IllegalArgumentException(
                                "Matrix is not positive definite (diagonal[" + j + "] = " + diag + ")");
                    }
                    L[j][j] = diag.sqrt();
                } else {
                    // Off-diagonal elements
                    for (int k = 0; k < j; k++) {
                        sum = sum.add(L[i][k].multiply(L[j][k]));
                    }
                    L[i][j] = matrix.get(i, j).subtract(sum).divide(L[j][j]);
                }
            }
        }

        return new CholeskyDecomposition(createMatrix(L));
    }

    /**
     * Solves Ax = b using Cholesky decomposition.
     * <p>
     * Two-step process:
     * 1. Solve Ly = b (forward substitution)
     * 2. Solve L^T x = y (backward substitution)
     * </p>
     */
    public Real[] solve(Real[] b) {
        int n = b.length;
        Real[] y = new Real[n];
        Real[] x = new Real[n];

        // Forward substitution: Ly = b
        for (int i = 0; i < n; i++) {
            Real sum = Real.ZERO;
            for (int j = 0; j < i; j++) {
                sum = sum.add(L.get(i, j).multiply(y[j]));
            }
            y[i] = b[i].subtract(sum).divide(L.get(i, i));
        }

        // Backward substitution: L^T x = y
        for (int i = n - 1; i >= 0; i--) {
            Real sum = Real.ZERO;
            for (int j = i + 1; j < n; j++) {
                sum = sum.add(L.get(j, i).multiply(x[j]));
            }
            x[i] = y[i].subtract(sum).divide(L.get(i, i));
        }

        return x;
    }

    /**
     * Computes determinant: det(A) = (∏ L[i][i])²
     */
    public Real determinant() {
        Real prod = Real.ONE;
        for (int i = 0; i < L.rows(); i++) {
            prod = prod.multiply(L.get(i, i));
        }
        return prod.multiply(prod);
    }

    /**
     * Computes matrix inverse using Cholesky.
     */
    public Matrix<Real> inverse() {
        int n = L.rows();
        List<List<Real>> inv = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Real[] ei = new Real[n];
            for (int j = 0; j < n; j++) {
                ei[j] = (i == j) ? Real.ONE : Real.ZERO;
            }

            Real[] col = solve(ei);
            List<Real> colList = new ArrayList<>();
            for (Real val : col) {
                colList.add(val);
            }
            inv.add(colList);
        }

        // Transpose result
        List<List<Real>> invT = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(inv.get(j).get(i));
            }
            invT.add(row);
        }

        return DenseMatrix.of(invT, Reals.getInstance());
    }

    public Matrix<Real> getL() {
        return L;
    }

    /**
     * Returns L^T (upper triangular).
     */
    public Matrix<Real> getLT() {
        int n = L.rows();
        List<List<Real>> lt = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(L.get(j, i));
            }
            lt.add(row);
        }

        return DenseMatrix.of(lt, Reals.getInstance());
    }

    private static Matrix<Real> createMatrix(Real[][] data) {
        List<List<Real>> rows = new ArrayList<>();
        for (Real[] row : data) {
            List<Real> rowList = new ArrayList<>();
            for (Real val : row) {
                rowList.add(val);
            }
            rows.add(rowList);
        }
        return DenseMatrix.of(rows, Reals.getInstance());
    }
}
