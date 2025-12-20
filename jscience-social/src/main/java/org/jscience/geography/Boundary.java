/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.geography;

import java.util.*;

/**
 * Represents a geographic boundary/border.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Boundary {

    public enum Type {
        NATIONAL, STATE, PROVINCIAL, COUNTY, MUNICIPAL,
        NATURAL, MARITIME, TREATY_DEFINED
    }

    private final String name;
    private Type type;
    private final List<Coordinate> points = new ArrayList<>();
    private double lengthKm;
    private String entity1;
    private String entity2;

    public Boundary(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public double getLengthKm() {
        return lengthKm;
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

    // Setters
    public void setType(Type type) {
        this.type = type;
    }

    public void setLengthKm(double length) {
        this.lengthKm = length;
    }

    public void setEntities(String entity1, String entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    public void addPoint(Coordinate coord) {
        points.add(coord);
    }

    /**
     * Calculates length from points if available.
     */
    public double calculateLength() {
        if (points.size() < 2)
            return 0;
        double total = 0;
        for (int i = 1; i < points.size(); i++) {
            total += points.get(i - 1).distanceTo(points.get(i));
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Boundary '%s' (%s): %.1f km", name, type, lengthKm);
    }

    // Notable boundaries
    public static Boundary usCanadaBorder() {
        Boundary b = new Boundary("US-Canada Border", Type.NATIONAL);
        b.setEntities("United States", "Canada");
        b.setLengthKm(8891);
        return b;
    }
}
