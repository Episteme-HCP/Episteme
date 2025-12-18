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
 * Hamiltonian mechanics - phase space formulation of classical mechanics.
 * <p>
 * Hamiltonian: H = Σpᵢq̇ᵢ - L
 * Hamilton's equations: q̇ = ∂H/∂p, ṗ = -∂H/∂q
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class HamiltonianMechanics {

    /**
     * Hamiltonian for free particle: H = p²/(2m)
     */
    public static Real hamiltonianFreeParticle(Real momentum, Real mass) {
        return momentum.multiply(momentum).divide(Real.TWO.multiply(mass));
    }

    /**
     * Hamiltonian: H = T + V (total energy in conservative system)
     */
    public static Real hamiltonian(Real kineticEnergy, Real potentialEnergy) {
        return kineticEnergy.add(potentialEnergy);
    }

    /**
     * Hamiltonian from Lagrangian: H = pq̇ - L
     */
    public static Real hamiltonianFromLagrangian(Real momentum, Real velocity, Real lagrangian) {
        return momentum.multiply(velocity).subtract(lagrangian);
    }

    /**
     * Hamiltonian for harmonic oscillator: H = p²/(2m) + ½kx²
     */
    public static Real hamiltonianHarmonicOscillator(Real momentum, Real mass,
            Real springConstant, Real position) {
        Real kinetic = momentum.multiply(momentum).divide(Real.TWO.multiply(mass));
        Real potential = Real.of(0.5).multiply(springConstant).multiply(position).multiply(position);
        return kinetic.add(potential);
    }

    /**
     * Poisson bracket: {f,g} = Σᵢ(∂f/∂qᵢ ∂g/∂pᵢ - ∂f/∂pᵢ ∂g/∂qᵢ)
     * Simplified 1D version
     */
    public static Real poissonBracket1D(Real dfDq, Real dfDp, Real dgDq, Real dgDp) {
        return dfDq.multiply(dgDp).subtract(dfDp.multiply(dgDq));
    }

    /**
     * Phase space volume element: dΓ = Πᵢ dqᵢ dpᵢ
     * (Preserved by Hamiltonian flow - Liouville's theorem)
     */
    public static Real phaseSpaceVolume(Real[] q, Real[] p) {
        // Simplified: just multiply intervals
        Real volume = Real.ONE;
        for (int i = 0; i < q.length; i++) {
            volume = volume.multiply(q[i]).multiply(p[i]);
        }
        return volume;
    }

    /**
     * Hamiltonian for charged particle in EM field:
     * H = (p - qA)²/(2m) + qφ
     * (Simplified scalar version)
     */
    public static Real hamiltonianEMField(Real momentum, Real charge, Real vectorPotential,
            Real mass, Real scalarPotential) {
        Real effectiveP = momentum.subtract(charge.multiply(vectorPotential));
        Real kinetic = effectiveP.multiply(effectiveP).divide(Real.TWO.multiply(mass));
        Real potential = charge.multiply(scalarPotential);
        return kinetic.add(potential);
    }
}
