/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Interface for Hartree-Fock Self-Consistent Field (SCF) providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface SCFProvider extends AlgorithmProvider {

    /**
     * compute the Fock matrix from the Density matrix and integrals using double precision.
     * F = H + G(P)
     * 
     * @param densityMatrix flattened density matrix (N*N)
     * @param oneElectronIntegrals flattened core hamiltonian H (N*N)
     * @param twoElectronIntegrals flattened two-electron integrals (N*N*N*N)
     * @param fockMatrix output flattened fock matrix (N*N)
     * @param n basis set size
     */
    void computeFockMatrix(double[] densityMatrix, double[] oneElectronIntegrals, double[] twoElectronIntegrals, double[] fockMatrix, int n);

    /**
     * compute the Fock matrix from the Density matrix and integrals using Real precision.
     * F = H + G(P)
     */
    void computeFockMatrixReal(Real[] densityMatrix, Real[] oneElectronIntegrals, Real[] twoElectronIntegrals, Real[] fockMatrix, int n);

    @Override
    default String getName() {
        return "SCF Provider (Quantum Chemistry)";
    }
}
