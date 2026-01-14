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
 * This class defines the standard exponential distribution with a
 * specified rate parameter.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class ExponentialDistribution extends GammaDistribution
    implements Serializable {
    //Parameter
    /** DOCUMENT ME! */
    double rate;

/**
     * This general constructor creates a new exponential distribution with a
     * specified rate.
     *
     * @param r the rate
     */
    public ExponentialDistribution(double r) {
        setRate(r);
    }

/**
     * This default constructor creates a new exponential distribution with
     * rate 1.
     */
    public ExponentialDistribution() {
        this(1);
    }

    /**
     * This method sets the rate parameter.
     *
     * @param r the rate
     */
    public void setRate(double r) {
        if (r <= 0) {
            r = 1;
        }

        rate = r;
        super.setParameters(1, 1 / rate);
    }

    /**
     * This method gets the rate.
     *
     * @return the rate
     */
    public double getRate() {
        return 1 / getScale();
    }

    /**
     * This method computes the probability denstiy function.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        if (x < 0) {
            return 0;
        } else {
            return rate * Math.exp(-rate * x);
        }
    }

    /**
     * This method defines the cumulative distribution function.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the cumulative probability at x
     */
    public double getCDF(double x) {
        return 1 - Math.exp(-rate * x);
    }

    /**
     * The method computes the quantile function.
     *
     * @param p a probability in (0, 1)
     *
     * @return the quantile of order p
     */
    public double getQuantile(double p) {
        return -Math.log(1 - p) / rate;
    }

    /**
     * This method computes the moment of order n.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMoment(int n) {
        return StatisticsUtils.factorial(n) / Math.pow(rate, n);
    }

    /**
     * This method sets the gamma shape parameter, which must be 1.
     *
     * @param k the shape parameter
     */
    public void setShape(double k) {
        super.setShape(1);
    }

    /**
     * This method sets the gamma shape and scale parameters. The shape
     * parameter must be 1.
     *
     * @param k the shape parameter
     * @param b the scale parameter
     */
    public void setParameters(double k, double b) {
        super.setParameters(1, b);
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Exponential distribution [rate = " + rate + "]";
    }
}
