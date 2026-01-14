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

package org.jscience.mathematics.wavelet.cdf3_5;

import org.jscience.mathematics.wavelet.Cascades;
import org.jscience.mathematics.wavelet.IllegalScalingException;
import org.jscience.mathematics.wavelet.MultiscaleFunction;


/**
 * Cohen-Daubechies-Feauveau with N=3 and Ntilde=5 adapted to the interval
 * by Deslauriers-Dubuc-Lemire
 *
 * @author Daniel Lemire
 */
public final class DualWavelet3_5 extends MultiscaleFunction
    implements Cloneable {
    /** DOCUMENT ME! */
    private static CDF3_5 cdf = new CDF3_5();

    /** DOCUMENT ME! */
    private int n0;

    /** DOCUMENT ME! */
    private int k;

/**
     * Creates a new DualWavelet3_5 object.
     *
     * @param N0 DOCUMENT ME!
     * @param K  DOCUMENT ME!
     */
    public DualWavelet3_5(int N0, int K) {
        setParameters(N0, K);
    }

/**
     * Creates a new DualWavelet3_5 object.
     */
    public DualWavelet3_5() {
    }

    /**
     * Check if another object is equal to this DualWavelet3_5 object
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object a) {
        if ((a != null) && (a instanceof DualWavelet3_5)) {
            DualWavelet3_5 iv = (DualWavelet3_5) a;

            if ((this.dimension(0) != iv.dimension(0)) ||
                    (this.position() != iv.position())) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * Return a String representation of the object
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String ans = new String("[n0=");
        ans.concat(Integer.toString(n0));
        ans.concat("][k=");
        ans.concat(Integer.toString(k));
        ans.concat("]");

        return (ans);
    }

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
    public int getFilterType() {
        return (cdf.filtretype);
    }

    /**
     * Set the parameters for this object
     *
     * @param N0 number of scaling function on the scale of this object
     * @param K position or number of this object
     *
     * @throws IllegalScalingException if N0 is not large enough
     */
    public void setParameters(int N0, int K) {
        if (N0 < cdf.minlength) {
            throw new IllegalScalingException(N0, cdf.minlength);
        }

        n0 = N0;
        k = K;
    }

    /**
     * Return a copy of this object
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        DualWavelet3_5 s = (DualWavelet3_5) super.clone();
        s.n0 = n0;
        s.k = k;

        return (s);
    }

    /**
     * Return as an array the sampled values of the function
     *
     * @param j1 number of iterations
     *
     * @return DOCUMENT ME!
     */
    public double[] evaluate(int j1) {
        return (cdf.evalWavelet(n0, k, j1));
    }

    /**
     * Given that the wavelet is written in terms of a scale containing
     * dimension() scaling functions and going jfin scales ahead (iterating
     * jfin times), tells you how many scaling functions you'll need.
     *
     * @param jfin number of iterations
     *
     * @return DOCUMENT ME!
     */
    public int dimension(int jfin) {
        return (Cascades.dimension(n0, jfin + 1, cdf.filtretype));
    }

    /**
     * Number of scaling functions at scale where this wavelet belongs.
     *
     * @return DOCUMENT ME!
     */
    public int dimension() {
        return (dimension(0));
    }

    /**
     * Tells you what is the number of this wavelet. Wavelets are
     * numbered from left to right with the one at the left boundary being
     * noted 0.
     *
     * @return DOCUMENT ME!
     */
    public int position() {
        return (k);
    }
}
