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
 * This class models the distribution of the order statistic of a specified
 * order from a random sample of a specified size from a specified sampling
 * distribution.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class OrderStatisticDistribution extends Distribution
    implements Serializable {
    /** DOCUMENT ME! */
    private Distribution distribution;

    /** DOCUMENT ME! */
    private int sampleSize;

    /** DOCUMENT ME! */
    private int order;

/**
     * This general constructor creates a new order statistic distribution
     * corresponding to a specified sampling distribution, sample size, and
     * order.
     *
     * @param d the probability distribution
     * @param n the sample size
     * @param k the order
     */
    public OrderStatisticDistribution(Distribution d, int n, int k) {
        setParameters(d, n, k);
    }

/**
     * This default constructor creates a new order statistic distribution with
     * the uniform distribution, sample size n = 2, and order k = 1.
     */
    public OrderStatisticDistribution() {
        this(new ContinuousUniformDistribution(), 2, 1);
    }

    /**
     * This method sets the parameters: the sampling distribution,
     * sample size, and order.
     *
     * @param d the probability distribution
     * @param n the sample size
     * @param k the order
     */
    public void setParameters(Distribution d, int n, int k) {
        //Correct for invalid parameters
        if (n < 1) {
            n = 1;
        }

        if (k < 1) {
            k = 1;
        } else if (k > n) {
            k = n;
        }

        //Assign parameters
        distribution = d;
        sampleSize = n;
        order = k;
        setDomain(distribution.getDomain());
    }

    /**
     * This method computes the probability density function.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        double p = distribution.getCDF(x);

        if (distribution.getType() == DISCRETE) {
            return getCDF(x) - getCDF(x - getDomain().getWidth());
        } else {
            return order * StatisticsUtils.combinations(sampleSize, order) * Math.pow(p,
                order - 1) * Math.pow(1 - p, sampleSize - order) * distribution.getDensity(x);
        }
    }

    /**
     * This method computes the cumulative distribution function.
     *
     * @param x a number in the domain of x
     *
     * @return the cumulative probability at x
     */
    public double getCDF(double x) {
        double sum = 0;
        double p = distribution.getCDF(x);

        for (int j = order; j <= sampleSize; j++)
            sum = sum +
                (StatisticsUtils.combinations(sampleSize, j) * Math.pow(p, j) * Math.pow(1 -
                    p, sampleSize - j));

        return sum;
    }

    /**
     * This method sets the sampling distribution.
     *
     * @param d the sampling distribution
     */
    public void setDistribution(Distribution d) {
        setParameters(d, sampleSize, order);
    }

    /**
     * This method returns the sampling distribution.
     *
     * @return the sampling distribution
     */
    public Distribution getDistribution() {
        return distribution;
    }

    /**
     * This method sets the sample size.
     *
     * @param n the sample size
     */
    public void setSampleSize(int n) {
        setParameters(distribution, n, order);
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
     * This method sets the order.
     *
     * @param k the order
     */
    public void setOrder(int k) {
        setParameters(distribution, sampleSize, k);
    }

    /**
     * This method returns the order
     *
     * @return DOCUMENT ME!
     */
    public int getOrder() {
        return order;
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Order statistic distribution [sampling distribution = " +
        distribution + ", sample size = " + sampleSize + "order + " + order +
        "]";
    }
}
