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
package org.jscience.mathematics.optimization;

import org.jscience.mathematics.numbers.real.Real;

import org.jscience.mathematics.analysis.Function;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Optimization algorithms.
 */
public class OptimizationTest {

    @Test
    public void testGradientDescent() {
        // Function: f(x) = x^2
        // Gradient: f'(x) = 2x
        // Minimum at x = 0

        Function<Real, Real> f = new Function<Real, Real>() {
            @Override
            public Real evaluate(Real x) {
                return x.multiply(x);
            }
        };

        Function<Real, Real> gradient = new Function<Real, Real>() {
            @Override
            public Real evaluate(Real x) {
                return x.multiply(Real.of(2));
            }
        };

        Real start = Real.of(10); // Start at x=10
        Real learningRate = Real.of(0.1);
        Real tolerance = Real.of(0.001);
        int maxIterations = 100;

        Real result = org.jscience.mathematics.optimization.Optimizer.gradientDescent(f, gradient, start, learningRate,
                tolerance, maxIterations);

        // Should be close to 0
        assertTrue(result.abs().compareTo(Real.of(0.1)) < 0);
    }
}
