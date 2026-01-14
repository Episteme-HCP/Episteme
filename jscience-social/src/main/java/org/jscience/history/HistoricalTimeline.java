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

import java.util.*;

/**
 * A timeline representing a sequence of historical events with FuzzyDate
 * support.
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HistoricalTimeline {

    private final String name;
    private final List<HistoricalEvent> events = new ArrayList<>();

    public HistoricalTimeline(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public HistoricalTimeline addEvent(HistoricalEvent event) {
        events.add(event);
        return this;
    }

    public List<HistoricalEvent> getEvents() {
        return events.stream()
                .sorted(Comparator.comparing(HistoricalEvent::getStartDate))
                .toList();
    }

    public List<HistoricalEvent> getEventsBetween(FuzzyDate start, FuzzyDate end) {
        return events.stream()
                .filter(e -> e.getStartDate().compareTo(start) >= 0 &&
                        e.getEndDate().compareTo(end) <= 0)
                .sorted(Comparator.comparing(HistoricalEvent::getStartDate))
                .toList();
    }

    public List<HistoricalEvent> getEventsByCategory(HistoricalEvent.Category category) {
        return events.stream()
                .filter(e -> e.getCategory() == category)
                .sorted(Comparator.comparing(HistoricalEvent::getStartDate))
                .toList();
    }

    /**
     * Gets events in BCE period.
     */
    public List<HistoricalEvent> getBceEvents() {
        return events.stream()
                .filter(e -> e.getStartDate().isBce())
                .sorted(Comparator.comparing(HistoricalEvent::getStartDate))
                .toList();
    }

    public Optional<HistoricalEvent> getEarliestEvent() {
        return events.stream().min(Comparator.comparing(HistoricalEvent::getStartDate));
    }

    public Optional<HistoricalEvent> getLatestEvent() {
        return events.stream().max(Comparator.comparing(HistoricalEvent::getEndDate));
    }

    /**
     * Returns approximate time span in years.
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
    public String toString() {
        return String.format("HistoricalTimeline '%s' with %d events", name, events.size());
    }

    // Factory for common timelines
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

    public static HistoricalTimeline ancientHistory() {
        return new HistoricalTimeline("Ancient History")
                .addEvent(HistoricalEvent.GREAT_PYRAMID)
                .addEvent(HistoricalEvent.FOUNDING_OF_ROME)
                .addEvent(HistoricalEvent.BATTLE_OF_MARATHON)
                .addEvent(HistoricalEvent.FALL_OF_ROME);
    }
}


