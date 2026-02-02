/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Interface for Wave Equation simulation providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface WaveProvider extends AlgorithmProvider {

    /**
     * Performs one time step of the wave simulation using double precision.
     */
    void solve(double[][] u, double[][] uPrev, int width, int height, double c, double damping);

    /**
     * Performs one time step of the wave simulation using Real precision.
     */
    void solve(Real[][] u, Real[][] uPrev, int width, int height, Real c, Real damping);

    @Override
    default String getName() {
        return "Wave Equation Provider";
    }
}
