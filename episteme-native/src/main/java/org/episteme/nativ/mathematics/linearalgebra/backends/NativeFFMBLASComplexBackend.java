/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.nativ.mathematics.linearalgebra.backends;

import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.core.mathematics.sets.Complexes;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Native FFM BLAS/LAPACK provider specialized for Complex numbers.
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, CPUBackend.class, AlgorithmProvider.class})
public class NativeFFMBLASComplexBackend extends AbstractNativeFFMBLASBackend<Complex> {

    @Override
    public String getName() {
        return "Native FFM-BLAS (Complex)";
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        return ring instanceof Complexes || (ring != null && ring.zero() instanceof Complex);
    }

    @Override
    public String getId() {
        return "ffm-blas-complex";
    }
}
