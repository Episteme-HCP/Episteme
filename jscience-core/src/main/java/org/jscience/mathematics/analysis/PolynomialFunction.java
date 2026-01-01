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

package org.jscience.mathematics.analysis;

import java.util.Map;
import java.util.HashMap;
import org.jscience.mathematics.structures.rings.Ring;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.mathematics.algebra.rings.PolynomialRing;

/**
 * Represents a polynomial function P(x) over a Ring R.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PolynomialFunction<R> implements DifferentiableFunction<R, R>, IntegrableFunction<R, R> {

    private final PolynomialRing.Polynomial<R> polynomial;
    private final PolynomialRing<R> ring;

    /**
     * Creates a polynomial function from a polynomial object.
     * 
     * @param polynomial the underlying polynomial
     * @param ring       the polynomial ring
     */
    public PolynomialFunction(PolynomialRing.Polynomial<R> polynomial, PolynomialRing<R> ring) {
        this.polynomial = polynomial;
        this.ring = ring;
    }

    @Override
    public R evaluate(R x) {
        return polynomial.evaluate(x);
    }

    @Override
    public Function<R, R> differentiate() {
        Map<Integer, R> coeffs = polynomial.getCoefficients();
        Map<Integer, R> derivCoeffs = new HashMap<>();

        Ring<R> r = ring.getCoefficientRing();

        for (Map.Entry<Integer, R> entry : coeffs.entrySet()) {
            int deg = entry.getKey();
            if (deg > 0) {
                R val = entry.getValue();
                // Multiply by degree: val * deg
                R newVal = r.zero();
                // Naive repeated addition
                for (int i = 0; i < deg; i++) {
                    newVal = r.add(newVal, val);
                }

                derivCoeffs.put(deg - 1, newVal);
            }
        }

        return new PolynomialFunction<>(ring.create(derivCoeffs), ring);
    }

    @Override
    public Function<R, R> integrate() {
        if (!(ring.getCoefficientRing() instanceof Field)) {
            throw new UnsupportedOperationException("Integration requires a Field (division support). Current ring: "
                    + ring.getCoefficientRing().getClass().getSimpleName());
        }

        Field<R> field = (Field<R>) ring
                .getCoefficientRing();

        Map<Integer, R> coeffs = polynomial.getCoefficients();
        Map<Integer, R> intCoeffs = new HashMap<>();

        for (Map.Entry<Integer, R> entry : coeffs.entrySet()) {
            int deg = entry.getKey();
            R val = entry.getValue();

            // Divide by (deg + 1)
            R divisor = field.one();
            for (int i = 0; i < deg; i++) {
                divisor = field.add(divisor, field.one());
            }

            R newVal = field.divide(val, divisor);
            intCoeffs.put(deg + 1, newVal);
        }

        return new PolynomialFunction<>(ring.create(intCoeffs), ring);
    }

    @Override
    public R integrate(R start, R end) {
        Function<R, R> F = integrate();
        R fEnd = F.evaluate(end);
        R fStart = F.evaluate(start);

        Ring<R> r = ring.getCoefficientRing();
        return r.add(fEnd, r.negate(fStart));
    }

    @Override
    public String getDomain() {
        return ring.description().split("\\[")[0];
    }

    @Override
    public String getCodomain() {
        return getDomain();
    }

    @Override
    public String toString() {
        return polynomial.toString();
    }
}

