package org.jscience.architecture.lift;

/**
 * Represents an elevator car.
 */
public class Elevator {

    public enum State {
        WAITING,
        MOVING_UP,
        MOVING_DOWN,
        OPENING,
        CLOSING
    }

    private final String id;
    private int currentFloor;
    private int targetFloor;
    private State state;
    private final int capacity; // Max persons
    private int passengerCount;

    // Physical properties could be here (speed, etc.) defined in legacy
    // KinematicModel
    // For V1 we accept basic state transition logic.

    public Elevator(String id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.currentFloor = 0; // Ground floor default
        this.state = State.WAITING;
        this.passengerCount = 0;
    }

    public String getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public State getState() {
        return state;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void addPassenger() {
        if (passengerCount < capacity) {
            passengerCount++;
        }
    }

    public void removePassenger() {
        if (passengerCount > 0) {
            passengerCount--;
        }
    }

    public void call(int floor) {
        if (state == State.WAITING) {
            targetFloor = floor;
            if (targetFloor > currentFloor) {
                state = State.MOVING_UP;
            } else if (targetFloor < currentFloor) {
                state = State.MOVING_DOWN;
            } else {
                state = State.OPENING;
            }
        }
    }

    /**
     * Advances simulation one tick/step
     */
    public void tick() {
        switch (state) {
            case MOVING_UP:
                currentFloor++; // Simplified movement
                if (currentFloor == targetFloor) {
                    state = State.OPENING;
                }
                break;
            case MOVING_DOWN:
                currentFloor--;
                if (currentFloor == targetFloor) {
                    state = State.OPENING;
                }
                break;
            case OPENING:
                // Simulate time to open
                state = State.CLOSING; // Immediate close for V1 simple test
                break;
            case CLOSING:
                state = State.WAITING;
                break;
            case WAITING:
                break;
        }
    }
}
