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

package org.jscience.mathematics.statistics.montecarlo;

import java.util.Random;
import java.util.function.Function;

/**
 * Monte Carlo integration methods.
 * <p>
 * Uses random sampling to estimate integrals. Particularly effective
 * for high-dimensional integrals where grid methods fail.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MonteCarloIntegration {

    private final Random random;

    public MonteCarloIntegration() {
        this.random = new Random();
    }

    public MonteCarloIntegration(long seed) {
        this.random = new Random(seed);
    }

    /**
     * 1D Monte Carlo integration: Ã¢Ë†Â«[a,b] f(x) dx.
     * 
     * @param f       Function to integrate
     * @param a       Lower bound
     * @param b       Upper bound
     * @param samples Number of random samples
     * @return Estimated integral value
     */
    public double integrate1D(Function<Double, Double> f, double a, double b, int samples) {
        double sum = 0;
        double width = b - a;

        for (int i = 0; i < samples; i++) {
            double x = a + random.nextDouble() * width;
            sum += f.apply(x);
        }

        return (sum / samples) * width;
    }

    /**
     * Multi-dimensional Monte Carlo integration over a hypercube.
     * 
     * @param f       Function to integrate (takes double[] point)
     * @param lower   Lower bounds for each dimension
     * @param upper   Upper bounds for each dimension
     * @param samples Number of random samples
     * @return Estimated integral value
     */
    public double integrateND(Function<double[], Double> f, double[] lower, double[] upper, int samples) {
        int dim = lower.length;
        double volume = 1.0;
        for (int i = 0; i < dim; i++) {
            volume *= (upper[i] - lower[i]);
        }

        double sum = 0;
        double[] point = new double[dim];

        for (int i = 0; i < samples; i++) {
            for (int d = 0; d < dim; d++) {
                point[d] = lower[d] + random.nextDouble() * (upper[d] - lower[d]);
            }
            sum += f.apply(point);
        }

        return (sum / samples) * volume;
    }

    /**
     * Importance sampling Monte Carlo.
     * <p>
     * Uses a proposal distribution to reduce variance.
     * </p>
     * 
     * @param f        Function to integrate
     * @param proposal Proposal PDF q(x) from which we sample
     * @param sampler  Samples from proposal distribution
     * @param samples  Number of samples
     * @return Estimated integral of f(x) * target(x) / q(x) with respect to q(x)
     */
    public double importanceSampling(
            Function<Double, Double> f,
            Function<Double, Double> proposal,
            java.util.function.Supplier<Double> sampler,
            int samples) {

        double sum = 0;
        for (int i = 0; i < samples; i++) {
            double x = sampler.get();
            double q = proposal.apply(x);
            if (q > 0) {
                sum += f.apply(x) / q;
            }
        }
        return sum / samples;
    }

    /**
     * Estimate integral with variance (for error bounds).
     * 
     * @param f       Function to integrate
     * @param a       Lower bound
     * @param b       Upper bound
     * @param samples Number of samples
     * @return [estimate, standard_error]
     */
    public double[] integrate1DWithError(Function<Double, Double> f, double a, double b, int samples) {
        double sum = 0;
        double sumSq = 0;
        double width = b - a;

        for (int i = 0; i < samples; i++) {
            double x = a + random.nextDouble() * width;
            double fx = f.apply(x);
            sum += fx;
            sumSq += fx * fx;
        }

        double mean = sum / samples;
        double variance = (sumSq / samples) - (mean * mean);
        double estimate = mean * width;
        double stdError = width * Math.sqrt(variance / samples);

        return new double[] { estimate, stdError };
    }
}


