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

package org.jscience.geography;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Angle;
import org.jscience.measure.quantity.Length;
import org.jscience.mathematics.numbers.real.Real;

import java.util.Objects;

/**
 * Represents a geographic coordinate (latitude/longitude) using proper
 * unit-safe quantities.
 * <p>
 * Uses WGS84 datum by default. This class provides a simpler API for
 * social/human geography applications while internally using proper
 * Real precision where appropriate.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Coordinate {

    public static final Quantity<Length> EARTH_RADIUS = Quantities.create(6371000.0, Units.METER);

    private final Quantity<Angle> latitude;
    private final Quantity<Angle> longitude;
    private final Quantity<Length> altitude;

    /**
     * Creates a coordinate from latitude and longitude quantities.
     */
    public Coordinate(Quantity<Angle> latitude, Quantity<Angle> longitude) {
        this(latitude, longitude, Quantities.create(0, Units.METER));
    }

    /**
     * Creates a coordinate from latitude, longitude, and altitude quantities.
     */
    public Coordinate(Quantity<Angle> latitude, Quantity<Angle> longitude, Quantity<Length> altitude) {
        double latDeg = latitude.to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double lonDeg = longitude.to(Units.DEGREE_ANGLE).getValue().doubleValue();

        if (latDeg < -90 || latDeg > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
        if (lonDeg < -180 || lonDeg > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    /**
     * Creates a coordinate from double values in degrees.
     */
    public Coordinate(double latitudeDegrees, double longitudeDegrees) {
        this(Quantities.create(latitudeDegrees, Units.DEGREE_ANGLE),
                Quantities.create(longitudeDegrees, Units.DEGREE_ANGLE),
                Quantities.create(0, Units.METER));
    }

    /**
     * Creates a coordinate from double values in degrees with altitude in meters.
     */
    public Coordinate(double latitudeDegrees, double longitudeDegrees, double altitudeMeters) {
        this(Quantities.create(latitudeDegrees, Units.DEGREE_ANGLE),
                Quantities.create(longitudeDegrees, Units.DEGREE_ANGLE),
                Quantities.create(altitudeMeters, Units.METER));
    }

    /**
     * Returns the latitude as a Quantity.
     */
    public Quantity<Angle> getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude as a Quantity.
     */
    public Quantity<Angle> getLongitude() {
        return longitude;
    }

    /**
     * Returns the altitude as a Quantity.
     */
    public Quantity<Length> getAltitude() {
        return altitude;
    }

    /**
     * Returns latitude in degrees as a Real.
     */
    public Real getLatitudeDegrees() {
        return latitude.to(Units.DEGREE_ANGLE).getValue();
    }

    /**
     * Returns longitude in degrees as a Real.
     */
    public Real getLongitudeDegrees() {
        return longitude.to(Units.DEGREE_ANGLE).getValue();
    }

    /**
     * Returns altitude in meters as a Real.
     */
    public Real getAltitudeMeters() {
        return altitude.to(Units.METER).getValue();
    }

    /**
     * Calculates distance to another coordinate using Haversine formula.
     * Uses double precision for internal calculation logic for performance.
     *
     * @param other target coordinate
     * @return distance as a Length quantity
     */
    public Quantity<Length> distanceTo(Coordinate other) {
        double lat1 = Math.toRadians(getLatitudeDegrees().doubleValue());
        double lat2 = Math.toRadians(other.getLatitudeDegrees().doubleValue());
        double dLat = Math.toRadians(other.getLatitudeDegrees().doubleValue() - getLatitudeDegrees().doubleValue());
        double dLon = Math.toRadians(other.getLongitudeDegrees().doubleValue() - getLongitudeDegrees().doubleValue());

        double sinDLatHalf = Math.sin(dLat / 2);
        double sinDLonHalf = Math.sin(dLon / 2);

        double a = sinDLatHalf * sinDLatHalf +
                Math.cos(lat1) * Math.cos(lat2) * sinDLonHalf * sinDLonHalf;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS.getValue().doubleValue() * c;

        return Quantities.create(distance, Units.METER);
    }

    /**
     * Calculates bearing to another coordinate.
     *
     * @return bearing as an Angle quantity (0-360 degrees)
     */
    public Quantity<Angle> bearingTo(Coordinate other) {
        double lat1 = Math.toRadians(getLatitudeDegrees().doubleValue());
        double lat2 = Math.toRadians(other.getLatitudeDegrees().doubleValue());
        double dLon = Math.toRadians(other.getLongitudeDegrees().doubleValue() - getLongitudeDegrees().doubleValue());

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double bearing = Math.toDegrees(Math.atan2(y, x));

        // Normalize to 0-360
        bearing = (bearing + 360) % 360;
        return Quantities.create(bearing, Units.DEGREE_ANGLE);
    }

    /**
     * Returns coordinate in DMS (degrees, minutes, seconds) format.
     */
    public String toDMS() {
        return String.format("%s, %s",
                formatDMS(getLatitudeDegrees().doubleValue(), 'N', 'S'),
                formatDMS(getLongitudeDegrees().doubleValue(), 'E', 'W'));
    }

    private String formatDMS(double coord, char pos, char neg) {
        char dir = coord >= 0 ? pos : neg;
        double absCoord = Math.abs(coord);
        int d = (int) absCoord;
        double m = (absCoord - d) * 60;
        int mi = (int) m;
        double s = (m - mi) * 60;
        return String.format("%d°%d'%.2f\"%c", d, mi, s, dir);
    }

    @Override
    public String toString() {
        return String.format("(%.6f°, %.6f°)", getLatitudeDegrees().doubleValue(), getLongitudeDegrees().doubleValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Coordinate other = (Coordinate) obj;
        return getLatitudeDegrees().equals(other.getLatitudeDegrees()) &&
                getLongitudeDegrees().equals(other.getLongitudeDegrees()) &&
                getAltitudeMeters().equals(other.getAltitudeMeters());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitudeDegrees(), getLongitudeDegrees(), getAltitudeMeters());
    }

    // ========== Notable locations ==========

    public static final Coordinate GREENWICH = new Coordinate(51.4772, 0.0);
    public static final Coordinate NORTH_POLE = new Coordinate(90.0, 0.0);
    public static final Coordinate SOUTH_POLE = new Coordinate(-90.0, 0.0);
    public static final Coordinate EQUATOR_PRIME = new Coordinate(0.0, 0.0);
}
