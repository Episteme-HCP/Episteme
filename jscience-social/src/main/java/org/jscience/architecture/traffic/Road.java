package org.jscience.architecture.traffic;

import org.jscience.geography.Coordinate;
import org.jscience.geography.GeoPath;

import java.util.List;

/**
 * Represents a road for vehicles.
 */
public class Road extends GeoPath {

    private final String name;
    private double speedLimit; // in km/h or m/s? Let's use m/s internally.
    private int lanes;

    public Road(String name, List<Coordinate> coordinates, double speedLimit, int lanes) {
        super(coordinates);
        this.name = name;
        this.speedLimit = speedLimit;
        this.lanes = lanes;
    }

    public String getName() {
        return name;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public int getLanes() {
        return lanes;
    }
}
