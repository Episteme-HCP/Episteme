/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

        FerromagneticLayer free = valve.getFreeLayer();
        Real originalAngle = Real.of(free.getMagnetizationAngle()); // Assuming 2D for initial MVP state restore

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
