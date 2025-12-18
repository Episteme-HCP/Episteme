package org.jscience.physics.astronomy.spectroscopy;

/**
 * Doppler shift and redshift calculations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class DopplerShift {

    /** Speed of light in km/s */
    public static final double C_KM_S = 299792.458;

    private DopplerShift() {
    } // Utility class

    /**
     * Calculates radial velocity from observed and rest wavelengths.
     * v = c * (λ_obs - λ_rest) / λ_rest
     * 
     * @param observedWavelength Observed wavelength
     * @param restWavelength     Rest (lab) wavelength
     * @return Radial velocity in same units as c (default: km/s)
     */
    public static double radialVelocity(double observedWavelength, double restWavelength) {
        return C_KM_S * (observedWavelength - restWavelength) / restWavelength;
    }

    /**
     * Calculates redshift z from wavelengths.
     * z = (λ_obs - λ_rest) / λ_rest
     * 
     * @param observedWavelength Observed wavelength
     * @param restWavelength     Rest wavelength
     * @return Redshift z
     */
    public static double redshift(double observedWavelength, double restWavelength) {
        return (observedWavelength - restWavelength) / restWavelength;
    }

    /**
     * Converts redshift to radial velocity.
     * For z << 1: v ≈ c * z
     * For relativistic: v = c * ((1+z)² - 1) / ((1+z)² + 1)
     * 
     * @param z                      Redshift
     * @param relativisticCorrection Use relativistic formula if true
     * @return Velocity in km/s
     */
    public static double redshiftToVelocity(double z, boolean relativisticCorrection) {
        if (relativisticCorrection) {
            double zp1 = 1 + z;
            return C_KM_S * (zp1 * zp1 - 1) / (zp1 * zp1 + 1);
        } else {
            return C_KM_S * z;
        }
    }

    /**
     * Converts velocity to redshift.
     * For v << c: z ≈ v/c
     * For relativistic: z = sqrt((1+v/c)/(1-v/c)) - 1
     * 
     * @param velocityKmS            Velocity in km/s
     * @param relativisticCorrection Use relativistic formula if true
     * @return Redshift z
     */
    public static double velocityToRedshift(double velocityKmS, boolean relativisticCorrection) {
        double beta = velocityKmS / C_KM_S;
        if (relativisticCorrection) {
            return Math.sqrt((1 + beta) / (1 - beta)) - 1;
        } else {
            return beta;
        }
    }

    /**
     * Calculates observed wavelength from rest wavelength and velocity.
     * λ_obs = λ_rest * (1 + v/c)
     */
    public static double observedWavelength(double restWavelength, double velocityKmS) {
        return restWavelength * (1 + velocityKmS / C_KM_S);
    }

    /**
     * Cosmological distance estimate using Hubble law.
     * d = v / H0 = c * z / H0
     * 
     * @param z  Redshift
     * @param H0 Hubble constant (km/s/Mpc), typically ~70
     * @return Distance in Mpc
     */
    public static double hubbleDistance(double z, double H0) {
        return C_KM_S * z / H0;
    }

    /**
     * Hubble constant in km/s/Mpc (Planck 2018 value).
     */
    public static final double H0_PLANCK = 67.4;

    // --- Common spectral lines (wavelength in Angstroms) ---

    /** Hydrogen alpha (Balmer series) */
    public static final double H_ALPHA = 6562.8;

    /** Hydrogen beta */
    public static final double H_BETA = 4861.3;

    /** Calcium K line */
    public static final double CA_K = 3933.7;

    /** Calcium H line */
    public static final double CA_H = 3968.5;

    /** Sodium D1 line */
    public static final double NA_D1 = 5895.9;

    /** Sodium D2 line */
    public static final double NA_D2 = 5889.9;
}
