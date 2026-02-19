/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.backends;

import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.BackendDiscovery;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.cpu.CPUExecutionContext;
import org.jscience.core.technical.backend.simd.SIMDBackend;
import org.jscience.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import com.google.auto.service.AutoService;

/**
 * SIMD-accelerated CPU backend for JScience dense linear algebra.
 * <p>
 * This backend uses {@code SIMDRealDoubleMatrix} (backed by the JDK Vector API)
 * to exploit SIMD vector instructions for dense matrix and vector operations.
 * It is the default, always-available linear algebra backend.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, SIMDBackend.class})
public class CPUDenseLinearAlgebraBackend implements SIMDBackend {

    @Override
    public String getType() {
        return BackendDiscovery.TYPE_LINEAR_ALGEBRA;
    }

    @Override
    public String getId() {
        return "cpudense";
    }

    @Override
    public String getName() {
        return "JScience CPU Dense (SIMD)";
    }

    @Override
    public String getDescription() {
        return "CPU-based dense linear algebra backend accelerated via the JDK Vector API (SIMD).";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getPriority() {
        return 50; // Standard priority
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public Object createBackend() {
        return new CPUDenseLinearAlgebraProvider<>();
    }

    // ---- SIMDBackend ---

    @Override
    public String getSimdLevel() {
        // Actual level detected by JDK at runtime; GENERIC is the safe conservative default
        return "GENERIC";
    }

    @Override
    public int getPreferredVectorWidth() {
        // 32 bytes = 256 bits (AVX2 friendly); JVM selects optimal at JIT time
        return 32;
    }
}
