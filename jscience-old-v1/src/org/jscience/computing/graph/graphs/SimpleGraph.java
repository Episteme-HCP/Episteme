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

/* ----------------
 * SimpleGraph.java
 * ----------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: SimpleGraph.java,v 1.2 2007-10-23 18:16:35 virtualcall Exp $
 *
 * Changes
 * -------
 * 05-Aug-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph.graphs;

import org.jscience.computing.graph.EdgeFactory;
import org.jscience.computing.graph.UndirectedGraph;
import org.jscience.computing.graph.edges.EdgeFactories;


/**
 * A simple graph. A simple graph is an undirected graph for which at most
 * one edge connects any two vertices, and loops are not permitted.  If you're
 * unsure about simple graphs, see: <a
 * href="http://mathworld.wolfram.com/SimpleGraph.html">
 * http://mathworld.wolfram.com/SimpleGraph.html</a>.
 */
public class SimpleGraph extends AbstractBaseGraph implements UndirectedGraph {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 3545796589454112304L;

/**
     * Creates a new simple graph with the specified edge factory.
     *
     * @param ef the edge factory of the new graph.
     */
    public SimpleGraph(EdgeFactory ef) {
        super(ef, false, false);
    }

/**
     * Creates a new simple graph.
     */
    public SimpleGraph() {
        this(new EdgeFactories.UndirectedEdgeFactory());
    }
}
