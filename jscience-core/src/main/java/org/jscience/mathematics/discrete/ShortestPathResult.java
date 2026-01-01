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

import java.util.List;
import java.util.Optional;

/**
 * Result of a shortest path computation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ShortestPathResult<V, W> {

    private final V source;
    private final V target;
    private final W distance;
    private final List<V> path;
    private final boolean hasPath;
    private final boolean hasNegativeCycle;

    private ShortestPathResult(V source, V target, W distance, List<V> path,
            boolean hasPath, boolean hasNegativeCycle) {
        this.source = source;
        this.target = target;
        this.distance = distance;
        this.path = path;
        this.hasPath = hasPath;
        this.hasNegativeCycle = hasNegativeCycle;
    }

    /**
     * Creates a successful path result.
     */
    public static <V, W> ShortestPathResult<V, W> withPath(V source, V target,
            W distance, List<V> path) {
        return new ShortestPathResult<>(source, target, distance, path, true, false);
    }

    /**
     * Creates a no-path result (unreachable).
     */
    public static <V, W> ShortestPathResult<V, W> noPath(V source, V target) {
        return new ShortestPathResult<>(source, target, null, null, false, false);
    }

    /**
     * Creates a negative cycle result.
     */
    public static <V, W> ShortestPathResult<V, W> negativeCycle(V source, V target) {
        return new ShortestPathResult<>(source, target, null, null, false, true);
    }

    public V getSource() {
        return source;
    }

    public V getTarget() {
        return target;
    }

    public Optional<W> getDistance() {
        return Optional.ofNullable(distance);
    }

    public Optional<List<V>> getPath() {
        return Optional.ofNullable(path);
    }

    public boolean hasPath() {
        return hasPath;
    }

    public boolean hasNegativeCycle() {
        return hasNegativeCycle;
    }

    @Override
    public String toString() {
        if (hasNegativeCycle) {
            return "ShortestPath[NEGATIVE CYCLE: " + source + " -> " + target + "]";
        }
        if (!hasPath) {
            return "ShortestPath[NO PATH: " + source + " -> " + target + "]";
        }
        return "ShortestPath[" + source + " -> " + target + " = " + distance + ", path=" + path + "]";
    }
}


