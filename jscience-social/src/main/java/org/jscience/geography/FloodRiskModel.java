package org.jscience.geography;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Identifies areas at risk of flooding.
 */
public final class FloodRiskModel {

    private FloodRiskModel() {}

    /**
     * Probability of flood based on elevation and rainfall.
     */
    public static Real riskFactor(double elevation, double rainfallRate, double soilSaturation) {
        double risk = (rainfallRate * soilSaturation) / (elevation + 1.0);
        return Real.of(Math.min(1.0, risk));
    }
}
