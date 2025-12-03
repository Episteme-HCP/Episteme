package org.jscience.demo.conciousness;

import org.jscience.mathematics.number.Real;
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

        // At Φ = Φ₀, V should be 0 (ground state)
        Real v1 = field.evaluatePotential(phi0);
        assertEquals(0.0, v1.doubleValue(), TOLERANCE);

        // At Φ = 0, V = λ/4 * Φ₀⁴ = 1/4 * 16 = 4
        Real v2 = field.evaluatePotential(Real.ZERO);
        assertEquals(4.0, v2.doubleValue(), TOLERANCE);
    }

    @Test
    public void testDerivativeEvaluation() {
        Real lambda = Real.of(1.0);
        Real phi0 = Real.of(2.0);
        SymbolicConsciousnessField field = new SymbolicConsciousnessField(lambda, phi0);

        // At Φ = Φ₀, derivative should be 0
        Real dv1 = field.evaluateDerivative(phi0);
        assertEquals(0.0, dv1.doubleValue(), TOLERANCE);

        // At Φ = 0, derivative should be 0 (unstable equilibrium)
        Real dv2 = field.evaluateDerivative(Real.ZERO);
        assertEquals(0.0, dv2.doubleValue(), TOLERANCE);
    }

    @Test
    public void testSymmetryBreaking() {
        Real lambda = Real.of(0.5);
        Real phi0 = Real.of(1.0);
        SymbolicConsciousnessField field = new SymbolicConsciousnessField(lambda, phi0);

        // Ground states at Φ = ±Φ₀
        assertEquals(0.0, field.evaluatePotential(Real.of(1.0)).doubleValue(), TOLERANCE);
        assertEquals(0.0, field.evaluatePotential(Real.of(-1.0)).doubleValue(), TOLERANCE);

        // Symmetric state Φ=0 has higher potential (unstable)
        Real vSymmetric = field.evaluatePotential(Real.ZERO);
        assertTrue(vSymmetric.doubleValue() > 0.0);
    }

    @Test
    public void testSecondDerivative() {
        Real lambda = Real.of(1.0);
        Real phi0 = Real.of(1.0);
        SymbolicConsciousnessField field = new SymbolicConsciousnessField(lambda, phi0);

        // At Φ = 0, second derivative should be negative (local maximum)
        Real d2v = field.evaluateSecondDerivative(Real.ZERO);
        assertTrue(d2v.doubleValue() < 0.0, "Second derivative at Φ=0 should be negative (unstable)");

        // At Φ = Φ₀, second derivative should be positive (local minimum)
        Real d2vGround = field.evaluateSecondDerivative(phi0);
        assertTrue(d2vGround.doubleValue() > 0.0, "Second derivative at Φ=Φ₀ should be positive (stable)");
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
        assertTrue(potentialStr.contains("Φ"));
        assertTrue(derivativeStr.contains("Φ"));
    }
}
