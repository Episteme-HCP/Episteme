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
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
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
