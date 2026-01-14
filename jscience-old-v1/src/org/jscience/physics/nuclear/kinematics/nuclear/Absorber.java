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

package org.jscience.physics.nuclear.kinematics.nuclear;

import java.io.Serializable;


/**
 * An abstraction of a thickness of material acting as a stopper of
 * energetic ions.
 *
 * @author <a href="mailto:dale@visser.name">Dale W. Visser</a>
 */
public abstract class Absorber implements Serializable {
    /** Units of thickness, ug/cm^2, mg/cm^2, cm */
    public final static int MICROGRAM_CM2 = 1;

    /**
     * DOCUMENT ME!
     */
    public final static int MILLIGRAM_CM2 = 2;

    /**
     * DOCUMENT ME!
     */
    public final static int CM = 3;

    /**
     * DOCUMENT ME!
     */
    public final static int MIL = 4;

    /**
     * DOCUMENT ME!
     */
    protected int[] Z;

    /** Densities in g/cm^3.  (E.g. water would be ~ 1.0). */
    protected double[] density;

    /** Fraction of nuclei that are this species. */
    protected double[] fractions;

    /**
     * DOCUMENT ME!
     */
    protected double thickness;

    /**
     * Returns thickness in micrograms/cm^2.
     *
     * @return DOCUMENT ME!
     */
    public abstract double getThickness();

    /**
     * DOCUMENT ME!
     *
     * @param fin DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected double[] setFractions(double[] fin) {
        double[] fout = new double[fin.length];
        double sum = getSum(fin);

        for (int i = 0; i < fin.length; i++)
            fout[i] = fin[i] / sum;

        return fout;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fin DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double getSum(double[] fin) {
        double sum = 0.0;

        for (int i = 0; i < fin.length; i++)
            sum += fin[i];

        return sum;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getElements() {
        return Z;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getFractions() {
        return fractions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param units DOCUMENT ME!
     */
    public void setThickness(double value, int units) {
        if (units == MICROGRAM_CM2) {
            thickness = value;
        } else if (units == MILLIGRAM_CM2) {
            thickness = value * 1000.0;
        } else if (units == CM) {
            thickness = getDensity() /*g/cm^3*/ * 1000000.0 /*ug/cm^3*/ * value;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setThickness(double value) {
        setThickness(value, MICROGRAM_CM2);
    }

    /**
     * Returns mass density of absorber in g/cm^3.
     *
     * @return DOCUMENT ME!
     */
    public abstract double getDensity();

    /**
     * Returns new absorber identical to this one, with thickness
     * multiplied by <code>factor</code>.
     *
     * @param factor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Absorber getNewInstance(double factor);
}
