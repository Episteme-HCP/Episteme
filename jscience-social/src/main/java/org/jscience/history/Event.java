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

/**
 * Represents a historical event with date range and categorization.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Event {

    public enum Category {
        POLITICAL, MILITARY, CULTURAL, SCIENTIFIC, ECONOMIC, RELIGIOUS, NATURAL
    }

    private final String name;
    private final String description;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Category category;
    private final String location;

    public Event(String name, String description, LocalDate startDate, LocalDate endDate,
            Category category, String location) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.location = location;
    }

    public Event(String name, LocalDate date, Category category) {
        this(name, null, date, date, category, null);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Category getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public Period getDuration() {
        return Period.between(startDate, endDate);
    }

    public boolean overlaps(Event other) {
        return !startDate.isAfter(other.endDate) && !endDate.isBefore(other.startDate);
    }

    @Override
    public String toString() {
        if (startDate.equals(endDate)) {
            return String.format("%s (%s) - %s", name, startDate, category);
        }
        return String.format("%s (%s to %s) - %s", name, startDate, endDate, category);
    }

    // Notable historical events
    public static final Event FRENCH_REVOLUTION = new Event("French Revolution",
            "Political revolution in France", LocalDate.of(1789, 7, 14),
            LocalDate.of(1799, 11, 9), Category.POLITICAL, "France");

    public static final Event WORLD_WAR_I = new Event("World War I",
            "Global military conflict", LocalDate.of(1914, 7, 28),
            LocalDate.of(1918, 11, 11), Category.MILITARY, "Global");

    public static final Event WORLD_WAR_II = new Event("World War II",
            "Global military conflict", LocalDate.of(1939, 9, 1),
            LocalDate.of(1945, 9, 2), Category.MILITARY, "Global");

    public static final Event MOON_LANDING = new Event("Apollo 11 Moon Landing",
            LocalDate.of(1969, 7, 20), Category.SCIENTIFIC);
}
