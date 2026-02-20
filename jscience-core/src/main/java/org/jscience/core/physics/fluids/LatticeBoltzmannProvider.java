/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.physics.fluids;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Interface for Lattice Boltzmann Method (LBM) providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface LatticeBoltzmannProvider extends AlgorithmProvider {

    void evolve(double[][][] f, boolean[][] obstacle, double omega);

    void evolve(Real[][][] f, boolean[][] obstacle, Real omega);

    @Override
    default String getName() {
        return "Lattice Boltzmann Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "Lattice Boltzmann";
    }
}
