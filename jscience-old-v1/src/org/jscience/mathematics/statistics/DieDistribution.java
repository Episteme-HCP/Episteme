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
 * This class models the distribution for a standard 6-sided die.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class DieDistribution extends FiniteDistribution implements Serializable {
    /** DOCUMENT ME! */
    public final static int FAIR = 0;

    /** DOCUMENT ME! */
    public final static int FLAT16 = 1;

    /** DOCUMENT ME! */
    public final static int FLAT25 = 2;

    /** DOCUMENT ME! */
    public final static int FLAT34 = 3;

    /** DOCUMENT ME! */
    public final static int LEFT = 4;

    /** DOCUMENT ME! */
    public final static int RIGHT = 5;

/**
     * This general constructor creates a new die distribution with specified
     * probabilities.
     *
     * @param p the array of probabilities
     */
    public DieDistribution(double[] p) {
        super(1, 6, 1, p);
    }

/**
     * This special constructor creates a new die distribution of a special
     * type (1-6 flat, 2-5 flat, 3-4 flat, skewed left, skewed right, or
     * fair).
     *
     * @param n the type of distribution
     */
    public DieDistribution(int n) {
        super(1, 6, 1);
        setProbabilities(n);
    }

/**
     * This default constructor creates a new fair die distribution.
     */
    public DieDistribution() {
        this(FAIR);
    }

    /**
     * This method specifies the probabilities for the special types
     * (fair, 1-6 flat, 2-5 flat, 3-4 flat, skewed left, or skewed right).
     *
     * @param n the type of distribution
     */
    public void setProbabilities(int n) {
        if (n == FLAT16) {
            setProbabilities(new double[] {
                    1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 8, 1.0 / 4
                });
        } else if (n == FLAT25) {
            setProbabilities(new double[] {
                    1.0 / 8, 1.0 / 4, 1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 8
                });
        } else if (n == FLAT34) {
            setProbabilities(new double[] {
                    1.0 / 8, 1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 8, 1.0 / 8
                });
        } else if (n == LEFT) {
            setProbabilities(new double[] {
                    1.0 / 21, 2.0 / 21, 3.0 / 21, 4.0 / 21, 5.0 / 21, 6.0 / 21
                });
        } else if (n == RIGHT) {
            setProbabilities(new double[] {
                    6.0 / 21, 5.0 / 21, 4.0 / 21, 3.0 / 21, 2.0 / 21, 1.0 / 21
                });
        } else {
            setProbabilities(new double[] {
                    1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6, 1.0 / 6
                });
        }
    }

    /**
     * This method sets the probabilities. The length of the array must
     * be 6.
     *
     * @param p the array of probabilities
     */
    public void setProbabilities(double[] p) {
        if (p.length == 6) {
            super.setProbabilities(p);
        }
    }

    /**
     * This method ensures that the finite distribution parameters are
     * not changed to inappropriate values.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public void setParameters(double a, double b, double w, double[] p) {
        super.setParameters(1, 6, 1, p);
    }

    /**
     * This method returns a string that gives the name of the
     * distribution and the values of the parameters.
     *
     * @return a string giving the name of the distribution and the values of
     *         the parameters
     */
    public String toString() {
        return "Die distribution [probabilities = " + getProbabilities() + "]";
    }
}
