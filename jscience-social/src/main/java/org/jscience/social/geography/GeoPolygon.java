/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.social.geography;

import org.jscience.natural.earth.coordinates.EarthCoordinate;
import org.jscience.natural.earth.coordinates.GeodeticCoordinate;
import org.jscience.core.mathematics.geometry.boundaries.Boundary;
import org.jscience.core.mathematics.geometry.boundaries.BoundingBox;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Units;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

/**
 * A geographical polygon defined by an exterior ring and zero or more interior rings (holes).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class GeoPolygon implements Boundary<EarthCoordinate> {

    private static final long serialVersionUID = 1L;


    @Attribute
    private final List<GeodeticCoordinate> exterior = new ArrayList<>();

    @Attribute
    private final List<List<GeodeticCoordinate>> interiors = new ArrayList<>();

    public GeoPolygon() {
    }

    public void setExterior(List<GeodeticCoordinate> points) {
        this.exterior.clear();
        this.exterior.addAll(points);
    }

    public List<GeodeticCoordinate> getExterior() {
        return Collections.unmodifiableList(exterior);
    }

    public void addInterior(List<GeodeticCoordinate> points) {
        this.interiors.add(new ArrayList<>(points));
    }

    public List<List<GeodeticCoordinate>> getInteriors() {
        return Collections.unmodifiableList(interiors);
    }

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public boolean contains(EarthCoordinate point) {
        if (isEmpty() || point == null) return false;
        
        GeodeticCoordinate geoPt = point.toGeodetic();
        double x = geoPt.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double y = geoPt.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        
        // Ray casting algorithm for exterior
        boolean inside = isPointInRing(x, y, exterior);
        if (!inside) return false;
        
        // Check holes
        for (List<GeodeticCoordinate> hole : interiors) {
            if (isPointInRing(x, y, hole)) return false;
        }
        
        return true;
    }

    private boolean isPointInRing(double x, double y, List<GeodeticCoordinate> ring) {
        boolean inside = false;
        for (int i = 0, j = ring.size() - 1; i < ring.size(); j = i++) {
            double xi = ring.get(i).getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            double yi = ring.get(i).getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            double xj = ring.get(j).getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            double yj = ring.get(j).getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            
            boolean intersect = ((yi > y) != (yj > y))
                && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
            if (intersect) inside = !inside;
        }
        return inside;
    }

    @Override
    public boolean isEmpty() {
        return exterior.isEmpty();
    }

    @Override
    public EarthCoordinate getCentroid() {
        if (isEmpty()) return null;
        double sumLat = 0, sumLon = 0, sumH = 0;
        for (GeodeticCoordinate c : exterior) {
            sumLat += c.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            sumLon += c.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            sumH += c.getHeight().to(Units.METER).getValue().doubleValue();
        }
        int n = exterior.size();
        return new GeodeticCoordinate(sumLat / n, sumLon / n, sumH / n);
    }

    @Override
    public BoundingBox<EarthCoordinate> getBoundingBox() {
        if (isEmpty()) return null;
        double minLat = Double.POSITIVE_INFINITY;
        double maxLat = Double.NEGATIVE_INFINITY;
        double minLon = Double.POSITIVE_INFINITY;
        double maxLon = Double.NEGATIVE_INFINITY;
        
        for (GeodeticCoordinate p : exterior) {
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
        // Shoelace formula in degrees^2 (rough estimate)
        double area = 0;
        for (int i = 0; i < exterior.size(); i++) {
            GeodeticCoordinate c1 = exterior.get(i);
            GeodeticCoordinate c2 = exterior.get((i + 1) % exterior.size());
            double x1 = c1.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            double y1 = c1.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            double x2 = c2.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            double y2 = c2.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            area += x1 * y2 - x2 * y1;
        }
        return Real.of(Math.abs(area) / 2.0);
    }

    @Override
    public Real getBoundaryMeasure() {
        double perimeter = 0;
        for (int i = 0; i < exterior.size(); i++) {
            perimeter += exterior.get(i).distanceTo(exterior.get((i + 1) % exterior.size())).to(Units.METER).getValue().doubleValue();
        }
        return Real.of(perimeter);
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

