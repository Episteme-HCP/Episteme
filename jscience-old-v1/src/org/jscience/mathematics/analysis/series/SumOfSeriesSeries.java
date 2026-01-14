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
 * The SumOfSeriesSeries is the series defined as the sum of two series.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class SumOfSeriesSeries implements PrimitiveSeries {
    /** DOCUMENT ME! */
    private PrimitiveSeries series1;

    /** DOCUMENT ME! */
    private PrimitiveSeries series2;

    //be sure to have not null arguments
    /**
     * Creates a new SumOfSeriesSeries object.
     *
     * @param series1 DOCUMENT ME!
     * @param series2 DOCUMENT ME!
     */
    public SumOfSeriesSeries(PrimitiveSeries series1, PrimitiveSeries series2) {
        this.series1 = series1;
        this.series2 = series2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValueAtRank(int n) {
        return series1.getValueAtRank(n) + series2.getValueAtRank(n);
    }

    //this method will return the following
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isConvergent() {
        return series1.isConvergent() && series2.isConvergent();
    }

    //tries to compute the value at infinitum
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        return Double.NaN;
    }
}
