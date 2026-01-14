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

package org.jscience.chemistry;

//import com.dautelle.physics.*;
//import com.dautelle.physics.models.*;

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
public class Atom extends Object {


    /**
     * Constructs an atom, using default isotope.
     */
    public Atom(Element e) {


    }

    //constructs an atom using user supplied isotope (that is the mass number)
    public Atom(Element e, int isotope) {


    }

    //get the chemical element from which this Atom was built
    public Element getCorrespondingElement() {

        return null;

    }

    //we should provide a method to compute the binding energy needed to maintain all these protons and neutrons together
    //with orbitting electrons by bringing them one by one from infinity to their final location
    public double computeExactMass() {

        return 0;

    }

    //this is the sum of the mass from all protons, electrons end neutrons
    //does not actually set the mass
    //this computation does not reflect any nucleon binding energy
    private double computeMass() {

        return 0;

    }

    //the mass obtained by combining the isotopes and their abundance
    public double computeMeanMass() {
        return 0;
    }

//actual electronic emission or capture is not performed in any way
//refer to the corresponding element or spectral subpackage for information on electronic energy levels
    //or look at http://physics.nist.gov/cgi-bin/AtData/main_asd

    //has at least one free electron to be bonded
    //using the electron valence from the element and the number of electrons in excess
    //as well as the current bonded pairs
    //we could compute a method with signature
    //public boolean hasFreeElectrons()

    //gets the number of excess or missing positive charges
    //for example Cl- returns +1 (as there is one extra electron)

    public int getExcessElectrons() {

        return 0;

    }

    public int getNumElectrons() {

        return 0;

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

    }

    //as a result of ionisation, an atom may lose electrons for example from H to H+
    //of course the normal form for H is H2
    //you can always throw away electrons by firing high energy particles at the atom
    public void removeElectron() {

    }


    //this should happen only with currently unbounded atoms or noble gaz particles (which are naturally unbounded)
    public boolean isAtomic() {

        return getBonds().size() == 0;

    }

    public void setCharge(int value) {

    }

    //the bonds from this atom
    public Set getBonds() {

        return null;

    }

    //the user has already checked that there was at least one free electron
    //adding a bond after promotion extends the molecule to the newly bonded atom
    public void addBond(Bond b) {

    }

    //break the bond from this atom to another
    //bond must exist
    //the Atom at the other side of the Bond is returned
    //this reduces the bonding level, actual Bond may still exist
    //remember that destroying bonds in the molecule after its creation actually change the molecule contents
    public Atom breakBond(Bond b) {

        return null;

    }

    //breaks all bondings, the two atoms are not linked anymore
    //bond must exist
    //the Atom at the other side of the Bond is returned
    //remember that destroying bonds in the molecule after its creation actually change the molecule contents
    public Atom breakFullBond(Bond b) {

        return null;
    }

    //the direct atom neighbors
    //but not this Atom
    public Set getNeighbors() {

        return null;

    }

    //get all atoms conected to this one
    //including this one
    public Set getAllNeighbors() {

        return null;

    }

    public boolean isInMolecule() {

        return false;

    }

    //may return null
    public Molecule getMolecule() {

        return null;

    }

    //you may set null
    protected void setMolecule(Molecule molecule) {

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

        return null;

    }

}
