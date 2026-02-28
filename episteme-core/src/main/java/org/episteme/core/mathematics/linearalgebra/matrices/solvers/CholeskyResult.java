/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices.solvers;

import org.episteme.core.mathematics.linearalgebra.Matrix;

/**
 * Result of a Cholesky decomposition.
 * <p>
 * A = L * L^T
 * </p>
 *
 * @param <E> the element type
 * @param L   the lower triangular matrix
 */
public record CholeskyResult<E>(Matrix<E> L) {}
