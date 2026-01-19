package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Social network analysis primitives.
 */
public final class SocialNetworkAnalysis {

    private SocialNetworkAnalysis() {}

    /**
     * Calculates the degree centrality of each node in a graph.
     * 
     * @param adjacencyList Mapping of nodes to their neighbors.
     * @param normalized Whether to divide by (n-1).
     * @return Map of nodes to their centrality scores.
     */
    public static <T> Map<T, Real> calculateDegreeCentrality(Map<T, List<T>> adjacencyList, boolean normalized) {
        Map<T, Real> centralities = new HashMap<>();
        int n = adjacencyList.size();
        
        for (Map.Entry<T, List<T>> entry : adjacencyList.entrySet()) {
            int degree = entry.getValue() != null ? entry.getValue().size() : 0;
            Real score = Real.of(degree);
            if (normalized && n > 1) {
                score = score.divide(Real.of(n - 1));
            }
            centralities.put(entry.getKey(), score);
        }
        return centralities;
    }
}
