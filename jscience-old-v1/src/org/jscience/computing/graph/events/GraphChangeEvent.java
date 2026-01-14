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

/* ---------------------
 * GraphChangeEvent.java
 * ---------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: GraphChangeEvent.java,v 1.3 2007-10-23 18:16:33 virtualcall Exp $
 *
 * Changes
 * -------
 * 10-Aug-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph.events;

import java.util.EventObject;


/**
 * An event which indicates that a graph has changed. This class is a root
 * for graph change events.
 *
 * @author Barak Naveh
 *
 * @since Aug 10, 2003
 */
public class GraphChangeEvent extends EventObject {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 3834592106026382391L;

    /** The type of graph change this event indicates. */
    protected int m_type;

/**
     * Creates a new graph change event.
     *
     * @param eventSource the source of the event.
     * @param type        the type of event.
     */
    public GraphChangeEvent(Object eventSource, int type) {
        super(eventSource);
        m_type = type;
    }

    /**
     * Returns the event type.
     *
     * @return the event type.
     */
    public int getType() {
        return m_type;
    }
}
