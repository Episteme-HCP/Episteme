/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.linearalgebra.matrices.solvers;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;

/**
 * Result of an Eigenvalue Decomposition.
 * <p>
 * A * V = V * diag(D)
 * </p>
 *
 * @param <E> the element type
 * @param V   the eigenvectors (as columns)
 * @param D   the eigenvalues (as a vector)
 */
public record EigenResult<E>(Matrix<E> V, Vector<E> D) {}
