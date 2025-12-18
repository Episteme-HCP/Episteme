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
package org.jscience.mathematics.analysis.integration;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.analysis.polynomials.Polynomials;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Gauss-Legendre Quadrature.
 * <p>
 * Approximates integrals using roots of Legendre polynomials as nodes.
 * Exact for polynomials of degree 2n-1.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public final class GaussLegendre {

    private GaussLegendre() {
        // Utility class
    }

    /**
     * Computes the integral of f(x) over [-1, 1] using n points.
     * 
     * @param f function to integrate
     * @param n number of points
     * @return integral value
     */
    public static Real integrate(Function<Real, Real> f, int n) {
        Real[] roots = getRoots(n);
        Real[] weights = getWeights(n, roots);

        Real sum = Real.ZERO;
        for (int i = 0; i < n; i++) {
            sum = sum.add(weights[i].multiply(f.evaluate(roots[i])));
        }
        return sum;
    }

    /**
     * Computes the integral of f(x) over [a, b] using n points.
     * <p>
     * Performs change of variables from [-1, 1] to [a, b].
     * </p>
     * 
     * @param f function to integrate
     * @param a lower bound
     * @param b upper bound
     * @param n number of points
     * @return integral value
     */
    public static Real integrate(Function<Real, Real> f, Real a, Real b, int n) {
        Real center = b.add(a).divide(Real.of(2));
        Real halfWidth = b.subtract(a).divide(Real.of(2));

        Function<Real, Real> transformedF = x -> f.evaluate(halfWidth.multiply(x).add(center));

        return halfWidth.multiply(integrate(transformedF, n));
    }

    /**
     * Computes roots of Legendre polynomial P_n(x).
     * <p>
     * Uses Newton's method to find roots.
     * </p>
     */
    private static Real[] getRoots(int n) {
        Real[] roots = new Real[n];

        // Initial guesses using cosine approximation
        for (int i = 0; i < n; i++) {
            double guess = Math.cos(Math.PI * (i + 0.75) / (n + 0.5));
            Real x = Real.of(guess);

            // Newton's method: x_{k+1} = x_k - P_n(x_k) / P'_n(x_k)
            for (int iter = 0; iter < 100; iter++) {
                Real Pn = Polynomials.legendre(n, x);
                Real PnPrime = Polynomials.legendreDerivative(n, x);

                Real delta = Pn.divide(PnPrime);
                x = x.subtract(delta);

                if (delta.abs().compareTo(Real.of(1e-14)) < 0) {
                    break;
                }
            }
            roots[i] = x;
        }
        return roots;
    }

    /**
     * Computes weights for Gauss-Legendre quadrature.
     * <p>
     * w_i = 2 / ((1 - x_i^2) * [P'_n(x_i)]^2)
     * </p>
     */
    private static Real[] getWeights(int n, Real[] roots) {
        Real[] weights = new Real[n];
        for (int i = 0; i < n; i++) {
            Real x = roots[i];
            Real PnPrime = Polynomials.legendreDerivative(n, x);

            Real denominator = Real.ONE.subtract(x.multiply(x)).multiply(PnPrime.multiply(PnPrime));
            weights[i] = Real.of(2).divide(denominator);
        }
        return weights;
    }
}


