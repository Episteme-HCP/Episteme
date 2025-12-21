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
package org.jscience.mathematics.analysis.polynomials;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Utility class for computing orthogonal polynomials.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class Polynomials {

    private Polynomials() {
        // Utility class
    }

    /**
     * Evaluates the Legendre polynomial P_n(x) at the given point.
     * Uses the recurrence relation:
     * P_0(x) = 1
     * P_1(x) = x
     * (n+1) P_{n+1}(x) = (2n+1) x P_n(x) - n P_{n-1}(x)
     * 
     * @param n degree of the polynomial
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

        Real Pnm1 = Real.ONE; // P_{n-1} starts as P_0
        Real Pn = x; // P_n starts as P_1

        for (int k = 1; k < n; k++) {
            // P_{k+1} = ((2k+1) x P_k - k P_{k-1}) / (k+1)
            Real twoKp1 = Real.of(2 * k + 1);
            Real kReal = Real.of(k);
            Real kp1Real = Real.of(k + 1);

            Real Pnp1 = twoKp1.multiply(x).multiply(Pn).subtract(kReal.multiply(Pnm1)).divide(kp1Real);

            Pnm1 = Pn;
            Pn = Pnp1;
        }

        return Pn;
    }

    /**
     * Evaluates the derivative of the Legendre polynomial P'_n(x) at the given
     * point.
     * Uses the relation:
     * P'_n(x) = n (x P_n(x) - P_{n-1}(x)) / (x^2 - 1) for x != ±1
     * 
     * @param n degree of the polynomial
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
        if (n == 1) {
            return Real.ONE;
        }

        Real Pn = legendre(n, x);
        Real Pnm1 = legendre(n - 1, x);

        Real x2m1 = x.multiply(x).subtract(Real.ONE);

        // Handle case where x is very close to ±1
        if (x2m1.abs().compareTo(Real.of(1e-14)) < 0) {
            // Use alternative formula at endpoints
            // P'_n(1) = n(n+1)/2, P'_n(-1) = (-1)^{n+1} n(n+1)/2
            Real nReal = Real.of(n);
            Real result = nReal.multiply(nReal.add(Real.ONE)).divide(Real.of(2));
            if (x.compareTo(Real.ZERO) < 0 && n % 2 == 0) {
                result = result.negate();
            }
            return result;
        }

        Real nReal = Real.of(n);
        return nReal.multiply(x.multiply(Pn).subtract(Pnm1)).divide(x2m1);
    }

    /**
     * Evaluates the Chebyshev polynomial of the first kind T_n(x).
     * T_n(cos(θ)) = cos(nθ)
     * 
     * @param n degree
     * @param x point (typically in [-1, 1])
     * @return T_n(x)
     */
    public static Real chebyshevT(int n, Real x) {
        if (n < 0) {
            throw new IllegalArgumentException("Degree must be non-negative");
        }

        if (n == 0)
            return Real.ONE;
        if (n == 1)
            return x;

        Real Tnm1 = Real.ONE;
        Real Tn = x;
        Real two = Real.of(2);

        for (int k = 1; k < n; k++) {
            Real Tnp1 = two.multiply(x).multiply(Tn).subtract(Tnm1);
            Tnm1 = Tn;
            Tn = Tnp1;
        }

        return Tn;
    }

    /**
     * Evaluates the Chebyshev polynomial of the second kind U_n(x).
     * 
     * @param n degree
     * @param x point
     * @return U_n(x)
     */
    public static Real chebyshevU(int n, Real x) {
        if (n < 0) {
            throw new IllegalArgumentException("Degree must be non-negative");
        }

        if (n == 0)
            return Real.ONE;
        if (n == 1)
            return Real.of(2).multiply(x);

        Real Unm1 = Real.ONE;
        Real Un = Real.of(2).multiply(x);
        Real two = Real.of(2);

        for (int k = 1; k < n; k++) {
            Real Unp1 = two.multiply(x).multiply(Un).subtract(Unm1);
            Unm1 = Un;
            Un = Unp1;
        }

        return Un;
    }

    /**
     * Evaluates the Hermite polynomial H_n(x) (physicist's convention).
     * H_0(x) = 1, H_1(x) = 2x, H_{n+1}(x) = 2x H_n(x) - 2n H_{n-1}(x)
     * 
     * @param n degree
     * @param x point
     * @return H_n(x)
     */
    public static Real hermite(int n, Real x) {
        if (n < 0) {
            throw new IllegalArgumentException("Degree must be non-negative");
        }

        if (n == 0)
            return Real.ONE;
        if (n == 1)
            return Real.of(2).multiply(x);

        Real Hnm1 = Real.ONE;
        Real Hn = Real.of(2).multiply(x);
        Real two = Real.of(2);

        for (int k = 1; k < n; k++) {
            Real twoK = Real.of(2 * k);
            Real Hnp1 = two.multiply(x).multiply(Hn).subtract(twoK.multiply(Hnm1));
            Hnm1 = Hn;
            Hn = Hnp1;
        }

        return Hn;
    }

    /**
     * Evaluates the Laguerre polynomial L_n(x).
     * L_0(x) = 1, L_1(x) = 1-x, (n+1) L_{n+1}(x) = (2n+1-x) L_n(x) - n L_{n-1}(x)
     * 
     * @param n degree
     * @param x point
     * @return L_n(x)
     */
    public static Real laguerre(int n, Real x) {
        if (n < 0) {
            throw new IllegalArgumentException("Degree must be non-negative");
        }

        if (n == 0)
            return Real.ONE;
        if (n == 1)
            return Real.ONE.subtract(x);

        Real Lnm1 = Real.ONE;
        Real Ln = Real.ONE.subtract(x);

        for (int k = 1; k < n; k++) {
            Real kp1 = Real.of(k + 1);
            Real twoKp1mx = Real.of(2 * k + 1).subtract(x);
            Real kReal = Real.of(k);

            Real Lnp1 = twoKp1mx.multiply(Ln).subtract(kReal.multiply(Lnm1)).divide(kp1);
            Lnm1 = Ln;
            Ln = Lnp1;
        }

        return Ln;
    }
}
