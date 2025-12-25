package org.jscience.physics.astronomy.coordinates;

/**
 * Handles Axial Precession (Precession of the Equinoxes).
 */
public class Precession {

    /**
     * Applies approximate precession to J2000 equatorial coordinates.
     * Does not include Nutation.
     * 
     * @param raJ2000        Right Ascension (J2000) in Degrees
     * @param decJ2000       Declination (J2000) in Degrees
     * @param yearsFromJ2000 Time difference in years from J2000.0
     * @return double[] { RA_Current (deg), Dec_Current (deg) }
     */
    public static double[] apply(double raJ2000, double decJ2000, double yearsFromJ2000) {
        // Approximate formulas (valid for a few centuries/millennia)
        // m ≈ 3.075 s/yr ≈ 46.125 arcsec/yr ≈ 0.0128125 deg/yr
        // n ≈ 1.336 s/yr ≈ 20.043 arcsec/yr ≈ 0.0055675 deg/yr

        double t = yearsFromJ2000;

        // Constants in Degrees per Year
        double m = 0.0128125;
        double n = 0.0055675;

        double raRad = Math.toRadians(raJ2000);
        double decRad = Math.toRadians(decJ2000);

        // Precession rates
        double dRa_dt = m + n * Math.sin(raRad) * Math.tan(decRad);
        double dDec_dt = n * Math.cos(raRad);

        // Apply
        double raNew = raJ2000 + dRa_dt * t;
        double decNew = decJ2000 + dDec_dt * t;

        // Normalize
        raNew = normalizeDegrees(raNew);
        // Clamp Dec just in case
        decNew = Math.max(-90, Math.min(90, decNew));

        return new double[] { raNew, decNew };
    }

    private static double normalizeDegrees(double deg) {
        deg = deg % 360.0;
        return deg < 0 ? deg + 360.0 : deg;
    }
}
