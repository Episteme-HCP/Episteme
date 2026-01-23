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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * A timeline representing a sequence of historical events with {@link FuzzyDate} support.
 * Provides analytical methods to filter and explore historical periods.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class HistoricalTimeline implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The display name of the timeline. */
    @Attribute
    private final String name;

    /** The collection of events in this timeline. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<HistoricalEvent> events = new ArrayList<>();

    /**
     * Creates a new HistoricalTimeline.
     * 
     * @param name the name of the timeline
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is blank
     */
    public HistoricalTimeline(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }

    /**
     * Returns the name of the timeline.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds an event to the timeline.
     *
     * @param event the event to add
     * @return this timeline for chaining
     * @throws NullPointerException if event is null
     */
    public HistoricalTimeline addEvent(HistoricalEvent event) {
        events.add(Objects.requireNonNull(event, "HistoricalEvent cannot be null"));
        return this;
    }

    /**
     * Returns all events in this timeline, sorted by start date.
     *
     * @return unmodifiable sorted list of events
     */
    public List<HistoricalEvent> getEvents() {
        return events.stream()
                .sorted(Comparator.comparing(HistoricalEvent::getStartDate))
                .toList();
    }

    /**
     * Returns an unmodifiable list of raw events.
     * 
     * @return the internal events list
     */
    public List<HistoricalEvent> getRawEvents() {
        return Collections.unmodifiableList(events);
    }

    /**
     * Returns events occurring between two fuzzy dates.
     * 
     * @param start start of period
     * @param end end of period
     * @return filtered unmodifiable list of events
     * @throws NullPointerException if start or end is null
     */
    public List<HistoricalEvent> getEventsBetween(FuzzyDate start, FuzzyDate end) {
        Objects.requireNonNull(start, "Start date cannot be null");
        Objects.requireNonNull(end, "End date cannot be null");
        return events.stream()
                .filter(e -> e.getStartDate().compareTo(start) >= 0 &&
                        e.getEndDate().compareTo(end) <= 0)
                .sorted(Comparator.comparing(HistoricalEvent::getStartDate))
                .toList();
    }

    /**
     * Returns events in a specific category.
     * 
     * @param category the category to filter by
     * @return filtered unmodifiable list of events
     * @throws NullPointerException if category is null
     */
    public List<HistoricalEvent> getEventsByCategory(HistoricalEvent.Category category) {
        Objects.requireNonNull(category, "Category cannot be null");
        return events.stream()
                .filter(e -> e.getCategory() == category)
                .sorted(Comparator.comparing(HistoricalEvent::getStartDate))
                .toList();
    }

    /**
     * Returns events occurring in the BCE (Before Common Era) period.
     *
     * @return unmodifiable list of BCE events
     */
    public List<HistoricalEvent> getBceEvents() {
        return events.stream()
                .filter(e -> e.getStartDate().isBce())
                .sorted(Comparator.comparing(HistoricalEvent::getStartDate))
                .toList();
    }

    /**
     * Returns the earliest event in the timeline.
     *
     * @return optional earliest event
     */
    public Optional<HistoricalEvent> getEarliestEvent() {
        return events.stream().min(Comparator.comparing(HistoricalEvent::getStartDate));
    }

    /**
     * Returns the latest event in the timeline.
     *
     * @return optional latest event
     */
    public Optional<HistoricalEvent> getLatestEvent() {
        return events.stream().max(Comparator.comparing(HistoricalEvent::getEndDate));
    }

    /**
     * Returns the approximate total time span of this timeline in years.
     * 
     * @return time span in years
     */
    public int getTimeSpanYears() {
        Optional<HistoricalEvent> earliest = getEarliestEvent();
        Optional<HistoricalEvent> latest = getLatestEvent();
        if (earliest.isPresent() && latest.isPresent()) {
            Integer startYear = earliest.get().getStartDate().getYear();
            Integer endYear = latest.get().getEndDate().getYear();
            if (startYear != null && endYear != null) {
                int start = earliest.get().getStartDate().isBce() ? -startYear : startYear;
                int end = latest.get().getEndDate().isBce() ? -endYear : endYear;
                return end - start;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoricalTimeline that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(events, that.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, events);
    }

    @Override
    public String toString() {
        return String.format("HistoricalTimeline '%s' with %d events", name, events.size());
    }

    /**
     * Factory method creating a timeline of major world history events.
     *
     * @return world history timeline
     */
    public static HistoricalTimeline worldHistory() {
        return new HistoricalTimeline("World History")
                .addEvent(HistoricalEvent.GREAT_PYRAMID)
                .addEvent(HistoricalEvent.FOUNDING_OF_ROME)
                .addEvent(HistoricalEvent.BATTLE_OF_MARATHON)
                .addEvent(HistoricalEvent.FALL_OF_ROME)
                .addEvent(HistoricalEvent.FRENCH_REVOLUTION)
                .addEvent(HistoricalEvent.WORLD_WAR_I)
                .addEvent(HistoricalEvent.WORLD_WAR_II)
                .addEvent(HistoricalEvent.MOON_LANDING);
    }

    /**
     * Factory method creating a timeline of major ancient history events.
     *
     * @return ancient history timeline
     */
    public static HistoricalTimeline ancientHistory() {
        return new HistoricalTimeline("Ancient History")
                .addEvent(HistoricalEvent.GREAT_PYRAMID)
                .addEvent(HistoricalEvent.FOUNDING_OF_ROME)
                .addEvent(HistoricalEvent.BATTLE_OF_MARATHON)
                .addEvent(HistoricalEvent.FALL_OF_ROME);
    }
}
