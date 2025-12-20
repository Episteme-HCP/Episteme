package org.jscience.geography;

/**
 * Handles Geographic Information System (GIS) profiles and projections.
 */
public class GISProfile {

    public enum Projection {
        MERCATOR, EQUIRECTANGULAR, ORTHOGRAPHIC
    }

    private final Projection projection;

    public GISProfile(Projection projection) {
        this.projection = projection;
    }

    /**
     * Projects a 3D coordinate (lat/lon) to a 2D map coordinate (x/y).
     * Simple implementation for demonstration.
     */
    public double[] project(Coordinate coord) {
        double lat = Math.toRadians(coord.getLatitude());
        double lon = Math.toRadians(coord.getLongitude());

        switch (projection) {
            case MERCATOR:
                double x = lon;
                double y = Math.log(Math.tan(Math.PI / 4 + lat / 2));
                return new double[] { x, y };
            case EQUIRECTANGULAR:
                return new double[] { lon, lat };
            default:
                throw new UnsupportedOperationException("Projection not implemented");
        }
    }

    /**
     * Calculates great-circle distance (Haversine) in meters.
     */
    public static double calculateDistance(Coordinate c1, Coordinate c2) {
        final int R = 6371000; // Earth radius in meters
        double lat1 = Math.toRadians(c1.getLatitude());
        double lat2 = Math.toRadians(c2.getLatitude());
        double dLat = Math.toRadians(c2.getLatitude() - c1.getLatitude());
        double dLon = Math.toRadians(c2.getLongitude() - c1.getLongitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
