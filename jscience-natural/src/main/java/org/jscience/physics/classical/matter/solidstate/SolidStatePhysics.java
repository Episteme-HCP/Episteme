package org.jscience.physics.classical.matter.solidstate;

/**
 * Solid state physics calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class SolidStatePhysics {

    /** Planck constant (J·s) */
    public static final double H = 6.62607015e-34;

    /** Reduced Planck constant */
    public static final double HBAR = 1.054571817e-34;

    /** Boltzmann constant (J/K) */
    public static final double K_B = 1.380649e-23;

    /** Electron mass (kg) */
    public static final double M_E = 9.1093837015e-31;

    /** Elementary charge (C) */
    public static final double E = 1.602176634e-19;

    /**
     * Fermi energy for free electron gas.
     * E_F = (ℏ²/2m) * (3π²n)^(2/3)
     * 
     * @param electronDensity n in m⁻³
     * @return Fermi energy (J)
     */
    public static double fermiEnergy(double electronDensity) {
        double kF = Math.pow(3 * Math.PI * Math.PI * electronDensity, 1.0 / 3);
        return HBAR * HBAR * kF * kF / (2 * M_E);
    }

    /**
     * Fermi velocity.
     * v_F = ℏk_F / m
     */
    public static double fermiVelocity(double electronDensity) {
        double kF = Math.pow(3 * Math.PI * Math.PI * electronDensity, 1.0 / 3);
        return HBAR * kF / M_E;
    }

    /**
     * Density of states at Fermi level (3D free electrons).
     * g(E_F) = 3n / (2E_F)
     */
    public static double densityOfStates(double electronDensity, double fermiEnergy) {
        return 1.5 * electronDensity / fermiEnergy;
    }

    /**
     * Fermi-Dirac distribution.
     * f(E) = 1 / (exp((E-μ)/k_B T) + 1)
     */
    public static double fermiDirac(double E, double mu, double temperature) {
        double x = (E - mu) / (K_B * temperature);
        if (x > 50)
            return 0;
        if (x < -50)
            return 1;
        return 1.0 / (Math.exp(x) + 1);
    }

    /**
     * Debye frequency.
     * ω_D = v_s * (6π²n)^(1/3)
     * 
     * @param soundVelocity Speed of sound (m/s)
     * @param atomDensity   Number density of atoms (m⁻³)
     */
    public static double debyeFrequency(double soundVelocity, double atomDensity) {
        return soundVelocity * Math.pow(6 * Math.PI * Math.PI * atomDensity, 1.0 / 3);
    }

    /**
     * Debye temperature.
     * θ_D = ℏω_D / k_B
     */
    public static double debyeTemperature(double debyeFrequency) {
        return HBAR * debyeFrequency / K_B;
    }

    /**
     * Phonon energy.
     * E = ℏω
     */
    public static double phononEnergy(double frequency) {
        return HBAR * frequency;
    }

    /**
     * Bose-Einstein distribution for phonons.
     * n(ω) = 1 / (exp(ℏω/k_B T) - 1)
     */
    public static double boseEinstein(double frequency, double temperature) {
        double x = HBAR * frequency / (K_B * temperature);
        if (x > 50)
            return 0;
        return 1.0 / (Math.exp(x) - 1);
    }

    /**
     * Band gap from semiconductor conductivity temperature dependence.
     * σ ∝ exp(-E_g / 2k_B T)
     */
    public static double conductivityFromBandGap(double sigma0, double bandGap, double temperature) {
        return sigma0 * Math.exp(-bandGap / (2 * K_B * temperature));
    }

    /**
     * Intrinsic carrier concentration.
     * n_i = √(N_c * N_v) * exp(-E_g / 2k_B T)
     */
    public static double intrinsicCarrierConcentration(double Nc, double Nv,
            double bandGap, double temperature) {
        return Math.sqrt(Nc * Nv) * Math.exp(-bandGap / (2 * K_B * temperature));
    }

    /**
     * Effective mass from band curvature.
     * m* = ℏ² / (d²E/dk²)
     */
    public static double effectiveMass(double bandCurvature) {
        return HBAR * HBAR / bandCurvature;
    }

    /**
     * Hall coefficient.
     * R_H = 1 / (n * e) for electrons
     */
    public static double hallCoefficient(double carrierDensity, boolean isElectron) {
        return (isElectron ? -1 : 1) / (carrierDensity * E);
    }

    /**
     * Mobility from conductivity and carrier density.
     * μ = σ / (n * e)
     */
    public static double mobility(double conductivity, double carrierDensity) {
        return conductivity / (carrierDensity * E);
    }

    /**
     * Drude conductivity.
     * σ = n e² τ / m*
     */
    public static double drudeConductivity(double carrierDensity, double relaxationTime,
            double effectiveMass) {
        return carrierDensity * E * E * relaxationTime / effectiveMass;
    }

    // --- Common semiconductor band gaps (eV) ---
    public static final double BANDGAP_SI = 1.12;
    public static final double BANDGAP_GE = 0.67;
    public static final double BANDGAP_GAAS = 1.42;
    public static final double BANDGAP_GAN = 3.4;
    public static final double BANDGAP_INP = 1.35;
}
