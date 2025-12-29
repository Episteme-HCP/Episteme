/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.architecture.traffic;

import org.jscience.geography.Coordinate;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a vehicle in traffic.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Vehicle {

    private final String id;
    private Coordinate position;
    private Real speed; // m/s
    private Road currentRoad;

    public Vehicle(String id) {
        this.id = id;
        this.speed = Real.ZERO;
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

    public Real getSpeed() {
        return speed;
    }

    public void setSpeed(Real speed) {
        this.speed = speed;
    }

    public void setSpeed(double speed) {
        this.speed = Real.of(speed);
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    public void enterRoad(Road road) {
        this.currentRoad = road;
        if (road != null && !road.getCoordinates().isEmpty()) {
            this.position = road.getStart();
        }
    }

    public void move(double seconds) {
        if (currentRoad != null && speed.doubleValue() > 0) {
            // Very simple movement along line segments would go here.
        }
    }
}
