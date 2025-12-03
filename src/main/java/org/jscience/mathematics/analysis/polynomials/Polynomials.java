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
package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.number.Real;

/**
 * Utility class for evaluating orthogonal polynomials.
 * <p>
 * Includes Legendre and Jacobi polynomials, which are essential for
 * Finite Element Method (FEM) shape functions and Gaussian quadrature.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public final class Polynomials {

    private Polynomials() {
        // Utility class
    }

    /**
     * Evaluates the Legendre polynomial P_n(x) at a given point x.
     * <p>
     * Uses the recurrence relation:
     * (n+1)P_{n+1}(x) = (2n+1)xP_n(x) - nP_{n-1}(x)
     * </p>
     * 
     * @param n degree of the polynomial (n >= 0)
     * @param x point at which to evaluate
     * @return P_n(x)
     */
    public static Real legendre(int n, Real x) {
        if (n < 0) {
            throw new IllegalArgumentException("Degree must be non-negative");
        }
        if (n == 0) {
            return Real.ONE;
        }
        if (n == 1) {
            return x;
        }

        Real p0 = Real.ONE;
        Real p1 = x;
        Real p2 = p1;

        for (int i = 1; i < n; i++) {
            // P_{i+1} = ((2i+1)xP_i - iP_{i-1}) / (i+1)
            Real term1 = x.multiply(p1).multiply(Real.of(2 * i + 1));
            Real term2 = p0.multiply(Real.of(i));
            p2 = term1.subtract(term2).divide(Real.of(i + 1));

            p0 = p1;
            p1 = p2;
        }

        return p2;
    }

    /**
     * Evaluates the derivative of the Legendre polynomial P'_n(x).
     * <p>
     * Uses the relation:
     * (x^2 - 1)P'_n(x) = n(xP_n(x) - P_{n-1}(x))
     * </p>
     * 
     * @param n degree of the polynomial (n >= 0)
     * @param x point at which to evaluate
     * @return P'_n(x)
     */
    public static Real legendreDerivative(int n, Real x) {
        if (n < 0) {
            throw new IllegalArgumentException("Degree must be non-negative");
        }
        if (n == 0) {
            return Real.ZERO;
        }

        // Handle singularities at x = +/- 1
        if (x.abs().subtract(Real.ONE).abs().compareTo(Real.of(1e-12)) < 0) {
            // P'_n(1) = n(n+1)/2
            // P'_n(-1) = (-1)^(n-1) * n(n+1)/2
            Real val = Real.of(n * (n + 1) / 2.0);
            if (x.compareTo(Real.ZERO) < 0 && (n % 2 == 0)) {
                return val.negate();
            }
            return val;
        }

        Real Pn = legendre(n, x);
        Real Pn_1 = legendre(n - 1, x);

        // P'_n(x) = n(xP_n(x) - P_{n-1}(x)) / (x^2 - 1)
        Real numerator = x.multiply(Pn).subtract(Pn_1).multiply(Real.of(n));
        Real denominator = x.multiply(x).subtract(Real.ONE);

        return numerator.divide(denominator);
    }

    /**
     * Evaluates the Jacobi polynomial P_n^{alpha, beta}(x).
     * 
     * @param n     degree (n >= 0)
     * @param alpha parameter alpha > -1
     * @param beta  parameter beta > -1
     * @param x     point at which to evaluate
     * @return P_n^{alpha, beta}(x)
     */
    public static Real jacobi(int n, double alpha, double beta, Real x) {
        if (n < 0) {
            throw new IllegalArgumentException("Degree must be non-negative");
        }
        if (n == 0) {
            return Real.ONE;
        }
        if (n == 1) {
            // 0.5 * (alpha - beta + (alpha + beta + 2) * x)
            return Real.of(0.5 * (alpha - beta)).add(x.multiply(Real.of(0.5 * (alpha + beta + 2))));
        }

        Real p0 = Real.ONE;
        Real p1 = Real.of(0.5 * (alpha - beta)).add(x.multiply(Real.of(0.5 * (alpha + beta + 2))));
        Real p2 = p1;

        for (int i = 1; i < n; i++) {
            double a1 = 2 * (i + 1) * (i + alpha + beta + 1) * (2 * i + alpha + beta);
            double a2 = (2 * i + alpha + beta + 1) * (alpha * alpha - beta * beta);
            double a3 = (2 * i + alpha + beta) * (2 * i + alpha + beta + 1) * (2 * i + alpha + beta + 2);
            double a4 = 2 * (i + alpha) * (i + beta) * (2 * i + alpha + beta + 2);

            // a1 * P_{i+1} = (a2 + a3 * x) * P_i - a4 * P_{i-1}
            Real term1 = p1.multiply(Real.of(a2).add(x.multiply(Real.of(a3))));
            Real term2 = p0.multiply(Real.of(a4));

            p2 = term1.subtract(term2).divide(Real.of(a1));

            p0 = p1;
            p1 = p2;
        }

        return p2;
    }
}


