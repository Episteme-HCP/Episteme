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
package org.jscience.mathematics.topology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a metric (distance function).
 * <p>
 * A metric must satisfy:
 * <ul>
 * <li>Non-negativity: d(x,y) ≥ 0</li>
 * <li>Identity: d(x,y) = 0 ⟺ x = y</li>
 * <li>Symmetry: d(x,y) = d(y,x)</li>
 * <li>Triangle inequality: d(x,z) ≤ d(x,y) + d(y,z)</li>
 * </ul>
 * </p>
 * 
 * @param <T> the type of objects being measured
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Metric<T> {

    /**
     * Computes the distance between two objects.
     * 
     * @param a the first object
     * @param b the second object
     * @return the distance between a and b (always non-negative)
     */
    Real distance(T a, T b);
}

