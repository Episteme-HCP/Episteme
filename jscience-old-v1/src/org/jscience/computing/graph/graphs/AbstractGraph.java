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

/* ------------------
 * AbstractGraph.java
 * ------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: AbstractGraph.java,v 1.3 2007-10-23 18:16:34 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph.graphs;

import org.jscience.computing.graph.Edge;
import org.jscience.computing.graph.Graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * A skeletal implementation of the <tt>Graph</tt> interface, to minimize
 * the effort required to implement graph interfaces. This implementation is
 * applicable to both: directed graphs and undirected graphs.
 *
 * @author Barak Naveh
 *
 * @see org.jscience.computing.graph.Graph
 * @see org.jscience.computing.graph.DirectedGraph
 * @see org.jscience.computing.graph.UndirectedGraph
 */
public abstract class AbstractGraph implements Graph {
/**
     * Construct a new empty graph object.
     */
    public AbstractGraph() {
    }

    /**
     * 
     * @see Graph#addAllEdges(Collection)
     */
    public boolean addAllEdges(Collection edges) {
        boolean modified = false;

        for (Iterator iter = edges.iterator(); iter.hasNext();) {
            modified |= addEdge((Edge) iter.next());
        }

        return modified;
    }

    /**
     * 
     * @see Graph#addAllVertices(Collection)
     */
    public boolean addAllVertices(Collection vertices) {
        boolean modified = false;

        for (Iterator iter = vertices.iterator(); iter.hasNext();) {
            modified |= addVertex(iter.next());
        }

        return modified;
    }

    /**
     * 
     * @see Graph#containsEdge(Object,Object)
     */
    public boolean containsEdge(Object sourceVertex, Object targetVertex) {
        return getEdge(sourceVertex, targetVertex) != null;
    }

    /**
     * 
     * @see Graph#removeAllEdges(Collection)
     */
    public boolean removeAllEdges(Collection edges) {
        boolean modified = false;

        for (Iterator iter = edges.iterator(); iter.hasNext();) {
            modified |= removeEdge((Edge) iter.next());
        }

        return modified;
    }

    /**
     * 
     * @see Graph#removeAllEdges(Object,Object)
     */
    public List removeAllEdges(Object sourceVertex, Object targetVertex) {
        List removed = getAllEdges(sourceVertex, targetVertex);
        removeAllEdges(removed);

        return removed;
    }

    /**
     * 
     * @see Graph#removeAllVertices(Collection)
     */
    public boolean removeAllVertices(Collection vertices) {
        boolean modified = false;

        for (Iterator iter = vertices.iterator(); iter.hasNext();) {
            modified |= removeVertex(iter.next());
        }

        return modified;
    }

    /**
     * Returns a string of the parenthesized pair (V, E) representing
     * this G=(V,E) graph. 'V' is the string representation of the vertex set,
     * and 'E' is the string representation of the edge set.
     *
     * @return a string representation of this graph.
     */
    public String toString() {
        return toStringFromSets(vertexSet(), edgeSet());
    }

    /**
     * Ensures that the specified vertex exists in this graph, or else
     * throws exception.
     *
     * @param v vertex
     *
     * @return <code>true</code> if this assertion holds.
     *
     * @throws NullPointerException if specified vertex is <code>null</code>.
     * @throws IllegalArgumentException if specified vertex does not exist in
     *         this graph.
     */
    protected boolean assertVertexExist(Object v) {
        if (containsVertex(v)) {
            return true;
        } else if (v == null) {
            throw new NullPointerException();
        } else {
            throw new IllegalArgumentException("no such vertex in graph");
        }
    }

    /**
     * Removes all the edges in this graph that are also contained in
     * the specified edge array.  After this call returns, this graph will
     * contain no edges in common with the specified edges. This method will
     * invoke the {@link Graph#removeEdge(Edge)} method.
     *
     * @param edges edges to be removed from this graph.
     *
     * @return <tt>true</tt> if this graph changed as a result of the call.
     *
     * @see Graph#removeEdge(Edge)
     * @see Graph#containsEdge(Edge)
     */
    protected boolean removeAllEdges(Edge[] edges) {
        boolean modified = false;

        for (int i = 0; i < edges.length; i++) {
            modified |= removeEdge(edges[i]);
        }

        return modified;
    }

    /**
     * Helper for subclass implementations of toString(  ).
     *
     * @param vertexSet the vertex set V to be printed
     * @param edgeSet the edge set E to be printed
     *
     * @return a string representation of (V,E)
     */
    protected String toStringFromSets(Collection vertexSet, Collection edgeSet) {
        return "(" + vertexSet.toString() + ", " + edgeSet.toString() + ")";
    }
}
