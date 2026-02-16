/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.simd;

import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.HardwareAccelerator;

/**
 * Interface for SIMD-accelerated compute backends.
 * <p>
 * Implementations use the JDK Vector API (jdk.incubator.vector) or
 * native SIMD intrinsics via FFM/Panama to accelerate numerical operations.
 * </p>
 * <p>
 * Example implementations:
 * <ul>
 * <li>{@code SIMDRealLinearAlgebraProvider} — vectorized linear algebra</li>
 * <li>Future: NEON (ARM), SVE, AVX-512 specialized backends</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface SIMDBackend extends ComputeBackend {

    @Override
    default String getType() {
        return "simd";
    }

    @Override
    default HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU; // SIMD runs on CPU
    }

    /**
     * Returns the SIMD instruction set level available.
     * <p>
     * Examples: "AVX2", "AVX-512", "NEON", "SVE", "GENERIC"
     * </p>
     */
    String getSimdLevel();

    /**
     * Returns the preferred vector width in bytes for double operations.
     * <p>
     * Typical values: 32 (AVX2), 64 (AVX-512), 16 (NEON)
     * </p>
     */
    int getPreferredVectorWidth();
}
