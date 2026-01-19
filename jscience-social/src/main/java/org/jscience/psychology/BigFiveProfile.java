package org.jscience.psychology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models the Big Five personality traits (OCEAN).
 */
public final class BigFiveProfile {

    private BigFiveProfile() {}

    public record OCEAN(
        double openness,
        double conscientiousness,
        double extraversion,
        double agreeableness,
        double neuroticism
    ) {}

    /**
     * Standardizes a raw score into a T-score (Mean=50, SD=10).
     */
    public static Real toTScore(double raw, double mean, double sd) {
        return Real.of(50 + 10 * (raw - mean) / sd);
    }

    /**
     * Calculates compatibility between two profiles based on Euclidean distance.
     */
    public static Real calculateCompatibility(OCEAN p1, OCEAN p2) {
        double dist = Math.sqrt(
            Math.pow(p1.openness() - p2.openness(), 2) +
            Math.pow(p1.conscientiousness() - p2.conscientiousness(), 2) +
            Math.pow(p1.extraversion() - p2.extraversion(), 2) +
            Math.pow(p1.agreeableness() - p2.agreeableness(), 2) +
            Math.pow(p1.neuroticism() - p2.neuroticism(), 2)
        );
        return Real.of(1.0 / (1.0 + dist));
    }
}
