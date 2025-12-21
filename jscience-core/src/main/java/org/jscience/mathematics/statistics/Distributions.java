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
package org.jscience.mathematics.statistics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.real.RealMath;

/**
 * Common probability distributions for statistical analysis.
 * <p>
 * Provides PDF, CDF, and parameter calculations for various distributions
 * using arbitrary precision {@link Real} numbers.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Distributions {

    private static final Real SQRT_2PI = Real.of(Math.sqrt(2 * Math.PI));
    private static final Real SQRT_2 = Real.of(Math.sqrt(2));

    private Distributions() {
    }

    // === Normal Distribution ===

    /**
     * Normal distribution PDF.
     * f(x) = (1/σ√2π) * e^(-(x-μ)²/(2σ²))
     */
    public static Real normalPdf(Real x, Real mean, Real stdDev) {
        Real z = x.subtract(mean).divide(stdDev);
        Real exponent = z.multiply(z).multiply(Real.of(-0.5));
        return exponent.exp().divide(stdDev.multiply(SQRT_2PI));
    }

    /**
     * Standard normal PDF (μ=0, σ=1).
     */
    public static Real standardNormalPdf(Real z) {
        return normalPdf(z, Real.ZERO, Real.ONE);
    }

    /**
     * Normal distribution CDF (approximation using error function).
     */
    public static Real normalCdf(Real x, Real mean, Real stdDev) {
        // z = (x - mean) / (stdDev * sqrt(2))
        Real z = x.subtract(mean).divide(stdDev.multiply(SQRT_2));
        // 0.5 * (1 + erf(z))
        return Real.of(0.5).multiply(Real.ONE.add(RealMath.erf(z)));
    }

    /**
     * Standard normal CDF.
     */
    public static Real standardNormalCdf(Real z) {
        return normalCdf(z, Real.ZERO, Real.ONE);
    }

    /**
     * Inverse standard normal (quantile function).
     */
    public static Real standardNormalQuantile(Real p) {
        return RealMath.standardNormalQuantile(p);
    }

    // === Poisson Distribution ===

    /**
     * Poisson distribution PMF.
     * P(X=k) = (λ^k * e^(-λ)) / k!
     */
    public static Real poissonPmf(int k, Real lambda) {
        if (k < 0)
            return Real.ZERO;
        // lambda^k * exp(-lambda) / k!
        return lambda.pow(k).multiply(lambda.negate().exp()).divide(RealMath.factorial(k));
    }

    /**
     * Poisson distribution CDF.
     */
    public static Real poissonCdf(int k, Real lambda) {
        Real sum = Real.ZERO;
        for (int i = 0; i <= k; i++) {
            sum = sum.add(poissonPmf(i, lambda));
        }
        return sum;
    }

    // === Exponential Distribution ===

    /**
     * Exponential distribution PDF.
     * f(x) = λ * e^(-λx) for x ≥ 0
     */
    public static Real exponentialPdf(Real x, Real lambda) {
        if (x.sign() < 0)
            return Real.ZERO;
        Real exponent = lambda.negate().multiply(x);
        return lambda.multiply(exponent.exp());
    }

    /**
     * Exponential distribution CDF.
     * F(x) = 1 - e^(-λx)
     */
    public static Real exponentialCdf(Real x, Real lambda) {
        if (x.sign() < 0)
            return Real.ZERO;
        Real exponent = lambda.negate().multiply(x);
        return Real.ONE.subtract(exponent.exp());
    }

    /**
     * Exponential quantile function.
     */
    public static Real exponentialQuantile(Real p, Real lambda) {
        if (p.sign() <= 0)
            return Real.ZERO;
        if (p.compareTo(Real.ONE) >= 0)
            return Real.POSITIVE_INFINITY;
        // -ln(1-p) / lambda
        return Real.ONE.subtract(p).log().negate().divide(lambda);
    }

    // === Binomial Distribution ===

    /**
     * Binomial distribution PMF.
     * P(X=k) = C(n,k) * p^k * (1-p)^(n-k)
     */
    public static Real binomialPmf(int k, int n, Real p) {
        if (k < 0 || k > n)
            return Real.ZERO;
        Real coeff = binomialCoefficient(n, k);
        Real term1 = p.pow(k);
        Real term2 = Real.ONE.subtract(p).pow(n - k);
        return coeff.multiply(term1).multiply(term2);
    }

    /**
     * Binomial coefficient C(n, k).
     */
    public static Real binomialCoefficient(int n, int k) {
        if (k < 0 || k > n)
            return Real.ZERO;
        if (k == 0 || k == n)
            return Real.ONE;
        if (k > n - k)
            k = n - k;

        Real result = Real.ONE;
        for (int i = 0; i < k; i++) {
            // result = result * (n - i) / (i + 1)
            result = result.multiply(Real.of(n - i)).divide(Real.of(i + 1));
        }
        return result;
    }

    /**
     * Binomial distribution CDF.
     */
    public static Real binomialCdf(int k, int n, Real p) {
        Real sum = Real.ZERO;
        for (int i = 0; i <= k; i++) {
            sum = sum.add(binomialPmf(i, n, p));
        }
        return sum;
    }

    // === Chi-Squared Distribution ===

    /**
     * Chi-squared distribution PDF.
     * f(x) = (x^(k/2-1) * e^(-x/2)) / (2^(k/2) * Γ(k/2))
     */
    public static Real chiSquaredPdf(Real x, int k) {
        if (x.sign() < 0)
            return Real.ZERO;
        Real halfK = Real.of(k).divide(Real.of(2));

        // Numerator: x^(k/2 - 1) * e^(-x/2)
        Real term1 = x.pow(halfK.subtract(Real.ONE));
        Real term2 = x.negate().divide(Real.of(2)).exp();

        // Denominator: 2^(k/2) * Gamma(k/2)
        Real term3 = Real.of(2).pow(halfK);
        Real term4 = RealMath.gamma(halfK);

        return term1.multiply(term2).divide(term3.multiply(term4));
    }

    // === Descriptive Statistics ===

    /**
     * Sample mean.
     */
    public static Real mean(Real[] data) {
        if (data.length == 0)
            return Real.NaN;
        Real sum = Real.ZERO;
        for (Real d : data)
            sum = sum.add(d);
        return sum.divide(Real.of(data.length));
    }

    /**
     * Sample variance (unbiased).
     */
    public static Real variance(Real[] data) {
        if (data.length <= 1)
            return Real.ZERO;
        Real m = mean(data);
        Real sumSq = Real.ZERO;
        for (Real d : data) {
            Real diff = d.subtract(m);
            sumSq = sumSq.add(diff.multiply(diff));
        }
        return sumSq.divide(Real.of(data.length - 1));
    }

    /**
     * Sample standard deviation.
     */
    public static Real stdDev(Real[] data) {
        return variance(data).sqrt();
    }

    /**
     * Covariance.
     */
    public static Real covariance(Real[] x, Real[] y) {
        if (x.length != y.length)
            throw new IllegalArgumentException("Arrays must have same length");
        if (x.length <= 1)
            return Real.ZERO;
        Real mx = mean(x);
        Real my = mean(y);
        Real sum = Real.ZERO;
        for (int i = 0; i < x.length; i++) {
            sum = sum.add(x[i].subtract(mx).multiply(y[i].subtract(my)));
        }
        return sum.divide(Real.of(x.length - 1));
    }

    /**
     * Pearson correlation coefficient.
     */
    public static Real correlation(Real[] x, Real[] y) {
        Real cov = covariance(x, y);
        Real sx = stdDev(x);
        Real sy = stdDev(y);
        if (sx.isZero() || sy.isZero())
            return Real.NaN;
        return cov.divide(sx.multiply(sy));
    }
}