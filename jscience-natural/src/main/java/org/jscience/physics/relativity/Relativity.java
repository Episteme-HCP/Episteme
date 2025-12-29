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

package org.jscience.physics.relativity;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.PhysicalConstants;

/**
 * Special and General Relativity.
 * <p>
 * Lorentz transformations, time dilation, length contraction, relativistic
 * dynamics.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Relativity {

    /**
     * Lorentz factor: γ = 1/√(1 - v²/c²)
     */
    /**
     * Lorentz factor: γ = 1/√(1 - v²/c²)
     */
    public static Real lorentzFactor(Real velocity) {
        Real c = PhysicalConstants.SPEED_OF_LIGHT.getValue();
        Real beta = velocity.divide(c);
        Real betaSquared = beta.multiply(beta);
        return Real.ONE.divide(Real.ONE.subtract(betaSquared).sqrt());
    }

    /**
     * Time dilation: Δt = γΔt₀
     * Returns dilated time for moving observer
     */
    public static Real timeDilation(Real properTime, Real velocity) {
        return lorentzFactor(velocity).multiply(properTime);
    }

    /**
     * Length contraction: L = L₀/γ
     * Returns contracted length in moving frame
     */
    public static Real lengthContraction(Real properLength, Real velocity) {
        return properLength.divide(lorentzFactor(velocity));
    }

    /**
     * Relativistic momentum: p = γm₀v
     */
    public static Real relativisticMomentum(Real restMass, Real velocity) {
        return lorentzFactor(velocity).multiply(restMass).multiply(velocity);
    }

    /**
     * Relativistic kinetic energy: KE = (γ - 1)m₀c²
     */
    public static Real relativisticKineticEnergy(Real restMass, Real velocity) {
        Real c = PhysicalConstants.SPEED_OF_LIGHT.getValue();
        Real gamma = lorentzFactor(velocity);
        return gamma.subtract(Real.ONE).multiply(restMass).multiply(c).multiply(c);
    }

    /**
     * Total relativistic energy: E = γm₀c²
     */
    public static Real totalEnergy(Real restMass, Real velocity) {
        Real c = PhysicalConstants.SPEED_OF_LIGHT.getValue();
        return lorentzFactor(velocity).multiply(restMass).multiply(c).multiply(c);
    }

    /**
     * Energy-momentum relation: E² = (pc)² + (m₀c²)²
     * Returns total energy from momentum
     */
    public static Real energyFromMomentum(Real momentum, Real restMass) {
        Real c = PhysicalConstants.SPEED_OF_LIGHT.getValue();
        Real pc = momentum.multiply(c);
        Real m0c2 = restMass.multiply(c).multiply(c);
        return pc.multiply(pc).add(m0c2.multiply(m0c2)).sqrt();
    }

    /**
     * Velocity addition formula: v = (v₁ + v₂)/(1 + v₁v₂/c²)
     */
    public static Real velocityAddition(Real v1, Real v2) {
        Real c = PhysicalConstants.SPEED_OF_LIGHT.getValue();
        Real numerator = v1.add(v2);
        Real denominator = Real.ONE.add(v1.multiply(v2).divide(c.multiply(c)));
        return numerator.divide(denominator);
    }

    /**
     * Doppler effect (relativistic): f = f₀√((1-β)/(1+β))
     * For source moving directly away (β = v/c)
     */
    public static Real relativisticDoppler(Real sourceFreq, Real velocity, boolean approaching) {
        Real c = PhysicalConstants.SPEED_OF_LIGHT.getValue();
        Real beta = velocity.divide(c);

        Real factor;
        if (approaching) {
            factor = Real.ONE.add(beta).divide(Real.ONE.subtract(beta)).sqrt();
        } else {
            factor = Real.ONE.subtract(beta).divide(Real.ONE.add(beta)).sqrt();
        }

        return sourceFreq.multiply(factor);
    }

    /**
     * Schwarzschild radius: r_s = 2GM/c²
     * Event horizon radius for black hole
     */
    public static Real schwarzschildRadius(Real mass) {
        Real G = PhysicalConstants.GRAVITATIONAL_CONSTANT.getValue();
        Real c = PhysicalConstants.SPEED_OF_LIGHT.getValue();
        return Real.TWO.multiply(G).multiply(mass).divide(c.multiply(c));
    }

    /**
     * Gravitational time dilation: t = t₀/√(1 - r_s/r)
     * Near massive object at distance r
     */
    public static Real gravitationalTimeDilation(Real properTime, Real radius, Real schwarzschildRadius) {
        Real factor = Real.ONE.subtract(schwarzschildRadius.divide(radius)).sqrt();
        return properTime.divide(factor);
    }

    /**
     * Escape velocity: v_esc = √(2GM/r) = c√(r_s/r)
     */
    public static Real escapeVelocity(Real mass, Real radius) {
        Real G = PhysicalConstants.GRAVITATIONAL_CONSTANT.getValue();
        return Real.TWO.multiply(G).multiply(mass).divide(radius).sqrt();
    }

    /**
     * Photon sphere radius: r_ph = 3GM/c² = 1.5 r_s
     */
    public static Real photonSphereRadius(Real mass) {
        return Real.of(1.5).multiply(schwarzschildRadius(mass));
    }
}
