package org.jscience.chemistry.computational;

/**
 * Molecular orbital calculations using Hückel theory.
 * 
 * Hückel MO theory is a simple LCAO method for conjugated π systems.
 * Secular equation: |H - ES| = 0
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class MolecularOrbitals {

    /** Coulomb integral α (energy of electron in 2p orbital) */
    private final double alpha;

    /** Resonance integral β (interaction between adjacent atoms) */
    private final double beta;

    public MolecularOrbitals(double alpha, double beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    /**
     * Default constructor with typical values (β < 0, α arbitrary).
     */
    public MolecularOrbitals() {
        this(-6.0, -2.5); // Typical values in eV
    }

    /**
     * Calculates π-electron energies for a linear conjugated chain.
     * E_k = α + 2β·cos(kπ/(n+1)) for k = 1, 2, ..., n
     * 
     * @param numAtoms Number of sp² carbon atoms
     * @return Array of orbital energies (lowest first)
     */
    public double[] linearChainEnergies(int numAtoms) {
        double[] energies = new double[numAtoms];
        for (int k = 1; k <= numAtoms; k++) {
            energies[k - 1] = alpha + 2 * beta * Math.cos(k * Math.PI / (numAtoms + 1));
        }
        return energies;
    }

    /**
     * Calculates π-electron energies for a cyclic conjugated system.
     * E_k = α + 2β·cos(2πk/n) for k = 0, 1, ..., n-1
     * 
     * @param numAtoms Number of atoms in the ring
     * @return Array of orbital energies
     */
    public double[] cyclicEnergies(int numAtoms) {
        double[] energies = new double[numAtoms];
        for (int k = 0; k < numAtoms; k++) {
            energies[k] = alpha + 2 * beta * Math.cos(2 * Math.PI * k / numAtoms);
        }
        // Sort by energy
        java.util.Arrays.sort(energies);
        return energies;
    }

    /**
     * Calculates total π-electron energy for a system.
     * 
     * @param orbitalEnergies Orbital energies
     * @param numElectrons    Number of π electrons
     * @return Total π-electron energy
     */
    public double totalPiEnergy(double[] orbitalEnergies, int numElectrons) {
        double total = 0;
        int electrons = numElectrons;
        for (double e : orbitalEnergies) {
            if (electrons >= 2) {
                total += 2 * e;
                electrons -= 2;
            } else if (electrons == 1) {
                total += e;
                electrons--;
            } else {
                break;
            }
        }
        return total;
    }

    /**
     * Calculates delocalization energy (resonance stabilization).
     * DE = E_π(actual) - E_π(localized)
     * 
     * @param actualEnergy   Total π energy from Hückel
     * @param numDoubleBonds Number of isolated double bonds in reference
     * @return Delocalization energy
     */
    public double delocalizationEnergy(double actualEnergy, int numDoubleBonds) {
        // Reference: isolated ethene has E = 2(α + β)
        double localizedEnergy = numDoubleBonds * 2 * (alpha + beta);
        return actualEnergy - localizedEnergy;
    }

    /**
     * HOMO-LUMO gap (frontier orbital energy difference).
     * 
     * @param orbitalEnergies Sorted orbital energies
     * @param numElectrons    Number of electrons
     * @return HOMO-LUMO gap
     */
    public double homoLumoGap(double[] orbitalEnergies, int numElectrons) {
        int homoIndex = (numElectrons + 1) / 2 - 1; // 0-indexed
        int lumoIndex = homoIndex + 1;
        if (lumoIndex >= orbitalEnergies.length) {
            return Double.NaN;
        }
        return orbitalEnergies[lumoIndex] - orbitalEnergies[homoIndex];
    }

    /**
     * Checks if a cyclic system satisfies Hückel's rule (aromatic).
     * 4n+2 π electrons = aromatic
     */
    public static boolean isAromatic(int numPiElectrons) {
        return (numPiElectrons - 2) % 4 == 0 && numPiElectrons >= 2;
    }

    /**
     * Checks if antiaromatic (4n π electrons).
     */
    public static boolean isAntiaromatic(int numPiElectrons) {
        return numPiElectrons % 4 == 0 && numPiElectrons > 0;
    }

    // --- Common molecules ---

    /** Benzene: 6 carbon ring, 6 π electrons */
    public double[] benzeneEnergies() {
        return cyclicEnergies(6);
    }

    /** Butadiene: 4 carbon chain, 4 π electrons */
    public double[] butadieneEnergies() {
        return linearChainEnergies(4);
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }
}
