package org.episteme.social.geography;

import org.episteme.natural.earth.coordinates.EarthCoordinate;
import org.episteme.natural.earth.coordinates.GeodeticCoordinate;
import org.episteme.core.mathematics.geometry.boundaries.Boundary;
import org.episteme.core.mathematics.geometry.boundaries.BoundingBox;
import org.episteme.core.mathematics.geometry.boundaries.CompositeBoundary;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Length;
import org.episteme.core.util.Named;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a geographical path, which can consist of multiple disjoint segments 
 * (MultiLineString).
 * Refactored to leverage {@link CompositeBoundary}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class GeoPath extends CompositeBoundary<EarthCoordinate> implements Named {

    private static final long serialVersionUID = 2L;

    @Attribute
    private String name;

    public GeoPath() {
        super();
    }

    public GeoPath(String name) {
        this();
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Compatibility method: adds a point to the first segment.
     */
    public void addPoint(GeodeticCoordinate point) {
        List<Boundary<EarthCoordinate>> inclusions = getInclusions();
        if (inclusions.isEmpty()) {
            addInclusion(new SimpleGeoPath());
            inclusions = getInclusions();
        }
        Boundary<EarthCoordinate> first = inclusions.get(0);
        if (first instanceof SimpleGeoPath) {
            ((SimpleGeoPath) first).addPoint(point);
        }
    }

    /**
     * Compatibility method: returns points of the first segment.
     */
    public List<GeodeticCoordinate> getPoints() {
        List<Boundary<EarthCoordinate>> inclusions = getInclusions();
        if (inclusions.isEmpty()) return Collections.emptyList();
        Boundary<EarthCoordinate> first = inclusions.get(0);
        if (first instanceof SimpleGeoPath) {
            return ((SimpleGeoPath) first).getPoints();
        }
        return Collections.emptyList();
    }

    /**
     * Compatibility method: returns the first point of the first segment.
     */
    public GeodeticCoordinate getStart() {
        List<GeodeticCoordinate> pts = getPoints();
        return pts.isEmpty() ? null : pts.get(0);
    }

    /**
     * Compatibility method: returns the last point of the first segment.
     */
    public GeodeticCoordinate getEnd() {
        List<GeodeticCoordinate> pts = getPoints();
        return pts.isEmpty() ? null : pts.get(pts.size() - 1);
    }

    /**
     * Compatibility method: returns the number of points in the first segment.
     */
    public int size() {
        return getPoints().size();
    }

    /**
     * Calculates the total length of all segments.
     */
    public Quantity<Length> getTotalLength() {
        double total = 0;
        for (Boundary<EarthCoordinate> b : getInclusions()) {
            if (b instanceof SimpleGeoPath) {
                total += ((SimpleGeoPath) b).getLength().to(Units.METER).getValue().doubleValue();
            }
        }
        return Quantities.create(total, Units.METER);
    }

    @Override
    public String toString() {
        return String.format("GeoPath: %s (%d segments, total length %s)", name, getInclusions().size(), getTotalLength());
    }

    /**
     * Inner class representing a single continuous path segment.
     */
    @Persistent
    public static class SimpleGeoPath implements Boundary<EarthCoordinate> {
        private static final long serialVersionUID = 1L;

        @Attribute
        private final List<GeodeticCoordinate> points = new ArrayList<>();

        public void addPoint(GeodeticCoordinate point) {
            points.add(Objects.requireNonNull(point));
        }

        public List<GeodeticCoordinate> getPoints() {
            return Collections.unmodifiableList(points);
        }

        public Quantity<Length> getLength() {
            double total = 0.0;
            for (int i = 0; i < points.size() - 1; i++) {
                Quantity<Length> dist = points.get(i).distanceTo(points.get(i + 1));
                if (dist != null) total += dist.to(Units.METER).getValue().doubleValue();
            }
            return Quantities.create(total, Units.METER);
        }

        @Override public int getDimension() { return 1; }

        @Override
        public boolean contains(EarthCoordinate point) {
            if (point == null) return false;
            for (GeodeticCoordinate p : points) {
                if (p.equals(point)) return true;
            }
            return false;
        }

        @Override public boolean isEmpty() { return points.isEmpty(); }

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
            double minLat = Double.POSITIVE_INFINITY, maxLat = Double.NEGATIVE_INFINITY;
            double minLon = Double.POSITIVE_INFINITY, maxLon = Double.NEGATIVE_INFINITY;
            for (GeodeticCoordinate p : points) {
                double lat = p.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
                double lon = p.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
                if (lat < minLat) minLat = lat; if (lat > maxLat) maxLat = lat;
                if (lon < minLon) minLon = lon; if (lon > maxLon) maxLon = lon;
            }
            return new GeoBoundingBox(new GeodeticCoordinate(minLat, minLon, 0), new GeodeticCoordinate(maxLat, maxLon, 0));
        }

        @Override public Real getMeasure() { return getLength().to(Units.METER).getValue(); }
        @Override public Real getBoundaryMeasure() { return Real.ZERO; }
        @Override public boolean intersects(Boundary<EarthCoordinate> other) { return getBoundingBox().intersects(other.getBoundingBox()); }
        @Override public Boundary<EarthCoordinate> union(Boundary<EarthCoordinate> other) { return null; }
        @Override public Boundary<EarthCoordinate> intersection(Boundary<EarthCoordinate> other) { return null; }
        @Override public Boundary<EarthCoordinate> convexHull() { return null; }
        @Override public Boundary<EarthCoordinate> translate(EarthCoordinate offset) { return this; }
        @Override public Boundary<EarthCoordinate> scale(Real factor) { return this; }
    }
}



