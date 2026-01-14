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
 * This class models a basic discrete distribution on a finite set of
 * points, with specified probabilities.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class FiniteDistribution extends Distribution implements Serializable {
    /** DOCUMENT ME! */
    private double lowerValue;

    /** DOCUMENT ME! */
    private double upperValue;

    /** DOCUMENT ME! */
    private double width;

    /** DOCUMENT ME! */
    private int size;

    /** DOCUMENT ME! */
    private double[] probabilities;

/**
     * This general constructor creates a new finite distribution on a finite
     * set of points with a specified array of probabilities.
     *
     * @param a the lower value of the domain
     * @param b the upper value of the domain
     * @param w the step size of the domain
     * @param p the array of probabilities
     */
    public FiniteDistribution(double a, double b, double w, double[] p) {
        setParameters(a, b, w, p);
    }

/**
     * This special constructor creates the uniform distribuiton on the finite
     * set of points.
     *
     * @param a the lower value of the domain
     * @param b the upper value of the domain
     * @param w the step size of the domain
     */
    public FiniteDistribution(double a, double b, double w) {
        setParameters(a, b, w);
    }

/**
     * This special constructor creates a new uniform distribution on {1, 2...,
     * 6}.
     */
    public FiniteDistribution() {
        this(1, 6, 1);
    }

    /**
     * This method sets the parameters: the domain and the
     * probabilities.
     *
     * @param a the lower value of the domain
     * @param b the upper value of the domain
     * @param w the step size of the domain
     * @param p the array of probabilities
     */
    public void setParameters(double a, double b, double w, double[] p) {
        setDomain(a, b, w);

        if (p.length != size) {
            probabilities = new double[size];

            for (int i = 0; i < size; i++)
                probabilities[i] = 1.0 / size;
        } else {
            probabilities = StatisticsUtils.getProbabilities(p);
        }
    }

    /**
     * This method sets the domain parameters. The probability array
     * corresponds to the uniform distribution.
     *
     * @param a the lower value of the domain
     * @param b the upper value of the domain
     * @param w the step size of the domain
     */
    public void setParameters(double a, double b, double w) {
        setDomain(a, b, w);
        probabilities = new double[size];

        for (int i = 0; i < size; i++)
            probabilities[i] = 1.0 / size;
    }

    /**
     * This method sets the domain and assigns the lower value, upper
     * value and width.
     *
     * @param a the lower value of the domain
     * @param b the upper value of the domain
     * @param w the width of the domain
     */
    private void setDomain(double a, double b, double w) {
        super.setDomain(a, b, w, DISCRETE);

        DistributionDomain domain = getDomain();
        lowerValue = domain.getLowerValue();
        upperValue = domain.getUpperValue();
        width = domain.getWidth();
        size = domain.getSize();
    }

    /**
     * This method computes the probability density function.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the probability density at x
     */
    public double getDensity(double x) {
        int j = getDomain().getIndex(x);

        if ((0 <= j) & (j < size)) {
            return probabilities[j];
        } else {
            return 0;
        }
    }

    /**
     * This method sets the probabilities. Error correction is
     * performed to ensure that the probabilities are nonnegative and sum to
     * 1.
     *
     * @param p the array of probabilities
     */
    public void setProbabilities(double[] p) {
        setParameters(lowerValue, upperValue, width, p);
    }

    /**
     * This method sets an individual probability. The other
     * probabilities would be rescaled appropriately, so that the sum is still
     * 1.
     *
     * @param i the index
     * @param p the probability
     */
    public void setProbabilities(int i, double p) {
        if (i < 0) {
            i = 0;
        } else if (i > (size - 1)) {
            i = size - 1;
        }

        probabilities[i] = p;
        setParameters(lowerValue, upperValue, width, probabilities);
    }

    /**
     * This method gets the probability for a specified index.
     *
     * @param i the index
     *
     * @return the probability associated with the index
     */
    public double getProbabilities(int i) {
        if (i < 0) {
            i = 0;
        } else if (i >= size) {
            i = size - 1;
        }

        return probabilities[i];
    }

    /**
     * This method gets the entire array of probabilities.
     *
     * @return the array of probabilities
     */
    public double[] getProbabilities() {
        return probabilities;
    }

    /**
     * This method sets the lower value.
     *
     * @param a the lower value
     */
    public void setLowerValue(double a) {
        setParameters(a, upperValue, width, probabilities);
    }

    /**
     * This method returns the lower value.
     *
     * @return the lower value
     */
    public double getLowerValue() {
        return lowerValue;
    }

    /**
     * This method sets the upper value.
     *
     * @param b the upper value
     */
    public void setUpperValue(double b) {
        setParameters(lowerValue, b, width, probabilities);
    }

    /**
     * This method returns the lower value.
     *
     * @return the lower value
     */
    public double getUpperValue() {
        return upperValue;
    }

    /**
     * This method sets the step size.
     *
     * @param w the width
     */
    public void setWidth(double w) {
        setParameters(lowerValue, upperValue, w, probabilities);
    }

    /**
     * This method returns the width.
     *
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * This method returns the size (the number of values).
     *
     * @return the number of values
     */
    public int getSize() {
        return size;
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Finite distribution [lower value = " + lowerValue +
        ", upper value = " + upperValue + ", width = " + width +
        ", probabilities = " + probabilities + "]";
    }
}
