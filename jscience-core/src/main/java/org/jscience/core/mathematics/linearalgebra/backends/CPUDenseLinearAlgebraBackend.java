/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.linearalgebra.backends;

import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.BackendDiscovery;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.HardwareAccelerator;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.cpu.CPUExecutionContext;
import org.jscience.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import com.google.auto.service.AutoService;

/**
 * Backend for JScience CPU (Dense) Linear Algebra.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(Backend.class)
public class CPUDenseLinearAlgebraBackend implements ComputeBackend {

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
        return "JScience CPU (Dense)";
    }

    @Override
    public String getDescription() {
        return "Native Java CPU-based dense linear algebra provider.";
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
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public Object createBackend() {
        return new CPUDenseLinearAlgebraProvider<>();
    }
}
