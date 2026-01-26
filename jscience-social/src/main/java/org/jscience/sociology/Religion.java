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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a religion, faith tradition, or spiritual system.
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Religion implements ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    /**
     * Categories of religious systems based on theological structure.
     */
    public enum Type {
        MONOTHEISTIC, POLYTHEISTIC, PANTHEISTIC, ATHEISTIC,
        ANIMISTIC, SHAMANISTIC, PHILOSOPHICAL
    }

    public static final Religion BUDDHISM = new Religion("Buddhism", Type.PHILOSOPHICAL);

    @Attribute
    private Type type;
    
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

    @Attribute
    private final List<org.jscience.philosophy.Belief> complexBeliefs = new ArrayList<>();

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
        this.id = new SimpleIdentification(trimmedName);
        setName(trimmedName);
    }

    /**
     * Creates a new religion with name and type.
     * @param name the name
     * @param type the theological type
     */
    public Religion(String name, Type type) {
        this(name);
        this.type = type;
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
    public Type getType() {
        return type;
    }

    /**
     * Sets the religious category.
     * @param type the type to set
     */
    public void setType(Type type) {
        this.type = type;
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

    public void addBelief(org.jscience.philosophy.Belief belief) {
        if (belief != null) complexBeliefs.add(belief);
    }

    public List<org.jscience.philosophy.Belief> getCoreBeliefs() {
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
