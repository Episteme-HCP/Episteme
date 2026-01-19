package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Designer for natural and mechanical ventilation.
 */
public final class VentilationCalculator {

    private VentilationCalculator() {}

    /**
     * Calculates required Air Changes per Hour (ACH).
     */
    public static Real requiredACH(int occupancy, double volumePerPerson, double roomVolume) {
        double flowRateNeeded = occupancy * 30; // 30 m3/h per person
        return Real.of(flowRateNeeded / roomVolume);
    }

    /**
     * Calculates Airflow from stack effect.
     * Q = Cd * A * sqrt(2 * g * h * (Ti - Te) / Ti)
     */
    public static Real stackEffectFlow(double area, double height, double tempIn, double tempOut) {
        double g = 9.81;
        double flow = 0.65 * area * Math.sqrt(2 * g * height * (tempIn - tempOut) / (tempIn + 273));
        return Real.of(flow);
    }
}
