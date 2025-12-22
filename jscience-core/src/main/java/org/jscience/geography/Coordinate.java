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
package org.jscience.geography;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a geographic coordinate (latitude/longitude).
 * <p>
 * Uses WGS84 datum by default.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Coordinate {

    private static final Real NINETY = Real.of(90);
    private static final Real NEG_NINETY = Real.of(-90);
    private static final Real ONE_EIGHTY = Real.of(180);
    private static final Real NEG_ONE_EIGHTY = Real.of(-180);
    private static final Real EARTH_RADIUS = Real.of(6371000);
    private static final Real THREE_SIXTY = Real.of(360);

    private final Real latitude; // -90 to +90
    private final Real longitude; // -180 to +180
    private final Real altitude; // meters above sea level

    public Coordinate(Real latitude, Real longitude) {
        this(latitude, longitude, Real.ZERO);
    }

    public Coordinate(Real latitude, Real longitude, Real altitude) {
        if (latitude.compareTo(NEG_NINETY) < 0 || latitude.compareTo(NINETY) > 0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude.compareTo(NEG_ONE_EIGHTY) < 0 || longitude.compareTo(ONE_EIGHTY) > 0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public Coordinate(double latitude, double longitude) {
        this(Real.of(latitude), Real.of(longitude), Real.ZERO);
    }

    public Coordinate(double latitude, double longitude, double altitude) {
        this(Real.of(latitude), Real.of(longitude), Real.of(altitude));
    }

    public Real getLatitude() {
        return latitude;
    }

    public Real getLongitude() {
        return longitude;
    }

    public Real getAltitude() {
        return altitude;
    }

    /**
     * Calculates distance to another coordinate using Haversine formula.
     *
     * @param other target coordinate
     * @return distance in meters as Real
     */
    public Real distanceTo(Coordinate other) {
        Real lat1 = latitude.toRadians();
        Real lat2 = other.latitude.toRadians();
        Real dLat = other.latitude.subtract(latitude).toRadians();
        Real dLon = other.longitude.subtract(longitude).toRadians();

        Real sinDLatHalf = dLat.divide(Real.TWO).sin();
        Real sinDLonHalf = dLon.divide(Real.TWO).sin();

        Real a = sinDLatHalf.pow(2).add(
                lat1.cos().multiply(lat2.cos()).multiply(sinDLonHalf.pow(2)));
        Real sqrtA = a.sqrt();
        Real sqrtOneMinusA = Real.ONE.subtract(a).sqrt();
        Real c = Real.TWO.multiply(sqrtA.atan2(sqrtOneMinusA));
        return EARTH_RADIUS.multiply(c);
    }

    /**
     * Calculates bearing to another coordinate.
     *
     * @return bearing in degrees (0-360)
     */
    public Real bearingTo(Coordinate other) {
        Real lat1 = latitude.toRadians();
        Real lat2 = other.latitude.toRadians();
        Real dLon = other.longitude.subtract(longitude).toRadians();

        Real y = dLon.sin().multiply(lat2.cos());
        Real x = lat1.cos().multiply(lat2.sin()).subtract(
                lat1.sin().multiply(lat2.cos()).multiply(dLon.cos()));
        Real bearing = y.atan2(x).toDegrees();
        // Normalize to 0-360
        Real normalized = bearing.add(THREE_SIXTY);
        return normalized.mod(THREE_SIXTY);
    }

    /**
     * Returns coordinate in DMS format.
     */
    public String toDMS() {
        return String.format("%s, %s", formatDMS(latitude, 'N', 'S'), formatDMS(longitude, 'E', 'W'));
    }

    private String formatDMS(Real coord, char pos, char neg) {
        char dir = coord.compareTo(Real.ZERO) >= 0 ? pos : neg;
        double absCoord = coord.abs().doubleValue();
        int d = (int) absCoord;
        double m = (absCoord - d) * 60;
        int mi = (int) m;
        double s = (m - mi) * 60;
        return String.format("%d°%d'%.2f\"%c", d, mi, s, dir);
    }

    @Override
    public String toString() {
        return String.format("(%.6f, %.6f)", latitude.doubleValue(), longitude.doubleValue());
    }

    // Notable locations
    public static final Coordinate GREENWICH = new Coordinate(Real.of(51.4772), Real.ZERO);
    public static final Coordinate NORTH_POLE = new Coordinate(NINETY, Real.ZERO);
    public static final Coordinate SOUTH_POLE = new Coordinate(NEG_NINETY, Real.ZERO);
    public static final Coordinate EQUATOR_PRIME = new Coordinate(Real.ZERO, Real.ZERO);
}