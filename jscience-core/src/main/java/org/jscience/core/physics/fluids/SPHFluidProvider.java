/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.physics.fluids;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Interface for Smoothed Particle Hydrodynamics (SPH) fluid simulation providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface SPHFluidProvider extends AlgorithmProvider {

    void step(double[] positions, double[] velocities, double[] densities, double[] pressures, double[] forces,
            int numParticles, double dt, double mass, double restDensity, double stiffness, double viscosity,
            double smoothingRadius, double[] gravity);

    void stepReal(Real[] positions, Real[] velocities, Real[] densities, Real[] pressures, Real[] forces,
            int numParticles, Real dt, Real mass, Real restDensity, Real stiffness, Real viscosity,
            Real smoothingRadius, Real[] gravity);

    @Override
    default String getName() {
        return "SPH Fluid Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "SPH Fluid Dynamics";
    }
}
