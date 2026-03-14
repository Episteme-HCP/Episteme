/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;


/**
 * Marker interface for Linear Algebra Providers specialized for Sparse Matrices.
 * <p>
 * This allows specialized discovery of sparse algorithms via AlgorithmManager.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface SparseLinearAlgebraProvider<E> extends LinearAlgebraProvider<E> {

    @Override
    default String getAlgorithmType() {
        return "Linear Algebra (Sparse)";
    }

    /**
     * Solves Ax = b using BiCGSTAB (BiConjugate Gradient Stabilized) method.
     */
    default Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        throw new UnsupportedOperationException(getName() + " does not support BiCGSTAB");
    }

    /**
     * Solves Ax = b using Conjugate Gradient method.
     */
    default Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        throw new UnsupportedOperationException(getName() + " does not support Conjugate Gradient");
    }

    /**
     * Solves Ax = b using GMRES (Generalized Minimal Residual) method.
     */
    default Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        throw new UnsupportedOperationException(getName() + " does not support GMRES");
    }
}
