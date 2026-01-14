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

package org.jscience.mathematics.statistics.montecarlo;

import java.util.Random;
import java.util.function.Function;

/**
 * Metropolis-Hastings Markov Chain Monte Carlo (MCMC).
 * <p>
 * Samples from complex probability distributions. Essential for
 * statistical physics and Bayesian inference.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MetropolisHastings {

    private final Random random;

    public MetropolisHastings() {
        this.random = new Random();
    }

    public MetropolisHastings(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Run Metropolis-Hastings sampling.
     * 
     * @param logProbability Log probability of target distribution (unnormalized
     *                       OK)
     * @param proposal       Proposal distribution: given current state, generate
     *                       candidate
     * @param initial        Initial state
     * @param samples        Number of samples to generate
     * @param burnIn         Number of initial samples to discard
     * @return Array of samples from the target distribution
     */
    public double[] sample1D(
            Function<Double, Double> logProbability,
            Function<Double, Double> proposal,
            double initial,
            int samples,
            int burnIn) {

        double[] result = new double[samples];
        double current = initial;
        double currentLogP = logProbability.apply(current);

        int total = samples + burnIn;

        for (int i = 0; i < total; i++) {
            // Propose new state
            double candidate = proposal.apply(current);
            double candidateLogP = logProbability.apply(candidate);

            // Acceptance ratio (in log space)
            double logAlpha = candidateLogP - currentLogP;

            // Accept or reject
            if (Math.log(random.nextDouble()) < logAlpha) {
                current = candidate;
                currentLogP = candidateLogP;
            }

            // Record sample (after burn-in)
            if (i >= burnIn) {
                result[i - burnIn] = current;
            }
        }

        return result;
    }

    /**
     * Multi-dimensional Metropolis-Hastings.
     * 
     * @param logProbability Log probability of target distribution
     * @param proposalStd    Standard deviation for Gaussian random walk proposal
     * @param initial        Initial state vector
     * @param samples        Number of samples
     * @param burnIn         Burn-in period
     * @return 2D array [sample_index][dimension]
     */
    public double[][] sampleND(
            Function<double[], Double> logProbability,
            double[] proposalStd,
            double[] initial,
            int samples,
            int burnIn) {

        int dim = initial.length;
        double[][] result = new double[samples][dim];
        double[] current = initial.clone();
        double currentLogP = logProbability.apply(current);

        int total = samples + burnIn;

        for (int i = 0; i < total; i++) {
            // Propose: Gaussian random walk
            double[] candidate = new double[dim];
            for (int d = 0; d < dim; d++) {
                candidate[d] = current[d] + proposalStd[d] * random.nextGaussian();
            }

            double candidateLogP = logProbability.apply(candidate);
            double logAlpha = candidateLogP - currentLogP;

            if (Math.log(random.nextDouble()) < logAlpha) {
                current = candidate;
                currentLogP = candidateLogP;
            }

            if (i >= burnIn) {
                result[i - burnIn] = current.clone();
            }
        }

        return result;
    }

    /**
     * Compute mean of samples.
     */
    public static double mean(double[] samples) {
        double sum = 0;
        for (double s : samples)
            sum += s;
        return sum / samples.length;
    }

    /**
     * Compute variance of samples.
     */
    public static double variance(double[] samples) {
        double m = mean(samples);
        double sumSq = 0;
        for (double s : samples) {
            double diff = s - m;
            sumSq += diff * diff;
        }
        return sumSq / (samples.length - 1);
    }

    /**
     * Effective sample size (accounts for autocorrelation).
     */
    public static double effectiveSampleSize(double[] samples) {
        int n = samples.length;
        double[] acf = autocorrelation(samples, Math.min(n / 2, 100));

        double tau = 1.0;
        for (int i = 1; i < acf.length; i++) {
            if (acf[i] < 0.05)
                break;
            tau += 2 * acf[i];
        }

        return n / tau;
    }

    private static double[] autocorrelation(double[] samples, int maxLag) {
        int n = samples.length;
        double m = mean(samples);
        double var = variance(samples);

        double[] acf = new double[maxLag];
        for (int lag = 0; lag < maxLag; lag++) {
            double sum = 0;
            for (int i = 0; i < n - lag; i++) {
                sum += (samples[i] - m) * (samples[i + lag] - m);
            }
            acf[lag] = sum / ((n - lag) * var);
        }
        return acf;
    }
}


