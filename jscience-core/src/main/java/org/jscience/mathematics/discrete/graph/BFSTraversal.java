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
package org.jscience.mathematics.discrete.graph;

import org.jscience.mathematics.discrete.Graph;

import java.util.*;

/**
 * Breadth-First Search (BFS) traversal strategy.
 * <p>
 * Explores neighbors level by level before moving to the next depth.
 * Uses CPU-based queue implementation.
 * </p>
 * 
 * @param <V> the type of vertices
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BFSTraversal<V> implements GraphTraversalStrategy<V> {

    @Override
    public void traverse(Graph<V> graph, V start, VertexVisitor<V> visitor) {
        Queue<VertexDepthPair<V>> queue = new LinkedList<>();
        Set<V> visited = new HashSet<>();

        queue.offer(new VertexDepthPair<>(start, 0));
        visited.add(start);

        while (!queue.isEmpty()) {
            VertexDepthPair<V> current = queue.poll();
            visitor.visit(current.vertex, current.depth);

            // Add all unvisited neighbors to queue
            for (V neighbor : graph.neighbors(current.vertex)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(new VertexDepthPair<>(neighbor, current.depth + 1));
                }
            }
        }
    }

    /**
     * Helper class to store vertex-depth pairs in the queue.
     */
    private static class VertexDepthPair<V> {
        final V vertex;
        final int depth;

        VertexDepthPair(V vertex, int depth) {
            this.vertex = vertex;
            this.depth = depth;
        }
    }
}