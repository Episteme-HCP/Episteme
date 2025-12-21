/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
/**
 * Graph traversal strategies and algorithms.
 * <p>
 * This package provides pluggable traversal algorithms for graphs with optional
 * backend acceleration support.
 * </p>
 * <p>
 * <b>Standard traversals:</b>
 * <ul>
 * <li>{@link org.jscience.mathematics.discrete.graph.DFSTraversal} -
 * Depth-first search</li>
 * <li>{@link org.jscience.mathematics.discrete.graph.BFSTraversal} -
 * Breadth-first search</li>
 * </ul>
 * </p>
 * <p>
 * <b>Usage example:</b>
 * 
 * <pre>
 * Graph&lt;String&gt; graph = new DirectedGraph&lt;&gt;();
 * // ... populate graph ...
 * 
 * // DFS traversal
 * graph.dfs("start", (vertex, depth) -> {
 *     System.out.println(" ".repeat(depth) + vertex);
 * });
 * 
 * // Custom strategy
 * GraphTraversalStrategy&lt;String&gt; strategy = new BFSTraversal&lt;&gt;();
 * graph.traverse("start", strategy, (vertex, depth) -> {
 *     System.out.println("Level " + depth + ": " + vertex);
 * });
 * </pre>
 * </p>
 * 
 * @author Silvere Martin-Michiellot (silvere.martin@gmail.com)
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
package org.jscience.mathematics.discrete.graph;