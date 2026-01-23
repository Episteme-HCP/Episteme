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

package org.jscience.history.time;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.jscience.mathematics.algebra.Interval;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a historical time interval where an event likely occurred, capturing chronological uncertainty.
 * Implementing {@link Interval}, it allows for set-theoretic operations like intersection and bounding.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class UncertainDate implements Interval<ChronoLocalDate>, Comparable<UncertainDate>, Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute
    private final LocalDate earliest;

    @Attribute
    private final LocalDate latest;

    @Attribute
    private final boolean closedLeft;

    @Attribute
    private final boolean closedRight;

    /**
     * Creates an exact UncertainDate (interval of zero width).
     *
     * @param exact the precise date
     * @throws NullPointerException if exact is null
     */
    public UncertainDate(LocalDate exact) {
        this(exact, exact, true, true);
    }

    /**
     * Creates an UncertainDate within a specific range.
     *
     * @param earliest the start of the interval
     * @param latest the end of the interval
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if earliest is after latest
     */
    public UncertainDate(LocalDate earliest, LocalDate latest) {
        this(earliest, latest, true, true);
    }

    /**
     * Creates an exact UncertainDate from an Instant.
     *
     * @param exact the precise moment
     * @throws NullPointerException if exact is null
     */
    public UncertainDate(Instant exact) {
        this(LocalDate.ofInstant(Objects.requireNonNull(exact, "Instant cannot be null"), ZoneId.of("UTC")));
    }

    /**
     * Creates an UncertainDate from a range of instants.
     *
     * @param earliest start moment
     * @param latest end moment
     */
    public UncertainDate(Instant earliest, Instant latest) {
        this(LocalDate.ofInstant(Objects.requireNonNull(earliest, "Earliest instant cannot be null"), ZoneId.of("UTC")), 
             LocalDate.ofInstant(Objects.requireNonNull(latest, "Latest instant cannot be null"), ZoneId.of("UTC")));
    }
    
    /**
     * Fully parameterized constructor for an UncertainDate interval.
     * 
     * @param earliest start date
     * @param latest end date
     * @param closedLeft if true, the start date is included in the interval
     * @param closedRight if true, the end date is included in the interval
     * @throws NullPointerException if earliest or latest is null
     * @throws IllegalArgumentException if earliest is after latest
     */
    public UncertainDate(LocalDate earliest, LocalDate latest, boolean closedLeft, boolean closedRight) {
        this.earliest = Objects.requireNonNull(earliest, "Earliest date cannot be null");
        this.latest = Objects.requireNonNull(latest, "Latest date cannot be null");
        if (earliest.isAfter(latest)) {
             throw new IllegalArgumentException("Earliest date [" + earliest + "] must be before or equal to latest date [" + latest + "]");
        }
        this.closedLeft = closedLeft;
        this.closedRight = closedRight;
    }

    /**
     * Static factory for an interval between two dates.
     */
    public static UncertainDate between(LocalDate start, LocalDate end) {
        return new UncertainDate(start, end);
    }

    /**
     * Static factory for a date "circa" a specific year (encompassing the whole year).
     */
    public static UncertainDate circa(int year) {
        return new UncertainDate(LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31));
    }

    /**
     * Static factory for a certain specific date.
     */
    public static UncertainDate certain(int year, int month, int day) {
        return new UncertainDate(LocalDate.of(year, month, day));
    }

    public LocalDate getEarliestPossible() { return earliest; }
    public LocalDate getLatestPossible() { return latest; }

    @Override public int getDimension() { return 1; }
    @Override public ChronoLocalDate getMin(int dimension) { return earliest; }
    @Override public ChronoLocalDate getMax(int dimension) { return latest; }
    @Override public boolean isClosedLeft(int dimension) { return closedLeft; }
    @Override public boolean isClosedRight(int dimension) { return closedRight; }

    @Override
    public boolean contains(ChronoLocalDate[] point) {
        if (point == null || point.length != 1 || point[0] == null) return false;
        ChronoLocalDate p = point[0];
        boolean afterMin = closedLeft ? !p.isBefore(earliest) : p.isAfter(earliest);
        boolean beforeMax = closedRight ? !p.isAfter(latest) : p.isBefore(latest);
        return afterMin && beforeMax;
    }

    @Override
    public boolean contains(Interval<ChronoLocalDate> other) {
        Objects.requireNonNull(other, "Interval to check cannot be null");
        if (other.getDimension() != 1) return false;
        return this.contains(new ChronoLocalDate[]{other.getMin(0)}) && this.contains(new ChronoLocalDate[]{other.getMax(0)});
    }

    @Override
    public boolean overlaps(Interval<ChronoLocalDate> other) {
        Objects.requireNonNull(other, "Interval to check cannot be null");
        if (other.getDimension() != 1) return false;
        ChronoLocalDate oMin = other.getMin(0);
        ChronoLocalDate oMax = other.getMax(0);
        return !(latest.isBefore(oMin) || earliest.isAfter(oMax));
    }

    @Override
    public Interval<ChronoLocalDate> intersection(Interval<ChronoLocalDate> other) {
        if (!overlaps(other)) return null;
        ChronoLocalDate oMin = other.getMin(0);
        ChronoLocalDate oMax = other.getMax(0);
        LocalDate newMin = earliest.isAfter(oMin) ? earliest : (LocalDate) oMin;
        LocalDate newMax = latest.isBefore(oMax) ? latest : (LocalDate) oMax;
        return new UncertainDate(newMin, newMax);
    }

    @Override
    public Interval<ChronoLocalDate> boundingInterval(Interval<ChronoLocalDate> other) {
        Objects.requireNonNull(other, "Other interval cannot be null");
        ChronoLocalDate oMin = other.getMin(0);
        ChronoLocalDate oMax = other.getMax(0);
        LocalDate newMin = earliest.isBefore(oMin) ? earliest : (LocalDate) oMin;
        LocalDate newMax = latest.isAfter(oMax) ? latest : (LocalDate) oMax;
        return new UncertainDate(newMin, newMax);
    }

    @Override public boolean isEmpty() { return earliest == null; }
    
    @Override
    public String notation() {
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
        return (closedLeft ? "[" : "(") + earliest.format(fmt) + ", " + latest.format(fmt) + (closedRight ? "]" : ")");
    }

    @Override
    public String toString() {
        if (earliest.equals(latest)) return earliest.toString();
        return notation();
    }

    @Override
    public int compareTo(UncertainDate other) {
        Objects.requireNonNull(other, "Date candidate for comparison cannot be null");
        return this.earliest.compareTo(other.earliest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UncertainDate that = (UncertainDate) o;
        return closedLeft == that.closedLeft &&
                closedRight == that.closedRight &&
                Objects.equals(earliest, that.earliest) &&
                Objects.equals(latest, that.latest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(earliest, latest, closedLeft, closedRight);
    }
}
