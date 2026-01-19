package org.jscience.architecture.lift;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a building equipped with elevators for lift simulation.
 * 
 * Dispatcher Algorithm: Estimated Time of Arrival (ETA)
 * Reference: Barney, G. C. (2003). Elevator Traffic Handbook.
 */
public class LiftBuilding {

    private final String name;
    private final int minFloor;
    private final int maxFloor;
    private final List<Elevator> elevators;
    private final List<List<Passenger>> floorQueues;

    public LiftBuilding(String name, int minFloor, int maxFloor) {
        this.name = name;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.elevators = new ArrayList<>();
        
        int floorCount = maxFloor - minFloor + 1;
        this.floorQueues = new ArrayList<>(floorCount);
        for (int i = 0; i < floorCount; i++) {
            floorQueues.add(new ArrayList<>());
        }
    }

    public void addElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    /**
     * External hall call from a specific floor.
     * Uses ETA algorithm to dispatch the most suitable elevator.
     */
    public void hallCall(int floor, Elevator.Direction direction) {
        Elevator best = null;
        int minEta = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            int eta = calculateETA(e, floor, direction);
            if (eta < minEta) {
                minEta = eta;
                best = e;
            }
        }

        if (best != null) {
            best.hallCall(floor, direction);
        }
    }

    private int calculateETA(Elevator e, int targetFloor, Elevator.Direction callDir) {
        int dist = Math.abs(e.getCurrentFloor() - targetFloor);
        
        // Penalize if moving away
        if (e.getDirection() == Elevator.Direction.UP && targetFloor < e.getCurrentFloor()) dist += 10;
        if (e.getDirection() == Elevator.Direction.DOWN && targetFloor > e.getCurrentFloor()) dist += 10;
        
        // Penalize if moving in wrong direction
        if (e.getDirection() != Elevator.Direction.NONE && e.getDirection() != callDir) dist += 5;
        
        return dist;
    }

    public void addPassenger(Passenger p) {
        int floorIdx = p.getSourceFloor() - minFloor;
        if (floorIdx >= 0 && floorIdx < floorQueues.size()) {
            floorQueues.get(floorIdx).add(p);
            hallCall(p.getSourceFloor(), p.getDestinationFloor() > p.getSourceFloor() ? 
                    Elevator.Direction.UP : Elevator.Direction.DOWN);
        }
    }

    public void tick() {
        for (Elevator elevator : elevators) {
            elevator.tick();
            
            // Handle passengers entering the elevator at the current floor
            if (elevator.getState() == Elevator.State.OPEN) {
                int floorIdx = elevator.getCurrentFloor() - minFloor;
                if (floorIdx >= 0 && floorIdx < floorQueues.size()) {
                    List<Passenger> queue = floorQueues.get(floorIdx);
                    queue.removeIf(p -> {
                        if (elevator.getPassengerCount() < elevator.getCapacity()) {
                            elevator.addPassenger(p);
                            return true;
                        }
                        return false;
                    });
                }
            }
        }
    }

    public String getName() { return name; }
    public int getMinFloor() { return minFloor; }
    public int getMaxFloor() { return maxFloor; }
    public List<Elevator> getElevators() { return Collections.unmodifiableList(elevators); }
}
