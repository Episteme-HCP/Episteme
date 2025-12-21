/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * Utility for coordinate transformations.
 * Centralizes conversion logic between systems.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CoordinateConverter {

    /**
     * Equatorial (J2000) to Galactic.
     */
    public static GalacticCoordinate equatorialToGalactic(EquatorialCoordinate eq) {
        return GalacticCoordinate.fromEquatorial(eq);
    }

    /**
     * Galactic to Equatorial (J2000).
     */
    public static EquatorialCoordinate galacticToEquatorial(GalacticCoordinate gal) {
        return gal.toEquatorial();
    }

    /**
     * Equatorial to Horizontal (Alt/Az).
     * Requires observer location and time.
     */
    public static HorizontalCoordinate equatorialToHorizontal(EquatorialCoordinate eq,
            double latitude,
            double lmst) {
        return HorizontalCoordinate.fromEquatorial(eq, latitude, lmst);
    }

    /**
     * Horizontal to Equatorial.
     * Requires observer location and time.
     */
    public static EquatorialCoordinate horizontalToEquatorial(HorizontalCoordinate hor,
            double latitude,
            double lmst) {
        return hor.toEquatorial(latitude, lmst);
    }

    /**
     * Ecliptic to Equatorial (J2000).
     * Standard obliquity ε = 23.4392911°
     */
    public static EquatorialCoordinate eclipticToEquatorial(double eclipticLon, double eclipticLat) {
        double eps = Math.toRadians(23.4392911);
        double l = Math.toRadians(eclipticLon);
        double b = Math.toRadians(eclipticLat);

        double sinDec = Math.sin(b) * Math.cos(eps) + Math.cos(b) * Math.sin(eps) * Math.sin(l);
        double dec = Math.asin(sinDec);

        double y = Math.sin(l) * Math.cos(eps) - Math.tan(b) * Math.sin(eps);
        double x = Math.cos(l);
        double ra = Math.atan2(y, x);

        return new EquatorialCoordinate(normalizeDegrees(Math.toDegrees(ra)), Math.toDegrees(dec));
    }

    /**
     * Equatorial to Ecliptic.
     * Returns [Longitude, Latitude] in degrees.
     */
    public static double[] equatorialToEcliptic(EquatorialCoordinate eq) {
        double eps = Math.toRadians(23.4392911);
        double ra = Math.toRadians(eq.getRaDegrees());
        double dec = Math.toRadians(eq.getDecDegrees());

        double sinLat = Math.sin(dec) * Math.cos(eps) - Math.cos(dec) * Math.sin(eps) * Math.sin(ra);
        double lat = Math.asin(sinLat);

        double y = Math.sin(ra) * Math.cos(eps) + Math.tan(dec) * Math.sin(eps);
        double x = Math.cos(ra);
        double lon = Math.atan2(y, x);

        return new double[] { normalizeDegrees(Math.toDegrees(lon)), Math.toDegrees(lat) };
    }

    private static double normalizeDegrees(double deg) {
        deg = deg % 360.0;
        return deg < 0 ? deg + 360.0 : deg;
    }
}
