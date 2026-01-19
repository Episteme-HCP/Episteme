package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Simplified seismic analysis simulator.
 */
public final class EarthquakeSimulator {

    private EarthquakeSimulator() {}

    /**
     * Calculates the Base Shear using simplified equivalent lateral force procedure.
     * V = Cs * W
     */
    public static Real calculateBaseShear(double buildingWeight, double responseCoeff) {
        return Real.of(buildingWeight * responseCoeff);
    }

    /**
     * Estimates fundamental period of a building.
     * Ta = Ct * h^n
     */
    public static Real estimatePeriod(double height, boolean isSteel) {
        double ct = isSteel ? 0.0724 : 0.0466;
        double n = isSteel ? 0.8 : 0.9;
        return Real.of(ct * Math.pow(height, n));
    }
}
