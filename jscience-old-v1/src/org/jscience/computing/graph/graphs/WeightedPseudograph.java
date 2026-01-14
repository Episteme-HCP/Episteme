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

/* ------------------------
 * WeightedPseudograph.java
 * ------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: WeightedPseudograph.java,v 1.2 2007-10-23 18:16:35 virtualcall Exp $
 *
 * Changes
 * -------
 * 05-Aug-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph.graphs;

import org.jscience.computing.graph.EdgeFactory;
import org.jscience.computing.graph.WeightedGraph;
import org.jscience.computing.graph.edges.EdgeFactories;


/**
 * A weighted pseudograph. A weighted pseudograph is a non-simple
 * undirected graph in which both graph loops and multiple edges are
 * permitted. The edges of a weighted pseudograph have weights. If you're
 * unsure about pseudographs, see: <a
 * href="http://mathworld.wolfram.com/Pseudograph.html">
 * http://mathworld.wolfram.com/Pseudograph.html</a>.
 */
public class WeightedPseudograph extends Pseudograph implements WeightedGraph {
    /** DOCUMENT ME! */
    private static final long serialVersionUID = 3257290244524356152L;

/**
     * Creates a new weighted pseudograph with the specified edge factory.
     *
     * @param ef the edge factory of the new graph.
     */
    public WeightedPseudograph(EdgeFactory ef) {
        super(ef);
    }

/**
     * Creates a new weighted pseudograph.
     */
    public WeightedPseudograph() {
        this(new EdgeFactories.UndirectedWeightedEdgeFactory());
    }
}
