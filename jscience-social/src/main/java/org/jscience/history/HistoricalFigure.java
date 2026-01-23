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

package org.jscience.history;

import java.io.Serializable;
import java.util.*;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a significant person in history.
 * Records biographical data, roles, and major historical achievements.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class HistoricalFigure implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Roles identifying the nature of the person's historical significance. */
    public enum Role {
        RULER, MILITARY, RELIGIOUS, PHILOSOPHER, SCIENTIST,
        ARTIST, EXPLORER, REVOLUTIONARY, REFORMER, INVENTOR
    }

    /** The full name of the figure. */
    @Attribute
    private final String name;

    /** The historical roles played by this figure. */
    @Attribute
    private final Set<Role> roles = EnumSet.noneOf(Role.class);

    /** Official title (e.g., "Emperor", "Pharaoh"). */
    @Attribute
    private String title;

    /** Main nationality or cultural affiliation. */
    @Attribute
    private String nationality;

    /** Estimated or recorded birth date. */
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private FuzzyDate birthDate;

    /** Estimated or recorded death date. */
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private FuzzyDate deathDate;

    /** Summary of life and historical importance. */
    @Attribute
    private String biography;

    /** List of major historical accomplishments. */
    @Attribute
    private final List<String> achievements = new ArrayList<>();

    /**
     * Creates a new HistoricalFigure.
     * 
     * @param name the figure's name
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is blank
     */
    public HistoricalFigure(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }

    /**
     * Creates a new HistoricalFigure with an initial role.
     * 
     * @param name the figure's name
     * @param primaryRole the main historical role
     * @throws NullPointerException if name or primaryRole is null
     */
    public HistoricalFigure(String name, Role primaryRole) {
        this(name);
        this.roles.add(Objects.requireNonNull(primaryRole, "Role cannot be null"));
    }

    /**
     * Returns the name of the figure.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable set of roles.
     *
     * @return the roles
     */
    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    /**
     * Returns the person's title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the person's nationality.
     *
     * @return nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Returns the birth date.
     *
     * @return birth date (possibly fuzzy)
     */
    public FuzzyDate getBirthDate() {
        return birthDate;
    }

    /**
     * Returns the death date.
     *
     * @return death date (possibly fuzzy)
     */
    public FuzzyDate getDeathDate() {
        return deathDate;
    }

    /**
     * Returns the biography text.
     *
     * @return biography
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Returns an unmodifiable list of achievements.
     *
     * @return achievements
     */
    public List<String> getAchievements() {
        return Collections.unmodifiableList(achievements);
    }

    /**
     * Sets the official title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the nationality.
     *
     * @param nationality nationality
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * Sets the birth date.
     *
     * @param date birth date
     */
    public void setBirthDate(FuzzyDate date) {
        this.birthDate = date;
    }

    /**
     * Sets the death date.
     *
     * @param date death date
     */
    public void setDeathDate(FuzzyDate date) {
        this.deathDate = date;
    }

    /**
     * Sets the biography.
     *
     * @param bio biography text
     */
    public void setBiography(String bio) {
        this.biography = bio;
    }

    /**
     * Adds a historical role.
     *
     * @param role the role to add
     * @throws NullPointerException if role is null
     */
    public void addRole(Role role) {
        roles.add(Objects.requireNonNull(role, "Role cannot be null"));
    }

    /**
     * Adds an achievement.
     *
     * @param achievement the achievement text
     * @throws NullPointerException if achievement is null
     */
    public void addAchievement(String achievement) {
        achievements.add(Objects.requireNonNull(achievement, "Achievement cannot be null"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoricalFigure that)) return false;
        return Objects.equals(name, that.name) &&
                Objects.equals(birthDate, that.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthDate);
    }

    @Override
    public String toString() {
        return String.format("%s%s (%s)",
                title != null ? title + " " : "", name, roles);
    }

    /**
     * Static factory for Napoleon Bonaparte.
     *
     * @return HistoricalFigure instance
     */
    public static HistoricalFigure napoleon() {
        HistoricalFigure h = new HistoricalFigure("Napoleon Bonaparte", Role.RULER);
        h.addRole(Role.MILITARY);
        h.setTitle("Emperor");
        h.setNationality("French");
        h.setBirthDate(FuzzyDate.of(1769, 8, 15));
        h.setDeathDate(FuzzyDate.of(1821, 5, 5));
        h.addAchievement("Napoleonic Code");
        h.addAchievement("Battle of Austerlitz");
        return h;
    }

    /**
     * Static factory for Cleopatra VII.
     *
     * @return HistoricalFigure instance
     */
    public static HistoricalFigure cleopatra() {
        HistoricalFigure h = new HistoricalFigure("Cleopatra VII", Role.RULER);
        h.setTitle("Pharaoh");
        h.setNationality("Egyptian");
        h.setBirthDate(FuzzyDate.bce(69));
        h.setDeathDate(FuzzyDate.of(-30, 8, 12).getEra() == FuzzyDate.Era.BCE ? FuzzyDate.of(-30, 8, 12) : FuzzyDate.bce(30)); 
        // Wait, FuzzyDate.of logic might need adjustment if I pass negative years. 
        // Current FuzzyDate.of(year) assumes Era.CE. Let's use bce() factory instead.
        h.setDeathDate(FuzzyDate.bce(30)); // Approximate enough.
        return h;
    }
}
