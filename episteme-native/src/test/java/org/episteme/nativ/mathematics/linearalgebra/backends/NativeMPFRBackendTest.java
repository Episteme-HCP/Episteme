/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.structures.rings.Ring;

public class NativeMPFRBackendTest {

    @Test
    public void testBackendAvailability() {
        System.out.println("--- MPFR Loading Diagnostics ---");
        System.out.println("User Dir: " + System.getProperty("user.dir"));
        System.out.println("Java Library Path: " + System.getProperty("java.library.path"));
        
        java.io.File libsDir = new java.io.File("libs");
        System.out.println("Checking 'libs' directory: " + libsDir.getAbsolutePath());
        if (libsDir.exists()) {
            System.out.println("'libs' exists. Contents:");
            String[] files = libsDir.list();
            if (files != null) {
                for (String f : files) {
                    if (f.contains("mpfr")) {
                        java.io.File dll = new java.io.File(libsDir, f);
                        System.out.println("  - Found: " + f + " (Size: " + dll.length() + ", Executable: " + dll.canExecute() + ")");
                    }
                }
            }
        } else {
            System.err.println("'libs' directory NOT found at expected root!");
        }

        NativeMPFRDenseLinearAlgebraBackend<Real> provider = new NativeMPFRDenseLinearAlgebraBackend<>();
        System.out.println("MPFR Backend Availability: " + provider.isAvailable());
        if (!provider.isAvailable()) {
            System.err.println("Failure cause for 'mpfr': " + org.episteme.nativ.technical.backend.nativ.NativeFFMLoader.getFailureCause("mpfr"));
        }
        assertTrue(provider.isAvailable(), "MPFR Backend should be available after build");
    }

    @Test
    public void testSimpleMultiplication() {
        NativeMPFRDenseLinearAlgebraBackend<Real> provider = new NativeMPFRDenseLinearAlgebraBackend<>();
        if (!provider.isAvailable()) {
            System.err.println("Skipping test: MPFR not available");
            return;
        }

        Ring<Real> ring = Real.ZERO;
        Real[][] dataA = {
            {Real.of("1.2345678901234567890"), Real.of("2.3456789012345678901")},
            {Real.of("3.4567890123456789012"), Real.of("4.5678901234567890123")}
        };
        Real[][] dataB = {
            {Real.of("1.0"), Real.of("0.0")},
            {Real.of("0.0"), Real.of("1.0")}
        };

        Matrix<Real> a = DenseMatrix.of(dataA, ring);
        Matrix<Real> b = DenseMatrix.of(dataB, ring);

        Matrix<Real> c = provider.multiply(a, b);

        assertNotNull(c);
        assertEquals(a.rows(), c.rows());
        assertEquals(b.cols(), c.cols());

        // Check a few values
        assertEquals(a.get(0, 0).bigDecimalValue(), c.get(0, 0).bigDecimalValue());
        assertEquals(a.get(1, 1).bigDecimalValue(), c.get(1, 1).bigDecimalValue());
    }
}
