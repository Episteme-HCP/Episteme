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
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;
import org.jscience.geography.Locatable;
import org.jscience.geography.Place;
import org.jscience.util.Temporal;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a significant historical event with support for imprecise dating.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class HistoricalEvent implements Identified<String>, Locatable, Temporal, Serializable {

    private static final long serialVersionUID = 1L;

    /** Event categories for filtering and analysis. */
    public enum Category {
        POLITICAL, MILITARY, CULTURAL, SCIENTIFIC, ECONOMIC, RELIGIOUS, NATURAL
    }

    @Id
    private final String id;

    /** The display name of the event. */
    @Attribute
    private final String name;

    /** Detailed description of the event. */
    @Attribute
    private final String description;

    /** Start date of the event (possibly fuzzy). */
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final FuzzyDate startDate;

    /** End date of the event (defaults to startDate if instantaneous). */
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final FuzzyDate endDate;

    /** Category for classification. */
    @Attribute
    private final Category category;

    /** Place where the event occurred. */
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Place location;

    /**
     * Complete constructor for a historical event.
     * 
     * @param name common name
     * @param description detailed text
     * @param startDate when it started
     * @param endDate when it ended (can be null for instantaneous events)
     * @param category the category
     * @param location the place
     * @throws NullPointerException if any required argument is null
     */
    public HistoricalEvent(String name, String description, FuzzyDate startDate, FuzzyDate endDate,
            Category category, Place location) {
        this.id = UUID.randomUUID().toString();
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = description;
        this.startDate = Objects.requireNonNull(startDate, "Start date cannot be null");
        this.endDate = endDate != null ? endDate : startDate;
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.location = location;
    }

    /**
     * Short constructor for simpler events.
     * 
     * @param name common name
     * @param date the date of the event
     * @param category the category
     * @throws NullPointerException if any required argument is null
     */
    public HistoricalEvent(String name, FuzzyDate date, Category category) {
        this(name, null, date, date, category, null);
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public FuzzyDate getStartDate() {
        return startDate;
    }

    public FuzzyDate getEndDate() {
        return endDate;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public Place getPosition() {
        return location;
    }

    @Override
    public Instant getTimestamp() {
        if (startDate != null && startDate.isKnown() && !startDate.isBce()) {
            try {
                return startDate.toLocalDate().atStartOfDay(ZoneOffset.UTC).toInstant();
            } catch (IllegalStateException | java.time.DateTimeException e) {
                // Fallback for imprecise modern dates
                return Instant.MIN;
            }
        }
        return Instant.MIN;
    }

    /**
     * Returns approximate duration in years.
     * 
     * @return duration in years (positive integer)
     */
    public int getDurationYears() {
        if (startDate == null || endDate == null)
            return 0;
        Integer startYear = startDate.getYear();
        Integer endYear = endDate.getYear();
        if (startYear == null || endYear == null)
            return 0;

        int start = startDate.isBce() ? -startYear : startYear;
        int end = endDate.isBce() ? -endYear : endYear;
        return end - start;
    }

    /**
     * Checks if this event overlaps in time with another.
     * 
     * @param other the other event
     * @return true if they overlap
     * @throws NullPointerException if other is null
     */
    public boolean overlaps(HistoricalEvent other) {
        Objects.requireNonNull(other, "Other event cannot be null");
        return startDate.compareTo(other.endDate) <= 0 &&
                endDate.compareTo(other.startDate) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoricalEvent that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        if (startDate.equals(endDate)) {
            return String.format("%s (%s) - %s", name, startDate, category);
        }
        return String.format("%s (%s to %s) - %s", name, startDate, endDate, category);
    }

    // Notable historical events with FuzzyDate
    public static final HistoricalEvent FRENCH_REVOLUTION = new HistoricalEvent(
            "French Revolution",
            "Political revolution in France", 
            FuzzyDate.of(1789, 7, 14),
            FuzzyDate.of(1799, 11, 9), Category.POLITICAL,
            new Place("France", Place.Type.COUNTRY));

    public static final HistoricalEvent WORLD_WAR_I = new HistoricalEvent(
            "World War I",
            "Global military conflict", 
            FuzzyDate.of(1914, 7, 28),
            FuzzyDate.of(1918, 11, 11), Category.MILITARY,
            new Place("Global", Place.Type.REGION));

    public static final HistoricalEvent WORLD_WAR_II = new HistoricalEvent(
            "World War II",
            "Global military conflict", 
            FuzzyDate.of(1939, 9, 1),
            FuzzyDate.of(1945, 9, 2), Category.MILITARY,
            new Place("Global", Place.Type.REGION));

    public static final HistoricalEvent MOON_LANDING = new HistoricalEvent(
            "Apollo 11 Moon Landing",
            FuzzyDate.of(1969, 7, 20), Category.SCIENTIFIC);

    // Ancient events with BCE support
    public static final HistoricalEvent FALL_OF_ROME = new HistoricalEvent(
            "Fall of Western Rome",
            "End of the Western Roman Empire", 
            FuzzyDate.of(476, 9, 4),
            FuzzyDate.of(476, 9, 4), Category.POLITICAL,
            new Place("Rome", Place.Type.CITY));

    public static final HistoricalEvent BATTLE_OF_MARATHON = new HistoricalEvent(
            "Battle of Marathon",
            "Greek victory over Persian forces", 
            FuzzyDate.bce(490),
            FuzzyDate.bce(490), Category.MILITARY,
            new Place("Marathon", Place.Type.CITY));

    public static final HistoricalEvent FOUNDING_OF_ROME = new HistoricalEvent(
            "Founding of Rome",
            "Traditional founding date of Rome", 
            FuzzyDate.bce(753),
            FuzzyDate.bce(753), Category.POLITICAL,
            new Place("Rome", Place.Type.CITY));

    public static final HistoricalEvent GREAT_PYRAMID = new HistoricalEvent(
            "Construction of Great Pyramid",
            "Building of the Great Pyramid of Giza", 
            FuzzyDate.circaBce(2560),
            FuzzyDate.circaBce(2540), Category.CULTURAL,
            new Place("Giza", Place.Type.CITY));
}
