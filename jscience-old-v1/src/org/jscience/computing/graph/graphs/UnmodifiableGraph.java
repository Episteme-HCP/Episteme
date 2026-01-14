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
 * UnmodifiableGraph.java
 * ----------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: UnmodifiableGraph.java,v 1.2 2007-10-21 21:07:58 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph.graphs;

import org.jscience.computing.graph.Edge;
import org.jscience.computing.graph.Graph;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * An unmodifiable view of the backing graph specified in the constructor. This
 * graph allows modules to provide users with "read-only" access to internal
 * graphs. Query operations on this graph "read through" to the backing graph,
 * and attempts to modify this graph result in an
 * <code>UnsupportedOperationException</code>.
 * <p/>
 * <p/>
 * This graph does <i>not</i> pass the hashCode and equals operations through
 * to the backing graph, but relies on <tt>Object</tt>'s <tt>equals</tt> and
 * <tt>hashCode</tt> methods.  This graph will be serializable if the backing
 * graph is serializable.
 * </p>
 *
 * @author Barak Naveh
 * @since Jul 24, 2003
 */
public class UnmodifiableGraph extends GraphDelegator implements Serializable {
    private static final long serialVersionUID = 3544957670722713913L;
    private static final String UNMODIFIABLE = "this graph is unmodifiable";

    /**
     * Creates a new unmodifiable graph based on the specified backing graph.
     *
     * @param g the backing graph on which an unmodifiable graph is to be
     *          created.
     */
    public UnmodifiableGraph(Graph g) {
        super(g);
    }

    /**
     * @see Graph#addAllEdges(Collection)
     */
    public boolean addAllEdges(Collection edges) {
        throw new UnsupportedOperationException(UNMODIFIABLE);
    }

    /**
     * @see Graph#addAllVertices(Collection)
     */
    public boolean addAllVertices(Collection vertices) {
        throw new UnsupportedOperationException(UNMODIFIABLE);
    }

    /**
     * @see Graph#addEdge(Edge)
     */
    public boolean addEdge(Edge e) {
        throw new UnsupportedOperationException(UNMODIFIABLE);
    }

    /**
     * @see Graph#addEdge(Object,Object)
     */
    public Edge addEdge(Object sourceVertex, Object targetVertex) {
        throw new UnsupportedOperationException(UNMODIFIABLE);
    }

    /**
     * @see Graph#addVertex(Object)
     */
    public boolean addVertex(Object v) {
        throw new UnsupportedOperationException(UNMODIFIABLE);
    }

    /**
     * @see Graph#removeAllEdges(Collection)
     */
    public boolean removeAllEdges(Collection edges) {
        throw new UnsupportedOperationException(UNMODIFIABLE);
    }

    /**
     * @see Graph#removeAllEdges(Object,Object)
     */
    public List removeAllEdges(Object sourceVertex, Object targetVertex) {
        throw new UnsupportedOperationException(UNMODIFIABLE);
    }

    /**
     * @see Graph#removeAllVertices(Collection)
     */
    public boolean removeAllVertices(Collection vertices) {
        throw new UnsupportedOperationException(UNMODIFIABLE);
    }

    /**
     * @see Graph#removeEdge(Edge)
     */
    public boolean removeEdge(Edge e) {
        throw new UnsupportedOperationException(UNMODIFIABLE);
    }

    /**
     * @see Graph#removeEdge(Object,Object)
     */
    public Edge removeEdge(Object sourceVertex, Object targetVertex) {
        throw new UnsupportedOperationException(UNMODIFIABLE);
    }

    /**
     * @see Graph#removeVertex(Object)
     */
    public boolean removeVertex(Object v) {
        throw new UnsupportedOperationException(UNMODIFIABLE);
    }
}
