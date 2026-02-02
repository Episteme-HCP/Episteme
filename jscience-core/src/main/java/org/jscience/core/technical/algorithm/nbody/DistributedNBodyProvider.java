/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.nbody;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.NBodyProvider;

/**
 * Distributed N-body simulation provider.
 * Manages domain decomposition and MPI/RPC dispatch.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class DistributedNBodyProvider implements NBodyProvider {

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public boolean isAvailable() {
        // Future: Check if MPI cluster or distributed environment is configured
        return false;
    }

    @Override
    public void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening) {
        throw new UnsupportedOperationException("Distributed N-Body simulation not yet implemented.");
    }

    @Override
    public void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening) {
        throw new UnsupportedOperationException("Distributed N-Body simulation not yet implemented.");
    }

    @Override
    public String getName() {
        return "Distributed N-Body simulation (MPI/Cloud)";
    }
}
