/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.biology.structure;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import java.util.List;

/**
 * Provider for molecular folding algorithms.
 */
public interface DnaFoldingProvider extends AlgorithmProvider {

    /**
     * Calculates energy for a 3D structure.
     */
    Real calculateEnergy(List<Real[]> points, String sequence);
    double calculateEnergy(double[][] points, String sequence);
    float calculateEnergy(float[][] points, String sequence);
    
    double calculateEnergy(double[] flatPoints, String sequence);
    float calculateEnergy(float[] flatPoints, String sequence);

    /**
     * Performs a single Monte-Carlo step.
     */
    void step(List<Real[]> points, String sequence, double temperature, long seed);
    void step(double[][] points, String sequence, double temperature, long seed);
    void step(float[][] points, String sequence, float temperature, long seed);
    
    void step(double[] flatPoints, String sequence, double temperature, long seed);
    void step(float[] flatPoints, String sequence, float temperature, long seed);
}
