package org.jscience.physics.quantum;

/**
 * Quantum Harmonic Oscillator calculations.
 */
public class QuantumHarmonicOscillator {

    public static final double HBAR = 1.054571817e-34; // Reduced Planck constant (J·s)

    private QuantumHarmonicOscillator() {
    }

    /**
     * Energy eigenvalues.
     * E_n = ℏω(n + 1/2)
     * 
     * @param n     Quantum number (0, 1, 2, ...)
     * @param omega Angular frequency (rad/s)
     * @return Energy in Joules
     */
    public static double energyLevel(int n, double omega) {
        return HBAR * omega * (n + 0.5);
    }

    /**
     * Angular frequency from spring constant and mass.
     * ω = sqrt(k/m)
     */
    public static double angularFrequency(double springConstant, double mass) {
        return Math.sqrt(springConstant / mass);
    }

    /**
     * Ground state energy.
     * E_0 = ℏω/2
     */
    public static double groundStateEnergy(double omega) {
        return HBAR * omega / 2;
    }

    /**
     * Transition energy between levels.
     * ΔE = ℏω (for adjacent levels)
     */
    public static double transitionEnergy(int n1, int n2, double omega) {
        return Math.abs(energyLevel(n2, omega) - energyLevel(n1, omega));
    }

    /**
     * Classical amplitude for given energy.
     * A = sqrt(2E/(mω²))
     */
    public static double classicalAmplitude(double energy, double mass, double omega) {
        return Math.sqrt(2 * energy / (mass * omega * omega));
    }

    /**
     * Probability density at x=0 for ground state.
     * |ψ_0(0)|² = sqrt(mω/(πℏ))
     */
    public static double groundStateProbabilityAt0(double mass, double omega) {
        return Math.sqrt(mass * omega / (Math.PI * HBAR));
    }

    /**
     * Zero-point motion (uncertainty in position).
     * Δx_0 = sqrt(ℏ/(2mω))
     */
    public static double zeroPointMotion(double mass, double omega) {
        return Math.sqrt(HBAR / (2 * mass * omega));
    }
}
