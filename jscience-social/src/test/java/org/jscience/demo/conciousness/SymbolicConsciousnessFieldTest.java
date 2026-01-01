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

package org.jscience.demo.conciousness;

import org.jscience.mathematics.numbers.real.Real;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SymbolicConsciousnessFieldTest {

    private static final double TOLERANCE = 1e-6;

    @Test
    public void testSymbolicConstruction() {
        Real lambda = Real.of(1.0);
        Real phi0 = Real.of(2.0);
        SymbolicConsciousnessField field = new SymbolicConsciousnessField(lambda, phi0);

        assertNotNull(field.getFieldVariable());
        assertNotNull(field.getPotential());
        assertNotNull(field.getDerivative());
        assertNotNull(field.getSecondDerivative());
    }

    @Test
    public void testPotentialEvaluation() {
        Real lambda = Real.of(1.0);
        Real phi0 = Real.of(2.0);
        SymbolicConsciousnessField field = new SymbolicConsciousnessField(lambda, phi0);

        // At ÃŽÂ¦ = ÃŽÂ¦Ã¢â€šâ‚¬, V should be 0 (ground state)
        Real v1 = field.evaluatePotential(phi0);
        assertEquals(0.0, v1.doubleValue(), TOLERANCE);

        // At ÃŽÂ¦ = 0, V = ÃŽÂ»/4 * ÃŽÂ¦Ã¢â€šâ‚¬Ã¢ÂÂ´ = 1/4 * 16 = 4
        Real v2 = field.evaluatePotential(Real.ZERO);
        assertEquals(4.0, v2.doubleValue(), TOLERANCE);
    }

    @Test
    public void testDerivativeEvaluation() {
        Real lambda = Real.of(1.0);
        Real phi0 = Real.of(2.0);
        SymbolicConsciousnessField field = new SymbolicConsciousnessField(lambda, phi0);

        // At ÃŽÂ¦ = ÃŽÂ¦Ã¢â€šâ‚¬, derivative should be 0
        Real dv1 = field.evaluateDerivative(phi0);
        assertEquals(0.0, dv1.doubleValue(), TOLERANCE);

        // At ÃŽÂ¦ = 0, derivative should be 0 (unstable equilibrium)
        Real dv2 = field.evaluateDerivative(Real.ZERO);
        assertEquals(0.0, dv2.doubleValue(), TOLERANCE);
    }

    @Test
    public void testSymmetryBreaking() {
        Real lambda = Real.of(0.5);
        Real phi0 = Real.of(1.0);
        SymbolicConsciousnessField field = new SymbolicConsciousnessField(lambda, phi0);

        // Ground states at ÃŽÂ¦ = Ã‚Â±ÃŽÂ¦Ã¢â€šâ‚¬
        assertEquals(0.0, field.evaluatePotential(Real.of(1.0)).doubleValue(), TOLERANCE);
        assertEquals(0.0, field.evaluatePotential(Real.of(-1.0)).doubleValue(), TOLERANCE);

        // Symmetric state ÃŽÂ¦=0 has higher potential (unstable)
        Real vSymmetric = field.evaluatePotential(Real.ZERO);
        assertTrue(vSymmetric.doubleValue() > 0.0);
    }

    @Test
    public void testSecondDerivative() {
        Real lambda = Real.of(1.0);
        Real phi0 = Real.of(1.0);
        SymbolicConsciousnessField field = new SymbolicConsciousnessField(lambda, phi0);

        // At ÃŽÂ¦ = 0, second derivative should be negative (local maximum)
        Real d2v = field.evaluateSecondDerivative(Real.ZERO);
        assertTrue(d2v.doubleValue() < 0.0, "Second derivative at ÃŽÂ¦=0 should be negative (unstable)");

        // At ÃŽÂ¦ = ÃŽÂ¦Ã¢â€šâ‚¬, second derivative should be positive (local minimum)
        Real d2vGround = field.evaluateSecondDerivative(phi0);
        assertTrue(d2vGround.doubleValue() > 0.0, "Second derivative at ÃŽÂ¦=ÃŽÂ¦Ã¢â€šâ‚¬ should be positive (stable)");
    }

    @Test
    public void testSymbolicOutput() {
        Real lambda = Real.of(1.0);
        Real phi0 = Real.of(1.0);
        SymbolicConsciousnessField field = new SymbolicConsciousnessField(lambda, phi0);

        String potentialStr = field.potentialToString();
        String derivativeStr = field.derivativeToString();

        assertNotNull(potentialStr);
        assertNotNull(derivativeStr);
        assertTrue(potentialStr.contains("ÃŽÂ¦"));
        assertTrue(derivativeStr.contains("ÃŽÂ¦"));
    }
}


