/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.solvers;

import org.jscience.core.mathematics.linearalgebra.Matrix;

/**
 * Result of a QR decomposition.
 * <p>
 * A = Q * R
 * </p>
 *
 * @param <E> the element type
 * @param Q   the orthogonal matrix
 * @param R   the upper triangular matrix
 */
public record QRResult<E>(Matrix<E> Q, Matrix<E> R) {}
