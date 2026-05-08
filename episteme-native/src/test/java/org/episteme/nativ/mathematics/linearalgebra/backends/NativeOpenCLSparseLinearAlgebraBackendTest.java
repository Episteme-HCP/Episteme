/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.junit.jupiter.api.Test;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealFloat;
import org.episteme.core.mathematics.numbers.real.RealDouble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verification test for Native OpenCL Sparse Linear Algebra Backends.
 */
public class NativeOpenCLSparseLinearAlgebraBackendTest {

    private static final Logger logger = LoggerFactory.getLogger(NativeOpenCLSparseLinearAlgebraBackendTest.class);

    @Test
    public void testFloatBackendAvailability() {
        try (NativeOpenCLSparseLinearAlgebraFloatBackend backend = new NativeOpenCLSparseLinearAlgebraFloatBackend()) {
            boolean available = backend.isAvailable();
            logger.info("Native OpenCL Sparse Float Backend available: {}", available);
            if (available) {
                assertNotNull(backend.getName());
                assertTrue(backend.getPriority() > 0);
            }
        }
    }

    @Test
    public void testDoubleBackendAvailability() {
        try (NativeOpenCLSparseLinearAlgebraDoubleBackend backend = new NativeOpenCLSparseLinearAlgebraDoubleBackend()) {
            boolean available = backend.isAvailable();
            logger.info("Native OpenCL Sparse Double Backend available: {}", available);
            if (available) {
                assertNotNull(backend.getName());
                assertTrue(backend.getPriority() > 0);
            }
        }
    }

    @Test
    public void testSpmvFloat() {
        try (NativeOpenCLSparseLinearAlgebraFloatBackend backend = new NativeOpenCLSparseLinearAlgebraFloatBackend()) {
            if (!backend.isAvailable()) {
                logger.warn("OpenCL Sparse Float Backend not available, skipping test.");
                return;
            }

            RealFloat[][] dataA = {{RealFloat.create(1.0f), RealFloat.create(0.0f)}, {RealFloat.create(0.0f), RealFloat.create(2.0f)}};
            Matrix<RealFloat> a = Matrix.of(dataA, RealFloat.RING);
            Vector<RealFloat> x = Vector.of(new RealFloat[]{RealFloat.create(10.0f), RealFloat.create(20.0f)}, RealFloat.RING);

            Vector<RealFloat> y = backend.multiply(a, x);
            
            assertNotNull(y);
            assertEquals(10.0f, y.get(0).floatValue(), 1e-6f);
            assertEquals(40.0f, y.get(1).floatValue(), 1e-6f);
        }
    }

    @Test
    public void testSpmvDouble() {
        try (NativeOpenCLSparseLinearAlgebraDoubleBackend backend = new NativeOpenCLSparseLinearAlgebraDoubleBackend()) {
            if (!backend.isAvailable()) {
                logger.warn("OpenCL Sparse Double Backend not available, skipping test.");
                return;
            }

            RealDouble[][] dataA = {{RealDouble.of(1.0), RealDouble.of(0.0)}, {RealDouble.of(0.0), RealDouble.of(2.0)}};
            Matrix<RealDouble> a = Matrix.of(dataA, RealDouble.RING);
            Vector<RealDouble> x = Vector.of(new RealDouble[]{RealDouble.of(10.0), RealDouble.of(20.0)}, RealDouble.RING);

            Vector<RealDouble> y = backend.multiply(a, x);
            
            assertNotNull(y);
            assertEquals(10.0, y.get(0).doubleValue(), 1e-9);
            assertEquals(40.0, y.get(1).doubleValue(), 1e-9);
        }
    }
}
