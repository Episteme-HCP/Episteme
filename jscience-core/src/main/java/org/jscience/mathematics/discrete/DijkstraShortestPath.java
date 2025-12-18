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
package org.jscience.mathematics.discrete;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Dijkstra's algorithm for finding the shortest paths in a weighted graph.
 * <p>
 * Computes shortest paths from a single source vertex to all other vertices
 * (or a specific target) in a graph with non-negative edge weights.
 * </p>
 *
 * @param <V> the type of vertices
 * @param <W> the type of edge weights
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DijkstraShortestPath<V, W> {

    private final WeightedGraph<V, W> graph;
    private final GraphWeightAdapter<W> adapter;

    /**
     * Creates a new Dijkstra algorithm instance.
     * 
     * @param graph   the weighted graph
     * @param adapter the weight adapter for operations
     */
    public DijkstraShortestPath(WeightedGraph<V, W> graph, GraphWeightAdapter<W> adapter) {
        this.graph = Objects.requireNonNull(graph);
        this.adapter = Objects.requireNonNull(adapter);
    }

    /**
     * Creates a new Dijkstra algorithm instance for Real weights.
     * 
     * @param graph the weighted graph with Real weights
     * @param <V>   the vertex type
     * @return a new instance
     */
    public static <V> DijkstraShortestPath<V, Real> ofReal(WeightedGraph<V, Real> graph) {
        return new DijkstraShortestPath<>(graph, GraphWeightAdapter.REAL);
    }

    /**
     * Creates a new Dijkstra algorithm instance for Double weights.
     * 
     * @param graph the weighted graph with Double weights
     * @param <V>   the vertex type
     * @return a new instance
     */
    public static <V> DijkstraShortestPath<V, Double> ofDouble(WeightedGraph<V, Double> graph) {
        return new DijkstraShortestPath<>(graph, GraphWeightAdapter.DOUBLE);
    }

    /**
     * Computes the shortest path from source to target.
     * 
     * @param source the starting vertex
     * @param target the destination vertex
     * @return the list of vertices in the path, or empty list if no path exists
     */
    public List<V> findPath(V source, V target) {
        Map<V, V> predecessors = new HashMap<>();
        Map<V, W> distances = new HashMap<>();
        compute(source, target, predecessors, distances);

        if (!predecessors.containsKey(target) && !source.equals(target)) {
            return Collections.emptyList();
        }

        return reconstructPath(predecessors, source, target);
    }

    /**
     * Computes the length of the shortest path from source to target.
     * 
     * @param source the starting vertex
     * @param target the destination vertex
     * @return the distance, or null if no path exists
     */
    public W getDistance(V source, V target) {
        Map<V, V> predecessors = new HashMap<>();
        Map<V, W> distances = new HashMap<>();
        compute(source, target, predecessors, distances);
        return distances.get(target);
    }

    /**
     * Computes shortest paths from source to all reachable vertices.
     * 
     * @param source the starting vertex
     * @return map of vertex to shortest distance from source
     */
    public Map<V, W> computeAllDistances(V source) {
        Map<V, V> predecessors = new HashMap<>();
        Map<V, W> distances = new HashMap<>();
        compute(source, null, predecessors, distances);
        return distances;
    }

    private void compute(V source, V target, Map<V, V> predecessors, Map<V, W> distances) {
        PriorityQueue<VertexDistance<V, W>> queue = new PriorityQueue<>(
                (a, b) -> adapter.compare(a.distance, b.distance));

        Set<V> visited = new HashSet<>();

        distances.put(source, adapter.zero());
        queue.offer(new VertexDistance<>(source, adapter.zero()));

        while (!queue.isEmpty()) {
            VertexDistance<V, W> current = queue.poll();
            V u = current.vertex;

            if (visited.contains(u))
                continue;
            visited.add(u);

            if (target != null && u.equals(target)) {
                return; // Found target
            }

            // Optimization: if we found a shorter path to u already, skip
            // (Handled by visited set, but also check distance)
            if (adapter.compare(current.distance, distances.get(u)) > 0) {
                continue;
            }

            for (WeightedEdge<V, W> edge : graph.getWeightedEdgesFrom(u)) {
                V v = edge.getTarget();
                if (visited.contains(v))
                    continue;

                W weight = edge.getWeight();
                if (adapter.compare(weight, adapter.zero()) < 0) {
                    throw new IllegalArgumentException("Dijkstra's algorithm does not support negative weights");
                }

                W newDist = adapter.add(distances.get(u), weight);
                W oldDist = distances.get(v);

                if (oldDist == null || adapter.compare(newDist, oldDist) < 0) {
                    distances.put(v, newDist);
                    predecessors.put(v, u);
                    queue.offer(new VertexDistance<>(v, newDist));
                }
            }
        }
    }

    private List<V> reconstructPath(Map<V, V> predecessors, V source, V target) {
        List<V> path = new ArrayList<>();
        V current = target;

        while (current != null) {
            path.add(current);
            if (current.equals(source))
                break;
            current = predecessors.get(current);
        }

        if (path.isEmpty() || !path.get(path.size() - 1).equals(source)) {
            return Collections.emptyList();
        }

        Collections.reverse(path);
        return path;
    }

    private static class VertexDistance<V, W> {
        final V vertex;
        final W distance;

        VertexDistance(V vertex, W distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }
}
