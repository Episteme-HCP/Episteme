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

package org.jscience.history.clock;

import javax.swing.event.EventListenerList;
import java.util.Objects;

/**
 * An abstract base class for chronological services capable of broadcasting time updates to listeners.
 * Concrete implementations may represent real-time clocks, simulation engines, or network-synchronized services.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public abstract class TimeServer {
    
    /** Internal registry of event listeners. */
    private final EventListenerList listenerList = new EventListenerList();

    /**
     * Activates the time server, initiating update broadcasts.
     */
    public abstract void start();

    /**
     * Deactivates the time server, halting all broadcasts.
     */
    public abstract void stop();

    /**
     * Propagates a time event to all registered listeners.
     *
     * @param evt the time event to dispatch
     * @throws NullPointerException if event is null
     */
    public void dispatchTimeEvent(TimeEvent evt) {
        Objects.requireNonNull(evt, "TimeEvent cannot be null");
        TimeListener[] listeners = getTimeListeners();
        for (TimeListener target : listeners) {
            target.timeChanged(evt);
        }
    }

    /**
     * Adds a listener to be notified of time updates.
     *
     * @param listener the observer to add
     * @throws NullPointerException if listener is null
     */
    public void addTimeListener(TimeListener listener) {
        Objects.requireNonNull(listener, "Listener cannot be null");
        listenerList.add(TimeListener.class, listener);
    }

    /**
     * Removes a listener from the notification list.
     *
     * @param listener the observer to remove
     */
    public void removeTimeListener(TimeListener listener) {
        listenerList.remove(TimeListener.class, listener);
    }

    /**
     * Returns an array of all currently registered time listeners.
     *
     * @return array of listeners (may be empty)
     */
    public TimeListener[] getTimeListeners() {
        return listenerList.getListeners(TimeListener.class);
    }
}
