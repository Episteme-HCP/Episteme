package org.jscience.psychology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Normalization and scoring for psychometric tests.
 */
public final class PsychometricNormalizer {

    private PsychometricNormalizer() {}

    /**
     * Converts a raw score to a Z-score.
     * Z = (X - mean) / stdDev
     */
    public static Real toZScore(Real rawScore, Real mean, Real stdDev) {
        if (stdDev.compareTo(Real.of(0.0)) == 0) return Real.of(0.0);
        return rawScore.subtract(mean).divide(stdDev);
    }

    /**
     * Converts a Z-score to a T-score (Mean=50, StdDev=10).
     * T = Z * 10 + 50
     */
    public static Real toTScore(Real zScore) {
        return zScore.multiply(Real.of(10.0)).add(Real.of(50.0));
    }

    /**
     * Converts a raw score to an IQ score (Mean=100, StdDev=15).
     * IQ = Z * 15 + 100
     */
    public static Real toIQScore(Real zScore) {
        return zScore.multiply(Real.of(15.0)).add(Real.of(100.0));
    }
}
