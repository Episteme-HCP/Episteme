/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;
import javafx.scene.chart.XYChart;

/**
 * SOT Switching Phase Diagram calculator.
 * Determines the critical current and field combinations for magnetization reversal.
 */
public class SOTSwitchingDiagram {

    /**
     * Calculates the switching phase diagram J_c vs H_ext.
     * 
     * @param sot SOT layer stack
     * @param layer Ferromagnetic layer
     * @param hMin Minimum external field (A/m)
     * @param hMax Maximum external field (A/m)
     * @param hSteps Number of field steps
     * @param jMax Maximum current density to test (A/m²)
     * @param jSteps Number of current steps
     * @return XYChart series with (H_ext, J_c) data points for switching boundary
     */
    public static XYChart.Series<Number, Number> calculatePhaseDiagram(
            SpinOrbitTorque sot,
            FerromagneticLayer layer,
            double hMin, double hMax, int hSteps,
            double jMax, int jSteps) {
        
        XYChart.Series<Number, Number> switchingBoundary = new XYChart.Series<>();
        switchingBoundary.setName("Switching Boundary");
        
        double hStep = (hMax - hMin) / hSteps;
        double jStep = jMax / jSteps;
        
        Real[] currentDir = {Real.ONE, Real.ZERO, Real.ZERO}; // Current along X
        Real alpha = Real.of(0.01);
        Real gamma = Real.of(1.76e11);
        Real dt = Real.of(1e-12);
        
        for (double h = hMin; h <= hMax; h += hStep) {
            // Reset layer to initial state (pointing up)
            layer.setMagnetization(Real.ZERO, Real.ZERO, Real.ONE);
            
            double jCritical = 0;
            
            // Sweep current density to find switching threshold
            for (double j = 0; j <= jMax; j += jStep) {
                // Simulate for 5 ns
                boolean switched = simulateSwitching(sot, layer, Real.of(j), Real.of(h), 
                        currentDir, alpha, gamma, dt, 5000);
                
                if (switched) {
                    jCritical = j;
                    break;
                }
            }
            
            if (jCritical > 0) {
                switchingBoundary.getData().add(new XYChart.Data<>(h / 1e3, jCritical / 1e10)); // kA/m, 10^10 A/m²
            }
        }
        
        return switchingBoundary;
    }
    
    private static boolean simulateSwitching(SpinOrbitTorque sot, FerromagneticLayer layer,
            Real j, Real hExt, Real[] currentDir, Real alpha, Real gamma, Real dt, int steps) {
        
        Real initialMz = layer.getMagnetization()[2];
        
        for (int i = 0; i < steps; i++) {
            // Effective field: external + anisotropy (simplified)
            Real[] hEff = {Real.ZERO, Real.ZERO, hExt};
            
            // Add SOT
            Real[] tauDL = sot.calculateDampingLikeTorque(j, layer, currentDir);
            Real[] tauFL = sot.calculateFieldLikeTorque(j, layer, currentDir);
            
            // Convert torque to pseudo-field
            Real ms = layer.getMaterial().getSaturationMagnetization();
            for (int d = 0; d < 3; d++) {
                hEff[d] = hEff[d].add(tauDL[d].divide(ms)).add(tauFL[d].divide(ms));
            }
            
            // LLG step
            Real[] newM = SpinTransport.stepLLG(layer, hEff, dt, alpha, gamma);
            layer.setMagnetization(newM[0], newM[1], newM[2]);
        }
        
        // Check if Mz flipped sign
        Real finalMz = layer.getMagnetization()[2];
        return initialMz.multiply(finalMz).compareTo(Real.ZERO) < 0;
    }
}
