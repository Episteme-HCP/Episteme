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
package org.jscience.mathematics.statistics.distributions;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.statistics.ContinuousDistribution;

public class BetaDistribution extends ContinuousDistribution {
    private final Real alpha, beta, betaFunction;

    public BetaDistribution(Real alpha, Real beta) {
        if (alpha.compareTo(Real.ZERO) <= 0 || beta.compareTo(Real.ZERO) <= 0) {
            throw new IllegalArgumentException("α, β must be positive");
        }
        this.alpha = alpha;
        this.beta = beta;
        this.betaFunction = computeBetaFunction(alpha, beta);
    }

    private Real computeBetaFunction(Real a, Real b) {
        double gammaA = Math.exp(lgamma(a.doubleValue()));
        double gammaB = Math.exp(lgamma(b.doubleValue()));
        double gammaAB = Math.exp(lgamma(a.add(b).doubleValue()));
        return Real.of((gammaA * gammaB) / gammaAB);
    }

    private double lgamma(double x) {
        return (x - 0.5) * Math.log(x) - x + 0.5 * Math.log(2 * Math.PI);
    }

    @Override
    public Real density(Real x) {
        if (x.compareTo(Real.ZERO) < 0 || x.compareTo(Real.ONE) > 0)
            return Real.ZERO;
        double xVal = x.doubleValue();
        double numerator = Math.pow(xVal, alpha.doubleValue() - 1) *
                Math.pow(1 - xVal, beta.doubleValue() - 1);
        return Real.of(numerator).divide(betaFunction);
    }

    @Override
    public Real mean() {
        return alpha.divide(alpha.add(beta));
    }

    @Override
    public Real variance() {
        Real sum = alpha.add(beta);
        return alpha.multiply(beta).divide(sum.multiply(sum).multiply(sum.add(Real.ONE)));
    }

    @Override
    public String toString() {
        return String.format("Beta(α=%.4f, β=%.4f)", alpha.doubleValue(), beta.doubleValue());
    }
}