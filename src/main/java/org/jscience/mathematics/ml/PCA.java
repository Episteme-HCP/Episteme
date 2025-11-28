package org.jscience.mathematics.ml;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.linear.SVD;
import org.jscience.mathematics.vector.Matrix;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.sets.Reals;
import java.util.List;
import java.util.ArrayList;

/**
 * Principal Component Analysis (PCA) - Dimensionality reduction.
 * <p>
 * Finds principal components (directions of max variance) using SVD.
 * Used for: feature reduction, visualization, noise reduction.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class PCA {

    private Matrix<Real> components; // Principal components (eigenvectors)
    private Real[] explainedVariance; // Variance explained by each component
    private Real[] mean; // Feature means (for centering)

    /**
     * Fits PCA model to data.
     * 
     * @param data        n samples × d features
     * @param nComponents number of components to keep (≤ d)
     */
    public void fit(Real[][] data, int nComponents) {
        int n = data.length;
        int d = data[0].length;

        if (nComponents > d) {
            throw new IllegalArgumentException("nComponents must be ≤ number of features");
        }

        // 1. Center data (subtract mean)
        mean = new Real[d];
        for (int j = 0; j < d; j++) {
            mean[j] = Real.ZERO;
            for (int i = 0; i < n; i++) {
                mean[j] = mean[j].add(data[i][j]);
            }
            mean[j] = mean[j].divide(Real.of(n));
        }

        Real[][] centered = new Real[n][d];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d; j++) {
                centered[i][j] = data[i][j].subtract(mean[j]);
            }
        }

        // 2. Compute SVD of centered data
        Matrix<Real> X = createMatrix(centered);
        SVD svd = SVD.decompose(X);

        // 3. Principal components are right singular vectors (V)
        Matrix<Real> V = svd.getV();

        // Extract first nComponents columns
        List<List<Real>> componentRows = new ArrayList<>();
        for (int i = 0; i < d; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < nComponents; j++) {
                row.add(V.get(i, j));
            }
            componentRows.add(row);
        }
        components = DenseMatrix.of(componentRows, Reals.getInstance());

        // 4. Explained variance
        Real[] singularValues = svd.getSingularValues();
        explainedVariance = new Real[nComponents];
        Real totalVariance = Real.ZERO;

        for (int i = 0; i < Math.min(nComponents, singularValues.length); i++) {
            Real variance = singularValues[i].multiply(singularValues[i]).divide(Real.of(n - 1));
            explainedVariance[i] = variance;
            totalVariance = totalVariance.add(variance);
        }

        // Normalize to get proportion of variance explained
        for (int i = 0; i < explainedVariance.length; i++) {
            if (!totalVariance.isZero()) {
                explainedVariance[i] = explainedVariance[i].divide(totalVariance);
            }
        }
    }

    /**
     * Transforms data to principal component space.
     * 
     * @param data data to transform (same features as training data)
     * @return transformed data (n × nComponents)
     */
    public Real[][] transform(Real[][] data) {
        if (components == null) {
            throw new IllegalStateException("PCA not fitted yet");
        }

        int n = data.length;
        int d = data[0].length;
        int nComponents = components.cols();

        // Center data
        Real[][] centered = new Real[n][d];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d; j++) {
                centered[i][j] = data[i][j].subtract(mean[j]);
            }
        }

        // Project: X_transformed = X_centered * components
        Real[][] transformed = new Real[n][nComponents];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < nComponents; j++) {
                transformed[i][j] = Real.ZERO;
                for (int k = 0; k < d; k++) {
                    transformed[i][j] = transformed[i][j].add(
                            centered[i][k].multiply(components.get(k, j)));
                }
            }
        }

        return transformed;
    }

    /**
     * Inverse transform: reconstructs original space from PCA space.
     * <p>
     * Useful for visualization, noise filtering.
     * </p>
     */
    public Real[][] inverseTransform(Real[][] transformedData) {
        int n = transformedData.length;
        int nComponents = transformedData[0].length;
        int d = mean.length;

        // X_reconstructed = transformed * components^T + mean
        Real[][] reconstructed = new Real[n][d];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d; j++) {
                reconstructed[i][j] = mean[j];
                for (int k = 0; k < nComponents; k++) {
                    reconstructed[i][j] = reconstructed[i][j].add(
                            transformedData[i][k].multiply(components.get(j, k)));
                }
            }
        }

        return reconstructed;
    }

    /**
     * Returns explained variance ratio for each component.
     */
    public Real[] getExplainedVarianceRatio() {
        return explainedVariance;
    }

    /**
     * Returns total explained variance ratio (sum of all components).
     */
    public Real getTotalExplainedVariance() {
        Real total = Real.ZERO;
        for (Real var : explainedVariance) {
            total = total.add(var);
        }
        return total;
    }

    public Matrix<Real> getComponents() {
        return components;
    }

    private Matrix<Real> createMatrix(Real[][] data) {
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
