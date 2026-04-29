/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.economics.growth;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Provider for economic growth algorithms (Solow, Ramsey, etc.).
 */
public interface EconomyProvider extends AlgorithmProvider {

    /**
     * Evolves the capital state using high-precision Real numbers.
     */
    Real evolve(Real k, EconomyParameters params, double dt, double dW);

    /**
     * Evolves the capital state using double precision.
     */
    double evolve(double k, EconomyParameters params, double dt, double dW);

    /**
     * Evolves the capital state using float precision.
     */
    float evolve(float k, EconomyParameters params, float dt, float dW);
}
