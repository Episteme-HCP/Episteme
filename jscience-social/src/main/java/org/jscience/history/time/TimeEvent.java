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

package org.jscience.history.time;

import java.util.EventObject;
import java.util.Objects;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * An event indicating a change or occurrence in a time simulation or tracking system.
 * This is primarily used by the {@link TimeServer} to notify listeners of tick updates.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class TimeEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    /** Type ID for a generic time change event. */
    public final static int TIME_CHANGED = 1;

    /** The current state of time at the moment of the event. */
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Time time;

    /** The unique identifier of the event type. */
    @Attribute
    private final int id;

    /**
     * Constructs a new TimeEvent.
     * 
     * @param source the server producing the event
     * @param time the chronological state
     * @param id type identifier
     * @throws NullPointerException if source or time is null
     */
    public TimeEvent(TimeServer source, Time time, int id) {
        super(Objects.requireNonNull(source, "TimeServer source cannot be null"));
        this.time = Objects.requireNonNull(time, "Time cannot be null");
        this.id = id;
    }

    /**
     * Returns a string representation of the event type.
     *
     * @return a descriptive string for the event ID
     */
    public String paramString() {
        return switch (getID()) {
            case TIME_CHANGED -> "TIME_CHANGED";
            default -> "unknown type";
        };
    }

    /**
     * Returns the server that originated the event.
     *
     * @return the time server
     */
    public TimeServer getTimeServer() {
        return (TimeServer) getSource();
    }

    /**
     * Returns the time associated with the event.
     *
     * @return the time state
     */
    public Time getTime() {
        return time;
    }

    /**
     * Returns the event identifier.
     *
     * @return the type ID
     */
    public int getID() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("TimeEvent[%s] at %s", paramString(), time);
    }
}
