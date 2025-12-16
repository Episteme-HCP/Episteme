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
import org.jscience.mathematics.sequences.InfiniteSequence;
import java.util.function.Function;

/**
 * Power series: Σ a_n * x^n, n=0..∞
 * <p>
 * Power series are fundamental for Taylor and Maclaurin expansions.
 * A power series has a radius of convergence R such that:
 * <ul>
 * <li>Series converges absolutely for |x| < R</li>
 * <li>Series diverges for |x| > R</li>
 * <li>Behavior at |x| = R varies</li>
 * </ul>
 * </p>
 * 
 * <h2>Common Examples</h2>
 * 
 * <pre>
 * // e^x = Σ x^n/n!, R = ∞
 * PowerSeries exp = PowerSeries.exponential();
 * 
 * // sin(x) = Σ (-1)^n * x^(2n+1)/(2n+1)!, R = ∞
 * PowerSeries sin = PowerSeries.sine();
 * 
 * // 1/(1-x) = Σ x^n, R = 1
 * PowerSeries geometric = new PowerSeries(n -> Real.ONE);
 * </pre>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class PowerSeries implements InfiniteSeries<Real>, InfiniteSequence<Real> {

    private final Function<Integer, Real> coefficients;
    private final Real x; // Point of evaluation
    private final Real radiusOfConvergence;

    /**
     * Creates a power series with given coefficients evaluated at x.
     * 
     * @param coefficients        function mapping n to a_n
     * @param x                   point of evaluation
     * @param radiusOfConvergence radius of convergence R
     */
    public PowerSeries(Function<Integer, Real> coefficients, Real x, Real radiusOfConvergence) {
        if (coefficients == null || x == null || radiusOfConvergence == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        this.coefficients = coefficients;
        this.x = x;
        this.radiusOfConvergence = radiusOfConvergence;
    }

    /**
     * Creates a power series with given coefficients evaluated at x = 0
     * (Maclaurin).
     * 
     * @param coefficients function mapping n to a_n
     */
    public PowerSeries(Function<Integer, Real> coefficients) {
        this(coefficients, Real.ZERO, Real.POSITIVE_INFINITY);

    }

    @Override
    public Real term(int k) {
        if (k < 0) {
            throw new IllegalArgumentException("Term index must be non-negative");
        }
        // a_k * x^k
        Real ak = coefficients.apply(k);
        Real xPowK = Real.ONE;
        for (int i = 0; i < k; i++) {
            xPowK = xPowK.multiply(x);
        }
        return ak.multiply(xPowK);
    }

    @Override
    public Real partialSum(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative");
        }

        Real sum = Real.ZERO;
        for (int k = 0; k <= n; k++) {
            sum = sum.add(term(k));
        }
        return sum;
    }

    @Override
    public boolean isConvergent() {
        // Converges if |x| < R
        double xAbs = Math.abs(x.doubleValue());
        double r = radiusOfConvergence.doubleValue();
        return xAbs < r;
    }

    @Override
    public Real limit() {
        if (!isConvergent()) {
            throw new ArithmeticException("Series diverges for |x| >= R");
        }
        // Sum terms until convergence or max iterations
        Real sum = Real.ZERO;
        double epsilon = 1e-12;
        int maxIter = 1000;

        for (int k = 0; k < maxIter; k++) {
            Real term = term(k);
            sum = sum.add(term);

            // Check convergence
            if (Math.abs(term.doubleValue()) < epsilon) {
                return sum;
            }
        }

        // If not converged, return best effort (or could throw warning)
        return sum;
    }

    /**
     * Returns the radius of convergence.
     * 
     * @return R
     */
    public Real getRadiusOfConvergence() {
        return radiusOfConvergence;
    }

    /**
     * Evaluates the power series at a different point.
     * 
     * @param newX new evaluation point
     * @return new PowerSeries evaluated at newX
     */
    public PowerSeries at(Real newX) {
        return new PowerSeries(coefficients, newX, radiusOfConvergence);
    }

    // Factory methods for common power series

    /**
     * Creates the exponential series: e^x = Σ x^n/n!
     * 
     * @return exponential power series
     */
    public static PowerSeries exponential() {
        return new PowerSeries(n -> {
            long factorial = 1;
            for (int i = 1; i <= n; i++) {
                factorial *= i;
            }
            return Real.ONE.divide(Real.of(factorial));
        }, Real.ZERO, Real.POSITIVE_INFINITY);
    }

    /**
     * Creates the sine series: sin(x) = Σ (-1)^n * x^(2n+1)/(2n+1)!
     * 
     * @return sine power series
     */
    public static PowerSeries sine() {
        return new PowerSeries(n -> {
            if (n % 2 == 0)
                return Real.ZERO; // Only odd powers

            int power = n;
            long factorial = 1;
            for (int i = 1; i <= power; i++) {
                factorial *= i;
            }

            Real coeff = Real.ONE.divide(Real.of(factorial));
            if ((n / 2) % 2 == 1) {
                coeff = coeff.negate(); // Alternating signs
            }
            return coeff;
        }, Real.ZERO, Real.POSITIVE_INFINITY);
    }

    /**
     * Creates the cosine series: cos(x) = Σ (-1)^n * x^(2n)/(2n)!
     * 
     * @return cosine power series
     */
    public static PowerSeries cosine() {
        return new PowerSeries(n -> {
            if (n % 2 == 1)
                return Real.ZERO; // Only even powers

            long factorial = 1;
            for (int i = 1; i <= n; i++) {
                factorial *= i;
            }

            Real coeff = Real.ONE.divide(Real.of(factorial));
            if ((n / 2) % 2 == 1) {
                coeff = coeff.negate(); // Alternating signs
            }
            return coeff;
        }, Real.ZERO, Real.POSITIVE_INFINITY);
    }

    /**
     * Creates the natural logarithm series: ln(1+x) = Σ (-1)^(n-1) * x^n / n for n
     * >= 1
     * 
     * @return logarithm power series (centered at 0 for ln(1+x))
     */
    public static PowerSeries logarithm() {
        return new PowerSeries(n -> {
            if (n == 0)
                return Real.ZERO;
            Real val = Real.ONE.divide(Real.of(n));
            if ((n - 1) % 2 == 1) {
                val = val.negate();
            }
            return val;
        }, Real.ZERO, Real.ONE);
    }

    /**
     * Creates the arctangent series: atan(x) = Σ (-1)^n * x^(2n+1) / (2n+1)
     * 
     * @return arctangent power series
     */
    public static PowerSeries inverseTangent() {
        return new PowerSeries(n -> {
            if (n % 2 == 0)
                return Real.ZERO;
            int k = (n - 1) / 2;
            Real val = Real.ONE.divide(Real.of(n));
            if (k % 2 == 1) {
                val = val.negate();
            }
            return val;
        }, Real.ZERO, Real.ONE);
    }

    /**
     * Creates the hyperbolic sine series: sinh(x) = Σ x^(2n+1)/(2n+1)!
     * 
     * @return hyperbolic sine power series
     */
    public static PowerSeries hyperbolicSine() {
        return new PowerSeries(n -> {
            if (n % 2 == 0)
                return Real.ZERO;
            long factorial = 1;
            for (int i = 1; i <= n; i++) {
                factorial *= i;
            }
            return Real.ONE.divide(Real.of(factorial));
        }, Real.ZERO, Real.POSITIVE_INFINITY);
    }

    /**
     * Creates the hyperbolic cosine series: cosh(x) = Σ x^(2n)/(2n)!
     * 
     * @return hyperbolic cosine power series
     */
    public static PowerSeries hyperbolicCosine() {
        return new PowerSeries(n -> {
            if (n % 2 == 1)
                return Real.ZERO;
            long factorial = 1;
            for (int i = 1; i <= n; i++) {
                factorial *= i;
            }
            return Real.ONE.divide(Real.of(factorial));
        }, Real.ZERO, Real.POSITIVE_INFINITY);
    }

    @Override
    public Real get(int index) {
        return partialSum(index);
    }

    /**
     * Generates a Taylor series expansion for a differentiable function.
     * f(x) = f(a) + f'(a)(x-a) + f''(a)(x-a)^2/2! + ...
     * 
     * @param f      the differentiable function
     * @param center the center point 'a'
     * @param order  the maximum order of derivative to use (or -1 for infinite if
     *               possible, but here we need a limit for practical generation if
     *               not symbolic)
     *               Actually, PowerSeries takes a function n -> coefficient.
     *               If we can compute n-th derivative, we can create the series.
     * @return the Taylor series
     */
    public static PowerSeries taylor(org.jscience.mathematics.analysis.DifferentiableFunction<Real, Real> f,
            Real center) {
        return taylor(f, center, 20); // Default to 20 terms
    }

    /**
     * Generates a Taylor series expansion with memoized derivatives.
     * <p>
     * This overload pre-computes derivatives up to the specified order for
     * better performance when evaluating multiple terms.
     * </p>
     * f(x) = f(a) + f'(a)(x-a) + f''(a)(x-a)^2/2! + ...
     * 
     * @param f        the differentiable function
     * @param center   the center point 'a'
     * @param maxOrder the maximum order of derivative to compute
     * @return the Taylor series
     */
    public static PowerSeries taylor(org.jscience.mathematics.analysis.DifferentiableFunction<Real, Real> f,
            Real center, int maxOrder) {
        // Pre-compute and cache all derivatives up to maxOrder
        final Real[] cachedDerivativeValues = new Real[maxOrder + 1];
        final long[] cachedFactorials = new long[maxOrder + 1];

        org.jscience.mathematics.analysis.DifferentiableFunction<Real, Real> currentDerivative = f;
        cachedFactorials[0] = 1;

        for (int n = 0; n <= maxOrder; n++) {
            cachedDerivativeValues[n] = currentDerivative.evaluate(center);
            if (n > 0) {
                cachedFactorials[n] = cachedFactorials[n - 1] * n;
            }
            if (n < maxOrder) {
                currentDerivative = (org.jscience.mathematics.analysis.DifferentiableFunction<Real, Real>) currentDerivative
                        .differentiate();
            }
        }

        return new PowerSeries(n -> {
            if (n > maxOrder) {
                return Real.ZERO; // Beyond computed order
            }
            return cachedDerivativeValues[n].divide(Real.of(cachedFactorials[n]));
        }, center, Real.POSITIVE_INFINITY);
    }

    @Override
    public String toString() {
        return String.format("PowerSeries(x=%s, R=%s, %s)",
                x, radiusOfConvergence, isConvergent() ? "convergent" : "divergent");
    }
}
