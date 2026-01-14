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
 * AsUndirectedGraph.java
 * ----------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id: AsUndirectedGraph.java,v 1.2 2007-10-21 21:07:56 virtualcall Exp $
 *
 * Changes
 * -------
 * 14-Aug-2003 : Initial revision (JVS);
 *
 */
package org.jscience.computing.graph.graphs;

import org.jscience.computing.graph.DirectedGraph;
import org.jscience.computing.graph.Edge;
import org.jscience.computing.graph.UndirectedGraph;
import org.jscience.computing.graph.edges.UndirectedEdge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * An undirected view of the backing directed graph specified in the
 * constructor.  This graph allows modules to apply algorithms designed for
 * undirected graphs to a directed graph by simply ignoring edge direction. If
 * the backing directed graph is an <a
 * href="http://mathworld.wolfram.com/OrientedGraph.html"> oriented graph</a>,
 * then the view will be a simple graph; otherwise, it will be a multigraph.
 * Query operations on this graph "read through" to the backing graph.
 * Attempts to add edges will result in an
 * <code>UnsupportedOperationException</code>, but vertex addition/removal and
 * edge removal are all supported (and immediately reflected in the backing
 * graph).
 * <p/>
 * <p/>
 * Note that edges returned by this graph's accessors are really just the edges
 * of the underlying directed graph.  Since there is no interface distinction
 * between directed and undirected edges, this detail should be irrelevant to
 * algorithms.
 * </p>
 * <p/>
 * <p/>
 * This graph does <i>not</i> pass the hashCode and equals operations through
 * to the backing graph, but relies on <tt>Object</tt>'s <tt>equals</tt> and
 * <tt>hashCode</tt> methods.  This graph will be serializable if the backing
 * graph is serializable.
 * </p>
 *
 * @author John V. Sichi
 * @since Aug 14, 2003
 */
public class AsUndirectedGraph extends GraphDelegator implements Serializable,
        UndirectedGraph {
    private static final long serialVersionUID = 3257845485078065462L;
    private static final String NO_EDGE_ADD =
            "this graph does not support edge addition";
    private static final String UNDIRECTED =
            "this graph only supports undirected operations";

    /**
     * Constructor for AsUndirectedGraph.
     *
     * @param g the backing directed graph over which an undirected view is to
     *          be created.
     */
    public AsUndirectedGraph(DirectedGraph g) {
        super(g);
    }

    /**
     * @see org.jscience.computing.graph.Graph#getAllEdges(Object,Object)
     */
    public List getAllEdges(Object sourceVertex, Object targetVertex) {
        List forwardList = super.getAllEdges(sourceVertex, targetVertex);

        if (sourceVertex.equals(targetVertex)) {
            // avoid duplicating loops
            return forwardList;
        }

        List reverseList = super.getAllEdges(targetVertex, sourceVertex);
        List list =
                new ArrayList(forwardList.size() + reverseList.size());
        list.addAll(forwardList);
        list.addAll(reverseList);

        return list;
    }

    /**
     * @see org.jscience.computing.graph.Graph#getEdge(Object,Object)
     */
    public Edge getEdge(Object sourceVertex, Object targetVertex) {
        Edge edge = super.getEdge(sourceVertex, targetVertex);

        if (edge != null) {
            return edge;
        }

        // try the other direction
        return super.getEdge(targetVertex, sourceVertex);
    }

    /**
     * @see org.jscience.computing.graph.Graph#addAllEdges(Collection)
     */
    public boolean addAllEdges(Collection edges) {
        throw new UnsupportedOperationException(NO_EDGE_ADD);
    }

    /**
     * @see org.jscience.computing.graph.Graph#addEdge(Edge)
     */
    public boolean addEdge(Edge e) {
        throw new UnsupportedOperationException(NO_EDGE_ADD);
    }

    /**
     * @see org.jscience.computing.graph.Graph#addEdge(Object,Object)
     */
    public Edge addEdge(Object sourceVertex, Object targetVertex) {
        throw new UnsupportedOperationException(NO_EDGE_ADD);
    }

    /**
     * @see UndirectedGraph#degreeOf(Object)
     */
    public int degreeOf(Object vertex) {
        // this counts loops twice, which is consistent with AbstractBaseGraph
        return super.inDegreeOf(vertex) + super.outDegreeOf(vertex);
    }

    /**
     * @see DirectedGraph#inDegreeOf(Object)
     */
    public int inDegreeOf(Object vertex) {
        throw new UnsupportedOperationException(UNDIRECTED);
    }

    /**
     * @see DirectedGraph#incomingEdgesOf(Object)
     */
    public List incomingEdgesOf(Object vertex) {
        throw new UnsupportedOperationException(UNDIRECTED);
    }

    /**
     * @see DirectedGraph#outDegreeOf(Object)
     */
    public int outDegreeOf(Object vertex) {
        throw new UnsupportedOperationException(UNDIRECTED);
    }

    /**
     * @see DirectedGraph#outgoingEdgesOf(Object)
     */
    public List outgoingEdgesOf(Object vertex) {
        throw new UnsupportedOperationException(UNDIRECTED);
    }

    /**
     * @see AbstractBaseGraph#toString()
     */
    public String toString() {
        // take care to print edges using the undirected convention
        Collection edgeSet = new ArrayList();

        Iterator iter = edgeSet().iterator();

        while (iter.hasNext()) {
            Edge edge = (Edge) iter.next();
            edgeSet.add(new UndirectedEdge(edge.getSource(),
                    edge.getTarget()));
        }

        return super.toStringFromSets(vertexSet(), edgeSet);
    }
}
