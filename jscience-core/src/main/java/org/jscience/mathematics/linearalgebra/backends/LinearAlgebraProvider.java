package org.jscience.mathematics.linearalgebra.backends;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;

import org.jscience.technical.backend.ComputeBackend;

/**
 * Service provider interface for linear algebra operations.
 * <p>
 * Implementations of this interface provide the actual computation logic
 * for vectors and matrices, allowing for different backends (Java CPU, Native
 * BLAS, GPU CUDA).
 * </p>
 * 
 * @param <E> the type of scalar elements
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public interface LinearAlgebraProvider<E> extends ComputeBackend {

    // --- Vector Operations ---

    Vector<E> add(Vector<E> a, Vector<E> b);

    Vector<E> subtract(Vector<E> a, Vector<E> b);

    Vector<E> multiply(Vector<E> vector, E scalar);

    E dot(Vector<E> a, Vector<E> b);

    default E norm(Vector<E> a) {
        throw new UnsupportedOperationException("Norm not implemented by this provider");
    }

    // --- Matrix Operations ---

    Matrix<E> add(Matrix<E> a, Matrix<E> b);

    Matrix<E> subtract(Matrix<E> a, Matrix<E> b);

    Matrix<E> multiply(Matrix<E> a, Matrix<E> b);

    Vector<E> multiply(Matrix<E> a, Vector<E> b);

    Matrix<E> inverse(Matrix<E> a);

    E determinant(Matrix<E> a);

    /**
     * Solves the linear system Ax = b.
     * 
     * @param a Matrix A
     * @param b Vector b
     * @return Vector x such that Ax = b
     */
    Vector<E> solve(Matrix<E> a, Vector<E> b);

    Matrix<E> transpose(Matrix<E> a);

    Matrix<E> scale(E scalar, Matrix<E> a);

    // Future extensions: eigenvalue, etc.
}
