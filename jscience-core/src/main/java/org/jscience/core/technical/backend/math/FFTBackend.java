/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.math;

import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.ExecutionContext;
import java.nio.DoubleBuffer;

/**
 * Interface for high-performance FFT operations allowing native acceleration.
 */
public interface FFTBackend extends ComputeBackend {
    
    void forward(int n, DoubleBuffer input, DoubleBuffer output);
    void backward(int n, DoubleBuffer input, DoubleBuffer output);

    @Override
    default ExecutionContext createContext() {
        return null;
    }
}

