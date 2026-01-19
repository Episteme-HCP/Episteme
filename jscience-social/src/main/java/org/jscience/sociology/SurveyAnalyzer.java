package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Analyzes social survey data with weighting.
 */
public final class SurveyAnalyzer {

    private SurveyAnalyzer() {}

    /**
     * Calculates weighted average of responses.
     */
    public static Real weightedMean(double[] responses, double[] weights) {
        double sum = 0;
        double weightSum = 0;
        for (int i = 0; i < responses.length; i++) {
            sum += responses[i] * weights[i];
            weightSum += weights[i];
        }
        return Real.of(sum / weightSum);
    }
}
