/*
 * TwoElectronIntegrals.java
 *
 * Created on July 28, 2004, 7:03 AM
 */
package org.jscience.chemistry.quantum;

import org.jscience.chemistry.quantum.basis.ContractedGaussian;
import org.jscience.chemistry.quantum.integral.Integrals;

import java.util.ArrayList;


/**
 * The 2E integral driver.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class TwoElectronIntegrals {
    /** DOCUMENT ME! */
    private BasisFunctions basisFunctions;

    /** Holds value of property twoEIntegrals. */
    private double[] twoEIntegrals;

/**
     * Creates a new instance of TwoElectronIntegrals
     *
     * @param basisFunctions the basis functions to be used
     */
    public TwoElectronIntegrals(BasisFunctions basisFunctions) {
        this.basisFunctions = basisFunctions;

        // compute the 2E integrals
        compute2E();
    }

    /**
     * compute the 2E integrals, and store it in a single 1D array, in
     * the form [ijkl].
     */
    protected void compute2E() {
        ArrayList bfs = basisFunctions.getBasisFunctions();

        // allocate required memory
        int noOfBasisFunctions = bfs.size();
        int noOfIntegrals = (noOfBasisFunctions * (noOfBasisFunctions + 1) * ((noOfBasisFunctions * noOfBasisFunctions) +
            noOfBasisFunctions + 2)) / 8;

        twoEIntegrals = new double[noOfIntegrals];

        int i;
        int j;
        int k;
        int l;
        int ij;
        int kl;
        int ijkl;

        ContractedGaussian bfi;
        ContractedGaussian bfj;
        ContractedGaussian bfk;
        ContractedGaussian bfl;

        // we only need i <= j, k <= l, and ij <= kl
        for (i = 0; i < noOfBasisFunctions; i++) {
            bfi = (ContractedGaussian) bfs.get(i);

            for (j = 0; j < (i + 1); j++) {
                bfj = (ContractedGaussian) bfs.get(j);
                ij = ((i * (i + 1)) / 2) + j;

                for (k = 0; k < noOfBasisFunctions; k++) {
                    bfk = (ContractedGaussian) bfs.get(k);

                    for (l = 0; l < (k + 1); l++) {
                        bfl = (ContractedGaussian) bfs.get(l);

                        kl = ((k * (k + 1)) / 2) + l;

                        if (ij >= kl) {
                            ijkl = Integrals.ijkl2intindex(i, j, k, l);

                            // record the 2E integrals
                            twoEIntegrals[ijkl] = Integrals.coulomb(bfi, bfj,
                                    bfk, bfl);
                        } // end if
                    } // end l loop
                } // end k loop
            } // end of j loop
        } // end of i loop
    }

    /**
     * Getter for property twoEIntegrals.
     *
     * @return Value of property twoEIntegrals.
     */
    public double[] getTwoEIntegrals() {
        return this.twoEIntegrals;
    }

    /**
     * Setter for property twoEIntegrals.
     *
     * @param twoEIntegrals New value of property twoEIntegrals.
     */
    public void setTwoEIntegrals(double[] twoEIntegrals) {
        this.twoEIntegrals = twoEIntegrals;
    }
} // end of class TwoElectronIntegrals
