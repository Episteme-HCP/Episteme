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
package org.jscience.physics.statistical;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Statistical distribution functions for particles.
 * <p>
 * Provides implementations for:
 * <ul>
 * <li>Maxwell-Boltzmann (classical)</li>
 * <li>Fermi-Dirac (fermions)</li>
 * <li>Bose-Einstein (bosons)</li>
 * </ul>
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StatisticalDistributions {

    // Boltzmann constant k_B in J/K
    public static final Real K_B = Real.of(1.380649e-23);

    /**
     * Maxwell-Boltzmann distribution: $f(E) = A \exp(-E / k_B T)$
     * Returns UNNORMALIZED probability.
     */
    public static Real maxwellBoltzmann(Real energy, Real temperature) {
        Real exponent = energy.negate().divide(K_B.multiply(temperature));
        return exponent.exp();
    }

    /**
     * Fermi-Dirac distribution: $f(E) = 1 / (\exp((E - \mu) / k_B T) + 1)$
     * Chemical potential $\mu$ is often the Fermi energy at T=0.
     */
    public static Real fermiDirac(Real energy, Real chemicalPotential, Real temperature) {
        Real exponent = energy.subtract(chemicalPotential).divide(K_B.multiply(temperature));
        return Real.ONE.divide(exponent.exp().add(Real.ONE));
    }

    /**
     * Bose-Einstein distribution: $f(E) = 1 / (\exp((E - \mu) / k_B T) - 1)$
     * For photons/phonons, $\mu = 0$.
     */
    public static Real boseEinstein(Real energy, Real chemicalPotential, Real temperature) {
        Real exponent = energy.subtract(chemicalPotential).divide(K_B.multiply(temperature));
        return Real.ONE.divide(exponent.exp().subtract(Real.ONE));
    }

    /**
     * Partition function for a system with discrete energy levels.
     * $Z = \sum_i \exp(-E_i / k_B T)$
     */
    public static Real partitionFunction(Real[] energyLevels, Real temperature) {
        Real sum = Real.ZERO;
        for (Real e : energyLevels) {
            sum = sum.add(maxwellBoltzmann(e, temperature));
        }
        return sum;
    }
}
