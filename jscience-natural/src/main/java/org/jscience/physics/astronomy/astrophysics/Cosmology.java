package org.jscience.physics.astronomy.astrophysics;

/**
 * Cosmology calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Cosmology {

    /** Speed of light (km/s) */
    public static final double C_KM_S = 299792.458;

    /** Hubble constant (km/s/Mpc) - Planck 2018 */
    public static final double H0_PLANCK = 67.4;

    /** Critical density (kg/m³) for H0 = 67.4 */
    public static final double RHO_CRIT = 8.5e-27;

    /** Age of universe (seconds) */
    public static final double AGE_UNIVERSE = 4.35e17;

    /** Present density parameters (Planck 2018) */
    public static final double OMEGA_M = 0.315; // Matter
    public static final double OMEGA_LAMBDA = 0.685; // Dark energy
    public static final double OMEGA_R = 9.2e-5; // Radiation

    /**
     * Hubble's law: recession velocity.
     * v = H0 * d
     * 
     * @param distanceMpc Distance in megaparsecs
     * @param H0          Hubble constant (km/s/Mpc)
     * @return Recession velocity (km/s)
     */
    public static double recessionVelocity(double distanceMpc, double H0) {
        return H0 * distanceMpc;
    }

    /**
     * Hubble distance from redshift (low-z approximation).
     * d = cz / H0
     */
    public static double hubbleDistance(double z, double H0) {
        return C_KM_S * z / H0;
    }

    /**
     * Hubble time (age of universe if expansion were constant).
     * t_H = 1/H0
     * 
     * @param H0 Hubble constant (km/s/Mpc)
     * @return Hubble time in Gyr
     */
    public static double hubbleTime(double H0) {
        // Convert H0 to 1/seconds, then to Gyr
        return 977.8 / H0; // Gyr
    }

    /**
     * Cosmological redshift to scale factor.
     * a = 1/(1+z)
     */
    public static double scaleFactor(double z) {
        return 1.0 / (1 + z);
    }

    /**
     * Scale factor to redshift.
     * z = 1/a - 1
     */
    public static double redshiftFromScaleFactor(double a) {
        return 1.0 / a - 1;
    }

    /**
     * Lookback time (simplified, matter-dominated).
     * t_lookback ≈ t_H * (1 - (1+z)^(-3/2))
     */
    public static double lookbackTime(double z, double H0) {
        double tH = hubbleTime(H0);
        return tH * (1 - Math.pow(1 + z, -1.5));
    }

    /**
     * Comoving distance (simplified integral for ΛCDM).
     */
    public static double comovingDistance(double z, double H0, double omegaM, double omegaLambda) {
        // Numerical integration of dc = c * dz / H(z)
        int steps = 1000;
        double dz = z / steps;
        double dc = 0;

        for (int i = 0; i < steps; i++) {
            double zi = (i + 0.5) * dz;
            double Ez = Math.sqrt(omegaM * Math.pow(1 + zi, 3) + omegaLambda);
            dc += dz / Ez;
        }

        return C_KM_S * dc / H0; // Mpc
    }

    /**
     * Luminosity distance.
     * d_L = (1+z) * d_c
     */
    public static double luminosityDistance(double z, double comovingDistance) {
        return (1 + z) * comovingDistance;
    }

    /**
     * Angular diameter distance.
     * d_A = d_c / (1+z)
     */
    public static double angularDiameterDistance(double z, double comovingDistance) {
        return comovingDistance / (1 + z);
    }

    /**
     * Cosmic microwave background temperature at redshift z.
     * T(z) = T0 * (1+z)
     */
    public static double cmbTemperature(double z) {
        double T0 = 2.725; // Present CMB temperature (K)
        return T0 * (1 + z);
    }

    /**
     * Dark energy equation of state parameter.
     * For cosmological constant: w = -1
     */
    public static double darkEnergyDensity(double z, double w) {
        return Math.pow(1 + z, 3 * (1 + w));
    }

    /**
     * Friedmann equation: H²/H0² = E²(z)
     * E(z) = √(Ωm(1+z)³ + ΩΛ + Ωr(1+z)⁴)
     */
    public static double friedmannE(double z, double omegaM, double omegaLambda, double omegaR) {
        return Math.sqrt(omegaM * Math.pow(1 + z, 3)
                + omegaLambda
                + omegaR * Math.pow(1 + z, 4));
    }

    /**
     * Deceleration parameter at present.
     * q0 = Ωm/2 - ΩΛ
     * q0 < 0 means accelerating expansion
     */
    public static double decelerationParameter(double omegaM, double omegaLambda) {
        return omegaM / 2 - omegaLambda;
    }
}
