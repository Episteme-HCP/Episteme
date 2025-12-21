/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.architecture.lift;

/**
 * Represents an elevator car. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
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
