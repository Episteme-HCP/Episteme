/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.biology.structure;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Provider for protein folding using the HP model.
 */
public interface ProteinFoldingProvider extends AlgorithmProvider {

    /**
     * Calculates energy for a fold.
     */
    Real calculateEnergy(int[][] positions, boolean[] isHydrophobic);
    double calculateEnergyDouble(int[][] positions, boolean[] isHydrophobic);
    float calculateEnergyFloat(int[][] positions, boolean[] isHydrophobic);

    /**
     * Performs Monte-Carlo steps.
     */
    void simulate(int[][] positions, boolean[] isHydrophobic, int iterations, double temperature, long seed);
}
