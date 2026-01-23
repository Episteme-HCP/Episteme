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

import org.jscience.history.time.UncertainDate;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;
import org.jscience.util.Temporal;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * A class representing a historical event.
 * Uses {@link UncertainDate} to handle potential fuzziness in dating.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Event implements Comparable<Event>, Temporal, Serializable {

    private static final long serialVersionUID = 1L;

    /** The date of the event, possibly uncertain. */
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final UncertainDate date;

    /** A description of what happened. */
    @Attribute
    private final String happening;

    /**
     * Creates a new Event object for a specific date (UTC).
     * 
     * @param date the date of the event
     * @param happening description of the event
     * @throws NullPointerException if date or happening is null
     */
    public Event(LocalDate date, String happening) {
        this(new UncertainDate(Objects.requireNonNull(date, "Date cannot be null")), happening);
    }
    
    /**
     * Creates a new Event object for a specific instant (Point in time).
     * 
     * @param exact the exact instant of the event
     * @param happening description of the event
     * @throws NullPointerException if exact or happening is null
     */
    public Event(Instant exact, String happening) {
        this(new UncertainDate(Objects.requireNonNull(exact, "Exact instant cannot be null")), happening);
    }

    /**
     * Creates a new Event object with a duration or uncertain range.
     * 
     * @param start the start of the interval
     * @param end the end of the interval (defaults to start if null)
     * @param happening description of the event
     * @throws NullPointerException if start or happening is null
     */
    public Event(Instant start, Instant end, String happening) {
        this(new UncertainDate(Objects.requireNonNull(start, "Start instant cannot be null"), 
                end != null ? end : start), happening);
    }
    
    /**
     * Creates a new Event with an UncertainDate.
     * 
     * @param date the uncertain date
     * @param happening description of the event
     * @throws NullPointerException if date or happening is null
     */
    public Event(UncertainDate date, String happening) {
        this.date = Objects.requireNonNull(date, "UncertainDate cannot be null");
        this.happening = Objects.requireNonNull(happening, "Happening description cannot be null");
    }

    /**
     * Returns the date of the event.
     *
     * @return the date
     */
    public UncertainDate getDate() {
        return date;
    }
    
    /**
     * Returns the start instant of the event.
     *
     * @return the start instant
     */
    public Instant getStart() {
        var start = date.getEarliestOfMidPoints();
        if (start != null) return start;
        
        var min = date.getEarliestPossible();
        if (min != null) return min;
        
        // Fallback to LocalDate if Instant is not available
        var ld = (LocalDate) date.getMin(0);
        return ld.atStartOfDay(ZoneOffset.UTC).toInstant();
    }

    /**
     * Returns the end instant of the event.
     *
     * @return the end instant
     */
    public Instant getEnd() {
        var end = date.getLatestOfMidPoints();
        if (end != null) return end;
        
        var max = date.getLatestPossible();
        if (max != null) return max;
        
        // Fallback to LocalDate if Instant is not available
        var ld = (LocalDate) date.getMax(0);
        return ld.atStartOfDay(ZoneOffset.UTC).toInstant();
    }

    @Override
    public Instant getTimestamp() {
        return getStart();
    }

    /**
     * Returns the description of the event.
     *
     * @return description
     */
    public String getHappening() {
        return happening;
    }

    /**
     * Checks if this event happens strictly before another event.
     *
     * @param event the other event
     * @return true if this happens before event
     * @throws NullPointerException if event is null
     */
    public boolean happensBefore(Event event) {
        Objects.requireNonNull(event, "Other event cannot be null");
        return this.date.getMax(0).isBefore(event.date.getMin(0));
    }

    /**
     * Checks if this event happens strictly after another event.
     *
     * @param event the other event
     * @return true if this happens after event
     * @throws NullPointerException if event is null
     */
    public boolean happensAfter(Event event) {
        Objects.requireNonNull(event, "Other event cannot be null");
        return this.date.getMin(0).isAfter(event.date.getMax(0));
    }
    
    /**
     * Checks if this event's date range overlaps with another event's.
     *
     * @param event the other event
     * @return true if they overlap
     * @throws NullPointerException if event is null
     */
    public boolean overlaps(Event event) {
        Objects.requireNonNull(event, "Other event cannot be null");
        return this.date.overlaps(event.date);
    }
    
    /**
     * Checks if this event's date range contains another event's.
     *
     * @param event the other event
     * @return true if this contains event
     * @throws NullPointerException if event is null
     */
    public boolean contains(Event event) {
        Objects.requireNonNull(event, "Other event cannot be null");
        return this.date.contains(event.date);
    }

    @Override
    public int compareTo(Event o) {
        return this.date.getMin(0).compareTo(o.date.getMin(0));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event other)) return false;
        return Objects.equals(date, other.date) && 
               Objects.equals(happening, other.happening);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, happening);
    }

    @Override
    public String toString() {
        return date.toString() + ": " + happening;
    }
}
