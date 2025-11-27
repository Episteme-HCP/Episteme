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

import org.jscience.mathematics.number.Real;
import java.util.Comparator;

/**
 * Adapter for handling generic edge weights in graph algorithms.
 * <p>
 * Defines the necessary operations (addition, zero, comparison) for
 * algorithms like Dijkstra's shortest path or Minimum Spanning Tree
 * to work with any weight type.
 * </p>
 *
 * @param <W> the type of weight
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface GraphWeightAdapter<W> extends Comparator<W> {

    /**
     * Returns the zero element (identity for addition).
     * 
     * @return the zero weight
     */
    W zero();

    /**
     * Adds two weights.
     * 
     * @param a the first weight
     * @param b the second weight
     * @return the sum of a and b
     */
    W add(W a, W b);

    /**
     * Compares two weights.
     * 
     * @param a the first weight
     * @param b the second weight
     * @return negative if a < b, zero if a = b, positive if a > b
     */
    @Override
    int compare(W a, W b);

    // --- Standard Implementations ---

    /**
     * Adapter for {@link org.jscience.mathematics.number.Real}.
     */
    GraphWeightAdapter<Real> REAL = new GraphWeightAdapter<Real>() {
        @Override
        public Real zero() {
            return Real.ZERO;
        }

        @Override
        public Real add(Real a, Real b) {
            return a.add(b);
        }

        @Override
        public int compare(Real a, Real b) {
            return a.compareTo(b);
        }
    };

    /**
     * Adapter for {@link java.lang.Double}.
     */
    GraphWeightAdapter<Double> DOUBLE = new GraphWeightAdapter<Double>() {
        @Override
        public Double zero() {
            return 0.0;
        }

        @Override
        public Double add(Double a, Double b) {
            return a + b;
        }

        @Override
        public int compare(Double a, Double b) {
            return Double.compare(a, b);
        }
    };

    /**
     * Adapter for {@link java.lang.Integer}.
     */
    GraphWeightAdapter<Integer> INTEGER = new GraphWeightAdapter<Integer>() {
        @Override
        public Integer zero() {
            return 0;
        }

        @Override
        public Integer add(Integer a, Integer b) {
            return a + b;
        }

        @Override
        public int compare(Integer a, Integer b) {
            return Integer.compare(a, b);
        }
    };
}
