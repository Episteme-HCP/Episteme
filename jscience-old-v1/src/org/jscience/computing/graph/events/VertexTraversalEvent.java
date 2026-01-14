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
 * VertexTraversalEvent.java
 * -------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: VertexTraversalEvent.java,v 1.3 2007-10-23 18:16:33 virtualcall Exp $
 *
 * Changes
 * -------
 * 11-Aug-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph.events;

import java.util.EventObject;


/**
 * A traversal event for a graph vertex.
 *
 * @author Barak Naveh
 *
 * @since Aug 11, 2003
 */
public class VertexTraversalEvent extends EventObject {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 3688790267213918768L;

    /** The traversed vertex. */
    protected Object m_vertex;

/**
     * Creates a new VertexTraversalEvent.
     *
     * @param eventSource the source of the event.
     * @param vertex      the traversed vertex.
     */
    public VertexTraversalEvent(Object eventSource, Object vertex) {
        super(eventSource);
        m_vertex = vertex;
    }

    /**
     * Returns the traversed vertex.
     *
     * @return the traversed vertex.
     */
    public Object getVertex() {
        return m_vertex;
    }
}
