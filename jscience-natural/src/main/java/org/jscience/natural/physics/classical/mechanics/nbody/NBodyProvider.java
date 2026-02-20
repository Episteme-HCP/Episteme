/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.physics.classical.mechanics.nbody;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Interface for N-Body simulation providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface NBodyProvider extends AlgorithmProvider {

    void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening);

    void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening);

    default void step(double[] positions, double[] velocities, double[] masses, int numBodies, double G, double dt, double softening) {
        double[] forces = new double[numBodies * 3];
        computeForces(positions, masses, forces, G, softening);
        for (int i = 0; i < numBodies; i++) {
            for (int k = 0; k < 3; k++) {
                velocities[i * 3 + k] += forces[i * 3 + k] / masses[i] * dt;
                positions[i * 3 + k] += velocities[i * 3 + k] * dt;
            }
        }
    }

    default void stepReal(Real[] positions, Real[] velocities, Real[] masses, int numBodies, Real G, Real dt, Real softening) {
        Real[] forces = new Real[numBodies * 3];
        computeForces(positions, masses, forces, G, softening);
        for (int i = 0; i < numBodies; i++) {
            for (int k = 0; k < 3; k++) {
                velocities[i * 3 + k] = velocities[i * 3 + k].add(forces[i * 3 + k].divide(masses[i]).multiply(dt));
                positions[i * 3 + k] = positions[i * 3 + k].add(velocities[i * 3 + k].multiply(dt));
            }
        }
    }

    @Override
    default String getName() {
        return "N-Body Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "N-Body Simulation";
    }
}
