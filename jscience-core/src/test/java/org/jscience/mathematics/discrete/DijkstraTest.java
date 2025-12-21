/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
import java.util.Arrays;

public class DijkstraTest {

    @Test
    public void testDijkstraWithReal() {
        WeightedGraph<String, Real> graph = WeightedGraphBuilder
                .<String, Real>directed()
                .addVertices("A", "B", "C", "D")
                .addEdge("A", "B", Real.of(10.0))
                .addEdge("A", "C", Real.of(5.0))
                .addEdge("C", "B", Real.of(2.0))
                .addEdge("B", "D", Real.of(1.0))
                .build();

        DijkstraShortestPath<String, Real> dijkstra = DijkstraShortestPath.ofReal(graph);

        // Path A -> D
        List<String> path = dijkstra.findPath("A", "D");
        Real distance = dijkstra.getDistance("A", "D");

        // Expected: A -> C -> B -> D (Distance 8.0)
        assertEquals(Arrays.asList("A", "C", "B", "D"), path, "Path should be A->C->B->D");
        assertEquals(Real.of(8.0), distance, "Distance should be 8.0");
    }

    @Test
    public void testDijkstraWithDouble() {
        WeightedGraph<String, Double> graph = WeightedGraphBuilder
                .<String, Double>directed()
                .addVertices("S", "U", "V", "T")
                .addEdge("S", "U", 10.0)
                .addEdge("S", "V", 5.0)
                .addEdge("V", "U", 3.0) // Better path to U via V
                .addEdge("U", "T", 1.0)
                .addEdge("V", "T", 9.0)
                .build();

        DijkstraShortestPath<String, Double> dijkstra = DijkstraShortestPath.ofDouble(graph);

        // Path S -> T
        // S->V(5) + V->U(3) + U->T(1) = 9.0
        List<String> path = dijkstra.findPath("S", "T");
        Double distance = dijkstra.getDistance("S", "T");

        assertEquals(Arrays.asList("S", "V", "U", "T"), path, "Path should be S->V->U->T");
        assertEquals(9.0, distance, 1e-9, "Distance should be 9.0");
    }
}
