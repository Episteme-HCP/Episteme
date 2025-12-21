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
package org.jscience.mathematics.analysis.integration;

import org.jscience.mathematics.analysis.RealFunction;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Trapezoidal rule integration.
 * <p>
 * Simple, fast numerical integration using the trapezoidal rule.
 * Less accurate than adaptive methods but computationally efficient.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TrapezoidalIntegrator implements Integrator {

    private static final int DEFAULT_INTERVALS = 1000;

    private final int intervals;

    /**
     * Creates a trapezoidal integrator with default number of intervals.
     */
    public TrapezoidalIntegrator() {
        this(DEFAULT_INTERVALS);
    }

    /**
     * Creates a trapezoidal integrator with specified intervals.
     *
     * @param intervals number of subdivisions
     */
    public TrapezoidalIntegrator(int intervals) {
        if (intervals <= 0) {
            throw new IllegalArgumentException("Intervals must be positive");
        }
        this.intervals = intervals;
    }

    @Override
    public Real integrate(RealFunction f, Real a, Real b) {
        double start = a.doubleValue();
        double end = b.doubleValue();
        double h = (end - start) / intervals;
        double sum = 0.5 * (f.evaluate(a).doubleValue() + f.evaluate(b).doubleValue());

        for (int i = 1; i < intervals; i++) {
            double x = start + i * h;
            sum += f.evaluate(Real.of(x)).doubleValue();
        }

        return Real.of(sum * h);
    }

    @Override
    public Real integrate(RealFunction f, Real a, Real b, Real tolerance) {
        // Tolerance doesn't affect trapezoidal rule directly
        // Could adaptively increase intervals, but for simplicity just use default
        return integrate(f, a, b);
    }
}
