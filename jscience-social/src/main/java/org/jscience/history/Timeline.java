/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * A timeline representing a sequence of historical events.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Timeline {

    private final String name;
    private final List<HistoricalEvent> events = new ArrayList<>();

    public Timeline(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Timeline addEvent(HistoricalEvent event) {
        events.add(event);
        return this;
    }

    public List<HistoricalEvent> getEvents() {
        return events.stream()
                .sorted(Comparator.comparing((HistoricalEvent e) -> e.getStartDate().getYear())) // Simplified sort for
                                                                                                 // now
                .toList();
    }

    public List<HistoricalEvent> getEventsBetween(org.jscience.history.FuzzyDate start,
            org.jscience.history.FuzzyDate end) {
        return events.stream()
                .filter(e -> e.getStartDate().compareTo(start) >= 0 && e.getEndDate().compareTo(end) <= 0)
                .sorted(Comparator.comparing(e -> e.getStartDate().getYear()))
                .toList();
    }

    public List<HistoricalEvent> getEventsByCategory(HistoricalEvent.Category category) {
        return events.stream()
                .filter(e -> e.getCategory() == category)
                .sorted(Comparator.comparing(e -> e.getStartDate().getYear()))
                .toList();
    }

    public Optional<HistoricalEvent> getEarliestEvent() {
        return events.stream().min(Comparator.comparing(e -> e.getStartDate().getYear()));
    }

    public Optional<HistoricalEvent> getLatestEvent() {
        return events.stream().max(Comparator.comparing(e -> e.getEndDate().getYear()));
    }

    // Simplified time span logic as FuzzyDate doesn't support Period easily yet
    public int getTimeSpanYears() {
        Optional<HistoricalEvent> earliest = getEarliestEvent();
        Optional<HistoricalEvent> latest = getLatestEvent();
        if (earliest.isPresent() && latest.isPresent()) {
            int start = earliest.get().getStartDate().getYear();
            if (earliest.get().getStartDate().isBce())
                start = -start;
            int end = latest.get().getEndDate().getYear();
            if (latest.get().getEndDate().isBce())
                end = -end;
            return end - start;
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Timeline '%s' with %d events", name, events.size());
    }

    // Factory for common timelines
    public static Timeline worldHistory() {
        return new Timeline("World History")
                .addEvent(HistoricalEvent.FRENCH_REVOLUTION)
                .addEvent(HistoricalEvent.WORLD_WAR_I)
                .addEvent(HistoricalEvent.WORLD_WAR_II)
                .addEvent(HistoricalEvent.MOON_LANDING);
    }
}
