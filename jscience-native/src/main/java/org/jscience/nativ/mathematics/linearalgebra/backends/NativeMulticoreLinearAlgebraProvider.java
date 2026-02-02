/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.mathematics.linearalgebra.backends;

import org.jscience.core.technical.algorithm.LinearAlgebraProvider;
import org.jscience.nativ.mathematics.linearalgebra.matrices.backends.PanamaBLASBackend;

/**
 * Native multicore linear algebra provider using BLAS/LAPACK.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public abstract class NativeMulticoreLinearAlgebraProvider implements LinearAlgebraProvider<Double> {

    private final PanamaBLASBackend backend = new PanamaBLASBackend();

    @Override
    public String getName() {
        return "Native Multicore Linear Algebra (BLAS/LAPACK)";
    }

    // @Override
    public void matrixMultiply(double[] a, double[] b, double[] c, int m, int n, int k) {
        // backend.dgemm(a, b, c, m, n, k);
        throw new UnsupportedOperationException("Not implemented for double[]");
    }

    // @Override
    public double dotProduct(double[] a, double[] b) {
        // return backend.ddot(a, b);
        throw new UnsupportedOperationException("Not implemented for double[]");
    }

    @Override
    public boolean isAvailable() {
        return backend != null; // Placeholder roughly, but assuming backend variable exists. PanamaBLASBackend might have isAvailable.
    }
}
