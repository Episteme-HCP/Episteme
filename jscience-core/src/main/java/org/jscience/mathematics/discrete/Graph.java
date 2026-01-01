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

package org.jscience.mathematics.discrete;

import java.util.*;

/**
 * Represents a mathematical graph G = (V, E).
 * <p>
 * A graph consists of vertices (nodes) and edges connecting them.
 * Supports both directed and undirected graphs.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Graph<V> {

    /**
     * Returns all vertices in this graph.
     * 
     * @return unmodifiable set of vertices
     */
    Set<V> vertices();

    /**
     * Returns all edges in this graph.
     * 
     * @return unmodifiable set of edges
     */
    Set<Edge<V>> edges();

    /**
     * Adds a vertex to the graph.
     * 
     * @param vertex the vertex to add
     * @return true if vertex was added, false if already present
     */
    boolean addVertex(V vertex);

    /**
     * Adds an edge between two vertices.
     * 
     * @param source the source vertex
     * @param target the target vertex
     * @return true if edge was added
     */
    boolean addEdge(V source, V target);

    /**
     * Returns neighbors of a vertex.
     * 
     * @param vertex the vertex
     * @return set of adjacent vertices
     */
    Set<V> neighbors(V vertex);

    /**
     * Returns the degree (number of edges) of a vertex.
     * 
     * @param vertex the vertex
     * @return degree of vertex
     */
    int degree(V vertex);

    /**
     * Checks if this graph is directed.
     * 
     * @return true if directed, false if undirected
     */
    boolean isDirected();

    /**
     * Performs depth-first search (DFS) traversal.
     * 
     * @param start   the starting vertex
     * @param visitor the visitor to call for each vertex
     */
    default void dfs(V start, org.jscience.mathematics.discrete.graph.VertexVisitor<V> visitor) {
        java.util.Set<V> visited = new java.util.HashSet<>();
        java.util.Deque<java.util.Map.Entry<V, Integer>> stack = new java.util.ArrayDeque<>();
        stack.push(new java.util.AbstractMap.SimpleEntry<>(start, 0));
        while (!stack.isEmpty()) {
            java.util.Map.Entry<V, Integer> entry = stack.pop();
            V current = entry.getKey();
            int depth = entry.getValue();
            if (!visited.contains(current)) {
                visited.add(current);
                visitor.visit(current, depth);
                for (V neighbor : neighbors(current)) {
                    if (!visited.contains(neighbor)) {
                        stack.push(new java.util.AbstractMap.SimpleEntry<>(neighbor, depth + 1));
                    }
                }
            }
        }
    }

    /**
     * Performs breadth-first search (BFS) traversal.
     * 
     * @param start   the starting vertex
     * @param visitor the visitor to call for each vertex
     */
    default void bfs(V start, org.jscience.mathematics.discrete.graph.VertexVisitor<V> visitor) {
        java.util.Set<V> visited = new java.util.HashSet<>();
        java.util.Queue<V> queue = new java.util.LinkedList<>();
        java.util.Map<V, Integer> depths = new java.util.HashMap<>();
        queue.add(start);
        visited.add(start);
        depths.put(start, 0);
        while (!queue.isEmpty()) {
            V current = queue.poll();
            int currentDepth = depths.get(current);
            visitor.visit(current, currentDepth);
            for (V neighbor : neighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    depths.put(neighbor, currentDepth + 1);
                    queue.add(neighbor);
                }
            }
        }
    }

    /**
     * Returns the number of vertices in the graph.
     * 
     * @return the number of vertices
     */
    int vertexCount();

    /**
     * Represents an edge in a graph.
     */
    interface Edge<V> {
        V source();

        V target();
    }
}

