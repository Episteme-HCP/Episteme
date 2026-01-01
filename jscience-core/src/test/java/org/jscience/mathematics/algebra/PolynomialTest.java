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

package org.jscience.mathematics.algebra;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import org.jscience.mathematics.analysis.functions.Polynomial;

public class PolynomialTest {

    @Test
    public void testPolynomialCreation() {
        // p(x) = 2 + 3x + xÃ‚Â²
        Polynomial<Real> p = Polynomial.of(Reals.getInstance(),
                Real.of(2), Real.of(3), Real.ONE);

        assertEquals(2, p.degree());
        assertEquals(Real.of(2), p.getCoefficient(0));
        assertEquals(Real.of(3), p.getCoefficient(1));
        assertEquals(Real.ONE, p.getCoefficient(2));
    }

    @Test
    public void testPolynomialAddition() {
        // (2 + 3x) + (1 + x) = 3 + 4x
        Polynomial<Real> p1 = Polynomial.of(Reals.getInstance(), Real.of(2), Real.of(3));
        Polynomial<Real> p2 = Polynomial.of(Reals.getInstance(), Real.ONE, Real.ONE);

        Polynomial<Real> sum = p1.add(p2);

        assertEquals(Real.of(3), sum.getCoefficient(0));
        assertEquals(Real.of(4), sum.getCoefficient(1));
    }

    @Test
    public void testPolynomialMultiplication() {
        // (1 + x) * (1 + x) = 1 + 2x + xÃ‚Â²
        Polynomial<Real> p = Polynomial.of(Reals.getInstance(), Real.ONE, Real.ONE);

        Polynomial<Real> square = p.multiply(p);

        assertEquals(2, square.degree());
        assertEquals(Real.ONE, square.getCoefficient(0));
        assertEquals(Real.of(2), square.getCoefficient(1));
        assertEquals(Real.ONE, square.getCoefficient(2));
    }

    @Test
    public void testPolynomialEvaluation() {
        // p(x) = 2 + 3x + xÃ‚Â²
        // p(2) = 2 + 6 + 4 = 12
        Polynomial<Real> p = Polynomial.of(Reals.getInstance(),
                Real.of(2), Real.of(3), Real.ONE);

        Real result = p.evaluate(Real.of(2));

        assertEquals(Real.of(12), result);
    }
}

