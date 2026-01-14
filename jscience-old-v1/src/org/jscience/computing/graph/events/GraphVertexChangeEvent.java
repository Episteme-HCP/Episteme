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

/* ---------------------------
 * GraphVertexChangeEvent.java
 * ---------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: GraphVertexChangeEvent.java,v 1.3 2007-10-23 18:16:33 virtualcall Exp $
 *
 * Changes
 * -------
 * 10-Aug-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph.events;

/**
 * An event which indicates that a graph vertex has changed, or is about to
 * change. The event can be used either as an indication <i>after</i> the
 * vertex has  been added or removed, or <i>before</i> it is added. The type
 * of the event can be tested using the {@link
 * org.jscience.computing.graph.event.GraphChangeEvent#getType()} method.
 *
 * @author Barak Naveh
 *
 * @since Aug 10, 2003
 */
public class GraphVertexChangeEvent extends GraphChangeEvent {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 3690189962679104053L;

    /**
     * Before vertex added event. This event is fired before a vertex
     * is added to a graph.
     */
    public static final int BEFORE_VERTEX_ADDED = 11;

    /**
     * Before vertex removed event. This event is fired before a vertex
     * is removed from a graph.
     */
    public static final int BEFORE_VERTEX_REMOVED = 12;

    /**
     * Vertex added event. This event is fired after a vertex is added
     * to a graph.
     */
    public static final int VERTEX_ADDED = 13;

    /**
     * Vertex removed event. This event is fired after a vertex is
     * removed from a graph.
     */
    public static final int VERTEX_REMOVED = 14;

    /** The vertex that this event is related to. */
    protected Object m_vertex;

/**
     * Creates a new GraphVertexChangeEvent object.
     *
     * @param eventSource the source of the event.
     * @param type        the type of the event.
     * @param vertex      the vertex that the event is related to.
     */
    public GraphVertexChangeEvent(Object eventSource, int type, Object vertex) {
        super(eventSource, type);
        m_vertex = vertex;
    }

    /**
     * Returns the vertex that this event is related to.
     *
     * @return the vertex that this event is related to.
     */
    public Object getVertex() {
        return m_vertex;
    }
}
