/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.sociology;

import java.util.*;

/**
 * Represents a society.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Society implements org.jscience.geography.Locatable {

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
    private org.jscience.geography.Place location;

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

    @Override
    public org.jscience.geography.Place getLocation() {
        return location;
    }

    public void setLocation(org.jscience.geography.Place location) {
        this.location = location;
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


