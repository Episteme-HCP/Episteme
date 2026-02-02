/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.structures.rings.Ring;

/**
 * Service provider interface for linear algebra operations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface LinearAlgebraProvider<E> extends AlgorithmProvider {

    /**
     * Checks if this provider is compatible with the given ring.
     */
    default boolean isCompatible(Ring<?> ring) {
        return true; 
    }

    // --- Vector Operations ---
    Vector<E> add(Vector<E> a, Vector<E> b);
    Vector<E> subtract(Vector<E> a, Vector<E> b);
    Vector<E> multiply(Vector<E> vector, E scalar);
    E dot(Vector<E> a, Vector<E> b);
    E norm(Vector<E> a);

    // --- Matrix Operations ---
    Matrix<E> add(Matrix<E> a, Matrix<E> b);
    Matrix<E> subtract(Matrix<E> a, Matrix<E> b);
    Matrix<E> multiply(Matrix<E> a, Matrix<E> b);
    Vector<E> multiply(Matrix<E> a, Vector<E> b);
    Matrix<E> inverse(Matrix<E> a);
    E determinant(Matrix<E> a);
    Vector<E> solve(Matrix<E> a, Vector<E> b);
    Matrix<E> transpose(Matrix<E> a);
    Matrix<E> scale(E scalar, Matrix<E> a);

    @Override
    default String getName() {
        return "Linear Algebra Provider";
    }
}
