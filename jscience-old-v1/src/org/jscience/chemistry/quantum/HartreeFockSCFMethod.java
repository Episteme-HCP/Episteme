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
 * HartreeFockSCFMethod.java
 *
 * Created on August 6, 2004, 7:36 PM
 */
package org.jscience.chemistry.quantum;

import org.jscience.chemistry.quantum.event.SCFEvent;
import org.jscience.chemistry.quantum.integral.Integrals;
import org.jscience.chemistry.quantum.math.la.Diagonalizer;
import org.jscience.chemistry.quantum.math.la.DiagonalizerFactory;
import org.jscience.chemistry.quantum.math.matrix.Matrix;
import org.jscience.chemistry.quantum.math.vector.VectorND;


/**
 * Implements the Hartree-Fock (HF) SCF method for single point energy
 * evaluation of a molecule.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class HartreeFockSCFMethod extends SCFMethod {
    /** DOCUMENT ME! */
    protected SCFEvent scfEvent;

/**
     * Creates a new instance of HartreeFockSCFMethod
     *
     * @param molecule DOCUMENT ME!
     * @param oneEI    DOCUMENT ME!
     * @param twoEI    DOCUMENT ME!
     */
    public HartreeFockSCFMethod(Molecule molecule, OneElectronIntegrals oneEI,
        TwoElectronIntegrals twoEI) {
        super(molecule, oneEI, twoEI);

        scfEvent = new SCFEvent(this);
    }

    /**
     * Perform the SCF optimization of the molecular wave function
     * until the energy converges.
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public void scf() {
        // check first if closed shell run?
        int noOfElectrons = molecule.getNumberOfElectrons();
        int noOfOccupancies = noOfElectrons / 2;

        if ((noOfElectrons % 2) != 0) {
            // its open shell ... we do not support this
            throw new UnsupportedOperationException("Open shell systems are" +
                " not currently supported.");
        } // end if

        Matrix hamiltonian = oneEI.getHamiltonian();
        Matrix overlap = oneEI.getOverlap();

        boolean converged = false;
        double oldEnergy = 0.0;
        double energy = 0.0;
        double nuclearEnergy = nuclearEnergy();
        double eOne;
        double eTwo;

        // compute initial MOs
        computeOrbEAndS(hamiltonian, overlap);

        // start the SCF cycle
        for (scfIteration = 0; scfIteration < maxIteration; scfIteration++) {
            // make or guess density
            makeDensity(noOfOccupancies);

            // make the G matrix
            Matrix theGMatrix = makeGMatrix();

            // make fock matrix
            fock = hamiltonian.add(theGMatrix);

            // compute the new MOs
            computeOrbEAndS(fock, overlap);

            // compute the total energy at this point
            eOne = density.mul(hamiltonian).trace();
            eTwo = density.mul(fock).trace();

            energy = eOne + eTwo + nuclearEnergy;

            // fire the SCF event notification
            scfEvent.setType(SCFEvent.INFO_EVENT);
            scfEvent.setCurrentIteration(scfIteration);
            scfEvent.setCurrentEnergy(energy);
            fireSCFEventListenerScfEventOccured(scfEvent);

            // ckeck for convergence
            if (Math.abs(energy - oldEnergy) < energyTolerance) {
                converged = true;
                scfEvent.setType(SCFEvent.CONVERGED_EVENT);
                scfEvent.setCurrentIteration(scfIteration);
                scfEvent.setCurrentEnergy(energy);
                fireSCFEventListenerScfEventOccured(scfEvent);

                break;
            } // end if

            oldEnergy = energy;
        } // end of SCF iteration

        // not converged? then inform so...
        if (!converged) {
            scfEvent.setType(SCFEvent.FAILED_CONVERGENCE_EVENT);
            scfEvent.setCurrentIteration(scfIteration);
            scfEvent.setCurrentEnergy(energy);
            fireSCFEventListenerScfEventOccured(scfEvent);
        } // end if
    }

    /**
     * Compute the MOS... and eigen values
     *
     * @param hamiltonian DOCUMENT ME!
     * @param overlap DOCUMENT ME!
     */
    private void computeOrbEAndS(Matrix hamiltonian, Matrix overlap) {
        Matrix x = overlap.symmetricOrthogonalization();
        Matrix a = hamiltonian.similarityTransform(x);
        Diagonalizer diag = DiagonalizerFactory.getInstance()
                                               .getDefaultDiagonalizer();
        diag.diagonalize(a);

        orbE = diag.getEigenValues();
        mos = diag.getEigenVectors().mul(x);
    }

    /**
     * Make the density matrix
     *
     * @param noOfOccupancies DOCUMENT ME!
     */
    protected void makeDensity(int noOfOccupancies) {
        if (guessInitialDM && (scfIteration == 0)) {
            if (densityGuesser != null) {
                density = densityGuesser.guessDM(this);

                return;
            } // end if
        } // end if

        // else construct it from the MOs .. C*C'
        Matrix dVector = new Matrix(noOfOccupancies, mos.getRowCount());
        double[][] d = dVector.getMatrix();

        int i;
        int j;

        for (i = 0; i < noOfOccupancies; i++)
            for (j = 0; j < mos.getRowCount(); j++)
                d[i][j] = mos.getMatrixAt(i, j);

        density = dVector.transpose().mul(dVector);
    }

    /**
     * Make the G matrix <br>
     * i.e. Form the 2J-K integrals corresponding to a density matrix
     *
     * @return DOCUMENT ME!
     */
    protected Matrix makeGMatrix() {
        int noOfBasisFunctions = density.getRowCount();
        Matrix theGMatrix = new Matrix(noOfBasisFunctions);
        VectorND densityOneD = new VectorND(density); // form 1D vector of density
        VectorND tempVector = new VectorND(noOfBasisFunctions * noOfBasisFunctions);

        double[][] gMatrix = theGMatrix.getMatrix();
        double[] ints = twoEI.getTwoEIntegrals();
        double[] temp = tempVector.getVector();

        int i;
        int j;
        int k;
        int l;
        int kl;
        int indexJ;
        int indexK1;
        int indexK2;

        for (i = 0; i < noOfBasisFunctions; i++) {
            for (j = 0; j < (i + 1); j++) {
                tempVector.makeZero();
                kl = 0;

                for (k = 0; k < noOfBasisFunctions; k++) {
                    for (l = 0; l < noOfBasisFunctions; l++) {
                        indexJ = Integrals.ijkl2intindex(i, j, k, l);
                        indexK1 = Integrals.ijkl2intindex(i, k, j, l);
                        indexK2 = Integrals.ijkl2intindex(i, l, k, j);
                        temp[kl] = (2.0 * ints[indexJ]) -
                            (0.5 * ints[indexK1]) - (0.5 * ints[indexK2]);
                        kl++;
                    } // end l loop
                } // end k loop

                gMatrix[i][j] = gMatrix[j][i] = tempVector.dot(densityOneD);
            } // end j loop
        } // end i loop

        return theGMatrix;
    }
} // end of class HartreeFockSCFMethod
