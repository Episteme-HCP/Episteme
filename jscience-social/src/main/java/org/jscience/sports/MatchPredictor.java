package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Predicts match outcomes based on history and context.
 */
public final class MatchPredictor {

    private MatchPredictor() {}

    /**
     * Simple Poisson distribution based score prediction.
     */
    public static Real winProbability(double homeRating, double awayRating, double homeAdvantage) {
        double diff = (homeRating + homeAdvantage) - awayRating;
        double prob = 1.0 / (1.0 + Math.exp(-diff / 100.0));
        return Real.of(prob);
    }
}
