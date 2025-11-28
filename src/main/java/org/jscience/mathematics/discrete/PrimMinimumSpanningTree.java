package org.jscience.mathematics.discrete;

import java.util.*;
import org.jscience.mathematics.number.Real;

/**
 * Implementation of Prim's algorithm for finding the Minimum Spanning Tree.
 * 
 * @param <V> the type of vertices
 * @param <W> the type of weights
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class PrimMinimumSpanningTree<V, W> implements MinimumSpanningTree<V, W> {

    private final GraphWeightAdapter<W> weightAdapter;

    /**
     * Creates a new Prim's MST algorithm instance.
     * 
     * @param weightAdapter adapter for handling weights
     */
    public PrimMinimumSpanningTree(GraphWeightAdapter<W> weightAdapter) {
        this.weightAdapter = weightAdapter;
    }

    /**
     * Creates an instance for Double weights.
     */
    public static <V> PrimMinimumSpanningTree<V, Double> ofDouble() {
        return new PrimMinimumSpanningTree<>(GraphWeightAdapter.DOUBLE);
    }

    /**
     * Creates an instance for Real weights.
     */
    public static <V> PrimMinimumSpanningTree<V, Real> ofReal() {
        return new PrimMinimumSpanningTree<>(GraphWeightAdapter.REAL);
    }

    @Override
    public Set<WeightedEdge<V, W>> findMST(WeightedGraph<V, W> graph) {
        Set<WeightedEdge<V, W>> mst = new HashSet<>();
        Set<V> visited = new HashSet<>();
        Set<V> vertices = graph.vertices();

        if (vertices.isEmpty()) {
            return mst;
        }

        // Start from an arbitrary vertex
        V startVertex = vertices.iterator().next();
        visited.add(startVertex);

        // Priority queue to store edges, ordered by weight
        PriorityQueue<WeightedEdge<V, W>> pq = new PriorityQueue<>(
                (e1, e2) -> weightAdapter.compare(e1.getWeight(), e2.getWeight()));

        // Add initial edges
        addEdgesToPQ(graph, startVertex, pq, visited);

        while (!pq.isEmpty() && visited.size() < vertices.size()) {
            WeightedEdge<V, W> edge = pq.poll();

            V u = edge.getSource();
            V v = edge.getTarget();

            if (visited.contains(u) && visited.contains(v)) {
                continue;
            }

            // Identify the new vertex
            V newVertex = visited.contains(u) ? v : u;

            visited.add(newVertex);
            mst.add(edge);

            addEdgesToPQ(graph, newVertex, pq, visited);
        }

        return mst;
    }

    private void addEdgesToPQ(WeightedGraph<V, W> graph, V vertex,
            PriorityQueue<WeightedEdge<V, W>> pq, Set<V> visited) {
        for (WeightedEdge<V, W> edge : graph.getWeightedEdgesFrom(vertex)) {
            V target = edge.getTarget();
            if (!visited.contains(target)) {
                pq.add(edge);
            }
        }
    }
}
