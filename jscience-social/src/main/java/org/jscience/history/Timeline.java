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

import java.time.*;
import java.util.*;

/**
 * A timeline representing a sequence of historical events.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Timeline {

    private final String name;
    private final List<Event> events = new ArrayList<>();

    public Timeline(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Timeline addEvent(Event event) {
        events.add(event);
        return this;
    }

    public List<Event> getEvents() {
        return events.stream()
                .sorted(Comparator.comparing(Event::getStartDate))
                .toList();
    }

    public List<Event> getEventsBetween(LocalDate start, LocalDate end) {
        return events.stream()
                .filter(e -> !e.getStartDate().isBefore(start) && !e.getEndDate().isAfter(end))
                .sorted(Comparator.comparing(Event::getStartDate))
                .toList();
    }

    public List<Event> getEventsByCategory(Event.Category category) {
        return events.stream()
                .filter(e -> e.getCategory() == category)
                .sorted(Comparator.comparing(Event::getStartDate))
                .toList();
    }

    public Optional<Event> getEarliestEvent() {
        return events.stream().min(Comparator.comparing(Event::getStartDate));
    }

    public Optional<Event> getLatestEvent() {
        return events.stream().max(Comparator.comparing(Event::getEndDate));
    }

    public Period getTimeSpan() {
        Optional<Event> earliest = getEarliestEvent();
        Optional<Event> latest = getLatestEvent();
        if (earliest.isPresent() && latest.isPresent()) {
            return Period.between(earliest.get().getStartDate(), latest.get().getEndDate());
        }
        return Period.ZERO;
    }

    @Override
    public String toString() {
        return String.format("Timeline '%s' with %d events", name, events.size());
    }

    // Factory for common timelines
    public static Timeline worldHistory() {
        return new Timeline("World History")
                .addEvent(Event.FRENCH_REVOLUTION)
                .addEvent(Event.WORLD_WAR_I)
                .addEvent(Event.WORLD_WAR_II)
                .addEvent(Event.MOON_LANDING);
    }
}