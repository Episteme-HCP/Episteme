/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.junit.jupiter.api.Test;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.numbers.real.RealFloat;
import org.episteme.core.mathematics.numbers.real.RealDouble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verification test for Native CUDA Dense Linear Algebra Backends (Float and Double).
 */
public class NativeCUDADenseLinearAlgebraBackendTest {

    private static final Logger logger = LoggerFactory.getLogger(NativeCUDADenseLinearAlgebraBackendTest.class);

    @Test
    public void testFloatBackendAvailability() {
        try (NativeCUDADenseLinearAlgebraFloatBackend backend = new NativeCUDADenseLinearAlgebraFloatBackend()) {
            boolean available = backend.isAvailable();
            logger.info("Native CUDA Dense Float Backend available: {}", available);
            if (available) {
                assertNotNull(backend.getName());
                assertTrue(backend.getPriority() > 0);
            }
        }
    }

    @Test
    public void testDoubleBackendAvailability() {
        try (NativeCUDADenseLinearAlgebraDoubleBackend backend = new NativeCUDADenseLinearAlgebraDoubleBackend()) {
            boolean available = backend.isAvailable();
            logger.info("Native CUDA Dense Double Backend available: {}", available);
            if (available) {
                assertNotNull(backend.getName());
                assertTrue(backend.getPriority() > 0);
            }
        }
    }

    @Test
    public void testMultiplyFloat() {
        try (NativeCUDADenseLinearAlgebraFloatBackend backend = new NativeCUDADenseLinearAlgebraFloatBackend()) {
            if (!backend.isAvailable()) {
                logger.warn("CUDA Float Backend not available, skipping test.");
                return;
            }

            RealFloat[][] dataA = {{RealFloat.create(1.0f), RealFloat.create(2.0f)}, {RealFloat.create(3.0f), RealFloat.create(4.0f)}};
            RealFloat[][] dataB = {{RealFloat.create(5.0f), RealFloat.create(6.0f)}, {RealFloat.create(7.0f), RealFloat.create(8.0f)}};
            Matrix<RealFloat> a = Matrix.of(dataA, org.episteme.core.mathematics.sets.Reals.getInstance());
            Matrix<RealFloat> b = Matrix.of(dataB, org.episteme.core.mathematics.sets.Reals.getInstance());

            Matrix<RealFloat> result = backend.multiply(a, b);
            
            assertNotNull(result);
            assertEquals(19.0f, result.get(0, 0).floatValue(), 1e-6f);
            assertEquals(22.0f, result.get(0, 1).floatValue(), 1e-6f);
            assertEquals(43.0f, result.get(1, 0).floatValue(), 1e-6f);
            assertEquals(50.0f, result.get(1, 1).floatValue(), 1e-6f);
        }
    }

    @Test
    public void testMultiplyDouble() {
        try (NativeCUDADenseLinearAlgebraDoubleBackend backend = new NativeCUDADenseLinearAlgebraDoubleBackend()) {
            if (!backend.isAvailable()) {
                logger.warn("CUDA Double Backend not available, skipping test.");
                return;
            }

            RealDouble[][] dataA = {{RealDouble.of(1.0), RealDouble.of(2.0)}, {RealDouble.of(3.0), RealDouble.of(4.0)}};
            RealDouble[][] dataB = {{RealDouble.of(5.0), RealDouble.of(6.0)}, {RealDouble.of(7.0), RealDouble.of(8.0)}};
            Matrix<RealDouble> a = Matrix.of(dataA, org.episteme.core.mathematics.sets.Reals.getInstance());
            Matrix<RealDouble> b = Matrix.of(dataB, org.episteme.core.mathematics.sets.Reals.getInstance());

            Matrix<RealDouble> result = backend.multiply(a, b);
            
            assertNotNull(result);
            assertEquals(19.0, result.get(0, 0).doubleValue(), 1e-9);
            assertEquals(22.0, result.get(0, 1).doubleValue(), 1e-9);
            assertEquals(43.0, result.get(1, 0).doubleValue(), 1e-9);
            assertEquals(50.0, result.get(1, 1).doubleValue(), 1e-9);
        }
    }
}
