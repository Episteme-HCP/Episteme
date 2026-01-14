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

package org.jscience.mathematics.analysis.series;

/**
 * The Fibonacci mathematical series, 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55,
 * 89, 144, . . .
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class FibonacciSeries implements PrimitiveSeries {
/**
     * Creates a new FibonacciSeries object.
     */
    public FibonacciSeries() {
    }

    //always returns 0 for 0 or less
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValueAtRank(int n) {
        if (n > 1) {
            return getValueAtRank(n - 1) + getValueAtRank(n - 2);
        } else {
            if (n == 1) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    //is DOES diverge
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isConvergent() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        return Double.POSITIVE_INFINITY;
    }
}
