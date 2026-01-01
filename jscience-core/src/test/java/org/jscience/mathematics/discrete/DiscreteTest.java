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

import org.jscience.mathematics.numbers.real.Real;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Tests for Discrete Math operations (Graphs).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DiscreteTest {

    @Test
    public void testDijkstra() {
        // Graph:
        // A --1--> B --2--> C
        // | ^
        // 4 | 1
        // v |
        // D --1--> E

        WeightedGraph<String, Real> graph = new DirectedWeightedGraph<>();
        graph.addEdge("A", "B", Real.of(1));
        graph.addEdge("B", "C", Real.of(2));
        graph.addEdge("A", "D", Real.of(4));
        graph.addEdge("D", "E", Real.of(1));
        graph.addEdge("E", "B", Real.of(1));

        // Shortest path A -> C:
        // A -> B -> C (Cost 1 + 2 = 3)
        // A -> D -> E -> B -> C (Cost 4 + 1 + 1 + 2 = 8)

        DijkstraShortestPath<String, Real> dijkstra = DijkstraShortestPath.ofReal(graph);
        List<String> path = dijkstra.findPath("A", "C");
        assertEquals(3, path.size()); // A, B, C
        assertEquals("A", path.get(0));
        assertEquals("B", path.get(1));
        assertEquals("C", path.get(2));
    }
}

