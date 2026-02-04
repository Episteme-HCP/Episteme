/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.linearalgebra;

import org.jscience.core.technical.algorithm.LinearAlgebraBackend;

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
public interface SparseLinearAlgebraBackend<E> extends LinearAlgebraBackend<E> {
    // No additional methods needed for now, acts as a specialized type for discovery.
}
