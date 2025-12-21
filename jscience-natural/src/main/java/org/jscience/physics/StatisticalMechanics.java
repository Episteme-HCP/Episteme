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
package org.jscience.physics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;

/**
 * Statistical mechanics - bridge between microscopic and macroscopic physics.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StatisticalMechanics {

    /** Boltzmann distribution: P(E) = e^(-E/kT) / Z */
    public static Real boltzmannProbability(Real energy, Real temperature, Real partitionFunction) {
        Real kT = PhysicalConstants.k_B.multiply(temperature);
        return energy.divide(kT).negate().exp().divide(partitionFunction);
    }

    /** Exponent for Maxwell-Boltzmann prefactor: 3/2 */
    private static final Real THREE_HALVES = Real.of(3).divide(Real.TWO);

    /** Partition function: Z = Σᵢ e^(-Eᵢ/kT) */
    public static Real partitionFunction(Vector<Real> energies, Real temperature) {
        Real kT = PhysicalConstants.k_B.multiply(temperature);
        Real Z = Real.ZERO;
        for (int i = 0; i < energies.dimension(); i++) {
            Real energy = energies.get(i);
            Z = Z.add(energy.divide(kT).negate().exp());
        }
        return Z;
    }

    /** Boltzmann entropy: S = k ln(Ω) */
    public static Real boltzmannEntropy(Real numMicrostates) {
        return PhysicalConstants.k_B.multiply(numMicrostates.log());
    }

    /** Maxwell-Boltzmann speed distribution */
    public static Real maxwellBoltzmannSpeed(Real speed, Real mass, Real temperature) {
        Real k = PhysicalConstants.k_B;
        Real kT = k.multiply(temperature);
        Real m_2kT = mass.divide(Real.TWO.multiply(Real.PI).multiply(kT));
        Real prefactor = Real.of(4).multiply(Real.PI).multiply(m_2kT.pow(THREE_HALVES));
        Real exponent = mass.multiply(speed.pow(2)).divide(Real.TWO.multiply(kT)).negate();
        return prefactor.multiply(speed.pow(2)).multiply(exponent.exp());
    }

    /** Average energy from partition function */
    public static Real averageEnergy(Real partitionFunction, Real temperature, Real dZdT) {
        Real kT = PhysicalConstants.k_B.multiply(temperature);
        return kT.pow(2).multiply(dZdT).divide(partitionFunction);
    }

    /** Heat capacity: C = ∂<E>/∂T */
    public static Real heatCapacity(Real dEnergyDT) {
        return dEnergyDT;
    }

    /** Fermi-Dirac distribution: f(E) = 1/(e^((E-μ)/kT) + 1) */
    public static Real fermiDiracDistribution(Real energy, Real chemicalPotential, Real temperature) {
        Real kT = PhysicalConstants.k_B.multiply(temperature);
        Real exponent = energy.subtract(chemicalPotential).divide(kT);
        return Real.ONE.divide(Real.ONE.add(exponent.exp()));
    }

    /** Bose-Einstein distribution: f(E) = 1/(e^((E-μ)/kT) - 1) */
    public static Real boseEinsteinDistribution(Real energy, Real chemicalPotential, Real temperature) {
        Real kT = PhysicalConstants.k_B.multiply(temperature);
        Real exponent = energy.subtract(chemicalPotential).divide(kT);
        return Real.ONE.divide(exponent.exp().subtract(Real.ONE));
    }

    /** Gibbs free energy: G = H - TS */
    public static Real gibbsFreeEnergy(Real enthalpy, Real temperature, Real entropy) {
        return enthalpy.subtract(temperature.multiply(entropy));
    }

    /** Helmholtz free energy: F = U - TS */
    public static Real helmholtzFreeEnergy(Real internalEnergy, Real temperature, Real entropy) {
        return internalEnergy.subtract(temperature.multiply(entropy));
    }
}