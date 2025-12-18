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
package org.jscience.mathematics.numbers.real;

/**
 * Advanced mathematical functions for Real numbers.
 * <p>
 * Provides special functions like error function (erf) and gamma function
 * operating on {@link Real} values.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 2.0
 */
public class RealMath {

    private RealMath() {
    }

    /**
     * Error function approximation (Abramowitz and Stegun).
     * 
     * @param x the input value
     * @return erf(x)
     */
    public static Real erf(Real x) {
        // Use double approximation for now, but wrapped in Real for API consistency
        // TODO: Implement high-precision Erf for BigDecimal mode
        return Real.of(erf(x.doubleValue()));
    }

    private static double erf(double x) {
        double sign = x >= 0 ? 1 : -1;
        x = Math.abs(x);

        double a1 = 0.254829592;
        double a2 = -0.284496736;
        double a3 = 1.421413741;
        double a4 = -1.453152027;
        double a5 = 1.061405429;
        double p = 0.3275911;

        double t = 1.0 / (1.0 + p * x);
        double t2 = t * t;
        double t3 = t2 * t;
        double t4 = t3 * t;
        double t5 = t4 * t;

        double y = 1.0 - (a1 * t + a2 * t2 + a3 * t3 + a4 * t4 + a5 * t5)
                * Math.exp(-x * x);

        return sign * y;
    }

    /**
     * Inverse standard normal CDF (quantile function).
     */
    public static Real standardNormalQuantile(Real p) {
        return Real.of(standardNormalQuantile(p.doubleValue()));
    }

    private static double standardNormalQuantile(double p) {
        if (p <= 0)
            return Double.NEGATIVE_INFINITY;
        if (p >= 1)
            return Double.POSITIVE_INFINITY;
        if (p == 0.5)
            return 0;

        double sign = p < 0.5 ? -1 : 1;
        double q = p < 0.5 ? p : 1 - p;
        double t = Math.sqrt(-2 * Math.log(q));

        double c0 = 2.515517;
        double c1 = 0.802853;
        double c2 = 0.010328;
        double d1 = 1.432788;
        double d2 = 0.189269;
        double d3 = 0.001308;

        double z = t - (c0 + c1 * t + c2 * t * t) /
                (1 + d1 * t + d2 * t * t + d3 * t * t * t);

        return sign * z;
    }

    /**
     * Gamma function approximation (Lanczos).
     */
    public static Real gamma(Real x) {
        return Real.of(gamma(x.doubleValue()));
    }

    private static double gamma(double z) {
        if (z < 0.5) {
            return Math.PI / (Math.sin(Math.PI * z) * gamma(1 - z));
        }

        z -= 1;
        double[] g = { 676.5203681218851, -1259.1392167224028, 771.32342877765313,
                -176.61502916214059, 12.507343278686905, -0.13857109526572012,
                9.9843695780195716e-6, 1.5056327351493116e-7 };

        double x = 0.99999999999980993;
        for (int i = 0; i < g.length; i++) {
            x += g[i] / (z + i + 1);
        }

        double t = z + g.length - 0.5;
        return Math.sqrt(2 * Math.PI) * Math.pow(t, z + 0.5) * Math.exp(-t) * x;
    }

    /**
     * Log-gamma function.
     */
    public static Real lgamma(Real x) {
        if (x.doubleValue() <= 0)
            return Real.NaN;
        return Real.of(Math.log(gamma(x.doubleValue())));
    }

    /**
     * Factorial of integer n.
     */
    public static Real factorial(int n) {
        if (n < 0)
            return Real.NaN;
        if (n <= 1)
            return Real.ONE;
        double res = 1;
        for (int i = 2; i <= n; i++)
            res *= i;
        return Real.of(res);
    }
}
