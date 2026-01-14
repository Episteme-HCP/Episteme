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
 * This class is used to encapsulate the various wavelet multiresolution
 * (Dau2, Haar, ...). It assumes that your are working with the dyadic case
 * and with real numbers. It is meant to provide only the most basic methods
 * in order to be as general as possible. Also, for performance
 * considerations, this class should be as light as possible. However, in
 * practice, it is useful to put most of the actual implementation in a
 * multiresolution object instead of spreading it over many objects.
 *
 * @author Daniel Lemire
 */
public abstract class Multiresolution extends Object {
    /**
     * This method is used to compute how the number of scaling
     * functions changes from on scale to the other. Basically, if you have k
     * scaling function and a Filter of type t, you'll have 2k+t scaling
     * functions at the next scale (dyadic case). Notice that this method
     * assumes that one is working with the dyadic grid while the method
     * "previousDimension" define in the interface "Filter" doesn't.
     *
     * @return DOCUMENT ME!
     */
    public abstract int getFilterType();

    /**
     * DOCUMENT ME!
     *
     * @param n0 DOCUMENT ME!
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract MultiscaleFunction primaryScaling(int n0, int k);

    /**
     * DOCUMENT ME!
     *
     * @param n0 DOCUMENT ME!
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract MultiscaleFunction dualScaling(int n0, int k);

    /**
     * DOCUMENT ME!
     *
     * @param n0 DOCUMENT ME!
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract MultiscaleFunction primaryWavelet(int n0, int k);

    /**
     * DOCUMENT ME!
     *
     * @param n0 DOCUMENT ME!
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract MultiscaleFunction dualWavelet(int n0, int k);

    /**
     * This method return the number of "scaling" functions at the
     * previous scale given a number of scaling functions. The answer is
     * always smaller than the provided value (about half since this is a
     * dyadic implementation). This relates to the same idea as the "Filter
     * type". It is used by the interface "Filter". If needed it should be
     * overwritten, in particular, for performance considerations.
     *
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int previousDimension(int k) {
        return (Cascades.previousDimension(getFilterType(), k));
    }
}
