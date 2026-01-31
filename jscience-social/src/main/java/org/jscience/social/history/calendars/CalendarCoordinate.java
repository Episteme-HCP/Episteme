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

package org.jscience.social.history.calendars;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;
import org.jscience.social.history.time.TimePrecision;
import org.jscience.social.history.time.TimeCoordinate;

/**
 * A temporal coordinate based on an {@link AlternateCalendar} system.
 * Allows for conversion between different calendar systems.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class CalendarCoordinate implements TimeCoordinate, Serializable {

    private static final long serialVersionUID = 2L;

    private final AlternateCalendar calendar;
    private final TimePrecision precision;

    public CalendarCoordinate(AlternateCalendar calendar, TimePrecision precision) {
        this.calendar = Objects.requireNonNull(calendar, "Calendar cannot be null");
        this.precision = precision != null ? precision : TimePrecision.DAY;
    }

    public CalendarCoordinate(AlternateCalendar calendar) {
        this(calendar, TimePrecision.DAY);
    }

    public AlternateCalendar getCalendar() {
        return calendar;
    }

    @Override
    public Instant toInstant() {
        // Convert to Rata Die, then to Gregorian, then to java.time.Instant
        GregorianCalendar greg = new GregorianCalendar(calendar.toRD());
        return LocalDate.of(greg.getYear(), greg.getMonth(), greg.getDay())
                .atStartOfDay(ZoneOffset.UTC).toInstant();
    }

    @Override
    public TimePrecision getPrecision() {
        return precision;
    }

    @Override
    public boolean isFuzzy() {
        return precision.ordinal() > TimePrecision.DAY.ordinal();
    }

    /**
     * Converts this coordinate to another calendar system.
     * 
     * @param <C> the type of calendar
     * @param targetCalendar the target calendar class
     * @return the coordinate in the new system
     */
    public <C extends AlternateCalendar> CalendarCoordinate toCalendar(Class<C> targetCalendar) {
        try {
            C target = targetCalendar.getDeclaredConstructor(long.class).newInstance(calendar.toRD());
            return new CalendarCoordinate(target, precision);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert calendar: " + targetCalendar.getSimpleName(), e);
        }
    }

    @Override
    public int compareTo(TimeCoordinate o) {
        return this.toInstant().compareTo(o.toInstant());
    }

    @Override
    public String toString() {
        return calendar.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalendarCoordinate other)) return false;
        return calendar.toRD() == other.calendar.toRD() && precision == other.precision;
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendar.toRD(), precision);
    }
}

