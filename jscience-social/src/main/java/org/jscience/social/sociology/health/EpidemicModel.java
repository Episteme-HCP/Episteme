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

package org.jscience.social.sociology.health;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Implements epidemiological models such as SIR (Susceptible-Infectious-Recovered)
 * to simulate disease spread within a social group or network.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EpidemicModel {

    private EpidemicModel() {
        // Utility class
    }

    /**
     * Calculates the basic reproduction number (R0) given transmission rate and recovery rate.
     * R0 = beta / gamma
     * 
     * @param transmissionRate (Beta) The average number of contacts sufficient for transmission per unit time.
     * @param recoveryRate (Gamma) The rate at which infectious individuals recover (1/duration).
     * @return R0
     */
    public static Real calculateR0(Real transmissionRate, Real recoveryRate) {
        if (recoveryRate.equals(Real.ZERO)) {
            // Infinite spread if never recover
            return Real.of(Double.POSITIVE_INFINITY); 
        }
        return transmissionRate.divide(recoveryRate);
    }

    /**
     * Simulation step for a basic deterministic SIR model.
     * dS/dt = -beta * S * I / N
     * dI/dt = beta * S * I / N - gamma * I
     * dR/dt = gamma * I
     *
     * @param S Susceptible count
     * @param I Infectious count
     * @param R Recovered count
     * @param N Total population
     * @param beta Transmission rate
     * @param gamma Recovery rate
     * @param dt Time step
     * @return Array of new [S, I, R] values
     */
    public static Real[] stepSIR(Real S, Real I, Real R, Real N, Real beta, Real gamma, Real dt) {
        if (N.equals(Real.ZERO)) return new Real[]{S, I, R};

        // dS = -beta * S * I / N * dt
        Real dS = beta.negate().multiply(S).multiply(I).divide(N).multiply(dt);
        
        // dR = gamma * I * dt
        Real dR = gamma.multiply(I).multiply(dt);
        
        // dI = -dS - dR (Conservation)
        // dI = (beta * S * I / N - gamma * I) * dt
        Real dI = beta.multiply(S).multiply(I).divide(N).subtract(gamma.multiply(I)).multiply(dt);
        
        return new Real[] { S.add(dS), I.add(dI), R.add(dR) };
    }
}
