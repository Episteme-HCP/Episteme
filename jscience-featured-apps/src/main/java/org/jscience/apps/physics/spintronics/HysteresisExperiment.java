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

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.chart.XYChart;

/**
 * Automates hysteresis loop measurements.
 */
public class HysteresisExperiment {

    public static List<XYChart.Series<Number, Number>> runSweep(SpinValve valve, double hMin, double hMax,
            double step) {
        XYChart.Series<Number, Number> trace = new XYChart.Series<>();
        trace.setName("Trace (H+ " + hMin + "->" + hMax + ")");

        XYChart.Series<Number, Number> retrace = new XYChart.Series<>();
        retrace.setName("Retrace (H- " + hMax + "->" + hMin + ")");

        // Setup temporary valve model for simulation
        // We use a simplified Stoner-Wohlfarth model for hysteresis here
        // or just solve LLG equilibrium at each step

        // Forward Sweep
        for (double h = hMin; h <= hMax; h += step) {
            Real r = solveEquilibriumResistance(valve, h);
            trace.getData().add(new XYChart.Data<>(h, r.doubleValue() * 1e18));
        }

        // Backward Sweep
        for (double h = hMax; h >= hMin; h -= step) {
            Real r = solveEquilibriumResistance(valve, h);
            retrace.getData().add(new XYChart.Data<>(h, r.doubleValue() * 1e18));
        }

        List<XYChart.Series<Number, Number>> result = new ArrayList<>();
        result.add(trace);
        result.add(retrace);
        return result;
    }

    private static Real solveEquilibriumResistance(SpinValve valve, double hExt) {
        // Simplified energy minimization: E = -M.H_ext - K sin^2(theta)
        // Here we just use a simple sigmoid-like switch for demonstration if physics
        // engine is not fully relaxed
        // Ideally we run stepLLG until convergence at each H point.

        // Let's implement a quick relaxation using LLG
        FerromagneticLayer free = valve.getFreeLayer();
        Real[] hTotal = { Real.of(hExt), Real.ZERO, Real.ZERO }; // Field along X

        // Relax for 500 steps (approx 0.5 ns)
        for (int i = 0; i < 500; i++) {
            // High damping for fast convergence
            Real[] m = SpinTransport.stepLLG(free, hTotal, Real.of(1e-12), Real.of(0.5), Real.of(1.76e11));
            free.setMagnetization(m[0], m[1], m[2]);
        }

        return GMREffect.valetFertResistance(valve);
    }
}
