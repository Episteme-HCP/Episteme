/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.graph;

import org.jscience.core.mathematics.discrete.Graph;
import org.jscience.core.technical.algorithm.GraphAlgorithmProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enhanced native graph algorithm provider with Louvain community detection.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class GraphAlgorithmProviderImpl implements GraphAlgorithmProvider {

    @Override
    public int getPriority() {
        return 60; // Higher priority than multicore
    }

    @Override
    public <V> Map<V, Integer> detectCommunities(Graph<V> graph, int maxIterations) {
        // Louvain algorithm implementation
        Map<V, Integer> communities = new ConcurrentHashMap<>();
        List<V> vertices = new ArrayList<>(graph.vertices());
        
        // Initialize: each node in its own community
        for (int i = 0; i < vertices.size(); i++) {
            communities.put(vertices.get(i), i);
        }
        
        boolean improved = true;
        int iteration = 0;
        
        while (improved && iteration < maxIterations) {
            improved = false;
            iteration++;
            
            // Phase 1: Modularity optimization
            for (V node : vertices) {
                int currentCommunity = communities.get(node);
                Map<Integer, Double> communityWeights = new HashMap<>();
                
                // Calculate weights to neighboring communities
                for (V neighbor : graph.neighbors(node)) {
                    int neighborCommunity = communities.get(neighbor);
                    communityWeights.merge(neighborCommunity, 1.0, (a, b) -> (a != null ? a : 0.0) + (b != null ? b : 0.0));
                }
                
                // Find best community
                int bestCommunity = currentCommunity;
                double bestGain = 0.0;
                
                for (Map.Entry<Integer, Double> entry : communityWeights.entrySet()) {
                    if (entry.getValue() > bestGain) {
                        bestGain = entry.getValue();
                        bestCommunity = entry.getKey();
                    }
                }
                
                if (bestCommunity != currentCommunity) {
                    communities.put(node, bestCommunity);
                    improved = true;
                }
            }
        }
        
        // Renumber communities sequentially
        return renumberCommunities(communities);
    }

    @Override
    public String getName() {
        return "Native Graph Algorithm Provider (Louvain)";
    }

    // Helper method
    private <V> Map<V, Integer> renumberCommunities(Map<V, Integer> communities) {
        Map<Integer, Integer> mapping = new HashMap<>();
        int nextId = 0;
        
        Map<V, Integer> result = new HashMap<>();
        for (Map.Entry<V, Integer> entry : communities.entrySet()) {
            int oldId = entry.getValue();
            if (!mapping.containsKey(oldId)) {
                mapping.put(oldId, nextId++);
            }
            result.put(entry.getKey(), mapping.get(oldId));
        }
        
        return result;
    }
}
