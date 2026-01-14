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

/* -----------------------
 * DepthFirstIterator.java
 * -----------------------
 * (C) Copyright 2003, by Liviu Rau and Contributors.
 *
 * Original Author:  Liviu Rau
 * Contributor(s):   Barak Naveh
 *
 * $Id: DepthFirstIterator.java,v 1.3 2007-10-23 18:16:38 virtualcall Exp $
 *
 * Changes
 * -------
 * 29-Jul-2003 : Initial revision (LR);
 * 31-Jul-2003 : Fixed traversal across connected components (BN);
 * 06-Aug-2003 : Extracted common logic to TraverseUtils.XXFirstIterator (BN);
 * 31-Jan-2004 : Reparented and changed interface to parent class (BN);
 *
 */
package org.jscience.computing.graph.iterators;

import org.jscience.computing.graph.Edge;
import org.jscience.computing.graph.Graph;

import java.util.ArrayList;
import java.util.List;


/**
 * A depth-first iterator for a directed and an undirected graph. For this
 * iterator to work correctly the graph must not be modified during iteration.
 * Currently there are no means to ensure that, nor to fail-fast. The results
 * of such modifications are undefined.
 *
 * @author Liviu Rau
 * @author Barak Naveh
 *
 * @since Jul 29, 2003
 */
public class DepthFirstIterator extends CrossComponentIterator {
    /** DOCUMENT ME! */
    private List m_stack = new ArrayList();

/**
     * Creates a new depth-first iterator for the specified graph.
     *
     * @param g the graph to be iterated.
     */
    public DepthFirstIterator(Graph g) {
        this(g, null);
    }

/**
     * Creates a new depth-first iterator for the specified graph. Iteration
     * will start at the specified start vertex and will be limited to the
     * connected component that includes that vertex. If the specified start
     * vertex is <code>null</code>, iteration will start at an arbitrary
     * vertex and will not be limited, that is, will be able to traverse all
     * the graph.
     *
     * @param g           the graph to be iterated.
     * @param startVertex the vertex iteration to be started.
     */
    public DepthFirstIterator(Graph g, Object startVertex) {
        super(g, startVertex);
    }

    /**
     * 
     * @see org.jscience.computing.graph.iterators.CrossComponentIterator#isConnectedComponentExhausted()
     */
    protected boolean isConnectedComponentExhausted() {
        return m_stack.isEmpty();
    }

    /**
     * 
     * @see org.jscience.computing.graph.iterators.CrossComponentIterator#encounterVertex(java.lang.Object,
     *      org.jscience.computing.graph.Edge)
     */
    protected void encounterVertex(Object vertex, Edge edge) {
        putSeenData(vertex, null);
        m_stack.add(vertex);
    }

    /**
     * 
     * @see org.jscience.computing.graph.iterators.CrossComponentIterator#encounterVertexAgain(java.lang.Object,
     *      org.jscience.computing.graph.Edge)
     */
    protected void encounterVertexAgain(Object vertex, Edge edge) {
    }

    /**
     * 
     * @see org.jscience.computing.graph.iterators.CrossComponentIterator#provideNextVertex()
     */
    protected Object provideNextVertex() {
        return m_stack.remove(m_stack.size() - 1);
    }
}
