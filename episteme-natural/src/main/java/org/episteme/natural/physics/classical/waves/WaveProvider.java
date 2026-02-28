/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.classical.waves;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Interface for Wave Equation simulation providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface WaveProvider extends AlgorithmProvider {

    void solve(double[][] u, double[][] uPrev, int width, int height, double c, double damping);

    void solve(Real[][] u, Real[][] uPrev, int width, int height, Real c, Real damping);

    @Override
    default String getName() {
        return "Wave Equation Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "Wave Equation";
    }
}
