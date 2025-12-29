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
 * Graph algorithms including traversal, shortest paths, and connectivity.
 * <p>
 * Implements classic graph algorithms for analyzing graph structures and
 * finding
 * optimal paths.
 * </p>
 *
 * <h2>References</h2>
 * <ul>
 * <li>Edsger W. Dijkstra, "A Note on Two Problems in Connexion with Graphs",
 * Numerische Mathematik, Vol. 1, 1959, pp. 269-271</li>
 * <li>Thomas H. Cormen et al., "Introduction to Algorithms", 3rd Edition,
 * MIT Press, 2009 (comprehensive coverage of graph algorithms)</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GraphAlgorithms {

    /**
     * Performs breadth-first search from a starting vertex.
     * 
     * @param <V>   vertex type
     * @param graph the graph
     * @param start starting vertex
     * @return list of vertices in BFS order
     */
    public static <V> List<V> breadthFirstSearch(Graph<V> graph, V start) {
        List<V> result = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        Queue<V> queue = new LinkedList<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            V vertex = queue.poll();
            result.add(vertex);

            for (V neighbor : graph.neighbors(vertex)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        return result;
    }

    /**
     * Performs depth-first search from a starting vertex.
     * 
     * @param <V>   vertex type
     * @param graph the graph
     * @param start starting vertex
     * @return list of vertices in DFS order
     */
    public static <V> List<V> depthFirstSearch(Graph<V> graph, V start) {
        List<V> result = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        dfsHelper(graph, start, visited, result);
        return result;
    }

    private static <V> void dfsHelper(Graph<V> graph, V vertex, Set<V> visited, List<V> result) {
        visited.add(vertex);
        result.add(vertex);

        for (V neighbor : graph.neighbors(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsHelper(graph, neighbor, visited, result);
            }
        }
    }

    /**
     * Checks if the graph is connected (for undirected graphs).
     * 
     * @param <V>   vertex type
     * @param graph the graph
     * @return true if connected
     */
    public static <V> boolean isConnected(Graph<V> graph) {
        if (graph.vertices().isEmpty()) {
            return true;
        }

        V start = graph.vertices().iterator().next();
        List<V> reachable = breadthFirstSearch(graph, start);
        return reachable.size() == graph.vertices().size();
    }

    /**
     * Finds shortest path using Dijkstra's algorithm (unweighted = BFS).
     * 
     * @param <V>   vertex type
     * @param graph the graph
     * @param start starting vertex
     * @param end   ending vertex
     * @return shortest path or empty list if no path exists
     */
    public static <V> List<V> shortestPath(Graph<V> graph, V start, V end) {
        Map<V, V> parent = new HashMap<>();
        Set<V> visited = new HashSet<>();
        Queue<V> queue = new LinkedList<>();

        queue.offer(start);
        visited.add(start);
        parent.put(start, null);

        while (!queue.isEmpty()) {
            V vertex = queue.poll();

            if (vertex.equals(end)) {
                return reconstructPath(parent, start, end);
            }

            for (V neighbor : graph.neighbors(vertex)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, vertex);
                    queue.offer(neighbor);
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private static <V> List<V> reconstructPath(Map<V, V> parent, V start, V end) {
        List<V> path = new ArrayList<>();
        V current = end;

        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }

        Collections.reverse(path);
        return path;
    }
}