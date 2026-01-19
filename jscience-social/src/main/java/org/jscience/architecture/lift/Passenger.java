package org.jscience.architecture.lift;

import java.util.UUID;

/**
 * Represents a passenger in an elevator system.
 */
public class Passenger {

    public enum State {
        WAITING, GETTING_IN, TRAVELLING, GETTING_OUT, FINISHED
    }

    private final String id;
    private final int sourceFloor;
    private final int destinationFloor;
    private State state;
    private int waitTicks;
    private int travelTicks;
    private Elevator currentElevator;

    public Passenger(int sourceFloor, int destinationFloor) {
        if (sourceFloor == destinationFloor) {
            throw new IllegalArgumentException("Source and destination floors must be different");
        }
        this.id = UUID.randomUUID().toString();
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.state = State.WAITING;
        this.waitTicks = 0;
        this.travelTicks = 0;
    }

    public String getId() {
        return id;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public State getState() {
        return state;
    }

    public int getWaitTicks() {
        return waitTicks;
    }

    public int getTravelTicks() {
        return travelTicks;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void tick() {
        if (state == State.WAITING) {
            waitTicks++;
        } else if (state == State.TRAVELLING) {
            travelTicks++;
        }
    }

    public Elevator getCurrentElevator() {
        return currentElevator;
    }

    public void setElevator(Elevator elevator) {
        this.currentElevator = elevator;
    }

    @Override
    public String toString() {
        return String.format("Passenger[%s: %d -> %d, %s]", id.substring(0, 8), sourceFloor, destinationFloor, state);
    }
}
