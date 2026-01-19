package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models age-structured populations and demographic transitions.
 */
public final class Demography {

    private Demography() {}

    public record PopulationPyramid(
        int[] maleCounts, // 5-year cohorts: 0-4, 5-9, ...
        int[] femaleCounts
    ) {}

    /**
     * Leslie Matrix projection.
     * P(t+1) = L * P(t)
     * 
     * @param population Current population vector by age group
     * @param fertility Rates for each age group
     * @param survival Probabilities for each age group
     */
    public static double[] projectLeslie(double[] population, double[] fertility, double[] survival) {
        int n = population.length;
        double[] next = new double[n];
        
        // New births (first row of Leslie matrix)
        for (int i = 0; i < n; i++) {
            next[0] += population[i] * fertility[i];
        }
        
        // Aging (sub-diagonal)
        for (int i = 0; i < n - 1; i++) {
            next[i + 1] = population[i] * survival[i];
        }
        
        return next;
    }

    /**
     * Calculates Child-Woman Ratio (CWR).
     * CWR = (Children 0-4 / Women 15-49) * 1000
     */
    public static Real childWomanRatio(int children0to4, int women15to49) {
        if (women15to49 == 0) return Real.ZERO;
        return Real.of(1000.0 * children0to4 / women15to49);
    }

    /**
     * Net Reproduction Rate (NRR).
     * Sigma (Fertility_i * Survival_female_i)
     */
    public static Real netReproductionRate(double[] ageFertility, double[] femaleSurvival) {
        double nrr = 0;
        for (int i = 0; i < ageFertility.length; i++) {
            nrr += ageFertility[i] * femaleSurvival[i];
        }
        return Real.of(nrr);
    }

    /**
     * Categorizes Demographic Transition Stage (1-5).
     */
    public static int estimateTransitionStage(double birthRate, double deathRate) {
        if (birthRate > 35 && deathRate > 30) return 1; // High stationary
        if (birthRate > 30 && deathRate < 20) return 2; // Early expanding
        if (birthRate < 30 && deathRate < 15 && birthRate > deathRate) return 3; // Late expanding
        if (birthRate < 15 && deathRate < 15) return 4; // Low stationary
        if (birthRate < deathRate) return 5; // Declining
        return 3;
    }
}
