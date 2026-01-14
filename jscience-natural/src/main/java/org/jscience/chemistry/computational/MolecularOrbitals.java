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

package org.jscience.chemistry.computational;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Molecular orbital calculations using HÃƒÂ¼ckel theory.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MolecularOrbitals {

    private final Real alpha;
    private final Real beta;

    public MolecularOrbitals(Real alpha, Real beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    public MolecularOrbitals(double alpha, double beta) {
        this(Real.of(alpha), Real.of(beta));
    }

    public MolecularOrbitals() {
        this(-6.0, -2.5);
    }

    /** Linear chain energies: E_k = ÃŽÂ± + 2ÃŽÂ²Ã‚Â·cos(kÃâ‚¬/(n+1)) */
    public Real[] linearChainEnergies(int numAtoms) {
        Real[] energies = new Real[numAtoms];
        for (int k = 1; k <= numAtoms; k++) {
            Real cosArg = Real.of(k).multiply(Real.PI).divide(Real.of(numAtoms + 1)).cos();
            energies[k - 1] = alpha.add(Real.TWO.multiply(beta).multiply(cosArg));
        }
        return energies;
    }

    /** Cyclic energies: E_k = ÃŽÂ± + 2ÃŽÂ²Ã‚Â·cos(2Ãâ‚¬k/n) */
    public Real[] cyclicEnergies(int numAtoms) {
        Real[] energies = new Real[numAtoms];
        for (int k = 0; k < numAtoms; k++) {
            Real cosArg = Real.TWO.multiply(Real.PI).multiply(Real.of(k)).divide(Real.of(numAtoms)).cos();
            energies[k] = alpha.add(Real.TWO.multiply(beta).multiply(cosArg));
        }
        java.util.Arrays.sort(energies, (a, b) -> a.compareTo(b));
        return energies;
    }

    /** Total Ãâ‚¬-electron energy */
    public Real totalPiEnergy(Real[] orbitalEnergies, int numElectrons) {
        Real total = Real.ZERO;
        int electrons = numElectrons;
        for (Real e : orbitalEnergies) {
            if (electrons >= 2) {
                total = total.add(Real.TWO.multiply(e));
                electrons -= 2;
            } else if (electrons == 1) {
                total = total.add(e);
                electrons--;
            } else
                break;
        }
        return total;
    }

    /** Delocalization energy */
    public Real delocalizationEnergy(Real actualEnergy, int numDoubleBonds) {
        Real localizedEnergy = Real.of(numDoubleBonds * 2).multiply(alpha.add(beta));
        return actualEnergy.subtract(localizedEnergy);
    }

    /** HOMO-LUMO gap */
    public Real homoLumoGap(Real[] orbitalEnergies, int numElectrons) {
        int homoIndex = (numElectrons + 1) / 2 - 1;
        int lumoIndex = homoIndex + 1;
        if (lumoIndex >= orbitalEnergies.length)
            return Real.of(Double.NaN);
        return orbitalEnergies[lumoIndex].subtract(orbitalEnergies[homoIndex]);
    }

    public static boolean isAromatic(int numPiElectrons) {
        return (numPiElectrons - 2) % 4 == 0 && numPiElectrons >= 2;
    }

    public static boolean isAntiaromatic(int numPiElectrons) {
        return numPiElectrons % 4 == 0 && numPiElectrons > 0;
    }

    public Real[] benzeneEnergies() {
        return cyclicEnergies(6);
    }

    public Real[] butadieneEnergies() {
        return linearChainEnergies(4);
    }

    public Real getAlpha() {
        return alpha;
    }

    public Real getBeta() {
        return beta;
    }
}


