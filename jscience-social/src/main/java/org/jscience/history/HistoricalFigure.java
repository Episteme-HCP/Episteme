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

package org.jscience.history;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a historical figure.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HistoricalFigure {

    public enum Role {
        RULER, MILITARY, RELIGIOUS, PHILOSOPHER, SCIENTIST,
        ARTIST, EXPLORER, REVOLUTIONARY, REFORMER, INVENTOR
    }

    private final String name;
    private final Set<Role> roles = EnumSet.noneOf(Role.class);
    private String title;
    private String nationality;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String biography;
    private final List<String> achievements = new ArrayList<>();

    public HistoricalFigure(String name) {
        this.name = name;
    }

    public HistoricalFigure(String name, Role primaryRole) {
        this(name);
        this.roles.add(primaryRole);
    }

    // Getters
    public String getName() {
        return name;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public String getTitle() {
        return title;
    }

    public String getNationality() {
        return nationality;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public String getBiography() {
        return biography;
    }

    public List<String> getAchievements() {
        return Collections.unmodifiableList(achievements);
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setBirthDate(LocalDate date) {
        this.birthDate = date;
    }

    public void setDeathDate(LocalDate date) {
        this.deathDate = date;
    }

    public void setBiography(String bio) {
        this.biography = bio;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void addAchievement(String achievement) {
        achievements.add(achievement);
    }

    @Override
    public String toString() {
        return String.format("%s%s (%s)",
                title != null ? title + " " : "", name, roles);
    }

    // Historical figures
    public static HistoricalFigure napoleon() {
        HistoricalFigure h = new HistoricalFigure("Napoleon Bonaparte", Role.RULER);
        h.addRole(Role.MILITARY);
        h.setTitle("Emperor");
        h.setNationality("French");
        h.setBirthDate(LocalDate.of(1769, 8, 15));
        h.setDeathDate(LocalDate.of(1821, 5, 5));
        h.addAchievement("Napoleonic Code");
        h.addAchievement("Battle of Austerlitz");
        return h;
    }

    public static HistoricalFigure cleopatra() {
        HistoricalFigure h = new HistoricalFigure("Cleopatra VII", Role.RULER);
        h.setTitle("Pharaoh");
        h.setNationality("Egyptian");
        h.setBirthDate(LocalDate.of(-69, 1, 1));
        h.setDeathDate(LocalDate.of(-30, 8, 12));
        return h;
    }
}
