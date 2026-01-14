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

package org.jscience.mathematics.statistics;

import java.io.Serializable;


/**
 * This class models the distribution of the number of distinct sample
 * values when a sample of a specified size is chosen with replacement from a
 * finite population of a specified size.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class BirthdayDistribution extends Distribution implements Serializable {
    /** DOCUMENT ME! */
    private int populationSize;

    /** DOCUMENT ME! */
    private int sampleSize;

    /** DOCUMENT ME! */
    private double[][] prob;

/**
     * This general constructor creates a new birthday distribution with a
     * specified population size and sample size.
     *
     * @param n the population size
     * @param k the sample size
     */
    public BirthdayDistribution(int n, int k) {
        setParameters(n, k);
    }

/**
     * This default constructor creates a new birthday distribution with
     * population size 365 (the classical birthday problem) and sample size
     * 20.
     */
    public BirthdayDistribution() {
        this(365, 20);
    }

    /**
     * This method sets the parameters: the population size and the
     * sample size. Also, the probabilities are computed and stored in an
     * array, and the domain computed.
     *
     * @param n the population size
     * @param k the sample size
     */
    public void setParameters(int n, int k) {
        //Correct for invalid parameters
        if (n < 1) {
            n = 1;
        }

        if (k < 1) {
            k = 1;
        }

        int upperIndex;
        populationSize = n;
        sampleSize = k;
        setDomain(1, Math.min(populationSize, sampleSize), 1, DISCRETE);
        prob = new double[sampleSize + 1][populationSize + 1];
        prob[0][0] = 1;
        prob[1][1] = 1;

        for (int j = 1; j < sampleSize; j++) {
            if (j < populationSize) {
                upperIndex = j + 1;
            } else {
                upperIndex = (int) populationSize;
            }

            for (int m = 1; m <= upperIndex; m++) {
                prob[j + 1][m] = (prob[j][m] * ((double) m / populationSize)) +
                    (prob[j][m - 1] * ((double) (populationSize - m + 1) / populationSize));
            }
        }
    }

    /**
     * This method computes the probability density function.
     *
     * @param x a number in the domain of the distribuiton
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        int m = (int) (Math.rint(x));

        return prob[sampleSize][m];
    }

    /**
     * This method computes the mean of the distribution in terms of
     * the parameters.
     *
     * @return the mean
     */
    public double getMean() {
        return populationSize * (1 -
        Math.pow(1 - (1.0 / populationSize), sampleSize));
    }

    /**
     * This method computes the variance of the distribution in terms
     * of the parameters.
     *
     * @return the variance
     */
    public double getVariance() {
        return ((populationSize * (populationSize - 1) * Math.pow(1 -
            (2.0 / populationSize), sampleSize)) +
        (populationSize * Math.pow(1 - (1.0 / populationSize), sampleSize))) -
        (populationSize * populationSize * Math.pow(1 - (1.0 / populationSize),
            2 * sampleSize));
    }

    /**
     * This method returns the population size.
     *
     * @return the population size
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
     * This method sets the population size.
     *
     * @param n the population size
     */
    public void setPopulationSize(int n) {
        setParameters(n, sampleSize);
    }

    /**
     * This method returns the sample size.
     *
     * @return the sample size
     */
    public int getSampleSize() {
        return sampleSize;
    }

    /**
     * This method sets the sample size.
     *
     * @param k the sample size
     */
    public void setSampleSize(int k) {
        setParameters(populationSize, k);
    }

    /**
     * This method simulates a value from the distribution. This is
     * done by simulating a random sample from the population, and computing
     * the number of distinct sample values.
     *
     * @return a simulated value from the distribution
     */
    public double simulate() {
        int[] count = new int[populationSize];
        double distinct = 0;

        for (int i = 1; i <= sampleSize; i++) {
            int j = (int) (populationSize * Math.random());

            if (count[j] == 0) {
                distinct++;
            }

            count[j] = count[j]++;
        }

        return distinct;
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Birthday distribution [population size = " + populationSize +
        ", sample size = " + sampleSize + "]";
    }
}
