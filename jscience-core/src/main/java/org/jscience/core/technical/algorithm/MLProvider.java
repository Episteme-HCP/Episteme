package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Service Provider Interface for high-performance Machine Learning operations.
 */
public interface MLProvider extends AlgorithmProvider {

    /**
     * Performs K-Means clustering using double precision.
     * @param data double[][] of shape (samples, features)
     * @param k number of clusters
     * @param maxIterations maximum iterations
     * @return int[] of cluster assignments
     */
    int[] kMeans(double[][] data, int k, int maxIterations);

    /**
     * Performs Principal Component Analysis (PCA) using double precision.
     * @param data double[][] of shape (samples, features)
     * @param nComponents number of components to keep
     * @return double[][] of shape (samples, nComponents) - transformed data
     */
    double[][] pca(double[][] data, int nComponents);
    
    /**
     * Performs K-Means clustering using Real precision.
     * @param data Real[][] of shape (samples, features)
     * @param k number of clusters
     * @param maxIterations maximum iterations
     * @return int[] of cluster assignments
     */
    int[] kMeans(Real[][] data, int k, int maxIterations);

    /**
     * Performs Principal Component Analysis (PCA) using Real precision.
     * @param data Real[][] of shape (samples, features)
     * @param nComponents number of components to keep
     * @return Real[][] of shape (samples, nComponents) - transformed data
     */
    Real[][] pca(Real[][] data, int nComponents);
    
    @Override
    default String getName() {
        return "ML Provider";
    }
    
    @Override
    default String getAlgorithmType() {
        return "Machine Learning";
    }
}
