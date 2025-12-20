package org.jscience.politics;

import org.jscience.geography.Place;

/**
 * Represents an electoral district or constituency.
 */
public class Constituency {

    private final String name;
    private final Place area;
    private int population;
    private int electorateSize; // Number of eligible voters

    public Constituency(String name, Place area, int population) {
        this.name = name;
        this.area = area;
        this.population = population;
        this.electorateSize = (int) (population * 0.7); // Rough estimate default
    }

    public String getName() {
        return name;
    }

    public Place getArea() {
        return area;
    }

    public int getPopulation() {
        return population;
    }

    public int getElectorateSize() {
        return electorateSize;
    }

    public void setElectorateSize(int size) {
        this.electorateSize = size;
    }

    @Override
    public String toString() {
        return String.format("%s (%d voters)", name, electorateSize);
    }
}
