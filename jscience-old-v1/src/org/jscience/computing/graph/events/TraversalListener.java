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

/* ----------------------
 * TraversalListener.java
 * ----------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: TraversalListener.java,v 1.3 2007-10-23 18:16:33 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 11-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org.jscience.computing.graph.events;

/**
 * A listener on graph iterator or on a graph traverser.
 *
 * @author Barak Naveh
 * @since Jul 19, 2003
 */
public interface TraversalListener {
    /**
     * Called to inform listeners that the traversal of the current
     * connected component has finished.
     *
     * @param e the traversal event.
     */
    public void connectedComponentFinished(ConnectedComponentTraversalEvent e);

    /**
     * Called to inform listeners that a traversal of a new connected
     * component has started.
     *
     * @param e the traversal event.
     */
    public void connectedComponentStarted(ConnectedComponentTraversalEvent e);

    /**
     * Called to inform the listener that the specified edge have been
     * visited during the graph traversal. Depending on the traversal
     * algorithm, edge might be visited more than once.
     *
     * @param e the edge traversal event.
     */
    public void edgeTraversed(EdgeTraversalEvent e);

    /**
     * Called to inform the listener that the specified vertex have
     * been visited during the graph traversal. Depending on the traversal
     * algorithm, vertex might be visited more than once.
     *
     * @param e the vertex traversal event.
     */
    public void vertexTraversed(VertexTraversalEvent e);
}
