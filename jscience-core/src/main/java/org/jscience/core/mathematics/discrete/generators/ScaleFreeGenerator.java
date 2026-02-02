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
 * Generator for Scale-Free networks using the Barabási–Albert model.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ScaleFreeGenerator {

    /**
     * Generates a scale-free network.
     * 
     * @param n total number of nodes to generate
     * @param m0 initial number of nodes (fully connected)
     * @param m number of edges to add for each new node (m <= m0)
     * @return a scale-free graph
     */
    public static Graph<Natural> generate(int n, int m0, int m) {
        if (m > m0) throw new IllegalArgumentException("m must be <= m0.");
        
        UndirectedGraph<Natural> graph = new UndirectedGraph<>();
        List<Natural> attachmentBuffer = new ArrayList<>();
        
        // 1. Create initial seed (fully connected)
        for (int i = 0; i < m0; i++) {
            graph.addVertex(Natural.of(i));
        }
        for (int i = 0; i < m0; i++) {
            for (int j = i + 1; j < m0; j++) {
                Natural vI = Natural.of(i);
                Natural vJ = Natural.of(j);
                graph.addEdge(vI, vJ);
                attachmentBuffer.add(vI);
                attachmentBuffer.add(vJ);
            }
        }
        
        // 2. Add remaining nodes with preferential attachment
        Random rand = new Random();
        for (int i = m0; i < n; i++) {
            Natural vNew = Natural.of(i);
            graph.addVertex(vNew);
            Set<Natural> targets = new HashSet<>();
            while (targets.size() < m) {
                Natural target = attachmentBuffer.get(rand.nextInt(attachmentBuffer.size()));
                targets.add(target);
            }
            for (Natural target : targets) {
                graph.addEdge(vNew, target);
                attachmentBuffer.add(vNew);
                attachmentBuffer.add(target);
            }
        }
        
        return graph;
    }
}
