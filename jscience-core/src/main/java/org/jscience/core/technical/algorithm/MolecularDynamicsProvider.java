/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Interface for Molecular Dynamics (MD) simulation providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface MolecularDynamicsProvider extends AlgorithmProvider {

    /**
     * Updates particle positions and velocities using double precision.
     */
    void integrate(double[] positions, double[] velocities, double[] forces, double[] masses, double dt, double damping);

    /**
     * Computes harmonic bond forces between atoms using double precision.
     */
    void calculateBondForces(double[] positions, double[] forces, int[] bondIndices, double[] bondLengths, double[] bondConstants);

    /**
     * Computes Lennard-Jones forces between atoms using double precision.
     */
    void calculateNonBondedForces(double[] positions, double[] forces, double epsilon, double sigma, double cutoff);

    /**
     * Updates particle positions and velocities using Real precision.
     */
    void integrate(Real[] positions, Real[] velocities, Real[] forces, Real[] masses, Real dt, Real damping);

    /**
     * Computes harmonic bond forces between atoms using Real precision.
     */
    void calculateBondForces(Real[] positions, Real[] forces, int[] bondIndices, Real[] bondLengths, Real[] bondConstants);

    /**
     * Computes Lennard-Jones forces between atoms using Real precision.
     */
    void calculateNonBondedForces(Real[] positions, Real[] forces, Real epsilon, Real sigma, Real cutoff);

    @Override
    default String getName() {
        return "Molecular Dynamics Provider";
    }
}
