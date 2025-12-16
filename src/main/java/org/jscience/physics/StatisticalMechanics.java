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
import org.jscience.physics.PhysicalConstants;

/**
 * Statistical mechanics - bridge between microscopic and macroscopic physics.
 * <p>
 * Boltzmann distribution, partition functions, entropy from microstates.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class StatisticalMechanics {

    /**
     * Boltzmann distribution: P(E) = e^(-E/kT) / Z
     * Returns probability of state with energy E
     */
    /**
     * Boltzmann distribution: P(E) = e^(-E/kT) / Z
     * Returns probability of state with energy E
     */
    public static Real boltzmannProbability(Real energy, Real temperature, Real partitionFunction) {
        Real kT = PhysicalConstants.BOLTZMANN_CONSTANT.getValue().multiply(temperature);
        Real exponent = energy.divide(kT).negate();
        return Real.of(Math.exp(exponent.doubleValue())).divide(partitionFunction);
    }

    /**
     * Partition function (canonical ensemble): Z = Σᵢ e^(-Eᵢ/kT)
     */
    public static Real partitionFunction(Real[] energies, Real temperature) {
        Real kT = PhysicalConstants.BOLTZMANN_CONSTANT.getValue().multiply(temperature);
        Real Z = Real.ZERO;

        for (Real energy : energies) {
            Real exponent = energy.divide(kT).negate();
            Z = Z.add(Real.of(Math.exp(exponent.doubleValue())));
        }

        return Z;
    }

    /**
     * Boltzmann entropy: S = k ln(Ω)
     * where Ω is number of microstates
     */
    public static Real boltzmannEntropy(Real numMicrostates) {
        return PhysicalConstants.BOLTZMANN_CONSTANT.getValue()
                .multiply(Real.of(Math.log(numMicrostates.doubleValue())));
    }

    /**
     * Maxwell-Boltzmann speed distribution: f(v) = 4π(m/2πkT)^(3/2) v² e^(-mv²/2kT)
     */
    public static Real maxwellBoltzmannSpeed(Real speed, Real mass, Real temperature) {
        Real k = PhysicalConstants.BOLTZMANN_CONSTANT.getValue();

        double m = mass.doubleValue();
        double v = speed.doubleValue();
        double T = temperature.doubleValue();
        double kVal = k.doubleValue();

        double prefactor = 4 * Math.PI * Math.pow(m / (2 * Math.PI * kVal * T), 1.5);
        double exponent = -m * v * v / (2 * kVal * T);

        return Real.of(prefactor * v * v * Math.exp(exponent));
    }

    /**
     * Average energy from partition function: <E> = -∂ln(Z)/∂β where β = 1/kT
     */
    public static Real averageEnergy(Real partitionFunction, Real temperature, Real dZdT) {
        Real kT = PhysicalConstants.BOLTZMANN_CONSTANT.getValue().multiply(temperature);
        Real kT2 = kT.multiply(kT);
        return kT2.multiply(dZdT).divide(partitionFunction);
    }

    /**
     * Heat capacity: C = ∂<E>/∂T
     */
    public static Real heatCapacity(Real dEnergyDT) {
        return dEnergyDT;
    }

    /**
     * Fermi-Dirac distribution: f(E) = 1/(e^((E-μ)/kT) + 1)
     * For fermions (electrons, etc.)
     */
    public static Real fermiDiracDistribution(Real energy, Real chemicalPotential, Real temperature) {
        Real k = PhysicalConstants.BOLTZMANN_CONSTANT.getValue();
        Real exponent = energy.subtract(chemicalPotential).divide(k.multiply(temperature));
        return Real.ONE.divide(Real.ONE.add(Real.of(Math.exp(exponent.doubleValue()))));
    }

    /**
     * Bose-Einstein distribution: f(E) = 1/(e^((E-μ)/kT) - 1)
     * For bosons (photons, etc.)
     */
    public static Real boseEinsteinDistribution(Real energy, Real chemicalPotential, Real temperature) {
        Real k = PhysicalConstants.BOLTZMANN_CONSTANT.getValue();
        Real exponent = energy.subtract(chemicalPotential).divide(k.multiply(temperature));
        return Real.ONE.divide(Real.of(Math.exp(exponent.doubleValue())).subtract(Real.ONE));
    }

    /**
     * Gibbs free energy: G = H - TS = U + PV - TS
     */
    public static Real gibbsFreeEnergy(Real enthalpy, Real temperature, Real entropy) {
        return enthalpy.subtract(temperature.multiply(entropy));
    }

    /**
     * Helmholtz free energy: F = U - TS
     */
    public static Real helmholtzFreeEnergy(Real internalEnergy, Real temperature, Real entropy) {
        return internalEnergy.subtract(temperature.multiply(entropy));
    }
}
