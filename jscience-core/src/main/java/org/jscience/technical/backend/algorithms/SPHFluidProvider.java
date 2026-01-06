/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.technical.backend.algorithms;

/**
 * Interface for SPH (Smoothed Particle Hydrodynamics) fluid providers.
 * Allows switching between CPU (Multicore) and GPU implementations.
 */
public interface SPHFluidProvider {

    /**
     * Performs one simulation step (Density/Pressure, Forces, Integration).
     * 
     * @param positions       Particle positions [x, y, z, x, y, z...]
     * @param velocities      Particle velocities [vx, vy, vz...]
     * @param densities       Particle densities
     * @param pressures       Particle pressures
     * @param forces          Particle forces [fx, fy, fz...]
     * @param numParticles    Number of particles
     * @param dt              Time step
     * @param mass            Particle mass
     * @param restDensity     Rest density
     * @param stiffness       Stiffness constant
     * @param viscosity       Viscosity coefficient
     * @param smoothingRadius Smoothing radius (h)
     * @param gravity         Gravity vector [gx, gy, gz]
     */
    void step(double[] positions, double[] velocities, double[] densities, double[] pressures, double[] forces,
            int numParticles, double dt,
            double mass, double restDensity, double stiffness, double viscosity, double smoothingRadius,
            double[] gravity);

    /**
     * Returns the name of the provider.
     */
    String getName();
}
