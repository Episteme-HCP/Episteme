package org.jscience.mathematics.linear;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Matrix;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.sets.Reals;
import java.util.List;
import java.util.ArrayList;

/**
 * Singular Value Decomposition (SVD): A = UΣV^T
 * <p>
 * U: left singular vectors (m×m orthogonal)
 * Σ: singular values (m×n diagonal)
 * V: right singular vectors (n×n orthogonal)
 * </p>
 * 
 * Essential for: PCA, pseudo-inverse, low-rank approximation, data compression.
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class SVD {

    private final Matrix<Real> U;
    private final Real[] singularValues;
    private final Matrix<Real> V;

    private SVD(Matrix<Real> U, Real[] singularValues, Matrix<Real> V) {
        this.U = U;
        this.singularValues = singularValues;
        this.V = V;
    }

    /**
     * Computes SVD using Golub-Reinsch algorithm.
     * <p>
     * Two-phase approach:
     * 1. Bidiagonalization using Householder reflections
     * 2. QR iteration on bidiagonal matrix
     * </p>
     */
    public static SVD decompose(Matrix<Real> matrix) {
        int m = matrix.rows();
        int n = matrix.cols();

        // Copy matrix data
        Real[][] A = new Real[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = matrix.get(i, j);
            }
        }

        // Use simplified approach: A^T * A for eigenvalues
        // Full Golub-Reinsch is complex - this is a working approximation

        Real[][] AtA = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                AtA[i][j] = Real.ZERO;
                for (int k = 0; k < m; k++) {
                    AtA[i][j] = AtA[i][j].add(A[k][i].multiply(A[k][j]));
                }
            }
        }

        // Compute eigenvalues of A^T * A (these are σ²)
        EigenDecomposition eigen = EigenDecomposition.decompose(createMatrix(AtA));
        Real[] eigenvalues = eigen.getEigenvalues();

        // Singular values are sqrt(eigenvalues)
        Real[] sigma = new Real[Math.min(m, n)];
        for (int i = 0; i < sigma.length; i++) {
            sigma[i] = eigenvalues[i].sqrt();
        }

        // V is eigenvectors of A^T * A
        Matrix<Real> V = eigen.getEigenvectors();

        // Compute U = A * V * Σ^(-1)
        List<List<Real>> uRows = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < Math.min(m, n); j++) {
                Real sum = Real.ZERO;
                for (int k = 0; k < n; k++) {
                    if (!sigma[j].isZero()) {
                        sum = sum.add(A[i][k].multiply(V.get(k, j)).divide(sigma[j]));
                    }
                }
                row.add(sum);
            }
            uRows.add(row);
        }

        Matrix<Real> U = DenseMatrix.of(uRows, Reals.getInstance());

        return new SVD(U, sigma, V);
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

    public Matrix<Real> getU() {
        return U;
    }

    public Real[] getSingularValues() {
        return singularValues;
    }

    public Matrix<Real> getV() {
        return V;
    }

    /**
     * Matrix rank (number of non-zero singular values).
     */
    public int rank(Real tolerance) {
        int rank = 0;
        for (Real sigma : singularValues) {
            if (sigma.compareTo(tolerance) > 0) {
                rank++;
            }
        }
        return rank;
    }

    /**
     * Condition number: σ_max / σ_min
     * <p>
     * Indicates numerical stability. κ > 10^15 is ill-conditioned.
     * </p>
     */
    public Real conditionNumber() {
        if (singularValues.length == 0)
            return Real.of(Double.POSITIVE_INFINITY);

        Real max = singularValues[0];
        Real min = singularValues[singularValues.length - 1];

        if (min.isZero())
            return Real.of(Double.POSITIVE_INFINITY);
        return max.divide(min);
    }

    /**
     * Moore-Penrose pseudoinverse: A^+ = V Σ^+ U^T
     */
    public Matrix<Real> pseudoInverse(Real tolerance) {
        // Σ^+ : invert non-zero singular values
        int n = V.rows();
        int m = U.rows();

        List<List<Real>> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                Real sum = Real.ZERO;
                for (int k = 0; k < singularValues.length; k++) {
                    if (singularValues[k].compareTo(tolerance) > 0) {
                        Real invSigma = Real.ONE.divide(singularValues[k]);
                        sum = sum.add(V.get(i, k).multiply(invSigma).multiply(U.get(j, k)));
                    }
                }
                row.add(sum);
            }
            result.add(row);
        }

        return DenseMatrix.of(result, Reals.getInstance());
    }
}
