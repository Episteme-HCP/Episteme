/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.discrete.graph;

import org.jscience.technical.backend.ComputeBackend;
import org.jscience.mathematics.discrete.Graph;

/**
 * Strategy interface for graph traversal algorithms.
 * <p>
 * Implementations provide different traversal strategies (DFS, BFS, etc.)
 * and can optionally leverage compute backends for acceleration.
 * </p>
 * <p>
 * <b>Usage:</b>
 *
 * <pre>
 * GraphTraversalStrategy&lt;String&gt; dfs = new DFSTraversal&lt;&gt;();
 * graph.traverse(startVertex, dfs, (vertex, depth) -&gt; {
 * System.out.println(" ".repeat(depth) + vertex);
 * });
 * </pre>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface GraphTraversalStrategy<V> {

    /**
     * Traverses the graph starting from the given vertex.
     * <p>
     * The implementation chooses the appropriate backend automatically
     * (CPU or GPU) based on graph size and backend availability.
     * </p>
     * 
     * @param graph   the graph to traverse
     * @param start   the starting vertex
     * @param visitor the visitor to call for each vertex
     */
    void traverse(Graph<V> graph, V start, VertexVisitor<V> visitor);

    /**
     * Traverses the graph using a specific backend.
     * <p>
     * This allows advanced users to override automatic backend selection.
     * </p>
     * 
     * @param graph   the graph to traverse
     * @param start   the starting vertex
     * @param visitor the visitor to call for each vertex
     * @param backend the backend to use
     */
    default void traverse(Graph<V> graph, V start,
            VertexVisitor<V> visitor,
            ComputeBackend backend) {
        // Default: ignore backend parameter and use automatic selection
        traverse(graph, start, visitor);
    }
}