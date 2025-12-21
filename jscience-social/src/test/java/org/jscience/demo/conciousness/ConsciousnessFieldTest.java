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
package org.jscience.demo.conciousness;

import org.jscience.mathematics.numbers.real.Real;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConsciousnessFieldTest {

    private static final double TOLERANCE = 1e-9;

    @Test
    public void testPotential() {
        Real lambda = Real.of(1.0);
        Real phi0 = Real.of(2.0);
        ConsciousnessField field = new ConsciousnessField(lambda, phi0);

        // V(Φ) = λ/4 (Φ² - Φ₀²)²

        // At Φ = Φ₀, V should be 0 (ground state)
        assertEquals(0.0, field.potential(phi0).doubleValue(), TOLERANCE);
        assertEquals(0.0, field.potential(phi0.negate()).doubleValue(), TOLERANCE);

        // At Φ = 0, V = λ/4 * (-Φ₀²)² = λ/4 * Φ₀⁴
        // 1/4 * 16 = 4
        assertEquals(4.0, field.potential(Real.ZERO).doubleValue(), TOLERANCE);

        // At Φ = 3
        // V = 1/4 * (9 - 4)² = 1/4 * 25 = 6.25
        assertEquals(6.25, field.potential(Real.of(3.0)).doubleValue(), TOLERANCE);
    }

    @Test
    public void testDerivative() {
        Real lambda = Real.of(1.0);
        Real phi0 = Real.of(2.0);
        ConsciousnessField field = new ConsciousnessField(lambda, phi0);

        // dV/dΦ = λ * Φ * (Φ² - Φ₀²)

        // At Φ = Φ₀, derivative should be 0
        assertEquals(0.0, field.potentialDerivative(phi0).doubleValue(), TOLERANCE);

        // At Φ = 0, derivative should be 0 (unstable equilibrium)
        assertEquals(0.0, field.potentialDerivative(Real.ZERO).doubleValue(), TOLERANCE);

        // At Φ = 3
        // dV/dΦ = 1 * 3 * (9 - 4) = 15
        assertEquals(15.0, field.potentialDerivative(Real.of(3.0)).doubleValue(), TOLERANCE);
    }

    @Test
    public void testStates() {
        Real lambda = Real.of(0.5);
        Real phi0 = Real.of(1.0);
        ConsciousnessField field = new ConsciousnessField(lambda, phi0);

        assertTrue(field.isSymmetricState(Real.ZERO));
        assertFalse(field.isSymmetricState(Real.of(0.1)));

        assertTrue(field.isGroundState(Real.of(1.0), TOLERANCE));
        assertTrue(field.isGroundState(Real.of(-1.0), TOLERANCE));
        assertFalse(field.isGroundState(Real.ZERO, TOLERANCE));
    }

    @Test
    public void testInvalidConstruction() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ConsciousnessField(Real.of(-1.0), Real.of(1.0));
        });
    }
}
