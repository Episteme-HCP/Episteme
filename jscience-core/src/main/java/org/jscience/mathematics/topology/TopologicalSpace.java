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

package org.jscience.mathematics.topology;

import org.jscience.mathematics.structures.sets.Set;

/**
 * Represents a topological space.
 * <p>
 * A topological space is a set equipped with a topology, which defines
 * which subsets are considered "open".
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface TopologicalSpace<T> extends Set<T> {

    /**
     * Checks if this space contains the given point.
     * <p>
     * Named containsPoint to avoid erasure conflict with Set.contains.
     * </p>
     * 
     * @param point the point to check
     * @return true if the point is in this space
     */
    boolean containsPoint(T point);

    /**
     * Checks if this set is open in the topology.
     * 
     * @return true if this is an open set
     */
    boolean isOpen();

    /**
     * Checks if this set is closed in the topology.
     * 
     * @return true if this is a closed set
     */
    boolean isClosed();
}
