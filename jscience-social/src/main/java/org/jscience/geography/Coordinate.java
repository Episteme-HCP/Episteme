/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.geography;

/**
 * Represents a geographic coordinate (latitude/longitude).
 * <p>
 * Uses WGS84 datum by default.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Coordinate {

    private final double latitude; // -90 to +90
    private final double longitude; // -180 to +180
    private final double altitude; // meters above sea level

    public Coordinate(double latitude, double longitude) {
        this(latitude, longitude, 0);
    }

    public Coordinate(double latitude, double longitude, double altitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    /**
     * Calculates distance to another coordinate using Haversine formula.
     *
     * @param other target coordinate
     * @return distance in meters
     */
    public double distanceTo(Coordinate other) {
        final double R = 6371000; // Earth radius in meters
        double lat1 = Math.toRadians(latitude);
        double lat2 = Math.toRadians(other.latitude);
        double dLat = Math.toRadians(other.latitude - latitude);
        double dLon = Math.toRadians(other.longitude - longitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Calculates bearing to another coordinate.
     *
     * @return bearing in degrees (0-360)
     */
    public double bearingTo(Coordinate other) {
        double lat1 = Math.toRadians(latitude);
        double lat2 = Math.toRadians(other.latitude);
        double dLon = Math.toRadians(other.longitude - longitude);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double bearing = Math.toDegrees(Math.atan2(y, x));
        return (bearing + 360) % 360;
    }

    /**
     * Returns coordinate in DMS format.
     */
    public String toDMS() {
        return String.format("%s, %s", formatDMS(latitude, 'N', 'S'), formatDMS(longitude, 'E', 'W'));
    }

    private String formatDMS(double coord, char pos, char neg) {
        char dir = coord >= 0 ? pos : neg;
        coord = Math.abs(coord);
        int d = (int) coord;
        double m = (coord - d) * 60;
        int mi = (int) m;
        double s = (m - mi) * 60;
        return String.format("%d°%d'%.2f\"%c", d, mi, s, dir);
    }

    @Override
    public String toString() {
        return String.format("(%.6f, %.6f)", latitude, longitude);
    }

    // Notable locations
    public static final Coordinate GREENWICH = new Coordinate(51.4772, 0.0);
    public static final Coordinate NORTH_POLE = new Coordinate(90, 0);
    public static final Coordinate SOUTH_POLE = new Coordinate(-90, 0);
    public static final Coordinate EQUATOR_PRIME = new Coordinate(0, 0);
}
