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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a building equipped with elevators.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @deprecated Use {@link LiftBuilding} instead. This class will be removed in a
 *             future version.
 */
@Deprecated
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
