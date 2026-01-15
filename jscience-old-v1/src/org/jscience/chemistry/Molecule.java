package org.jscience.chemistry;

import java.io.Serializable;
import java.util.Set;

/**
 * A class representing molecules.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
//portion of the code (one method) "stolen" from Chemistry Development Kit

//molecules are normally created by chemical reactions where energy is used to bond atoms
//we here provide a direct mean of building up the molecule

//TODO, the molecule is currently defined as a classicle particle
//relativistic equations would be quite interesting to used if supported by classicle particle

//said in short, there is probably a lot of space here for additional support
//if you want to contribute to this class feel free to contribute

//chemical compound available for example from http://webbook.nist.gov/chemistry/
public class Molecule extends Object implements Cloneable, Serializable {


    //breaking a bond from this molecule after creation actually destroys the molecule (see Atom)
    protected Molecule(Atom a) {

    }

    /**
     * Returns the formula in this molecule.
     */
    public String getFormula() {

        return null;

    }

    /**
     * @return
     */
    public double computeMolecularWeight() {

        return 0;

    }

    public double getMolecularWeight() {

        return 0;

    }

    public void setMolecularWeight(double molecularWeight) {

    }

    public double getMeltingPoint() {

        return 0;

    }

    public void setMeltingPoint(double meltingPoint) {

    }

    public double getBoilingPoint() {

        return 0;

    }

    public void setBoilingPoint(double boilingPoint) {

    }

    public double getDensity() {

        return 0;

    }

    public void setDensity(double density) {

    }

    public double getVanDerWaalsA() {

        return 0;

    }

    public void setVanDerWaalsA(double vanDerWaalsA) {

    }

    public double getVanDerWaalsB() {

        return 0;

    }

    public void setVanDerWaalsB(double vanDerWaalsB) {

    }

    public double getFormationEnthalpy() {

        return 0;

    }

    public void setFormationEnthalpy(double formationEnthalpy) {

    }

    public double getMeltingEnthalpy() {

        return 0;

    }

    public void setMeltingEnthalpy(double meltingEnthalpy) {

    }

    public double getFusionEnthalpy() {

        return 0;

    }

    public void setFusionEnthalpy(double fusionEnthalpy) {

    }

    public double getCombustionEnthaply() {

        return 0;

    }

    public void setCombustionEnthaply(double combustionEnthaply) {

    }

    public double getEntropy() {

        return 0;

    }

    public void setEntropy(double entropy) {

    }

    public double getMeltingEntropy() {

        return 0;

    }

    public void setMeltingEntropy(double meltingEntropy) {

    }

    public double getFusionEntropy() {

        return 0;

    }

    public void setFusionEntropy(double fusionEntropy) {

    }

    public double getSpecificHeat() {

        return 0;

    }

    public void setSpecificHeat(double specificHeat) {

    }

    /**
     * Returns the CAS registry number.
     */
    public String getCASRegistryNumber() {

        return null;

    }

    /**
     * Sets the CAS registry number. Empty means unknown but null is not allowed.
     */
    public void setCASRegistryNumber(String casRegistryNumber) {

    }

    public boolean isValidCASNumber() {

        return false;

    }

    /**
     * Checks wether the registry number is valid.
     * See <a href="http://www.cas.org/EO/checkdig.html">CAS website</a>.
     *
     * @cdk.keyword CAS number, validation
     */
    //copied from Chemistry Development Kit
    //http://cdk.sourceforge.net
    //by Egon Willighagen <egonw@sci.kun.nl>
    public static boolean isValidCASNumber(String casNumber) {

        return false;

    }

    //if molecule has received or lost charges
    public boolean isIon() {

        return false;

    }

    //gets the number of excess or missing positive charges
    //runs through all atoms of the molecule and counts for excessive or missing charge
    //overridden
    public double getCharge() {

        return 0;

    }

    //overridden
    private double setCharge(double charge) {

        return 0;

    }

    //retrieves all atoms as if they were unbound
    public Set getAtoms() {

        return null;

    }

    //looks for a specific atom
    public boolean containsAtom(Atom a) {

        return false;

    }

    public Set getBonds() {

        return null;

    }

    public boolean containsBond(Bond b) {

        return false;

    }

    //the energy from all bonds but not the energy of each atom
    public double computeBondingEnergy() {

        return 0;

    }

    //checks for identical formula
    public boolean isChemicalIsomer(Molecule m) {

        return false;

    }

    //checks for identical bonding
    //also known as structural isomer
    //you can test equality between two molecules with this method
    //bonding isomers always have the same formula (and therefore are chemical isomers
    public boolean isBondingIsomer(Molecule m) {

        return false;
    }

    //and maybe if anyone knows how to do this
    //public boolean isStereoIsomer(Molecule m)

    public Object clone() {

        return null;

    }

    public String toString() {

        return null;

    }

}
