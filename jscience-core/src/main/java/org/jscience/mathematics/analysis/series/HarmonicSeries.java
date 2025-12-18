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
package org.jscience.mathematics.analysis.series;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Harmonic series: Σ 1/n, n=1..∞
 * <p>
 * The harmonic series is known to diverge, though it grows very slowly.
 * It is useful in many areas of mathematics and computer science.
 * </p>
 * <p>
 * The partial sum H_n ≈ ln(n) + γ where γ ≈ 0.5772 is the Euler-Mascheroni
 * constant.
 * </p>
 * 
 * <h2>Example</h2>
 * 
 * <pre>
 * HarmonicSeries series = new HarmonicSeries();
 * Real h10 = series.partialSum(10); // 1 + 1/2 + 1/3 + ... + 1/10
 * </pre>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class HarmonicSeries implements InfiniteSeries<Real> {

    /** Euler-Mascheroni constant γ ≈ 0.5772156649 */
    public static final Real EULER_MASCHERONI = Real.of(0.5772156649015329);

    @Override
    public Real term(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("Harmonic series starts at k=1");
        }
        return Real.ONE.divide(Real.of(k));
    }

    @Override
    public Real partialSum(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }

        // H_n = 1 + 1/2 + 1/3 + ... + 1/n
        Real sum = Real.ZERO;
        for (int k = 1; k <= n; k++) {
            sum = sum.add(Real.ONE.divide(Real.of(k)));
        }
        return sum;
    }

    @Override
    public boolean isConvergent() {
        // The harmonic series is divergent
        return false;
    }

    @Override
    public Real limit() {
        throw new ArithmeticException("Harmonic series is divergent");
    }

    /**
     * Approximates the n-th harmonic number using the asymptotic formula.
     * <p>
     * H_n ≈ ln(n) + γ + 1/(2n) for large n
     * </p>
     * 
     * @param n the index
     * @return approximate value of H_n
     */
    public Real approximateSum(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }

        // H_n ≈ ln(n) + γ
        Real lnN = Real.of(Math.log(n));
        return lnN.add(EULER_MASCHERONI);
    }

    /**
     * Returns the difference between consecutive harmonic numbers.
     * <p>
     * H_n - H_{n-1} = 1/n
     * </p>
     * 
     * @param n the index (n > 0)
     * @return 1/n
     */
    public Real increment(int n) {
        return term(n);
    }

    @Override
    public String toString() {
        return "HarmonicSeries(Σ 1/n, divergent)";
    }
}

