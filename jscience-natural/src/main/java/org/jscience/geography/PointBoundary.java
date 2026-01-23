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

import java.util.Objects;

/**
 * A degenerate boundary representing a single point location.
 * <p>
 * This is the simplest form of boundary, representing an exact location
 * with no spatial extent. Useful for representing cities, landmarks,
 * or any point of interest.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.0
 * @since 2.0
 */
public class PointBoundary extends Boundary {

    private static final long serialVersionUID = 1L;

    /** Optional name for the point. */
    private final String name;

    /** Optional elevation in meters. */
    private final Double elevation;

    /**
     * Creates a point boundary at the specified coordinate.
     *
     * @param coordinate the location
     * @throws NullPointerException if coordinate is null
     */
    public PointBoundary(Coordinate coordinate) {
        super(coordinate);
        this.name = null;
        this.elevation = null;
    }

    /**
     * Creates a named point boundary.
     *
     * @param coordinate the location
     * @param name       the name of the point
     */
    public PointBoundary(Coordinate coordinate, String name) {
        super(coordinate);
        this.name = name;
        this.elevation = null;
    }

    /**
     * Creates a fully specified point boundary.
     *
     * @param coordinate the location
     * @param name       the name of the point
     * @param elevation  the elevation in meters
     */
    public PointBoundary(Coordinate coordinate, String name, Double elevation) {
        super(coordinate);
        this.name = name;
        this.elevation = elevation;
    }

    /**
     * Creates a point boundary from latitude and longitude.
     *
     * @param latitude  latitude in degrees
     * @param longitude longitude in degrees
     */
    public PointBoundary(double latitude, double longitude) {
        super(new Coordinate(latitude, longitude));
        this.name = null;
        this.elevation = null;
    }

    /**
     * Returns the coordinate of this point.
     *
     * @return the coordinate
     */
    public Coordinate getCoordinate() {
        return getCoordinates()[0];
    }

    /**
     * Returns the name of this point.
     *
     * @return the name, or null if unnamed
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the elevation of this point.
     *
     * @return elevation in meters, or null if unknown
     */
    public Double getElevation() {
        return elevation;
    }

    /**
     * Returns the latitude in degrees.
     *
     * @return latitude
     */
    public double getLatitude() {
        return getCoordinate().getLatitudeDegrees().doubleValue();
    }

    /**
     * Returns the longitude in degrees.
     *
     * @return longitude
     */
    public double getLongitude() {
        return getCoordinate().getLongitudeDegrees().doubleValue();
    }

    /**
     * Computes the distance to another point in meters.
     *
     * @param other the other point
     * @return distance in meters
     */
    public double distanceTo(PointBoundary other) {
        return getCoordinate().distanceTo(other.getCoordinate()).getValue().doubleValue();
    }

    /**
     * Computes the bearing to another point in degrees (0-360).
     *
     * @param other the other point
     * @return bearing in degrees
     */
    public double bearingTo(PointBoundary other) {
        return getCoordinate().bearingTo(other.getCoordinate()).getValue().doubleValue();
    }

    @Override
    public String toString() {
        if (name != null) {
            return "Point[" + name + " @ " + getCoordinate() + "]";
        }
        return "Point[" + getCoordinate() + "]";
    }

    // ===== Well-known locations =====

    public static final PointBoundary GREENWICH = new PointBoundary(51.4772, 0.0, "Greenwich Observatory");
    public static final PointBoundary MOUNT_EVEREST = new PointBoundary(
        new Coordinate(27.9881, 86.9250), "Mount Everest", 8848.86);
    public static final PointBoundary EIFFEL_TOWER = new PointBoundary(
        new Coordinate(48.8584, 2.2945), "Eiffel Tower", 330.0);
    public static final PointBoundary STATUE_OF_LIBERTY = new PointBoundary(
        new Coordinate(40.6892, -74.0445), "Statue of Liberty", 93.0);
}
