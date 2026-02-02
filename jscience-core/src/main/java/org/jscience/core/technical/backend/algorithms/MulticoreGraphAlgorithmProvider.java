/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.core.technical.backend.algorithms;

import org.jscience.core.mathematics.discrete.Graph;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Multicore implementation of graph algorithms.
 * Uses parallel processing for community detection.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MulticoreGraphAlgorithmProvider implements GraphAlgorithmProvider {

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
            
            // For LPA, we process vertices and update labels based on neighbors' most frequent label.
            // In multicore, we use a snapshot for consistency during one sweep.
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
