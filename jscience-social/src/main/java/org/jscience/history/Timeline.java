package org.jscience.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jscience.history.time.TimeCoordinate;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * A sequence or collection of events organized in time.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class Timeline<E extends Event> implements Serializable {

    private static final long serialVersionUID = 2L;

    @Attribute
    protected final String name;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    protected final List<E> events = new ArrayList<>();

    public Timeline(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    public Timeline() {
        this("Untitled Timeline");
    }

    public String getName() {
        return name;
    }

    public Timeline<E> addEvent(E event) {
        events.add(Objects.requireNonNull(event, "Event cannot be null"));
        return this;
    }

    public boolean removeEvent(E event) {
        return events.remove(event);
    }

    public List<E> getEvents() {
        return events.stream()
                .sorted(Comparator.comparing(Event::getWhen))
                .toList();
    }

    public List<E> getEventsBetween(TimeCoordinate start, TimeCoordinate end) {
        return events.stream()
                .filter(e -> e.getWhen().compareTo(start) >= 0 &&
                        e.getWhen().compareTo(end) <= 0)
                .sorted(Comparator.comparing(Event::getWhen))
                .toList();
    }

    public Optional<E> getEarliestEvent() {
        return events.stream().min(Comparator.comparing(Event::getWhen));
    }

    public Optional<E> getLatestEvent() {
        return events.stream().max(Comparator.comparing(Event::getWhen));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timeline<?> timeline)) return false;
        return Objects.equals(name, timeline.name) && Objects.equals(events, timeline.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, events);
    }

    @Override
    public String toString() {
        return String.format("%s with %d events", name, events.size());
    }
}
