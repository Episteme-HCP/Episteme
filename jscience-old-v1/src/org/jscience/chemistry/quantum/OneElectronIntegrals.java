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

/*
 * OneElectronIntegrals.java
 *
 * Created on July 28, 2004, 7:03 AM
 */
package org.jscience.chemistry.quantum;

import org.jscience.chemistry.quantum.basis.ContractedGaussian;
import org.jscience.chemistry.quantum.math.matrix.Matrix;

import java.util.ArrayList;


/**
 * The 1E integral (overlap S matrix) and 1E Hamiltonian matrices driver.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class OneElectronIntegrals {
    /** Holds value of property overlap. */
    private Matrix overlap;

    /** Holds value of property hamiltonian. */
    private Matrix hamiltonian;

    /** DOCUMENT ME! */
    private BasisFunctions basisFunctions;

    /** DOCUMENT ME! */
    private Molecule molecule;

/**
     * Creates a new instance of OneElectronIntegrals
     *
     * @param basisFunctions the basis functions to be used
     * @param mol            the Molecule object, of whose 1E integrals are to be
     *                       evaliated
     */
    public OneElectronIntegrals(BasisFunctions basisFunctions, Molecule mol) {
        this.basisFunctions = basisFunctions;
        this.molecule = mol;

        // compute the 1E integrals, form S matrix and hamiltonian
        compute1E();
    }

    /**
     * compute the 1E integrals, form S matrix and hamiltonian
     */
    protected void compute1E() {
        ArrayList bfs = basisFunctions.getBasisFunctions();
        int noOfBasisFunctions = bfs.size();
        int i;
        int j;
        int k;

        // allocate memory
        this.overlap = new Matrix(noOfBasisFunctions);
        this.hamiltonian = new Matrix(noOfBasisFunctions);

        double[][] overlap = this.overlap.getMatrix();
        double[][] hamiltonian = this.hamiltonian.getMatrix();

        // read in the atomic numbers
        int[] atomicNumbers = new int[molecule.getNumberOfAtoms()];
        AtomInfo ai = AtomInfo.getInstance();

        for (i = 0; i < atomicNumbers.length; i++) {
            atomicNumbers[i] = ai.getAtomicNumber(molecule.getAtom(i).getSymbol());
        } // end for

        ContractedGaussian bfi;
        ContractedGaussian bfj;

        // set up the S matrix and the hamiltonian h
        for (i = 0; i < noOfBasisFunctions; i++) {
            bfi = (ContractedGaussian) bfs.get(i);

            for (j = 0; j < noOfBasisFunctions; j++) {
                bfj = (ContractedGaussian) bfs.get(j);

                overlap[i][j] = bfi.overlap(bfj); // the overlap matrix
                hamiltonian[i][j] = bfi.kinetic(bfj); // KE matrix elements

                for (k = 0; k < atomicNumbers.length; k++) {
                    hamiltonian[i][j] += (atomicNumbers[k] * bfi.nuclear(bfj,
                        molecule.getAtom(k).getAtomCenter()));
                } // end for
            } // end for
        } // end for
    }

    /**
     * Getter for property overlap.
     *
     * @return Value of property overlap.
     */
    public Matrix getOverlap() {
        return this.overlap;
    }

    /**
     * Getter for property hamiltonian.
     *
     * @return Value of property hamiltonian.
     */
    public Matrix getHamiltonian() {
        return this.hamiltonian;
    }
} // emd of class OneElectronIntegrals
