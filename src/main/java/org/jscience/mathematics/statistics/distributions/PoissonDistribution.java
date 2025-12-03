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
package org.jscience.mathematics.statistics.distributions;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.statistics.DiscreteDistribution;

public class PoissonDistribution extends DiscreteDistribution {
    private final Real lambda;

    public PoissonDistribution(Real lambda) {
        if (lambda.compareTo(Real.ZERO) <= 0)
            throw new IllegalArgumentException("λ must be positive");
        this.lambda = lambda;
    }

    @Override
    public Real density(Real k) {
        int kInt = k.intValue();
        if (kInt < 0 || kInt != k.doubleValue())
            return Real.ZERO;

        double lambdaVal = lambda.doubleValue();
        double result = Math.pow(lambdaVal, kInt) * Math.exp(-lambdaVal);
        for (int i = 2; i <= kInt; i++)
            result /= i;
        return Real.of(result);
    }

    @Override
    public Real mean() {
        return lambda;
    }

    @Override
    public Real variance() {
        return lambda;
    }

    @Override
    public String toString() {
        return String.format("Poisson(λ=%.4f)", lambda.doubleValue());
    }
}


