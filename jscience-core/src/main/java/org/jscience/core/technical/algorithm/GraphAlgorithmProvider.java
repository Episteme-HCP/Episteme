/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.discrete.Graph;
import java.util.Map;

/**
 * Service provider interface for graph algorithms.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface GraphAlgorithmProvider extends AlgorithmProvider {
    
    /**
     * Detects communities in a graph.
     */
    <V> Map<V, Integer> detectCommunities(Graph<V> graph, int maxIterations);

    @Override
    default String getName() {
        return "Graph Algorithm Provider";
    }
}
