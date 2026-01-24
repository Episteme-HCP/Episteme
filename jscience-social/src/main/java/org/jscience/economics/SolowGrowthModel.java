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
        double capital,
        double labor,
        double output,
        double consumption,
        double investment,
        double capitalPerWorker,
        double outputPerWorker
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
            double initialK, double initialL, double a, double alpha,
            double s, double d, double n, double g, int periods) {
        
        List<GrowthState> history = new ArrayList<>();
        double k = initialK;
        double l = initialL;
        double tfp = a;

        for (int t = 0; t < periods; t++) {
            double y = tfp * Math.pow(k, alpha) * Math.pow(l, 1 - alpha);
            double i = s * y;
            double c = y - i;
            
            history.add(new GrowthState(t, k, l, y, c, i, l != 0 ? k/l : 0, l != 0 ? y/l : 0));

            // Accumulation equations
            k = k + i - d * k;
            l = l * (1 + n);
            tfp = tfp * (1 + g);
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
    public static Real calculateSteadyStateCapital(double a, double alpha, double s, 
            double d, double n, double g) {
        double power = 1.0 / (1.0 - alpha);
        if (n + d + g == 0) return Real.ZERO;
        double kStar = Math.pow((s * a) / (n + d + g), power);
        return Real.of(kStar);
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
    public static Real convergenceSpeed(double alpha, double n, double d, double g) {
        return Real.of((1 - alpha) * (n + d + g));
    }
}
