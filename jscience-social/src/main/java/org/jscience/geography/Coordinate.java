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
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Angle;

import java.io.Serializable;
import java.util.Objects;

/**
 * A basic geographical coordinate (latitude, longitude, altitude) for social geography applications.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Coordinate implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private Quantity<Angle> latitude;
    private Quantity<Angle> longitude;
    private Quantity<Length> altitude;

    /**
     * Creates a new coordinate.
     *
     * @param latitude  the latitude (usually in degrees)
     * @param longitude the longitude (usually in degrees)
     */
    public Coordinate(Quantity<Angle> latitude, Quantity<Angle> longitude) {
        this(latitude, longitude, Quantities.create(0, Units.METER));
    }

    /**
     * Creates a new coordinate with altitude.
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     * @param altitude  the altitude
     */
    public Coordinate(Quantity<Angle> latitude, Quantity<Angle> longitude, Quantity<Length> altitude) {
        this.latitude = Objects.requireNonNull(latitude);
        this.longitude = Objects.requireNonNull(longitude);
        this.altitude = Objects.requireNonNull(altitude);
    }

    /**
     * Creates a new coordinate from decimal degrees.
     *
     * @param latDegrees latitude in degrees
     * @param lonDegrees longitude in degrees
     */
    public Coordinate(double latDegrees, double lonDegrees) {
        this(Quantities.create(latDegrees, Units.DEGREE_ANGLE), 
             Quantities.create(lonDegrees, Units.DEGREE_ANGLE));
    }

    /**
     * Creates a new coordinate from decimal degrees and altitude in meters.
     *
     * @param latDegrees latitude in degrees
     * @param lonDegrees longitude in degrees
     * @param altMeters  altitude in meters
     */
    public Coordinate(double latDegrees, double lonDegrees, double altMeters) {
        this(Quantities.create(latDegrees, Units.DEGREE_ANGLE), 
             Quantities.create(lonDegrees, Units.DEGREE_ANGLE),
             Quantities.create(altMeters, Units.METER));
    }

    public Quantity<Angle> getLatitude() {
        return latitude;
    }

    public void setLatitude(Quantity<Angle> latitude) {
        this.latitude = latitude;
    }

    public Quantity<Angle> getLongitude() {
        return longitude;
    }

    public void setLongitude(Quantity<Angle> longitude) {
        this.longitude = longitude;
    }

    public Quantity<Length> getAltitude() {
        return altitude;
    }

    public void setAltitude(Quantity<Length> altitude) {
        this.altitude = altitude;
    }

    /**
     * Calculates the great-circle distance to another coordinate.
     *
     * @param other the other coordinate
     * @return the distance
     */
    public Quantity<Length> distanceTo(Coordinate other) {
        double lat1 = latitude.to(Units.RADIAN).getValue().doubleValue();
        double lon1 = longitude.to(Units.RADIAN).getValue().doubleValue();
        double lat2 = other.latitude.to(Units.RADIAN).getValue().doubleValue();
        double lon2 = other.longitude.to(Units.RADIAN).getValue().doubleValue();

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Earth radius approximation
        double r = 6371000; // meters
        return Quantities.create(r * c, Units.METER);
    }

    @Override
    public String toString() {
        return String.format("Coordinate[lat=%s, lon=%s, alt=%s]", latitude, longitude, altitude);
    }
}
