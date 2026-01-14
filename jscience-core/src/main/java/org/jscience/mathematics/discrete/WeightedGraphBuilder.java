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

package org.jscience.mathematics.discrete;

/**
 * Fluent builder for weighted graphs.
 * <p>
 * Provides a convenient API for constructing weighted graphs:
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class WeightedGraphBuilder<V, W> {

    private final WeightedGraph<V, W> graph;

    private WeightedGraphBuilder(WeightedGraph<V, W> graph) {
        this.graph = graph;
    }

    /**
     * Creates a builder for a directed weighted graph.
     * 
     * @param <V> the type of vertices
     * @param <W> the type of edge weights
     * @return a new builder
     */
    public static <V, W> WeightedGraphBuilder<V, W> directed() {
        return new WeightedGraphBuilder<>(new DirectedWeightedGraph<>());
    }

    /**
     * Creates a builder for a directed weighted graph with custom default weight.
     * 
     * @param <V>           the type of vertices
     * @param <W>           the type of edge weights
     * @param defaultWeight the default weight for unweighted edges
     * @return a new builder
     */
    public static <V, W> WeightedGraphBuilder<V, W> directed(W defaultWeight) {
        return new WeightedGraphBuilder<>(new DirectedWeightedGraph<>(defaultWeight));
    }

    /**
     * Adds a vertex to the graph.
     * 
     * @param vertex the vertex to add
     * @return this builder for chaining
     */
    public WeightedGraphBuilder<V, W> addVertex(V vertex) {
        graph.addVertex(vertex);
        return this;
    }

    /**
     * Adds multiple vertices to the graph.
     * 
     * @param vertices the vertices to add
     * @return this builder for chaining
     */
    @SafeVarargs
    public final WeightedGraphBuilder<V, W> addVertices(V... vertices) {
        for (V vertex : vertices) {
            graph.addVertex(vertex);
        }
        return this;
    }

    /**
     * Adds a weighted edge to the graph.
     * 
     * @param source the source vertex
     * @param target the target vertex
     * @param weight the edge weight
     * @return this builder for chaining
     */
    public WeightedGraphBuilder<V, W> addEdge(V source, V target, W weight) {
        graph.addEdge(source, target, weight);
        return this;
    }

    /**
     * Adds an unweighted edge using the default weight.
     * 
     * @param source the source vertex
     * @param target the target vertex
     * @return this builder for chaining
     */
    public WeightedGraphBuilder<V, W> addEdge(V source, V target) {
        graph.addEdge(source, target);
        return this;
    }

    /**
     * Builds and returns the graph.
     * 
     * @return the constructed weighted graph
     */
    public WeightedGraph<V, W> build() {
        return graph;
    }
}

