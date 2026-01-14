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

/* -----------------------------
 * DirectedWeightedSubgraph.java
 * -----------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: DirectedWeightedSubgraph.java,v 1.3 2007-10-23 18:16:34 virtualcall Exp $
 *
 * Changes
 * -------
 * 05-Aug-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph.graphs;

import org.jscience.computing.graph.DirectedGraph;
import org.jscience.computing.graph.WeightedGraph;

import java.util.Set;


/**
 * A directed weighted graph that is a subgraph on other graph.
 *
 * @see Subgraph
 */
public class DirectedWeightedSubgraph extends DirectedSubgraph
    implements WeightedGraph {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 3905799799168250680L;

/**
     * Creates a new weighted directed subgraph.
     *
     * @param base         the base (backing) graph on which the subgraph will be
     *                     based.
     * @param vertexSubset vertices to include in the subgraph. If
     *                     <code>null</code> then all vertices are included.
     * @param edgeSubset   edges to in include in the subgraph. If
     *                     <code>null</code> then all the edges whose vertices found in the
     *                     graph are included.
     */
    public DirectedWeightedSubgraph(WeightedGraph base, Set vertexSubset,
        Set edgeSubset) {
        super((DirectedGraph) base, vertexSubset, edgeSubset);
    }
}
