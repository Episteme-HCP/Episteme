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
 * Horizontal (Alt-Az) celestial coordinates.
 *
 * Altitude: Angle above horizon (0Ã‚Â° = horizon, 90Ã‚Â° = zenith).
 * Azimuth: Angle measured from North, clockwise (0Ã‚Â° = N, 90Ã‚Â° = E).
 *
 * These coordinates are local (depend on observer position and time).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HorizontalCoordinate {

    private final double altitude; // [Ã¢Ë†â€™90, +90] degrees
    private final double azimuth; // [0, 360) degrees

    public HorizontalCoordinate(double altitude, double azimuth) {
        this.altitude = Math.max(-90, Math.min(90, altitude));
        this.azimuth = normalizeAngle(azimuth);
    }

    /**
     * Converts equatorial coordinates to horizontal for a given observer.
     * 
     * @param eq       Equatorial coordinates
     * @param latitude Observer latitude (degrees, North positive)
     * @param lmst     Local Mean Sidereal Time (degrees)
     * @return Horizontal coordinates
     */
    public static HorizontalCoordinate fromEquatorial(
            EquatorialCoordinate eq, double latitude, double lmst) {

        double ha = Math.toRadians(lmst - eq.getRaDegrees()); // Hour angle
        double dec = Math.toRadians(eq.getDecDegrees());
        double lat = Math.toRadians(latitude);

        // Altitude
        double sinAlt = Math.sin(dec) * Math.sin(lat)
                + Math.cos(dec) * Math.cos(lat) * Math.cos(ha);
        double alt = Math.toDegrees(Math.asin(sinAlt));

        // Azimuth
        double cosAz = (Math.sin(dec) - Math.sin(lat) * sinAlt)
                / (Math.cos(lat) * Math.cos(Math.asin(sinAlt)));
        double az = Math.toDegrees(Math.acos(Math.min(1, Math.max(-1, cosAz))));

        if (Math.sin(ha) > 0) {
            az = 360.0 - az;
        }

        return new HorizontalCoordinate(alt, az);
    }

    /**
     * Converts horizontal coordinates to equatorial.
     * 
     * @param latitude Observer latitude (degrees)
     * @param lmst     Local Mean Sidereal Time (degrees)
     * @return Equatorial coordinates
     */
    public EquatorialCoordinate toEquatorial(double latitude, double lmst) {
        double alt = Math.toRadians(altitude);
        double az = Math.toRadians(azimuth);
        double lat = Math.toRadians(latitude);

        // Declination
        double sinDec = Math.sin(alt) * Math.sin(lat)
                + Math.cos(alt) * Math.cos(lat) * Math.cos(az);
        double dec = Math.asin(sinDec);

        // Hour angle
        double cosHa = (Math.sin(alt) - Math.sin(lat) * sinDec)
                / (Math.cos(lat) * Math.cos(dec));
        double ha = Math.acos(Math.min(1, Math.max(-1, cosHa)));

        if (Math.sin(az) > 0) {
            ha = 2 * Math.PI - ha;
        }

        double ra = normalizeAngle(lmst - Math.toDegrees(ha));

        return new EquatorialCoordinate(ra, Math.toDegrees(dec));
    }

    public double getAltitude() {
        return altitude;
    }

    public double getAzimuth() {
        return azimuth;
    }

    /**
     * Returns true if object is above horizon.
     */
    public boolean isVisible() {
        return altitude > 0;
    }

    /**
     * Returns airmass (secant approximation, valid for alt > 15Ã‚Â°).
     * For lower altitudes, use more accurate formulas.
     */
    public double getAirmass() {
        if (altitude <= 0)
            return Double.POSITIVE_INFINITY;
        double z = Math.toRadians(90 - altitude); // Zenith angle
        return 1.0 / Math.cos(z);
    }

    /**
     * Returns airmass using Hardie formula (better for low altitudes).
     */
    public double getAirmassHardie() {
        double z = 90 - altitude;
        double secZ = 1.0 / Math.cos(Math.toRadians(z));
        return secZ - 0.0018167 * (secZ - 1)
                - 0.002875 * Math.pow(secZ - 1, 2)
                - 0.0008083 * Math.pow(secZ - 1, 3);
    }

    private static double normalizeAngle(double deg) {
        deg = deg % 360.0;
        return deg < 0 ? deg + 360.0 : deg;
    }

    @Override
    public String toString() {
        return String.format("Alt=%.2fÃ‚Â°, Az=%.2fÃ‚Â°", altitude, azimuth);
    }
}


