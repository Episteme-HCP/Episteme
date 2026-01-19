package org.jscience.earth;

import org.jscience.mathematics.geometry.SpatialDataSet;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Specialized SpatialDataSet for Earth-based coordinates (Latitude, Longitude).
 * Adds support for projection handling and geographical attributes.
 */
public final class GeoSpatialDataSet extends SpatialDataSet {

    @Override
    public String getModelType() { return "GEOSPATIAL_EARTH_MAP"; }

    public record GeoPoint(double latitude, double longitude) {}

    public void addGeoLocation(String id, String label, double lat, double lon) {
        // Use Latitude and Longitude as Y and X
        addLocation(id, label, Real.of(lon), Real.of(lat));
    }

    public void addGeoFlow(String fromId, String toId, Real intensity) {
        addFlow(fromId, toId, intensity);
    }
}
