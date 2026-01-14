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

/**
 * Implementation of <code>Absorber</code> for gasses.
 *
 * @author <a href="mailto:dale@visser.name">Dale W Visser</a>
 */
public class GasAbsorber extends Absorber {
    /** DOCUMENT ME! */
    static final double MOLAR_VOLUME = 22414.10; //cm^3 per mole

    /** DOCUMENT ME! */
    static final double ATM = 760; //torr

    /** DOCUMENT ME! */
    double length; //length in cm;

/**
     * Given thickness in millimeters, pressure in torr, element components,
     * and the numbers of atoms for each element in the gas molecule, creates
     * an instance of absorber.
     *
     * @param length          in cm
     * @param pressure        in tor
     * @param components      elements symbols of molecular components
     * @param atomsInMolecole number of atoms of each element per molecule in same order as <code>components</code>
     */
    public GasAbsorber(double length, double pressure, String[] components,
        int[] atomsInMolecule) throws NuclearException {
        Z = new int[components.length]; //elements

        double[] A = new double[components.length]; //natural weights in AMU
        fractions = new double[components.length];

        double moleWeight = 0.0;
        int numAtoms = 0;

        for (int i = 0; i < components.length; i++) {
            Z[i] = EnergyLoss.getElement(components[i]);
            A[i] = EnergyLoss.getNaturalWeight(Z[i]);
            moleWeight += (A[i] * atomsInMolecule[i]);
            numAtoms += atomsInMolecule[i];
        }

        for (int i = 0; i < components.length; i++) {
            fractions[i] = (double) atomsInMolecule[i] / (double) numAtoms;
        }

        //             [g/mol]/[cm^3/mol](1 ATM)   [atm/atm]      [cm]
        this.thickness = (moleWeight / MOLAR_VOLUME) * (pressure / ATM) * (length); // g/cm^2
        this.thickness *= 1.0E6; // ug/cm^2
        this.length = length;
    }

/**
     * Creates a new GasAbsorber object.
     */
    private GasAbsorber() {
    }

/**
     * Creates a new GasAbsorber object.
     *
     * @param length          DOCUMENT ME!
     * @param pressure        DOCUMENT ME!
     * @param component       DOCUMENT ME!
     * @param atomsInMolecule DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    public GasAbsorber(double length, double pressure, String component,
        int atomsInMolecule) throws NuclearException {
        Z = new int[1]; //elements

        double[] A = new double[1]; //natural weights in AMU
        fractions = new double[1];

        double moleWeight;
        int numAtoms;
        Z[0] = EnergyLoss.getElement(component);
        A[0] = EnergyLoss.getNaturalWeight(Z[0]);
        moleWeight = A[0] * atomsInMolecule;
        numAtoms = atomsInMolecule;
        fractions[0] = 1.0;
        this.thickness = moleWeight / MOLAR_VOLUME / ATM * pressure * (length); // g/cm^2
        this.thickness *= 1.0E6; // ug/cm^2
        this.length = length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDensity() {
        return (thickness * 1.0e-6 /*g/cm^2*/) / length /*length in cm*/;
    }

    /**
     * Static factory method for isobutane gas absorber.
     *
     * @param length in cm
     * @param pressure in torr
     *
     * @return isobutane gas absorber object
     *
     * @throws NuclearException if something goes wrong running
     *         <code>GasAbsorber</code> constructor
     */
    static public GasAbsorber Isobutane(double length, double pressure)
        throws NuclearException {
        String[] elements = { "C", "H" };
        int[] numbers = { 4, 10 };

        return new GasAbsorber(length, pressure, elements, numbers);
    }

    /**
     * Static factory method for tetraflouromethane gas absorber.
     *
     * @param length in cm
     * @param pressure in torr
     *
     * @return isobutane gas absorber object
     *
     * @throws NuclearException if something goes wrong running
     *         <code>GasAbsorber</code> constructor
     */
    static public GasAbsorber CF4(double length, double pressure)
        throws NuclearException {
        String[] elements = { "C", "F" };
        int[] numbers = { 1, 4 };

        return new GasAbsorber(length, pressure, elements, numbers);
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
     * @param factor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Absorber getNewInstance(double factor) {
        Absorber rval = (Absorber) clone();
        rval.setThickness(getThickness() * factor);

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Object clone() {
        GasAbsorber rval = new GasAbsorber();
        rval.Z = new int[Z.length];
        rval.fractions = new double[Z.length];
        rval.thickness = thickness;
        rval.length = length;
        System.arraycopy(Z, 0, rval.Z, 0, Z.length);
        System.arraycopy(fractions, 0, rval.fractions, 0, fractions.length);

        return rval;
    }
}
