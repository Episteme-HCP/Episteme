/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.physics.molecular;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Interface for Molecular Dynamics (MD) simulation providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface MolecularDynamicsProvider extends AlgorithmProvider {

    void integrate(double[] positions, double[] velocities, double[] forces, double[] masses, double dt, double damping);
    void calculateBondForces(double[] positions, double[] forces, int[] bondIndices, double[] bondLengths, double[] bondConstants);
    void calculateNonBondedForces(double[] positions, double[] forces, double epsilon, double sigma, double cutoff);

    void integrate(Real[] positions, Real[] velocities, Real[] forces, Real[] masses, Real dt, Real damping);
    void calculateBondForces(Real[] positions, Real[] forces, int[] bondIndices, Real[] bondLengths, Real[] bondConstants);
    void calculateNonBondedForces(Real[] positions, Real[] forces, Real epsilon, Real sigma, Real cutoff);

    @Override
    default String getName() {
        return "Molecular Dynamics Provider";
    }

    @Override
    default String getAlgorithmType() {
        return "Molecular Dynamics";
    }
}
