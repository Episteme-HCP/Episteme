/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.sociology;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Models social mobility between classes using Markov chain transition matrices.
 * Provides tools to project future class distributions and analyze mobility metrics.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class SocialMobilityMatrix {

    private SocialMobilityMatrix() {}

    /**
     * Represents a socioeconomic class.
     * @param name Name of the class (e.g., "Upper Class")
     * @param rank Hierarchical rank (higher is better)
     */
    public record MobilityClass(String name, int rank) {}

    /**
     * Projects the class distribution after a specified number of generations.
     * Formula: P_future = P_current * M^n
     * 
     * @param initialDistribution Array representing initial population percentages in each class
     * @param transitionMatrix    Square matrix where M[i][j] is probability of moving from class i to class j
     * @param generations         Number of time steps to project
     * @return Array representing the future class distribution
     * @throws IllegalArgumentException if matrix dimensions do not match distribution
     */
    public static double[] projectClassDistribution(double[] initialDistribution, 
            double[][] transitionMatrix, int generations) {
        
        int n = initialDistribution.length;
        if (transitionMatrix.length != n || transitionMatrix[0].length != n) {
            throw new IllegalArgumentException("Transition matrix dimensions must match distribution length.");
        }

        double[] current = initialDistribution.clone();
        
        for (int g = 0; g < generations; g++) {
            double[] next = new double[n];
            for (int j = 0; j < n; j++) {
                // For each destination class j, sum contributions from all source classes i
                for (int i = 0; i < n; i++) {
                    next[j] += current[i] * transitionMatrix[i][j];
                }
            }
            current = next;
        }
        
        return current;
    }

    /**
     * Calculates the mobility index (Prais Index) of a transition matrix.
     * <p>
     * Index M = (n - trace(P)) / (n - 1)
     * </p>
     * Where trace(P) is the sum of diagonal elements (stayers).
     * 0 implies perfect immobility (identity matrix).
     * 1 implies perfect mobility (random chance, essentially).
     * 
     * @param matrix the transition matrix
     * @return the mobility index
     */
    public static Real mobilityIndex(double[][] matrix) {
        int n = matrix.length;
        if (n <= 1) return Real.ZERO;

        double trace = 0;
        for (int i = 0; i < n; i++) {
            trace += matrix[i][i];
        }
        
        // (n - sum of stayers) / (n - 1)
        return Real.of((n - trace) / (n - 1));
    }

    /**
     * Finds the equilibrium class distribution (Steady State) of the matrix.
     * Iterates until convergence (simplified numerical approach).
     * 
     * @param matrix the transition matrix
     * @return the equilibrium distribution array
     */
    public static double[] findEquilibrium(double[][] matrix) {
        int n = matrix.length;
        // Start with an arbitrary distribution, e.g., 100% in first class
        double[] initial = new double[n];
        initial[0] = 1.0;
        
        // Use a sufficient number of iterations to approximate steady state
        // 50-100 is usually enough for well-behaved stochastic matrices
        return projectClassDistribution(initial, matrix, 100);
    }
}
