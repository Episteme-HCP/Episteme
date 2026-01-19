package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models passive natural ventilation and airflow in buildings.
 */
public final class NaturalVentilation {

    private NaturalVentilation() {}

    /**
     * Calculates Stack Effect airflow.
     * Q = Cd * A * sqrt(2 * g * H * (Ti - To) / Ti)
     * 
     * @param cd Discharge coefficient (usually 0.65)
     * @param area Opening area (m2)
     * @param height Height between openings (m)
     * @param tempInside Kelvin
     * @param tempOutside Kelvin
     * @return Flow rate (m3/s)
     */
    public static Real stackEffectFlow(double cd, double area, double height, 
            double tempInside, double tempOutside) {
        if (tempInside <= tempOutside) return Real.ZERO;
        
        double g = 9.81;
        double flow = cd * area * Math.sqrt(2 * g * height * (tempInside - tempOutside) / tempInside);
        return Real.of(flow);
    }

    /**
     * Calculates Wind-Driven ventilation.
     * Q = Cv * A * v
     * 
     * @param cv Effectiveness (0.5-0.6 for perpendicular, 0.25-0.35 for diagonal)
     * @param area Opening area (m2)
     * @param windSpeed m/s
     */
    public static Real windDrivenFlow(double cv, double area, double windSpeed) {
        return Real.of(cv * area * windSpeed);
    }

    /**
     * Estimates Air Changes per Hour (ACH).
     * ACH = (Q * 3600) / Volume
     */
    public static Real calculateACH(double totalFlowRate, double buildingVolume) {
        return Real.of((totalFlowRate * 3600) / buildingVolume);
    }

    /**
     * Determines if Cross Ventilation is possible.
     */
    public static boolean isCrossVentilationPossible(double buildingDepth, double windowRatio) {
        // Rule of thumb: depth < 5 * height or depth < 15m for single side, etc.
        return buildingDepth < 12.0 && windowRatio > 0.05;
    }
}
