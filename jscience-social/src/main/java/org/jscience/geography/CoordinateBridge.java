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

import org.jscience.earth.coordinates.GeodeticCoordinate;
import org.jscience.earth.coordinates.ReferenceEllipsoid;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

/**
 * Bridge utility for converting between {@link Coordinate} (social geography)
 * and {@link GeodeticCoordinate} (scientific geodesy).
 * <p>
 * This class provides conversion methods to seamlessly work with both
 * coordinate representations depending on the use case.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class CoordinateBridge {

    private CoordinateBridge() {
        // Utility class
    }

    /**
     * Converts a social geography Coordinate to a scientific GeodeticCoordinate.
     *
     * @param coordinate the social geography coordinate
     * @return equivalent geodetic coordinate using WGS84 ellipsoid
     */
    public static GeodeticCoordinate toGeodetic(Coordinate coordinate) {
        return new GeodeticCoordinate(
                coordinate.getLatitude(),
                coordinate.getLongitude(),
                coordinate.getAltitude(),
                ReferenceEllipsoid.WGS84);
    }

    /**
     * Converts a social geography Coordinate to a GeodeticCoordinate with
     * specified reference ellipsoid.
     *
     * @param coordinate the social geography coordinate
     * @param ellipsoid  the reference ellipsoid to use
     * @return equivalent geodetic coordinate
     */
    public static GeodeticCoordinate toGeodetic(Coordinate coordinate,
            ReferenceEllipsoid ellipsoid) {
        return new GeodeticCoordinate(
                coordinate.getLatitude(),
                coordinate.getLongitude(),
                coordinate.getAltitude(),
                ellipsoid);
    }

    /**
     * Converts a scientific GeodeticCoordinate to a social geography Coordinate.
     *
     * @param geodetic the geodetic coordinate
     * @return equivalent social geography coordinate
     */
    public static Coordinate fromGeodetic(GeodeticCoordinate geodetic) {
        return new Coordinate(
                geodetic.getLatitude(),
                geodetic.getLongitude(),
                geodetic.getHeight());
    }

    /**
     * Creates a GeodeticCoordinate from latitude/longitude in degrees.
     *
     * @param latitudeDegrees  latitude in degrees
     * @param longitudeDegrees longitude in degrees
     * @param altitudeMeters   altitude in meters
     * @return geodetic coordinate using WGS84
     */
    public static GeodeticCoordinate geodeticFromDegrees(double latitudeDegrees,
            double longitudeDegrees,
            double altitudeMeters) {
        return new GeodeticCoordinate(
                Quantities.create(latitudeDegrees, Units.DEGREE_ANGLE),
                Quantities.create(longitudeDegrees, Units.DEGREE_ANGLE),
                Quantities.create(altitudeMeters, Units.METER),
                ReferenceEllipsoid.WGS84);
    }

    /**
     * Calculates the distance between two coordinates using the standard
     * Haversine calculation.
     *
     * @param from starting coordinate
     * @param to   ending coordinate
     * @return distance in meters
     */
    public static double haversineDistance(Coordinate from, Coordinate to) {
        return from.distanceTo(to).to(Units.METER).getValue().doubleValue();
    }
}
