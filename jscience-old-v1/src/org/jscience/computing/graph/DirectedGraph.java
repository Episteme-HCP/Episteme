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
 * DirectedGraph.java
 * ------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: DirectedGraph.java,v 1.3 2007-10-23 18:16:25 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph;

import java.util.List;


/**
 * A graph whose all edges are directed. This is the root interface of all
 * directed graphs.
 * <p/>
 * <p/>
 * See <a href="http://mathworld.wolfram.com/DirectedGraph.html">
 * http://mathworld.wolfram.com/DirectedGraph.html</a> for more on directed
 * graphs.
 * </p>
 *
 * @author Barak Naveh
 * @since Jul 14, 2003
 */
public interface DirectedGraph extends Graph {
    /**
     * Returns the "in degree" of the specified vertex. An in degree of
     * a vertex in a directed graph is the number of inward directed edges
     * from that vertex. See <a
     * href="http://mathworld.wolfram.com/Indegree.html">
     * http://mathworld.wolfram.com/Indegree.html</a>.
     *
     * @param vertex vertex whose degree is to be calculated.
     *
     * @return the degree of the specified vertex.
     */
    public int inDegreeOf(Object vertex);

    /**
     * Returns a list of all edges incoming into the specified vertex.
     *
     * @param vertex the vertex for which the list of incoming edges to be
     *        returned.
     *
     * @return a list of all edges incoming into the specified vertex.
     */
    public List incomingEdgesOf(Object vertex);

    /**
     * Returns the "out degree" of the specified vertex. An out degree
     * of a vertex in a directed graph is the number of outward directed edges
     * from that vertex. See <a
     * href="http://mathworld.wolfram.com/Outdegree.html">
     * http://mathworld.wolfram.com/Outdegree.html</a>.
     *
     * @param vertex vertex whose degree is to be calculated.
     *
     * @return the degree of the specified vertex.
     */
    public int outDegreeOf(Object vertex);

    /**
     * Returns a list of all edges outgoing from the specified vertex.
     *
     * @param vertex the vertex for which the list of outgoing edges to be
     *        returned.
     *
     * @return a list of all edges outgoing from the specified vertex.
     */
    public List outgoingEdgesOf(Object vertex);
}
