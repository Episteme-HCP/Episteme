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

/* --------------------
 * ListenableGraph.java
 * --------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: ListenableGraph.java,v 1.3 2007-10-23 18:16:25 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 10-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org.jscience.computing.graph;

import org.jscience.computing.graph.events.GraphListener;
import org.jscience.computing.graph.events.VertexSetListener;


/**
 * A graph that supports listeners on structural change events.
 *
 * @author Barak Naveh
 * @see org.jscience.computing.graph.events.GraphListener
 * @see org.jscience.computing.graph.events.VertexSetListener
 * @since Jul 20, 2003
 */
public interface ListenableGraph extends Graph {
    /**
     * Adds the specified graph listener to this graph, if not already
     * present.
     *
     * @param l the listener to be added.
     */
    public void addGraphListener(GraphListener l);

    /**
     * Adds the specified vertex set listener to this graph, if not
     * already present.
     *
     * @param l the listener to be added.
     */
    public void addVertexSetListener(VertexSetListener l);

    /**
     * Removes the specified graph listener from this graph, if
     * present.
     *
     * @param l he listener to be removed.
     */
    public void removeGraphListener(GraphListener l);

    /**
     * Removes the specified vertex set listener from this graph, if
     * present.
     *
     * @param l the listener to be removed.
     */
    public void removeVertexSetListener(VertexSetListener l);
}
