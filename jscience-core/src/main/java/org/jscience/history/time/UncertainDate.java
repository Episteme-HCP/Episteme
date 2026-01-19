package org.jscience.history.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import org.jscience.mathematics.algebra.Interval;

/**
 * Uncertain Date (Probabilistic Date).
 * Represents a historical time interval in which an event likely occurred.
 * Uses ChronoLocalDate for calendar-based historical analysis.
 */
public class UncertainDate implements Interval<ChronoLocalDate>, Comparable<UncertainDate> {

    private final LocalDate earliest; 
    private final LocalDate latest;   
    private final boolean closedLeft;
    private final boolean closedRight;

    public UncertainDate(LocalDate exact) {
        this(exact, exact, true, true);
    }

    public UncertainDate(LocalDate earliest, LocalDate latest) {
        this(earliest, latest, true, true);
    }

    public UncertainDate(Instant exact) {
        this(LocalDate.ofInstant(exact, ZoneId.of("UTC")));
    }

    public UncertainDate(Instant earliest, Instant latest) {
        this(LocalDate.ofInstant(earliest, ZoneId.of("UTC")), 
             LocalDate.ofInstant(latest, ZoneId.of("UTC")));
    }
    
    public UncertainDate(LocalDate earliest, LocalDate latest, boolean closedLeft, boolean closedRight) {
        if (earliest != null && latest != null && earliest.isAfter(latest)) {
             throw new IllegalArgumentException("Earliest date must be before latest date");
        }
        this.earliest = earliest;
        this.latest = latest;
        this.closedLeft = closedLeft;
        this.closedRight = closedRight;
    }

    public static UncertainDate between(LocalDate start, LocalDate end) {
        return new UncertainDate(start, end);
    }

    public static UncertainDate circa(int year) {
        return new UncertainDate(LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31));
    }

    public static UncertainDate certain(int year, int month, int day) {
        return new UncertainDate(LocalDate.of(year, month, day));
    }

    public LocalDate getEarliestPossible() { return earliest; }
    public LocalDate getLatestPossible() { return latest; }

    @Override public int getDimension() { return 1; }
    @Override public ChronoLocalDate getMin(int dimension) { return (ChronoLocalDate) earliest; }
    @Override public ChronoLocalDate getMax(int dimension) { return (ChronoLocalDate) latest; }
    @Override public boolean isClosedLeft(int dimension) { return closedLeft; }
    @Override public boolean isClosedRight(int dimension) { return closedRight; }

    @Override
    public boolean contains(ChronoLocalDate[] point) {
        if (point.length != 1) return false;
        ChronoLocalDate p = point[0];
        if (p == null) return false;
        boolean afterMin = closedLeft ? !p.isBefore(earliest) : p.isAfter(earliest);
        boolean beforeMax = closedRight ? !p.isAfter(latest) : p.isBefore(latest);
        return afterMin && beforeMax;
    }

    @Override
    public boolean contains(Interval<ChronoLocalDate> other) {
        if (other.getDimension() != 1) return false;
        return this.contains(new ChronoLocalDate[]{other.getMin(0)}) && this.contains(new ChronoLocalDate[]{other.getMax(0)});
    }

    @Override
    public boolean overlaps(Interval<ChronoLocalDate> other) {
        if (other.getDimension() != 1) return false;
        ChronoLocalDate oMin = other.getMin(0);
        ChronoLocalDate oMax = other.getMax(0);
        // Note: isBefore/isAfter on ChronoLocalDate
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
        return this.earliest.compareTo(other.earliest);
    }
}
