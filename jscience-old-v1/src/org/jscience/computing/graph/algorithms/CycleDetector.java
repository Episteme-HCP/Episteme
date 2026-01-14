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
 * CycleDetector.java
 * ------------------
 * (C) Copyright 2004, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id: CycleDetector.java,v 1.3 2007-10-23 18:16:31 virtualcall Exp $
 *
 * Changes
 * -------
 * 16-Sept-2004 : Initial revision (JVS);
 *
 */
package org.jscience.computing.graph.algorithms;

import org.jscience.computing.graph.DirectedGraph;
import org.jscience.computing.graph.Edge;
import org.jscience.computing.graph.Graph;
import org.jscience.computing.graph.iterators.DepthFirstIterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Performs cycle detection on a graph. The <i>inspected graph</i> is
 * specified at construction time and cannot be modified. Currently, the
 * detector supports only directed graphs.
 *
 * @author John V. Sichi
 *
 * @since Sept 16, 2004
 */
public class CycleDetector {
    /** Graph on which cycle detection is being performed. */
    Graph m_graph;

/**
     * Creates a cycle detector for the specified graph.  Currently only
     * directed graphs are supported.
     *
     * @param graph the DirectedGraph in which to detect cycles
     */
    public CycleDetector(DirectedGraph graph) {
        m_graph = graph;
    }

    /**
     * Performs yes/no cycle detection on the entire graph.
     *
     * @return true iff the graph contains at least one cycle
     */
    public boolean detectCycles() {
        try {
            execute(null, null);
        } catch (CycleDetectedException ex) {
            return true;
        }

        return false;
    }

    /**
     * Performs yes/no cycle detection on an individual vertex.
     *
     * @param v the vertex to test
     *
     * @return true if v is on at least one cycle
     */
    public boolean detectCyclesContainingVertex(Object v) {
        try {
            execute(null, v);
        } catch (CycleDetectedException ex) {
            return true;
        }

        return false;
    }

    /**
     * Finds the vertex set for the subgraph of all cycles.
     *
     * @return set of all vertices which participate in at least one cycle in
     *         this graph
     */
    public Set findCycles() {
        Set set = new HashSet();
        execute(set, null);

        return set;
    }

    /**
     * Finds the vertex set for the subgraph of all cycles which
     * contain a particular vertex.
     *
     * @param v the vertex to test
     *
     * @return set of all vertices reachable from v via at least one cycle
     */
    public Set findCyclesContainingVertex(Object v) {
        Set set = new HashSet();
        execute(set, v);

        return set;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     * @param v DOCUMENT ME!
     */
    private void execute(Set s, Object v) {
        ProbeIterator iter = new ProbeIterator(s, v);

        while (iter.hasNext()) {
            iter.next();
        }
    }

    /**
     * Exception thrown internally when a cycle is detected during a
     * yes/no cycle test.  Must be caught by top-level detection method.
     */
    private static class CycleDetectedException extends RuntimeException {
        /** DOCUMENT ME! */
        private static final long serialVersionUID = 3834305137802950712L;
    }

    /**
     * Version of DFS which maintains a backtracking path used to probe
     * for cycles.
     */
    private class ProbeIterator extends DepthFirstIterator {
        /** DOCUMENT ME! */
        private List m_path;

        /** DOCUMENT ME! */
        private Set m_cycleSet;

/**
         * Creates a new ProbeIterator object.
         *
         * @param cycleSet    DOCUMENT ME!
         * @param startVertex DOCUMENT ME!
         */
        ProbeIterator(Set cycleSet, Object startVertex) {
            super(m_graph, startVertex);
            m_cycleSet = cycleSet;
            m_path = new ArrayList();
        }

        /**
         * {@inheritDoc}
         */
        protected void encounterVertexAgain(Object vertex, Edge edge) {
            super.encounterVertexAgain(vertex, edge);

            int i = m_path.indexOf(vertex);

            if (i > -1) {
                if (m_cycleSet == null) {
                    // we're doing yes/no cycle detection
                    throw new CycleDetectedException();
                }

                for (; i < m_path.size(); ++i) {
                    m_cycleSet.add(m_path.get(i));
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        protected Object provideNextVertex() {
            Object v = super.provideNextVertex();

            // backtrack
            for (int i = m_path.size() - 1; i >= 0; --i) {
                if (m_graph.containsEdge(m_path.get(i), v)) {
                    break;
                }

                m_path.remove(i);
            }

            m_path.add(v);

            return v;
        }
    }
}
