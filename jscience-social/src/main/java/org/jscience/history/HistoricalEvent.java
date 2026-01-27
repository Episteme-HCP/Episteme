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

import java.util.Objects;
import org.jscience.earth.Place;
import org.jscience.history.time.TimeCoordinate;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a significant historical event with support for imprecise dating and duration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class HistoricalEvent extends Event implements org.jscience.util.Positioned<Place> {

    private static final long serialVersionUID = 4L;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final TimeCoordinate endDate;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Place location;

    public HistoricalEvent(String name, String description, TimeCoordinate startDate, TimeCoordinate endDate,
            EventCategory category, Place location) {
        super(name, description, startDate, category);
        this.endDate = endDate != null ? endDate : startDate;
        this.location = location;
    }

    public HistoricalEvent(String name, TimeCoordinate date, EventCategory category) {
        this(name, null, date, date, category, null);
    }

    public TimeCoordinate getStartDate() {
        return when;
    }

    public TimeCoordinate getEndDate() {
        return endDate;
    }

    @Override
    public Place getPosition() {
        return location;
    }

    public boolean overlaps(HistoricalEvent other) {
        Objects.requireNonNull(other, "Other event cannot be null");
        return when.compareTo(other.endDate) <= 0 &&
                endDate.compareTo(other.when) >= 0;
    }

    @Override
    public String toString() {
        if (when.equals(endDate)) {
            return String.format("%s (%s) - %s", getName(), when, category);
        }
        return String.format("%s (%s to %s) - %s", getName(), when, endDate, category);
    }
}
