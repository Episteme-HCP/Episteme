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
package org.jscience.mathematics.discrete;

import java.util.*;

/**
 * Bellman-Ford algorithm for single-source shortest paths.
 * <p>
 * Unlike Dijkstra's algorithm, Bellman-Ford can handle negative edge weights
 * and detects negative cycles. Time complexity is O(VE).
 * </p>
 * 
 * @param <V> the type of vertices
 * @param <W> the type of weights
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BellmanFordShortestPath<V, W> {

    private final GraphWeightAdapter<W> weightAdapter;

    public BellmanFordShortestPath(GraphWeightAdapter<W> weightAdapter) {
        this.weightAdapter = weightAdapter;
    }

    /**
     * Creates an instance for Double weights.
     */
    public static <V> BellmanFordShortestPath<V, Double> ofDouble() {
        return new BellmanFordShortestPath<>(GraphWeightAdapter.DOUBLE);
    }

    /**
     * Creates an instance for Real weights.
     */
    public static <V> BellmanFordShortestPath<V, org.jscience.mathematics.numbers.real.Real> ofReal() {
        return new BellmanFordShortestPath<>(GraphWeightAdapter.REAL);
    }

    /**
     * Computes shortest path from source to target.
     * 
     * @param graph  the graph
     * @param source source vertex
     * @param target target vertex
     * @return result containing distance and path, or negative cycle detection
     */
    public ShortestPathResult<V, W> findPath(WeightedGraph<V, W> graph, V source, V target) {
        Map<V, W> distances = new HashMap<>();
        Map<V, V> predecessors = new HashMap<>();
        Set<V> vertices = graph.vertices();

        // Initialize distances
        for (V vertex : vertices) {
            distances.put(vertex, null);
        }
        distances.put(source, weightAdapter.zero());

        // Relax edges V-1 times
        int n = vertices.size();
        for (int i = 0; i < n - 1; i++) {
            boolean updated = false;
            for (V u : vertices) {
                if (distances.get(u) == null)
                    continue;

                for (WeightedEdge<V, W> edge : graph.getWeightedEdgesFrom(u)) {
                    V v = edge.getTarget();
                    W newDist = weightAdapter.add(distances.get(u), edge.getWeight());

                    if (distances.get(v) == null || weightAdapter.compare(newDist, distances.get(v)) < 0) {
                        distances.put(v, newDist);
                        predecessors.put(v, u);
                        updated = true;
                    }
                }
            }
            if (!updated)
                break; // Early termination if no updates
        }

        // Check for negative cycles
        for (V u : vertices) {
            if (distances.get(u) == null)
                continue;

            for (WeightedEdge<V, W> edge : graph.getWeightedEdgesFrom(u)) {
                V v = edge.getTarget();
                W newDist = weightAdapter.add(distances.get(u), edge.getWeight());

                if (distances.get(v) != null && weightAdapter.compare(newDist, distances.get(v)) < 0) {
                    return ShortestPathResult.negativeCycle(source, target);
                }
            }
        }

        // No path to target
        if (distances.get(target) == null) {
            return ShortestPathResult.noPath(source, target);
        }

        // Reconstruct path
        List<V> path = reconstructPath(predecessors, source, target);
        return ShortestPathResult.withPath(source, target, distances.get(target), path);
    }

    /**
     * Computes shortest paths from source to all vertices.
     * 
     * @param graph  the graph
     * @param source source vertex
     * @return map of distances from source, or empty if negative cycle detected
     */
    public Optional<Map<V, W>> findAllPaths(WeightedGraph<V, W> graph, V source) {
        Map<V, W> distances = new HashMap<>();
        Set<V> vertices = graph.vertices();

        // Initialize
        for (V vertex : vertices) {
            distances.put(vertex, null);
        }
        distances.put(source, weightAdapter.zero());

        // Relax edges
        int n = vertices.size();
        for (int i = 0; i < n - 1; i++) {
            for (V u : vertices) {
                if (distances.get(u) == null)
                    continue;

                for (WeightedEdge<V, W> edge : graph.getWeightedEdgesFrom(u)) {
                    V v = edge.getTarget();
                    W newDist = weightAdapter.add(distances.get(u), edge.getWeight());

                    if (distances.get(v) == null || weightAdapter.compare(newDist, distances.get(v)) < 0) {
                        distances.put(v, newDist);
                    }
                }
            }
        }

        // Check for negative cycles
        for (V u : vertices) {
            if (distances.get(u) == null)
                continue;

            for (WeightedEdge<V, W> edge : graph.getWeightedEdgesFrom(u)) {
                V v = edge.getTarget();
                W newDist = weightAdapter.add(distances.get(u), edge.getWeight());

                if (distances.get(v) != null && weightAdapter.compare(newDist, distances.get(v)) < 0) {
                    return Optional.empty(); // Negative cycle detected
                }
            }
        }

        return Optional.of(distances);
    }

    private List<V> reconstructPath(Map<V, V> predecessors, V source, V target) {
        List<V> path = new ArrayList<>();
        V current = target;

        while (current != null && !current.equals(source)) {
            path.add(current);
            current = predecessors.get(current);
        }

        if (current == null) {
            return Collections.emptyList();
        }

        path.add(source);
        Collections.reverse(path);
        return path;
    }
}
