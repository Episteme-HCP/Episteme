/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.geography;

/**
 * Represents geographic/geopolitical region.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Region {

    public enum Type {
        CONTINENT, SUBCONTINENT, COUNTRY, STATE, PROVINCE,
        COUNTY, CITY, DISTRICT, TIMEZONE, CLIMATE_ZONE
    }

    private final String name;
    private Type type;
    private String parentRegion;
    private double areaSqKm;
    private long population;
    private Coordinate center;
    private String capital;

    public Region(String name, Type type) {
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

    public String getParentRegion() {
        return parentRegion;
    }

    public double getAreaSqKm() {
        return areaSqKm;
    }

    public long getPopulation() {
        return population;
    }

    public Coordinate getCenter() {
        return center;
    }

    public String getCapital() {
        return capital;
    }

    // Setters
    public void setType(Type type) {
        this.type = type;
    }

    public void setParentRegion(String parent) {
        this.parentRegion = parent;
    }

    public void setAreaSqKm(double area) {
        this.areaSqKm = area;
    }

    public void setPopulation(long pop) {
        this.population = pop;
    }

    public void setCenter(Coordinate center) {
        this.center = center;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    /**
     * Returns population density (people per sq km).
     */
    public double getPopulationDensity() {
        return areaSqKm > 0 ? population / areaSqKm : 0;
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %.0f km², pop %d", name, type, areaSqKm, population);
    }

    // Major regions
    public static Region europe() {
        Region r = new Region("Europe", Type.CONTINENT);
        r.setAreaSqKm(10_180_000);
        r.setPopulation(750_000_000);
        return r;
    }

    public static Region california() {
        Region r = new Region("California", Type.STATE);
        r.setParentRegion("United States");
        r.setAreaSqKm(423_970);
        r.setPopulation(39_000_000);
        r.setCapital("Sacramento");
        return r;
    }
}
