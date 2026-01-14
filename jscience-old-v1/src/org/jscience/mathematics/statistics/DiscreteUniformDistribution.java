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
 * This class models the discrete uniform distribution on a finite set.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class DiscreteUniformDistribution extends FiniteDistribution
    implements Serializable {
/**
     * This general constructor creates a new discrete uniform distribution on
     * a specified domain.
     *
     * @param a the lower value of the domain
     * @param b the upper value of the domain
     * @param w the  step size of the domain
     */
    public DiscreteUniformDistribution(double a, double b, double w) {
        super(a, b, w);
    }

/**
     * This default constructor creates a new discrete uniform distribution on
     * {1, 2, 3, 4, 5, 6}.
     */
    public DiscreteUniformDistribution() {
        this(1, 6, 1);
    }

    /**
     * This method simulates a value from the distribution.
     *
     * @return a simulated value from the distribution
     */
    public double simulate() {
        DistributionDomain d = getDomain();
        double a = d.getLowerValue();
        double b = d.getUpperValue();
        double x = a + (Math.random() * (b - a));

        return d.getNearestValue(x);
    }

    /**
     * This method sets the probabilities to ensure that the uniform
     * distribution is not changed.
     *
     * @param p the array of probabilities
     */
    public void setProbabilities(double[] p) {
    }

    /**
     * This method sets the finite distribution parameters to ensure
     * that the uniform distribution is not changed.
     *
     * @param a the lower value
     * @param b the upper value
     * @param w the width
     * @param p the array of probabilities
     */
    public void setParameters(double a, double b, double w, double[] p) {
        setParameters(a, b, w);
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Discrete uniform distribution [domain = " + getDomain() + "]";
    }
}
