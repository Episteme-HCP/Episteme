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
package org.jscience.geography;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;

/**
 * Represents a sequence of geographic coordinates forming a path or route.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeoPath {

    private final List<Coordinate> coordinates = new ArrayList<>();
    private String name;

    public GeoPath() {
    }

    public GeoPath(List<Coordinate> coordinates) {
        this.coordinates.addAll(coordinates);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPoint(Coordinate coord) {
        coordinates.add(coord);
    }

    public List<Coordinate> getCoordinates() {
        return Collections.unmodifiableList(coordinates);
    }

    public int size() {
        return coordinates.size();
    }

    public Coordinate getStart() {
        return coordinates.isEmpty() ? null : coordinates.get(0);
    }

    public Coordinate getEnd() {
        return coordinates.isEmpty() ? null : coordinates.get(coordinates.size() - 1);
    }

    /**
     * Checks if the path is closed (start and end are at the same location).
     */
    public boolean isClosed() {
        if (coordinates.size() < 2)
            return false;
        Coordinate start = getStart();
        Coordinate end = getEnd();
        double latDiff = Math.abs(start.getLatitudeDegrees() - end.getLatitudeDegrees());
        double lonDiff = Math.abs(start.getLongitudeDegrees() - end.getLongitudeDegrees());
        return latDiff < 1e-9 && lonDiff < 1e-9;
    }

    /**
     * Calculates the total length of the path.
     * 
     * @return total length as a Length quantity
     */
    public Quantity<Length> getLength() {
        double lengthMeters = 0.0;
        for (int i = 0; i < coordinates.size() - 1; i++) {
            lengthMeters += coordinates.get(i).distanceTo(coordinates.get(i + 1))
                    .to(Units.METER).getValue().doubleValue();
        }
        return org.jscience.measure.Quantities.create(lengthMeters, Units.METER);
    }

    /**
     * Returns a reversed copy of this path.
     */
    public GeoPath reverse() {
        List<Coordinate> reversed = new ArrayList<>(coordinates);
        Collections.reverse(reversed);
        return new GeoPath(reversed);
    }
}
