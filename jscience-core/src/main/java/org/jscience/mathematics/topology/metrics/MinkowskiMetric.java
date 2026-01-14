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

package org.jscience.mathematics.topology.metrics;

import org.jscience.mathematics.topology.Metric;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;

/**
 * Minkowski metric (Lp norm).
 * <p>
 * d(x,y) = (ÃŽÂ£|xÃ¡ÂµÂ¢ - yÃ¡ÂµÂ¢|Ã¡Âµâ€“)^(1/p)
 * </p>
 * <p>
 * Special cases:
 * - p = 1: Manhattan metric
 * - p = 2: Euclidean metric
 * - p = Ã¢Ë†Å¾: Chebyshev metric
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MinkowskiMetric implements Metric<Vector<Real>> {

    private final Real p;

    /**
     * Creates a Minkowski metric with the given p value.
     * 
     * @param p the power (must be >= 1)
     */
    public MinkowskiMetric(Real p) {
        if (p.compareTo(Real.ONE) < 0) {
            throw new IllegalArgumentException("p must be >= 1");
        }
        this.p = p;
    }

    /**
     * Creates a Minkowski metric with the given p value.
     * 
     * @param p the power (must be >= 1)
     */
    public MinkowskiMetric(double p) {
        this(Real.of(p));
    }

    @Override
    public Real distance(Vector<Real> a, Vector<Real> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }

        Real sum = Real.ZERO;
        for (int i = 0; i < a.dimension(); i++) {
            Real diff = a.get(i).subtract(b.get(i)).abs();
            sum = sum.add(diff.pow(p));
        }

        return sum.pow(Real.ONE.divide(p));
    }

    /**
     * Returns the p value.
     * 
     * @return the power
     */
    public Real getP() {
        return p;
    }
}


