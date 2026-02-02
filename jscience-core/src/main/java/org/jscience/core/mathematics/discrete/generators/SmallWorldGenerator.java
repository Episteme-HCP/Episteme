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

package org.jscience.core.mathematics.discrete.generators;

import org.jscience.core.mathematics.discrete.Graph;
import org.jscience.core.mathematics.discrete.UndirectedGraph;
import org.jscience.core.mathematics.numbers.integers.Natural;
import java.util.*;

/**
 * Generator for Small-World networks using the Watts-Strogatz model.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SmallWorldGenerator {

    /**
     * Generates a small-world network.
     * 
     * @param n the number of nodes
     * @param k the mean degree (must be even)
     * @param beta the rewriting probability (0 to 1)
     * @return a small-world graph
     */
    public static Graph<Natural> generate(int n, int k, double beta) {
        if (k % 2 != 0) throw new IllegalArgumentException("k must be even.");
        
        UndirectedGraph<Natural> graph = new UndirectedGraph<>();
        
        // 1. Create a ring lattice
        for (int i = 0; i < n; i++) {
            graph.addVertex(Natural.of(i));
        }
        
        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k / 2; j++) {
                graph.addEdge(Natural.of(i), Natural.of((i + j) % n));
            }
        }
        
        // 2. Rewire edges
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k / 2; j++) {
                if (rand.nextDouble() < beta) {
                    // Choose a new target and adding it as a shortcut
                    Natural source = Natural.of(i);
                    Natural newTarget;
                    do {
                        newTarget = Natural.of(rand.nextInt(n));
                    } while (newTarget.equals(source) || graph.neighbors(source).contains(newTarget));
                    
                    graph.addEdge(source, newTarget);
                }
            }
        }
        
        return graph;
    }
}
