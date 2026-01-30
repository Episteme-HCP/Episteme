/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend.math;

import org.jscience.technical.backend.ComputeBackend;
import org.jscience.technical.backend.ExecutionContext;
import java.nio.DoubleBuffer;

/**
 * Interface for high-performance matrix operations allowing native acceleration.
 */
public interface MatrixBackend extends ComputeBackend {

    /**
     * General Matrix Multiplication: C = alpha * A * B + beta * C
     */
    void dgemm(int rowsA, int colsA, int colsB,
               DoubleBuffer A, int lda,
               DoubleBuffer B, int ldb,
               DoubleBuffer C, int ldc,
               double alpha, double beta);

    /**
     * Matrix-Vector Multiplication: y = alpha * A * x + beta * y
     */
    void dgemv(int rowsA, int colsA,
               DoubleBuffer A, int lda,
               DoubleBuffer x, int incx,
               DoubleBuffer y, int incy,
               double alpha, double beta);

    @Override
    default ExecutionContext createContext() {
        return null;
    }
}
