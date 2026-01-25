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
import java.util.List;
import java.util.Objects;
import org.jscience.util.identity.AbstractIdentifiedEntity;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a religion, faith tradition, or spiritual system.
 * Extends AbstractIdentifiedEntity to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Religion extends AbstractIdentifiedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Categories of religious systems based on theological structure.
     */
    public enum Type {
        MONOTHEISTIC, POLYTHEISTIC, PANTHEISTIC, ATHEISTIC,
        ANIMISTIC, SHAMANISTIC, PHILOSOPHICAL
    }

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
    private final List<String> holidays = new ArrayList<>();

    /**
     * Creates a new religion with the specified name.
     *
     * @param name the unique name of the religion
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public Religion(String name) {
        super(new SimpleIdentification(Objects.requireNonNull(name, "Name cannot be null").trim()));
        if (getId().toString().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        setName(getId().toString());
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
        if (holiday != null) holidays.add(holiday);
    }

    public List<String> getHolidays() {
        return Collections.unmodifiableList(holidays);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Religion)) return false;
        Religion religion = (Religion) o;
        return Objects.equals(getName(), religion.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return getName() + " (" + type + ")";
    }
}
