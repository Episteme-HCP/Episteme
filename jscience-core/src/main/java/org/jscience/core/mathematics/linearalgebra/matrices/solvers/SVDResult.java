/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.matrices.solvers;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;

/**
 * Result of a Singular Value Decomposition (SVD).
 * <p>
 * A = U * S * V^T
 * </p>
 *
 * @param <E> the element type
 * @param U   the left singular vectors
 * @param S   the singular values (as a vector)
 * @param V   the right singular vectors
 */
public record SVDResult<E>(Matrix<E> U, Vector<E> S, Matrix<E> V) {}
