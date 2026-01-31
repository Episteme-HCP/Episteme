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

package org.jscience.social.sociology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.jscience.natural.earth.Place;
import org.jscience.social.linguistics.Language;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.UUIDIdentification;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;
import org.jscience.core.util.Positioned;

/**
 * Represents a society, defined by its type, culture, institutions, and geographic location.
 * Provides a framework for modeling societal development levels, from hunter-gatherer to information societies.
 * Modernized to implement ComprehensiveIdentification and support enhanced features like multilinguality.
 * * @version 1.2
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Society implements Positioned<Place>, ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    @Deprecated
    public enum Type {
        @Deprecated HUNTER_GATHERER, @Deprecated PASTORAL, @Deprecated HORTICULTURAL, @Deprecated AGRICULTURAL,
        @Deprecated INDUSTRIAL, @Deprecated POST_INDUSTRIAL, @Deprecated INFORMATION;
        
        @Deprecated
        public SocietyType toSocietyType() {
            return SocietyType.valueOf(this.name());
        }
    }

    @Id
    private final Identification id;
    
    @Attribute
    private final Map<String, Object> traits = new HashMap<>();
    
    @Attribute
    private SocietyType type;
    
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

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Language> languages = new HashSet<>();

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Religion> religions = new HashSet<>();

    /**
     * Creates a new society with the specified name.
     *
     * @param name the name of the society
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public Society(String name) {
        this.id = new UUIDIdentification(UUID.randomUUID());
        setName(Objects.requireNonNull(name, "Name cannot be null").trim());
        if (getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    /**
     * Creates a new society with name and type.
     * @param name the name
     * @param type the societal type
     */
    public Society(String name, SocietyType type) {
        this(name);
        this.type = type;
    }

    @Deprecated
    public Society(String name, Type type) {
        this(name, type != null ? type.toSocietyType() : SocietyType.OTHER);
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    /**
     * Returns the societal type/development stage.
     * @return the type
     */
    public SocietyType getType() {
        return type;
    }

    /**
     * Sets the societal type.
     * @param type the type to set
     */
    public void setType(SocietyType type) {
        this.type = type;
    }

    @Deprecated
    public Type getLegacyType() {
        try {
            return Type.valueOf(type.name());
        } catch (Exception e) {
            return Type.AGRICULTURAL;
        }
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

    public void addLanguage(Language language) {
        if (language != null) languages.add(language);
    }

    public Set<Language> getLanguages() {
        return Collections.unmodifiableSet(languages);
    }

    public void addReligion(Religion religion) {
        if (religion != null) religions.add(religion);
    }

    public Set<Religion> getReligions() {
        return Collections.unmodifiableSet(religions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Society other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Society '%s' (%s), population: %d", getName(), type, populationCount);
    }
}

