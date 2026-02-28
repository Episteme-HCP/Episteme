/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.quantum.scf;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Interface for Hartree-Fock Self-Consistent Field (SCF) providers.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface SCFProvider extends AlgorithmProvider {

    void computeFockMatrix(double[] densityMatrix, double[] oneElectronIntegrals, double[] twoElectronIntegrals, double[] fockMatrix, int n);

    void computeFockMatrixReal(Real[] densityMatrix, Real[] oneElectronIntegrals, Real[] twoElectronIntegrals, Real[] fockMatrix, int n);

    @Override
    default String getName() {
        return "SCF Provider (Quantum Chemistry)";
    }

    @Override
    default String getAlgorithmType() {
        return "Self-Consistent Field";
    }
}
