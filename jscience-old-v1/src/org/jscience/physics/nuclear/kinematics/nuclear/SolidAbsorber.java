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

import java.io.IOException;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.io.StringReader;

import java.util.Vector;


/**
 * Implementation of <code>Absorber</code> for a solid.
 *
 * @author <a href="mailto:dale@visser.name">Dale W Visser</a>
 */
public class SolidAbsorber extends Absorber implements Serializable {
    /**
     * DOCUMENT ME!
     */
    static java.text.DecimalFormat df = new java.text.DecimalFormat("0.000#");

    /**
     * Creates a new SolidAbsorber object.
     *
     * @param thickness DOCUMENT ME!
     * @param units DOCUMENT ME!
     * @param components DOCUMENT ME!
     * @param fractions DOCUMENT ME!
     *
     * @throws NuclearException DOCUMENT ME!
     */
    public SolidAbsorber(double thickness, int units, String[] components,
        double[] fractions) throws NuclearException {
        if (components.length != fractions.length) {
            throw new NuclearException(
                "SolidAbsorber constructor: Arrays not equal size!");
        }

        this.fractions = setFractions(fractions);
        Z = new int[components.length];

        for (int i = 0; i < components.length; i++) {
            Z[i] = EnergyLoss.getElement(components[i]);
        }

        setDensity();
        setThick(thickness, units);
    }

    /**
     * Creates a new SolidAbsorber object.
     *
     * @param thickness DOCUMENT ME!
     * @param units DOCUMENT ME!
     * @param component DOCUMENT ME!
     *
     * @throws NuclearException DOCUMENT ME!
     */
    public SolidAbsorber(double thickness, int units, String component)
        throws NuclearException {
        fractions = new double[1];
        fractions[0] = 1.0;
        Z = new int[1];
        Z[0] = EnergyLoss.getElement(component);
        setDensity();
        setThick(thickness, units);

        if (getThickness() == 0.0) {
            throw new NuclearException("No thickness!");
        }

        //System.out.println("new SolidAbsorber: "+component+" "+getThickness()+" ug/cm^2");
    }

    /**
     * Creates a new SolidAbsorber object.
     *
     * @param thickness DOCUMENT ME!
     * @param units DOCUMENT ME!
     * @param component DOCUMENT ME!
     *
     * @throws NuclearException DOCUMENT ME!
     */
    public SolidAbsorber(double thickness, int units, Nucleus component)
        throws NuclearException {
        fractions = new double[1];
        fractions[0] = 1.0;
        Z = new int[1];
        Z[0] = component.Z;
        setDensity();
        setThick(thickness, units);

        if (getThickness() == 0.0) {
            throw new NuclearException("No thickness!");
        }

        //System.out.println("new SolidAbsorber: "+component+" "+getThickness()+" ug/cm^2");
    }

    /**
     * Creates a new SolidAbsorber object.
     *
     * @param spec DOCUMENT ME!
     * @param thickness DOCUMENT ME!
     *
     * @throws NuclearException DOCUMENT ME!
     */
    public SolidAbsorber(String spec, double thickness)
        throws NuclearException {
        Vector v_elements = new Vector(1, 1);
        Vector v_amounts = new Vector(1, 1);

        try {
            StreamTokenizer st = new StreamTokenizer(new StringReader(spec));
            int type = st.nextToken();

            while ((type != StreamTokenizer.TT_EOF) &&
                    (type != StreamTokenizer.TT_EOL)) {
                if (type == StreamTokenizer.TT_WORD) {
                    v_elements.addElement(st.sval);
                    type = st.nextToken();

                    if (type == StreamTokenizer.TT_NUMBER) {
                        v_amounts.addElement(new Double(st.nval));
                    }
                }

                type = st.nextToken();
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }

        String[] components = new String[v_elements.size()];
        double[] fractions = new double[v_amounts.size()];

        for (int i = 0; i < v_amounts.size(); i++) {
            components[i] = (String) v_elements.elementAt(i);
            fractions[i] = ((Double) v_amounts.elementAt(i)).doubleValue();
        }

        if (components.length != fractions.length) {
            throw new NuclearException(
                "SolidAbsorber constructor: Arrays not equal size!");
        }

        this.fractions = setFractions(fractions);
        Z = new int[components.length];

        for (int i = 0; i < components.length; i++) {
            Z[i] = EnergyLoss.getElement(components[i]);
        }

        setDensity();
        setThick(thickness, SolidAbsorber.MICROGRAM_CM2);
    }

    /**
     * Creates a new SolidAbsorber object.
     */
    private SolidAbsorber() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param thickness DOCUMENT ME!
     * @param units DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NuclearException DOCUMENT ME!
     */
    static public SolidAbsorber Mylar(double thickness, int units)
        throws NuclearException {
        String[] mylarElements = { "C", "H", "O" };
        double[] mylarFractions = { 10, 8, 4 };

        return new SolidAbsorber(thickness, units, mylarElements, mylarFractions);
    }

    /**
     * DOCUMENT ME!
     *
     * @param thickness DOCUMENT ME!
     * @param units DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NuclearException DOCUMENT ME!
     */
    static public SolidAbsorber Kapton(double thickness, int units)
        throws NuclearException {
        String[] kaptonElements = { "H", "C", "N", "O" };
        double[] kaptonFractions = { 10, 22, 2, 5 };

        return new SolidAbsorber(thickness, units, kaptonElements,
            kaptonFractions);
    }

    /**
     * DOCUMENT ME!
     */
    private void setDensity() {
        density = new double[Z.length];

        for (int i = 0; i < Z.length; i++) {
            density[i] = fractions[i] * EnergyLoss.getDensity(Z[i]);
        }
    }

    /**
     * Estimated density of solid in g/cm^3.
     *
     * @return DOCUMENT ME!
     */
    public double getDensity() {
        double rval = 0.0;

        for (int i = 0; i < Z.length; i++) {
            rval += this.density[i];
        }

        return rval;
    }

    /**
     * Returns thickness in micrograms/cm^2.
     *
     * @return DOCUMENT ME!
     */
    public double getThickness() {
        return thickness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param units DOCUMENT ME!
     */
    private void setThick(double x, int units) {
        switch (units) {
        case MICROGRAM_CM2:
            this.thickness = x;

            break;

        case MILLIGRAM_CM2:
            this.thickness = x * 1000;

            break;

        case CM:
            this.thickness = 1.0e6 * x * getDensity();

            break;

        case MIL:
            this.thickness = 1.0e6 * (x * 0.00254) * getDensity();

        default: //MICROGRAM_CM2
            this.thickness = x;

            break;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param factor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Absorber getNewInstance(double factor) {
        SolidAbsorber rval = (SolidAbsorber) clone();
        rval.setThickness(getThickness() * factor);

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Object clone() {
        SolidAbsorber rval = new SolidAbsorber();
        rval.Z = new int[Z.length];
        rval.fractions = new double[Z.length];
        rval.density = new double[Z.length];
        rval.thickness = thickness;
        //rval.length=length;
        System.arraycopy(Z, 0, rval.Z, 0, Z.length);
        System.arraycopy(fractions, 0, rval.fractions, 0, fractions.length);
        System.arraycopy(density, 0, rval.density, 0, density.length);

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        String rval = "";

        for (int i = 0; i < Z.length; i++) {
            rval += (Nucleus.getElementSymbol(Z[i]) + " ");
            rval += (df.format(fractions[i]) + " ");
        }

        return rval;
    }
}
