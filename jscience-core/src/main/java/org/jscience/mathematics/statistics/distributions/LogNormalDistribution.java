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

/**
 * LogNormal distribution.
 * If X ~ LogNormal(μ, σ²), then ln(X) ~ Normal(μ, σ²) * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class LogNormalDistribution extends ContinuousDistribution {
    private final Real mu;
    private final Real sigma;

    public LogNormalDistribution(Real mu, Real sigma) {
        if (sigma.compareTo(Real.ZERO) <= 0) {
            throw new IllegalArgumentException("Sigma must be positive");
        }
        this.mu = mu;
        this.sigma = sigma;
    }

    @Override
    public Real density(Real x) {
        if (x.compareTo(Real.ZERO) <= 0)
            return Real.ZERO;

        double xVal = x.doubleValue();
        double muVal = mu.doubleValue();
        double sigmaVal = sigma.doubleValue();

        double lnX = Math.log(xVal);
        double exponent = -Math.pow(lnX - muVal, 2) / (2 * sigmaVal * sigmaVal);
        double result = Math.exp(exponent) / (xVal * sigmaVal * Math.sqrt(2 * Math.PI));

        return Real.of(result);
    }

    @Override
    public Real mean() {
        return Real.of(Math.exp(mu.doubleValue() + sigma.doubleValue() * sigma.doubleValue() / 2));
    }

    @Override
    public Real variance() {
        double muVal = mu.doubleValue();
        double sigmaVal = sigma.doubleValue();
        double exp2mu = Math.exp(2 * muVal);
        double expsigma2 = Math.exp(sigmaVal * sigmaVal);
        return Real.of(exp2mu * expsigma2 * (expsigma2 - 1));
    }

    @Override
    public String toString() {
        return String.format("LogNormal(μ=%.4f, σ=%.4f)", mu.doubleValue(), sigma.doubleValue());
    }
}