package org.jscience.mathematics.discrete;

import java.util.Set;

/**
 * Interface for Minimum Spanning Tree (MST) algorithms.
 * 
 * @param <V> the type of vertices
 * @param <W> the type of weights
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public interface MinimumSpanningTree<V, W> {

    /**
     * Finds the Minimum Spanning Tree of the given graph.
     * 
     * @param graph the graph to find the MST for
     * @return a set of edges representing the MST
     */
    Set<WeightedEdge<V, W>> findMST(WeightedGraph<V, W> graph);
}
