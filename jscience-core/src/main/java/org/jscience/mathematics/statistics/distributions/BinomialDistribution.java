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

package org.jscience.mathematics.statistics.distributions;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.statistics.DiscreteDistribution;

/**
 * Binomial distribution Bin(n, p).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BinomialDistribution extends DiscreteDistribution {

    private final int n;
    private final Real p;

    public BinomialDistribution(int n, Real p) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be positive");
        if (p.compareTo(Real.ZERO) < 0 || p.compareTo(Real.ONE) > 0) {
            throw new IllegalArgumentException("p must be in [0,1]");
        }
        this.n = n;
        this.p = p;
    }

    @Override
    public Real density(Real k) {
        int kInt = k.intValue();
        if (kInt < 0 || kInt > n || kInt != k.doubleValue())
            return Real.ZERO;

        double binomCoeff = binomialCoefficient(n, kInt);
        double pk = Math.pow(p.doubleValue(), kInt);
        double qnk = Math.pow(1 - p.doubleValue(), n - kInt);
        return Real.of(binomCoeff * pk * qnk);
    }

    private double binomialCoefficient(int n, int k) {
        if (k > n - k)
            k = n - k;
        double result = 1;
        for (int i = 0; i < k; i++) {
            result *= (n - i);
            result /= (i + 1);
        }
        return result;
    }

    @Override
    public Real mean() {
        return Real.of(n).multiply(p);
    }

    @Override
    public Real variance() {
        return Real.of(n).multiply(p).multiply(Real.ONE.subtract(p));
    }

    @Override
    public String toString() {
        return String.format("Bin(n=%d, p=%.4f)", n, p.doubleValue());
    }
}

