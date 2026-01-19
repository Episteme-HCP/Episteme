package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Models the diffusion of culture, innovations, or rumors through a population.
 */
public final class CulturalDiffusionModel {

    private CulturalDiffusionModel() {}

    public enum AdoptionStatus { INNOVATOR, EARLY_ADOPTER, EARLY_MAJORITY, LATE_MAJORITY, LAGGARD }

    public record CultureTrait(
        String name,
        double complexity, // 0-1 (higher is harder to adopt)
        double utility      // 0-1 (higher is more attractive)
    ) {}

    /**
     * Bass Diffusion Model for innovation adoption.
     * dN(t)/dt = (p + q * N(t)/M) * (M - N(t))
     * 
     * @param p Coefficient of innovation (external influence)
     * @param q Coefficient of imitation (internal influence)
     * @param m Potential market/population size
     */
    public static List<Double> simulateBassDiffusion(double p, double q, double m, int periods) {
        List<Double> adopters = new ArrayList<>();
        double n = 0; // Cumulative adopters

        for (int t = 0; t < periods; t++) {
            double dn = (p + q * (n / m)) * (m - n);
            n += dn;
            adopters.add(n);
        }
        return adopters;
    }

    /**
     * Calculates the probability of cultural transmission between two agents.
     * Based on Axelrod's Model: Probability proportional to similarity.
     */
    public static Real transmissionProbability(int[] traits1, int[] traits2) {
        int shared = 0;
        for (int i = 0; i < traits1.length; i++) {
            if (traits1[i] == traits2[i]) shared++;
        }
        return Real.of((double) shared / traits1.length);
    }

    /**
     * Categorizes an adopter based on their position in the adoption timeline.
     * Uses standard bell curve percentages.
     */
    public static AdoptionStatus getAdopterType(double cumulativePercent) {
        if (cumulativePercent <= 2.5) return AdoptionStatus.INNOVATOR;
        if (cumulativePercent <= 16.0) return AdoptionStatus.EARLY_ADOPTER;
        if (cumulativePercent <= 50.0) return AdoptionStatus.EARLY_MAJORITY;
        if (cumulativePercent <= 84.0) return AdoptionStatus.LATE_MAJORITY;
        return AdoptionStatus.LAGGARD;
    }

    /**
     * Estimates "Cultural Distance" between two societies.
     */
    public static Real culturalDistance(Map<String, Double> values1, Map<String, Double> values2) {
        double sumSq = 0;
        Set<String> allKeys = new HashSet<>(values1.keySet());
        allKeys.addAll(values2.keySet());

        for (String key : allKeys) {
            double v1 = values1.getOrDefault(key, 0.5);
            double v2 = values2.getOrDefault(key, 0.5);
            sumSq += Math.pow(v1 - v2, 2);
        }
        return Real.of(Math.sqrt(sumSq));
    }
}
