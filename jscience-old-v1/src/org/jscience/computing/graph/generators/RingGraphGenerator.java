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
 * GraphGenerator.java
 * -------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id: RingGraphGenerator.java,v 1.3 2007-10-23 18:16:34 virtualcall Exp $
 *
 * Changes
 * -------
 * 16-Sep-2003 : Initial revision (JVS);
 *
 */
package org.jscience.computing.graph.generators;

import org.jscience.computing.graph.Graph;
import org.jscience.computing.graph.VertexFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Generates a ring graph of any size. A ring graph is a graph that
 * contains a single cycle that passes through all its vertices exactly once.
 * For a directed graph, the generated edges are oriented consistently around
 * the ring.
 *
 * @author John V. Sichi
 *
 * @since Sep 16, 2003
 */
public class RingGraphGenerator implements GraphGenerator {
    /** DOCUMENT ME! */
    private int m_size;

/**
     * Construct a new RingGraphGenerator.
     *
     * @param size number of vertices to be generated
     * @throws IllegalArgumentException if the specified size is negative.
     */
    public RingGraphGenerator(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("must be non-negative");
        }

        m_size = size;
    }

    /**
     * {@inheritDoc}
     */
    public void generateGraph(Graph target, VertexFactory vertexFactory,
        Map resultMap) {
        if (m_size < 1) {
            return;
        }

        LinearGraphGenerator linearGenerator = new LinearGraphGenerator(m_size);
        Map privateMap = new HashMap();
        linearGenerator.generateGraph(target, vertexFactory, privateMap);

        Object startVertex = privateMap.get(LinearGraphGenerator.START_VERTEX);
        Object endVertex = privateMap.get(LinearGraphGenerator.END_VERTEX);
        target.addEdge(endVertex, startVertex);
    }
}
