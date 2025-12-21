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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BellmanFordTest {

    @Test
    public void testPositiveWeights() {
        // Same graph as Dijkstra test - should get same result
        WeightedGraph<String, Double> graph = WeightedGraphBuilder
                .<String, Double>directed()
                .addVertices("A", "B", "C", "D")
                .addEdge("A", "B", 1.0)
                .addEdge("A", "C", 4.0)
                .addEdge("B", "C", 2.0)
                .addEdge("B", "D", 5.0)
                .addEdge("C", "D", 1.0)
                .build();

        BellmanFordShortestPath<String, Double> bf = BellmanFordShortestPath.ofDouble();
        ShortestPathResult<String, Double> result = bf.findPath(graph, "A", "D");

        assertTrue(result.hasPath());
        assertFalse(result.hasNegativeCycle());
        assertEquals(4.0, result.getDistance().get(), 1e-9);
        assertEquals("[A, B, C, D]", result.getPath().get().toString());
    }

    @Test
    public void testNegativeWeights() {
        // Graph with negative weights (but no negative cycle)
        WeightedGraph<String, Double> graph = WeightedGraphBuilder
                .<String, Double>directed()
                .addVertices("A", "B", "C")
                .addEdge("A", "B", 5.0)
                .addEdge("A", "C", 2.0)
                .addEdge("C", "B", -4.0) // Negative weight
                .build();

        BellmanFordShortestPath<String, Double> bf = BellmanFordShortestPath.ofDouble();
        ShortestPathResult<String, Double> result = bf.findPath(graph, "A", "B");

        assertTrue(result.hasPath());
        assertFalse(result.hasNegativeCycle());
        // Path A->C->B = 2 + (-4) = -2 is better than A->B = 5
        assertEquals(-2.0, result.getDistance().get(), 1e-9);
        assertEquals("[A, C, B]", result.getPath().get().toString());
    }

    @Test
    public void testNegativeCycleDetection() {
        // Graph with negative cycle: A->B->C->A has total weight -1
        WeightedGraph<String, Double> graph = WeightedGraphBuilder
                .<String, Double>directed()
                .addVertices("A", "B", "C")
                .addEdge("A", "B", 1.0)
                .addEdge("B", "C", -2.0)
                .addEdge("C", "A", -1.0) // Creates negative cycle
                .build();

        BellmanFordShortestPath<String, Double> bf = BellmanFordShortestPath.ofDouble();
        ShortestPathResult<String, Double> result = bf.findPath(graph, "A", "B");

        assertTrue(result.hasNegativeCycle());
        assertFalse(result.hasPath());
    }

    @Test
    public void testUnreachableVertex() {
        WeightedGraph<String, Double> graph = WeightedGraphBuilder
                .<String, Double>directed()
                .addVertices("A", "B", "C")
                .addEdge("A", "B", 1.0)
                // C is unreachable from A
                .build();

        BellmanFordShortestPath<String, Double> bf = BellmanFordShortestPath.ofDouble();
        ShortestPathResult<String, Double> result = bf.findPath(graph, "A", "C");

        assertFalse(result.hasPath());
        assertFalse(result.hasNegativeCycle());
        assertTrue(result.getDistance().isEmpty());
    }
}
