package org.jscience.history.calendars;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;
import org.jscience.history.temporal.Precision;
import org.jscience.history.temporal.TemporalCoordinate;

/**
 * A temporal coordinate based on an {@link AlternateCalendar} system.
 * Allows for conversion between different calendar systems.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class CalendarCoordinate implements TemporalCoordinate, Serializable {

    private static final long serialVersionUID = 2L;

    private final AlternateCalendar calendar;
    private final Precision precision;

    public CalendarCoordinate(AlternateCalendar calendar, Precision precision) {
        this.calendar = Objects.requireNonNull(calendar, "Calendar cannot be null");
        this.precision = precision != null ? precision : Precision.DAY;
    }

    public CalendarCoordinate(AlternateCalendar calendar) {
        this(calendar, Precision.DAY);
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
    public Precision getPrecision() {
        return precision;
    }

    @Override
    public boolean isFuzzy() {
        return precision.ordinal() > Precision.DAY.ordinal();
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
    public int compareTo(TemporalCoordinate o) {
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
