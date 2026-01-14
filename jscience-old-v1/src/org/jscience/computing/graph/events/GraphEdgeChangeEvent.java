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

/* -------------------------
 * GraphEdgeChangeEvent.java
 * -------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: GraphEdgeChangeEvent.java,v 1.3 2007-10-23 18:16:33 virtualcall Exp $
 *
 * Changes
 * -------
 * 10-Aug-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph.events;

import org.jscience.computing.graph.Edge;


/**
 * An event which indicates that a graph edge has changed, or is about to
 * change. The event can be used either as an indication <i>after</i> the edge
 * has been added or removed, or <i>before</i> it is added. The type of the
 * event can be tested using the {@link
 * org.jscience.computing.graph.event.GraphChangeEvent#getType()} method.
 *
 * @author Barak Naveh
 *
 * @since Aug 10, 2003
 */
public class GraphEdgeChangeEvent extends GraphChangeEvent {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 3618134563335844662L;

    /**
     * Before edge added event. This event is fired before an edge is
     * added to a graph.
     */
    public static final int BEFORE_EDGE_ADDED = 21;

    /**
     * Before edge removed event. This event is fired before an edge is
     * removed from a graph.
     */
    public static final int BEFORE_EDGE_REMOVED = 22;

    /**
     * Edge added event. This event is fired after an edge is added to
     * a graph.
     */
    public static final int EDGE_ADDED = 23;

    /**
     * Edge removed event. This event is fired after an edge is removed
     * from a graph.
     */
    public static final int EDGE_REMOVED = 24;

    /** The edge that this event is related to. */
    protected Edge m_edge;

/**
     * Constructor for GraphEdgeChangeEvent.
     *
     * @param eventSource the source of this event.
     * @param type        the event type of this event.
     * @param e           the edge that this event is related to.
     */
    public GraphEdgeChangeEvent(Object eventSource, int type, Edge e) {
        super(eventSource, type);
        m_edge = e;
    }

    /**
     * Returns the edge that this event is related to.
     *
     * @return the edge that this event is related to.
     */
    public Edge getEdge() {
        return m_edge;
    }
}
