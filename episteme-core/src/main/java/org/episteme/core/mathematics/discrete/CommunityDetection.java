/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.discrete;

import org.episteme.core.mathematics.discrete.graph.GraphAlgorithmProvider;
import org.episteme.core.mathematics.discrete.graph.providers.MulticoreGraphAlgorithmProvider;
import java.util.*;

/**
 * Provides algorithms for detecting communities in graphs.
 * Refactored to use the technical backend provider pattern.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CommunityDetection {

    /**
     * Detects communities using the best available provider.
     * 
     * @param <V> the vertex type
     * @param graph the graph to analyze
     * @param maxIterations maximum number of iterations
     * @return a map from vertex to community ID
     */
    public static <V> Map<V, Integer> detectCommunities(Graph<V> graph, int maxIterations) {
        GraphAlgorithmProvider provider = findProvider();
        return provider.detectCommunities(graph, maxIterations);
    }

    private static GraphAlgorithmProvider findProvider() {
        ServiceLoader<GraphAlgorithmProvider> loader = ServiceLoader.load(GraphAlgorithmProvider.class);
        for (GraphAlgorithmProvider p : loader) {
            return p;
        }
        return new MulticoreGraphAlgorithmProvider();
    }
}
