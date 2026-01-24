package org.jscience.history;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.jscience.history.temporal.TemporalCoordinate;
import org.jscience.util.Named;
import org.jscience.util.Temporal;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a significant historical or scientific event.
 * Base class focusing on the temporal aspect of an event.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class Event implements Identified<String>, Named, Temporal<TemporalCoordinate>, Comparable<Event>, Serializable {

    private static final long serialVersionUID = 2L;

    public enum Category {
        POLITICAL, MILITARY, CULTURAL, SCIENTIFIC, ECONOMIC, RELIGIOUS, NATURAL, OTHER
    }

    @Id
    protected final String id;

    @Attribute
    protected final String name;

    @Attribute
    protected final String description;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    protected final TemporalCoordinate when;

    @Attribute
    protected final Category category;

    public Event(String name, String description, TemporalCoordinate when, Category category) {
        this.id = UUID.randomUUID().toString();
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = description;
        this.when = Objects.requireNonNull(when, "Time coordinate cannot be null");
        this.category = category != null ? category : Category.OTHER;
    }

    public Event(String name, TemporalCoordinate when) {
        this(name, null, when, Category.OTHER);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public TemporalCoordinate getWhen() {
        return when;
    }

    public Category getCategory() {
        return category;
    }

    public boolean happensBefore(Event other) {
        return this.when.toInstant().isBefore(other.when.toInstant());
    }

    public boolean happensAfter(Event other) {
        return this.when.toInstant().isAfter(other.when.toInstant());
    }

    @Override
    public int compareTo(Event o) {
        return this.when.compareTo(o.when);
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %s", name, when, category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
