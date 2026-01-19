package org.jscience.history;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * A class representing a Set of events.
 */
public class Timeline {
    
    private final Set<Event> events;

    public Timeline() {
        this.events = new TreeSet<>(); // Ordered
    }

    public Set<Event> getEvents() {
        return Collections.unmodifiableSet(events);
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }
}
