/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.technical.backend.gpu.cuda;

import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.Operation;

/**
 * CUDA Execution Context.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CUDAExecutionContext implements ExecutionContext {

    public CUDAExecutionContext() {
        // Initialize CUDA context if needed, or rely on JCuda's static context
        // management
    }

    @Override
    public <T> T execute(Operation<T> operation) {
        return operation.compute(this);
    }

    @Override
    public void close() {
        // Cleanup resources
    }
}
