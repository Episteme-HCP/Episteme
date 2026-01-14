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

package org.jscience.mathematics.analysis.functions;

import java.util.*;
import org.jscience.mathematics.structures.rings.Ring;

/**
 * Polynomial with coefficients in a ring.
 * <p>
 * Represents polynomials like: aÃ¢â€šâ‚¬ + aÃ¢â€šÂx + aÃ¢â€šâ€šxÃ‚Â² + ... + aÃ¢â€šâ„¢xÃ¢ÂÂ¿
 * Supports arithmetic operations and evaluation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Polynomial<R> {

    private final List<R> coefficients; // index i = coefficient of x^i
    private final Ring<R> ring;

    /**
     * Creates a polynomial from coefficients (lowest degree first).
     * 
     * @param coefficients coefficients [aÃ¢â€šâ‚¬, aÃ¢â€šÂ, aÃ¢â€šâ€š, ...]
     * @param ring         the coefficient ring
     */
    public Polynomial(List<R> coefficients, Ring<R> ring) {
        this.ring = ring;
        this.coefficients = new ArrayList<>(coefficients);
        normalize();
    }

    /**
     * Creates a polynomial from coefficients.
     */
    @SafeVarargs
    public static <R> Polynomial<R> of(Ring<R> ring, R... coefficients) {
        return new Polynomial<>(Arrays.asList(coefficients), ring);
    }

    /**
     * Creates constant polynomial.
     */
    public static <R> Polynomial<R> constant(R value, Ring<R> ring) {
        return new Polynomial<>(Collections.singletonList(value), ring);
    }

    /**
     * Creates the polynomial x.
     */
    public static <R> Polynomial<R> x(Ring<R> ring) {
        return new Polynomial<>(Arrays.asList(ring.zero(), ring.one()), ring);
    }

    /**
     * Creates zero polynomial.
     */
    public static <R> Polynomial<R> zero(Ring<R> ring) {
        return constant(ring.zero(), ring);
    }

    /**
     * Creates monomial: coefficient * x^degree.
     */
    public static <R> Polynomial<R> monomial(R coefficient, int degree, Ring<R> ring) {
        List<R> coeffs = new ArrayList<>();
        for (int i = 0; i < degree; i++) {
            coeffs.add(ring.zero());
        }
        coeffs.add(coefficient);
        return new Polynomial<>(coeffs, ring);
    }

    /**
     * Returns degree of polynomial (highest power with non-zero coefficient).
     */
    public int degree() {
        return coefficients.size() - 1;
    }

    /**
     * Returns true if this polynomial is zero.
     */
    public boolean isZero() {
        return coefficients.size() == 1 && coefficients.get(0).equals(ring.zero());
    }

    /**
     * Returns coefficient of x^i (alias for getCoefficient).
     */
    public R coefficient(int i) {
        return getCoefficient(i);
    }

    /**
     * Returns coefficient of x^i.
     */
    public R getCoefficient(int i) {
        return i < coefficients.size() ? coefficients.get(i) : ring.zero();
    }

    /**
     * Adds two polynomials.
     */
    public Polynomial<R> add(Polynomial<R> other) {
        int maxDegree = Math.max(this.degree(), other.degree());
        List<R> result = new ArrayList<>();

        for (int i = 0; i <= maxDegree; i++) {
            R a = this.getCoefficient(i);
            R b = other.getCoefficient(i);
            result.add(ring.add(a, b));
        }

        return new Polynomial<>(result, ring);
    }

    /**
     * Subtracts two polynomials.
     */
    public Polynomial<R> subtract(Polynomial<R> other) {
        int maxDegree = Math.max(this.degree(), other.degree());
        List<R> result = new ArrayList<>();

        for (int i = 0; i <= maxDegree; i++) {
            R a = this.getCoefficient(i);
            R b = other.getCoefficient(i);
            result.add(ring.subtract(a, b));
        }

        return new Polynomial<>(result, ring);
    }

    /**
     * Multiplies two polynomials.
     */
    public Polynomial<R> multiply(Polynomial<R> other) {
        int newDegree = this.degree() + other.degree();
        List<R> result = new ArrayList<>(Collections.nCopies(newDegree + 1, ring.zero()));

        for (int i = 0; i <= this.degree(); i++) {
            for (int j = 0; j <= other.degree(); j++) {
                R product = ring.multiply(this.getCoefficient(i), other.getCoefficient(j));
                result.set(i + j, ring.add(result.get(i + j), product));
            }
        }

        return new Polynomial<>(result, ring);
    }

    /**
     * Evaluates polynomial at given value.
     */
    public R evaluate(R x) {
        if (coefficients.isEmpty())
            return ring.zero();

        // Horner's method: aÃ¢â€šâ‚¬ + x(aÃ¢â€šÂ + x(aÃ¢â€šâ€š + ...))
        R result = coefficients.get(coefficients.size() - 1);
        for (int i = coefficients.size() - 2; i >= 0; i--) {
            result = ring.add(coefficients.get(i), ring.multiply(x, result));
        }
        return result;
    }

    /**
     * Returns derivative of polynomial.
     */
    public Polynomial<R> derivative() {
        if (degree() == 0) {
            return constant(ring.zero(), ring);
        }

        List<R> result = new ArrayList<>();
        for (int i = 1; i < coefficients.size(); i++) {
            // d/dx(aÃ¡ÂµÂ¢xÃ¢ÂÂ±) = iÃ‚Â·aÃ¡ÂµÂ¢xÃ¢ÂÂ±Ã¢ÂÂ»Ã‚Â¹
            R coeff = coefficients.get(i);
            for (int j = 1; j < i; j++) {
                coeff = ring.add(coeff, coefficients.get(i));
            }
            result.add(coeff);
        }

        return new Polynomial<>(result, ring);
    }

    private void normalize() {
        // Remove leading zeros
        while (!coefficients.isEmpty() && coefficients.get(coefficients.size() - 1).equals(ring.zero())) {
            coefficients.remove(coefficients.size() - 1);
        }
        if (coefficients.isEmpty()) {
            coefficients.add(ring.zero());
        }
    }

    @Override
    public String toString() {
        if (coefficients.isEmpty() || (coefficients.size() == 1 && coefficients.get(0).equals(ring.zero()))) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = coefficients.size() - 1; i >= 0; i--) {
            R coeff = coefficients.get(i);
            if (coeff.equals(ring.zero()))
                continue;

            if (sb.length() > 0)
                sb.append(" + ");

            sb.append(coeff);
            if (i > 0) {
                sb.append("x");
                if (i > 1)
                    sb.append("^").append(i);
            }
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Polynomial))
            return false;
        Polynomial<?> other = (Polynomial<?>) obj;
        return coefficients.equals(other.coefficients);
    }

    @Override
    public int hashCode() {
        return coefficients.hashCode();
    }
}


