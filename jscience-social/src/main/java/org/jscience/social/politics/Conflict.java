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

package org.jscience.social.politics;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.jscience.social.sociology.Group;
import org.jscience.core.util.Positioned;
import org.jscience.natural.earth.Place;
import org.jscience.core.util.Commented;
import org.jscience.core.util.Named;
import org.jscience.core.util.Temporal;
import org.jscience.social.history.time.TimeCoordinate;

/**
 * Represents a political or social conflict between multiple populations or groups.
 * Tracks temporal span, geographical location, and participating parties.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Conflict implements Named, Commented, Positioned<Place>, Temporal<TimeCoordinate>, Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private final Set<Group> groups = new HashSet<>();
    private Place place;
    private final TimeCoordinate startingDate;
    private TimeCoordinate endDate;
    private String comments;

    /**
     * Creates a new Conflict.
     *
     * @param name         the name of the conflict
     * @param involvedGroups groups participating in the conflict
     * @param place        the primary location of the conflict
     * @param startingDate the timestamp when the conflict began
     * @throws NullPointerException if any mandatory argument is null
     * @throws IllegalArgumentException if name is empty or no groups are involved
     */
    public Conflict(String name, Set<Group> involvedGroups, Place place, TimeCoordinate startingDate) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        
        Objects.requireNonNull(involvedGroups, "Groups cannot be null");
        if (involvedGroups.isEmpty()) throw new IllegalArgumentException("Conflict must involve at least one group.");
        this.groups.addAll(involvedGroups);
        
        this.place = Objects.requireNonNull(place, "Place cannot be null");
        this.startingDate = Objects.requireNonNull(startingDate, "Starting date cannot be null");
        this.comments = "";
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the conflict.
     * @param name the new name
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    /**
     * Returns an unmodifiable set of participating groups.
     * @return the involved groups
     */
    public Set<Group> getGroups() {
        return Collections.unmodifiableSet(groups);
    }

    @Override
    public Place getPosition() {
        return place;
    }

    /**
     * Returns the primary geographical location.
     * @return the place
     */
    public Place getPlace() {
        return place;
    }

    /**
     * Updates the conflict's primary location.
     * @param place the new location
     */
    public void setPlace(Place place) {
        this.place = Objects.requireNonNull(place, "Place cannot be null");
    }

    @Override
    public TimeCoordinate getWhen() {
        return startingDate;
    }

    /**
     * Returns the exact starting timestamp.
     * @return starting date
     */
    public TimeCoordinate getStartingDate() {
        return startingDate;
    }

    /**
     * Returns the timestamp when the conflict was resolved or ceased.
     * @return end date, or null if ongoing
     */
    public TimeCoordinate getEndDate() {
        return endDate;
    }

    /**
     * Sets the timestamp for conflict resolution.
     * @param endDate the resolution date
     */
    public void setEndDate(TimeCoordinate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String getComments() {
        return comments;
    }

    @Override
    public void setComments(String comments) {
        this.comments = Objects.requireNonNull(comments, "Comments cannot be null");
    }

    @Override
    public Map<String, Object> getTraits() {
        return Collections.emptyMap();
    }

    @Override
    public String toString() {
        return String.format("%s (%d groups involved) at %s", 
            name, groups.size(), (place != null ? place.getName() : "Unknown"));
    }
}

