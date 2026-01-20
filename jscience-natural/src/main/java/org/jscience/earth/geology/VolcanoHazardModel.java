package org.jscience.earth.geology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Advanced Volcanic Hazard Model.
 * Includes Energy Cone models for Pyroclastic Flows and Ash Dispersion.
 */
public final class VolcanoHazardModel {

    private VolcanoHazardModel() {}

    /**
     * Predicts Pyroclastic Flow runout distance using the "Energy Cone" model (H/L ratio).
     * 
     * @param height Vent height (m)
     * @param hlRatio Mobility ratio (H/L), typical values 0.1 to 0.3
     */
    public static Real pyroclasticRunout(double height, double hlRatio) {
        return Real.of(height / hlRatio);
    }

    /**
     * Estimates ash fall thickness using a spatial decay model.
     * T(d) = T0 * exp(-k * d)
     * 
     * @param thicknessAtVent T0 in cm
     * @param distanceKm d in km
     * @param windSpeed m/s
     */
    public static Real ashThickness(double thicknessAtVent, double distanceKm, double windSpeed) {
        // Distortion due to wind (simplified)
        double k = 0.5 / (1.0 + windSpeed * 0.1);
        return Real.of(thicknessAtVent * Math.exp(-k * distanceKm));
    }

    /**
     * Calculates the plume height using the Morton, Taylor and Turner (MTT) model.
     * H = 1.67 * (Q^0.25)
     * 
     * @param massDischargeRate Q (kg/s)
     * @return Plume height in Km
     */
    public static Real estimatePlumeHeight(double massDischargeRate) {
        return Real.of(1.67 * Math.pow(massDischargeRate, 0.25));
    }

    /**
     * Evaluates the risk level based on population density and flow proximity.
     */
    public static double combinedRiskScore(double distToVent, double plumeHeight, double popDensity) {
        double sourceTerm = plumeHeight / distToVent;
        return Math.min(1.0, sourceTerm * popDensity / 1000.0);
    }
}
