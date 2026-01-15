/*
 * BasisFunctions.java
 *
 * Created on July 26, 2004, 7:04 AM
 */
package org.jscience.chemistry.quantum;

import org.jscience.chemistry.quantum.basis.*;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Class to construct basis functions of a given molecule and a basis set
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class BasisFunctions {
    /** Holds value of property basisFunctions. */
    private ArrayList basisFunctions;

/**
     * Creates a new instance of BasisFunctions
     *
     * @param molecule  the Molecule whose basis function is requested
     * @param basisName the name of the basis set (like sto3g)
     * @throws Exception DOCUMENT ME!
     */
    public BasisFunctions(Molecule molecule, String basisName)
        throws Exception {
        // initilize the basis functions
        getBasisFunctions(molecule, basisName);
    }

    /**
     * Getter for property basisFunctions.
     *
     * @return Value of property basisFunctions.
     */
    public ArrayList getBasisFunctions() {
        return this.basisFunctions;
    }

    /**
     * Getter for property basisFunctions.
     *
     * @param molecule the Molecule whose basis function is requested
     * @param basisName the name of the basis set (like sto3g)
     *
     * @return Value of property basisFunctions.
     *
     * @throws Exception DOCUMENT ME!
     */
    private ArrayList getBasisFunctions(Molecule molecule, String basisName)
        throws Exception {
        BasisSet basis = BasisReader.getInstance().readBasis(basisName);
        Iterator atoms = molecule.getAtoms();

        basisFunctions = new ArrayList();

        Atom atom;
        AtomicBasis atomicBasis;

        while (atoms.hasNext()) { // loop over atoms
            atom = (Atom) atoms.next();
            atomicBasis = basis.getAtomicBasis(atom.getSymbol());

            Iterator orbitals = atomicBasis.getOrbitals().iterator();
            Orbital orbital;

            while (orbitals.hasNext()) { // loop over atom orbitals
                orbital = (Orbital) orbitals.next();

                Iterator pList = PowerList.getInstance()
                                          .getPowerList(orbital.getType());
                Power power;

                while (pList.hasNext()) { // and the power list, sp2 etc..
                    power = (Power) pList.next();

                    ContractedGaussian cg = new ContractedGaussian(atom.getAtomCenter(),
                            power);
                    Iterator coeff = orbital.getCoefficients().iterator();
                    Iterator exp = orbital.getExponents().iterator();

                    while (coeff.hasNext()) { // build the CG from PGs
                        cg.addPrimitive(((Double) exp.next()).doubleValue(),
                            ((Double) coeff.next()).doubleValue());
                    } // end while

                    cg.normalize();
                    basisFunctions.add(cg); // add this CG to list
                } // end while
            } // end while
        } // end while

        return this.basisFunctions;
    }
} // end of class BasisFunctions
