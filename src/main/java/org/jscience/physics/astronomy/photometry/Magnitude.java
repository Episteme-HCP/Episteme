package org.jscience.physics.astronomy.photometry;

/**
 * Stellar magnitude calculations and conversions.
 * 
 * Apparent magnitude (m): Brightness as seen from Earth.
 * Absolute magnitude (M): Brightness at 10 parsecs distance.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Magnitude {

    private Magnitude() {
    } // Utility class

    /**
     * Converts apparent magnitude and distance to absolute magnitude.
     * M = m - 5 * log10(d) + 5
     * 
     * @param apparentMag Apparent magnitude
     * @param distancePc  Distance in parsecs
     * @return Absolute magnitude
     */
    public static double toAbsolute(double apparentMag, double distancePc) {
        return apparentMag - 5 * Math.log10(distancePc) + 5;
    }

    /**
     * Converts absolute magnitude and distance to apparent magnitude.
     * m = M + 5 * log10(d) - 5
     * 
     * @param absoluteMag Absolute magnitude
     * @param distancePc  Distance in parsecs
     * @return Apparent magnitude
     */
    public static double toApparent(double absoluteMag, double distancePc) {
        return absoluteMag + 5 * Math.log10(distancePc) - 5;
    }

    /**
     * Calculates distance modulus.
     * μ = m - M = 5 * log10(d) - 5
     * 
     * @param apparentMag Apparent magnitude
     * @param absoluteMag Absolute magnitude
     * @return Distance modulus
     */
    public static double distanceModulus(double apparentMag, double absoluteMag) {
        return apparentMag - absoluteMag;
    }

    /**
     * Calculates distance from distance modulus.
     * d = 10^((μ + 5) / 5)
     * 
     * @param distanceModulus μ = m - M
     * @return Distance in parsecs
     */
    public static double distanceFromModulus(double distanceModulus) {
        return Math.pow(10, (distanceModulus + 5) / 5);
    }

    /**
     * Combines magnitudes of multiple sources.
     * m_total = -2.5 * log10(Σ 10^(-m_i/2.5))
     * 
     * @param magnitudes Array of magnitudes
     * @return Combined magnitude
     */
    public static double combine(double... magnitudes) {
        double totalFlux = 0;
        for (double m : magnitudes) {
            totalFlux += Math.pow(10, -m / 2.5);
        }
        return -2.5 * Math.log10(totalFlux);
    }

    /**
     * Calculates flux ratio from magnitude difference.
     * F1/F2 = 10^((m2 - m1) / 2.5)
     */
    public static double fluxRatio(double mag1, double mag2) {
        return Math.pow(10, (mag2 - mag1) / 2.5);
    }

    /**
     * Converts magnitude difference to flux ratio.
     */
    public static double deltaMagToFluxRatio(double deltaMag) {
        return Math.pow(10, -deltaMag / 2.5);
    }

    /**
     * Converts B-V color index to effective temperature (approximate).
     * Using Ballesteros formula.
     * 
     * @param bMinusV B-V color index
     * @return Effective temperature in Kelvin
     */
    public static double colorToTemperature(double bMinusV) {
        return 4600 * (1.0 / (0.92 * bMinusV + 1.7) + 1.0 / (0.92 * bMinusV + 0.62));
    }

    /**
     * Converts absolute magnitude to luminosity (solar units).
     * L/L☉ = 10^((M☉ - M) / 2.5)
     * 
     * @param absoluteMag Absolute visual magnitude
     * @return Luminosity in solar luminosities
     */
    public static double toLuminosity(double absoluteMag) {
        double solarAbsMag = 4.83; // Sun's absolute visual magnitude
        return Math.pow(10, (solarAbsMag - absoluteMag) / 2.5);
    }

    /**
     * Converts luminosity to absolute magnitude.
     * M = M☉ - 2.5 * log10(L/L☉)
     */
    public static double fromLuminosity(double luminositySolar) {
        double solarAbsMag = 4.83;
        return solarAbsMag - 2.5 * Math.log10(luminositySolar);
    }

    // --- Reference magnitudes ---

    /** Sun's apparent visual magnitude */
    public static final double SUN_APPARENT = -26.74;

    /** Sun's absolute visual magnitude */
    public static final double SUN_ABSOLUTE = 4.83;

    /** Full Moon apparent magnitude */
    public static final double FULL_MOON = -12.74;

    /** Sirius apparent magnitude */
    public static final double SIRIUS = -1.46;

    /** Vega apparent magnitude (defines magnitude zero point) */
    public static final double VEGA = 0.03;
}
