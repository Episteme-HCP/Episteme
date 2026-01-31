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

package org.jscience.social.sociology;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Provides primitive algorithms and metrics for Social Network Analysis (SNA).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class SocialNetworkAnalysis {

    private SocialNetworkAnalysis() {}

    /**
     * Calculates the degree centrality of each node in a network graph.
     * Degree centrality is defined as the number of links incident upon a node.
     * 
     * @param <T>           the type of the node identifier
     * @param adjacencyList a map representing the graph, where keys are nodes and values are lists of neighbors
     * @param normalized    if true, the centrality score is normalized by dividing by (n-1)
     * @return a map associating each node with its centrality score
     */
    public static <T> Map<T, Real> calculateDegreeCentrality(Map<T, List<T>> adjacencyList, boolean normalized) {
        Map<T, Real> centralities = new HashMap<>();
        if (adjacencyList == null) return centralities;

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

