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

package org.jscience.history.archeology;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import org.jscience.geography.Place;
import org.jscience.sociology.Culture;
import org.jscience.util.Positioned;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a civilization, modeled as a culture distributed through space and time.
 * Covers both living and historical societies, tracking their geographical extent and duration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
@Persistent
public class Civilization implements Positioned, Serializable {

    private static final long serialVersionUID = 1L;

    /** The associated core culture of the civilization. */
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Culture culture;

    /** Geographical location or center of the civilization. */
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Place place;

    /** Estimated birth or formation date of the civilization. */
    @Attribute
    private final Date startDate;

    /** Estimated decline or dissolution date; null if the civilization is still active. */
    @Attribute
    private Date endDate;

    /** Summary of main characteristics and achievements. */
    @Attribute
    private String description = "";

    /**
     * Creates a new Civilization.
     *
     * @param culture the representative culture
     * @param place primary geographic location
     * @param startDate the founding date
     * @throws NullPointerException if any argument is null
     */
    public Civilization(Culture culture, Place place, Date startDate) {
        this.culture = Objects.requireNonNull(culture, "Culture cannot be null");
        this.place = Objects.requireNonNull(place, "Place cannot be null");
        this.startDate = Objects.requireNonNull(startDate, "Start date cannot be null");
    }

    /**
     * Returns the culture.
     *
     * @return the culture
     */
    public Culture getCulture() {
        return culture;
    }

    /**
     * Returns the primary geographical position.
     *
     * @return the place
     */
    @Override
    public Place getPosition() {
        return place;
    }

    /**
     * Returns the founding date.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Returns the end date, or null if still existing.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the civilization.
     *
     * @param endDate the dissolution date
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description characterization string
     * @throws NullPointerException if description is null
     */
    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description, "Description cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Civilization that)) return false;
        return Objects.equals(culture, that.culture) && 
               Objects.equals(place, that.place) && 
               Objects.equals(startDate, that.startDate) && 
               Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(culture, place, startDate, endDate);
    }

    @Override
    public String toString() {
        return String.format("Civilization: %s in %s (%s - %s)", culture, place, startDate, 
            endDate != null ? endDate : "present");
    }
}
