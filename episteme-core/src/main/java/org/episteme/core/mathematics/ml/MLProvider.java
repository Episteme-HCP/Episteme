package org.episteme.core.mathematics.ml;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Service Provider Interface for high-performance Machine Learning operations.
 */
public interface MLProvider extends AlgorithmProvider {

    int[] kMeans(float[][] data, int k, int maxIterations);
    float[][] pca(float[][] data, int nComponents);

    int[] kMeans(double[][] data, int k, int maxIterations);
    double[][] pca(double[][] data, int nComponents);

    int[] kMeans(Real[][] data, int k, int maxIterations);
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
