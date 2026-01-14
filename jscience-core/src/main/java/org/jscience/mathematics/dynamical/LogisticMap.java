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

package org.jscience.mathematics.dynamical;

/**
 * Represents the Logistic map: x_{n+1} = r * x_n * (1 - x_n).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LogisticMap implements DiscreteMap {

    private final double r;

    /**
     * Creates a Logistic map with parameter r.
     *
     * @param r the growth rate parameter (typically [0, 4])
     */
    public LogisticMap(double r) {
        this.r = r;
    }

    @Override
    public double[] map(double[] state) {
        if (state.length != 1) {
            throw new IllegalArgumentException("Logistic map is 1-dimensional");
        }
        double x = state[0];
        return new double[] { r * x * (1.0 - x) };
    }

    @Override
    public int dimensions() {
        return 1;
    }

    @Override
    public double hausdorffDimension() {
        // For r=4, dimension is 1. For other chaotic values, it's < 1.
        // This is a simplification.
        return 1.0;
    }
}


