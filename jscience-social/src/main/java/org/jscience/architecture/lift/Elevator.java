package org.jscience.architecture.lift;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an elevator in a lift system.
 * 
 * Algorithm: Collective Control (SCAN / Elevator Algorithm)
 * Reference: Knuth, D. E. (1973). The Art of Computer Programming, Vol. 1.
 * 
 * This implementation handles multiple requests by moving in one direction 
 * until all stops in that direction are exhausted.
 */
public class Elevator {

    public enum State {
        IDLE, MOVING, OPENING, OPEN, CLOSING
    }

    public enum Direction {
        UP, DOWN, NONE
    }

    private final String id;
    private final int capacity;
    private int currentFloor;
    private State state = State.IDLE;
    private Direction direction = Direction.NONE;
    
    private final boolean[] upRequests;
    private final boolean[] downRequests;
    private final List<Passenger> passengers = new ArrayList<>();
    
    private int openTicks = 0;
    private static final int DOOR_HOLD_TIME = 3;

    public Elevator(String id, int capacity, int maxFloors) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.capacity = capacity;
        this.currentFloor = 0;
        this.upRequests = new boolean[maxFloors];
        this.downRequests = new boolean[maxFloors];
    }

    public String getId() { return id; }
    public int getCurrentFloor() { return currentFloor; }
    public State getState() { return state; }
    public Direction getDirection() { return direction; }

    /**
     * Internal request from a passenger inside the elevator to go to a specific floor.
     */
    public void pressFloorButton(int floor) {
        if (floor >= 0 && floor < upRequests.length) {
            if (floor > currentFloor) upRequests[floor] = true;
            else if (floor < currentFloor) downRequests[floor] = true;
            else upRequests[floor] = true; // At current floor
        }
    }

    /**
     * External hall call from a specific floor going a certain direction.
     */
    public void hallCall(int floor, Direction callDir) {
        if (floor >= 0 && floor < upRequests.length) {
            if (callDir == Direction.UP) upRequests[floor] = true;
            else if (callDir == Direction.DOWN) downRequests[floor] = true;
        }
    }

    public void tick() {
        for (Passenger p : passengers) p.tick();

        switch (state) {
            case IDLE:
                determineDirection();
                if (direction != Direction.NONE) {
                    state = State.MOVING;
                }
                break;

            case MOVING:
                moveOneFloor();
                if (shouldStopAtCurrent()) {
                    state = State.OPENING;
                }
                break;

            case OPENING:
                state = State.OPEN;
                openTicks = 0;
                clearRequestsAtCurrent();
                handleExitingPassengers();
                break;

            case OPEN:
                openTicks++;
                if (openTicks >= DOOR_HOLD_TIME) {
                    state = State.CLOSING;
                }
                break;

            case CLOSING:
                state = State.IDLE;
                break;
        }
    }

    private void determineDirection() {
        if (direction == Direction.NONE || direction == Direction.UP) {
            if (hasRequestsAbove()) direction = Direction.UP;
            else if (hasRequestsBelow()) direction = Direction.DOWN;
            else direction = Direction.NONE;
        } else {
            if (hasRequestsBelow()) direction = Direction.DOWN;
            else if (hasRequestsAbove()) direction = Direction.UP;
            else direction = Direction.NONE;
        }
    }

    private boolean hasRequestsAbove() {
        for (int i = currentFloor + 1; i < upRequests.length; i++) {
            if (upRequests[i] || downRequests[i]) return true;
        }
        return false;
    }

    private boolean hasRequestsBelow() {
        for (int i = 0; i < currentFloor; i++) {
            if (upRequests[i] || downRequests[i]) return true;
        }
        return false;
    }

    private boolean shouldStopAtCurrent() {
        if (direction == Direction.UP) {
            return upRequests[currentFloor] || !hasRequestsAbove();
        } else if (direction == Direction.DOWN) {
            return downRequests[currentFloor] || !hasRequestsBelow();
        }
        return false;
    }

    private void clearRequestsAtCurrent() {
        upRequests[currentFloor] = false;
        downRequests[currentFloor] = false;
    }

    private void moveOneFloor() {
        if (direction == Direction.UP) currentFloor++;
        else if (direction == Direction.DOWN) currentFloor--;
    }

    private void handleExitingPassengers() {
        passengers.removeIf(p -> {
            if (p.getDestinationFloor() == currentFloor) {
                p.setState(Passenger.State.FINISHED);
                return true;
            }
            return false;
        });
    }

    public void addPassenger(Passenger p) {
        if (passengers.size() < capacity) {
            passengers.add(p);
            p.setElevator(this);
            p.setState(Passenger.State.TRAVELLING);
            pressFloorButton(p.getDestinationFloor());
        }
    }

    public int getPassengerCount() { return passengers.size(); }
    public int getCapacity() { return capacity; }
}


