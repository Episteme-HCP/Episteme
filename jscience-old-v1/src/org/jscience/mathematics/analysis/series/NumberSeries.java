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
 * A mathematical series.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//some series that could be implemented http://mathworld.wolfram.com/MaclaurinSeries.html
//may be we should implement Mapping althougth be be better define a FunctionOfSeries
//You may want to have a look at http://www.research.att.com/~njas/sequences/Seis.html, the On-Line Encyclopedia of Integer Sequences
public interface NumberSeries extends AbstractSeries {
    //n should always be greater or equal to zero, though no error is thrown otherwise
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Number getValueAtRank(int n);

    //there is probably some things to implement here using http://mathworld.wolfram.com/ConvergenceTests.html
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Number getValue();

    //we could also getMean() getVariance(), etc.
}
