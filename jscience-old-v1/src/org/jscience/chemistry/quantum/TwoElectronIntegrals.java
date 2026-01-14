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
