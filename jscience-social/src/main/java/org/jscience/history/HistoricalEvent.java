package org.jscience.history;

import java.io.Serializable;
import java.util.Objects;
import org.jscience.geography.Place;
import org.jscience.history.temporal.TemporalCoordinate;
import org.jscience.util.Positioned;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a significant historical event with support for imprecise dating and duration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class HistoricalEvent extends Event implements Positioned, Serializable {

    private static final long serialVersionUID = 4L;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final TemporalCoordinate endDate;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Place location;

    public HistoricalEvent(String name, String description, TemporalCoordinate startDate, TemporalCoordinate endDate,
            Event.Category category, Place location) {
        super(name, description, startDate, category);
        this.endDate = endDate != null ? endDate : startDate;
        this.location = location;
    }

    public HistoricalEvent(String name, TemporalCoordinate date, Event.Category category) {
        this(name, null, date, date, category, null);
    }

    public TemporalCoordinate getStartDate() {
        return when;
    }

    public TemporalCoordinate getEndDate() {
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
            return String.format("%s (%s) - %s", name, when, category);
        }
        return String.format("%s (%s to %s) - %s", name, when, endDate, category);
    }
}
