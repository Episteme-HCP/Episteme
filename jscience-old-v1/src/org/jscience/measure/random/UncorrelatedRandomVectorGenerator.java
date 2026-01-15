package org.jscience.measure.random;

import java.io.Serializable;


/**
 * This class allows to generate random vectors with uncorrelated
 * components.
 *
 * @author L. Maisonobe
 * @version $Id: UncorrelatedRandomVectorGenerator.java,v 1.3 2007-10-23 18:20:17 virtualcall Exp $
 */
public class UncorrelatedRandomVectorGenerator implements Serializable,
    RandomVectorGenerator {
    /** Mean vector. */
    private double[] mean;

    /** Standard deviation vector. */
    private double[] standardDeviation;

    /** Underlying scalar generator. */
    RandomGenerator generator;

    /** Storage for the random vector. */
    private double[] random;

/**
     * Simple constructor.
     * <p/>
     * <p/>
     * Build an uncorrelated random vector generator from its mean and standard
     * deviation vectors.
     * </p>
     *
     * @param mean              expected mean values for all components
     * @param standardDeviation standard deviation for all components
     * @param generator         underlying generator for uncorrelated normalized
     *                          components
     * @throws IllegalArgumentException if there is a dimension mismatch
     *                                  between the mean and standard deviation vectors
     */
    public UncorrelatedRandomVectorGenerator(double[] mean,
        double[] standardDeviation, RandomGenerator generator) {
        if (mean.length != standardDeviation.length) {
            throw new IllegalArgumentException("dimension mismatch");
        }

        this.mean = mean;
        this.standardDeviation = standardDeviation;

        this.generator = generator;
        random = new double[mean.length];
    }

/**
     * Simple constructor.
     * <p/>
     * <p/>
     * Build a null mean random and unit standard deviation uncorrelated vector
     * generator
     * </p>
     *
     * @param dimension dimension of the vectors to generate
     * @param generator underlying generator for uncorrelated normalized
     *                  components
     */
    public UncorrelatedRandomVectorGenerator(int dimension,
        RandomGenerator generator) {
        mean = new double[dimension];
        standardDeviation = new double[dimension];

        for (int i = 0; i < dimension; ++i) {
            mean[i] = 0;
            standardDeviation[i] = 1;
        }

        this.generator = generator;
        random = new double[dimension];
    }

    /**
     * Get the underlying normalized components generator.
     *
     * @return underlying uncorrelated components generator
     */
    public RandomGenerator getGenerator() {
        return generator;
    }

    /**
     * Generate a correlated random vector.
     *
     * @return a random vector as an array of double. The generator
     *         <em>will</em> reuse the same array for each call, in order to
     *         save the allocation time, so the user should keep a copy by
     *         himself if he needs so.
     */
    public double[] nextVector() {
        for (int i = 0; i < random.length; ++i) {
            random[i] = mean[i] +
                (standardDeviation[i] * generator.nextDouble());
        }

        return random;
    }
}
