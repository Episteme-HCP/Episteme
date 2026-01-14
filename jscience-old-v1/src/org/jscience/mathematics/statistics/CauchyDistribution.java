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
 * This class models the Cauchy distribution.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class CauchyDistribution extends StudentDistribution
    implements Serializable {
    //Constructor
    /**
     * Creates a new CauchyDistribution object.
     */
    public CauchyDistribution() {
        super(1);
    }

    /**
     * This method computes the cumulative distribuiton function in
     * closed form.
     *
     * @param x a number in the domain of the distribution
     *
     * @return the cumulative probability at x
     */
    public double getCDF(double x) {
        return 0.5 + (Math.atan(x) / Math.PI);
    }

    /**
     * This method computes the quantile function in closed form.
     *
     * @param p a probability in (0, 1)
     *
     * @return the quantile of order p
     */
    public double getQuantile(double p) {
        return Math.tan(Math.PI * (p - 0.5));
    }

    /**
     * This method sets the degrees of freedom which is fixed at 1.
     *
     * @param n the degrees of freedom
     */
    public void setDegrees(int n) {
        super.setDegrees(1);
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Cauchy distribution";
    }
}
