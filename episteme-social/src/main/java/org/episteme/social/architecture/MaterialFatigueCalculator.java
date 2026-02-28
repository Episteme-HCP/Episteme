/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.architecture;

import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Predicts material fatigue life and cumulative damage under cyclic loading 
 * conditions. It implements Basquin's Equation and the Palmgren-Miner linear 
 * damage accumulation rule.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class MaterialFatigueCalculator {

    private MaterialFatigueCalculator() {}

    /**
     * Estimates the number of cycles to failure (N) using Basquin's Equation.
     * Formula: S_amp = a * (N^b) => N = (S_amp / a)^(1/b)
     * 
     * @param stressAmplitude the amplitude of the cyclic stress
     * @param a material constant (intercept)
     * @param b material constant (fatigue exponent, usually negative)
     * @return number of cycles until failure as a Real value
     */
    public static Real cyclesToFailure(double stressAmplitude, double a, double b) {
        if (b == 0) return Real.ZERO;
        return Real.of(Math.pow(stressAmplitude / a, 1.0 / b));
    }

    /**
     * Calculates cumulative structural damage using the Palmgren-Miner rule.
     * Failure is predicted when the damage sum reaches or exceeds 1.0.
     * Formula: Sum(ni / Ni)
     * 
     * @param cyclesAtStress array of actual applied cycles (ni) at different stress levels
     * @param capacityAtStress array of corresponding failure capacities (Ni) at those levels
     * @return cumulative damage fraction (0.0 to 1.0+)
     */
    public static Real cumulativeDamage(double[] cyclesAtStress, double[] capacityAtStress) {
        double damage = 0;
        int len = Math.min(cyclesAtStress.length, capacityAtStress.length);
        for (int i = 0; i < len; i++) {
            if (capacityAtStress[i] != 0) {
                damage += cyclesAtStress[i] / capacityAtStress[i];
            }
        }
        return Real.of(damage);
    }
}

