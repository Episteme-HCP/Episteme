/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.matrices.solvers;

import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;

/**
 * Result of an LU decomposition.
 * <p>
 * P * A = L * U
 * </p>
 *
 * @param <E> the element type
 * @param L   the lower triangular matrix
 * @param U   the upper triangular matrix
 * @param P   the permutation vector
 */
public record LUResult<E>(Matrix<E> L, Matrix<E> U, Vector<E> P) {}
