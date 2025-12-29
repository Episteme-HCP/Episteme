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

/**
 * Model for Universal Consciousness as a Foundational Field.
 * <p>
 * Based on the theoretical framework proposing consciousness as a fundamental
 * field
 * exhibiting symmetry breaking dynamics.
 * </p>
 * <p>
 * <strong>Reference:</strong>
 * Strømme, M. (2025). "Universal consciousness as foundational field: A
 * theoretical
 * bridge between quantum physics and non-dual philosophy." AIP Advances,
 * 15(11).
 * DOI: <a href="https://doi.org/10.1063/5.0290984">10.1063/5.0290984</a>
 * </p>
 * <p>
 * The field potential is described by:
 *
 * <pre>
 * V(Φ) = λ/4 (Φ² - Φ₀²)²
 * </pre>
 *
 * where:
 * <ul>
 * <li>Φ is the consciousness field strength</li>
 * <li>Φ₀ is the vacuum expectation value (symmetry breaking scale)</li>
 * <li>λ is the self-coupling constant</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ConsciousnessField {

    private final Real lambda; // Coupling constant
    private final Real phi0; // Vacuum expectation value

    /**
     * Creates a new Consciousness Field model.
     * 
     * @param lambda self-coupling constant (must be positive)
     * @param phi0   vacuum expectation value
     */
    public ConsciousnessField(Real lambda, Real phi0) {
        if (lambda.sign() <= 0) {
            throw new IllegalArgumentException("Coupling constant lambda must be positive");
        }
        this.lambda = lambda;
        this.phi0 = phi0;
    }

    /**
     * Calculates the potential energy density V(Φ).
     * 
     * @param phi field value
     * @return potential energy density
     */
    public Real potential(Real phi) {
        // V(Φ) = λ/4 (Φ² - Φ₀²)²
        Real phiSq = phi.pow(2);
        Real phi0Sq = phi0.pow(2);
        Real diff = phiSq.subtract(phi0Sq);
        return lambda.divide(Real.of(4)).multiply(diff.pow(2));
    }

    /**
     * Calculates the field derivative dV/dΦ.
     * 
     * @param phi field value
     * @return derivative of potential
     */
    public Real potentialDerivative(Real phi) {
        // dV/dΦ = λ * Φ * (Φ² - Φ₀²)
        Real phiSq = phi.pow(2);
        Real phi0Sq = phi0.pow(2);
        return lambda.multiply(phi).multiply(phiSq.subtract(phi0Sq));
    }

    /**
     * Checks if the field is in a symmetric state (Φ = 0).
     * In this model, Φ=0 is a local maximum (unstable) if Φ₀ > 0.
     * 
     * @param phi field value
     * @return true if phi is zero
     */
    public boolean isSymmetricState(Real phi) {
        return phi.isZero();
    }

    /**
     * Checks if the field is in a ground state (Φ = ±Φ₀).
     * 
     * @param phi       field value
     * @param tolerance numerical tolerance
     * @return true if phi is close to ±Φ₀
     */
    public boolean isGroundState(Real phi, double tolerance) {
        Real diff = phi.abs().subtract(phi0.abs()).abs();
        return diff.doubleValue() < tolerance;
    }

    public Real getLambda() {
        return lambda;
    }

    public Real getPhi0() {
        return phi0;
    }
}
