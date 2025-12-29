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

/**
 * An edge in a weighted graph.
 * <p>
 * Represents a directed edge from a source vertex to a target vertex
 * with an associated weight. Can also represent undirected edges.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface WeightedEdge<V, W> {

    /**
     * Returns the source vertex of this edge.
     * 
     * @return the source vertex
     */
    V getSource();

    /**
     * Returns the target vertex of this edge.
     * 
     * @return the target vertex
     */
    V getTarget();

    /**
     * Returns the weight of this edge.
     * 
     * @return the edge weight
     */
    W getWeight();

    /**
     * Checks if this edge connects the given vertices.
     * <p>
     * For undirected interpretation, returns true if the edge connects
     * u to v OR v to u.
     * </p>
     * 
     * @param u the first vertex
     * @param v the second vertex
     * @return true if this edge connects u and v (in either direction)
     */
    default boolean connects(V u, V v) {
        return (getSource().equals(u) && getTarget().equals(v)) ||
                (getSource().equals(v) && getTarget().equals(u));
    }

    /**
     * Checks if this edge is incident to the given vertex.
     * 
     * @param vertex the vertex to check
     * @return true if vertex is source or target
     */
    default boolean isIncidentTo(V vertex) {
        return getSource().equals(vertex) || getTarget().equals(vertex);
    }
}