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
        try (NativeCUDASparseLinearAlgebraDoubleBackend backend = new NativeCUDASparseLinearAlgebraDoubleBackend()) {
            if (!backend.isAvailable()) {
                System.err.println("CUDA not available, skipping solver tests.");
                return;
            }

        org.episteme.core.mathematics.sets.Reals reals = org.episteme.core.mathematics.sets.Reals.getInstance();
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<org.episteme.core.mathematics.numbers.real.RealDouble> A = 
            org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix.zeros(3, 3, org.episteme.core.mathematics.numbers.real.RealDouble.RING);
        A.set(0, 0, org.episteme.core.mathematics.numbers.real.RealDouble.of(2.0));
        A.set(1, 1, org.episteme.core.mathematics.numbers.real.RealDouble.of(3.0));
        A.set(2, 2, org.episteme.core.mathematics.numbers.real.RealDouble.of(4.0));

        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.RealDouble> b = 
            org.episteme.core.mathematics.linearalgebra.Vector.of(new org.episteme.core.mathematics.numbers.real.RealDouble[]{
                org.episteme.core.mathematics.numbers.real.RealDouble.of(2.0),
                org.episteme.core.mathematics.numbers.real.RealDouble.of(6.0),
                org.episteme.core.mathematics.numbers.real.RealDouble.of(12.0)}, 
                org.episteme.core.mathematics.numbers.real.RealDouble.RING);
        
        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.RealDouble> x0 = 
            org.episteme.core.mathematics.linearalgebra.Vector.of(new org.episteme.core.mathematics.numbers.real.RealDouble[]{
                org.episteme.core.mathematics.numbers.real.RealDouble.of(0.0),
                org.episteme.core.mathematics.numbers.real.RealDouble.of(0.0),
                org.episteme.core.mathematics.numbers.real.RealDouble.of(0.0)}, 
                org.episteme.core.mathematics.numbers.real.RealDouble.RING);

        org.episteme.core.mathematics.numbers.real.RealDouble tol = org.episteme.core.mathematics.numbers.real.RealDouble.of(1e-10);

        // Test CG
        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.RealDouble> xCG = 
            backend.conjugateGradient(A, b, x0, tol, 100);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, toArray(xCG), 1e-8);

        // Test BiCGSTAB
        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.RealDouble> xBi = 
            backend.bicgstab(A, b, x0, tol, 100);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, toArray(xBi), 1e-8);

        // Test GMRES
        org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.RealDouble> xGM = 
            backend.gmres(A, b, x0, tol, 10, 5);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, toArray(xGM), 1e-8);
        }
    }

    private double[] toArray(org.episteme.core.mathematics.linearalgebra.Vector<org.episteme.core.mathematics.numbers.real.RealDouble> v) {
        double[] res = new double[v.dimension()];
        for (int i = 0; i < v.dimension(); i++) res[i] = v.get(i).doubleValue();
        return res;
    }
}

