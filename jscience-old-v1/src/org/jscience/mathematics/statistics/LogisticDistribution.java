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
 * This class models the logistic distribution.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class LogisticDistribution extends Distribution implements Serializable {
/**
     * This default constructor creates a new logsitic distribution and sets up
     * the default computational domain (&minus;7, 7).
     */
    public LogisticDistribution() {
        setDomain(-7, 7, 0.14, CONTINUOUS);
    }

    /**
     * This method computes the probability density function,which is
     * f(x) = e<sup>x</sup> / (1 + e<sup>x</sup>)<sup>2</sup>
     *
     * @param x a real number
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        double e = Math.exp(x);

        return e / ((1 + e) * (1 + e));
    }

    /**
     * This method computes the maximum value of the density function,
     * which is 1/4, the value of the density at x = 0.
     *
     * @return the maximum value of the density function
     */
    public double getMaxDensity() {
        return 0.25;
    }

    /**
     * This method computes the cumulative distribution function, which
     * is F(x) = e<sup>x</sup> / (1 + e<sup>x</sup>)
     *
     * @param x a real number
     *
     * @return the cumulative probability at x
     */
    public double getCDF(double x) {
        double e = Math.exp(x);

        return e / (1 + e);
    }

    /**
     * This method comptues the quantile function.
     *
     * @param p a probability in (0, 1)
     *
     * @return the quantile of order p
     */
    public double getQuantile(double p) {
        return Math.log(p / (1 - p));
    }

    /**
     * This method returns the mean of the distribution, which is 0.
     *
     * @return the mean
     */
    public double getMean() {
        return 0;
    }

    /**
     * This method computes the variance of the distribution, which is
     * &pi;<sup>2</sup> / 3.
     *
     * @return the variance method in Distribution.
     */
    public double getVariance() {
        return (Math.PI * Math.PI) / 3;
    }

    /**
     * This method computes the moment generating function, which is
     * &Gamma;(1 + t) / &Gamma;(1 &minus; t).
     *
     * @param t a number &lt; 1
     *
     * @return the moment generating function at t
     */
    public double getMGF(double t) {
        return StatisticsUtils.gamma(1 + t) * StatisticsUtils.gamma(1 - t);
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Logistic distribution";
    }
}
