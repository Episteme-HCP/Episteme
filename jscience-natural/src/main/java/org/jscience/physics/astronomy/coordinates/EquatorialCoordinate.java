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
 * Equatorial celestial coordinates (Right Ascension and Declination).
 * 
 * RA is measured in hours (0-24h) or degrees (0-360°) along celestial equator.
 * Dec is measured in degrees (-90° to +90°) from celestial equator.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EquatorialCoordinate {

    private final double ra; // Right Ascension in degrees [0, 360)
    private final double dec; // Declination in degrees [-90, +90]

    /**
     * Creates equatorial coordinates from RA/Dec in degrees.
     */
    public EquatorialCoordinate(double raDegrees, double decDegrees) {
        this.ra = normalizeAngle(raDegrees);
        this.dec = Math.max(-90, Math.min(90, decDegrees));
    }

    /**
     * Creates equatorial coordinates from RA in hours and Dec in degrees.
     */
    public static EquatorialCoordinate fromHours(double raHours, double decDegrees) {
        return new EquatorialCoordinate(raHours * 15.0, decDegrees);
    }

    /**
     * Creates from HMS (hours, minutes, seconds) and DMS (degrees, arcmin, arcsec).
     */
    public static EquatorialCoordinate fromHmsDms(
            int raH, int raM, double raS,
            int decD, int decArcMin, double decArcSec) {

        double raHours = raH + raM / 60.0 + raS / 3600.0;
        double decSign = (decD < 0 || (decD == 0 && decArcMin < 0)) ? -1 : 1;
        double decDeg = Math.abs(decD) + decArcMin / 60.0 + decArcSec / 3600.0;
        decDeg *= decSign;

        return fromHours(raHours, decDeg);
    }

    /**
     * Right Ascension in degrees.
     */
    public double getRaDegrees() {
        return ra;
    }

    /**
     * Right Ascension in hours.
     */
    public double getRaHours() {
        return ra / 15.0;
    }

    /**
     * Declination in degrees.
     */
    public double getDecDegrees() {
        return dec;
    }

    /**
     * Returns RA as HMS string (e.g., "12h 34m 56.7s").
     */
    public String getRaHms() {
        double hours = getRaHours();
        int h = (int) hours;
        double minFrac = (hours - h) * 60;
        int m = (int) minFrac;
        double s = (minFrac - m) * 60;
        return String.format("%02dh %02dm %05.2fs", h, m, s);
    }

    /**
     * Returns Dec as DMS string (e.g., "+45° 12' 34.5"").
     */
    public String getDecDms() {
        double absD = Math.abs(dec);
        int d = (int) absD;
        double minFrac = (absD - d) * 60;
        int m = (int) minFrac;
        double s = (minFrac - m) * 60;
        char sign = dec >= 0 ? '+' : '-';
        return String.format("%c%02d° %02d' %05.2f\"", sign, d, m, s);
    }

    /**
     * Angular separation from another coordinate in degrees.
     * Uses spherical law of cosines.
     */
    public double angularSeparation(EquatorialCoordinate other) {
        double ra1 = Math.toRadians(this.ra);
        double dec1 = Math.toRadians(this.dec);
        double ra2 = Math.toRadians(other.ra);
        double dec2 = Math.toRadians(other.dec);

        double cosSep = Math.sin(dec1) * Math.sin(dec2)
                + Math.cos(dec1) * Math.cos(dec2) * Math.cos(ra1 - ra2);

        return Math.toDegrees(Math.acos(Math.min(1, Math.max(-1, cosSep))));
    }

    private static double normalizeAngle(double deg) {
        deg = deg % 360.0;
        return deg < 0 ? deg + 360.0 : deg;
    }

    @Override
    public String toString() {
        return String.format("RA=%s, Dec=%s", getRaHms(), getDecDms());
    }

    // --- Well-known stars ---

    /** Polaris (α UMi) */
    public static final EquatorialCoordinate POLARIS = fromHmsDms(2, 31, 49.09, 89, 15, 50.8);

    /** Sirius (α CMa) */
    public static final EquatorialCoordinate SIRIUS = fromHmsDms(6, 45, 8.9, -16, 42, 58.0);

    /** Vega (α Lyr) */
    public static final EquatorialCoordinate VEGA = fromHmsDms(18, 36, 56.3, 38, 47, 1.3);

    /** Betelgeuse (α Ori) */
    public static final EquatorialCoordinate BETELGEUSE = fromHmsDms(5, 55, 10.3, 7, 24, 25.4);
}
