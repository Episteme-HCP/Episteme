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
 * Represents a historical era or period.
 * Tracks time span, geographical region, and key events defining the period.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class HistoricalEra implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The common name of the era. */
    @Attribute
    private final String name;

    /** Estimated start date. */
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private FuzzyDate startDate;

    /** Estimated end date. */
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private FuzzyDate endDate;

    /** Descriptive summary of the era. */
    @Attribute
    private String description;

    /** Primary geographical region where the era is defined. */
    @Attribute
    private String region;

    /** Key historical events belonging to this era. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<HistoricalEvent> keyEvents = new ArrayList<>();

    /**
     * Creates a new HistoricalEra.
     * 
     * @param name the name of the era
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is blank
     */
    public HistoricalEra(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }

    /**
     * Creates a new HistoricalEra with defined boundaries.
     * 
     * @param name the name of the era
     * @param startDate start date
     * @param endDate end date
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is blank
     */
    public HistoricalEra(String name, FuzzyDate startDate, FuzzyDate endDate) {
        this(name);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the name of the era.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the start date.
     *
     * @return start date (fuzzy)
     */
    public FuzzyDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the end date.
     *
     * @return end date (fuzzy)
     */
    public FuzzyDate getEndDate() {
        return endDate;
    }

    /**
     * Returns the description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the region.
     *
     * @return region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Returns an unmodifiable list of key events.
     *
     * @return key events
     */
    public List<HistoricalEvent> getKeyEvents() {
        return Collections.unmodifiableList(keyEvents);
    }

    /**
     * Sets the start date.
     *
     * @param date start date
     */
    public void setStartDate(FuzzyDate date) {
        this.startDate = date;
    }

    /**
     * Sets the end date.
     *
     * @param date end date
     */
    public void setEndDate(FuzzyDate date) {
        this.endDate = date;
    }

    /**
     * Sets the description.
     *
     * @param desc description
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    /**
     * Sets the region.
     *
     * @param region region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Adds a key event to this era.
     *
     * @param event the event to add
     * @throws NullPointerException if event is null
     */
    public void addKeyEvent(HistoricalEvent event) {
        keyEvents.add(Objects.requireNonNull(event, "HistoricalEvent cannot be null"));
    }

    /**
     * Returns the approximate duration in years.
     * 
     * @return duration in years
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoricalEra that)) return false;
        return Objects.equals(name, that.name) && 
               Objects.equals(startDate, that.startDate) && 
               Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startDate, region);
    }

    @Override
    public String toString() {
        return String.format("Era '%s' (%s to %s)", name,
                startDate != null ? startDate : "unknown",
                endDate != null ? endDate : "unknown");
    }

    // Static factories for common eras

    public static HistoricalEra renaissance() {
        HistoricalEra e = new HistoricalEra("Renaissance",
                FuzzyDate.circa(1400), FuzzyDate.circa(1600));
        e.setRegion("Europe");
        e.setDescription("Cultural movement emphasizing humanism and arts");
        return e;
    }

    public static HistoricalEra industrialRevolution() {
        HistoricalEra e = new HistoricalEra("Industrial Revolution",
                FuzzyDate.circa(1760), FuzzyDate.circa(1840));
        e.setRegion("Britain, Europe");
        e.setDescription("Transition to machine manufacturing");
        return e;
    }

    public static HistoricalEra ancientGreece() {
        HistoricalEra e = new HistoricalEra("Ancient Greece",
                FuzzyDate.circaBce(800), FuzzyDate.circaBce(31));
        e.setRegion("Greece, Mediterranean");
        e.setDescription("Classical Greek civilization");
        return e;
    }

    public static HistoricalEra romanEmpire() {
        HistoricalEra e = new HistoricalEra("Roman Empire",
                FuzzyDate.bce(27), FuzzyDate.of(476));
        e.setRegion("Mediterranean, Europe");
        e.setDescription("Ancient Roman imperial period");
        return e;
    }

    public static HistoricalEra middleAges() {
        HistoricalEra e = new HistoricalEra("Middle Ages",
                FuzzyDate.circa(500), FuzzyDate.circa(1500));
        e.setRegion("Europe");
        e.setDescription("Medieval period in European history");
        return e;
    }
}
