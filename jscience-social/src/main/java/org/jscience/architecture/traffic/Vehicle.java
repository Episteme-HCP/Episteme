package org.jscience.architecture.traffic;

import org.jscience.geography.Coordinate;

/**
 * Represents a vehicle in traffic.
 */
public class Vehicle {

    private final String id;
    private Coordinate position;
    private double speed; // m/s
    private Road currentRoad; // The road segment currently travelling

    public Vehicle(String id) {
        this.id = id;
        this.speed = 0;
    }

    public String getId() {
        return id;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    public void enterRoad(Road road) {
        this.currentRoad = road;
        // Ideally snap to road start
        if (road != null && !road.getCoordinates().isEmpty()) {
            this.position = road.getStart();
        }
    }

    // Simple update tick
    public void move(double seconds) {
        if (currentRoad != null && speed > 0) {
            // Very simple movement along line segments would go here.
            // For V1 shell, we just hold data.
        }
    }
}
