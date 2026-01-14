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
 * This class defines the chi-square distribution with a specifed degrees
 * of freedom parameter.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class ChiSquareDistribution extends GammaDistribution
    implements Serializable {
    /** DOCUMENT ME! */
    private int degrees;

/**
     * This general constructor creates a new chi-square distribuiton with a
     * specified degrees of freedom parameter.
     *
     * @param n the degrees of freedom
     */
    public ChiSquareDistribution(int n) {
        setDegrees(n);
    }

/**
     * This default constructor creates a new chi-square distribution with 1
     * degree of freedom.
     */
    public ChiSquareDistribution() {
        this(1);
    }

    /**
     * This method sets the degrees of freedom parameter and computes
     * the defaut domain.
     *
     * @param n the degrees of freedom
     */
    public void setDegrees(int n) {
        //Correct invalid parameter
        if (n <= 0) {
            n = 1;
        }

        degrees = n;
        super.setParameters(0.5 * degrees, 2);
    }

    /**
     * This method returns the degrees of freedom parameter.
     *
     * @return the degrees of freedom
     */
    public int getDegrees() {
        return degrees;
    }

    /**
     * This method simulates a value from the distribuiton, as the sum
     * of squares of independent, standard normal distribution.
     *
     * @return a simulated value form the distribution
     */
    public double simulate() {
        double V;
        double Z;
        double r;
        double theta;
        V = 0;

        for (int i = 1; i <= degrees; i++) {
            r = Math.sqrt(-2 * Math.log(Math.random()));
            theta = 2 * Math.PI * Math.random();
            Z = r * Math.cos(theta);
            V = V + (Z * Z);
        }

        return V;
    }

    /**
     * This method sets the shape parameter, which must be n/2, where n
     * is the degrees of freedom.
     *
     * @param k the shape parameter
     */
    public void setShape(double k) {
        super.setShape(0.5 * degrees);
    }

    /**
     * This method sets the scale parameter, which must be 2.
     *
     * @param b the scale parameter
     */
    public void setScale(double b) {
        super.setScale(b);
    }

    /**
     * This method sets the gamma parameters, which must be related to
     * the degrees of freedom.
     *
     * @param k the shape parameter
     * @param b the scale parameter
     */
    public void setParameters(double k, double b) {
        super.setParameters(0.5 * degrees, 2);
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Chi-squre distribution [degrees of freedom = " + degrees + "]";
    }
}
