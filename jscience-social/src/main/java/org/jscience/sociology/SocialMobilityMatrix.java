package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models social mobility using transition matrices.
 */
public final class SocialMobilityMatrix {

    private SocialMobilityMatrix() {}

    public record MobilityClass(String name, int rank) {}

    /**
     * Projects class distribution after N generations.
     * P_future = P_current * M^n
     */
    public static double[] projectClassDistribution(double[] initialDistribution, 
            double[][] transitionMatrix, int generations) {
        
        int n = initialDistribution.length;
        double[] current = initialDistribution.clone();
        
        for (int g = 0; g < generations; g++) {
            double[] next = new double[n];
            for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                    next[j] += current[i] * transitionMatrix[i][j];
                }
            }
            current = next;
        }
        
        return current;
    }

    /**
     * Calculates mobility index (trace / size).
     * 0 = total immobility, 1 = total mobility (perfectly random)
     */
    public static Real mobilityIndex(double[][] matrix) {
        int n = matrix.length;
        double trace = 0;
        for (int i = 0; i < n; i++) {
            trace += matrix[i][i];
        }
        // Prais Index: (n - trace) / (n - 1)
        return Real.of((n - trace) / (n - 1));
    }

    /**
     * Finds the equilibrium class distribution (Steady State).
     */
    public static double[] findEquilibrium(double[][] matrix) {
        // Use a high number of iterations as approximation
        return projectClassDistribution(new double[]{1.0, 0, 0, 0, 0}, matrix, 50);
    }
}
