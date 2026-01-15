package org.jscience.chemistry;

import org.jscience.util.Named;

import java.util.Collections;
import java.util.Map;


/**
 * A class representing chemical elements.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.7
 */

//see http://www.webelements.com/ for a periodic table
public class Element extends Object implements java.io.Serializable, Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String symbol;

    /** DOCUMENT ME! */
    private int atomicNumber;

    /** DOCUMENT ME! */
    private int massNumber;

    /** DOCUMENT ME! */
    private int[] quantumLayers;

    /** DOCUMENT ME! */
    private double electronegativity; //Pauling

    /** DOCUMENT ME! */
    private double covalentRadius;

    /** DOCUMENT ME! */
    private double atomicRadius;

    /** DOCUMENT ME! */
    private double[] ionizationEnergies;

    /** DOCUMENT ME! */
    private double electronicAffinity;

    /** DOCUMENT ME! */
    private double meltingPoint;

    /** DOCUMENT ME! */
    private double boilingPoint;

    /** DOCUMENT ME! */
    private double density; //of solid

    /** DOCUMENT ME! */
    private double formationEnthalpy; //H

    //meltingEnthalpy
    //fusionEnthalpy
    //combustionEnthaply
    /** DOCUMENT ME! */
    private double formationEnergy; //Gibbs

    /** DOCUMENT ME! */
    private double entropy; //S

    //meltingEntropy
    //fusionEntropy
    /** DOCUMENT ME! */
    private double specificHeat; //C

    /** DOCUMENT ME! */
    private double electricalConductivity;

    /** DOCUMENT ME! */
    private double thermalConductivity;

    /** DOCUMENT ME! */
    private Map isotopes;

    //TODO, there is probably a way to compute the light energy (ray spectrum) for every element but I have no idea on how to do that
    //see http://www.colorado.edu/physics/2000/applets/a2.html for example
    //TODO, also we should be able to get the electron valence(s) for every element
    //http://www.colorado.edu/physics/2000/periodic_table/valence_electrons.html
/**
     * Constructs an element.
     *
     * @param title DOCUMENT ME!
     * @param label DOCUMENT ME!
     */
    public Element(String title, String label) {
        name = title;
        symbol = label;
        isotopes = Collections.EMPTY_MAP;
    }

    /**
     * Returns the name.
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the atomic number.
     *
     * @return DOCUMENT ME!
     */
    public int getAtomicNumber() {
        return atomicNumber;
    }

    /**
     * DOCUMENT ME!
     *
     * @param z DOCUMENT ME!
     */
    protected void setAtomicNumber(int z) {
        atomicNumber = z;
    }

    /**
     * Returns the mass number.
     *
     * @return DOCUMENT ME!
     */
    public int getMassNumber() {
        return massNumber;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    protected void setMassNumber(int m) {
        massNumber = m;
    }

    /**
     * Returns the number of electrons in each of the quantum layers in
     * ascending order (s, p, d, f, g, h, i...) for this element.
     *
     * @return DOCUMENT ME!
     */

    //1s 2s 2p 3s 3p 4s 3d 4p 5s 4d 5p 6s 4f 5d 6p 7s 5f 6d 7s 7p
    //TODO, there is probably a way to compute that based on the atomic number but I really don't know how to
    public int[] getQuantumLayers() {
        return quantumLayers;
    }

    /**
     * DOCUMENT ME!
     *
     * @param layers DOCUMENT ME!
     */
    protected void setQuantumLayers(int[] layers) {
        quantumLayers = layers;
    }

    /**
     * Returns the electronegativity (Pauling).
     *
     * @return DOCUMENT ME!
     */
    public double getElectronegativity() {
        return electronegativity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param en DOCUMENT ME!
     */
    protected void setElectronegativity(double en) {
        electronegativity = en;
    }

    /**
     * Returns the covalent radius.
     *
     * @return DOCUMENT ME!
     */
    public double getCovalentRadius() {
        return covalentRadius;
    }

    /**
     * DOCUMENT ME!
     *
     * @param covRadius DOCUMENT ME!
     */
    protected void setCovalentRadius(double covRadius) {
        covalentRadius = covRadius;
    }

    /**
     * Returns the atomic radius.
     *
     * @return DOCUMENT ME!
     */
    public double getAtomicRadius() {
        return atomicRadius;
    }

    /**
     * DOCUMENT ME!
     *
     * @param atomRadius DOCUMENT ME!
     */
    protected void setAtomicRadius(double atomRadius) {
        atomicRadius = atomRadius;
    }

    /**
     * Returns the energy needed to remove the first, second...
     * electron.
     *
     * @return DOCUMENT ME!
     */
    public double[] getIonizationEnergies() {
        return ionizationEnergies;
    }

    //http://physics.nist.gov/cgi-bin/AtData/main_asd
    /**
     * DOCUMENT ME!
     *
     * @param energies DOCUMENT ME!
     */
    protected void setIonizationEnergies(double[] energies) {
        ionizationEnergies = energies;
    }

    /**
     * Returns the energy needed to gain an electron.
     *
     * @return DOCUMENT ME!
     */
    public double getElectronicAffinity() {
        return electronicAffinity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param affinity DOCUMENT ME!
     */
    protected void setElectronicAffinity(double affinity) {
        electronicAffinity = affinity;
    }

    /**
     * Returns the melting point (K).
     *
     * @return DOCUMENT ME!
     */
    public double getMeltingPoint() {
        return meltingPoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param melt DOCUMENT ME!
     */
    protected void setMeltingPoint(double melt) {
        meltingPoint = melt;
    }

    /**
     * Returns the boiling point (K).
     *
     * @return DOCUMENT ME!
     */
    public double getBoilingPoint() {
        return boilingPoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param boil DOCUMENT ME!
     */
    protected void setBoilingPoint(double boil) {
        boilingPoint = boil;
    }

    /**
     * Returns the density (293K).
     *
     * @return DOCUMENT ME!
     */
    public double getDensity() {
        return density;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rho DOCUMENT ME!
     */
    protected void setDensity(double rho) {
        density = rho;
    }

    /**
     * Returns the formation enthalpy.
     *
     * @return DOCUMENT ME!
     */
    public double getFormationEnthalpy() {
        return formationEnthalpy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param enthaply DOCUMENT ME!
     */
    protected void setFormationEnthalpy(double enthaply) {
        formationEnthalpy = enthaply;
    }

    /**
     * Returns the formation energy (Gibbs energy).
     *
     * @return DOCUMENT ME!
     */
    public double getFormationEnergy() {
        return formationEnergy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param energy DOCUMENT ME!
     */
    protected void setFormationEnergy(double energy) {
        formationEnergy = energy;
    }

    /**
     * Returns the entropy.
     *
     * @return DOCUMENT ME!
     */
    public double getEntropy() {
        return entropy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param entropy DOCUMENT ME!
     */
    protected void setEntropy(double entropy) {
        entropy = entropy;
    }

    /**
     * Returns the specific heat.
     *
     * @return DOCUMENT ME!
     */
    public double getSpecificHeat() {
        return specificHeat;
    }

    /**
     * DOCUMENT ME!
     *
     * @param heat DOCUMENT ME!
     */
    protected void setSpecificHeat(double heat) {
        specificHeat = heat;
    }

    /**
     * Returns the electrical conductivity.
     *
     * @return DOCUMENT ME!
     */
    public double getElectricalConductivity() {
        return electricalConductivity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param elect DOCUMENT ME!
     */
    protected void setElectricalConductivity(double elect) {
        electricalConductivity = elect;
    }

    /**
     * Returns the thermal conductivity.
     *
     * @return DOCUMENT ME!
     */
    public double getThermalConductivity() {
        return thermalConductivity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param therm DOCUMENT ME!
     */
    protected void setThermalConductivity(double therm) {
        thermalConductivity = therm;
    }

    /**
     * Returns the map of mass number/abundance.
     *
     * @return DOCUMENT ME!
     */
    public Map getIsotopes() {
        return isotopes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param isotopes DOCUMENT ME!
     */
    protected void setIsotopes(Map isotopes) {
        this.isotopes = isotopes;
    }

    /**
     * Compares two elements for equality.
     *
     * @param e an element.
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object e) {
        return (e != null) && (e instanceof Element) &&
        (atomicNumber == ((Element) e).atomicNumber) &&
        (massNumber == ((Element) e).massNumber);
    }

    /**
     * Returns the chemical symbol.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return symbol;
    }
}
