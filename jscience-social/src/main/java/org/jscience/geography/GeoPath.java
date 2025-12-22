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
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a path or track consisting of a sequence of coordinates.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeoPath {

    private final List<Coordinate> coordinates;

    public GeoPath(List<Coordinate> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            throw new IllegalArgumentException("Path must have at least one coordinate");
        }
        this.coordinates = new ArrayList<>(coordinates);
    }

    public List<Coordinate> getCoordinates() {
        return Collections.unmodifiableList(coordinates);
    }

    public Coordinate getStart() {
        return coordinates.get(0);
    }

    public Coordinate getEnd() {
        return coordinates.get(coordinates.size() - 1);
    }

    public boolean isClosed() {
        if (coordinates.size() < 2)
            return false;
        Coordinate start = getStart();
        Coordinate end = getEnd();
        Real latDiff = start.getLatitude().subtract(end.getLatitude()).abs();
        Real lonDiff = start.getLongitude().subtract(end.getLongitude()).abs();
        Real epsilon = Real.of(1e-9);
        return latDiff.compareTo(epsilon) < 0 && lonDiff.compareTo(epsilon) < 0;
    }

    public Real getLength() {
        Real length = Real.ZERO;
        for (int i = 0; i < coordinates.size() - 1; i++) {
            length = length.add(coordinates.get(i).distanceTo(coordinates.get(i + 1)));
        }
        return length;
    }

    public GeoPath reverse() {
        List<Coordinate> reversed = new ArrayList<>(coordinates);
        Collections.reverse(reversed);
        return new GeoPath(reversed);
    }
}
