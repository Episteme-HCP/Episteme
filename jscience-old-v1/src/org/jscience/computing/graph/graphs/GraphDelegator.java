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

/* -------------------
 * GraphDelegator.java
 * -------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: GraphDelegator.java,v 1.2 2007-10-21 21:07:57 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph.graphs;

import org.jscience.computing.graph.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * A graph backed by the the graph specified at the constructor, which
 * delegates all its methods to the backing graph. Operations on this graph
 * "pass through" to the to the backing graph. Any modification made to this
 * graph or the backing graph is reflected by the other.
 * <p/>
 * <p/>
 * This graph does <i>not</i> pass the hashCode and equals operations through
 * to the backing graph, but relies on <tt>Object</tt>'s <tt>equals</tt> and
 * <tt>hashCode</tt> methods.
 * </p>
 * <p/>
 * <p/>
 * This class is mostly used as a base for extending subclasses.
 * </p>
 *
 * @author Barak Naveh
 * @since Jul 20, 2003
 */
public class GraphDelegator extends AbstractGraph implements Graph,
        Serializable {
    private static final long serialVersionUID = 3257005445226181425L;

    /**
     * The graph to which operations are delegated.
     */
    private Graph m_delegate;

    /**
     * Constructor for GraphDelegator.
     *
     * @param g the backing graph (the delegate).
     * @throws NullPointerException
     */
    public GraphDelegator(Graph g) {
        super();

        if (g == null) {
            throw new NullPointerException();
        }

        m_delegate = g;
    }

    /**
     * @see Graph#getAllEdges(Object,Object)
     */
    public List getAllEdges(Object sourceVertex, Object targetVertex) {
        return m_delegate.getAllEdges(sourceVertex, targetVertex);
    }

    /**
     * @see Graph#getEdge(Object,Object)
     */
    public Edge getEdge(Object sourceVertex, Object targetVertex) {
        return m_delegate.getEdge(sourceVertex, targetVertex);
    }

    /**
     * @see Graph#getEdgeFactory()
     */
    public EdgeFactory getEdgeFactory() {
        return m_delegate.getEdgeFactory();
    }

    /**
     * @see Graph#addEdge(Edge)
     */
    public boolean addEdge(Edge e) {
        return m_delegate.addEdge(e);
    }

    /**
     * @see Graph#addEdge(Object,Object)
     */
    public Edge addEdge(Object sourceVertex, Object targetVertex) {
        return m_delegate.addEdge(sourceVertex, targetVertex);
    }

    /**
     * @see Graph#addVertex(Object)
     */
    public boolean addVertex(Object v) {
        return m_delegate.addVertex(v);
    }

    /**
     * @see Graph#containsEdge(Edge)
     */
    public boolean containsEdge(Edge e) {
        return m_delegate.containsEdge(e);
    }

    /**
     * @see Graph#containsVertex(Object)
     */
    public boolean containsVertex(Object v) {
        return m_delegate.containsVertex(v);
    }

    /**
     * @see UndirectedGraph#degreeOf(Object)
     */
    public int degreeOf(Object vertex) {
        return ((UndirectedGraph) m_delegate).degreeOf(vertex);
    }

    /**
     * @see Graph#edgeSet()
     */
    public Set edgeSet() {
        return m_delegate.edgeSet();
    }

    /**
     * @see Graph#edgesOf(Object)
     */
    public List edgesOf(Object vertex) {
        return m_delegate.edgesOf(vertex);
    }

    /**
     * @see DirectedGraph#inDegreeOf(Object)
     */
    public int inDegreeOf(Object vertex) {
        return ((DirectedGraph) m_delegate).inDegreeOf(vertex);
    }

    /**
     * @see DirectedGraph#incomingEdgesOf(Object)
     */
    public List incomingEdgesOf(Object vertex) {
        return ((DirectedGraph) m_delegate).incomingEdgesOf(vertex);
    }

    /**
     * @see DirectedGraph#outDegreeOf(Object)
     */
    public int outDegreeOf(Object vertex) {
        return ((DirectedGraph) m_delegate).outDegreeOf(vertex);
    }

    /**
     * @see DirectedGraph#outgoingEdgesOf(Object)
     */
    public List outgoingEdgesOf(Object vertex) {
        return ((DirectedGraph) m_delegate).outgoingEdgesOf(vertex);
    }

    /**
     * @see Graph#removeEdge(Edge)
     */
    public boolean removeEdge(Edge e) {
        return m_delegate.removeEdge(e);
    }

    /**
     * @see Graph#removeEdge(Object,Object)
     */
    public Edge removeEdge(Object sourceVertex, Object targetVertex) {
        return m_delegate.removeEdge(sourceVertex, targetVertex);
    }

    /**
     * @see Graph#removeVertex(Object)
     */
    public boolean removeVertex(Object v) {
        return m_delegate.removeVertex(v);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return m_delegate.toString();
    }

    /**
     * @see Graph#vertexSet()
     */
    public Set vertexSet() {
        return m_delegate.vertexSet();
    }
}
