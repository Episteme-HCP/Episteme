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
 * Utility functions for common economic modeling curves (Logistic, Logit, Richards).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class EconomicsUtils {

    private EconomicsUtils() {}

    /** Generalised logistic curve (Richards' curve). */
    public static double getRichards(double t, double lower, double upper,
            double maxTime, double growthRate, double nearMax) {
        return lower + (upper / Math.pow(1 + (nearMax * Math.exp(-growthRate * (t - maxTime))), 1/nearMax));
    }

    /** Standard logistic curve. */
    public static double getLogistic(double t, double cap, double m, double n, double tau) {
        double expTerm = Math.exp(-t / tau);
        return (cap * (1 + (m * expTerm))) / (1 + (n * expTerm));
    }

    /** Standard logit function (log-odds). */
    public static double getLogit(double p) {
        if (p <= 0 || p >= 1) return Double.NaN;
        return Math.log(p / (1.0 - p));
    }
}
