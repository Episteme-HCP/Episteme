/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Interface for Navier-Stokes Fluid Dynamics providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface NavierStokesProvider extends AlgorithmProvider {

    /**
     * Solves one time step of the Navier-Stokes equations using double precision.
     */
    void solve(double[] density, double[] u, double[] v, double[] w, double dt, double viscosity, int width, int height, int depth);

    /**
     * Solves one time step of the Navier-Stokes equations using Real precision.
     */
    void solve(Real[] density, Real[] u, Real[] v, Real[] w, Real dt, Real viscosity, int width, int height, int depth);

    @Override
    default String getName() {
        return "Navier-Stokes Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "Navier-Stokes";
    }
}
