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

package org.jscience.mathematics.discrete;

import java.util.*;

/**
 * Floyd-Warshall algorithm for all-pairs shortest paths.
 * <p>
 * Computes shortest paths between all pairs of vertices in O(VÃ‚Â³) time.
 * Can handle negative weights but will detect negative cycles.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Floyd, R. W. (1962). Algorithm 97: Shortest Path. <i>Communications of the ACM</i>, 5(6), 345.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FloydWarshallShortestPath<V, W> {

    private final GraphWeightAdapter<W> weightAdapter;

    public FloydWarshallShortestPath(GraphWeightAdapter<W> weightAdapter) {
        this.weightAdapter = weightAdapter;
    }

    public static <V> FloydWarshallShortestPath<V, Double> ofDouble() {
        return new FloydWarshallShortestPath<>(GraphWeightAdapter.DOUBLE);
    }

    public static <V> FloydWarshallShortestPath<V, org.jscience.mathematics.numbers.real.Real> ofReal() {
        return new FloydWarshallShortestPath<>(GraphWeightAdapter.REAL);
    }

    /**
     * Computes all-pairs shortest paths.
     * 
     * @param graph the graph
     * @return map of (source, target) pairs to distances, or empty if negative
     *         cycle
     */
    public Optional<Map<V, Map<V, W>>> findAllPaths(WeightedGraph<V, W> graph) {
        List<V> vertices = new ArrayList<>(graph.vertices());
        int n = vertices.size();
        Map<Integer, V> indexToVertex = new HashMap<>();
        Map<V, Integer> vertexToIndex = new HashMap<>();

        for (int i = 0; i < n; i++) {
            indexToVertex.put(i, vertices.get(i));
            vertexToIndex.put(vertices.get(i), i);
        }

        // Initialize distance matrix
        @SuppressWarnings("unchecked")
        W[][] dist = (W[][]) new Object[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = weightAdapter.zero();
                } else {
                    dist[i][j] = null; // Infinity
                }
            }
        }

        // Fill in edge weights
        for (V u : vertices) {
            int i = vertexToIndex.get(u);
            for (WeightedEdge<V, W> edge : graph.getWeightedEdgesFrom(u)) {
                int j = vertexToIndex.get(edge.getTarget());
                dist[i][j] = edge.getWeight();
            }
        }

        // Floyd-Warshall algorithm
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != null && dist[k][j] != null) {
                        W newDist = weightAdapter.add(dist[i][k], dist[k][j]);
                        if (dist[i][j] == null || weightAdapter.compare(newDist, dist[i][j]) < 0) {
                            dist[i][j] = newDist;
                        }
                    }
                }
            }
        }

        // Check for negative cycles
        for (int i = 0; i < n; i++) {
            if (weightAdapter.compare(dist[i][i], weightAdapter.zero()) < 0) {
                return Optional.empty(); // Negative cycle detected
            }
        }

        // Convert to map
        Map<V, Map<V, W>> result = new HashMap<>();
        for (int i = 0; i < n; i++) {
            Map<V, W> row = new HashMap<>();
            for (int j = 0; j < n; j++) {
                if (dist[i][j] != null) {
                    row.put(indexToVertex.get(j), dist[i][j]);
                }
            }
            result.put(indexToVertex.get(i), row);
        }

        return Optional.of(result);
    }
}


