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
 * @since 2.0
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
