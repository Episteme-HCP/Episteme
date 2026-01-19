package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Predicts material fatigue life under cyclic loads.
 */
public final class MaterialFatigueCalculator {

    private MaterialFatigueCalculator() {}

    /**
     * Basquin's Equation: S = a * N^b
     * Returns number of cycles to failure.
     */
    public static Real cyclesToFailure(double stressAmplitude, double a, double b) {
        return Real.of(Math.pow(stressAmplitude / a, 1.0 / b));
    }

    /**
     * Palmgren-Miner Linear Damage rule.
     * Sum(ni/Ni) = 1.0
     */
    public static Real cumulativeDamage(double[] cyclesAtStress, double[] capacityAtStress) {
        double damage = 0;
        for (int i = 0; i < cyclesAtStress.length; i++) {
            damage += cyclesAtStress[i] / capacityAtStress[i];
        }
        return Real.of(damage);
    }
}
