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

import java.util.Objects;

/**
 * Default implementation of a weighted edge.
 * 
 * @param <V> the type of vertices
 * @param <W> the type of weights
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class DefaultWeightedEdge<V, W> implements WeightedEdge<V, W> {

    private final V source;
    private final V target;
    private final W weight;

    /**
     * Creates a new weighted edge.
     * 
     * @param source the source vertex
     * @param target the target vertex
     * @param weight the edge weight
     */
    public DefaultWeightedEdge(V source, V target, W weight) {
        this.source = Objects.requireNonNull(source, "Source vertex cannot be null");
        this.target = Objects.requireNonNull(target, "Target vertex cannot be null");
        this.weight = Objects.requireNonNull(weight, "Weight cannot be null");
    }

    @Override
    public V getSource() {
        return source;
    }

    @Override
    public V getTarget() {
        return target;
    }

    @Override
    public W getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof WeightedEdge))
            return false;
        WeightedEdge<?, ?> other = (WeightedEdge<?, ?>) obj;
        return source.equals(other.getSource()) &&
                target.equals(other.getTarget()) &&
                weight.equals(other.getWeight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, weight);
    }

    @Override
    public String toString() {
        return source + " --(" + weight + ")--> " + target;
    }
}