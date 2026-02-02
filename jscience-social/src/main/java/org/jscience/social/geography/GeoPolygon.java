package org.jscience.social.geography;

import org.jscience.natural.earth.coordinates.EarthCoordinate;
import org.jscience.natural.earth.coordinates.GeodeticCoordinate;
import org.jscience.core.mathematics.geometry.boundaries.Boundary;
import org.jscience.core.mathematics.geometry.boundaries.BoundingBox;
import org.jscience.core.mathematics.geometry.boundaries.CompositeBoundary;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Units;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A geographical polygon supporting multiple disjoint regions (exclaves) 
 * and holes (enclaves).
 * Refactored to leverage {@link CompositeBoundary}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class GeoPolygon extends CompositeBoundary<EarthCoordinate> {

    private static final long serialVersionUID = 2L;

    public GeoPolygon() {
        super();
    }

    /**
     * Compatibility constructor for a single simple polygon.
     */
    public GeoPolygon(List<GeodeticCoordinate> exterior) {
        this();
        setExterior(exterior);
    }

    /**
     * Sets the main exterior ring of the first exclave.
     */
    public void setExterior(List<GeodeticCoordinate> points) {
        // Clear existing inclusions and add this one
        // Note: This is a simplified implementation of "setExterior" 
        // for backward compatibility.
        SimpleGeoPolygon poly = new SimpleGeoPolygon();
        poly.setExterior(points);
        addInclusion(poly);
    }

    /**
     * Returns the exterior ring of the first exclave.
     */
    public List<GeodeticCoordinate> getExterior() {
        List<Boundary<EarthCoordinate>> inclusions = getInclusions();
        if (inclusions.isEmpty()) return Collections.emptyList();
        Boundary<EarthCoordinate> first = inclusions.get(0);
        if (first instanceof SimpleGeoPolygon) {
            return ((SimpleGeoPolygon) first).getExterior();
        }
        return Collections.emptyList();
    }

    /**
     * Adds an interior ring (hole) to the first exclave.
     */
    public void addInterior(List<GeodeticCoordinate> points) {
        List<Boundary<EarthCoordinate>> inclusions = getInclusions();
        if (inclusions.isEmpty()) {
            addInclusion(new SimpleGeoPolygon());
            inclusions = getInclusions();
        }
        Boundary<EarthCoordinate> first = inclusions.get(0);
        if (first instanceof SimpleGeoPolygon) {
            ((SimpleGeoPolygon) first).addInterior(points);
        }
    }

    /**
     * Inner class representing a simple polygon (1 exterior + N interiors).
     */
    @Persistent
    public static class SimpleGeoPolygon implements Boundary<EarthCoordinate> {
        private static final long serialVersionUID = 1L;

        @Attribute
        private final List<GeodeticCoordinate> exterior = new ArrayList<>();
        @Attribute
        private final List<List<GeodeticCoordinate>> interiors = new ArrayList<>();

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

        @Override public int getDimension() { return 2; }

        @Override
        public boolean contains(EarthCoordinate point) {
            if (isEmpty() || point == null) return false;
            GeodeticCoordinate geoPt = point.toGeodetic();
            double x = geoPt.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            double y = geoPt.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
            
            boolean inside = isPointInRing(x, y, exterior);
            if (!inside) return false;
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
                boolean intersect = ((yi > y) != (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
                if (intersect) inside = !inside;
            }
            return inside;
        }

        @Override public boolean isEmpty() { return exterior.isEmpty(); }

        @Override
        public EarthCoordinate getCentroid() {
            if (isEmpty()) return null;
            double sumLat = 0, sumLon = 0, sumH = 0;
            for (GeodeticCoordinate c : exterior) {
                sumLat += c.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
                sumLon += c.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
                sumH += c.getHeight().to(Units.METER).getValue().doubleValue();
            }
            return new GeodeticCoordinate(sumLat / exterior.size(), sumLon / exterior.size(), sumH / exterior.size());
        }

        @Override
        public BoundingBox<EarthCoordinate> getBoundingBox() {
            if (isEmpty()) return null;
            double minLat = Double.POSITIVE_INFINITY, maxLat = Double.NEGATIVE_INFINITY;
            double minLon = Double.POSITIVE_INFINITY, maxLon = Double.NEGATIVE_INFINITY;
            for (GeodeticCoordinate p : exterior) {
                double lat = p.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
                double lon = p.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
                if (lat < minLat) minLat = lat; if (lat > maxLat) maxLat = lat;
                if (lon < minLon) minLon = lon; if (lon > maxLon) maxLon = lon;
            }
            return new GeoBoundingBox(new GeodeticCoordinate(minLat, minLon, 0), new GeodeticCoordinate(maxLat, maxLon, 0));
        }

        @Override
        public Real getMeasure() {
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

        @Override public boolean intersects(Boundary<EarthCoordinate> other) { return getBoundingBox().intersects(other.getBoundingBox()); }
        @Override public Boundary<EarthCoordinate> union(Boundary<EarthCoordinate> other) { return null; }
        @Override public Boundary<EarthCoordinate> intersection(Boundary<EarthCoordinate> other) { return null; }
        @Override public Boundary<EarthCoordinate> convexHull() { return null; }
        @Override public Boundary<EarthCoordinate> translate(EarthCoordinate offset) { return this; }
        @Override public Boundary<EarthCoordinate> scale(Real factor) { return this; }
    }
}

