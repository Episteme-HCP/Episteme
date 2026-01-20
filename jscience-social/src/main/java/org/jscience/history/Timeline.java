package org.jscience.history;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing a Set of events.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Timeline extends Object {
    /** DOCUMENT ME! */
    private Set events;

/**
     * Creates a new Timeline object.
     */
    public Timeline() {
        this.events = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getEvents() {
        return events;
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void removeEvent(Event event) {
        events.remove(event);
    }
}
