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

package org.jscience.mathematics.discrete.graph.flow;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.discrete.WeightedGraph;
import java.util.*;

/**
 * Ford-Fulkerson algorithm for maximum flow.
 * <p>
 * Finds maximum flow from source to sink in flow network.
 * Uses DFS to find augmenting paths. O(E * max_flow) complexity.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MaxFlow {

    /**
     * Computes maximum flow using Ford-Fulkerson with DFS.
     * 
     * @param <V>    the type of vertices
     * @param graph  flow network (must be a WeightedGraph with Real weights)
     * @param source source vertex
     * @param sink   sink vertex
     * @return maximum flow value
     */
    public static <V> Real fordFulkerson(WeightedGraph<V, Real> graph, V source, V sink) {
        List<V> vertices = new ArrayList<>(graph.vertices());
        Map<V, Integer> vertexToIndex = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) {
            vertexToIndex.put(vertices.get(i), i);
        }

        if (!vertexToIndex.containsKey(source) || !vertexToIndex.containsKey(sink)) {
            throw new IllegalArgumentException("Source or sink not in graph");
        }

        int s = vertexToIndex.get(source);
        int t = vertexToIndex.get(sink);
        int n = vertices.size();

        // Residual capacity graph
        Real[][] residual = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Real weight = graph.getWeight(vertices.get(i), vertices.get(j));
                residual[i][j] = (weight != null) ? weight : Real.ZERO;
            }
        }

        Real maxFlow = Real.ZERO;
        int[] parent = new int[n];

        // While there exists augmenting path
        while (hasAugmentingPath(residual, s, t, parent)) {
            // Find minimum capacity along path
            Real pathFlow = Real.of(Double.MAX_VALUE);
            for (int v = t; v != s; v = parent[v]) {
                int u = parent[v];
                if (residual[u][v].compareTo(pathFlow) < 0) {
                    pathFlow = residual[u][v];
                }
            }

            // Update residual capacities
            for (int v = t; v != s; v = parent[v]) {
                int u = parent[v];
                residual[u][v] = residual[u][v].subtract(pathFlow);
                residual[v][u] = residual[v][u].add(pathFlow);
            }

            maxFlow = maxFlow.add(pathFlow);
        }

        return maxFlow;
    }

    /**
     * Finds augmenting path using DFS.
     */
    private static boolean hasAugmentingPath(Real[][] residual, int source, int sink, int[] parent) {
        int n = residual.length;
        boolean[] visited = new boolean[n];
        Arrays.fill(parent, -1);

        Stack<Integer> stack = new Stack<>();
        stack.push(source);
        visited[source] = true;

        while (!stack.isEmpty()) {
            int u = stack.pop();

            if (u == sink) {
                return true;
            }

            for (int v = 0; v < n; v++) {
                if (!visited[v] && residual[u][v].compareTo(Real.ZERO) > 0) {
                    visited[v] = true;
                    parent[v] = u;
                    stack.push(v);
                }
            }
        }

        return false;
    }

    /**
     * Edmonds-Karp algorithm (Ford-Fulkerson with BFS).
     * <p>
     * O(VEÃ‚Â²) complexity - polynomial time.
     * Guaranteed to terminate for irrational capacities.
     * </p>
     */
    public static <V> Real edmondsKarp(WeightedGraph<V, Real> graph, V source, V sink) {
        List<V> vertices = new ArrayList<>(graph.vertices());
        Map<V, Integer> vertexToIndex = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) {
            vertexToIndex.put(vertices.get(i), i);
        }

        if (!vertexToIndex.containsKey(source) || !vertexToIndex.containsKey(sink)) {
            throw new IllegalArgumentException("Source or sink not in graph");
        }

        int s = vertexToIndex.get(source);
        int t = vertexToIndex.get(sink);
        int n = vertices.size();

        Real[][] residual = new Real[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Real weight = graph.getWeight(vertices.get(i), vertices.get(j));
                residual[i][j] = (weight != null) ? weight : Real.ZERO;
            }
        }

        Real maxFlow = Real.ZERO;
        int[] parent = new int[n];

        while (bfs(residual, s, t, parent)) {
            // Find minimum capacity
            Real pathFlow = Real.of(Double.MAX_VALUE);
            for (int v = t; v != s; v = parent[v]) {
                int u = parent[v];
                if (residual[u][v].compareTo(pathFlow) < 0) {
                    pathFlow = residual[u][v];
                }
            }

            // Update residual capacities
            for (int v = t; v != s; v = parent[v]) {
                int u = parent[v];
                residual[u][v] = residual[u][v].subtract(pathFlow);
                residual[v][u] = residual[v][u].add(pathFlow);
            }

            maxFlow = maxFlow.add(pathFlow);
        }

        return maxFlow;
    }

    private static boolean bfs(Real[][] residual, int source, int sink, int[] parent) {
        int n = residual.length;
        boolean[] visited = new boolean[n];
        Arrays.fill(parent, -1);

        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 0; v < n; v++) {
                if (!visited[v] && residual[u][v].compareTo(Real.ZERO) > 0) {
                    if (v == sink) {
                        parent[v] = u;
                        return true;
                    }
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        return false;
    }
}


