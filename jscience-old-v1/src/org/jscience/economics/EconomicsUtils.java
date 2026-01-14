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

package org.jscience.economics;

/**
 * A class representing some useful methods for economics
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class EconomicsUtils extends Object {
    /**
     * The generalised logistic curve, or see
     * http://en.wikipedia.org/wiki/Generalised_logistic_curve
     *
     * @param t DOCUMENT ME!
     * @param lower DOCUMENT ME!
     * @param upper DOCUMENT ME!
     * @param maximum DOCUMENT ME!
     * @param growthRate DOCUMENT ME!
     * @param nearMaximum DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final double getRichards(double t, double lower, double upper,
        double maximum, double growthRate, double nearMaximum) {
        return lower +
        (upper / Math.pow(1 +
            (nearMaximum * Math.exp(-growthRate * (t - maximum))), nearMaximum));
    }

    /**
     * The logistic curve, or see
     * http://en.wikipedia.org/wiki/Logistic_curve
     *
     * @param t DOCUMENT ME!
     * @param a DOCUMENT ME!
     * @param m DOCUMENT ME!
     * @param n DOCUMENT ME!
     * @param tau DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final double getLogistic(double t, double a, double m, double n,
        double tau) {
        return (a * (1 + (m * Math.exp(-t / tau)))) / (1 +
        (n * Math.exp(-t / tau)));
    }

    /**
     * The logit curve, or see
     * http://en.wikipedia.org/wiki/Logistic_regression
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final double getLogit(double t) {
        return Math.log(t / (1 - t));
    }
}
