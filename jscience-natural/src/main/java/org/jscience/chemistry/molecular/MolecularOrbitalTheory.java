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

package org.jscience.chemistry.molecular;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MolecularOrbitalTheory {

    private MolecularOrbitalTheory() {
    }

    /**
     * Calculates bond order.
     * Bond Order = (bonding eÃ¢ÂÂ» - antibonding eÃ¢ÂÂ») / 2
     */
    public static double bondOrder(int bondingElectrons, int antibondingElectrons) {
        return (bondingElectrons - antibondingElectrons) / 2.0;
    }

    /**
     * Predicts if molecule is paramagnetic (has unpaired electrons).
     */
    public static boolean isParamagnetic(int unpairedElectrons) {
        return unpairedElectrons > 0;
    }

    /**
     * HOMO-LUMO gap estimation (simplified).
     * Larger gap = more stable, less reactive.
     * 
     * @param homoEnergy HOMO energy (eV)
     * @param lumoEnergy LUMO energy (eV)
     * @return Gap in eV
     */
    public static double homoLumoGap(double homoEnergy, double lumoEnergy) {
        return lumoEnergy - homoEnergy;
    }

    /**
     * Simple HÃƒÂ¼ckel MO energy for linear polyene.
     * E_k = ÃŽÂ± + 2ÃŽÂ² cos(kÃâ‚¬/(n+1))
     * 
     * @param n     Number of carbon atoms
     * @param k     Orbital index (1 to n)
     * @param alpha Coulomb integral (reference energy)
     * @param beta  Resonance integral (typically negative)
     */
    public static double huckelEnergy(int n, int k, double alpha, double beta) {
        return alpha + 2 * beta * Math.cos(k * Math.PI / (n + 1));
    }

    /**
     * Calculates number of bonding/antibonding MOs for diatomic.
     */
    public static int[] diatomicMOCount(int totalValenceElectrons) {
        // For homonuclear diatomics, fill in order
        // ÃÆ’2s(2) ÃÆ’*2s(2) ÃÆ’2p(2) Ãâ‚¬2p(4) Ãâ‚¬*2p(4) ÃÆ’*2p(2)
        int bonding = 0;
        int antibonding = 0;
        int remaining = totalValenceElectrons;

        // ÃÆ’2s bonding
        int fill = Math.min(2, remaining);
        bonding += fill;
        remaining -= fill;

        // ÃÆ’*2s antibonding
        fill = Math.min(2, remaining);
        antibonding += fill;
        remaining -= fill;

        // For O2, F2 order differs, but simplified here
        // ÃÆ’2p bonding
        fill = Math.min(2, remaining);
        bonding += fill;
        remaining -= fill;

        // Ãâ‚¬2p bonding (2 orbitals, 4 electrons max)
        fill = Math.min(4, remaining);
        bonding += fill;
        remaining -= fill;

        // Ãâ‚¬*2p antibonding
        fill = Math.min(4, remaining);
        antibonding += fill;
        remaining -= fill;

        // ÃÆ’*2p antibonding
        fill = Math.min(2, remaining);
        antibonding += fill;

        return new int[] { bonding, antibonding };
    }

    /**
     * Predicts magnetism of O2.
     * O2 has 12 valence electrons, 2 unpaired in Ãâ‚¬*2p.
     */
    public static String o2Magnetism() {
        return "Paramagnetic (2 unpaired electrons in Ãâ‚¬*2p)";
    }

    /**
     * Delocalization energy for benzene (HÃƒÂ¼ckel).
     * E_deloc = 6ÃŽÂ± + 8ÃŽÂ² for benzene vs 6ÃŽÂ± + 6ÃŽÂ² for 3 isolated double bonds
     * Stabilization = 2|ÃŽÂ²| Ã¢â€°Ë† 150 kJ/mol
     */
    public static double benzeneDelocalizationEnergy(double beta) {
        return 2 * Math.abs(beta); // In units of |ÃŽÂ²|
    }
}


