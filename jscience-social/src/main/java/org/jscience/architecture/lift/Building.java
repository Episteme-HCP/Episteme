package org.jscience.architecture.lift;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a building equipped with elevators.
 */
public class Building {

    private final String name;
    private final int minFloor;
    private final int maxFloor;
    private final List<Elevator> elevators;

    public Building(String name, int minFloor, int maxFloor) {
        this.name = name;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.elevators = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getMinFloor() {
        return minFloor;
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    public void addElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    public List<Elevator> getElevators() {
        return Collections.unmodifiableList(elevators);
    }

    public void tick() {
        for (Elevator elevator : elevators) {
            elevator.tick();
        }
    }
}
