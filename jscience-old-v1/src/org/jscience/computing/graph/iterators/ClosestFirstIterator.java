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
 * ClosestFirstIterator.java
 * -------------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   Barak Naveh
 *
 * $Id: ClosestFirstIterator.java,v 1.2 2007-10-21 21:07:59 virtualcall Exp $
 *
 * Changes
 * -------
 * 02-Sep-2003 : Initial revision (JVS);
 * 31-Jan-2004 : Reparented and changed interface to parent class (BN);
 * 29-May-2005 : Added radius support (JVS);
 *
 */
package org.jscience.computing.graph.iterators;

import org.jscience.computing.graph.Edge;
import org.jscience.computing.graph.Graph;
import org.jscience.computing.graph.util.FibonacciHeap;

/**
 * A closest-first iterator for a directed or undirected graph. For this
 * iterator to work correctly the graph must not be modified during iteration.
 * Currently there are no means to ensure that, nor to fail-fast. The results
 * of such modifications are undefined.
 * <p/>
 * <p/>
 * The metric for <i>closest</i> here is the path length from a start vertex.
 * Edge.getWeight() is summed to calculate path length. Negative edge weights
 * will result in an IllegalArgumentException.  Optionally, path length may be
 * bounded by a finite radius.
 * </p>
 *
 * @author John V. Sichi
 * @since Sep 2, 2003
 */
public class ClosestFirstIterator extends CrossComponentIterator {
    /**
     * Priority queue of fringe vertices.
     */
    private FibonacciHeap m_heap = new FibonacciHeap();

    /**
     * Maximum distance to search.
     */
    private double m_radius = Double.POSITIVE_INFINITY;

    /**
     * Creates a new closest-first iterator for the specified graph.
     *
     * @param g the graph to be iterated.
     */
    public ClosestFirstIterator(Graph g) {
        this(g, null);
    }

    /**
     * Creates a new closest-first iterator for the specified graph. Iteration
     * will start at the specified start vertex and will be limited to the
     * connected component that includes that vertex. If the specified start
     * vertex is <code>null</code>, iteration will start at an arbitrary
     * vertex and will not be limited, that is, will be able to traverse all
     * the graph.
     *
     * @param g           the graph to be iterated.
     * @param startVertex the vertex iteration to be started.
     */
    public ClosestFirstIterator(Graph g, Object startVertex) {
        this(g, startVertex, Double.POSITIVE_INFINITY);
    }

    /**
     * Creates a new radius-bounded closest-first iterator for the specified
     * graph. Iteration will start at the specified start vertex and will be
     * limited to the subset of the connected component which includes that
     * vertex and is reachable via paths of length less than or equal to the
     * specified radius.  The specified start vertex may not be
     * <code>null</code>.
     *
     * @param g           the graph to be iterated.
     * @param startVertex the vertex iteration to be started.
     * @param radius      limit on path length, or Double.POSITIVE_INFINITY for
     *                    unbounded search.
     */
    public ClosestFirstIterator(Graph g, Object startVertex, double radius) {
        super(g, startVertex);
        m_radius = radius;
        checkRadiusTraversal(isCrossComponentTraversal());
    }

    // override AbstractGraphIterator
    public void setCrossComponentTraversal(boolean crossComponentTraversal) {
        checkRadiusTraversal(crossComponentTraversal);
        super.setCrossComponentTraversal(crossComponentTraversal);
    }

    /**
     * Get the length of the shortest path known to the given vertex.  If the
     * vertex has already been visited, then it is truly the shortest path
     * length; otherwise, it is the best known upper bound.
     *
     * @param vertex vertex being sought from start vertex
     * @return length of shortest path known, or Double.POSITIVE_INFINITY if no
     *         path found yet
     */
    public double getShortestPathLength(Object vertex) {
        QueueEntry entry = (QueueEntry) getSeenData(vertex);

        if (entry == null) {
            return Double.POSITIVE_INFINITY;
        }

        return entry.getShortestPathLength();
    }

    /**
     * Get the spanning tree edge reaching a vertex which has been seen already
     * in this traversal.  This edge is the last link in the shortest known
     * path between the start vertex and the requested vertex.  If the vertex
     * has already been visited, then it is truly the minimum spanning tree
     * edge; otherwise, it is the best candidate seen so far.
     *
     * @param vertex the spanned vertex.
     * @return the spanning tree edge, or null if the vertex either has not
     *         been seen yet or is the start vertex.
     */
    public Edge getSpanningTreeEdge(Object vertex) {
        QueueEntry entry = (QueueEntry) getSeenData(vertex);

        if (entry == null) {
            return null;
        }

        return entry.m_spanningTreeEdge;
    }

    /**
     * @see org.jscience.computing.graph.iterators.CrossComponentIterator#isConnectedComponentExhausted()
     */
    protected boolean isConnectedComponentExhausted() {
        if (m_heap.size() == 0) {
            return true;
        } else {
            if (m_heap.min().getKey() > m_radius) {
                m_heap.clear();

                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * @see org.jscience.computing.graph.iterators.CrossComponentIterator#encounterVertex(java.lang.Object,
     *org.jscience.computing.graph.Edge)
     */
    protected void encounterVertex(Object vertex, Edge edge) {
        QueueEntry entry = createSeenData(vertex, edge);
        putSeenData(vertex, entry);
        m_heap.insert(entry, entry.getShortestPathLength());
    }

    /**
     * Override superclass.  When we see a vertex again, we need to see if the
     * new edge provides a shorter path than the old edge.
     *
     * @param vertex the vertex re-encountered
     * @param edge   the edge via which the vertex was re-encountered
     */
    protected void encounterVertexAgain(Object vertex, Edge edge) {
        QueueEntry entry = (QueueEntry) getSeenData(vertex);

        if (entry.m_frozen) {
            // no improvement for this vertex possible
            return;
        }

        double candidatePathLength = calculatePathLength(vertex, edge);

        if (candidatePathLength < entry.getShortestPathLength()) {
            entry.m_spanningTreeEdge = edge;
            m_heap.decreaseKey(entry, candidatePathLength);
        }
    }

    /**
     * @see org.jscience.computing.graph.iterators.CrossComponentIterator#provideNextVertex()
     */
    protected Object provideNextVertex() {
        QueueEntry entry = (QueueEntry) m_heap.removeMin();
        entry.m_frozen = true;

        return entry.m_vertex;
    }

    private void assertNonNegativeEdge(Edge edge) {
        if (edge.getWeight() < 0) {
            throw new IllegalArgumentException("negative edge weights not allowed");
        }
    }

    /**
     * Determine path length to a vertex via an edge, using the path length for
     * the opposite vertex.
     *
     * @param vertex the vertex for which to calculate the path length.
     * @param edge   the edge via which the path is being extended.
     * @return calculated path length.
     */
    private double calculatePathLength(Object vertex, Edge edge) {
        assertNonNegativeEdge(edge);

        Object otherVertex = edge.oppositeVertex(vertex);
        QueueEntry otherEntry = (QueueEntry) getSeenData(otherVertex);

        return otherEntry.getShortestPathLength() + edge.getWeight();
    }

    private void checkRadiusTraversal(boolean crossComponentTraversal) {
        if (crossComponentTraversal && (m_radius != Double.POSITIVE_INFINITY)) {
            throw new IllegalArgumentException("radius may not be specified for cross-component traversal");
        }
    }

    /**
     * The first time we see a vertex, make up a new queue entry for it.
     *
     * @param vertex a vertex which has just been encountered.
     * @param edge   the edge via which the vertex was encountered.
     * @return the new queue entry.
     */
    private QueueEntry createSeenData(Object vertex, Edge edge) {
        double shortestPathLength;

        if (edge == null) {
            shortestPathLength = 0;
        } else {
            shortestPathLength = calculatePathLength(vertex, edge);
        }

        QueueEntry entry = new QueueEntry(shortestPathLength);
        entry.m_vertex = vertex;
        entry.m_spanningTreeEdge = edge;

        return entry;
    }

    /**
     * Private data to associate with each entry in the priority queue.
     */
    private static class QueueEntry extends FibonacciHeap.Node {
        /**
         * Best spanning tree edge to vertex seen so far.
         */
        Edge m_spanningTreeEdge;

        /**
         * The vertex reached.
         */
        Object m_vertex;

        /**
         * True once m_spanningTreeEdge is guaranteed to be the true minimum.
         */
        boolean m_frozen;

        QueueEntry(double key) {
            super(key);
        }

        double getShortestPathLength() {
            return getKey();
        }
    }
}
