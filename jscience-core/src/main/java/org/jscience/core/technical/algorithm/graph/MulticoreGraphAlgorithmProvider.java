/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.graph;

import org.jscience.core.mathematics.discrete.Graph;
import org.jscience.core.technical.algorithm.GraphAlgorithmProvider;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Multicore implementation of graph algorithms.
 * Uses parallel processing for community detection.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class MulticoreGraphAlgorithmProvider implements GraphAlgorithmProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public String getName() {
        return "Multicore Graph Algorithms (CPU)";
    }

    @Override
    public <V> Map<V, Integer> detectCommunities(Graph<V> graph, int maxIterations) {
        Map<V, Integer> labels = new ConcurrentHashMap<>();
        int labelId = 0;
        List<V> vertices = new ArrayList<>(graph.vertices());
        
        for (V v : vertices) {
            labels.put(v, labelId++);
        }
        
        Random rand = new Random();
        for (int iter = 0; iter < maxIterations; iter++) {
            Collections.shuffle(vertices, rand);
            Map<V, Integer> snapshot = new HashMap<>(labels);
            
            long changeCount = vertices.parallelStream().mapToLong(v -> {
                Set<V> neighbors = graph.neighbors(v);
                if (neighbors.isEmpty()) return 0;
                
                Map<Integer, Integer> counts = new HashMap<>();
                for (V neighbor : neighbors) {
                    Integer label = snapshot.get(neighbor);
                    if (label != null) {
                        counts.put(label, counts.getOrDefault(label, 0) + 1);
                    }
                }
                
                if (counts.isEmpty()) return 0;

                int maxCount = -1;
                List<Integer> bestLabels = new ArrayList<>();
                for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
                    if (entry.getValue() > maxCount) {
                        maxCount = entry.getValue();
                        bestLabels.clear();
                        bestLabels.add(entry.getKey());
                    } else if (entry.getValue() == maxCount) {
                        bestLabels.add(entry.getKey());
                    }
                }
                
                int newLabel = bestLabels.get(rand.nextInt(bestLabels.size()));
                Integer oldLabel = snapshot.get(v);
                if (oldLabel == null || oldLabel != newLabel) {
                    labels.put(v, newLabel);
                    return 1;
                }
                return 0;
            }).sum();

            if (changeCount == 0) break;
        }
        
        return labels;
    }
}
