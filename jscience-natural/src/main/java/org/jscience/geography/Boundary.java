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

import org.jscience.util.Positioned;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a geographical boundary as a convex hull of coordinates.
 * <p>
 * A boundary defines a closed polygonal region where the last coordinate is
 * implicitly connected to the first. This is the base class for various
 * boundary types:
 * </p>
 * <ul>
 *   <li>{@link PointBoundary} - A single point location (degenerate boundary)</li>
 *   <li>{@link FuzzyBoundary} - A boundary with uncertainty/probability</li>
 *   <li>{@link TimedBoundary} - A boundary that changes over time</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public class Boundary implements Positioned, Serializable {

    private static final long serialVersionUID = 1L;

    /** The coordinates defining this boundary (convex hull). */
    protected final Coordinate[] coordinates;

    /** Whether each edge is included in the boundary. */
    protected final boolean[] edgesIncluded;

    /**
     * Creates an empty boundary (point at origin).
     */
    public Boundary() {
        this.coordinates = new Coordinate[0];
        this.edgesIncluded = new boolean[0];
    }

    /**
     * Creates a boundary from a single coordinate (a point).
     *
     * @param point the point coordinate
     * @throws NullPointerException if point is null
     */
    public Boundary(Coordinate point) {
        Objects.requireNonNull(point, "Point cannot be null");
        this.coordinates = new Coordinate[] { point };
        this.edgesIncluded = new boolean[] { true };
    }

    /**
     * Creates a polygonal boundary from an array of coordinates.
     * The polygon is closed: last coordinate connects to the first.
     *
     * @param coordinates the coordinates defining the boundary vertices
     * @throws IllegalArgumentException if coordinates is null or empty
     */
    public Boundary(Coordinate[] coordinates) {
        if (coordinates == null || coordinates.length == 0) {
            throw new IllegalArgumentException("Coordinates array cannot be null or empty");
        }
        this.coordinates = Arrays.copyOf(coordinates, coordinates.length);
        this.edgesIncluded = new boolean[coordinates.length];
        Arrays.fill(this.edgesIncluded, true);
    }

    /**
     * Creates a polygonal boundary with explicit edge inclusion.
     *
     * @param coordinates     the coordinates defining the boundary vertices
     * @param edgesIncluded   whether each edge is included (true) or excluded (false)
     * @throws IllegalArgumentException if arrays are null or have mismatched lengths
     */
    public Boundary(Coordinate[] coordinates, boolean[] edgesIncluded) {
        if (coordinates == null || edgesIncluded == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        if (coordinates.length != edgesIncluded.length) {
            throw new IllegalArgumentException(
                "Coordinates and edgesIncluded arrays must have same length");
        }
        this.coordinates = Arrays.copyOf(coordinates, coordinates.length);
        this.edgesIncluded = Arrays.copyOf(edgesIncluded, edgesIncluded.length);
    }

    /**
     * Returns the coordinates defining this boundary.
     *
     * @return array of coordinates (defensive copy)
     */
    public Coordinate[] getCoordinates() {
        return Arrays.copyOf(coordinates, coordinates.length);
    }

    /**
     * Returns the number of vertices in this boundary.
     *
     * @return vertex count
     */
    public int getVertexCount() {
        return coordinates.length;
    }

    /**
     * Returns whether this is a point (single coordinate).
     *
     * @return true if point boundary
     */
    public boolean isPoint() {
        return coordinates.length == 1;
    }

    /**
     * Returns whether this is a line (two coordinates).
     *
     * @return true if line boundary
     */
    public boolean isLine() {
        return coordinates.length == 2;
    }

    /**
     * Returns whether this represents a polygon (3+ coordinates).
     *
     * @return true if polygon boundary
     */
    public boolean isPolygon() {
        return coordinates.length >= 3;
    }

    /**
     * Returns the edge inclusion flags.
     *
     * @return array of edge inclusion flags (defensive copy)
     */
    public boolean[] getEdgesIncluded() {
        return Arrays.copyOf(edgesIncluded, edgesIncluded.length);
    }

    /**
     * Returns the centroid of this boundary.
     *
     * @return centroid coordinate, or null if empty
     */
    public Coordinate getCentroid() {
        if (coordinates.length == 0) {
            return null;
        }
        if (coordinates.length == 1) {
            return coordinates[0];
        }

        double latSum = 0, lonSum = 0;
        for (Coordinate c : coordinates) {
            latSum += c.getLatitudeDegrees().doubleValue();
            lonSum += c.getLongitudeDegrees().doubleValue();
        }
        return new Coordinate(latSum / coordinates.length, lonSum / coordinates.length);
    }

    /**
     * Checks if a coordinate is contained within this boundary.
     * Uses ray casting algorithm for polygon containment.
     *
     * @param point the point to check
     * @return true if point is inside or on the boundary
     */
    public boolean contains(Coordinate point) {
        if (point == null || coordinates.length == 0) {
            return false;
        }
        if (coordinates.length == 1) {
            return coordinates[0].equals(point);
        }
        if (coordinates.length == 2) {
            // Line case - check if point is on the line segment
            return isOnLineSegment(point, coordinates[0], coordinates[1]);
        }

        // Ray casting for polygon
        double x = point.getLongitudeDegrees().doubleValue();
        double y = point.getLatitudeDegrees().doubleValue();
        boolean inside = false;

        for (int i = 0, j = coordinates.length - 1; i < coordinates.length; j = i++) {
            double xi = coordinates[i].getLongitudeDegrees().doubleValue();
            double yi = coordinates[i].getLatitudeDegrees().doubleValue();
            double xj = coordinates[j].getLongitudeDegrees().doubleValue();
            double yj = coordinates[j].getLatitudeDegrees().doubleValue();

            if (((yi > y) != (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi) + xi)) {
                inside = !inside;
            }
        }
        return inside;
    }

    private boolean isOnLineSegment(Coordinate p, Coordinate a, Coordinate b) {
        double px = p.getLongitudeDegrees().doubleValue();
        double py = p.getLatitudeDegrees().doubleValue();
        double ax = a.getLongitudeDegrees().doubleValue();
        double ay = a.getLatitudeDegrees().doubleValue();
        double bx = b.getLongitudeDegrees().doubleValue();
        double by = b.getLatitudeDegrees().doubleValue();

        double crossProduct = (py - ay) * (bx - ax) - (px - ax) * (by - ay);
        if (Math.abs(crossProduct) > 1e-9) return false;

        return Math.min(ax, bx) <= px && px <= Math.max(ax, bx) &&
               Math.min(ay, by) <= py && py <= Math.max(ay, by);
    }

    /**
     * Computes the approximate area of this boundary in square meters.
     * Uses the Shoelace formula with spherical correction.
     *
     * @return area in square meters
     */
    public double computeArea() {
        if (coordinates.length < 3) {
            return 0.0;
        }

        // Shoelace approximation (works for small regions)
        double sum = 0;
        for (int i = 0; i < coordinates.length; i++) {
            int j = (i + 1) % coordinates.length;
            double lat1 = Math.toRadians(coordinates[i].getLatitudeDegrees().doubleValue());
            double lon1 = Math.toRadians(coordinates[i].getLongitudeDegrees().doubleValue());
            double lat2 = Math.toRadians(coordinates[j].getLatitudeDegrees().doubleValue());
            double lon2 = Math.toRadians(coordinates[j].getLongitudeDegrees().doubleValue());
            sum += (lon2 - lon1) * (2 + Math.sin(lat1) + Math.sin(lat2));
        }
        double earthRadius = 6371000; // meters
        return Math.abs(sum * earthRadius * earthRadius / 2.0);
    }

    /**
     * Computes the perimeter of this boundary in meters.
     *
     * @return perimeter in meters
     */
    public double computePerimeter() {
        if (coordinates.length < 2) {
            return 0.0;
        }

        double perimeter = 0;
        for (int i = 0; i < coordinates.length; i++) {
            int j = (i + 1) % coordinates.length;
            perimeter += coordinates[i].distanceTo(coordinates[j]).getValue().doubleValue();
        }
        return perimeter;
    }

    @Override
    public Object getPosition() {
        return getCentroid();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Boundary)) return false;
        Boundary other = (Boundary) obj;
        return Arrays.equals(coordinates, other.coordinates) &&
               Arrays.equals(edgesIncluded, other.edgesIncluded);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(coordinates);
        result = 31 * result + Arrays.hashCode(edgesIncluded);
        return result;
    }

    @Override
    public String toString() {
        if (coordinates.length == 0) {
            return "Boundary[empty]";
        }
        if (coordinates.length == 1) {
            return "Boundary[point=" + coordinates[0] + "]";
        }
        return "Boundary[vertices=" + coordinates.length + ", centroid=" + getCentroid() + "]";
    }

    // ===== Factory methods =====

    /**
     * Creates a point boundary.
     *
     * @param lat latitude in degrees
     * @param lon longitude in degrees
     * @return point boundary
     */
    public static Boundary point(double lat, double lon) {
        return new Boundary(new Coordinate(lat, lon));
    }

    /**
     * Creates a rectangular boundary from two corners.
     *
     * @param lat1 latitude of first corner
     * @param lon1 longitude of first corner
     * @param lat2 latitude of opposite corner
     * @param lon2 longitude of opposite corner
     * @return rectangular boundary
     */
    public static Boundary rectangle(double lat1, double lon1, double lat2, double lon2) {
        return new Boundary(new Coordinate[] {
            new Coordinate(lat1, lon1),
            new Coordinate(lat1, lon2),
            new Coordinate(lat2, lon2),
            new Coordinate(lat2, lon1)
        });
    }

    /**
     * Creates a circular boundary approximated with n vertices.
     *
     * @param centerLat center latitude
     * @param centerLon center longitude
     * @param radiusMeters radius in meters
     * @param vertices number of vertices (minimum 8)
     * @return circular boundary approximation
     */
    public static Boundary circle(double centerLat, double centerLon, double radiusMeters, int vertices) {
        vertices = Math.max(8, vertices);
        Coordinate[] coords = new Coordinate[vertices];
        double earthRadius = 6371000;
        double angularRadius = radiusMeters / earthRadius;

        for (int i = 0; i < vertices; i++) {
            double angle = 2 * Math.PI * i / vertices;
            double lat = Math.asin(
                Math.sin(Math.toRadians(centerLat)) * Math.cos(angularRadius) +
                Math.cos(Math.toRadians(centerLat)) * Math.sin(angularRadius) * Math.cos(angle)
            );
            double lon = Math.toRadians(centerLon) + Math.atan2(
                Math.sin(angle) * Math.sin(angularRadius) * Math.cos(Math.toRadians(centerLat)),
                Math.cos(angularRadius) - Math.sin(Math.toRadians(centerLat)) * Math.sin(lat)
            );
            coords[i] = new Coordinate(Math.toDegrees(lat), Math.toDegrees(lon));
        }
        return new Boundary(coords);
    }
}
