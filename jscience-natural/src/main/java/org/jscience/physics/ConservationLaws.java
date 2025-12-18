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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.physics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Conservation laws - fundamental principles governing all physical systems.
 * <p>
 * Based on Noether's theorem: every continuous symmetry corresponds to a
 * conservation law.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class ConservationLaws {

    /**
     * Conservation of energy in closed system.
     * E_initial = E_final
     */
    public static boolean checkEnergyConservation(Real initialEnergy, Real finalEnergy, Real tolerance) {
        return initialEnergy.subtract(finalEnergy).abs().compareTo(tolerance) < 0;
    }

    /**
     * Conservation of linear momentum in closed system.
     * p_initial = p_final (vector sum)
     */
    public static boolean checkMomentumConservation(Real[] initialMomentum, Real[] finalMomentum, Real tolerance) {
        if (initialMomentum.length != finalMomentum.length)
            return false;

        for (int i = 0; i < initialMomentum.length; i++) {
            if (initialMomentum[i].subtract(finalMomentum[i]).abs().compareTo(tolerance) >= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Conservation of angular momentum in closed system.
     * L_initial = L_final
     */
    public static boolean checkAngularMomentumConservation(Real initialL, Real finalL, Real tolerance) {
        return initialL.subtract(finalL).abs().compareTo(tolerance) < 0;
    }

    /**
     * Conservation of electric charge.
     * Q_total = constant
     */
    public static boolean checkChargeConservation(Real initialCharge, Real finalCharge, Real tolerance) {
        return initialCharge.subtract(finalCharge).abs().compareTo(tolerance) < 0;
    }

    /**
     * Conservation of mass-energy (relativistic).
     * E² = (pc)² + (m₀c²)²
     * 
     * Total mass-energy is conserved in all reference frames.
     */
    public static Real totalMassEnergy(Real momentum, Real restMass, Real c) {
        Real pc = momentum.multiply(c);
        Real mc2 = restMass.multiply(c).multiply(c);
        return pc.multiply(pc).add(mc2.multiply(mc2)).sqrt();
    }

    /**
     * Work-energy theorem: W = ΔKE
     * Work done equals change in kinetic energy
     */
    public static Real workEnergyTheorem(Real initialKE, Real finalKE) {
        return finalKE.subtract(initialKE);
    }

    /**
     * Impulse-momentum theorem: J = Δp
     * Impulse equals change in momentum
     */
    public static Real impulseMomentumTheorem(Real initialP, Real finalP) {
        return finalP.subtract(initialP);
    }

    /**
     * Conservation in elastic collision (1D).
     * Returns final velocities [v1', v2']
     */
    public static Real[] elasticCollision1D(Real m1, Real v1, Real m2, Real v2) {
        // Momentum: m1*v1 + m2*v2 = m1*v1' + m2*v2'
        // Energy: ½m1*v1² + ½m2*v2² = ½m1*v1'² + ½m2*v2'²

        Real m1m2 = m1.add(m2);
        Real m1m1 = m1.subtract(m2);
        Real m2m1 = m2.subtract(m1);

        Real v1Prime = m1m1.multiply(v1).add(Real.TWO.multiply(m2).multiply(v2)).divide(m1m2);
        Real v2Prime = m2m1.multiply(v2).add(Real.TWO.multiply(m1).multiply(v1)).divide(m1m2);

        return new Real[] { v1Prime, v2Prime };
    }

    /**
     * Coefficient of restitution: e = (v2' - v1')/(v1 - v2)
     * e = 1: elastic, e = 0: perfectly inelastic
     */
    public static Real coefficientOfRestitution(Real v1, Real v2, Real v1Prime, Real v2Prime) {
        return v2Prime.subtract(v1Prime).divide(v1.subtract(v2)).abs();
    }
}
