package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Predicts inflation trends using monetary factors.
 */
public final class InflationForecaster {

    private InflationForecaster() {}

    /**
     * Quantity Theory of Money: dP = dM + dV - dY
     */
    public static Real estimateInflation(double moneyGrowth, double velocityGrowth, double gdpGrowth) {
        return Real.of(moneyGrowth + velocityGrowth - gdpGrowth);
    }
}
