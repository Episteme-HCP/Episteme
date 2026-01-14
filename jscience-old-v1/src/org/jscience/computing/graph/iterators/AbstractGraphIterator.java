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

/* --------------------------
 * AbstractGraphIterator.java
 * --------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: AbstractGraphIterator.java,v 1.3 2007-10-23 18:16:38 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 11-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org.jscience.computing.graph.iterators;

import org.jscience.computing.graph.events.ConnectedComponentTraversalEvent;
import org.jscience.computing.graph.events.EdgeTraversalEvent;
import org.jscience.computing.graph.events.TraversalListener;
import org.jscience.computing.graph.events.VertexTraversalEvent;

import java.util.ArrayList;
import java.util.List;


/**
 * An empty implementation of a graph iterator to minimize the effort
 * required to implement graph iterators.
 *
 * @author Barak Naveh
 *
 * @since Jul 19, 2003
 */
public abstract class AbstractGraphIterator implements GraphIterator {
    /** DOCUMENT ME! */
    private List m_traversalListeners = new ArrayList();

    /** DOCUMENT ME! */
    private boolean m_crossComponentTraversal = true;

    /** DOCUMENT ME! */
    private boolean m_reuseEvents = false;

    /**
     * Sets the cross component traversal flag - indicates whether to
     * traverse the graph across connected components.
     *
     * @param crossComponentTraversal if <code>true</code> traverses across
     *        connected components.
     */
    public void setCrossComponentTraversal(boolean crossComponentTraversal) {
        m_crossComponentTraversal = crossComponentTraversal;
    }

    /**
     * Test whether this iterator is set to traverse the graph across
     * connected components.
     *
     * @return <code>true</code> if traverses across connected components,
     *         otherwise <code>false</code>.
     */
    public boolean isCrossComponentTraversal() {
        return m_crossComponentTraversal;
    }

    /**
     * 
     * @see GraphIterator#setReuseEvents(boolean)
     */
    public void setReuseEvents(boolean reuseEvents) {
        m_reuseEvents = reuseEvents;
    }

    /**
     * 
     * @see GraphIterator#isReuseEvents()
     */
    public boolean isReuseEvents() {
        return m_reuseEvents;
    }

    /**
     * Adds the specified traversal listener to this iterator.
     *
     * @param l the traversal listener to be added.
     */
    public void addTraversalListener(TraversalListener l) {
        if (!m_traversalListeners.contains(l)) {
            m_traversalListeners.add(l);
        }
    }

    /**
     * Unsupported.
     *
     * @throws UnsupportedOperationException
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the specified traversal listener from this iterator.
     *
     * @param l the traversal listener to be removed.
     */
    public void removeTraversalListener(TraversalListener l) {
        m_traversalListeners.remove(l);
    }

    /**
     * Informs all listeners that the traversal of the current
     * connected component finished.
     *
     * @param e the connected component finished event.
     */
    protected void fireConnectedComponentFinished(
        ConnectedComponentTraversalEvent e) {
        int len = m_traversalListeners.size();

        for (int i = 0; i < len; i++) {
            TraversalListener l = (TraversalListener) m_traversalListeners.get(i);
            l.connectedComponentFinished(e);
        }
    }

    /**
     * Informs all listeners that a traversal of a new connected
     * component has started.
     *
     * @param e the connected component started event.
     */
    protected void fireConnectedComponentStarted(
        ConnectedComponentTraversalEvent e) {
        int len = m_traversalListeners.size();

        for (int i = 0; i < len; i++) {
            TraversalListener l = (TraversalListener) m_traversalListeners.get(i);
            l.connectedComponentStarted(e);
        }
    }

    /**
     * Informs all listeners that a the specified edge was visited.
     *
     * @param e the edge traversal event.
     */
    protected void fireEdgeTraversed(EdgeTraversalEvent e) {
        int len = m_traversalListeners.size();

        for (int i = 0; i < len; i++) {
            TraversalListener l = (TraversalListener) m_traversalListeners.get(i);
            l.edgeTraversed(e);
        }
    }

    /**
     * Informs all listeners that a the specified vertex was visited.
     *
     * @param e the vertex traversal event.
     */
    protected void fireVertexTraversed(VertexTraversalEvent e) {
        int len = m_traversalListeners.size();

        for (int i = 0; i < len; i++) {
            TraversalListener l = (TraversalListener) m_traversalListeners.get(i);
            l.vertexTraversed(e);
        }
    }
}
