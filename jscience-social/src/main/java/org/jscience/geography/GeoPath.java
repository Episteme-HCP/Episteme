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

package org.jscience.geography;

import org.jscience.earth.coordinates.GeodeticCoordinate;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.util.Named;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a sequence of geodetic coordinates forming a path, route, or boundary.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class GeoPath implements Named, Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute
    private String name;

    @Attribute
    private final List<GeodeticCoordinate> points = new ArrayList<>();

    public GeoPath() {
    }

    public GeoPath(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPoint(GeodeticCoordinate point) {
        points.add(Objects.requireNonNull(point));
    }

    public List<GeodeticCoordinate> getPoints() {
        return Collections.unmodifiableList(points);
    }

    public int size() {
        return points.size();
    }

    public GeodeticCoordinate getStart() {
        return points.isEmpty() ? null : points.get(0);
    }

    public GeodeticCoordinate getEnd() {
        return points.isEmpty() ? null : points.get(points.size() - 1);
    }

    /**
     * Calculates the total length of the path by summing segment distances.
     * 
     * @return total length quantity
     */
    public Quantity<Length> getLength() {
        double totalMeters = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            Quantity<Length> dist = points.get(i).distanceTo(points.get(i + 1));
            if (dist != null) {
                totalMeters += dist.to(Units.METER).getValue().doubleValue();
            }
        }
        return Quantities.create(totalMeters, Units.METER);
    }

    /**
     * Returns a reversed copy of this path.
     */
    public GeoPath reverse() {
        GeoPath reversed = new GeoPath(name != null ? name + " (reversed)" : null);
        List<GeodeticCoordinate> revPoints = new ArrayList<>(points);
        Collections.reverse(revPoints);
        reversed.points.addAll(revPoints);
        return reversed;
    }

    @Override
    public String toString() {
        return String.format("Path: %s (%d points, %s)", name, points.size(), getLength());
    }
}
