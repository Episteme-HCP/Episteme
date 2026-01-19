package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models labor market dynamics (unemployment, NAIRU).
 */
public final class LaborMarketModel {

    private LaborMarketModel() {}

    /**
     * Okun's Law: dU = -0.5 * (dGDP - 3)
     */
    public static Real okunDeltaUnemployment(double gdpGrowth) {
        return Real.of(-0.5 * (gdpGrowth - 3.0));
    }
}
