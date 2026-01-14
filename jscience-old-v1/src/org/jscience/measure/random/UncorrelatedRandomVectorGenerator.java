/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
