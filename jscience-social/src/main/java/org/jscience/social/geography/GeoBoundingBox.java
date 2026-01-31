/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.social.geography;

import org.jscience.natural.earth.coordinates.EarthCoordinate;
import org.jscience.natural.earth.coordinates.GeodeticCoordinate;
import org.jscience.core.mathematics.geometry.boundaries.BoundingBox;
import org.jscience.core.measure.Units;

import java.util.Objects;

/**
 * Geographic bounding box implementation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeoBoundingBox implements BoundingBox<EarthCoordinate> {

    private final GeodeticCoordinate min;
    private final GeodeticCoordinate max;

    public GeoBoundingBox(GeodeticCoordinate min, GeodeticCoordinate max) {
        this.min = Objects.requireNonNull(min);
        this.max = Objects.requireNonNull(max);
    }

    @Override
    public EarthCoordinate getMin() { return min; }

    @Override
    public EarthCoordinate getMax() { return max; }

    @Override
    public boolean contains(EarthCoordinate point) {
        if (!(point instanceof GeodeticCoordinate)) return false;
        GeodeticCoordinate gp = (GeodeticCoordinate) point;
        double lat = gp.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double lon = gp.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        
        double minLat = min.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double minLon = min.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double maxLat = max.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double maxLon = max.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        
        return lat >= minLat && lat <= maxLat && lon >= minLon && lon <= maxLon;
    }

    @Override
    public boolean intersects(BoundingBox<EarthCoordinate> other) {
        if (other == null) return false;
        
        double minLat = min.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double minLon = min.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double maxLat = max.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double maxLon = max.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        
        GeodeticCoordinate oMin = (GeodeticCoordinate) other.getMin();
        GeodeticCoordinate oMax = (GeodeticCoordinate) other.getMax();
        
        double otMinLat = oMin.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double otMinLon = oMin.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double otMaxLat = oMax.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double otMaxLon = oMax.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        
        return minLat <= otMaxLat && maxLat >= otMinLat &&
               minLon <= otMaxLon && maxLon >= otMinLon;
    }

    @Override
    public BoundingBox<EarthCoordinate> include(EarthCoordinate point) {
        if (!(point instanceof GeodeticCoordinate)) return this;
        GeodeticCoordinate gp = (GeodeticCoordinate) point;
        
        double lat = gp.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        double lon = gp.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue();
        
        double minLat = Math.min(lat, min.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue());
        double minLon = Math.min(lon, min.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue());
        double maxLat = Math.max(lat, max.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue());
        double maxLon = Math.max(lon, max.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue());
        
        return new GeoBoundingBox(new GeodeticCoordinate(minLat, minLon, 0), new GeodeticCoordinate(maxLat, maxLon, 0));
    }

    @Override
    public BoundingBox<EarthCoordinate> merge(BoundingBox<EarthCoordinate> other) {
        if (other == null) return this;
        GeoBoundingBox expanded = (GeoBoundingBox) include(other.getMin());
        return expanded.include(other.getMax());
    }
}

