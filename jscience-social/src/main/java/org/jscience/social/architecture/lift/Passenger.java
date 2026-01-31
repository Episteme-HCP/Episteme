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
import java.util.UUID;

/**
 * Represents an individual agent utilizing a vertical transportation system. 
 * Tracks waiting time, transit time, and movement through various logical 
 * states of a trip.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Passenger implements Serializable {

    private static final long serialVersionUID = 2L;

    /** Movement states of the passenger agent. */
    public enum State {
        /** Passenger is queued at the source floor. */
        WAITING, 
        /** In the process of boarding a cab. */
        GETTING_IN, 
        /** Currently inside a cab in motion. */
        TRAVELLING, 
        /** In the process of exiting a cab at destination. */
        GETTING_OUT, 
        /** Trip completed. */
        FINISHED
    }

    private final String id;
    private final int sourceFloor;
    private final int destinationFloor;
    private State state;
    private int waitTicks;
    private int travelTicks;
    private Elevator currentElevator;

    /**
     * Initializes a new passenger with a specific travel intent.
     * 
     * @param sourceFloor the index of the floor where the passenger starts
     * @param destinationFloor the index of the target floor
     * @throws IllegalArgumentException if source and destination are identical
     */
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

    /** @return the number of simulation ticks spent waiting outside a cab */
    public int getWaitTicks() {
        return waitTicks;
    }

    /** @return the number of simulation ticks spent inside a cab */
    public int getTravelTicks() {
        return travelTicks;
    }

    public void setState(State state) {
        this.state = state;
    }

    /**
     * Advances the passenger's internal timers based on current state.
     */
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
        return String.format("Passenger[%s: %d -> %d, %s]", 
            id.substring(0, Math.min(id.length(), 8)), sourceFloor, destinationFloor, state);
    }
}

