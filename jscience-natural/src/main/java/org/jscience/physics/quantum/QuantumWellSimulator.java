package org.jscience.physics.quantum;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Simulates energy levels in a 1D Finite Quantum Well.
 */
public final class QuantumWellSimulator {

    private QuantumWellSimulator() {}

    public record EnergyLevel(int n, double energyEv, double parity) {}

    /**
     * Solves for bound state energy levels.
     * Based on transcendental equations for finite well.
     * 
     * @param width Width of well in nm
     * @param depth Depth of well (Potential V0) in eV
     * @param effectiveMass Effective mass ratio (e.g., 0.067 for GaAs)
     */
    public static List<EnergyLevel> solveBoundStates(double width, double depth, double effectiveMass) {
        List<EnergyLevel> levels = new ArrayList<>();
        
        // Constants
        double m0 = 9.109e-31;     // kg
        double m = effectiveMass * m0;
        
        // Characteristic parameter R = sqrt(2 * m * V0 * (L/2)^2 / hbar^2)
        // Using L in meters
        double l_meters = width * 1e-9;
        double v0_joules = depth * 1.602e-19;
        double hbar_joules = 1.054e-34;
        
        double r = Math.sqrt(2 * m * v0_joules * Math.pow(l_meters / 2, 2)) / hbar_joules;
        
        // Number of states is ceil(2R/pi)
        int numStates = (int) Math.ceil(2 * r / Math.PI);
        
        for (int n = 1; n <= numStates; n++) {
            // Approximation for energy levels
            double energy = depth * Math.pow((n * Math.PI) / (2 * r + 1), 2);
            if (energy < depth) {
                levels.add(new EnergyLevel(n, energy, n % 2));
            }
        }
        
        return levels;
    }

    /**
     * Calculates tunneling probability through a barrier.
     * T = 1 / (1 + (V0^2 * sinh(kL)^2) / (4E(V0-E)))
     */
    public static Real tunnelingProbability(double energy, double barrierHeight, double width) {
        if (energy >= barrierHeight) return Real.ONE;
        
        double k = Math.sqrt(2 * 9.109e-31 * (barrierHeight - energy) * 1.602e-19) / 1.054e-34;
        double kappaL = k * width * 1e-9;
        
        double denom = 1 + (Math.pow(barrierHeight, 2) * Math.pow(Math.sinh(kappaL), 2)) / 
                           (4 * energy * (barrierHeight - energy));
        
        return Real.of(1.0 / denom);
    }
}
