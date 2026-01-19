package org.jscience.geography;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models spatial interaction and migration flows.
 */
public final class SpatialInteraction {

    private SpatialInteraction() {}

    /**
     * Gravity Model for interactions between two locations.
     * I = G * (Pi * Pj) / dij^beta
     * 
     * @param p1 Population of location 1
     * @param p2 Population of location 2
     * @param distance Distance between locations
     * @param beta Friction of distance (typically 1.0 to 2.0)
     */
    public static Real interactionIntensity(double p1, double p2, double distance, double beta) {
        if (distance <= 0) return Real.ZERO;
        double intensity = (p1 * p2) / Math.pow(distance, beta);
        return Real.of(intensity);
    }

    /**
     * Migration Pull Force (Lee's Model factors).
     */
    public static Real migrationForce(double wageDiff, double distance, double unemploymentRate) {
        // Force is proportional to wage benefit, inversely to distance and risk
        double force = (wageDiff / distance) * (1.0 - unemploymentRate);
        return Real.of(force);
    }

    /**
     * Calculate Reilly's Law of Retail Gravitation (Breaking Point).
     * D12 = d / (1 + sqrt(P2/P1))
     * Point from city 1 where trade is split.
     */
    public static Real retailBreakingPoint(double d, double p1, double p2) {
        return Real.of(d / (1 + Math.sqrt(p2 / p1)));
    }

    /**
     * Huff Model for probability of choosing a destination.
     * Pj = (Sj / d_ij^b) / sum(Sk / d_ik^b)
     */
    public static double[] huffModelProbs(double[] sizes, double[] distances, double beta) {
        double[] utilities = new double[sizes.length];
        double sum = 0;
        for (int i = 0; i < sizes.length; i++) {
            utilities[i] = sizes[i] / Math.pow(distances[i], beta);
            sum += utilities[i];
        }
        for (int i = 0; i < sizes.length; i++) {
            utilities[i] /= sum;
        }
        return utilities;
    }
}
