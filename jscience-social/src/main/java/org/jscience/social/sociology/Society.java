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
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.jscience.natural.biology.ecology.Population;
import org.jscience.natural.biology.HomoSapiens;
import org.jscience.social.linguistics.Language;
import org.jscience.core.util.identity.UUIDIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;
import org.jscience.natural.engineering.eventdriven.EventDrivenEngine;
import org.jscience.natural.engineering.eventdriven.Event;

/**
 * Represents a society, defined by its type, culture, institutions, and geographic location.
 */
@Persistent
public class Society extends Population<Person> {

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

    @Attribute
    private SocietyType type;
    
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Culture culture;
    
    @Attribute
    private long populationCount;
    
    @Attribute
    private String governmentType;
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private List<SociologicalGroup> institutions;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Language> languages = new HashSet<>();

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Religion> religions = new HashSet<>();

    /**
     * Creates a new society with the specified name.
     */
    public Society(String name) {
        this(name, null, null);
    }

    /**
     * Creates a new society with the specified name and simulation engine.
     */
    public Society(String name, EventDrivenEngine engine) {
        this(name, null, engine);
    }

    /**
     * Creates a new society with name, type and simulation engine.
     */
    public Society(String name, SocietyType type, EventDrivenEngine engine) {
        super(new UUIDIdentification(UUID.randomUUID()), name, HomoSapiens.SPECIES, null, engine);
        this.type = type;
        this.institutions = new ArrayList<>();
    }

    public SocietyType getType() {
        return type;
    }

    public void setType(SocietyType type) {
        this.type = type;
    }

    public Culture getCulture() {
        return culture;
    }

    public void setCulture(Culture culture) {
        this.culture = culture;
    }

    public long getPopulationCount() {
        return populationCount;
    }

    public void setPopulationCount(long populationCount) {
        this.populationCount = populationCount;
    }

    public String getGovernmentType() {
        return governmentType;
    }

    public void setGovernmentType(String governmentType) {
        this.governmentType = governmentType;
    }

    public void addInstitution(SociologicalGroup institution) {
        if (institution != null && !institutions.contains(institution)) {
            institutions.add(institution);
        }
    }

    public List<SociologicalGroup> getInstitutions() {
        return Collections.unmodifiableList(institutions);
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
    public void processEvent(Event event) {
        // Default: handle global societal changes
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Society other)) return false;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Society '%s' (%s), population: %d", getName(), type, populationCount);
    }
}
