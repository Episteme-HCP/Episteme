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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.discrete;

import java.util.Set;

/**
 * A graph with weighted edges.
 * <p>
 * Extends the basic Graph interface to support edges with numeric weights.
 * Weights can be any comparable type (Real, Integer, Double, etc.).
 * </p>
 *
 * @param <V> the type of vertices
 * @param <W> the type of edge weights
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface WeightedGraph<V, W> extends Graph<V> {

    /**
     * Adds a weighted edge to the graph.
     * <p>
     * Vertices are automatically added if not already present.
     * </p>
     * 
     * @param source the source vertex
     * @param target the target vertex
     * @param weight the edge weight
     * @return true if the edge was added
     */
    boolean addEdge(V source, V target, W weight);

    /**
     * Returns the weight of an edge.
     * 
     * @param source the source vertex
     * @param target the target vertex
     * @return the edge weight, or null if no such edge exists
     */
    W getWeight(V source, V target);

    /**
     * Returns all weighted edges in the graph.
     * 
     * @return unmodifiable set of weighted edges
     */
    Set<WeightedEdge<V, W>> getWeightedEdges();

    /**
     * Returns weighted edges originating from a vertex.
     * 
     * @param vertex the source vertex
     * @return set of weighted edges from vertex
     */
    Set<WeightedEdge<V, W>> getWeightedEdgesFrom(V vertex);

    /**
     * Checks if an edge exists with a specific weight.
     * 
     * @param source the source vertex
     * @param target the target vertex
     * @param weight the expected weight
     * @return true if edge exists with given weight
     */
    default boolean hasEdge(V source, V target, W weight) {
        W actualWeight = getWeight(source, target);
        return actualWeight != null && actualWeight.equals(weight);
    }

    /**
     * Returns the default weight for unweighted edge additions.
     * <p>
     * Used when addEdge(V, V) is called without a weight.
     * Subclasses should override to provide appropriate default.
     * </p>
     * 
     * @return the default weight (e.g., 1.0, Real.ONE)
     */
    W getDefaultWeight();

    /**
     * Adds an unweighted edge using the default weight.
     * <p>
     * Maintains backward compatibility with Graph interface.
     * </p>
     */
    @Override
    default boolean addEdge(V source, V target) {
        return addEdge(source, target, getDefaultWeight());
    }
}
