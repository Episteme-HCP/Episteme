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
 * Abstract encapsulation mostly meant for wavelet functions (dyadic case).
 *
 * @author Daniel Lemire
 */
public abstract class MultiscaleFunction implements Cloneable {
    /**
     * DOCUMENT ME!
     *
     * @param jfin DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int dimension(int jfin);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int dimension();

    /**
     * Return a copy of the object
     *
     * @return DOCUMENT ME!
     *
     * @throws InternalError DOCUMENT ME!
     */
    public Object clone() {
        try {
            MultiscaleFunction mf = (MultiscaleFunction) super.clone();

            return (mf);
        } catch (CloneNotSupportedException cnse) {
            throw new InternalError();
        }
    }

    /**
     * Return a string representing the object
     *
     * @return DOCUMENT ME!
     */
    public abstract String toString();

    /**
     * Return as an array the sampled values of the function
     *
     * @param j1 number of iterations
     *
     * @return DOCUMENT ME!
     */
    public abstract double[] evaluate(int j1);

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
        double somme = 0;
        double[] values = evaluate(jfin);

        for (int k = 1; k < (values.length - 1); k++) {
            somme += values[k];
        }

        somme += (values[0] / 2);
        somme += (values[values.length - 1] / 2);
        somme = somme / (values.length - 1) * Math.abs(b - a);

        return (somme);
    }

    /**
     * Compute the mass (integral) of the interval 0,1
     *
     * @param jfin number of iterations to consider (precision)
     *
     * @return DOCUMENT ME!
     */
    public double mass(int jfin) {
        return (mass(0, 1, jfin));
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
    public abstract int getFilterType();

    /**
     * Check if another object is equal to this MultiscaleFunction
     * object
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean equals(Object o);
}
