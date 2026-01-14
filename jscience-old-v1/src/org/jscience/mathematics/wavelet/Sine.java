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

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.MathConstants;


/**
 * This class is used to be able to mix the wavelet and sine transforms. It
 * is in fact a normalised sine.
 *
 * @author Daniel Lemire
 */
public final class Sine extends MultiscaleFunction implements Cloneable {
    /** DOCUMENT ME! */
    private int n0;

    /** DOCUMENT ME! */
    private int freq;

    /** DOCUMENT ME! */
    private double normalisation;

/**
     * Creates a new Sine object.
     *
     * @param N0   DOCUMENT ME!
     * @param FREQ DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Sine(int N0, int FREQ) {
        if (N0 < 0) {
            throw new IllegalArgumentException("The length paramenter " + N0 +
                " must be positive");
        }

        if ((FREQ < 0) || (FREQ >= N0)) {
            throw new IllegalArgumentException("The frequency parameter " +
                FREQ + " must be between " + 0 + " and " + (N0 - 1));
        }

        n0 = N0;
        freq = FREQ;
        normalisation = Math.sqrt(n0 / 2d);
    }

    /**
     * Return a String representation of the object
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String ans = new String("[n0=");
        ans.concat(Integer.toString(n0));
        ans.concat("][freq=");
        ans.concat(Integer.toString(freq));
        ans.concat("]");

        return (ans);
    }

    /**
     * Check if another object is equal to this Sine object
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object a) {
        if ((a != null) && (a instanceof Sine)) {
            Sine iv = (Sine) a;

            if ((this.dimension(0) != iv.dimension(0)) ||
                    (this.getFrequency() != iv.getFrequency())) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFrequency() {
        return (freq);
    }

    /**
     * Return as an array the sampled values of the function
     *
     * @return DOCUMENT ME!
     */
    public double[] evaluate() {
        return (ArrayMathUtils.scalarMultiply(1.0 / normalisation,
            evaluate(n0, freq)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param N0 DOCUMENT ME!
     * @param FREQ DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double[] evaluate(int N0, int FREQ) {
        double[] ans = new double[N0];

        for (int k = 0; k < ans.length; k++) {
            ans[k] = Math.sin((MathConstants.TWO_PI * k * FREQ) / N0);
        }

        return (ans);
    }

    /**
     * Tells you how many samples you'll get from this function (will
     * not depend on the parameter)
     *
     * @param jfin DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int dimension(int jfin) {
        return (n0);
    }

    /**
     * Tells you how many samples you'll get from this function
     *
     * @return DOCUMENT ME!
     */
    public int dimension() {
        return (n0);
    }

    /**
     * Return a copy of this object
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        Sine s = (Sine) super.clone();
        s.n0 = n0;
        s.freq = freq;

        return (s);
    }

    /**
     * Return as an array the sampled values of the function
     *
     * @param j1 number of iterations (doesn't do anything)
     *
     * @return DOCUMENT ME!
     */
    public double[] evaluate(int j1) {
        return (evaluate());
    }

    /**
     * Compute the mass (integral)
     *
     * @param a left boundary of the interval
     * @param b right boundary of the interval
     * @param jfin number of iterations to consider (precision)
     *
     * @return DOCUMENT ME!
     */
    public double mass(double a, double b, int jfin) {
        double somme = 0.0;
        double[] values = evaluate(jfin);

        for (int k = 0; k < values.length; k++) {
            somme += values[k];
        }

        somme = somme / (values.length - 1) * Math.abs(b - a);

        return (somme);
    }

    /**
     * This method is used to compute how the number of scaling
     * functions changes from on scale to the other. Basically, if you have k
     * scaling function and a filter of type t, you'll have 2k+t scaling
     * functions at the next scale (dyadic case). Notice that this method
     * assumes that one is working with the dyadic grid while the method
     * "previousDimension" define in the interface "filter" doesn't.
     *
     * @return DOCUMENT ME!
     */
    public int getFilterType() {
        return (n0);
    }
}
