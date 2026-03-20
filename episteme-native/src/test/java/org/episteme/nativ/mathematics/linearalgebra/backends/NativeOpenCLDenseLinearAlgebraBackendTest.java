/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.junit.jupiter.api.Test;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verification test for NativeOpenCLDenseLinearAlgebraBackend.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class NativeOpenCLDenseLinearAlgebraBackendTest {

    private static final Logger logger = LoggerFactory.getLogger(NativeOpenCLDenseLinearAlgebraBackendTest.class);

    @Test
    public void testBackendAvailability() {
        NativeOpenCLDenseLinearAlgebraBackend backend = new NativeOpenCLDenseLinearAlgebraBackend();
        boolean available = backend.isAvailable();
        logger.info("Native OpenCL Dense Backend available: {}", available);
        
        if (available) {
            assertNotNull(backend.getName());
            assertTrue(backend.getPriority() > 0);
        }
    }

    @Test
    public void testSimpleMultiplyIfAvailable() {
        NativeOpenCLDenseLinearAlgebraBackend backend = new NativeOpenCLDenseLinearAlgebraBackend();
        if (!backend.isAvailable()) {
            logger.warn("OpenCL Backend not available, skipping functional test.");
            return;
        }

        Reals reals = Reals.getInstance();
        Real[][] dataA = {{Real.of(1.0), Real.of(2.0)}, {Real.of(3.0), Real.of(4.0)}};
        Real[][] dataB = {{Real.of(5.0), Real.of(6.0)}, {Real.of(7.0), Real.of(8.0)}};
        Matrix<Real> a = Matrix.of(dataA, reals);
        Matrix<Real> b = Matrix.of(dataB, reals);

        Matrix<Real> result = backend.multiply(a, b);
        
        assertNotNull(result);
        assertEquals(19.0, result.get(0, 0).doubleValue(), 1e-9);
        assertEquals(22.0, result.get(0, 1).doubleValue(), 1e-9);
        assertEquals(43.0, result.get(1, 0).doubleValue(), 1e-9);
        assertEquals(50.0, result.get(1, 1).doubleValue(), 1e-9);
    }
}
