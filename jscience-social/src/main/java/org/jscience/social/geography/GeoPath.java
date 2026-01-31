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

package org.jscience.social.geography;

import org.jscience.natural.earth.coordinates.EarthCoordinate;
import org.jscience.natural.earth.coordinates.GeodeticCoordinate;
import org.jscience.core.mathematics.geometry.boundaries.Boundary;
import org.jscience.core.mathematics.geometry.boundaries.BoundingBox;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantity;

import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.util.Named;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * Represents a sequence of geodetic coordinates forming a path, route, or boundary.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class GeoPath implements Named, Boundary<EarthCoordinate> {



    private static final long serialVersionUID = 1L;

    @Attribute
    private String name;

    @Attribute
    private final List<GeodeticCoordinate> points = new ArrayList<>();

    public GeoPath() {
    }

    public GeoPath(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPoint(GeodeticCoordinate point) {
        points.add(Objects.requireNonNull(point));
    }

    public List<GeodeticCoordinate> getPoints() {
        return Collections.unmodifiableList(points);
    }

    public int size() {
        return points.size();
    }

    public GeodeticCoordinate getStart() {
        return points.isEmpty() ? null : points.get(0);
    }

    public GeodeticCoordinate getEnd() {
        return points.isEmpty() ? null : points.get(points.size() - 1);
    }

    /**
     * Calculates the total length of the path by summing segment distances.
     * 
     * @return total length quantity
     */
    public Quantity<Length> getLength() {
        double totalMeters = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            Quantity<Length> dist = points.get(i).distanceTo(points.get(i + 1));
            if (dist != null) {
                totalMeters += dist.to(Units.METER).getValue().doubleValue();
            }
        }
        return Quantities.create(totalMeters, Units.METER);
    }

    /**
     * Returns a reversed copy of this path.
     */
    public GeoPath reverse() {
        GeoPath reversed = new GeoPath(name != null ? name + " (reversed)" : null);
        List<GeodeticCoordinate> revPoints = new ArrayList<>(points);
        Collections.reverse(revPoints);
        reversed.points.addAll(revPoints);
        return reversed;
    }

    @Override
    public String toString() {
        return String.format("Path: %s (%d points, %s)", name, points.size(), getLength());
    }

    // --- Boundary implementation ---
    
    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public boolean contains(EarthCoordinate point) {

        if (point == null) return false;
        // Exact containment on a path is rare, but we check vertices
        for (GeodeticCoordinate p : points) {
            if (p.equals(point)) return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return points.isEmpty();
    }

    @Override
    public EarthCoordinate getCentroid() {
        if (points.isEmpty()) return null;
        double sumLat = 0, sumLon = 0, sumH = 0;
        for (GeodeticCoordinate p : points) {
            sumLat += p.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            sumLon += p.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            sumH += p.getHeight().to(Units.METER).getValue().doubleValue();
        }
        return new GeodeticCoordinate(sumLat / points.size(), sumLon / points.size(), sumH / points.size());
    }

    @Override
    public BoundingBox<EarthCoordinate> getBoundingBox() {
        if (points.isEmpty()) return null;
        double minLat = Double.POSITIVE_INFINITY;
        double maxLat = Double.NEGATIVE_INFINITY;
        double minLon = Double.POSITIVE_INFINITY;
        double maxLon = Double.NEGATIVE_INFINITY;
        
        for (GeodeticCoordinate p : points) {
            double lat = p.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            double lon = p.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            if (lat < minLat) minLat = lat;
            if (lat > maxLat) maxLat = lat;
            if (lon < minLon) minLon = lon;
            if (lon > maxLon) maxLon = lon;
        }
        
        return new GeoBoundingBox(
            new GeodeticCoordinate(minLat, minLon, 0),
            new GeodeticCoordinate(maxLat, maxLon, 0)
        );
    }



    @Override
    public Real getMeasure() {
        return getLength().to(Units.METER).getValue();
    }

    @Override
    public Real getBoundaryMeasure() {
        return Real.ZERO;
    }

    @Override
    public boolean intersects(Boundary<EarthCoordinate> other) {
        if (other == null) return false;
        BoundingBox<EarthCoordinate> bbox = getBoundingBox();
        BoundingBox<EarthCoordinate> otherBbox = other.getBoundingBox();
        if (bbox == null || otherBbox == null) return false;
        return bbox.intersects(otherBbox);
    }


    @Override
    public Boundary<EarthCoordinate> union(Boundary<EarthCoordinate> other) {
        return null;
    }

    @Override
    public Boundary<EarthCoordinate> intersection(Boundary<EarthCoordinate> other) {
        return null;
    }

    @Override
    public Boundary<EarthCoordinate> convexHull() {
        return null;
    }

    @Override
    public Boundary<EarthCoordinate> translate(EarthCoordinate offset) {
        return this;
    }

    @Override
    public Boundary<EarthCoordinate> scale(Real factor) {
        return this;
    }
}



