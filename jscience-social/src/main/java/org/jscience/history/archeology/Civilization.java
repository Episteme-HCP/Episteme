package org.jscience.history.archeology;

import java.io.Serializable;
import java.util.Objects;
import org.jscience.earth.Place;
import org.jscience.history.time.TimeCoordinate;
import org.jscience.sociology.Culture;
import org.jscience.util.Positioned;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a civilization, modeled as a culture distributed through space and time.
 * Covers both living and historical societies, tracking their geographical extent and duration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class Civilization implements Positioned<Place>, Serializable {

    private static final long serialVersionUID = 2L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Culture culture;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Place place;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final TimeCoordinate startDate;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private TimeCoordinate endDate;

    @Attribute
    private String description = "";

    public Civilization(Culture culture, Place place, TimeCoordinate startDate) {
        this.culture = Objects.requireNonNull(culture, "Culture cannot be null");
        this.place = Objects.requireNonNull(place, "Place cannot be null");
        this.startDate = Objects.requireNonNull(startDate, "Start date cannot be null");
    }

    public Culture getCulture() {
        return culture;
    }

    @Override
    public Place getPosition() {
        return place;
    }

    public TimeCoordinate getStartDate() {
        return startDate;
    }

    public TimeCoordinate getEndDate() {
        return endDate;
    }

    public void setEndDate(TimeCoordinate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description, "Description cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Civilization that)) return false;
        return Objects.equals(culture, that.culture) && 
               Objects.equals(place, that.place) && 
               Objects.equals(startDate, that.startDate) && 
               Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(culture, place, startDate, endDate);
    }

    @Override
    public String toString() {
        return String.format("%s in %s (%s - %s)", culture, place, startDate, 
            endDate != null ? endDate : "present");
    }
}
