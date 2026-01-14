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
 * This class applies a location-scale tranformation to a given
 * distribution. In terms of the corresponding random variable X, the
 * transformation is Y = a + bX.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class LocationScaleDistribution extends Distribution
    implements Serializable {
    /** DOCUMENT ME! */
    private Distribution distribution;

    /** DOCUMENT ME! */
    private double location;

    /** DOCUMENT ME! */
    private double scale;

/**
     * This general constructor creates a new location-scale transformation on
     * a given distribuiton with given location and scale parameters.
     *
     * @param d the distribution
     * @param a the location parameter
     * @param b the scale parameter
     */
    public LocationScaleDistribution(Distribution d, double a, double b) {
        setParameters(d, a, b);
    }

/**
     * This default constructor creates a new location-scale distribution on
     * the normal distribution with location parameter 0 and scale parameter
     * 1. Of course, this is simply the standard normal distribution.
     */
    public LocationScaleDistribution() {
        this(new NormalDistribution(), 0, 1);
    }

    /**
     * This method sets the parameters, the distribution and the
     * location and scale parameters, and sets up the domain.
     *
     * @param d the distribution
     * @param a the location parameter
     * @param b the scale parameter
     */
    public void setParameters(Distribution d, double a, double b) {
        distribution = d;
        location = a;
        scale = b;

        DistributionDomain domain = distribution.getDomain();
        double l;
        double u;
        double w = domain.getWidth();
        int t = distribution.getType();

        if (t == DISCRETE) {
            l = domain.getLowerValue();
            u = domain.getUpperValue();
        } else {
            l = domain.getLowerBound();
            u = domain.getUpperBound();
        }

        if (scale == 0) {
            setDomain(location, location, 1, DISCRETE);
        } else if (scale < 0) {
            setDomain(location + (scale * u), location + (scale * l),
                -scale * w, t);
        } else {
            setDomain(location + (scale * l), location + (scale * u),
                scale * w, t);
        }
    }

    /**
     * This method computes the probability density function of the
     * location-scale distribution in terms of the location and scale
     * parameters and the density function of the given distribution.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the probability density function of x.
     */
    public double getDensity(double x) {
        if (getType() == DISCRETE) {
            if (scale == 0) {
                if (x == location) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return distribution.getDensity((x - location) / scale);
            }
        } else {
            return distribution.getDensity((x - location) / scale) * Math.abs(1 / scale);
        }
    }

    /**
     * This method returns the maximum value of the probability density
     * function of the location-scale distribution, which is the same as the
     * maximum value of the probability density function of the given
     * distribution.
     *
     * @return the maximum value of the probability density function
     */
    public double getMaxDensity() {
        return distribution.getMaxDensity();
    }

    /**
     * This mtehod computes the mean of the location-scale distribution
     * in terms of the mean of the given distribution and the location and
     * scale parameters.
     *
     * @return the mean of the distribution
     */
    public double getMean() {
        return location + (scale * distribution.getMean());
    }

    /**
     * This method returns the variance of the location-scale
     * distribution in terms of the given distribution and the location and
     * scale parameters.
     *
     * @return the variance of the distribution
     */
    public double getVariance() {
        return (scale * scale) * distribution.getVariance();
    }

    /**
     * This method returns a simulated value from the location-scale
     * distribution in terms of the given distribution and the location and
     * scale parameters.
     *
     * @return DOCUMENT ME!
     */
    public double simulate() {
        return location + (scale * distribution.simulate());
    }

    /**
     * This method returns the cumulative distribution function of the
     * location- scale distribution in terms of the CDF of the given
     * distribution and the location and scale parameters.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the cumulative probability at x
     */
    public double getCDF(double x) {
        if (scale > 0) {
            return distribution.getCDF((x - location) / scale);
        } else {
            return 1 - distribution.getCDF((x - location) / scale);
        }
    }

    /**
     * This method returns the quantile function of the location-scale
     * distribution in terms of the quantile function of the given
     * distribution and the location and scale parameters.
     *
     * @param p a probability in (0, 1)
     *
     * @return the quanitle of order p
     */
    public double getQuantile(double p) {
        if (scale > 0) {
            return location + (scale * distribution.getQuantile(p));
        } else {
            return location + (scale * distribution.getQuantile(1 - p));
        }
    }

    /**
     * This method sets the location parameter.
     *
     * @param a the location parameter
     */
    public void setLocation(double a) {
        setParameters(distribution, a, scale);
    }

    /**
     * This method returns the location parameter.
     *
     * @return the location parameter
     */
    public double getLocation() {
        return location;
    }

    /**
     * This method sets the scale parameter.
     *
     * @param b the scale parameter
     */
    public void setScale(double b) {
        setParameters(distribution, location, b);
    }

    /**
     * This method gets the scale parameter
     *
     * @return the scale parameter
     */
    public double getScale() {
        return scale;
    }

    /**
     * This method sets the distribution to be moved and scaled.
     *
     * @param d the distribution
     */
    public void setDistribution(Distribution d) {
        setParameters(d, location, scale);
    }

    /**
     * This method gets the underlying distribution that is being moved
     * and scaled.
     *
     * @return the distribution
     */
    public Distribution getDistribution() {
        return distribution;
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Location-scale distribution [basic distribution = " +
        distribution + ", location = " + location + ", scale = " + scale + "]";
    }
}
