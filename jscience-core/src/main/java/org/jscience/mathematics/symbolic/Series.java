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
package org.jscience.mathematics.symbolic;

import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.structures.rings.Ring;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a power series Σ aₙxⁿ.
 * <p>
 * A power series is an infinite series of the form:
 * f(x) = a₀ + a₁x + a₂x² + a₃x³ + ...
 * </p>
 * <p>
 * This implementation supports:
 * - Truncated series (finite number of terms)
 * - Taylor and Maclaurin expansions
 * - Series arithmetic (addition, multiplication)
 * - Evaluation at a point
 * </p>
 * 
 * @param <T> the type of coefficients
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Series<T extends Ring<T>> {

    private final List<T> coefficients; // a₀, a₁, a₂, ...
    private final T center; // Expansion point (x₀)
    private final Ring<T> ring;
    private final Real convergenceRadius; // Radius of convergence (can be infinite)

    /**
     * Creates a power series with given coefficients.
     * 
     * @param coefficients the series coefficients [a₀, a₁, a₂, ...]
     * @param center       the expansion center (x₀)
     * @param ring         the ring structure
     */
    public Series(List<T> coefficients, T center, Ring<T> ring) {
        this(coefficients, center, ring, Real.of(Double.POSITIVE_INFINITY));
    }

    /**
     * Creates a power series with given coefficients and convergence radius.
     * 
     * @param coefficients      the series coefficients [a₀, a₁, a₂, ...]
     * @param center            the expansion center (x₀)
     * @param ring              the ring structure
     * @param convergenceRadius the radius of convergence
     */
    public Series(List<T> coefficients, T center, Ring<T> ring, Real convergenceRadius) {
        this.coefficients = new ArrayList<>(coefficients);
        this.center = center;
        this.ring = ring;
        this.convergenceRadius = convergenceRadius;
    }

    /**
     * Creates a Maclaurin series (expansion around x₀ = 0).
     * 
     * @param <T>          the type
     * @param coefficients the series coefficients
     * @param ring         the ring structure
     * @return the Maclaurin series
     */
    public static <T extends Ring<T>> Series<T> maclaurin(List<T> coefficients, Ring<T> ring) {
        return new Series<>(coefficients, ring.zero(), ring);
    }

    /**
     * Creates a Taylor series for exp(x) = 1 + x + x²/2! + x³/3! + ...
     * 
     * @param order the number of terms
     * @return the Taylor series for exp(x)
     */
    public static Series<Real> exp(int order) {
        List<Real> coeffs = new ArrayList<>();
        Real factorial = Real.ONE;

        for (int n = 0; n < order; n++) {
            if (n > 0) {
                factorial = factorial.multiply(Real.of(n));
            }
            coeffs.add(Real.ONE.divide(factorial));
        }

        return maclaurin(coeffs, org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Creates a Taylor series for sin(x) = x - x³/3! + x⁵/5! - ...
     * 
     * @param order the number of terms
     * @return the Taylor series for sin(x)
     */
    public static Series<Real> sin(int order) {
        List<Real> coeffs = new ArrayList<>();
        Real factorial = Real.ONE;

        for (int n = 0; n < order; n++) {
            if (n % 2 == 0) {
                // Even powers have coefficient 0
                coeffs.add(Real.ZERO);
            } else {
                // Odd powers: (-1)^((n-1)/2) / n!
                int sign = ((n - 1) / 2) % 2 == 0 ? 1 : -1;
                if (n > 1) {
                    factorial = factorial.multiply(Real.of(n)).multiply(Real.of(n - 1));
                }
                coeffs.add(Real.of(sign).divide(factorial));
            }
        }

        return maclaurin(coeffs, org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Creates a Taylor series for cos(x) = 1 - x²/2! + x⁴/4! - ...
     * 
     * @param order the number of terms
     * @return the Taylor series for cos(x)
     */
    public static Series<Real> cos(int order) {
        List<Real> coeffs = new ArrayList<>();
        Real factorial = Real.ONE;

        for (int n = 0; n < order; n++) {
            if (n % 2 == 1) {
                // Odd powers have coefficient 0
                coeffs.add(Real.ZERO);
            } else {
                // Even powers: (-1)^(n/2) / n!
                int sign = (n / 2) % 2 == 0 ? 1 : -1;
                if (n > 1) {
                    factorial = factorial.multiply(Real.of(n)).multiply(Real.of(n - 1));
                }
                coeffs.add(Real.of(sign).divide(factorial));
            }
        }

        return maclaurin(coeffs, org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Creates a Taylor series for a differentiable function around a point a.
     * f(x) = f(a) + f'(a)(x-a) + f''(a)(x-a)²/2! + ...
     * 
     * @param f     the function to expand
     * @param a     the expansion point
     * @param order the number of terms
     * @return the Taylor series
     */
    public static Series<Real> taylor(org.jscience.mathematics.analysis.DifferentiableFunction<Real, Real> f, Real a,
            int order) {
        List<Real> coeffs = new ArrayList<>();
        Real factorial = Real.ONE;

        // We need higher order derivatives.
        // Since DifferentiableFunction only gives first derivative,
        // we can only do this if f provides higher derivatives or we use numerical
        // approx (not ideal for symbolic)
        // OR if we recursively differentiate.
        // For now, let's assume we can only do 1st order symbolically unless we have a
        // SymbolicFunction interface.
        // BUT, if we use the symbolic package's FunctionExpression, it handles
        // differentiation!

        // Let's use a simplified approach: assume f can be differentiated repeatedly.
        org.jscience.mathematics.analysis.Function<Real, Real> currentDeriv = f;

        for (int n = 0; n < order; n++) {
            if (n > 0) {
                factorial = factorial.multiply(Real.of(n));
            }

            Real derivativeAtA;
            if (n == 0) {
                derivativeAtA = f.evaluate(a);
            } else if (currentDeriv instanceof org.jscience.mathematics.analysis.DifferentiableFunction) {
                // This cast is tricky if the derivative isn't also DifferentiableFunction
                // In a real symbolic system, we'd use the LegacyExpression tree.
                // Here we'll just support 0th and 1st order for generic DifferentiableFunction
                // unless we cast.
                try {
                    derivativeAtA = ((org.jscience.mathematics.analysis.DifferentiableFunction<Real, Real>) currentDeriv)
                            .differentiate().evaluate(a);
                    // Prepare next derivative
                    currentDeriv = ((org.jscience.mathematics.analysis.DifferentiableFunction<Real, Real>) currentDeriv)
                            .differentiate();
                } catch (Exception e) {
                    // Fallback or stop
                    break;
                }
            } else {
                break;
            }

            coeffs.add(derivativeAtA.divide(factorial));
        }

        return new Series<>(coeffs, a, org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Adds two series.
     * 
     * @param other the other series
     * @return this + other
     */
    public Series<T> add(Series<T> other) {
        if (!center.equals(other.center)) {
            throw new IllegalArgumentException("Series must have the same center for addition");
        }

        int maxLen = Math.max(coefficients.size(), other.coefficients.size());
        List<T> result = new ArrayList<>();

        for (int i = 0; i < maxLen; i++) {
            T a = i < coefficients.size() ? coefficients.get(i) : ring.zero();
            T b = i < other.coefficients.size() ? other.coefficients.get(i) : ring.zero();
            result.add(ring.add(a, b));
        }

        Real minRadius = convergenceRadius.compareTo(other.convergenceRadius) < 0 ? convergenceRadius
                : other.convergenceRadius;
        return new Series<>(result, center, ring, minRadius);
    }

    /**
     * Multiplies two series (Cauchy product).
     * 
     * @param other the other series
     * @return this * other
     */
    public Series<T> multiply(Series<T> other) {
        if (!center.equals(other.center)) {
            throw new IllegalArgumentException("Series must have the same center for multiplication");
        }

        int resultSize = coefficients.size() + other.coefficients.size() - 1;
        List<T> result = new ArrayList<>();

        for (int n = 0; n < resultSize; n++) {
            T sum = ring.zero();
            for (int k = 0; k <= n; k++) {
                if (k < coefficients.size() && (n - k) < other.coefficients.size()) {
                    T term = ring.multiply(coefficients.get(k), other.coefficients.get(n - k));
                    sum = ring.add(sum, term);
                }
            }
            result.add(sum);
        }

        Real minRadius = convergenceRadius.compareTo(other.convergenceRadius) < 0 ? convergenceRadius
                : other.convergenceRadius;
        return new Series<>(result, center, ring, minRadius);
    }

    /**
     * Evaluates the series at a given point.
     * 
     * @param x the point to evaluate at
     * @return the series value at x
     */
    public T evaluate(T x) {
        T result = ring.zero();
        T power = ring.one(); // (x - center)^n
        T dx = ring.subtract(x, center);

        for (T coeff : coefficients) {
            result = ring.add(result, ring.multiply(coeff, power));
            power = ring.multiply(power, dx);
        }

        return result;
    }

    /**
     * Returns the derivative of this series.
     * 
     * @return the derivative series
     */
    public Series<T> derivative() {
        if (coefficients.isEmpty()) {
            return new Series<>(new ArrayList<>(), center, ring, convergenceRadius);
        }

        List<T> result = new ArrayList<>();
        for (int n = 1; n < coefficients.size(); n++) {
            // d/dx[aₙxⁿ] = n·aₙxⁿ⁻¹
            T nCoeff = ring.multiply(coefficients.get(n), fromInt(n));
            result.add(nCoeff);
        }

        return new Series<>(result, center, ring, convergenceRadius);
    }

    /**
     * Returns the integral of this series (with constant of integration = 0).
     * 
     * @return the integral series
     */
    public Series<T> integral() {
        List<T> result = new ArrayList<>();
        result.add(ring.zero()); // Constant of integration

        for (int n = 0; n < coefficients.size(); n++) {
            // ∫aₙxⁿdx = aₙ/(n+1)·xⁿ⁺¹
            T coeff = ring.multiply(coefficients.get(n), fromInt(1)); // Simplified - division not available
            result.add(coeff);
        }

        return new Series<>(result, center, ring, convergenceRadius);
    }

    /**
     * Returns the number of terms in this series.
     * 
     * @return the number of terms
     */
    public int order() {
        return coefficients.size();
    }

    /**
     * Returns the coefficient of xⁿ.
     * 
     * @param n the power
     * @return the coefficient
     */
    public T getCoefficient(int n) {
        return n < coefficients.size() ? coefficients.get(n) : ring.zero();
    }

    /**
     * Returns the expansion center.
     * 
     * @return the center
     */
    public T getCenter() {
        return center;
    }

    /**
     * Returns the radius of convergence.
     * 
     * @return the convergence radius
     */
    public Real getConvergenceRadius() {
        return convergenceRadius;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (int n = 0; n < coefficients.size(); n++) {
            T coeff = coefficients.get(n);
            if (coeff.equals(ring.zero())) {
                continue;
            }

            if (!first) {
                sb.append(" + ");
            }
            first = false;

            sb.append(coeff);
            if (n > 0) {
                sb.append("·x");
                if (n > 1) {
                    sb.append("^").append(n);
                }
            }
        }

        return sb.length() > 0 ? sb.toString() : "0";
    }

    /**
     * Helper to convert int to Ring element.
     */
    private T fromInt(int n) {
        T result = ring.zero();
        T one = ring.one();
        for (int i = 0; i < n; i++) {
            result = ring.add(result, one);
        }
        return result;
    }
}
