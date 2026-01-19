package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Simulates crowd dynamics and density for evacuation.
 */
public final class CrowdDensitySimulator {

    private CrowdDensitySimulator() {}

    /**
     * Calculates flow through an exit.
     * Flow = density * velocity * width
     */
    public static Real exitFlow(double density, double width) {
        double velocity = 1.2 * (1 - 0.25 * density); // Simplified speed reduction
        return Real.of(density * Math.max(0.1, velocity) * width);
    }
}
