/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.linearalgebra;

import org.jscience.core.technical.algorithm.LinearAlgebraProvider;
import java.nio.DoubleBuffer;

/**
 * Matrix multiplication algorithm provider.
 * <p>
 * Provides various implementations of matrix multiplication including
 * naive, blocked, Strassen, and GPU-accelerated variants.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface MatrixMultiplyProvider extends LinearAlgebraProvider {

    /**
     * Performs matrix multiplication: C = A × B
     *
     * @param A Left matrix (row-major, size m×k)
     * @param B Right matrix (row-major, size k×n)
     * @param C Result matrix (row-major, size m×n)
     * @param m Number of rows in A
     * @param n Number of columns in B
     * @param k Common dimension
     */
    void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, 
                       int m, int n, int k);

    @Override
    default String getAlgorithmType() {
        return "matrix_multiply";
    }
}
