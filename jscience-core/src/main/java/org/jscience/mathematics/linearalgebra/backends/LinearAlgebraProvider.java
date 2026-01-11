/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.linearalgebra.backends;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;

import org.jscience.technical.backend.ComputeBackend;
import org.jscience.technical.backend.BackendDiscovery;

/**
 * Service provider interface for linear algebra operations.
 * <p>
 * Implementations of this interface provide the actual computation logic
 * for vectors and matrices, allowing for different backends (Java CPU, Native
 * BLAS, GPU CUDA).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface LinearAlgebraProvider<E> extends ComputeBackend {

    @Override
    default String getType() {
        return BackendDiscovery.TYPE_LINEAR_ALGEBRA;
    }

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


