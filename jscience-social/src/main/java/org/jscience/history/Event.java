package org.jscience.history;

import org.jscience.history.time.UncertainDate;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.jscience.util.Temporal;

/**
 * A class representing a historical event.
 * Uses UncertainDate to handle potential fuzziness in dating.
 */
public class Event implements Comparable<Event>, Temporal {

    private final UncertainDate date;
    private final String happening;

    /**
     * Creates a new Event object for a specific date (UTC).
     */
    public Event(LocalDate date, String happening) {
        this(date.atStartOfDay(ZoneId.of("UTC")).toInstant(), happening);
    }
    
    /**
     * Creates a new Event object for a specific instant (Point in time).
     */
    public Event(Instant exact, String happening) {
        this.date = new UncertainDate(exact);
        this.happening = happening;
    }

    /**
     * Creates a new Event object with a duration or uncertain range.
     */
    public Event(Instant start, Instant end, String happening) {
        this.date = new UncertainDate(start, end != null ? end : start);
        this.happening = happening;
    }
    
    /**
     * Creates a new Event with an UncertainDate.
     */
    public Event(UncertainDate date, String happening) {
        this.date = date;
        this.happening = happening;
    }

    public UncertainDate getDate() {
        return date;
    }
    
    // Convenience for backward compatibility or simple access
    public Instant getStart() {
        LocalDate min = (LocalDate) date.getMin(0);
        return min.atStartOfDay(ZoneId.of("UTC")).toInstant();
    }

    public Instant getEnd() {
        LocalDate max = (LocalDate) date.getMax(0);
        return max.atStartOfDay(ZoneId.of("UTC")).toInstant();
    }

    @Override
    public Instant getTimestamp() {
        return getStart();
    }

    public String getHappening() {
        return happening;
    }

    public boolean happensBefore(Event event) {
        // Strict before: this.max < event.min
        return this.date.getMax(0).isBefore(event.date.getMin(0));
    }

    public boolean happensAfter(Event event) {
        // Strict after: this.min > event.max
        return this.date.getMin(0).isAfter(event.date.getMax(0));
    }
    
    public boolean overlaps(Event event) {
        return this.date.overlaps(event.date);
    }
    
    public boolean contains(Event event) {
        return this.date.contains(event.date);
    }

    @Override
    public int compareTo(Event o) {
        // Compare by earliest possible start time
        return this.date.getMin(0).compareTo(o.date.getMin(0));
    }
    
    @Override
    public String toString() {
        return date.toString() + ": " + happening;
    }
}
