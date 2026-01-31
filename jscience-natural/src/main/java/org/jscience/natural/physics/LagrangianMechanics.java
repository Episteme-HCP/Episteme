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

package org.jscience.natural.physics;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Lagrangian mechanics - analytical mechanics using generalized coordinates.
 * <p>
 * Based on Hamilton's principle of least action: ÃƒÅ½Ã‚Â´S = ÃƒÅ½Ã‚Â´ÃƒÂ¢Ã‹â€ Ã‚Â«L dt = 0
 * Euler-Lagrange equation: d/dt(ÃƒÂ¢Ã‹â€ Ã¢â‚¬Å¡L/ÃƒÂ¢Ã‹â€ Ã¢â‚¬Å¡qÃƒÅ’Ã¢â‚¬Â¡) - ÃƒÂ¢Ã‹â€ Ã¢â‚¬Å¡L/ÃƒÂ¢Ã‹â€ Ã¢â‚¬Å¡q = 0
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LagrangianMechanics {

    /**
     * Lagrangian for free particle: L = T = Ãƒâ€šÃ‚Â½mvÃƒâ€šÃ‚Â²
     */
    public static Real lagrangianFreeParticle(Real mass, Real velocity) {
        return Real.of(0.5).multiply(mass).multiply(velocity).multiply(velocity);
    }

    public static Real lagrangianFreeParticle(Real mass, org.jscience.core.mathematics.linearalgebra.Vector<Real> velocity) {
        Real v2 = velocity.norm().pow(2);
        return Real.of(0.5).multiply(mass).multiply(v2);
    }

    /**
     * Lagrangian for particle in potential: L = T - V = Ãƒâ€šÃ‚Â½mvÃƒâ€šÃ‚Â² - V(x)
     */
    public static Real lagrangian(Real kineticEnergy, Real potentialEnergy) {
        return kineticEnergy.subtract(potentialEnergy);
    }

    /**
     * Generalized momentum: p = ÃƒÂ¢Ã‹â€ Ã¢â‚¬Å¡L/ÃƒÂ¢Ã‹â€ Ã¢â‚¬Å¡qÃƒÅ’Ã¢â‚¬Â¡
     */
    public static Real generalizedMomentum(Real mass, Real generalizedVelocity) {
        // For simple case: p = m * qÃƒÅ’Ã¢â‚¬Â¡
        return mass.multiply(generalizedVelocity);
    }

    public static org.jscience.core.mathematics.linearalgebra.Vector<Real> generalizedMomentum(Real mass,
            org.jscience.core.mathematics.linearalgebra.Vector<Real> velocity) {
        return velocity.multiply(mass);
    }

    /**
     * Action integral: S = ÃƒÂ¢Ã‹â€ Ã‚Â«ÃƒÂ¢Ã¢â‚¬Å¡Ã‚ÂÃƒâ€šÃ‚Â² L dt
     * (Simplified as L * ÃƒÅ½Ã¢â‚¬Ât for constant Lagrangian)
     */
    public static Real action(Real lagrangian, Real timeInterval) {
        return lagrangian.multiply(timeInterval);
    }

    /**
     * Lagrangian for harmonic oscillator: L = Ãƒâ€šÃ‚Â½mÃƒÂ¡Ã‚ÂºÃ¢â‚¬Â¹Ãƒâ€šÃ‚Â² - Ãƒâ€šÃ‚Â½kxÃƒâ€šÃ‚Â²
     */
    public static Real lagrangianHarmonicOscillator(Real mass, Real velocity, Real springConstant, Real position) {
        Real kinetic = Real.of(0.5).multiply(mass).multiply(velocity).multiply(velocity);
        Real potential = Real.of(0.5).multiply(springConstant).multiply(position).multiply(position);
        return kinetic.subtract(potential);
    }

    /**
     * Lagrangian for pendulum: L = Ãƒâ€šÃ‚Â½mlÃƒâ€šÃ‚Â²ÃƒÅ½Ã‚Â¸ÃƒÅ’Ã¢â‚¬Â¡Ãƒâ€šÃ‚Â² + mgl cos(ÃƒÅ½Ã‚Â¸)
     */
    public static Real lagrangianPendulum(Real mass, Real length, Real angularVel, Real angle, Real g) {
        Real kinetic = Real.of(0.5).multiply(mass).multiply(length).multiply(length)
                .multiply(angularVel).multiply(angularVel);
        Real potential = mass.multiply(g).multiply(length).multiply(angle.cos());
        return kinetic.add(potential); // +mgl cos(ÃƒÅ½Ã‚Â¸) since we measure from bottom
    }

    /**
     * Lagrangian for particle in central force (2D polar): L = Ãƒâ€šÃ‚Â½m(ÃƒÂ¡Ã‚Â¹Ã¢â€žÂ¢Ãƒâ€šÃ‚Â² + rÃƒâ€šÃ‚Â²ÃƒÅ½Ã‚Â¸ÃƒÅ’Ã¢â‚¬Â¡Ãƒâ€šÃ‚Â²) -
     * V(r)
     */
    public static Real lagrangianCentralForce2D(Real mass, Real radialVel, Real radius,
            Real angularVel, Real potential) {
        Real radialKE = Real.of(0.5).multiply(mass).multiply(radialVel).multiply(radialVel);
        Real angularKE = Real.of(0.5).multiply(mass).multiply(radius).multiply(radius)
                .multiply(angularVel).multiply(angularVel);
        return radialKE.add(angularKE).subtract(potential);
    }
}



