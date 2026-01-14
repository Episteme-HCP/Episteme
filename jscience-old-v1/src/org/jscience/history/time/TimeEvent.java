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


/**
 * A class representing a way to have time related events.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class TimeEvent extends EventObject {
    /**
     * DOCUMENT ME!
     */
    public final static int TIME_CHANGED = 1;

    /**
     * DOCUMENT ME!
     */
    private Time time;

    /**
     * DOCUMENT ME!
     */
    private int id;

/**
     * Constructs an <code>TimeEvent</code> object.
     * @param source the <code>Time</code> object that originated the event
     * @param id     an integer indicating the type of event
     */
    public TimeEvent(TimeServer source, Time time, int id) {
        super(source);
        this.time = time;
        this.id = id;
    }

    /**
     * Returns a parameter string identifying this event. This method
     * is useful for event logging and for debugging.
     *
     * @return a string identifying the event and its attributes
     */
    public String paramString() {
        String typeStr;

        switch (getID()) {
        case TIME_CHANGED:
            typeStr = "TIME_CHANGED";

            break;

        default:
            typeStr = "unknown type";
        }

        return typeStr;
    }

    /**
     * Returns the originator of the event.
     *
     * @return the <code>TimeServer</code> object that originated the event
     *
     * @since 1.3
     */
    public TimeServer getTimeServer() {
        return (source instanceof TimeServer) ? (TimeServer) source : null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Time getTime() {
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getID() {
        return id;
    }
}
