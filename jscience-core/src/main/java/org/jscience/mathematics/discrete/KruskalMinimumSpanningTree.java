package org.jscience.mathematics.discrete;

import java.util.*;

/**
 * Kruskal's algorithm for finding Minimum Spanning Tree.
 * <p>
 * Alternative to Prim's algorithm. Sorts edges by weight and
 * greedily adds them if they don't create a cycle.
 * Uses Union-Find data structure. O(E log E) time.
 * </p>
 * 
 * @param <V> the type of vertices
 * @param <W> the type of weights
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class KruskalMinimumSpanningTree<V, W> implements MinimumSpanningTree<V, W> {

    private final GraphWeightAdapter<W> weightAdapter;

    public KruskalMinimumSpanningTree(GraphWeightAdapter<W> weightAdapter) {
        this.weightAdapter = weightAdapter;
    }

    public static <V> KruskalMinimumSpanningTree<V, Double> ofDouble() {
        return new KruskalMinimumSpanningTree<>(GraphWeightAdapter.DOUBLE);
    }

    public static <V> KruskalMinimumSpanningTree<V, org.jscience.mathematics.numbers.real.Real> ofReal() {
        return new KruskalMinimumSpanningTree<>(GraphWeightAdapter.REAL);
    }

    @Override
    public Set<WeightedEdge<V, W>> findMST(WeightedGraph<V, W> graph) {
        Set<WeightedEdge<V, W>> mst = new HashSet<>();
        List<WeightedEdge<V, W>> edges = new ArrayList<>(graph.getWeightedEdges());

        // Sort edges by weight
        edges.sort((e1, e2) -> weightAdapter.compare(e1.getWeight(), e2.getWeight()));

        UnionFind<V> uf = new UnionFind<>();
        for (V vertex : graph.vertices()) {
            uf.makeSet(vertex);
        }

        for (WeightedEdge<V, W> edge : edges) {
            V u = edge.getSource();
            V v = edge.getTarget();

            if (uf.find(u) != uf.find(v)) {
                mst.add(edge);
                uf.union(u, v);

                if (mst.size() == graph.vertices().size() - 1) {
                    break; // MST complete
                }
            }
        }

        return mst;
    }

    /**
     * Union-Find (Disjoint Set) data structure.
     * Supports near-constant time union and find operations.
     */
    private static class UnionFind<V> {
        private final Map<V, V> parent = new HashMap<>();
        private final Map<V, Integer> rank = new HashMap<>();

        void makeSet(V x) {
            parent.put(x, x);
            rank.put(x, 0);
        }

        V find(V x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x))); // Path compression
            }
            return parent.get(x);
        }

        void union(V x, V y) {
            V rootX = find(x);
            V rootY = find(y);

            if (rootX.equals(rootY))
                return;

            // Union by rank
            if (rank.get(rootX) < rank.get(rootY)) {
                parent.put(rootX, rootY);
            } else if (rank.get(rootX) > rank.get(rootY)) {
                parent.put(rootY, rootX);
            } else {
                parent.put(rootY, rootX);
                rank.put(rootX, rank.get(rootX) + 1);
            }
        }
    }
}
