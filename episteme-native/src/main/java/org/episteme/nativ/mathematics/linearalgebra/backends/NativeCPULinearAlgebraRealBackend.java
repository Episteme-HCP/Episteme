/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import com.google.auto.service.AutoService;

/**
 * Concrete Native CPU-BLAS provider for Real numbers.
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, CPUBackend.class, AlgorithmProvider.class})
public class NativeCPULinearAlgebraRealBackend extends AbstractNativeCPULinearAlgebraBackend<Real> {

    public NativeCPULinearAlgebraRealBackend() {
        super(Reals.getInstance());
    }

    @Override
    public String getName() {
        return "Native CPU-BLAS (Real)";
    }

    @Override
    public String getId() {
        return "cpu-real";
    }
}
