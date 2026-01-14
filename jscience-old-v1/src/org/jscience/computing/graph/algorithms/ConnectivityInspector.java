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
 * ConnectivityInspector.java
 * --------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   John V. Sichi
 *
 * $Id: ConnectivityInspector.java,v 1.2 2007-10-21 21:07:52 virtualcall Exp $
 *
 * Changes
 * -------
 * 06-Aug-2003 : Initial revision (BN);
 * 10-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org.jscience.computing.graph.algorithms;

import org.jscience.computing.graph.DirectedGraph;
import org.jscience.computing.graph.Graph;
import org.jscience.computing.graph.UndirectedGraph;
import org.jscience.computing.graph.events.*;
import org.jscience.computing.graph.graphs.AsUndirectedGraph;
import org.jscience.computing.graph.iterators.BreadthFirstIterator;

import java.util.*;

/**
 * Allows obtaining various connectivity aspects of a graph. The <i>inspected
 * graph</i> is specified at construction time and cannot be modified.
 * Currently, the inspector supports connected components for an undirected
 * graph and weakly connected components for a directed graph.  To find
 * strongly connected components, use {@link StrongConnectivityInspector}
 * instead.
 * <p/>
 * <p/>
 * The inspector methods work in a lazy fashion: no computation is performed
 * unless immediately necessary. Computation are done once and results and
 * cached within this class for future need.
 * </p>
 * <p/>
 * <p/>
 * The inspector is also a {@link org.jscience.computing.graph.events.GraphListener}. If
 * added as a listener to the inspected graph, the inspector will amend
 * internal cached results instead of recomputing them. It is efficient when a
 * few modifications are applied to a large graph. If many modifications are
 * expected it will not be efficient due to added overhead on graph update
 * operations. If inspector is added as listener to a graph other than the one
 * it inspects, results are undefined.
 * </p>
 *
 * @author Barak Naveh
 * @author John V. Sichi
 * @since Aug 6, 2003
 */
public class ConnectivityInspector implements GraphListener {
    List m_connectedSets;
    Map m_vertexToConnectedSet;
    private Graph m_graph;

    /**
     * Creates a connectivity inspector for the specified undirected graph.
     *
     * @param g the graph for which a connectivity inspector to be created.
     */
    public ConnectivityInspector(UndirectedGraph g) {
        init();
        m_graph = g;
    }

    /**
     * Creates a connectivity inspector for the specified directed graph.
     *
     * @param g the graph for which a connectivity inspector to be created.
     */
    public ConnectivityInspector(DirectedGraph g) {
        init();
        m_graph = new AsUndirectedGraph(g);
    }

    /**
     * Test if the inspected graph is connected. An empty graph is <i>not</i>
     * considered connected.
     *
     * @return <code>true</code> if and only if inspected graph is connected.
     */
    public boolean isGraphConnected() {
        return lazyFindConnectedSets().size() == 1;
    }

    /**
     * Returns a set of all vertices that are in the maximally connected
     * component together with the specified vertex. For more on maximally
     * connected component, see <a
     * href="http://www.nist.gov/dads/HTML/maximallyConnectedComponent.html">
     * http://www.nist.gov/dads/HTML/maximallyConnectedComponent.html</a>.
     *
     * @param vertex the vertex for which the connected set to be returned.
     * @return a set of all vertices that are in the maximally connected
     *         component together with the specified vertex.
     */
    public Set connectedSetOf(Object vertex) {
        Set connectedSet = (Set) m_vertexToConnectedSet.get(vertex);

        if (connectedSet == null) {
            connectedSet = new HashSet();

            BreadthFirstIterator i =
                    new BreadthFirstIterator(m_graph, vertex);

            while (i.hasNext()) {
                connectedSet.add(i.next());
            }

            m_vertexToConnectedSet.put(vertex, connectedSet);
        }

        return connectedSet;
    }

    /**
     * Returns a list of <code>Set</code>s, where each set contains all
     * vertices that are in the same maximally connected component. All graph
     * vertices occur in exactly one set.  For more on maximally connected
     * component, see <a
     * href="http://www.nist.gov/dads/HTML/maximallyConnectedComponent.html">
     * http://www.nist.gov/dads/HTML/maximallyConnectedComponent.html</a>.
     *
     * @return Returns a list of <code>Set</code>s, where each set contains all
     *         vertices that are in the same maximally connected component.
     */
    public List connectedSets() {
        return lazyFindConnectedSets();
    }

    /**
     * @see GraphListener#edgeAdded(GraphEdgeChangeEvent)
     */
    public void edgeAdded(GraphEdgeChangeEvent e) {
        init(); // for now invalidate cached results, in the future need to amend them.
    }

    /**
     * @see GraphListener#edgeRemoved(GraphEdgeChangeEvent)
     */
    public void edgeRemoved(GraphEdgeChangeEvent e) {
        init(); // for now invalidate cached results, in the future need to amend them.
    }

    /**
     * Tests if there is a path from the specified source vertex to the
     * specified target vertices. For a directed graph, direction is ignored
     * for this interpretation of path.
     * <p/>
     * <p/>
     * Note: Future versions of this method might not ignore edge directions
     * for directed graphs.
     * </p>
     *
     * @param sourceVertex one end of the path.
     * @param targetVertex another end of the path.
     * @return <code>true</code> if and only if there is a path from the source
     *         vertex to the target vertex.
     */
    public boolean pathExists(Object sourceVertex, Object targetVertex) {
        /*
        * TODO: Ignoring edge direction for directed graph may be
        * confusing. For directed graphs, consider Dijkstra's algorithm.
        */
        Set sourceSet = connectedSetOf(sourceVertex);

        return sourceSet.contains(targetVertex);
    }

    /**
     * @see org.jscience.computing.graph.events.VertexSetListener#vertexAdded(GraphVertexChangeEvent)
     */
    public void vertexAdded(GraphVertexChangeEvent e) {
        init(); // for now invalidate cached results, in the future need to amend them.
    }

    /**
     * @see org.jscience.computing.graph.events.VertexSetListener#vertexRemoved(GraphVertexChangeEvent)
     */
    public void vertexRemoved(GraphVertexChangeEvent e) {
        init(); // for now invalidate cached results, in the future need to amend them.
    }

    private void init() {
        m_connectedSets = null;
        m_vertexToConnectedSet = new HashMap();
    }

    private List lazyFindConnectedSets() {
        if (m_connectedSets == null) {
            m_connectedSets = new ArrayList();

            Set vertexSet = m_graph.vertexSet();

            if (vertexSet.size() > 0) {
                BreadthFirstIterator i =
                        new BreadthFirstIterator(m_graph, null);
                i.addTraversalListener(new MyTraversalListener());

                while (i.hasNext()) {
                    i.next();
                }
            }
        }

        return m_connectedSets;
    }

    /**
     * A traversal listener that groups all vertices according to to their
     * containing connected set.
     *
     * @author Barak Naveh
     * @since Aug 6, 2003
     */
    private class MyTraversalListener extends TraversalListenerAdapter {
        private Set m_currentConnectedSet;

        /**
         * @see TraversalListenerAdapter#connectedComponentFinished(ConnectedComponentTraversalEvent)
         */
        public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
            m_connectedSets.add(m_currentConnectedSet);
        }

        /**
         * @see TraversalListenerAdapter#connectedComponentStarted(ConnectedComponentTraversalEvent)
         */
        public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
            m_currentConnectedSet = new HashSet();
        }

        /**
         * @see TraversalListenerAdapter#vertexTraversed(Object)
         */
        public void vertexTraversed(VertexTraversalEvent e) {
            Object v = e.getVertex();
            m_currentConnectedSet.add(v);
            m_vertexToConnectedSet.put(v, m_currentConnectedSet);
        }
    }
}
