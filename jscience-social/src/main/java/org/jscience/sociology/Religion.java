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
import org.jscience.util.Named;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a religion, faith tradition, or spiritual system.
 * Encapsulates core metadata such as type, follower count, holy texts, and beliefs.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Religion implements Identified<String>, Named, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Categories of religious systems based on theological structure.
     */
    public enum Type {
        MONOTHEISTIC, POLYTHEISTIC, PANTHEISTIC, ATHEISTIC,
        ANIMISTIC, SHAMANISTIC, PHILOSOPHICAL
    }

    @Id
    private final String name;
    
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
        this.name = Objects.requireNonNull(name, "Name cannot be null").trim();
        if (this.name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
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
    public String getId() {
        return name;
    }

    @Override
    public String getName() {
        return name;
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

    /**
     * Returns the approximate number of global followers.
     * @return follower count
     */
    public long getFollowers() {
        return followers;
    }

    /**
     * Sets the global follower count.
     * @param followers count
     */
    public void setFollowers(long followers) {
        this.followers = followers;
    }

    /**
     * Returns the historical founder's name.
     * @return founder string, or null
     */
    public String getFounder() {
        return founder;
    }

    /**
     * Sets the historical founder.
     * @param founder founder name
     */
    public void setFounder(String founder) {
        this.founder = founder;
    }

    /**
     * Returns the year the religion was founded.
     * @return year (BCE is negative)
     */
    public int getFoundedYear() {
        return foundedYear;
    }

    /**
     * Sets the founding year.
     * @param foundedYear year
     */
    public void setFoundedYear(int foundedYear) {
        this.foundedYear = foundedYear;
    }

    /**
     * Returns the geographic region of origin.
     * @return region string
     */
    public String getOriginRegion() {
        return originRegion;
    }

    /**
     * Sets the region of origin.
     * @param originRegion region string
     */
    public void setOriginRegion(String originRegion) {
        this.originRegion = originRegion;
    }

    /**
     * Returns the primary holy text title.
     * @return holy text name
     */
    public String getHolyText() {
        return holyText;
    }

    /**
     * Sets the primary holy text.
     * @param holyText text name
     */
    public void setHolyText(String holyText) {
        this.holyText = holyText;
    }

    /**
     * Adds a belief or core tenet.
     * @param belief belief string
     */
    public void addBelief(String belief) {
        if (belief != null) beliefs.add(belief);
    }

    /**
     * Returns an unmodifiable list of core beliefs.
     * @return beliefs list
     */
    public List<String> getBeliefs() {
        return Collections.unmodifiableList(beliefs);
    }

    /**
     * Adds a religious practice or ritual.
     * @param practice practice string
     */
    public void addPractice(String practice) {
        if (practice != null) practices.add(practice);
    }

    /**
     * Returns an unmodifiable list of practices.
     * @return practices list
     */
    public List<String> getPractices() {
        return Collections.unmodifiableList(practices);
    }

    /**
     * Adds a religious holiday or celebration.
     * @param holiday holiday name
     */
    public void addHoliday(String holiday) {
        if (holiday != null) holidays.add(holiday);
    }

    /**
     * Returns an unmodifiable list of holidays.
     * @return holidays list
     */
    public List<String> getHolidays() {
        return Collections.unmodifiableList(holidays);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Religion)) return false;
        Religion religion = (Religion) o;
        return Objects.equals(name, religion.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
