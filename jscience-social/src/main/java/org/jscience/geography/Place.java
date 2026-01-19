package org.jscience.geography;

import org.jscience.geography.coordinates.Coordinates;

/**
 * A Geographical Place.
 * Represents a named entity at a location.
 */
public class Place {
    
    public enum Type {
        CITY, COUNTRY, REGION, CONTINENT, OTHER
    }

    private final String name;
    private final Type type;
    private final Coordinates coordinates; // or Boundary
    private String region;
    private String country;
    private int population;

    public Place(String name, Coordinates coordinates) {
        this(name, Type.OTHER, coordinates);
    }
    
    public Place(String name, Type type) {
        this(name, type, null);
    }

    public Place(String name, Type type, Coordinates coordinates) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Place name cannot be null or empty.");
        }
        this.name = name;
        this.type = type != null ? type : Type.OTHER;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }
    
    public Type getType() {
        return type;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
    
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }

    public void addInhabitant(Object inhabitant) {
        this.population++;
    }

    public void removeInhabitant(Object inhabitant) {
        if (this.population > 0) {
            this.population--;
        }
    }
    
    @Override
    public String toString() {
        return name + (coordinates != null ? " (" + coordinates + ")" : "");
    }
}
