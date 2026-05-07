/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated baseline test for NativeCUDASparseLinearAlgebraBackend.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class NativeCUDASparseLinearAlgebraBackendTest {

    @Test
    public void testSolvers() {
        try (NativeCUDASparseLinearAlgebraBackend backend = new NativeCUDASparseLinearAlgebraBackend()) {
            if (!backend.isAvailable()) {
                System.err.println("CUDA not available, skipping solver tests.");
                return;
            }

        org.episteme.core.mathematics.sets.Reals reals = org.episteme.core.mathematics.sets.Reals.getInstance();
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<org.episteme.core.mathematics.numbers.real.Real> A = 
            org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix.zeros(3, 3, reals);
        A.set(0, 0, org.episteme.core.mathematics.numbers.real.Real.of(2.0));
        A.set(1, 1, org.episteme.core.mathematics.numbers.real.Real.of(3.0));
        A.set(2, 2, org.episteme.core.mathematics.numbers.real.Real.of(4.0));

        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> b = 
            org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(new double[]{2.0, 6.0, 12.0});
        
        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> x0 = 
            org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector.of(new double[]{0.0, 0.0, 0.0});

        org.episteme.core.mathematics.numbers.real.Real tol = org.episteme.core.mathematics.numbers.real.Real.of(1e-10);

        // Test CG
        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> xCG = 
            backend.conjugateGradient(A, b, x0, tol, 100);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, toArray(xCG), 1e-8);

        // Test BiCGSTAB
        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> xBi = 
            backend.bicgstab(A, b, x0, tol, 100);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, toArray(xBi), 1e-8);

        // Test GMRES
        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> xGM = 
            backend.gmres(A, b, x0, tol, 10, 5);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, toArray(xGM), 1e-8);
        }
    }

    private double[] toArray(org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.Real> v) {
        double[] res = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) res[i] = v.get(i).doubleValue();
        return res;
    }
}

