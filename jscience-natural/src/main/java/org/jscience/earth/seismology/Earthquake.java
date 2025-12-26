package org.jscience.earth.seismology;

/**
 * Represents a seismic event.
 * Standard domain model for geology applications.
 */
public class Earthquake {
    private final double lat;
    private final double lon;
    private final double mag;
    private final double depth;

    public Earthquake(double lat, double lon, double mag, double depth) {
        this.lat = lat;
        this.lon = lon;
        this.mag = mag;
        this.depth = depth;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getMag() {
        return mag;
    }

    public double getDepth() {
        return depth;
    }
}
