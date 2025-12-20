/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.sociology;

import java.util.*;

/**
 * Represents a society.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Society {

    public enum Type {
        HUNTER_GATHERER, PASTORAL, HORTICULTURAL, AGRICULTURAL,
        INDUSTRIAL, POST_INDUSTRIAL, INFORMATION
    }

    private final String name;
    private Type type;
    private Culture culture;
    private long population;
    private String governmentType;
    private final List<Group> institutions = new ArrayList<>();

    public Society(String name) {
        this.name = name;
    }

    public Society(String name, Type type) {
        this(name);
        this.type = type;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Culture getCulture() {
        return culture;
    }

    public long getPopulation() {
        return population;
    }

    public String getGovernmentType() {
        return governmentType;
    }

    public List<Group> getInstitutions() {
        return Collections.unmodifiableList(institutions);
    }

    // Setters
    public void setType(Type type) {
        this.type = type;
    }

    public void setCulture(Culture culture) {
        this.culture = culture;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public void setGovernmentType(String type) {
        this.governmentType = type;
    }

    public void addInstitution(Group institution) {
        institutions.add(institution);
    }

    @Override
    public String toString() {
        return String.format("Society '%s' (%s, pop: %d)", name, type, population);
    }
}
