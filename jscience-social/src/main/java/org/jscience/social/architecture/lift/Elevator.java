/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.social.architecture.lift;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an autonomous elevator unit within a multi-floor transportation system.
 * It implements a collective control algorithm (also known as the SCAN or Elevator
 * algorithm) to optimize throughput and energy usage.
 *
 * <p>Reference: Knuth, D. E. (1973). The Art of Computer Programming, Vol. 1. 
 * Fundamental Algorithms.</p>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Elevator implements Serializable {

    private static final long serialVersionUID = 2L;

    /** Functional states of the elevator cabin. */
    public enum State {
        /** Cabin is stationary and unoccupied/unrequested. */
        IDLE, 
        /** Cabin is progressing between floors. */
        MOVING, 
        /** Door mechanism is engaging. */
        OPENING, 
        /** Door is fully retracted. */
        OPEN, 
        /** Door mechanism is disengaging. */
        CLOSING
    }

    /** Directional intent of the elevator motion. */
    public enum Direction {
        /** Moving toward higher floor indices. */
        UP, 
        /** Moving toward lower floor indices. */
        DOWN, 
        /** Stationary without pending directional intent. */
        NONE
    }

    private final String id;
    private final int capacity;
    private int currentFloor;
    private State state = State.IDLE;
    private Direction direction = Direction.NONE;
    
    // Request registries for floor buttons and hall lanterns
    private final boolean[] upRequests;
    private final boolean[] downRequests;
    private final List<Passenger> passengers = new ArrayList<>();
    
    private int openTicks = 0;
    private static final int DOOR_HOLD_TIME = 3;

    /**
     * Initializes a new elevator cabin with a fixed capacity and operational range.
     * 
     * @param id unique identifier for the cabin
     * @param capacity maximum number of passengers allowed
     * @param maxFloors the total number of floors served by this lift
     */
    public Elevator(String id, int capacity, int maxFloors) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.capacity = capacity;
        this.currentFloor = 0;
        int size = Math.max(1, maxFloors);
        this.upRequests = new boolean[size];
        this.downRequests = new boolean[size];
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

    public Direction getDirection() {
        return direction;
    }

    /**
     * Registers a request from the cabin's internal operating panel.
     * 
     * @param floor the target destination floor index
     */
    public void pressFloorButton(int floor) {
        if (floor >= 0 && floor < upRequests.length) {
            if (floor > currentFloor) {
                upRequests[floor] = true;
            } else if (floor < currentFloor) {
                downRequests[floor] = true;
            } else {
                upRequests[floor] = true; 
            }
        }
    }

    /**
     * Registers an external hall call request from a specific floor.
     * 
     * @param floor the floor where the call originated
     * @param callDir the intended travel direction (UP or DOWN)
     */
    public void hallCall(int floor, Direction callDir) {
        if (floor >= 0 && floor < upRequests.length) {
            if (callDir == Direction.UP) {
                upRequests[floor] = true;
            } else if (callDir == Direction.DOWN) {
                downRequests[floor] = true;
            }
        }
    }

    /**
     * Advances the elevator simulation by one discrete time step.
     * Processes motion physics, door timing, and logic transitions.
     */
    public void tick() {
        for (Passenger p : passengers) {
            p.tick();
        }

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
            if (hasRequestsAbove()) {
                direction = Direction.UP;
            } else if (hasRequestsBelow()) {
                direction = Direction.DOWN;
            } else {
                direction = Direction.NONE;
            }
        } else {
            if (hasRequestsBelow()) {
                direction = Direction.DOWN;
            } else if (hasRequestsAbove()) {
                direction = Direction.UP;
            } else {
                direction = Direction.NONE;
            }
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
        if (currentFloor >= 0 && currentFloor < upRequests.length) {
            upRequests[currentFloor] = false;
            downRequests[currentFloor] = false;
        }
    }

    private void moveOneFloor() {
        if (direction == Direction.UP && currentFloor < upRequests.length - 1) {
            currentFloor++;
        } else if (direction == Direction.DOWN && currentFloor > 0) {
            currentFloor--;
        }
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

    /**
     * Boards a passenger into the cabin if capacity permits.
     * 
     * @param p the passenger attempting to board
     */
    public void addPassenger(Passenger p) {
        if (passengers.size() < capacity && p != null) {
            passengers.add(p);
            p.setElevator(this);
            p.setState(Passenger.State.TRAVELLING);
            pressFloorButton(p.getDestinationFloor());
        }
    }

    /** @return the number of passengers currently in the cabin */
    public int getPassengerCount() {
        return passengers.size();
    }

    /** @return the maximum load capacity of the cabin */
    public int getCapacity() {
        return capacity;
    }
}

