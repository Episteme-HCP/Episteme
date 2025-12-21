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
package org.jscience.biology.ecology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Epidemiology models.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Epidemiology {

    /**
     * SIR (Susceptible-Infectious-Recovered) model simulation.
     * dS/dt = -beta * S * I / N
     * dI/dt = beta * S * I / N - gamma * I
     * dR/dt = gamma * I
     * 
     * @param S0    Initial susceptible
     * @param I0    Initial infectious
     * @param R0    Initial recovered
     * @param beta  Transmission rate
     * @param gamma Recovery rate
     * @param dt    Time step
     * @param steps Number of steps
     * @return Real[][] with columns [S, I, R] and rows = steps
     */
    public static Real[][] sirModel(Real S0, Real I0, Real R0,
            Real beta, Real gamma,
            Real dt, int steps) {
        Real[][] result = new Real[steps][3];

        Real S = S0;
        Real I = I0;
        Real R = R0;
        Real N = S0.add(I0).add(R0);

        for (int i = 0; i < steps; i++) {
            result[i][0] = S;
            result[i][1] = I;
            result[i][2] = R;

            Real infection = beta.multiply(S).multiply(I).divide(N);
            Real dS = infection.negate().multiply(dt);
            Real dI = infection.subtract(gamma.multiply(I)).multiply(dt);
            Real dR = gamma.multiply(I).multiply(dt);

            S = S.add(dS);
            I = I.add(dI);
            R = R.add(dR);
        }

        return result;
    }

    /**
     * Basic reproduction number (R0).
     * R0 = beta / gamma
     */
    public static Real basicReproductionNumber(Real beta, Real gamma) {
        return beta.divide(gamma);
    }

    /**
     * Herd immunity threshold.
     * HIT = 1 - 1/R0
     */
    public static Real herdImmunityThreshold(Real r0) {
        if (r0.compareTo(Real.ONE) <= 0)
            return Real.ZERO;
        return Real.ONE.subtract(Real.ONE.divide(r0));
    }
}
