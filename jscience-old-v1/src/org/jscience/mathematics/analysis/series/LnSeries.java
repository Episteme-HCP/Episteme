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
 * The Ln mathematical series, also named MercatorSeries
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this is really a raw implementation, we can do better using high precision numbers
public final class LnSeries implements PrimitiveSeries {
    /** DOCUMENT ME! */
    private double x;

    //computes ln (x+1)
    /**
     * Creates a new LnSeries object.
     *
     * @param x DOCUMENT ME!
     */
    public LnSeries(double x) {
        if (x > 0) {
            this.x = x;
        } else {
            throw new IllegalArgumentException("x must be greater or equal 0.");
        }
    }

    //computes ln (x+1)
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValueAtRank(int n) {
        if (n > 1) {
            //we won't get a high precision using Math.pow(), but who knows
            return getValueAtRank(n - 1) +
            ((Math.pow(-1, n + 1) * Math.pow(x, n)) / n);
        } else {
            if (n == 1) {
                return x;
            } else {
                throw new IllegalArgumentException("n must be greater than 0.");
            }
        }
    }

    //we should provide a method getPolynomial()
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isConvergent() {
        return true;
    }

    //computes ln (x+1)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        return Math.log(1 + x);
    }
}
