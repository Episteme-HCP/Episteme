/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.discrete;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

public class MSTTest {

    @Test
    public void testPrimMSTDouble() {
        // Undirected graph simulated with directed edges both ways
        WeightedGraph<String, Double> graph = WeightedGraphBuilder
                .<String, Double>directed()
                .addVertices("A", "B", "C", "D")
                // A-B (1)
                .addEdge("A", "B", 1.0).addEdge("B", "A", 1.0)
                // A-C (3)
                .addEdge("A", "C", 3.0).addEdge("C", "A", 3.0)
                // B-C (1)
                .addEdge("B", "C", 1.0).addEdge("C", "B", 1.0)
                // B-D (4)
                .addEdge("B", "D", 4.0).addEdge("D", "B", 4.0)
                // C-D (1)
                .addEdge("C", "D", 1.0).addEdge("D", "C", 1.0)
                .build();

        PrimMinimumSpanningTree<String, Double> prim = PrimMinimumSpanningTree.ofDouble();
        Set<WeightedEdge<String, Double>> mst = prim.findMST(graph);

        assertEquals(3, mst.size(), "MST should have 3 edges for 4 vertices");

        double totalWeight = 0.0;
        for (WeightedEdge<String, Double> edge : mst) {
            totalWeight += edge.getWeight();
        }

        assertEquals(3.0, totalWeight, 1e-9, "Total weight should be 3.0");

        // Check specific edges (ignoring direction for existence check)
        // Expected: (A,B), (B,C), (C,D) or their reverses
        // Since Prim's explores from start, if we start at A (first added):
        // A->B (1) added. Visited {A, B}
        // From {A, B}: A->C(3), B->C(1), B->D(4). Min is B->C(1).
        // Added B->C. Visited {A, B, C}
        // From {A, B, C}: A->C(3) [skip, C visited], B->D(4), C->D(1). Min is C->D(1).
        // Added C->D. Visited {A, B, C, D}
        // MST Edges: A->B, B->C, C->D.

        // Note: The implementation might pick B->A instead of A->B depending on how
        // edges are stored/iterated.
        // But the total weight is robust.
    }
}


