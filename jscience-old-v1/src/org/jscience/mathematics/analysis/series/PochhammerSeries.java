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
 * The Pochhammer Symbol mathematical series.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//see http://mathworld.wolfram.com/PochhammerSymbol.html
public final class PochhammerSeries implements PrimitiveSeries {
    /** DOCUMENT ME! */
    private double x;

/**
     * Creates a new PochhammerSeries object.
     *
     * @param x DOCUMENT ME!
     */
    public PochhammerSeries(double x) {
        this.x = x;
    }

    //n>0 strictly otherwise an error is thrown
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValueAtRank(int n) {
        if (n > 0) {
            return getValueAtRank(n - 1) * ((x + n) - 1);
        } else {
            if (n == 0) {
                return 1;
            } else {
                throw new IllegalArgumentException(
                    "n must be greater or equal to 0.");
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
        //actually, this result is pure speculation from my side
        return (x != 0);
    }

    //tries to compute the value at infinitum
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        if (x == 0) {
            return 0;
        } else {
            //actually, this result is pure speculation from my side
            return Double.POSITIVE_INFINITY;
        }
    }
}
