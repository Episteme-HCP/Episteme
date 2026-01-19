package org.jscience.economics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Valuates real estate using DCF and comparables.
 */
public final class RealEstateValuation {

    private RealEstateValuation() {}

    /**
     * Capitalization Rate formula: Value = NOI / CapRate
     */
    public static Real valuateByCapRate(double netOperatingIncome, double capRate) {
        return Real.of(netOperatingIncome / capRate);
    }
}
