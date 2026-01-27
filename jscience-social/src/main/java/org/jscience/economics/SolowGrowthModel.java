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

package org.jscience.economics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Analytical model of the Solow-Swan long-run economic growth theory. 
 * It models the production of an economy using a Cobb-Douglas production 
 * function Y = A * K^alpha * L^(1-alpha) and simulates capital accumulation 
 * over time.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class SolowGrowthModel {

    private SolowGrowthModel() {}

    /**
     * Snapshot of the economic variables at a specific time step in the 
     * growth simulation.
     */
    public record GrowthState(
        int year,
        Real capital,
        Real labor,
        Real output,
        Real consumption,
        Real investment,
        Real capitalPerWorker,
        Real outputPerWorker
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Simulates the temporal evolution of an economy following Solow growth dynamics.
     * 
     * @param initialK initial capital stock (K)
     * @param initialL initial labor force (L)
     * @param a initial Total Factor Productivity (TFP)
     * @param alpha capital share of income (typically 0.3 to 0.4)
     * @param s savings rate (0.0 to 1.0)
     * @param d capital depreciation rate (0.0 to 1.0)
     * @param n labor force growth rate
     * @param g technological progress growth rate
     * @param periods number of time steps to simulate
     * @return a history of GrowthState snapshots
     */
    public static List<GrowthState> simulate(
            Real initialK, Real initialL, Real a, Real alpha,
            Real s, Real d, Real n, Real g, int periods) {
        
        List<GrowthState> history = new ArrayList<>();
        Real k = initialK;
        Real l = initialL;
        Real tfp = a;
        Real oneMinusAlpha = Real.ONE.subtract(alpha);

        for (int t = 0; t < periods; t++) {
            // Y = tfp * K^alpha * L^(1-alpha)
            Real y = tfp.multiply(k.pow(alpha)).multiply(l.pow(oneMinusAlpha));
            Real i = s.multiply(y);
            Real c = y.subtract(i);
            
            Real kPerW = l.isZero() ? Real.ZERO : k.divide(l);
            Real yPerW = l.isZero() ? Real.ZERO : y.divide(l);
            
            history.add(new GrowthState(t, k, l, y, c, i, kPerW, yPerW));

            // Accumulation equations
            // k = k + i - d * k
            k = k.add(i).subtract(d.multiply(k));
            // l = l * (1 + n)
            l = l.multiply(Real.ONE.add(n));
            // tfp = tfp * (1 + g)
            tfp = tfp.multiply(Real.ONE.add(g));
        }
        return history;
    }

    /**
     * Calculates the Golden Rule steady-state level of capital per worker.
     * Formula: k* = (sA / (n + d + g))^(1 / (1 - alpha))
     * 
     * @param a total factor productivity
     * @param alpha capital share
     * @param s savings rate
     * @param d depreciation rate
     * @param n population growth rate
     * @param g TFP growth rate
     * @return steady state capital per worker as a Real
     */
    public static Real calculateSteadyStateCapital(Real a, Real alpha, Real s, 
            Real d, Real n, Real g) {
        Real power = Real.ONE.divide(Real.ONE.subtract(alpha));
        Real denominator = n.add(d).add(g);
        if (denominator.isZero()) return Real.ZERO;
        
        // k* = (sA / (n + d + g))^(1 / (1 - alpha))
        return s.multiply(a).divide(denominator).pow(power);
    }

    /**
     * Calculates the rate at which the economy converges toward its steady state.
     * Formula: lambda = (1 - alpha)(n + d + g)
     * 
     * @param alpha capital share
     * @param n population growth
     * @param d depreciation
     * @param g technology growth
     * @return the convergence rate value
     */
    public static Real convergenceSpeed(Real alpha, Real n, Real d, Real g) {
        return Real.ONE.subtract(alpha).multiply(n.add(d).add(g));
    }
}
