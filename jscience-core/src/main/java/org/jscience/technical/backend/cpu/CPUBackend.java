/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.technical.backend.cpu;

import org.jscience.technical.backend.ComputeBackend;
import org.jscience.technical.backend.ExecutionContext;

/**
 * CPU-based compute backend (default fallback).
 * <p>
 * This backend uses pure Java implementations and is always available.
 * It supports parallel operations via Java parallel streams.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CPUBackend implements ComputeBackend {

    @Override
    public String getId() {
        return "cpu";
    }

    @Override
    public String getName() {
        return "CPU";
    }

    @Override
    public String getDescription() {
        return "Standard CPU processing using multi-core Java implementations.";
    }

    @Override
    public boolean isAvailable() {
        return true; // CPU backend is always available
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public boolean supportsParallelOps() {
        return Runtime.getRuntime().availableProcessors() > 1;
    }

    @Override
    public boolean supportsFloatingPoint() {
        return true;
    }

    @Override
    public boolean supportsComplexNumbers() {
        return true;
    }

    @Override
    public int getPriority() {
        return 0; // Lowest priority (fallback)
    }
}
