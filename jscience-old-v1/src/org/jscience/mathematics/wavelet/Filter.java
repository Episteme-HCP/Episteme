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

package org.jscience.mathematics.wavelet;

/**
 * This interface is used to define wavelet filters. It is fairly general to
 * accomodate just about any filter (except complex ones). Since changing an
 * interface is painful, it must be as general as possible to start with.
 * Therefore it doesn't assume that you are using dyadic wavelets (for
 * example) and so, some object will implement somewhat redundant method that
 * builds on the dyadic grid (for simplicity).
 *
 * @author Daniel Lemire
 */
public interface Filter {
    /**
     * lowpass filter
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    double[] lowpass(double[] x);

    /**
     * Highpass filters are normalized in order to get L2
     * orthonormality of the resulting wavelets (when it applies). See the
     * class DiscreteHilbertSpace for an implementation of the L2 integration.
     *
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    double[] highpass(double[] y);

    /**
     * lowpass filter
     *
     * @param x DOCUMENT ME!
     * @param param a parameter for the filter
     *
     * @return DOCUMENT ME!
     */
    double[] lowpass(double[] x, double[] param);

    /**
     * Highpass filters are normalized in order to get L2
     * orthonormality of the resulting wavelets (when it applies). See the
     * class DiscreteHilbertSpace for an implementation of the L2 integration.
     *
     * @param y DOCUMENT ME!
     * @param param a parameter for the filter
     *
     * @return DOCUMENT ME!
     */
    double[] highpass(double[] y, double[] param);

    /**
     * This method return the number of "scaling" functions at the
     * previous scale given a number of scaling functions. The answer is
     * always smaller than the provided value (about half since this is a
     * dyadic implementation). This relates to the same idea as the "filter
     * type". However this method is used in the context of signal processing
     * for performance reasons.
     *
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int previousDimension(int k);
}
