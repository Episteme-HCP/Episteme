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
 * $Id: WheelGraphGenerator.java,v 1.3 2007-10-23 18:16:34 virtualcall Exp $
 *
 * Changes
 * -------
 * 16-Sep-2003 : Initial revision (JVS);
 *
 */
package org.jscience.computing.graph.generators;

import org.jscience.computing.graph.Graph;
import org.jscience.computing.graph.VertexFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


/**
 * Generates a <a href="http://mathworld.wolfram.com/WheelGraph.html">wheel
 * graph</a> of any size. Reminding a bicycle wheel, a wheel graph has a hub
 * vertex in the center and a rim of vertices around it that are connected to
 * each other (as a ring). The rim vertices are also connected to the hub with
 * edges that are called "spokes".
 *
 * @author John V. Sichi
 *
 * @since Sep 16, 2003
 */
public class WheelGraphGenerator implements GraphGenerator {
    /** Role for the hub vertex. */
    public static final String HUB_VERTEX = "Hub Vertex";

    /** DOCUMENT ME! */
    private boolean m_inwardSpokes;

    /** DOCUMENT ME! */
    private int m_size;

/**
     * Creates a new WheelGraphGenerator object. This constructor is more
     * suitable for undirected graphs, where spokes' direction is meaningless.
     * In the directed case, spokes will be oriented from rim to hub.
     *
     * @param size number of vertices to be generated.
     */
    public WheelGraphGenerator(int size) {
        this(size, true);
    }

/**
     * Construct a new WheelGraphGenerator.
     *
     * @param size         number of vertices to be generated.
     * @param inwardSpokes if <code>true</code> and graph is directed, spokes
     *                     are oriented from rim to hub; else from hub to rim.
     * @throws IllegalArgumentException
     */
    public WheelGraphGenerator(int size, boolean inwardSpokes) {
        if (size < 0) {
            throw new IllegalArgumentException("must be non-negative");
        }

        m_size = size;
        m_inwardSpokes = inwardSpokes;
    }

    /**
     * {@inheritDoc}
     */
    public void generateGraph(Graph target, final VertexFactory vertexFactory,
        Map resultMap) {
        if (m_size < 1) {
            return;
        }

        // A little trickery to intercept the rim generation.  This is
        // necessary since target may be initially non-empty, meaning we can't
        // rely on its vertex set after the rim is generated.
        final Collection rim = new ArrayList();
        VertexFactory rimVertexFactory = new VertexFactory() {
                public Object createVertex() {
                    Object vertex = vertexFactory.createVertex();
                    rim.add(vertex);

                    return vertex;
                }
            };

        RingGraphGenerator ringGenerator = new RingGraphGenerator(m_size - 1);
        ringGenerator.generateGraph(target, rimVertexFactory, resultMap);

        Object hubVertex = vertexFactory.createVertex();
        target.addVertex(hubVertex);

        if (resultMap != null) {
            resultMap.put(HUB_VERTEX, hubVertex);
        }

        Iterator rimIter = rim.iterator();

        while (rimIter.hasNext()) {
            Object rimVertex = rimIter.next();

            if (m_inwardSpokes) {
                target.addEdge(rimVertex, hubVertex);
            } else {
                target.addEdge(hubVertex, rimVertex);
            }
        }
    }
}
