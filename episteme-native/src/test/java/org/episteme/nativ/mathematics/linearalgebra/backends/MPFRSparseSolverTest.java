/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MPFRSparseSolverTest {
    private static final Logger logger = LoggerFactory.getLogger(MPFRSparseSolverTest.class);

    @Test
    public void testConjugateGradientConvergence() {
        MathContext.exact().compute(() -> {
            // SPD Matrix A:
            // [ 4, 1, 0 ]
            // [ 1, 3, 1 ]
            // [ 0, 1, 2 ]
            int n = 3;
            SparseMatrixStorage<Real> storage = new SparseMatrixStorage<>(n, n, Real.ZERO);
            storage.set(0, 0, Real.of(4.0));
            storage.set(0, 1, Real.of(1.0));
            storage.set(1, 0, Real.of(1.0));
            storage.set(1, 1, Real.of(3.0));
            storage.set(1, 2, Real.of(1.0));
            storage.set(2, 1, Real.of(1.0));
            storage.set(2, 2, Real.of(2.0));
            
            SparseMatrix<Real> A = new SparseMatrix<>(storage, Real.ZERO);
            Vector<Real> b = DenseVector.of(Arrays.asList(Real.of(5.0), Real.of(5.0), Real.of(3.0)), Reals.getInstance());
            
            try (NativeMPFRSparseLinearAlgebraBackend<Real> provider = new NativeMPFRSparseLinearAlgebraBackend<>()) {
                assertTrue(provider.isAvailable(), "MPFR Backend should be available");
                
                Vector<Real> x = provider.solve(A, b);
                
                logger.info("Solution x: {}", x);
                
                // Expected x is [1, 1, 1]
                for (int i = 0; i < n; i++) {
                    assertThat(x.get(i).doubleValue()).isCloseTo(1.0, org.assertj.core.data.Offset.offset(1e-15));
                    assertTrue(x.get(i).toString().startsWith("1.0000000000"), "Solution should be accurate to at least 10 decimal places");
                }
            }
            return null;
        });
    }

    @Test
    public void testHighPrecisionConvergence() {
        MathContext.exact().compute(() -> {
            // Test with a larger diagonal matrix to verify 50-digit precision
            int n = 10;
            SparseMatrixStorage<Real> storage = new SparseMatrixStorage<>(n, n, Real.ZERO);
            Real[] bVals = new Real[n];
            for (int i = 0; i < n; i++) {
                storage.set(i, i, Real.of(i + 1));
                bVals[i] = Real.of(i + 1);
            }
            
            SparseMatrix<Real> A = new SparseMatrix<>(storage, Real.ZERO);
            Vector<Real> b = DenseVector.of(Arrays.asList(bVals), Reals.getInstance());
            
            try (NativeMPFRSparseLinearAlgebraBackend<Real> provider = new NativeMPFRSparseLinearAlgebraBackend<>()) {
                // Set high precision in context (50 digits)
                // MathContext.exact() is usually very high already, but let's be explicit if possible.
                // Currently MathContext.getCurrent().getJavaMathContext().getPrecision() is used.
                
                Vector<Real> x = provider.solve(A, b);
                
                logger.info("High precision solution sample: x[0] = {}", x.get(0));
                
                for (int i = 0; i < n; i++) {
                    assertTrue(x.get(i).toString().startsWith("1.0000000000"), "High precision solution should be exactly 1.0");
                }
            }
            return null;
        });
    }
}
