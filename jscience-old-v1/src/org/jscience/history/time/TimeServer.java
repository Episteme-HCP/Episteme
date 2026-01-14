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

import java.util.Timer;

import javax.swing.*;
import javax.swing.event.EventListenerList;


/**
 * A class representing a way get time values.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class TimeServer {
    /** A list of event listeners for this component. */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * DOCUMENT ME!
     */
    public abstract void start();

    /**
     * DOCUMENT ME!
     */
    public abstract void stop();

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void dispatchTimeEvent(TimeEvent evt) {
        Object[] list = getTimeListeners();

        for (int i = 0; i < list.length; i++) {
            TimeListener target = (TimeListener) list[i];
            target.timeChanged(evt);
        }
    }

    /**
     * Adds the specified listener to receive events from this clock.
     *
     * @param l the TimeListener
     */
    public void addTimeListener(TimeListener l) {
        listenerList.add(TimeListener.class, l);
    }

    /**
     * Removes the specified TimeListener so that it no longer receives
     * events from this clock.
     *
     * @param l the TimeListener
     */
    public void removeTimeListener(TimeListener l) {
        listenerList.remove(TimeListener.class, l);
    }

    /**
     * Returns an array of all the <code>TimeListener</code>s added to
     * this <code>Clock</code> with <code>addTimeListener</code>.
     *
     * @return all of the <code>TimeListener</code>s added or an empty array if
     *         no listeners have been added
     *
     * @see #addTimeListener
     */
    public TimeListener[] getTimeListeners() {
        return (TimeListener[]) listenerList.getListeners(TimeListener.class);
    }
}
