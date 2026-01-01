/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.analysis.series;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Geometric series: ÃŽÂ£ ar^n, n=0..Ã¢Ë†Å¾
 * <p>
 * A geometric series converges if and only if |r| < 1.
 * When convergent, the sum is: S = a/(1-r)
 * </p>
 *
 * <h2>Example</h2>
 *
 * <pre>
 * // Series: 1 + 1/2 + 1/4 + 1/8 + ... = 2
 * GeometricSeries series = new GeometricSeries(Real.ONE, Real.of(0.5));
 * Real sum = series.limit(); // Returns 2.0
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeometricSeries implements InfiniteSeries<Real> {

    private final Real a; // First term
    private final Real r; // Common ratio

    /**
     * Creates a geometric series with first term a and common ratio r.
     * 
     * @param a the first term
     * @param r the common ratio
     */
    public GeometricSeries(Real a, Real r) {
        if (a == null || r == null) {
            throw new IllegalArgumentException("Terms cannot be null");
        }
        this.a = a;
        this.r = r;
    }

    /**
     * Convenience constructor with double values.
     * 
     * @param a the first term
     * @param r the common ratio
     */
    public GeometricSeries(double a, double r) {
        this(Real.of(a), Real.of(r));
    }

    @Override
    public Real term(int k) {
        if (k < 0) {
            throw new IllegalArgumentException("Term index must be non-negative");
        }
        // a * r^k
        Real rPowK = Real.ONE;
        for (int i = 0; i < k; i++) {
            rPowK = rPowK.multiply(r);
        }
        return a.multiply(rPowK);
    }

    @Override
    public Real partialSum(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative");
        }

        // S_n = a * (1 - r^(n+1)) / (1 - r) if r Ã¢â€°Â  1
        // S_n = a * (n+1) if r = 1

        if (r.equals(Real.ONE)) {
            return a.multiply(Real.of(n + 1));
        }

        Real rPowNPlus1 = Real.ONE;
        for (int i = 0; i <= n; i++) {
            rPowNPlus1 = rPowNPlus1.multiply(r);
        }

        Real numerator = Real.ONE.subtract(rPowNPlus1);
        Real denominator = Real.ONE.subtract(r);

        return a.multiply(numerator.divide(denominator));
    }

    @Override
    public boolean isConvergent() {
        // Converges if |r| < 1
        double rAbs = Math.abs(r.doubleValue());
        return rAbs < 1.0;
    }

    @Override
    public Real limit() {
        if (!isConvergent()) {
            throw new ArithmeticException("Series is divergent (|r| >= 1)");
        }
        // S = a / (1 - r)
        return a.divide(Real.ONE.subtract(r));
    }

    /**
     * Returns the first term of this series.
     * 
     * @return the first term a
     */
    public Real getFirstTerm() {
        return a;
    }

    /**
     * Returns the common ratio of this series.
     * 
     * @return the common ratio r
     */
    public Real getCommonRatio() {
        return r;
    }

    @Override
    public String toString() {
        return String.format("GeometricSeries(a=%s, r=%s, %s)",
                a, r, isConvergent() ? "convergent" : "divergent");
    }
}

