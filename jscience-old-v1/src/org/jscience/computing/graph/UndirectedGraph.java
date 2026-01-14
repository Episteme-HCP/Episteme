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

/* --------------------
 * UndirectedGraph.java
 * --------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: UndirectedGraph.java,v 1.3 2007-10-23 18:16:25 virtualcall Exp $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 *
 */
package org.jscience.computing.graph;

/**
 * A graph whose all edges are undirected. This is the root interface of all
 * undirected graphs.
 * <p/>
 * <p/>
 * See <a href="http://mathworld.wolfram.com/Graph.html">
 * http://mathworld.wolfram.com/Graph.html</a> for more on undirected and on
 * directed graphs.
 * </p>
 *
 * @author Barak Naveh
 * @since Jul 14, 2003
 */
public interface UndirectedGraph extends Graph {
    /**
     * Returns the degree of the specified vertex. A degree of a vertex
     * in an undirected graph is the number of edges touching that vertex.
     *
     * @param vertex vertex whose degree is to be calculated.
     *
     * @return the degree of the specified vertex.
     */
    public int degreeOf(Object vertex);
}
