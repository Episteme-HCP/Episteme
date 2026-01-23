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

package org.jscience.sociology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.jscience.geography.Place;
import org.jscience.util.Named;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a society, defined by its type, culture, institutions, and geographic location.
 * Provides a framework for modeling societal development levels, from hunter-gatherer to information societies.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Society implements org.jscience.geography.Locatable, Identified<String>, Named, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Categories of societal development stages.
     */
    public enum Type {
        HUNTER_GATHERER, PASTORAL, HORTICULTURAL, AGRICULTURAL,
        INDUSTRIAL, POST_INDUSTRIAL, INFORMATION
    }

    @Id
    private final String id;
    
    @Attribute
    private final String name;
    
    @Attribute
    private Type type;
    
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Culture culture;
    
    @Attribute
    private long populationCount;
    
    @Attribute
    private String governmentType;
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Group> institutions = new ArrayList<>();
    
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Place location;

    /**
     * Creates a new society with the specified name.
     *
     * @param name the name of the society
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public Society(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = Objects.requireNonNull(name, "Name cannot be null").trim();
        if (this.name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    /**
     * Creates a new society with name and type.
     * @param name the name
     * @param type the societal type
     */
    public Society(String name, Type type) {
        this(name);
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the societal type/development stage.
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the societal type.
     * @param type the type to set
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Returns the dominant culture of the society.
     * @return the culture
     */
    public Culture getCulture() {
        return culture;
    }

    /**
     * Sets the dominant culture of the society.
     * @param culture the culture to set
     */
    public void setCulture(Culture culture) {
        this.culture = culture;
    }

    /**
     * Returns the current population count.
     * @return population count
     */
    public long getPopulationCount() {
        return populationCount;
    }

    /**
     * Sets the population count.
     * @param populationCount count
     */
    public void setPopulationCount(long populationCount) {
        this.populationCount = populationCount;
    }

    /**
     * Returns the type of government.
     * @return government type string
     */
    public String getGovernmentType() {
        return governmentType;
    }

    /**
     * Sets the type of government.
     * @param governmentType government type string
     */
    public void setGovernmentType(String governmentType) {
        this.governmentType = governmentType;
    }

    /**
     * Adds an institutional group to the society.
     * @param institution the institution group
     */
    public void addInstitution(Group institution) {
        if (institution != null && !institutions.contains(institution)) {
            institutions.add(institution);
        }
    }

    /**
     * Returns an unmodifiable list of societal institutions.
     * @return institutions list
     */
    public List<Group> getInstitutions() {
        return Collections.unmodifiableList(institutions);
    }

    @Override
    public Place getPosition() {
        return location;
    }

    /**
     * Sets the primary geographic location of the society.
     * @param location the place to set
     */
    public void setLocation(Place location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format("Society '%s' (%s), population: %d", name, type, populationCount);
    }
}
