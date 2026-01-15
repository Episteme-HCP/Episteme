/***************************************************************
 * Nuclear Simulation Java Class Libraries
 * Copyright (C) 2003 Yale University
 *
 * Original Developer
 *     Dale Visser (dale@visser.name)
 *
 * OSI Certified Open Source Software
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the University of Illinois/NCSA 
 * Open Source License.
 *
 * This program is distributed in the hope that it will be 
 * useful, but without any warranty; without even the implied 
 * warranty of merchantability or fitness for a particular 
 * purpose. See the University of Illinois/NCSA Open Source 
 * License for more details.
 *
 * You should have received a copy of the University of 
 * Illinois/NCSA Open Source License along with this program; if 
 * not, see http://www.opensource.org/
 **************************************************************/
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
