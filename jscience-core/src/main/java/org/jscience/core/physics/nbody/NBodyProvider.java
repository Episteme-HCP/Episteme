/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.physics.nbody;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Interface for N-Body simulation providers.
 * 
 * <p>
 * <b>Reference:</b><br>
 * Aarseth, S. J. (2003). <i>Gravitational N-Body Simulations</i>. Cambridge University Press.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface NBodyProvider extends AlgorithmProvider {

    void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening);

    void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening);

    default void step(double[] masses, double[] positions, double[] velocities, 
                     double dt, double G, double softening) {
        int n = masses.length;
        double[] forces = new double[n * 3];
        
        computeForces(positions, masses, forces, G, softening);
        
        for (int i = 0; i < n; i++) {
            int idx = i * 3;
            double m = masses[i];
            velocities[idx] += 0.5 * dt * forces[idx] / m;
            velocities[idx + 1] += 0.5 * dt * forces[idx + 1] / m;
            velocities[idx + 2] += 0.5 * dt * forces[idx + 2] / m;
            positions[idx] += dt * velocities[idx];
            positions[idx + 1] += dt * velocities[idx + 1];
            positions[idx + 2] += dt * velocities[idx + 2];
        }
        
        computeForces(positions, masses, forces, G, softening);
        
        for (int i = 0; i < n; i++) {
            int idx = i * 3;
            double m = masses[i];
            velocities[idx] += 0.5 * dt * forces[idx] / m;
            velocities[idx + 1] += 0.5 * dt * forces[idx + 1] / m;
            velocities[idx + 2] += 0.5 * dt * forces[idx + 2] / m;
        }
    }

    default void step(Real[] masses, Real[] positions, Real[] velocities, 
                     Real dt, Real G, Real softening) {
        int n = masses.length;
        Real[] forces = new Real[n * 3];
        
        computeForces(positions, masses, forces, G, softening);
        
        Real halfDt = dt.divide(Real.of(2.0));
        
        for (int i = 0; i < n; i++) {
            int idx = i * 3;
            Real mi = masses[i];
            velocities[idx] = velocities[idx].add(halfDt.multiply(forces[idx]).divide(mi));
            velocities[idx + 1] = velocities[idx + 1].add(halfDt.multiply(forces[idx + 1]).divide(mi));
            velocities[idx + 2] = velocities[idx + 2].add(halfDt.multiply(forces[idx + 2]).divide(mi));
            positions[idx] = positions[idx].add(dt.multiply(velocities[idx]));
            positions[idx + 1] = positions[idx + 1].add(dt.multiply(velocities[idx + 1]));
            positions[idx + 2] = positions[idx + 2].add(dt.multiply(velocities[idx + 2]));
        }
        
        computeForces(positions, masses, forces, G, softening);
        
        for (int i = 0; i < n; i++) {
            int idx = i * 3;
            Real mi = masses[i];
            velocities[idx] = velocities[idx].add(halfDt.multiply(forces[idx]).divide(mi));
            velocities[idx + 1] = velocities[idx + 1].add(halfDt.multiply(forces[idx + 1]).divide(mi));
            velocities[idx + 2] = velocities[idx + 2].add(halfDt.multiply(forces[idx + 2]).divide(mi));
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
