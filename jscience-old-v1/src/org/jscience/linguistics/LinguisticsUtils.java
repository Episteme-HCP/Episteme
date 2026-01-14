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

package org.jscience.linguistics;

/**
 * A class representing some useful methods for linguistics
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class LinguisticsUtils extends Object {
    /**
     * The zipf's law, see http://en.wikipedia.org/wiki/Zipf%27s_law
     *
     * @param k DOCUMENT ME!
     * @param s DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final double getZipf(double k, double s, double n) {
        return 1 / (Math.pow(k, s) * harmonicSum(n, s));
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double harmonicSum(double n, double s) {
        int i;
        double result;

        result = 0;

        for (i = 1; i <= n; i++) {
            result = result + (1 / Math.pow(i, s));
        }

        return result;
    }
}
