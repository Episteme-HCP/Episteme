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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.discrete.graph;

import org.jscience.mathematics.discrete.Graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Depth-First Search (DFS) traversal strategy.
 * <p>
 * Explores as far as possible along each branch before backtracking.
 * Uses CPU-based recursive implementation.
 * </p>
 * 
 * @param <V> the type of vertices
 * 
 * @author Silvere Martin-Michiellot (silvere.martin@gmail.com)
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DFSTraversal<V> implements GraphTraversalStrategy<V> {

    @Override
    public void traverse(Graph<V> graph, V start, VertexVisitor<V> visitor) {
        Set<V> visited = new HashSet<>();
        dfsRecursive(graph, start, visitor, visited, 0);
    }

    /**
     * Recursive DFS helper method.
     * 
     * @param graph   the graph
     * @param current the current vertex
     * @param visitor the vertex visitor
     * @param visited set of already visited vertices
     * @param depth   current depth
     */
    private void dfsRecursive(Graph<V> graph, V current,
            VertexVisitor<V> visitor,
            Set<V> visited, int depth) {
        if (visited.contains(current)) {
            return;
        }

        visited.add(current);
        visitor.visit(current, depth);

        // Visit all neighbors
        for (V neighbor : graph.neighbors(current)) {
            dfsRecursive(graph, neighbor, visitor, visited, depth + 1);
        }
    }
}
