package org.jscience.law;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Analyzes formal justice and equity in resource distribution (Aristotelian Justice).
 */
public final class FormalJusticeAnalyzer {

    private FormalJusticeAnalyzer() {}

    /**
     * Distributive Justice (Proportional): 
     * Shares should be proportional to merit/contribution.
     * Ratio = Share / Contribution
     */
    public static Real distributiveEquityIndex(double[] contributions, double[] shares) {
        if (contributions.length != shares.length || contributions.length == 0) return Real.ZERO;
        
        double[] ratios = new double[contributions.length];
        double sumRatios = 0;
        for (int i = 0; i < contributions.length; i++) {
            ratios[i] = shares[i] / contributions[i];
            sumRatios += ratios[i];
        }
        
        double avg = sumRatios / contributions.length;
        double variance = 0;
        for (double r : ratios) variance += Math.pow(r - avg, 2);
        
        // Return 1.0 - normalized variance (1.0 = perfect justice)
        return Real.of(1.0 / (1.0 + Math.sqrt(variance)));
    }

    /**
     * Corrective Justice: Compares harm with compensation.
     */
    public static Real compensationAdequacy(double harmValue, double compensation) {
        if (harmValue == 0) return Real.ONE;
        return Real.of(compensation / harmValue);
    }
}
