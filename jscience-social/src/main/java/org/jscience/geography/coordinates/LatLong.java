package org.jscience.geography.coordinates;

/**
 * Lat/Lon Coordinates.
 * Simple 2D implementation (WGS84 implied).
 */
public class LatLong implements Coordinates {

    private final double latitude;
    private final double longitude;

    public LatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public double get(int dimension) {
        if (dimension == 0) return latitude;
        if (dimension == 1) return longitude;
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public String toString() {
        return String.format("Lat: %.6f, Lon: %.6f", latitude, longitude);
    }
}
