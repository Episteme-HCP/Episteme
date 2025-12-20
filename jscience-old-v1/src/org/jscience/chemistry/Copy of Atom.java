package org.jscience.chemistry;


import org.jscience.physics.PhysicsConstants;
import org.jscience.physics.kinematics.ClassicalParticle3D;
import org.jscience.physics.particles.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A class representing atoms.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//TODO, the atom is currently defined as a classicle particle
//relativistic equations would be quite interesting to used if supported by classicle particle

//said in short, there is probably a lot of space here for additional support
//if you want to contribute to this class feel free to contribute

//perhaps we should review the concept of Bond not only as covalent bond but other bonds such as http://en.wikipedia.org/wiki/Noncovalent_bonding because user may want to have such a handy class (shall also include ionic bond, metallic bond, hydrogen bond)
//see http://en.wikipedia.org/wiki/Chemical_bond
public class CopyAtom extends ClassicalParticle3D {

    private Element element;
    private int isotope;

    private int excessElectrons;
    private Set bonds;

    private Molecule molecule;

    /**
     * Constructs an atom, using default isotope.
     */
    public Atom(Element e) {

        if (e != null) {
            element = e;
            this.isotope = e.getMassNumber();
            //set the mass and charge for a ClassicleParticle
            setMass(computeMass());
            setCharge(0);
            excessElectrons = 0;
            bonds = Collections.EMPTY_SET;
            molecule = null;
        } else
            throw new IllegalArgumentException("The Atom constructor doesn't accept null arguments.");

    }

    //constructs an atom using user supplied isotope (that is the mass number)
    public Atom(Element e, int isotope) {

        if (e != null) {
            element = e;
            if (e.getIsotopes().containsKey(new Integer(isotope))) {
                this.isotope = isotope;
                //set the mass and charge for a ClassicleParticle
                setMass(computeMass());
                setCharge(0);
                excessElectrons = 0;
                bonds = Collections.EMPTY_SET;
                molecule = null;
            } else
                throw new IllegalArgumentException("There is no isotope with such a value for this Element.");
        } else
            throw new IllegalArgumentException("The Atom constructor doesn't accept null arguments.");

    }

    //get the chemical element from which this Atom was built
    public Element getCorrespondingElement() {

        return element;

    }

    /**
     * Returns the nucleus.
     */
    public Nucleon[] getNucleus() {

        Nucleon[] nucleus;
        int i;

        nucleus = new Nucleon[isotope];
        for (i = 0; i < element.getAtomicNumber(); i++)
            nucleus[i] = new Proton();
        for (; i < isotope; i++)
            nucleus[i] = new Neutron();

        return nucleus;

    }

    /**
     * Returns the electron shell.
     */
    public Lepton[] getShell() {

        Lepton[] shell;

        shell = new Lepton[getNumElectrons()];
        for (int i = 0; i < getNumElectrons(); i++)
            shell[i] = new Electron();

        return shell;

    }

    //we should provide a method to compute the binding energy needed to maintain all these protons and neutrons together
    //with orbitting electrons by bringing them one by one from infinity to their final location
    public double computeExactMass() {
        XXX //http://hyperphysics.phy-astr.gsu.edu/hbase/nucene/nucbin.html
    }

    //this is the sum of the mass from all protons, electrons end neutrons
    //does not actually set the mass
    //this computation does not reflect any nucleon binding energy
    private double computeMass() {

        return (element.getAtomicNumber() * PhysicsConstants.PROTON_MASS) + ((isotope - element.getAtomicNumber()) * PhysicsConstants.NEUTRON_MASS) + (getNumElectrons() * PhysicsConstants.ELECTRON_MASS);

    }

    //the mass obtained by combining the isotopes and their abundance
    public double computeMeanMass() {
        XXX
    }

//actual electronic emission or capture is not performed in any way
//refer to the corresponding element or spectral subpackage for information on electronic energy levels
    //or look at http://physics.nist.gov/cgi-bin/AtData/main_asd
    getCurrentEnergyLevel

    //has at least one free electron to be bonded
    //using the electron valence from the element and the number of electrons in excess
    //as well as the current bonded pairs
    //we could compute a method with signature
    //public boolean hasFreeElectrons()

    //gets the number of excess or missing positive charges
    //for example Cl- returns +1 (as there is one extra electron)

    public int getExcessElectrons() {

        return excessElectrons;

    }

    public int getNumElectrons() {

        return element.getAtomicNumber() + getExcessElectrons();

    }

    //if atom has received or lost charges
    //the atom can nevertheless be bonded (this is the expected behavior)
    public boolean isIon() {

        return getExcessElectrons() != 0;

    }

    //as a result of ionisation, an atom may gain electrons for example from Cl to Cl-
    //of course the normal form for Cl is Cl2
    //atom should have free electrons (this is not checked)
    public void addElectron() {

        //change the classicle particle charge
        excessElectrons += 1;
        setCharge(excessElectrons * PhysicsConstants.CHARGE);

    }

    //as a result of ionisation, an atom may lose electrons for example from H to H+
    //of course the normal form for H is H2
    //you can always throw away electrons by firing high energy particles at the atom
    public void removeElectron() {

        //change the classicle particle charge
        excessElectrons -= 1;
        setCharge(excessElectrons * PhysicsConstants.CHARGE);

    }

    //getCharge should be understood here as the formal charge
    //overridden, use addElectron or removeElectron
    public void setCharge(double value) {

        super.setCharge(value);

    }

    //this should happen only with currently unbounded atoms or noble gaz particles (which are naturally unbounded)
    public boolean isAtomic() {

        return getBonds().size() == 0;

    }

    //the bonds from this atom
    public Set getBonds() {

        return bonds;

    }

    //the user has already checked that there was at least one free electron
    //adding a bond after promotion extends the molecule to the newly bonded atom
    public void addBond(Bond b) {

        if (b != null) {
            if ((b.getAtom1() == this) || (b.getAtom2() == this)) {
                bonds.add(b);
                if (b.getAtom1() == this) {
                    b.getAtom2().setPrivateMolecule(molecule);
                } else {//b.getAtom2()==this
                    b.getAtom1().setPrivateMolecule(molecule);
                }
            } else
                throw new IllegalArgumentException("You can't add a Bond not linked to this Atom.");
        } else
            throw new IllegalArgumentException("You can't add a null Bond.");

    }

    //we assume here that the bond exists
    private void removeBond(Bond b) {

        bonds.remove(b);

    }

    //break the bond from this atom to another
    //bond must exist
    //the Atom at the other side of the Bond is returned
    //this reduces the bonding level, actual Bond may still exist
    //remember that destroying bonds in the molecule after its creation actually change the molecule contents
    public Atom breakBond(Bond b) {

        if (b != null) {
            if (bonds.contains(b)) {
                if (b.getKind() == Bond.SIMPLE) {
                    bonds.remove(b);
                    if (b.getAtom1() == this) {
                        b.getAtom2().removeBond(b);
                        b.getAtom2().setPrivateMolecule(null);
                        return b.getAtom2();
                    } else {//b.getAtom2()==this
                        b.getAtom1().removeBond(b);
                        b.getAtom1().setPrivateMolecule(null);
                        return b.getAtom1();
                    }
                } else {
                    if (b.getKind() == Bond.DOUBLE) {
                        b.setKind(Bond.SIMPLE);
                    } else {//Triple.BOND
                        b.setKind(Bond.DOUBLE);
                    }
                    if (b.getAtom1() == this) {
                        return b.getAtom2();
                    } else {//b.getAtom2()==this
                        return b.getAtom1();
                    }
                }
            } else
                throw new IllegalArgumentException("You can't remove a Bond not linked to this atom.");
        } else
            throw new IllegalArgumentException("You can't remove a null Bond.");

    }

    //breaks all bondings, the two atoms are not linked anymore
    //bond must exist
    //the Atom at the other side of the Bond is returned
    //remember that destroying bonds in the molecule after its creation actually change the molecule contents
    public Atom breakFullBond(Bond b) {

        if (b != null) {
            if (bonds.contains(b)) {
                bonds.remove(b);
                if (b.getAtom1() == this) {
                    b.getAtom2().removeBond(b);
                    b.getAtom2().setPrivateMolecule(null);
                    return b.getAtom2();
                } else {//b.getAtom2()==this
                    b.getAtom1().removeBond(b);
                    b.getAtom1().setPrivateMolecule(null);
                    return b.getAtom1();
                }
            } else
                throw new IllegalArgumentException("You can't remove a Bond not linked to this atom.");
        } else
            throw new IllegalArgumentException("You can't remove a null Bond.");

    }

    //the direct atom neighbors
    //but not this Atom
    public Set getNeighbors() {

        Iterator iterator;
        HashSet result;
        Bond currentBond;

        iterator = bonds.iterator();
        result = new HashSet();
        while (iterator.hasNext()) {
            currentBond = (Bond) iterator.next();
            if (currentBond.getAtom1() == this) {
                result.add(currentBond.getAtom2());
            } else {//currentBond.getAtom2()==this
                result.add(currentBond.getAtom1());
            }
        }

        return result;

    }

    //get all atoms conected to this one
    //including this one
    public Set getAllNeighbors() {

        Iterator iterator1;
        Iterator iterator2;
        HashSet currentAtoms;
        HashSet checkedAtoms;
        HashSet newAtoms;
        Atom currentAtom;
        Atom newAtom;
        Bond currentBond;

        currentAtoms = new HashSet();
        currentAtoms.add(this);
        checkedAtoms = new HashSet();
        while (currentAtoms.size() > 0) {
            iterator1 = currentAtoms.iterator();
            newAtoms = new HashSet();
            while (iterator1.hasNext()) {
                currentAtom = (Atom) iterator1.next();
                iterator2 = currentAtom.getBonds().iterator();
                while (iterator2.hasNext()) {
                    currentBond = ((Bond) iterator2.next());
                    if (currentBond.getAtom1() == currentAtom) {
                        newAtom = currentBond.getAtom2();
                    } else {//currentBond.getAtom2()== currentAtom
                        newAtom = currentBond.getAtom1();
                    }
                    if (!checkedAtoms.contains(newAtom)) {
                        newAtoms.add(newAtom);
                    }
                }
            }
            checkedAtoms.addAll(currentAtoms);
            currentAtoms = newAtoms;
        }

        return checkedAtoms;

    }

    public boolean isInMolecule() {

        return molecule != null;

    }

    //may return null
    public Molecule getMolecule() {

        return molecule;

    }

    private void setPrivateMolecule(Molecule molecule) {

        this.molecule = molecule;

    }

    //you may set null
    protected void setMolecule(Molecule molecule) {

        Iterator iterator;

        iterator = getAllNeighbors().iterator();
        while (iterator.hasNext()) {
            ((Atom) iterator.next()).setPrivateMolecule(molecule);
        }

    }

    //checks that all the Atoms connected to this atom including this one are fully bonded or miss some charge
    /**
     * public boolean isMolecule() {
     * <p/>
     * Iterator iterator;
     * boolean found;
     * <p/>
     * //check that there is no unbounded atom
     * iterator = getAllNeighbors().iterator();
     * found = false;
     * while (iterator.hasNext() && !found) {
     * found = ((Atom)iterator.next()).hasFreeElectrons();
     * }
     * <p/>
     * return !found;
     * <p/>
     * }
     */

    //a molecule is a view on some atomic structure
    //if the atom is fully bonded and all bonded atoms are themselves fully bonded then this atom can be viewed as a molecule and therefore promoted
    //remember that destroying bonds in the molecule after its creation actually change the molecule contents
    //also note that even if a group of bonded atoms are by design a molecule they won't be considered as such until actually be promotted to the Molecule status
    //this must be done manually
    public Molecule promoteAtom() {

        //if (isMolecule()) {
        return new Molecule(this);
        //} else throw new IllegalArgumentException("You can't promote an Atom that is not fully bonded or is a ion.");

    }

}
