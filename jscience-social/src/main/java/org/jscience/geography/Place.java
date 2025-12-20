/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.geography;

/**
 * Represents a named geographic place.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Place {

    public enum Type {
        CITY, TOWN, VILLAGE, NEIGHBORHOOD, LANDMARK,
        PARK, MOUNTAIN, RIVER, LAKE, OCEAN, ISLAND,
        BUILDING, MONUMENT, AIRPORT, STATION
    }

    private final String name;
    private final Type type;
    private Coordinate location;
    private String description;
    private String region;
    private String country;
    private long population;

    public Place(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Place(String name, Type type, Coordinate location) {
        this(name, type);
        this.location = location;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Coordinate getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public long getPopulation() {
        return population;
    }

    // Setters
    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    /**
     * Calculates distance to another place.
     */
    public double distanceTo(Place other) {
        if (location != null && other.location != null) {
            return location.distanceTo(other.location);
        }
        return Double.NaN;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, type);
    }

    // Notable places
    public static Place eiffelTower() {
        Place p = new Place("Eiffel Tower", Type.MONUMENT,
                new Coordinate(48.8584, 2.2945));
        p.setCountry("France");
        p.setDescription("Iron lattice tower on the Champ de Mars");
        return p;
    }

    public static Place mountEverest() {
        Place p = new Place("Mount Everest", Type.MOUNTAIN,
                new Coordinate(27.9881, 86.9250, 8848.86));
        p.setCountry("Nepal/China");
        p.setDescription("Highest mountain on Earth");
        return p;
    }
}
