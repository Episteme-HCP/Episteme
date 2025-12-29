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
 * A* search algorithm for heuristic-guided shortest path finding.
 * <p>
 * More efficient than Dijkstra when a good heuristic is available.
 * Requires non-negative edge weights.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AStarSearch<V, W> {

    private final GraphWeightAdapter<W> weightAdapter;
    private final AStarHeuristic<V, W> heuristic;

    public AStarSearch(GraphWeightAdapter<W> weightAdapter, AStarHeuristic<V, W> heuristic) {
        this.weightAdapter = weightAdapter;
        this.heuristic = heuristic;
    }

    public static <V> AStarSearch<V, Double> ofDouble(AStarHeuristic<V, Double> heuristic) {
        return new AStarSearch<>(GraphWeightAdapter.DOUBLE, heuristic);
    }

    /**
     * Finds shortest path from source to goal using A* search.
     * 
     * @param graph  the graph
     * @param source source vertex
     * @param goal   goal vertex
     * @return result containing path and distance
     */
    public ShortestPathResult<V, W> findPath(WeightedGraph<V, W> graph, V source, V goal) {
        Map<V, W> gScore = new HashMap<>(); // Cost from source
        Map<V, W> fScore = new HashMap<>(); // Estimated total cost
        Map<V, V> cameFrom = new HashMap<>();

        PriorityQueue<V> openSet = new PriorityQueue<>((a, b) -> {
            W fa = fScore.getOrDefault(a, null);
            W fb = fScore.getOrDefault(b, null);
            if (fa == null)
                return 1;
            if (fb == null)
                return -1;
            return weightAdapter.compare(fa, fb);
        });

        gScore.put(source, weightAdapter.zero());
        fScore.put(source, heuristic.estimate(source, goal));
        openSet.add(source);

        while (!openSet.isEmpty()) {
            V current = openSet.poll();

            if (current.equals(goal)) {
                List<V> path = reconstructPath(cameFrom, source, goal);
                return ShortestPathResult.withPath(source, goal, gScore.get(goal), path);
            }

            for (WeightedEdge<V, W> edge : graph.getWeightedEdgesFrom(current)) {
                V neighbor = edge.getTarget();
                W tentativeGScore = weightAdapter.add(gScore.get(current), edge.getWeight());

                if (!gScore.containsKey(neighbor) ||
                        weightAdapter.compare(tentativeGScore, gScore.get(neighbor)) < 0) {

                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, weightAdapter.add(tentativeGScore, heuristic.estimate(neighbor, goal)));

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return ShortestPathResult.noPath(source, goal);
    }

    private List<V> reconstructPath(Map<V, V> cameFrom, V source, V goal) {
        List<V> path = new ArrayList<>();
        V current = goal;

        while (!current.equals(source)) {
            path.add(current);
            current = cameFrom.get(current);
        }

        path.add(source);
        Collections.reverse(path);
        return path;
    }
}
