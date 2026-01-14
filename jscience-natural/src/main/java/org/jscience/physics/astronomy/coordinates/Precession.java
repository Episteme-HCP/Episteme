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

package org.jscience.physics.astronomy.coordinates;

/**
 * Handles Axial Precession (Precession of the Equinoxes).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
        // m Ã¢â€°Ë† 3.075 s/yr Ã¢â€°Ë† 46.125 arcsec/yr Ã¢â€°Ë† 0.0128125 deg/yr
        // n Ã¢â€°Ë† 1.336 s/yr Ã¢â€°Ë† 20.043 arcsec/yr Ã¢â€°Ë† 0.0055675 deg/yr

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


