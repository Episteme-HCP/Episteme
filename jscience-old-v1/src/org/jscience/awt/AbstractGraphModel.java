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

package org.jscience.awt;

import javax.swing.event.EventListenerList;


/**
 * The AbstractGraphModel class handles the dispatching of GraphDataEvents
 * to interested listeners.
 *
 * @author Mark Hale
 * @version 1.2
 */
public abstract class AbstractGraphModel extends Object {
    /** DOCUMENT ME! */
    private final EventListenerList listenerList = new EventListenerList();

    /** DOCUMENT ME! */
    private final GraphDataEvent dataChangedEvent = new GraphDataEvent(this);

    /**
     * Notifies all listeners that the graph data may have changed.
     */
    protected final void fireGraphDataChanged() {
        fireGraphChanged(dataChangedEvent);
    }

    /**
     * Notifies all listeners that a series may have changed.
     *
     * @param series DOCUMENT ME!
     */
    protected final void fireGraphSeriesUpdated(int series) {
        final GraphDataEvent event = new GraphDataEvent(this, series);
        fireGraphChanged(event);
    }

    /**
     * Notifies all listeners of a given graph data event.
     *
     * @param event DOCUMENT ME!
     */
    protected final void fireGraphChanged(GraphDataEvent event) {
        final Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == GraphDataListener.class) {
                ((GraphDataListener) listeners[i + 1]).dataChanged(event);
            }
        }
    }

    /**
     * Adds a listener to the list that's notified each time a change
     * to the graph data occurs.
     *
     * @param l DOCUMENT ME!
     */
    public final void addGraphDataListener(GraphDataListener l) {
        listenerList.add(GraphDataListener.class, l);
    }

    /**
     * Removes a listener from the list that's notified each time a
     * change to the graph data occurs.
     *
     * @param l DOCUMENT ME!
     */
    public final void removeGraphDataListener(GraphDataListener l) {
        listenerList.remove(GraphDataListener.class, l);
    }
}
