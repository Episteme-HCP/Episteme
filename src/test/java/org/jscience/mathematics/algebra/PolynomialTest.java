package org.jscience.mathematics.algebra;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.sets.Reals;

public class PolynomialTest {

    @Test
    public void testPolynomialCreation() {
        // p(x) = 2 + 3x + x²
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
        // (1 + x) * (1 + x) = 1 + 2x + x²
        Polynomial<Real> p = Polynomial.of(Reals.getInstance(), Real.ONE, Real.ONE);

        Polynomial<Real> square = p.multiply(p);

        assertEquals(2, square.degree());
        assertEquals(Real.ONE, square.getCoefficient(0));
        assertEquals(Real.of(2), square.getCoefficient(1));
        assertEquals(Real.ONE, square.getCoefficient(2));
    }

    @Test
    public void testPolynomialEvaluation() {
        // p(x) = 2 + 3x + x²
        // p(2) = 2 + 6 + 4 = 12
        Polynomial<Real> p = Polynomial.of(Reals.getInstance(),
                Real.of(2), Real.of(3), Real.ONE);

        Real result = p.evaluate(Real.of(2));

        assertEquals(Real.of(12), result);
    }
}
