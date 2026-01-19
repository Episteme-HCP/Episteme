package org.jscience.history;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Analyzes historical coins and their metal content.
 */
public final class NumismaticAnalyzer {

    private NumismaticAnalyzer() {}

    public record Coin(String id, double weightGrams, double purityRatio) {}

    /**
     * Calculates the intrinsic value of a coin based on metal price.
     */
    public static Real calculateIntrinsicValue(Coin coin, double metalPricePerGram) {
        return Real.of(coin.weightGrams() * coin.purityRatio() * metalPricePerGram);
    }

    /**
     * Gresham's Law simulator: "Bad money drives out good".
     * Estimates likelihood of hoarding based on difference in intrinsic value vs face value.
     */
    public static double hoardingProbability(double intrinsic1, double face1, 
                                             double intrinsic2, double face2) {
        // If (intrinsic1/face1) > (intrinsic2/face2), money 1 is "good"
        double ratio1 = intrinsic1 / face1;
        double ratio2 = intrinsic2 / face2;
        
        if (ratio1 > ratio2) return Math.min(1.0, (ratio1 - ratio2) * 5.0);
        return 0.0;
    }
}
