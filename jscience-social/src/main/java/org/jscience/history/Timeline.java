/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.history;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * A class representing a sequence or collection of events organized in time.
 * Provides methods to manage and query historical events.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Timeline implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The set of events in this timeline. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Event> events;

    /**
     * Creates a new, empty Timeline.
     */
    public Timeline() {
        this.events = new HashSet<>();
    }

    /**
     * Returns an unmodifiable view of the events in this timeline.
     *
     * @return an unmodifiable set of events
     */
    public Set<Event> getEvents() {
        return Collections.unmodifiableSet(events);
    }

    /**
     * Adds an event to the timeline.
     *
     * @param event the event to add
     * @return this timeline for chaining
     * @throws NullPointerException if event is null
     */
    public Timeline addEvent(Event event) {
        events.add(Objects.requireNonNull(event, "Event cannot be null"));
        return this;
    }

    /**
     * Removes an event from the timeline.
     *
     * @param event the event to remove
     * @return true if the event was removed
     */
    public boolean removeEvent(Event event) {
        return events.remove(event);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timeline timeline)) return false;
        return Objects.equals(events, timeline.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(events);
    }

    @Override
    public String toString() {
        return "Timeline with " + events.size() + " events";
    }
}
