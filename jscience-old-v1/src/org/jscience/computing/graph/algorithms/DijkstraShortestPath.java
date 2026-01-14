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
 * DijkstraShortestPath.java
 * -------------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 *
 * $Id: DijkstraShortestPath.java,v 1.3 2007-10-23 18:16:31 virtualcall Exp $
 *
 * Changes
 * -------
 * 02-Sep-2003 : Initial revision (JVS);
 * 29-May-2005 : Make non-static and add radius support (JVS);
 *
 */
package org.jscience.computing.graph.algorithms;

import org.jscience.computing.graph.Edge;
import org.jscience.computing.graph.Graph;
import org.jscience.computing.graph.iterators.ClosestFirstIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * An implementation of <a
 * href="http://mathworld.wolfram.com/DijkstrasAlgorithm.html"> Dijkstra's
 * shortest path algorithm</a> using <code>ClosestFirstIterator</code>.
 *
 * @author John V. Sichi
 *
 * @since Sep 2, 2003
 */
public final class DijkstraShortestPath {
    /** DOCUMENT ME! */
    private List m_edgeList;

    /** DOCUMENT ME! */
    private double m_pathLength;

/**
     * Creates and executes a new DijkstraShortestPath algorithm instance. An
     * instance is only good for a single search; after construction, it can
     * be accessed to retrieve information about the path found.
     *
     * @param graph       the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex   the vertex at which the path should end
     * @param radius      limit on path length, or Double.POSITIVE_INFINITY for
     *                    unbounded search
     */
    public DijkstraShortestPath(Graph graph, Object startVertex,
        Object endVertex, double radius) {
        ClosestFirstIterator iter = new ClosestFirstIterator(graph,
                startVertex, radius);

        while (iter.hasNext()) {
            Object vertex = iter.next();

            if (vertex.equals(endVertex)) {
                createEdgeList(iter, endVertex);
                m_pathLength = iter.getShortestPathLength(endVertex);

                return;
            }
        }

        m_edgeList = null;
        m_pathLength = Double.POSITIVE_INFINITY;
    }

    /**
     * Return the edges making up the path found.
     *
     * @return List of Edges, or null if no path exists
     */
    public List getPathEdgeList() {
        return m_edgeList;
    }

    /**
     * Return the length of the path found.
     *
     * @return path length, or Double.POSITIVE_INFINITY if no path exists
     */
    public double getPathLength() {
        return m_pathLength;
    }

    /**
     * Convenience method to find the shortest path via a single static
     * method call.  If you need a more advanced search (e.g. limited by
     * radius, or computation of the path length), use the constructor
     * instead.
     *
     * @param graph the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex the vertex at which the path should end
     *
     * @return List of Edges, or null if no path exists
     */
    public static List findPathBetween(Graph graph, Object startVertex,
        Object endVertex) {
        DijkstraShortestPath alg = new DijkstraShortestPath(graph, startVertex,
                endVertex, Double.POSITIVE_INFINITY);

        return alg.getPathEdgeList();
    }

    /**
     * DOCUMENT ME!
     *
     * @param iter DOCUMENT ME!
     * @param endVertex DOCUMENT ME!
     */
    private void createEdgeList(ClosestFirstIterator iter, Object endVertex) {
        m_edgeList = new ArrayList();

        while (true) {
            Edge edge = iter.getSpanningTreeEdge(endVertex);

            if (edge == null) {
                break;
            }

            m_edgeList.add(edge);
            endVertex = edge.oppositeVertex(endVertex);
        }

        Collections.reverse(m_edgeList);
    }
}
