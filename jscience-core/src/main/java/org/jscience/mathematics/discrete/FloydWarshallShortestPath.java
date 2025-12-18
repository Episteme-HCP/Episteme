package org.jscience.mathematics.discrete;

import java.util.*;

/**
 * Floyd-Warshall algorithm for all-pairs shortest paths.
 * <p>
 * Computes shortest paths between all pairs of vertices in O(V³) time.
 * Can handle negative weights but will detect negative cycles.
 * </p>
 * 
 * @param <V> the type of vertices
 * @param <W> the type of weights
 * 
 * @author Silvere Martin-Michiellot
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
