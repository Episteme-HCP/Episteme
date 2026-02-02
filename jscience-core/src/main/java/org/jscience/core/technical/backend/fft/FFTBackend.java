/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.fft;

import org.jscience.core.technical.backend.BackendProvider;
import java.nio.DoubleBuffer;

/**
 * Interface for high-performance FFT operations allowing native acceleration.
 * Classified as Infrastructure (Backend).
 */
public interface FFTBackend extends BackendProvider {
    
    @Override
    default String getType() {
        return "fft";
    }

    void forward(int n, DoubleBuffer input, DoubleBuffer output);
    void backward(int n, DoubleBuffer input, DoubleBuffer output);
}
