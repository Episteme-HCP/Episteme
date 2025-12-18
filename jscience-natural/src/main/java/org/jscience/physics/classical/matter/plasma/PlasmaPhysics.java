package org.jscience.physics.classical.matter.plasma;

/**
 * Plasma physics calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class PlasmaPhysics {

    /** Vacuum permittivity (F/m) */
    public static final double EPSILON_0 = 8.854187817e-12;

    /** Elementary charge (C) */
    public static final double E = 1.602176634e-19;

    /** Electron mass (kg) */
    public static final double M_E = 9.1093837015e-31;

    /** Proton mass (kg) */
    public static final double M_P = 1.67262192369e-27;

    /** Boltzmann constant (J/K) */
    public static final double K_B = 1.380649e-23;

    /**
     * Debye length: characteristic screening distance.
     * λ_D = √(ε₀ k_B T / n e²)
     * 
     * @param temperature Electron temperature (K)
     * @param density     Electron density (m⁻³)
     * @return Debye length (m)
     */
    public static double debyeLength(double temperature, double density) {
        return Math.sqrt(EPSILON_0 * K_B * temperature / (density * E * E));
    }

    /**
     * Plasma frequency: natural oscillation frequency.
     * ω_p = √(n e² / ε₀ m_e)
     * 
     * @param density Electron density (m⁻³)
     * @return Angular plasma frequency (rad/s)
     */
    public static double plasmaFrequency(double density) {
        return Math.sqrt(density * E * E / (EPSILON_0 * M_E));
    }

    /**
     * Plasma frequency in Hz.
     */
    public static double plasmaFrequencyHz(double density) {
        return plasmaFrequency(density) / (2 * Math.PI);
    }

    /**
     * Electron cyclotron frequency (gyrofrequency).
     * ω_ce = eB / m_e
     */
    public static double electronCyclotronFrequency(double magneticField) {
        return E * magneticField / M_E;
    }

    /**
     * Ion cyclotron frequency.
     * ω_ci = ZeB / m_i
     */
    public static double ionCyclotronFrequency(double magneticField, double ionMass, int chargeNumber) {
        return chargeNumber * E * magneticField / ionMass;
    }

    /**
     * Larmor radius (gyroradius).
     * r_L = m v_perp / (eB)
     */
    public static double larmorRadius(double velocity, double magneticField, double mass) {
        return mass * velocity / (E * magneticField);
    }

    /**
     * Thermal velocity.
     * v_th = √(k_B T / m)
     */
    public static double thermalVelocity(double temperature, double mass) {
        return Math.sqrt(K_B * temperature / mass);
    }

    /**
     * Plasma parameter: number of particles in Debye sphere.
     * Λ = n λ_D³
     * Plasma is ideal when Λ >> 1
     */
    public static double plasmaParameter(double density, double debyeLength) {
        return density * Math.pow(debyeLength, 3);
    }

    /**
     * Coulomb logarithm (approximate).
     * ln(Λ) ≈ ln(12π n λ_D³)
     */
    public static double coulombLogarithm(double density, double temperature) {
        double lambdaD = debyeLength(temperature, density);
        return Math.log(12 * Math.PI * plasmaParameter(density, lambdaD));
    }

    /**
     * Alfvén velocity.
     * v_A = B / √(μ₀ ρ)
     */
    public static double alfvenVelocity(double magneticField, double massDensity) {
        double mu0 = 4 * Math.PI * 1e-7;
        return magneticField / Math.sqrt(mu0 * massDensity);
    }

    /**
     * Sound speed in plasma.
     * c_s = √(γ k_B T / m_i)
     */
    public static double soundSpeed(double temperature, double ionMass, double gamma) {
        return Math.sqrt(gamma * K_B * temperature / ionMass);
    }

    /**
     * Beta: ratio of plasma pressure to magnetic pressure.
     * β = nkT / (B²/2μ₀)
     */
    public static double beta(double density, double temperature, double magneticField) {
        double mu0 = 4 * Math.PI * 1e-7;
        double plasmaPressure = density * K_B * temperature;
        double magneticPressure = magneticField * magneticField / (2 * mu0);
        return plasmaPressure / magneticPressure;
    }

    /**
     * Spitzer resistivity.
     * η = (π Z e² m_e^(1/2) ln(Λ)) / ((4πε₀)² (k_B T_e)^(3/2))
     */
    public static double spitzerResistivity(double temperature, double coulombLog, int Z) {
        double num = Math.PI * Z * E * E * Math.sqrt(M_E) * coulombLog;
        double den = 16 * Math.PI * Math.PI * EPSILON_0 * EPSILON_0
                * Math.pow(K_B * temperature, 1.5);
        return num / den;
    }

    /**
     * Collision frequency (electron-ion).
     */
    public static double collisionFrequency(double density, double temperature, double coulombLog) {
        return density * E * E * E * E * coulombLog
                / (6 * Math.pow(Math.PI, 1.5) * EPSILON_0 * EPSILON_0 * M_E * M_E
                        * Math.pow(K_B * temperature / M_E, 1.5));
    }
}
