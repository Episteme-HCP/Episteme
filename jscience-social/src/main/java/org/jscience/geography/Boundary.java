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

import java.util.*;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;

/**
 * Represents a geographic boundary/border.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Boundary {

    public enum Type {
        NATIONAL, STATE, PROVINCIAL, COUNTY, MUNICIPAL,
        NATURAL, MARITIME, TREATY_DEFINED
    }

    private final String name;
    private Type type;
    private final List<Coordinate> points = new ArrayList<>();
    private double lengthMeters;
    private String entity1;
    private String entity2;

    public Boundary(String name, Type type) {
        this.name = name;
        this.type = type;
        this.lengthMeters = 0.0;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    /**
     * Returns the length in kilometers.
     */
    public double getLengthKm() {
        return lengthMeters / 1000.0;
    }

    /**
     * Returns the length as a Length quantity.
     */
    public Quantity<Length> getLength() {
        return Quantities.create(lengthMeters, Units.METER);
    }

    public String getEntity1() {
        return entity1;
    }

    public String getEntity2() {
        return entity2;
    }

    public List<Coordinate> getPoints() {
        return Collections.unmodifiableList(points);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setLengthKm(double lengthKm) {
        this.lengthMeters = lengthKm * 1000.0;
    }

    public void setLengthMeters(double lengthMeters) {
        this.lengthMeters = lengthMeters;
    }

    public void setEntities(String entity1, String entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    public void addPoint(Coordinate coord) {
        points.add(coord);
    }

    /**
     * Calculates the total length of the boundary from its points.
     * 
     * @return length as a Length quantity
     */
    public Quantity<Length> calculateLength() {
        if (points.size() < 2)
            return Quantities.create(0, Units.METER);
        double total = 0.0;
        for (int i = 1; i < points.size(); i++) {
            total += points.get(i - 1).distanceTo(points.get(i))
                    .to(Units.METER).getValue().doubleValue();
        }
        return Quantities.create(total, Units.METER);
    }

    @Override
    public String toString() {
        return String.format("Boundary '%s' (%s): %.1f km", name, type, getLengthKm());
    }

    public static Boundary usCanadaBorder() {
        Boundary b = new Boundary("US-Canada Border", Type.NATIONAL);
        b.setEntities("United States", "Canada");
        b.setLengthKm(8891); // kilometers
        return b;
    }
}


