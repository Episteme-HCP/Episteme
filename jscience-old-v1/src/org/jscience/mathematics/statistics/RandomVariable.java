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

import org.jscience.util.Named;


/**
 * This class models a random variable in terms of a distribution and an
 * interval dataset. The dataset holds a random sample from the distribution.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class RandomVariable implements Named {
    /** DOCUMENT ME! */
    private Distribution distribution;

    /** DOCUMENT ME! */
    private IntervalData intervalData;

    /** DOCUMENT ME! */
    private String name;

/**
     * This general constructor creates a new random variable with a specified
     * probability distribution and name.
     *
     * @param d the probability distribution
     * @param n the name of the variable
     */
    public RandomVariable(Distribution d, String n) {
        distribution = d;
        name = n;
        intervalData = new IntervalData(distribution.getDomain(), name);
    }

/**
     * This special constructor creates a new random variable with a specified
     * probability distribution and the default name "X".
     *
     * @param d the probability distribution
     */
    public RandomVariable(Distribution d) {
        this(d, "X");
    }

/**
     * This default constructor creates a new random variable with a normal
     * distribution and with the default name "X".
     */
    public RandomVariable() {
        this(new NormalDistribution());
    }

    /**
     * This method assigns the probability distribution and create a
     * corresponding interval data distribution.
     *
     * @param d the probability distribution
     */
    public void setDistribution(Distribution d) {
        distribution = d;
        intervalData.setDomain(distribution.getDomain());
    }

    /**
     * This method gets the probability distribution.
     *
     * @return the probability distribution
     */
    public Distribution getDistribution() {
        return distribution;
    }

    /**
     * Get the data distribution.
     *
     * @return the interval data distribution
     */
    public IntervalData getIntervalData() {
        return intervalData;
    }

    /**
     * This method assigns a value to the random variable.
     *
     * @param x DOCUMENT ME!
     */
    public void setValue(double x) {
        intervalData.setValue(x);
    }

    /**
     * This method gets the current value of the random variable.
     *
     * @return the current value of the random variable
     */
    public double getValue() {
        return intervalData.getValue();
    }

    /**
     * This method simulates a value of the probability distribution
     * and assigns the value to the data distribution.
     */
    public void sample() {
        intervalData.setValue(distribution.simulate());
    }

    /**
     * This method simulates a value of the probability distribution,
     * assigns the value to the data distribution, and returns the value.
     *
     * @return a simulated value from the distribution
     */
    public double simulate() {
        double x = distribution.simulate();
        intervalData.setValue(x);

        return x;
    }

    /**
     * This method resets the data distribution.
     */
    public void reset() {
        intervalData.setDomain(distribution.getDomain());
    }

    /**
     * This method gets the name of the random variable.
     *
     * @return the name of the variable
     */
    public String getName() {
        return name;
    }

    /**
     * This method assign a name to the random variable.
     *
     * @param n the name of the random variable
     */
    public void setName(String n) {
        name = n;
        intervalData.setName(name);
    }
}
