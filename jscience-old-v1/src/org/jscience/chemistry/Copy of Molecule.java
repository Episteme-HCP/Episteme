package org.jscience.chemistry;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class CopyMolecule extends RigidBody3D implements Cloneable, Serializable {

    private Atom a;

    private double meltingPoint;
    private double boilingPoint;
    private double density;
    private double vanDerWaalsA;//coefficient A
    private double vanDerWaalsB;//coefficient B

    private double molecularWeight;


    private double formationEnthalpy;
    private double meltingEnthalpy;
    private double fusionEnthalpy;
    private double combustionEnthaply;
    private double entropy;
    private double meltingEntropy;
    private double fusionEntropy;
    private double specificHeat;

    private String casRegistryNumber;

    //breaking a bond from this molecule after creation actually destroys the molecule (see Atom)
    protected Molecule(Atom a) {

        Iterator iterator;

        if (a != null) {
            //checks that all atoms are fully bonded
            //if (a.isMolecule()) {
            //sends a signal to all atoms so that they know they are part of a molecule
            a.setMolecule(this);
            meltingPoint = 0;
            boilingPoint = 0;
            density = 0;
            vanDerWaalsA = 0;
            vanDerWaalsB = 0;
            molecularWeight = 0;
            formationEnthalpy = 0;
            meltingEnthalpy = 0;
            fusionEnthalpy = 0;
            combustionEnthaply = 0;
            entropy = 0;
            meltingEntropy = 0;
            fusionEntropy = 0;
            specificHeat = 0;
            casRegistryNumber = new String();
            //} else throw new IllegalArgumentException("You can't promote an Atom that is not fully bonded or is a ion.");
        } else
            throw new IllegalArgumentException("The Molecule constructor doesn't accept null arguments.");

    }

    /**
     * Returns the formula in this molecule.
     */
    public String getFormula() {

        Hashtable contents;
        Enumeration enumeration;
        Iterator iterator;
        Element currentElement;
        String result;

        iterator = getAtoms().iterator();
        contents = new Hashtable();
        while (iterator.hasNext()) {
            currentElement = ((Atom) iterator.next()).getCorrespondingElement();
            if (contents.containsKey(currentElement)) {
                contents.put(currentElement, new Integer(((Integer) contents.get(currentElement)).intValue()++));
            } else {
                contents.put(currentElement, new Integer(1));
            }
        }
        enumeration = contents.keys();
        result = new String();
        while (enumeration.hasMoreElements()) {
            currentElement = (Element) enumeration.nextElement();
            result.concat(currentElement.toString());
            result.concat(((Integer) contents.get(currentElement)).toString());
        }

        return result;

    }

    /**
     * @return
     */
    public double computeMolecularWeight() {

        Iterator iterator;
        double result;

        iterator = getAtoms().iterator();
        result = 0;
        while (iterator.hasNext()) {
            result += ((Atom) iterator.next()).getMass();
        }

        return result;

    }

    public double getMolecularWeight() {

        return molecularWeight;

    }

    public void setMolecularWeight(double molecularWeight) {

        this.molecularWeight = molecularWeight;

    }

    public double getMeltingPoint() {

        return meltingPoint;

    }

    public void setMeltingPoint(double meltingPoint) {

        this.meltingPoint = meltingPoint;

    }

    public double getBoilingPoint() {

        return boilingPoint;

    }

    public void setBoilingPoint(double boilingPoint) {

        this.boilingPoint = boilingPoint;

    }

    public double getDensity() {

        return density;

    }

    public void setDensity(double density) {

        this.density = density;

    }

    public double getVanDerWaalsA() {

        return vanDerWaalsA;

    }

    public void setVanDerWaalsA(double vanDerWaalsA) {

        this.vanDerWaalsA = vanDerWaalsA;

    }

    public double getVanDerWaalsB() {

        return vanDerWaalsB;

    }

    public void setVanDerWaalsB(double vanDerWaalsB) {

        this.vanDerWaalsB = vanDerWaalsB;

    }

    public double getFormationEnthalpy() {

        return formationEnthalpy;

    }

    public void setFormationEnthalpy(double formationEnthalpy) {

        this.formationEnthalpy = formationEnthalpy;

    }

    public double getMeltingEnthalpy() {

        return meltingEnthalpy;

    }

    public void setMeltingEnthalpy(double meltingEnthalpy) {

        this.meltingEnthalpy = meltingEnthalpy;

    }

    public double getFusionEnthalpy() {

        return fusionEnthalpy;

    }

    public void setFusionEnthalpy(double fusionEnthalpy) {

        this.fusionEnthalpy = fusionEnthalpy;

    }

    public double getCombustionEnthaply() {

        return combustionEnthaply;

    }

    public void setCombustionEnthaply(double combustionEnthaply) {

        this.combustionEnthaply = combustionEnthaply;

    }

    public double getEntropy() {

        return entropy;

    }

    public void setEntropy(double entropy) {

        this.entropy = entropy;

    }

    public double getMeltingEntropy() {

        return meltingEntropy;

    }

    public void setMeltingEntropy(double meltingEntropy) {

        this.meltingEntropy = meltingEntropy;

    }

    public double getFusionEntropy() {

        return fusionEntropy;

    }

    public void setFusionEntropy(double fusionEntropy) {

        this.fusionEntropy = fusionEntropy;

    }

    public double getSpecificHeat() {

        return specificHeat;

    }

    public void setSpecificHeat(double specificHeat) {

        this.specificHeat = specificHeat;

    }

    /**
     * Returns the CAS registry number.
     */
    public String getCASRegistryNumber() {

        return casRegistryNumber;

    }

    /**
     * Sets the CAS registry number. Empty means unknown but null is not allowed.
     */
    public void setCASRegistryNumber(String casRegistryNumber) {

        if (casRegistryNumber != null) {
            this.casRegistryNumber = casRegistryNumber;
        } else
            throw new IllegalArgumentException("The CAS number can't be null.");

    }

    public boolean isValidCASNumber() {

        return isValidCASNumber(getCASRegistryNumber());

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
        boolean overall = true;
        /*
        * check format
        */
        String format = "^(\\d+)-(\\d\\d)-(\\d)$";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(casNumber);
        overall = overall && matcher.matches();

        if (matcher.matches()) {
            /*
            * check number
            */
            String part1 = matcher.group(1);
            String part2 = matcher.group(2);
            String part3 = matcher.group(3);
            int part1value = Integer.parseInt(part1);
            if (part1value < 50) {
                overall = false;
                // CAS numbers start at 50-00-0
            } else {
                int total = 0;
                total = total + 1 * Integer.parseInt(part2.substring(1, 2));
                total = total + 2 * Integer.parseInt(part2.substring(0, 1));
                int length = part1.length();
                for (int i = 0; i < length; i++) {
                    total = total + (3 + i) * Integer.parseInt(part1.substring(length - 1 - i, length - i));
                }
                int digit = total % 10;
                overall = overall && (digit == Integer.parseInt(part3));
            }
        }

        return overall;

    }

    //if molecule has received or lost charges
    public boolean isIon() {

        return getCharge() != 0;

    }

    //gets the number of excess or missing positive charges
    //runs through all atoms of the molecule and counts for excessive or missing charge
    //overridden
    public double getCharge() {

        Iterator iterator;
        int result;

        iterator = a.getAllNeighbors().iterator();
        result = 0;
        while (iterator.hasNext()) {
            result += ((Atom) iterator.next()).getExcessElectrons();
        }

        return result;

    }

    //overridden
    private double setCharge(double charge) {

        super.setCharge(charge);

    }

    //retrieves all atoms as if they were unbound
    public Set getAtoms() {

        return a.getAllNeighbors();

    }

    //looks for a specific atom
    public boolean containsAtom(Atom a) {

        return getAtoms().contains(a);

    }

    public Set getBonds() {

        Iterator iterator;
        HashSet result;

        iterator = getAtoms().iterator();
        result = new HashSet();
        while (iterator.hasNext()) {
            result.addAll(((Atom) iterator.next()).getBonds());
        }

        return result;

    }

    public boolean containsBond(Bond b) {

        return getBonds().contains(b);

    }

    //the energy from all bonds but not the energy of each atom
    public double computeBondingEnergy() {

        Iterator iterator;
        double result;

        iterator = getBonds().iterator();
        result = 0;
        while (iterator.hasNext()) {
            result += ((Bond) iterator.next()).getEnergy();
        }

        return result;

    }

    //checks for identical formula
    public boolean isChemicalIsomer(Molecule m) {

        return (m != null) && m.getFormula().equals(this.getFormula());

    }

    //checks for identical bonding
    //also known as structural isomer
    //you can test equality between two molecules with this method
    //bonding isomers always have the same formula (and therefore are chemical isomers
    public boolean isBondingIsomer(Molecule m) {

        Iterator iterator;
        Atom startingAtom;
        Atom currentAtom;
        boolean result;

        if (m != null) {
            //iterate through these atoms and find if there is one for which all the bonds are the same
            iterator = getAtoms().iterator();
            result = false;
            startingAtom = (Atom) m.getAtoms().iterator().next();
            while (iterator.hasNext() && !result) {
                currentAtom = (Atom) iterator.next();
                result = isBondingIsomer(m, currentAtom);
            }

            return result;

        } else
            throw new IllegalArgumentException("You can't check for bonding isomer with a null Molecule.");

    }

    private boolean isBondingIsomer(Molecule m, Atom a) {

        XX

    }

    //and maybe if anyone knows how to do this
    //public boolean isStereoIsomer(Molecule m)

    public Object clone() {

        Molecule molecule;

        molecule = new Molecule(((Atom) getAtoms().iterator().next()));
        molecule.setMeltingPoint(getMeltingPoint());
        molecule.setBoilingPoint(getBoilingPoint());
        molecule.setDensity(getDensity());
        molecule.setVanDerWaalsA(getVanDerWaalsA());
        molecule.setVanDerWaalsB(getVanDerWaalsB());
        molecule.setFormationEnthalpy(getFormationEnthalpy());
        molecule.setMeltingEnthalpy(getMeltingEnthalpy());
        molecule.setFusionEnthalpy(getFusionEnthalpy());
        molecule.setCombustionEnthaply(getCombustionEnthaply());
        molecule.setEntropy(getEntropy());
        molecule.setMeltingEntropy(getMeltingEntropy());
        molecule.setFusionEntropy(getFusionEntropy());
        molecule.setSpecificHeat(getSpecificHeat());
        molecule.setCASRegistryNumber(getCASRegistryNumber());

        return molecule;

    }

    public String toString() {

        return getFormula();

    }

}
