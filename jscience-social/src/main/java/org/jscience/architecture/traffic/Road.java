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
package org.jscience.architecture.traffic;

import org.jscience.geography.Coordinate;
import org.jscience.geography.GeoPath;

import java.util.List;

/**
 * Represents a road for vehicles. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class Road extends GeoPath {

    private final String name;
    private double speedLimit; // in km/h or m/s? Let's use m/s internally.
    private int lanes;

    public Road(String name, List<Coordinate> coordinates, double speedLimit, int lanes) {
        super(coordinates);
        this.name = name;
        this.speedLimit = speedLimit;
        this.lanes = lanes;
    }

    public String getName() {
        return name;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public int getLanes() {
        return lanes;
    }
}
