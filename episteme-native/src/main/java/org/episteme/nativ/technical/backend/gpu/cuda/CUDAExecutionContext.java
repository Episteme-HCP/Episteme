/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.gpu.cuda;

import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.Operation;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.JCudaDriver;

/**
 * Execution context for CUDA operations.
 * Manages Driver API context state.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class CUDAExecutionContext implements ExecutionContext {

    private final CUcontext context;
    private final CUdevice device;

    /**
     * @deprecated Use {@link #CUDAExecutionContext(CUcontext, CUdevice)} for explicit context management.
     */
    @Deprecated
    public CUDAExecutionContext() {
        this.context = null;
        this.device = null;
    }

    public CUDAExecutionContext(CUcontext context, CUdevice device) {
        this.context = context;
        this.device = device;
    }

    public CUcontext getContext() {
        return context;
    }

    public CUdevice getDevice() {
        return device;
    }

    @Override
    public <T> T execute(Operation<T> operation) {
        if (context != null) {
            JCudaDriver.cuCtxPushCurrent(context);
        }
        try {
            return operation.compute(this);
        } finally {
            if (context != null) {
                JCudaDriver.cuCtxPopCurrent(new CUcontext());
            }
        }
    }

    @Override
    public void close() {
        // Resource management should be handled by the backend
    }
}
