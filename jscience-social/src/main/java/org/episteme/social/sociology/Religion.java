/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.sociology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.episteme.natural.earth.Place;
import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;
import org.episteme.social.philosophy.Belief;

/**
 * Represents a religion, faith tradition, or spiritual system.
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 * Support for clergy members and holy sites added.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Religion implements ComprehensiveIdentification {
// ...
// I will not replace the whole file. I will use multi_replace_file_content.

    private static final long serialVersionUID = 2L;

    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    @Deprecated
    public enum Type {
        @Deprecated MONOTHEISTIC, @Deprecated POLYTHEISTIC, @Deprecated PANTHEISTIC, @Deprecated ATHEISTIC,
        @Deprecated ANIMISTIC, @Deprecated SHAMANISTIC, @Deprecated PHILOSOPHICAL;
        
        @Deprecated
        public ReligionType toReligionType() {
            return ReligionType.valueOf(this.name());
        }
    }

    public static final Religion BUDDHISM = new Religion("Buddhism", ReligionType.PHILOSOPHICAL);

    @Attribute
    private ReligionType type;
    
    @Attribute
    private long followers;
    
    @Attribute
    private String founder;
    
    @Attribute
    private int foundedYear; // Negative for BCE
    
    @Attribute
    private String originRegion;
    
    @Attribute
    private String holyText;
    
    @Attribute
    private final List<String> beliefs = new ArrayList<>();
    
    @Attribute
    private final List<String> practices = new ArrayList<>();
    
    @Attribute
    private final List<String> holidayList = new ArrayList<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Belief> complexBeliefs = new ArrayList<>();

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Person> clergy = new HashSet<>();

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Place> holySites = new HashSet<>();

    /**
     * Creates a new religion with the specified name.
     *
     * @param name the unique name of the religion
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public Religion(String name) {
        String trimmedName = Objects.requireNonNull(name, "Name cannot be null").trim();
        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.id = new SimpleIdentification("Religion:" + UUID.randomUUID());
        setName(trimmedName);
    }

    /**
     * Creates a new religion with name and type.
     * @param name the name
     * @param type the theological type
     */
    public Religion(String name, ReligionType type) {
        this(name);
        this.type = type;
    }

    @Deprecated
    public Religion(String name, Type type) {
        this(name, type != null ? type.toReligionType() : ReligionType.OTHER);
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
     * Returns the religious category.
     * @return the type
     */
    public ReligionType getType() {
        return type;
    }

    /**
     * Sets the religious category.
     * @param type the type to set
     */
    public void setType(ReligionType type) {
        this.type = type;
    }

    @Deprecated
    public Type getLegacyType() {
        try {
            return Type.valueOf(type.name());
        } catch (Exception e) {
            return Type.PHILOSOPHICAL;
        }
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(int foundedYear) {
        this.foundedYear = foundedYear;
    }

    public String getOriginRegion() {
        return originRegion;
    }

    public void setOriginRegion(String originRegion) {
        this.originRegion = originRegion;
    }

    public String getHolyText() {
        return holyText;
    }

    public void setHolyText(String holyText) {
        this.holyText = holyText;
    }

    public void addBelief(String belief) {
        if (belief != null) beliefs.add(belief);
    }

    public void addBelief(Belief belief) {
        if (belief != null) complexBeliefs.add(belief);
    }

    public List<Belief> getCoreBeliefs() {
        return Collections.unmodifiableList(complexBeliefs);
    }

    public List<String> getBeliefs() {
        return Collections.unmodifiableList(beliefs);
    }

    public void addPractice(String practice) {
        if (practice != null) practices.add(practice);
    }

    public List<String> getPractices() {
        return Collections.unmodifiableList(practices);
    }

    public void addHoliday(String holiday) {
        if (holiday != null) holidayList.add(holiday);
    }

    public List<String> getHolidays() {
        return Collections.unmodifiableList(holidayList);
    }

    public void addClergyMember(Person person) {
        if (person != null) clergy.add(person);
    }

    public Set<Person> getClergy() {
        return Collections.unmodifiableSet(clergy);
    }

    public void addHolySite(Place place) {
        if (place != null) holySites.add(place);
    }

    public Set<Place> getHolySites() {
        return Collections.unmodifiableSet(holySites);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Religion religion)) return false;
        return Objects.equals(id, religion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName() + " (" + type + ")";
    }
}

