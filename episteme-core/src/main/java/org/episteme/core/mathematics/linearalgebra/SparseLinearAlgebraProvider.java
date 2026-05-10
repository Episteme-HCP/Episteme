/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.Real;

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
        return "Linear Algebra";
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

    /**
     * Converts a sparse matrix to its dense representation.
     */
    default Matrix<E> toDense(Matrix<E> a) {
        if (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa) {
            org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = sa.getSparseStorage();
            return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>(storage.toDense(), this, (org.episteme.core.mathematics.structures.rings.Ring<E>)a.getScalarRing());
        }
        return a; // Already dense or unknown type
    }

    /**
     * Converts a dense matrix to its sparse representation.
     */
    default Matrix<E> fromDense(Matrix<E> a) {
        if (!(a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix)) {
            org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
                org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage.fromDense(
                    (org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage<E>)a.getStorage(), 
                    (E)a.getScalarRing().zero());
            return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, this, (org.episteme.core.mathematics.structures.rings.Ring<E>)a.getScalarRing());
        }
        return a; // Already sparse
    }

    @Override
    default int rank(Matrix<E> a) {
        return LinearAlgebraProvider.super.rank(toDense(a));
    }

    @Override
    default E conditionNumber(Matrix<E> a) {
        return LinearAlgebraProvider.super.conditionNumber(toDense(a));
    }
}
